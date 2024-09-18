package whattoeat.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import whattoeat.app.dto.CreateCustomRecipeDTO;
import whattoeat.app.model.CustomRecipeFromUsers;
import whattoeat.app.service.service.RecipeService;

import java.util.List;

@Controller
public class AdminController {

    private final RecipeService recipeService;

    public AdminController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/adminPage")
    public String getAdminPage() {

        return "/adminPage";
    }

    @ModelAttribute("recipesToApprove")
    public List<CreateCustomRecipeDTO> getRecipesToApprove() {
        return recipeService.getAllCustomRecipes();
    }
}
