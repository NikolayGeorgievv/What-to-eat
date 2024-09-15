package whattoeat.app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {


    @GetMapping("/userProfile")
    public String getUserProfile() {
        return "userProfile";
    }


}
