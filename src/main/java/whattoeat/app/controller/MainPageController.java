package whattoeat.app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import whattoeat.app.service.service.UserService;

import java.util.List;

@Controller
public class MainPageController {

    private final UserService userService;

    public MainPageController(UserService userService) {
        this.userService = userService;
    }


    @ModelAttribute("userEmail")
    public String userEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    @GetMapping({"/", "/mainPage"})
    private String getMainPage() {
        return "mainPage";
    }
    @ModelAttribute("userNotifications")
    public List<String> getUserNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userService.getUserNotifications(userEmail);
    }
}
