package whattoeat.app.service.service;

import org.springframework.security.core.Authentication;
import whattoeat.app.dto.CreateCustomRecipeDTO;
import whattoeat.app.dto.RegisterUserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {

    Authentication login(String email);

    void registerUser(RegisterUserDTO registerUserDTO);


    void addRecipeToFavorites(Long recipeId, String userEmail);

    void removeRecipeFromFavorites(Long recipeId, String userEmail);

    Map<Long, Boolean> getFavorites(String email);

    List<String> getFavoriteRecipes(String userEmail);

    void addCustomRecipe(String userEmail, CreateCustomRecipeDTO recipeDTO);

    void addCustomRecipeToUser(String title);

    List<String> getUsersCustomRecipes(String userEmail);

    List<String> getUserNotifications(String userEmail);

    void sendApprovedNotificationToUser(String title);
}
