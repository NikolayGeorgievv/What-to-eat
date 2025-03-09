package whattoeat.app.service.impl;

import org.springframework.stereotype.Service;
import whattoeat.app.model.FavoriteRecipe;
import whattoeat.app.repository.FavoriteRecipeRepository;
import whattoeat.app.service.service.FavoriteRecipeService;

@Service
public class FavoriteRecipeServiceImpl implements FavoriteRecipeService {

    private final FavoriteRecipeRepository favoriteRecipeRepository;

    public FavoriteRecipeServiceImpl(FavoriteRecipeRepository favoriteRecipeRepository) {
        this.favoriteRecipeRepository = favoriteRecipeRepository;
    }

    @Override
    public void saveAndFlushFavoriteRecipe(FavoriteRecipe favoriteRecipe) {
        favoriteRecipeRepository.saveAndFlush(favoriteRecipe);
    }
}
