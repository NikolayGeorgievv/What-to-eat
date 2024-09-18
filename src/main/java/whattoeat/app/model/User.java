package whattoeat.app.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<CustomRecipeFromUsers> customRecipeFromUsers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_favorite_recipes", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "recipe")
    private List<String> favoriteRecipes;

    @ElementCollection
    @CollectionTable(name = "recipes_added_by_user", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "recipes_added_by_user")
    private List<String> recipesAddedByUser;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<UserRoleEntity> roles;

    public User() {
    }

    public User(String email, String password, List<String> favoriteRecipes) {
        this.email = email;
        this.password = password;
        this.favoriteRecipes = favoriteRecipes;
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

    public List<String> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public void setFavoriteRecipes(List<String> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }

    public List<UserRoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoleEntity> roles) {
        this.roles = roles;
    }

    public List<CustomRecipeFromUsers> getCustomRecipeFromUsers() {
        return customRecipeFromUsers;
    }

    public void setCustomRecipeFromUsers(List<CustomRecipeFromUsers> customRecipeFromUsers) {
        this.customRecipeFromUsers = customRecipeFromUsers;
    }

    public List<String> getRecipesAddedByUser() {
        return recipesAddedByUser;
    }

    public void setRecipesAddedByUser(List<String> recipesAddedByUser) {
        this.recipesAddedByUser = recipesAddedByUser;
    }
}
