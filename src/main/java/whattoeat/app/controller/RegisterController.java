package whattoeat.app.controller;


import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import whattoeat.app.dto.RegisterUserDTO;
import whattoeat.app.service.service.UserService;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/users/register")
    public String register(@Valid @ModelAttribute("registerUserDTO") RegisterUserDTO registerUserDTO, BindingResult bindingResult, RedirectAttributes rAtt) {
        boolean passwordsNotMatch = !registerUserDTO.getPassword().equals(registerUserDTO.getConfirmPassword());

        if (passwordsNotMatch) {
            bindingResult.addError(new FieldError("registerUserDTO", "confirmPassword", "Passwords should match."));
        }

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("registerUserDTO", registerUserDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.RegisterUserDTO", bindingResult);

            return "registerForm";
        }


        try {
        userService.registerUser(registerUserDTO);
        } catch (IllegalArgumentException e) {
            bindingResult.addError(new FieldError("registerUserDTO", "email", e.getMessage()));
            rAtt.addFlashAttribute("registerUserDTO", registerUserDTO);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.RegisterUserDTO", bindingResult);
            return "registerForm";
        }
        return "login";
    }

    @GetMapping("/users/register")
    public String register(RegisterUserDTO registerUserDTO) {

        return "registerForm";
    }

    @GetMapping("/users/termsAndConditions")
    public String termsAndConditions() {
        return "termsAndConditions";
    }
}
