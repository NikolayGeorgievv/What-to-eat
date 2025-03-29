package whattoeat.app.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import whattoeat.app.service.service.UserService;

import java.util.List;

@Controller
public class SearchPageController {

    private final UserService userService;

    public SearchPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/searchPage")
    private String getSearchPage(Model model) {
        model.addAttribute("recipeNameNotEmpty", false);
        return "searchPage";
    }

    @ModelAttribute("userNotifications")
    public List<String> getUserNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userService.getUserNotifications(userEmail);
    }

    @ModelAttribute("recipeNameNotEmpty")
    public boolean recipeNameNotEmpty() {
        return false;
    }
}
