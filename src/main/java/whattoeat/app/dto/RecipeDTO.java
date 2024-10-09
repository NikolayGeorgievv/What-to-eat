package whattoeat.app.dto;

import whattoeat.app.model.RecipeIngredient;

import java.util.List;

public class RecipeDTO {

    private Long id;
    private String name;
    private String preparationDescription;
    private List<RecipeIngredient> ingredients;
    private Integer likedCounter;

    public RecipeDTO(Long id, String name, String preparationDescription, List<RecipeIngredient> ingredients, Integer likedCounter) {
        this.id = id;
        this.name = name;
        this.preparationDescription = preparationDescription;
        this.ingredients = ingredients;
        this.likedCounter = likedCounter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPreparationDescription() {
        return preparationDescription;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPreparationDescription(String preparationDescription) {
        this.preparationDescription = preparationDescription;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Integer getLikedCounter() {
        return likedCounter;
    }

    public void setLikedCounter(Integer likedCounter) {
        this.likedCounter = likedCounter;
    }

    public String getIngredientsQuantity() {
        StringBuilder sb = new StringBuilder();
        ingredients.forEach(ingredient -> {
            if (ingredient.getQuantity() != null) {
                sb.append(ingredient.getIngredient().getName())
                        .append(" - ")
                        .append(ingredient.getQuantity());
            } else {
                sb.append(ingredient.getIngredient().getName())
                        .append(" - ");
            }
            sb.append("\n");
        });
        return sb.toString();
    }
}
