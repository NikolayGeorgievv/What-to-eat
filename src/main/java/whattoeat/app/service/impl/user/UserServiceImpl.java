package whattoeat.app.service.impl.user;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import whattoeat.app.dto.RegisterUserDTO;
import whattoeat.app.model.Notification;
import whattoeat.app.model.User;
import whattoeat.app.model.UserRoleEntity;
import whattoeat.app.repository.NotificationRepository;
import whattoeat.app.repository.RolesRepository;
import whattoeat.app.repository.UserRepository;
import whattoeat.app.service.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDetailImpl userDetail;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final NotificationRepository notificationRepository;

    public UserServiceImpl(UserDetailImpl userDetail, PasswordEncoder passwordEncoder, UserRepository userRepository, RolesRepository rolesRepository, NotificationRepository notificationRepository) {
        this.userDetail = userDetail;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.notificationRepository = notificationRepository;
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

//    @Override
//    public void removeRecipeFromFavorites(Long recipeId, String userEmail) {
//        Recipe recipeById = recipeService.findById(recipeId);
//        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
//        user.getFavoriteRecipes().remove(recipeById.getName());
//        recipeById.setLikedCounter(recipeById.getLikedCounter() - 1);
//        recipeRepository.saveAndFlush(recipeById);
//        userRepository.saveAndFlush(user);
//    }

    @Override
    public List<String> getFavoriteRecipes(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getFavoriteRecipes();
    }

    @Override
    public List<String> getUserNotifications(String userEmail) {
        try {
            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
            List<Notification> notifications = user.getNotifications();
            List<String> notificationMessages = new ArrayList<>();
            if (notifications.isEmpty()) {
                String noNotifications = "Все още нямате нови съобщения.";
                notificationMessages.add(noNotifications);
                return notificationMessages;
            }
            for (Notification notification : notifications) {
                notificationMessages.add(notification.getMessage());
            }
            return notificationMessages;
        } catch (IllegalArgumentException e) {
            return null;
        }

    }

//    @Override
//    public void sendApprovedNotificationToUser(String title) {
//        CustomRecipeFromUsers customRecipe = customRecipeFromUsersRepository.findByRecipeName(title).get();
//        User user = customRecipe.getAddedByUser();
//        Notification notification = new Notification();
//        notification.setMessage(setApprovedRecipeNotification(customRecipe.getRecipeName()));
//        notificationRepository.saveAndFlush(notification);
//        user.getNotifications().add(notification);
//        userRepository.saveAndFlush(user);
//    }

    @Override
    public Optional<User> findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    private String setApprovedRecipeNotification(String recipeName) {
        return String.format("Вашата рецепта %s беше одобрена!", recipeName);
    }

    private User mapUser(RegisterUserDTO registerUserDTO) {
        User user = new User();
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        List<String> favoriteRecipes = new ArrayList<>();
        user.setFavoriteRecipes(favoriteRecipes);
        List<Notification> notifications = new ArrayList<>();
        user.setNotifications(notifications);
        return user;
    }
}
