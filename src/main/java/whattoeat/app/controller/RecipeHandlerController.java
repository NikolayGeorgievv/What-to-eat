package whattoeat.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import whattoeat.app.dto.RecipeDTO;
import whattoeat.app.service.service.RecipeService;
import whattoeat.app.service.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RecipeHandlerController {

    private final RecipeService recipeService;
    private final UserService userService;

    public RecipeHandlerController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }


    @GetMapping("/resultPage")
    private String getSearchPage() {
        return "resultPage";
    }

    @GetMapping("/getSingleRecipe")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getFavoriteRecipe(@RequestParam Long recipeId) {
        RecipeDTO recipe = recipeService.findRecipeDTOById(recipeId);

        Map<String, String> response = new HashMap<>();
        response.put("title", recipe.getName());
        response.put("content", recipe.getPreparationDescription());
        return ResponseEntity.ok(response);
    }


    @ModelAttribute("favoritesMap")
    public Map<Long, Boolean> getFavorites() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        if (userEmail.equals("anonymousUser")) {
            return new HashMap<>();
        }
        return recipeService.getFavorites(userEmail);
    }

    @PostMapping("/addToFavorite")
    public String addToFavorite(@RequestParam String recipeTitle, @RequestParam String fullRecipe, Authentication authentication, Model model) {
        String userEmail = authentication.getName();

        recipeService.addRecipeToFavorites(recipeTitle, userEmail, fullRecipe);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("favoritesMap", getFavorites());
        return "redirect:/userProfile";
    }

//    @PostMapping("/removeFromFavorite")
//    @ResponseBody
//    public Map<String, Object> removeFromFavorite(@RequestParam Long recipeId, Authentication authentication) {
//        String userEmail = authentication.getName();
//        userService.removeRecipeFromFavorites(recipeId, userEmail);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("favoritesMap", getFavorites());
//        return response;
//    }

    @PostMapping("removeFavoriteRecipe")
    public String removeFavoriteRecipe(@RequestParam Long recipeId, Authentication authentication) {
        String userEmail = authentication.getName();
        recipeService.removeFavoriteRecipe(recipeId, userEmail);

        return "redirect:/userProfile";
    }

    @ModelAttribute("userNotifications")
    public List<String> getUserNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userService.getUserNotifications(userEmail);
    }

    @ModelAttribute("favoriteRecipes")
    public List<String> getFavoriteRecipes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userService.getFavoriteRecipes(userEmail);
    }
}
