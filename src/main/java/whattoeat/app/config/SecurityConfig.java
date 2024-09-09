package whattoeat.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import whattoeat.app.model.enums.UserRoleEnum;
import whattoeat.app.repository.UserRepository;
import whattoeat.app.service.impl.user.UserDetailImpl;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final String rememberMeKey;

    public SecurityConfig(@Value("${rememberMe}") String rememberMeKey) {
        this.rememberMeKey = rememberMeKey;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                // Allow all static resources to be available.
                                .requestMatchers(String.valueOf(PathRequest.toStaticResources().atCommonLocations())).permitAll()
                                .requestMatchers("/images/**", "/js/**", "/css/**", "/favicon/**").permitAll()
//                                // Allow anyone to see the home page, the registration page and the login form.
                                .requestMatchers("/mainPage", "/users/login", "/users/register", "/", "/users/login-error").permitAll()
                                .requestMatchers("/error", "/fragments/**", "/users/termsAndConditions").permitAll()
                                .requestMatchers("resultPage", "results", "search", "recipe", "searchPage").permitAll()
                                .requestMatchers("/adminPage").hasRole(UserRoleEnum.ADMIN.name())
                                .anyRequest().authenticated()
                ).formLogin(
                        formLogin -> {
                            formLogin
                                    .loginPage("/users/login")
                                    // The names of the input fields
                                    .usernameParameter("email")
                                    .passwordParameter("password")
                                    .defaultSuccessUrl("/userProfile")
                                    .failureUrl("/users/login-error");

                        }
                ).logout(
                        logout -> {
                            logout
                                    .logoutUrl("/logout")
                                    .logoutSuccessUrl("/mainPage")
                                    .invalidateHttpSession(true);
                        }
                ).rememberMe(
                        rememberMe ->
                                rememberMe
                                        .key(rememberMeKey)
                                        .rememberMeParameter("rememberMe")
                                        .rememberMeCookieName("rememberMe")
                ).csrf(AbstractHttpConfigurer::disable)
                .build();


    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    public UserDetailImpl userDetailsService(UserRepository userRepository) {
        return new UserDetailImpl(userRepository);
    }

}
