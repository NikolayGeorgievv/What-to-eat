package whattoeat.app.service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import whattoeat.app.dto.RecipeDTO;

public interface RecipeService {
    Page<RecipeDTO> getMatches(PageRequest of);

    Page<RecipeDTO> searchByProductName(String productName, PageRequest pageRequest);

    Page<RecipeDTO> searchByRecipeName(String recipeName, PageRequest pageRequest);

    RecipeDTO findByTitle(String title);
}
