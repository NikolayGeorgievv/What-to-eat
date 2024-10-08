package whattoeat.app.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import whattoeat.app.dto.CreateCustomRecipeDTO;
import whattoeat.app.dto.RecipeDTO;
import whattoeat.app.model.CustomRecipeFromUsers;
import whattoeat.app.model.Ingredient;
import whattoeat.app.model.Recipe;
import whattoeat.app.model.RecipeIngredient;
import whattoeat.app.repository.CustomRecipeFromUsersRepository;
import whattoeat.app.repository.IngredientRepository;
import whattoeat.app.repository.RecipeIngredientsRepository;
import whattoeat.app.repository.RecipeRepository;
import whattoeat.app.service.impl.seed.CSVService;
import whattoeat.app.service.service.RecipeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static whattoeat.app.constants.Constants.INVALID_RECIPE_NAME;
import static whattoeat.app.utils.RecipesUtils.*;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientsRepository recipeIngredientsRepository;
    private final CustomRecipeFromUsersRepository customRecipeFromUsersRepository;
    private final CSVService csvService;

    public RecipeServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, RecipeIngredientsRepository recipeIngredientsRepository, CustomRecipeFromUsersRepository customRecipeFromUsersRepository, CSVService csvService) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientsRepository = recipeIngredientsRepository;
        this.customRecipeFromUsersRepository = customRecipeFromUsersRepository;
        this.csvService = csvService;
    }


    @Override
    public Page<RecipeDTO> searchByProductName(String productName, PageRequest pageRequest) {
        List<Ingredient> ingredientList = new ArrayList<>();
        List<String> productsNames = parseIngredients(productName);
        productsNames.forEach(product -> {
            Optional<Ingredient> ingredient = ingredientRepository.findByName(product);
            ingredient.ifPresent(ingredientList::add);
        });

        List<String> ingredientNames = ingredientList.stream()
                .map(Ingredient::getName)
                .toList();


        List<Recipe> allByIngredients = recipeRepository.findAllByIngredients(ingredientNames);

        return getRecipeDTOS(pageRequest, allByIngredients);

    }



    @Override
    public Page<RecipeDTO> searchByRecipeName(String recipeName, PageRequest pageRequest) {

        List<Recipe> allRecipesByName = recipeRepository.findAllByNameContainingIgnoreCase(recipeName);
        return getRecipeDTOS(pageRequest, allRecipesByName);
    }

    @Override
    public RecipeDTO findByTitle(String title) {
        Recipe recipeByName = this.recipeRepository.findByNameIgnoreCase(title);
        return new RecipeDTO(
                recipeByName.getId(),
                recipeByName.getName(),
                recipeByName.getPreparationDescription(),
                recipeByName.getIngredients(),
                recipeByName.getLikedCounter()
        );
    }

    @Override
    public Recipe findById(Long recipeId) {
        if (recipeRepository.findById(recipeId).isEmpty()) {
            throw new IllegalArgumentException("Recipe with this id does not exist.");
        }
        return recipeRepository.findById(recipeId).get();
    }

    @Override
    public Recipe findByName(String recipeName) {
        Optional<Recipe> byName = recipeRepository.findByName(recipeName);
        if (byName.isEmpty()) {
            throw new IllegalArgumentException("Recipe with this name does not exist.");
        }
        return byName.get();
    }

    @Override
    public void addCustomRecipe(CustomRecipeFromUsers customRecipe) {
        if (recipeRepository.findByName(customRecipe.getRecipeName()).isPresent()) {
            throw new IllegalArgumentException(INVALID_RECIPE_NAME);
        }

        customRecipeFromUsersRepository.saveAndFlush(customRecipe);
    }

    @Override
    public List<CreateCustomRecipeDTO> getAllCustomRecipes() {
        List<CustomRecipeFromUsers> allCustomRecipes = customRecipeFromUsersRepository.findAll();
        List<CreateCustomRecipeDTO> customRecipeDTOs = new ArrayList<>();


        allCustomRecipes.forEach(customRecipe -> {
            CreateCustomRecipeDTO createCustomRecipeDTO = new CreateCustomRecipeDTO(
                    customRecipe.getRecipeName(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    customRecipe.getDescription()
            );
            String[] rowsArr = customRecipe.getProductNameAndQuantity().split("\\n");
            for (int i = 0; i < rowsArr.length; i++) {
                String productName = rowsArr[i].split(" - ")[0];
                String quantity = rowsArr[i].split(" -")[1];
                createCustomRecipeDTO.getProductName().add(productName);
                createCustomRecipeDTO.getQuantity().add(quantity);
            }
            customRecipeDTOs.add(createCustomRecipeDTO);
        });
        return customRecipeDTOs;
    }

    @Override
    public CreateCustomRecipeDTO findCustomRecipeByTitle(String title) {
        CustomRecipeFromUsers customRecipeByName = customRecipeFromUsersRepository.findCustomRecipeFromUsersByRecipeName(title);
        return mapCustomRecipeToDTO(customRecipeByName);
    }

    @Override
    public void approveCustomRecipe(String title) {
        CustomRecipeFromUsers customRecipeByName = customRecipeFromUsersRepository.findCustomRecipeFromUsersByRecipeName(title);
        mapCustomRecipeToRecipeEntityAndFlushIt(customRecipeByName,recipeRepository, ingredientRepository, recipeIngredientsRepository);
        csvService.writeCustomRecipe(customRecipeByName);

    }

    @Override
    public void rejectCustomRecipe(String title) {
        CustomRecipeFromUsers customRecipeByName = customRecipeFromUsersRepository.findCustomRecipeFromUsersByRecipeName(title);
        customRecipeFromUsersRepository.delete(customRecipeByName);
    }



    private Page<RecipeDTO> getRecipeDTOS(PageRequest pageRequest, List<Recipe> allRecipesByName) {
        List<RecipeDTO> recipeDTOs = mapRecipes(allRecipesByName);

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), recipeDTOs.size());
        return new PageImpl<>(recipeDTOs.subList(start, end), pageRequest, recipeDTOs.size());
    }

    public List<String> parseIngredients(String input) {
        String[] ingredientsArray = input.split("[,\\s]+");


        return Arrays.stream(ingredientsArray)
                .filter(ingredient -> !ingredient.isEmpty())
                .collect(Collectors.toList());
    }
}
