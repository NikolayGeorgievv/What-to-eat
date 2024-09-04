package whattoeat.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/users/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users/login-error")
    public String loginErr(Model model) {
        model.addAttribute("error", true);
        return "login";
    }
}
