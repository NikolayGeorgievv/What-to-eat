package whattoeat.app.dto;

public class RecipeDTO {

    private Long id;
    private String name;
    private String preparationDescription;

    public RecipeDTO(Long id, String name, String preparationDescription) {
        this.id = id;
        this.name = name;
        this.preparationDescription = preparationDescription;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPreparationDescription(String preparationDescription) {
        this.preparationDescription = preparationDescription;
    }
}
