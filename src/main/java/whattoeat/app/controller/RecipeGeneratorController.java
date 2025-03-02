package whattoeat.app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import whattoeat.app.service.rest.RecipeGenerator;

@Controller
public class RecipeGeneratorController {

    private final RecipeGenerator recipeGenerator;

    public RecipeGeneratorController(RecipeGenerator recipeGenerator) {
        this.recipeGenerator = recipeGenerator;
    }

    @GetMapping("/recipeGenerator")
    public String showForm(Model model) {
        model.addAttribute("recipe", ""); // Initialize recipe as empty
        return "recipeGenerator";
    }

    @PostMapping("/generate-recipe")
    public String generateRecipe(@RequestParam String ingredients, Model model) throws Exception {

        String text = recipeGenerator.getRecipe("Create a recipe using: " + ingredients);
        model.addAttribute("recipe", text);
        model.addAttribute("ingredients", ingredients);
        return "resultPage";

    }
}
