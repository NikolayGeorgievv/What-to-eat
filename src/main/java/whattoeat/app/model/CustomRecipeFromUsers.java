package whattoeat.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "custom_recipes_from_users")
public class CustomRecipeFromUsers extends BaseEntity {

    @ManyToOne
    private User addedByUser;
    @Column(nullable = false, name = "recipe_name", unique = true)
    private String recipeName;
    @Column(name = "product_name_and_quantity", nullable = false)
    private String productNameAndQuantity;
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;



    public CustomRecipeFromUsers() {
    }

    public User getAddedByUser() {
        return addedByUser;
    }

    public void setAddedByUser(User addedByUser) {
        this.addedByUser = addedByUser;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getProductNameAndQuantity() {
        return productNameAndQuantity;
    }

    public void setProductNameAndQuantity(String productNameAndQuantity) {
        this.productNameAndQuantity = productNameAndQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
