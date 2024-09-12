package whattoeat.app.service.service;

import org.springframework.security.core.Authentication;
import whattoeat.app.dto.RegisterUserDTO;

import java.util.Map;

public interface UserService {

    Authentication login(String email);

    void registerUser(RegisterUserDTO registerUserDTO);


    void addRecipeToFavorites(Long recipeId, String userEmail);

    void removeRecipeFromFavorites(Long recipeId, String userEmail);

    Map<Long, Boolean> getFavorites(String email);
}
