package whattoeat.app.utils;

import whattoeat.app.dto.CreateCustomRecipeDTO;
import whattoeat.app.dto.RecipeDTO;
import whattoeat.app.model.CustomRecipeFromUsers;
import whattoeat.app.model.Ingredient;
import whattoeat.app.model.Recipe;
import whattoeat.app.model.RecipeIngredient;
import whattoeat.app.repository.IngredientRepository;
import whattoeat.app.repository.RecipeIngredientsRepository;
import whattoeat.app.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

public class RecipesUtils {




    public static CreateCustomRecipeDTO mapCustomRecipeToDTO(CustomRecipeFromUsers customRecipeByName) {
        CreateCustomRecipeDTO createCustomRecipeDTO = new CreateCustomRecipeDTO(
                customRecipeByName.getRecipeName(),
                new ArrayList<>(),
                new ArrayList<>(),
                customRecipeByName.getDescription()
        );
        String[] rowsArr = customRecipeByName.getProductNameAndQuantity().split("\\n");
        for (int i = 0; i < rowsArr.length; i++) {
            String productName = rowsArr[i].split(" - ")[0];
            String quantity = rowsArr[i].split(" -")[1];
            createCustomRecipeDTO.getProductName().add(productName);
            createCustomRecipeDTO.getQuantity().add(quantity);
        }
        return createCustomRecipeDTO;
    }



    public static List<RecipeDTO> mapRecipes(List<Recipe> allByIngredients) {
        List<RecipeDTO> recipeDTOs = new ArrayList<>();
        allByIngredients.forEach(recipe -> {
            RecipeDTO recipeDTO = new RecipeDTO(recipe.getId(), recipe.getName(), recipe.getPreparationDescription(), recipe.getIngredients());
            recipeDTOs.add(recipeDTO);
        });
        return recipeDTOs;
    }

    public static void mapCustomRecipeToRecipeEntityAndFlushIt(
            CustomRecipeFromUsers customRecipeByName,
            RecipeRepository recipeRepository,
            IngredientRepository ingredientRepository,
            RecipeIngredientsRepository recipeIngredientsRepository) {

        Recipe recipe = new Recipe();
        recipe.setName(customRecipeByName.getRecipeName());
        recipe.setPreparationDescription(customRecipeByName.getDescription());
        recipeRepository.saveAndFlush(recipe);
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        String[] rowsArr = customRecipeByName.getProductNameAndQuantity().split("\\n");
        for (int i = 0; i < rowsArr.length; i++) {
            String productName = rowsArr[i].split(" - ")[0];
            String quantity = rowsArr[i].split(" -")[1];
            RecipeIngredient recipeIngredient = new RecipeIngredient();

            if (ingredientRepository.findByName(productName).isPresent()) {
                Ingredient ingredient = ingredientRepository.findByName(productName).get();
                recipeIngredient.setIngredient(ingredient);
                recipeIngredient.setRecipe(recipe);
                if (!quantity.isEmpty()) {
                    recipeIngredient.setQuantity(quantity);
                }
            } else {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(productName);
                ingredientRepository.saveAndFlush(ingredient);
                recipeIngredient.setIngredient(ingredient);
                recipeIngredient.setRecipe(recipe);
                if (!quantity.isEmpty()) {
                    recipeIngredient.setQuantity(quantity);
                }
            }
            recipeIngredients.add(recipeIngredient);
            recipeIngredientsRepository.saveAndFlush(recipeIngredient);

        }
        Recipe recipe1 = recipeRepository.findByName(customRecipeByName.getRecipeName()).get();
        recipe1.setIngredients(recipeIngredients);
        recipeRepository.saveAndFlush(recipe1);
    }

}
