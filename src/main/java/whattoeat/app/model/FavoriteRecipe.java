package whattoeat.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_favorite_recipes")
public class FavoriteRecipe extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Recipe recipe;

    @Column(name = "recipe_name")
    private String recipeName;

    @Column(name = "full_recipe", columnDefinition = "text")
    private String fullRecipe;

    public FavoriteRecipe() {}

    public FavoriteRecipe(User user, Recipe recipe, String recipeName, String fullRecipe) {
        this.user = user;
        this.recipe = recipe;
        this.recipeName = recipeName;
        this.fullRecipe = fullRecipe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getFullRecipe() {
        return fullRecipe;
    }

    public void setFullRecipe(String fullRecipe) {
        this.fullRecipe = fullRecipe;
    }
}
