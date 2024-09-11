package whattoeat.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import whattoeat.app.service.service.UserService;

@Controller
public class RecipeController {

    private final UserService userService;

    public RecipeController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/addToFavorite")
    public ResponseEntity<Void> addToFavorites(@RequestParam Long recipeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        userService.addRecipeToFavorites(recipeId, userEmail);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/removeFromFavorite")
    public ResponseEntity<Void> removeFromFavorites(@RequestParam Long recipeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        userService.removeRecipeFromFavorites(recipeId, userEmail);
        return ResponseEntity.ok().build();
    }
}
