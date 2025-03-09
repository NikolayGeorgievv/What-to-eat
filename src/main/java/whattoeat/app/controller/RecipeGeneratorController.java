package whattoeat.app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import whattoeat.app.service.rest.RecipeGenerator;
import whattoeat.app.utils.MarkdownUtils;

@Controller
public class RecipeGeneratorController {

    private final RecipeGenerator recipeGenerator;

    public RecipeGeneratorController(RecipeGenerator recipeGenerator) {
        this.recipeGenerator = recipeGenerator;
    }

    @PostMapping("/generate-recipe")
    public String generateRecipe(@RequestParam String searchType, @RequestParam(required = false) String ingredients,
                                 @RequestParam(required = false) String recipeName, Model model) throws Exception {
        String text = recipeGenerator.getRecipe(searchType ,ingredients, recipeName);
        String htmlRecipe = MarkdownUtils.convertMarkdownToHtml(text);
        model.addAttribute("recipe", htmlRecipe);
        model.addAttribute("ingredients", ingredients);
        return "resultPage";

    }
}
