package whattoeat.app.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
