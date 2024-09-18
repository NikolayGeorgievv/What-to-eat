package whattoeat.app.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import whattoeat.app.dto.CreateCustomRecipeDTO;
import whattoeat.app.service.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/addCustomRecipe")
    public ResponseEntity<?> addCustomRecipe(@RequestBody @Valid CreateCustomRecipeDTO recipeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        userService.addCustomRecipe(userEmail, recipeDTO);
        return ResponseEntity.ok().build();
    }

    @ModelAttribute("favoriteRecipes")
    public List<String> getFavoriteRecipes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userService.getFavoriteRecipes(userEmail);
    }
    @ModelAttribute("customRecipeDTO")
    public CreateCustomRecipeDTO getRecipeDTO() {
        return new CreateCustomRecipeDTO();
    }
}
