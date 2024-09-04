package whattoeat.app.service.impl.user;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import whattoeat.app.dto.RegisterUserDTO;
import whattoeat.app.model.User;
import whattoeat.app.model.UserRoleEntity;
import whattoeat.app.repository.RolesRepository;
import whattoeat.app.repository.UserRepository;
import whattoeat.app.service.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDetailImpl userDetail;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;

    public UserServiceImpl(UserDetailImpl userDetail, PasswordEncoder passwordEncoder, UserRepository userRepository, RolesRepository rolesRepository) {
        this.userDetail = userDetail;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
    }




    @Override
    public Authentication login(String email) {
        UserDetails userDetails = userDetail.loadUserByUsername(email);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        return auth;
    }

    @Override
    public void registerUser(RegisterUserDTO registerUserDTO) {

        if (userRepository.findByEmail(registerUserDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        User user = mapUser(registerUserDTO);

        //First registered user will be Admin
        if (this.userRepository.count() == 0) {
            List<UserRoleEntity> allRoles = rolesRepository.findAll();
            user.setRoles(allRoles);
        } else {
            UserRoleEntity userRole = rolesRepository.getReferenceById(1L);
            user.setRoles(List.of(userRole));
        }

        userRepository.saveAndFlush(user);
    }

    private User mapUser(RegisterUserDTO registerUserDTO) {
        User user = new User();
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        List<String> favoriteRecipes = new ArrayList<>();
        user.setFavoriteRecipes(favoriteRecipes);
        return user;
    }
}
