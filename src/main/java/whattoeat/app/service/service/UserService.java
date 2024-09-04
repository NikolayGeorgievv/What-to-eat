package whattoeat.app.service.service;

import org.springframework.security.core.Authentication;
import whattoeat.app.dto.RegisterUserDTO;

public interface UserService {

    Authentication login(String email);

    void registerUser(RegisterUserDTO registerUserDTO);


}
