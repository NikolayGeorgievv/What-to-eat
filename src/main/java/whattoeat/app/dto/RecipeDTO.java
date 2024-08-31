package whattoeat.app.dto;

import whattoeat.app.model.RecipeIngredient;

import java.util.List;

public class RecipeDTO {

    private String name;
    private String preparationDescription;
    private List<RecipeIngredient> ingredients;

    public RecipeDTO(String name, String preparationDescription, List<RecipeIngredient> ingredients) {
        this.name = name;
        this.preparationDescription = preparationDescription;
        this.ingredients = ingredients;
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
