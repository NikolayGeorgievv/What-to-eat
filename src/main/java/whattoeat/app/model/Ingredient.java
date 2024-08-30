package whattoeat.app.model;


import jakarta.persistence.*;
import whattoeat.app.model.enums.IngredientsCategories;

import java.util.Map;

@Entity
@Table(name = "ingredients")
public class Ingredient extends BaseEntity {

    @Column(nullable = false)
    private String name;

    public Ingredient() {
    }

    public Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
