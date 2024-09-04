package whattoeat.app.service.impl.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import whattoeat.app.model.UserRoleEntity;
import whattoeat.app.repository.UserRepository;


public class UserDetailImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .map(UserDetailImpl::map)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found!"));
    }


    private static UserDetails map(whattoeat.app.model.User user) {
        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(UserDetailImpl::mapRoles).toList())
                .build();

    }

    private static GrantedAuthority mapRoles(UserRoleEntity userRoleEntity) {
        return new SimpleGrantedAuthority(
                "ROLE_" + userRoleEntity.getRole().name()
        );
    }
}
