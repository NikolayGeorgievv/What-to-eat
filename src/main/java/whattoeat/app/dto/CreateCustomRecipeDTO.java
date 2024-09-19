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

    public CreateCustomRecipeDTO(String recipeName, List<String> productName, List<String> quantity, String preparationDescription) {
        this.recipeName = recipeName;
        this.productName = productName;
        this.quantity = quantity;
        this.preparationDescription = preparationDescription;
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
    public String listProductsAndQuantities() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < productName.size(); i++) {
            sb.append(productName.get(i));
            if (quantity.get(i).equals(" ")) {
                sb.append("\n");
            } else {
                sb.append(" - ").append(quantity.get(i)).append("\n");
            }
        }
        return sb.toString();
    }
}
