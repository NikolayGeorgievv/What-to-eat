package whattoeat.app.service.impl.user;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import whattoeat.app.dto.RegisterUserDTO;
import whattoeat.app.model.Recipe;
import whattoeat.app.model.User;
import whattoeat.app.model.UserRoleEntity;
import whattoeat.app.repository.RolesRepository;
import whattoeat.app.repository.UserRepository;
import whattoeat.app.service.service.RecipeService;
import whattoeat.app.service.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserDetailImpl userDetail;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final RecipeService recipeService;

    public UserServiceImpl(UserDetailImpl userDetail, PasswordEncoder passwordEncoder, UserRepository userRepository, RolesRepository rolesRepository, RecipeService recipeService) {
        this.userDetail = userDetail;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.recipeService = recipeService;
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

    @Override
    public void addRecipeToFavorites(Long recipeId, String userEmail) {
        Recipe recipeById = recipeService.findById(recipeId);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.getFavoriteRecipes().add(recipeById.getName());
        userRepository.saveAndFlush(user);
    }

    @Override
    public void removeRecipeFromFavorites(Long recipeId, String userEmail) {
        Recipe recipeById = recipeService.findById(recipeId);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.getFavoriteRecipes().remove(recipeById.getName());
        userRepository.saveAndFlush(user);
    }

    @Override
    public Map<Long, Boolean> getFavorites(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Map<Long, Boolean> userFavorites = new HashMap<>();
        user.getFavoriteRecipes().forEach(recipeName -> {
            Recipe recipe = recipeService.findByName(recipeName);
            userFavorites.put(recipe.getId(), true);
        });
        return userFavorites;
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
