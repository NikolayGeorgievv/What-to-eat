package whattoeat.app.service.service;

import org.springframework.security.core.Authentication;
import whattoeat.app.dto.RegisterUserDTO;
import whattoeat.app.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Authentication login(String email);

    void registerUser(RegisterUserDTO registerUserDTO);

//    void removeRecipeFromFavorites(Long recipeId, String userEmail);

    List<String> getFavoriteRecipes(String userEmail);

    List<String> getUserNotifications(String userEmail);

//    void sendApprovedNotificationToUser(String title);

    Optional<User> findByEmail(String userEmail);
}
