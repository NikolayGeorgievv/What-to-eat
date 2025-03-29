package whattoeat.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterUserDTO {

    @Email
    @Size(max = 40)
    private String email;


    @NotEmpty(message = "Please enter a password.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$#!%^*?&])[A-Za-z\\d@$#!%^*?&]{8,40}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character and be between 8 and 40 characters long.")
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
