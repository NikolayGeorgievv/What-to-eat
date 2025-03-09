package whattoeat.app.service.impl;

import org.springframework.stereotype.Service;
import whattoeat.app.model.Recipe;
import whattoeat.app.model.User;
import whattoeat.app.repository.RecipeRepository;
import whattoeat.app.service.rest.RecipeGenerator;
import whattoeat.app.service.service.RecipeService;
import whattoeat.app.service.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeGenerator recipeGenerator;
    private final UserService userService;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeGenerator recipeGenerator, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.recipeGenerator = recipeGenerator;
        this.userService = userService;
    }

    @Override
    public String generateRecipe(String searchType, String ingredients, String recipeName) throws Exception {
        return recipeGenerator.getRecipe(searchType, ingredients, recipeName);
    }

    @Override
    public String extractTitleFromGeneratedRecipe(String generatedRecipe) {
        return generatedRecipe.split(" \\.\\.\\.")[0].replace("## ", "").trim();
    }


    @Override
    public void addRecipeToFavorites(String recipeTitle, String userEmail, String fullRecipe) {
        Optional<User> optionalUser = userService.findByEmail(userEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.getFavoriteRecipes().add(recipeTitle);
            Recipe recipe = new Recipe();
            recipe.setName(recipeTitle);
            recipe.setPreparationDescription(fullRecipe);
            recipeRepository.saveAndFlush(recipe);
        } else {
            throw new RuntimeException("User not found!");
        }
    }

//    @Override
//    public RecipeDTO findByTitle(String title) {
//        Recipe recipeByName = this.recipeRepository.findByNameIgnoreCase(title);
//        return new RecipeDTO(
//                recipeByName.getId(),
//                recipeByName.getName(),
//                recipeByName.getPreparationDescription(),
//                recipeByName.getIngredients(),
//                recipeByName.getLikedCounter()
//        );
//    }

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
    public Map<Long, Boolean> getFavorites(String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Map<Long, Boolean> userFavorites = new HashMap<>();
        user.getFavoriteRecipes().forEach(recipeName -> {
            Recipe recipe = findByName(recipeName);
            userFavorites.put(recipe.getId(), true);
        });
        return userFavorites;
    }


    public List<String> parseIngredients(String input) {
        String[] ingredientsArray = input.split("[,\\s]+");


        return Arrays.stream(ingredientsArray)
                .filter(ingredient -> !ingredient.isEmpty())
                .collect(Collectors.toList());
    }
}
