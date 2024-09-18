package whattoeat.app.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import whattoeat.app.dto.CreateCustomRecipeDTO;
import whattoeat.app.service.service.UserService;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userProfile")
    public String getUserProfile() {
        return "userProfile";
    }

    @GetMapping("/getAddRecipeForm")
    public String getAddRecipe() {
        return "addRecipeForm";
    }

    @PostMapping("addCustomRecipe")
    public String addCustomRecipe(@RequestBody CreateCustomRecipeDTO recipeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        userService.addCustomRecipe(userEmail, recipeDTO);
        return "userProfile";
    }

    @ModelAttribute("favoriteRecipes")
    public List<String> getFavoriteRecipes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userService.getFavoriteRecipes(userEmail);
    }
}
