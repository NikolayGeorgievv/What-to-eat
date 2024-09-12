package whattoeat.app.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import whattoeat.app.dto.RecipeDTO;
import whattoeat.app.model.Ingredient;
import whattoeat.app.model.Recipe;
import whattoeat.app.repository.IngredientRepository;
import whattoeat.app.repository.RecipeIngredientsRepository;
import whattoeat.app.repository.RecipeRepository;
import whattoeat.app.service.service.RecipeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientsRepository recipeIngredientsRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, RecipeIngredientsRepository recipeIngredientsRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientsRepository = recipeIngredientsRepository;
    }

    @Override
    public Page<RecipeDTO> getMatches(PageRequest of) {
        recipeRepository.findAll(of);


        return null;
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

    private List<RecipeDTO> mapRecipes(List<Recipe> allByIngredients) {
        List<RecipeDTO> recipeDTOs = new ArrayList<>();
        allByIngredients.forEach(recipe -> {
            RecipeDTO recipeDTO = new RecipeDTO(recipe.getId() ,recipe.getName(), recipe.getPreparationDescription(), recipe.getIngredients());
            recipeDTOs.add(recipeDTO);
        });
        return recipeDTOs;
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
                recipeByName.getIngredients()
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
