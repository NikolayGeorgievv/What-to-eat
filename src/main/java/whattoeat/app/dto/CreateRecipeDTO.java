package whattoeat.app.dto;

import java.util.List;

public class CreateRecipeDTO {

    //TODO: ADD VALIDATION
    private String recipeName;
    private List<String> productName;
    private List<String> quantity;

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
}
