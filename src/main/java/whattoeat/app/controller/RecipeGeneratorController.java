package whattoeat.app.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import whattoeat.app.service.service.RecipeService;
import whattoeat.app.utils.MarkdownUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class RecipeGeneratorController {

    private final RecipeService recipeService;

    public RecipeGeneratorController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/generate-recipe")
    public String generateRecipe(@RequestParam String searchType, @RequestParam(required = false) String ingredients,
                                 @RequestParam(required = false) String recipeName, Model model,
                                 HttpSession session) throws Exception {

        List<String> previousRecipes = new ArrayList<>();

        String generatedRecipe = recipeService.generateRecipe(searchType, ingredients, recipeName);
        String recipeTitle = recipeService.extractTitleFromGeneratedRecipe(generatedRecipe);
        String parsedGeneratedRecipe = generatedRecipe.replace(" ...", "");
        String htmlRecipe = MarkdownUtils.convertMarkdownToHtml(parsedGeneratedRecipe);

        previousRecipes.add(recipeTitle);
        session.setAttribute("previousRecipes", previousRecipes);

        List<String> ingredientsList = (ingredients != null && !ingredients.isEmpty())
                ? Arrays.asList(ingredients.split(", "))  // Splitting by comma & optional spaces
                : new ArrayList<>();
        session.setAttribute("ingredients", ingredientsList);


        model.addAttribute("recipe", htmlRecipe);
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("recipeTitle", recipeTitle);
        model.addAttribute("recipeGenerated", true);

        if (!recipeName.isEmpty()){
            model.addAttribute("recipeNameNotEmpty", true);
        }else {
            model.addAttribute("recipeNameNotEmpty", false);
        }

        if (recipeService.isValidRecipe(parsedGeneratedRecipe)) {
            model.addAttribute("validRecipeGenerated", true);
        }
        return "resultPage";

    }

    @PostMapping("/generateAnotherRecipe")
    public String generateAnotherRecipe(HttpSession session, Model model) throws IOException, InterruptedException {

        // Retrieve stored ingredients & previous recipes
        List<String> ingredients = (List<String>) session.getAttribute("ingredients");
        List<String> previousRecipes = (List<String>) session.getAttribute("previousRecipes");

        if (ingredients == null) {
            return "redirect:/resultPage"; // Fallback if session expired
        }


        String generatedRecipe = recipeService.generateAnotherRecipe(ingredients, previousRecipes);
        String recipeTitle = recipeService.extractTitleFromGeneratedRecipe(generatedRecipe);
        String parsedGeneratedRecipe = generatedRecipe.replace(" ...", "");
        String htmlRecipe = MarkdownUtils.convertMarkdownToHtml(parsedGeneratedRecipe);

        // Store the new recipe
        previousRecipes.add(recipeTitle);
        session.setAttribute("previousRecipes", previousRecipes);

        model.addAttribute("recipe", htmlRecipe);
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("recipeTitle", recipeTitle);
        model.addAttribute("recipeGenerated", true);

        if (recipeService.isValidRecipe(parsedGeneratedRecipe)) {
            model.addAttribute("validRecipeGenerated", true);
        }

        return "resultPage";
    }

    @ModelAttribute("recipeNameNotEmpty")
    public boolean recipeNameNotEmpty() {
        return false;
    }
}
