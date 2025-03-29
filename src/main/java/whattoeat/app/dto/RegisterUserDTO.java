package whattoeat.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static whattoeat.app.constants.Constants.INVALID_EMAIL;
import static whattoeat.app.constants.Constants.INVALID_PASSWORD;

public class RegisterUserDTO {

    @Email
    @Size(max = 40)
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$",
            message = INVALID_EMAIL)
    private String email;


    @NotEmpty(message = "Please enter a password.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$#!%^*?&])[A-Za-z\\d@$#!%^*?&]{8,40}$",
            message = INVALID_PASSWORD)
    private String password;
    @NotEmpty
    private String confirmPassword;

    public RegisterUserDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
