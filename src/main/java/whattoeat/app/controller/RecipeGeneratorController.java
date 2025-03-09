package whattoeat.app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import whattoeat.app.service.service.RecipeService;
import whattoeat.app.utils.MarkdownUtils;

@Controller
public class RecipeGeneratorController {

    private final RecipeService recipeService;

    public RecipeGeneratorController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/generate-recipe")
    public String generateRecipe(@RequestParam String searchType, @RequestParam(required = false) String ingredients,
                                 @RequestParam(required = false) String recipeName, Model model) throws Exception {
        String generatedRecipe = recipeService.generateRecipe(searchType, ingredients, recipeName);
        String recipeTitle = recipeService.extractTitleFromGeneratedRecipe(generatedRecipe);
        String parsedGeneratedRecipe = generatedRecipe.replace(" ...", "");
        String htmlRecipe = MarkdownUtils.convertMarkdownToHtml(parsedGeneratedRecipe);

        model.addAttribute("recipe", htmlRecipe);
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("recipeTitle", recipeTitle);
        return "resultPage";

    }
}
