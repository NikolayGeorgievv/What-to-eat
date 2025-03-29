package whattoeat.app.service.service;

import org.springframework.stereotype.Service;
import whattoeat.app.dto.RecipeDTO;
import whattoeat.app.model.Recipe;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface RecipeService {


//    RecipeDTO findByTitle(String title);

    Recipe findById(Long recipeId);

    Recipe findByName(String recipeName);

    String generateRecipe(String searchType, String ingredients, String recipeName) throws Exception;

    String extractTitleFromGeneratedRecipe(String generatedRecipe);

    void addRecipeToFavorites(String recipeTitle, String userEmail, String fullRecipe);

    Map<Long, Boolean> getFavorites(String email);


    RecipeDTO findRecipeDTOById(Long id);

    void removeFavoriteRecipe(Long recipeId, String userEmail);

    boolean isValidRecipe(String parsedGeneratedRecipe);

    String generateAnotherRecipe(List<String> ingredients, List<String> previousRecipes) throws IOException, InterruptedException;
}
