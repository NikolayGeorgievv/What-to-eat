package whattoeat.app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CreateCustomRecipeDTO {

    @Size(max = 100, message = "Името на рецептата е твърде дълго.")
    @NotEmpty
    private String recipeName;
    private List<String> productName;
    private List<String> quantity;
    @Size(max = 2500, message = "Описанието на приготвянето е твърде дълго.")
    @NotEmpty
    private String preparationDescription;

    public CreateCustomRecipeDTO() {
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<String> getProductName() {
        return productName;
    }

    public void setProductName(List<String> productName) {
        this.productName = productName;
    }

    public List<String> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<String> quantity) {
        this.quantity = quantity;
    }

    public String getPreparationDescription() {
        return preparationDescription;
    }

    public void setPreparationDescription(String preparationDescription) {
        this.preparationDescription = preparationDescription;
    }
}
