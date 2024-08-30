package whattoeat.app.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import whattoeat.app.dto.RecipeDTO;
import whattoeat.app.repository.RecipeRepository;
import whattoeat.app.service.RecipeService;

import java.util.List;

@RestController
public class ResultPageController {

    private final RecipeService recipeService;

    public ResultPageController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/resultPage")
    private String getSearchPage() {
        return "resultPage";
    }
    @GetMapping("/results")
    public Page<RecipeDTO> getResults(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
        return recipeService.getMatches(PageRequest.of(page, size));
    }
    @GetMapping("/search")
    public List<RecipeDTO> search(@RequestParam String searchType, @RequestParam(required = false) String productName, @RequestParam(required = false) String recipeName) {
        if ("product".equals(searchType)) {
            return recipeService.searchByProductName(productName);
        } else if ("recipe".equals(searchType)) {
            return recipeService.searchByRecipeName(recipeName);
        } else {
            throw new IllegalArgumentException("Invalid search type");
        }
    }
}
