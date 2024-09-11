package whattoeat.app.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "recipes")
public class Recipe extends BaseEntity {

    @Column
    private String name;

    @Column
    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> ingredients;

    @Column(columnDefinition = "text")
    private String preparationDescription;

    @Column(name = "liked_counter")
    private int likedCounter;


    public Recipe() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getPreparationDescription() {
        return preparationDescription;
    }

    public void setPreparationDescription(String preparationDescription) {
        this.preparationDescription = preparationDescription;
    }

    public int getLikedCounter() {
        return likedCounter;
    }

    public void setLikedCounter(int likedCounter) {
        this.likedCounter = likedCounter;
    }
}
