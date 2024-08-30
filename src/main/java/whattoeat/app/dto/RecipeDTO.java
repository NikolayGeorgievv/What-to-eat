package whattoeat.app.dto;

import whattoeat.app.model.Ingredient;

import java.util.List;

public class RecipeDTO {

    private String name;
    private String preparationDescription;
    private List<Ingredient> ingredients;

    public RecipeDTO(String name, String preparationDescription, List<Ingredient> ingredients) {
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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPreparationDescription(String preparationDescription) {
        this.preparationDescription = preparationDescription;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
