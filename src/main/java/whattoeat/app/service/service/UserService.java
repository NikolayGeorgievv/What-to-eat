package whattoeat.app.service.service;

import org.springframework.security.core.Authentication;
import whattoeat.app.dto.RegisterUserDTO;

public interface UserService {

    Authentication login(String email);

    void registerUser(RegisterUserDTO registerUserDTO);


    void addRecipeToFavorites(Long recipeId, String userEmail);

    void removeRecipeFromFavorites(Long recipeId, String userEmail);

    boolean isFavorite(Long recipeId, String userEmail);
}
