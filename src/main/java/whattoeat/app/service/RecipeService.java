package whattoeat.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import whattoeat.app.dto.RecipeDTO;

import java.util.List;

public interface RecipeService {
    Page<RecipeDTO> getMatches(PageRequest of);

    List<RecipeDTO> searchByProductName(String productName);

    List<RecipeDTO> searchByRecipeName(String recipeName);
}
