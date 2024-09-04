package whattoeat.app.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import whattoeat.app.dto.RecipeDTO;
import whattoeat.app.service.service.RecipeService;

@Controller
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
    public String search(@RequestParam String searchType, @RequestParam(required = false) String productName, @RequestParam(required = false) String recipeName, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, Model model) {
        Page<RecipeDTO> resultPage;
        if ("product".equals(searchType)) {
            resultPage = recipeService.searchByProductName(productName, PageRequest.of(page, size));
        } else if ("recipe".equals(searchType)) {
            resultPage = recipeService.searchByRecipeName(recipeName, PageRequest.of(page, size));
        } else {
            throw new IllegalArgumentException("Invalid search type");
        }
        model.addAttribute("page", resultPage);
        model.addAttribute("searchType", searchType);
        model.addAttribute("productName", productName);
        model.addAttribute("recipeName", recipeName);
        return "resultPage";
    }

    @GetMapping("/recipe")
    public String getRecipe(@RequestParam("title") String title, Model model) {
        RecipeDTO recipe = recipeService.findByTitle(title);
        model.addAttribute("recipe", recipe);
        return "popularRecipeResult";
    }
}
