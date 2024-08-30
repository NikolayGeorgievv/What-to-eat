package whattoeat.app.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import whattoeat.app.dto.RecipeDTO;
import whattoeat.app.model.Ingredient;
import whattoeat.app.model.Recipe;
import whattoeat.app.model.RecipeIngredient;
import whattoeat.app.repository.IngredientRepository;
import whattoeat.app.repository.RecipeIngredientsRepository;
import whattoeat.app.repository.RecipeRepository;
import whattoeat.app.service.RecipeService;

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
    public List<RecipeDTO> searchByProductName(String productName) {
        List<Ingredient> ingredientList = new ArrayList<>();
        List<String> productsNames = parseIngredients(productName);
        productsNames.forEach(product ->{
            Optional<Ingredient> ingredient = ingredientRepository.findByName(product);
            ingredient.ifPresent(ingredientList::add);
        });

        List<String> ingredientNames = ingredientList.stream()
                .map(Ingredient::getName)
                .toList();


        List<Recipe> allByIngredients = recipeRepository.findAllByIngredients(ingredientNames);



        return List.of();
    }

    @Override
    public List<RecipeDTO> searchByRecipeName(String recipeName) {
        return List.of();
    }

    public List<String> parseIngredients(String input) {
        String[] ingredientsArray = input.split("[,\\s]+");


        return Arrays.stream(ingredientsArray)
                .filter(ingredient -> !ingredient.isEmpty())
                .collect(Collectors.toList());
    }
}
