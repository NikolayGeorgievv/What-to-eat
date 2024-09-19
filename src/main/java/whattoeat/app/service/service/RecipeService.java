package whattoeat.app.service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import whattoeat.app.dto.CreateCustomRecipeDTO;
import whattoeat.app.dto.RecipeDTO;
import whattoeat.app.model.CustomRecipeFromUsers;
import whattoeat.app.model.Recipe;

import java.util.List;

public interface RecipeService {

    Page<RecipeDTO> searchByProductName(String productName, PageRequest pageRequest);

    Page<RecipeDTO> searchByRecipeName(String recipeName, PageRequest pageRequest);

    RecipeDTO findByTitle(String title);

    Recipe findById(Long recipeId);

    Recipe findByName(String recipeName);

    void addCustomRecipe(CustomRecipeFromUsers customRecipe);

    List<CreateCustomRecipeDTO> getAllCustomRecipes();

}
