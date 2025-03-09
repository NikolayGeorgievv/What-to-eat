package whattoeat.app.service.service;

import org.springframework.stereotype.Service;
import whattoeat.app.model.FavoriteRecipe;

@Service
public interface FavoriteRecipeService {
    void saveAndFlushFavoriteRecipe(FavoriteRecipe favoriteRecipe);
}
