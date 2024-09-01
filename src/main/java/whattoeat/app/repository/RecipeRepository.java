package whattoeat.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import whattoeat.app.model.Ingredient;
import whattoeat.app.model.Recipe;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByName(String name);


    @Query(value = "SELECT r.*, COUNT(i.id) AS match_count " +
            "FROM recipe r " +
            "JOIN recipe_ingredients ri ON r.id = ri.recipe_id " +
            "JOIN ingredients i ON ri.ingredient_id = i.id " +
            "WHERE LOWER(i.name) IN :ingredientNames " +
            "GROUP BY r.id " +
            "ORDER BY match_count DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Recipe> findAllByIngredients(@Param("ingredientNames") List<String> ingredientNames);

    List<Recipe> findAllByNameContainingIgnoreCase(String recipeName);

    Recipe findByNameIgnoreCase(String title);


//    Optional<List<Recipe>> findAllByIngredientsContainingIgnoreCase(List<String> ingredients);
}
