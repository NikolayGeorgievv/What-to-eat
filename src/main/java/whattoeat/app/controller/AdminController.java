package whattoeat.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import whattoeat.app.dto.CreateCustomRecipeDTO;
import whattoeat.app.service.service.RecipeService;
import whattoeat.app.service.service.UserService;

import java.util.List;

@Controller
public class AdminController {

    private final RecipeService recipeService;
    private final UserService userService;

    public AdminController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/recipeApprovalPage")
    public String getRecipeApprovalPage(@RequestParam("title") String title, Model model) {
        CreateCustomRecipeDTO recipe = recipeService.findCustomRecipeByTitle(title);
        model.addAttribute("recipe", recipe);
        return "/recipeApprovalPage";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approveRecipe")
    public String approveRecipe(@RequestParam("recipeName") String title) {
        recipeService.approveCustomRecipe(title);
        userService.sendApprovedNotificationToUser(title);
        userService.addCustomRecipeToUser(title);
        return "redirect:/adminPage";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rejectRecipe")
    public String rejectCustomRecipe(@RequestParam("recipeName") String title) {
        recipeService.rejectCustomRecipe(title);
        return "redirect:/adminPage";
    }
}
