package whattoeat.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whattoeat.app.model.RecipeIngredient;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredient, Long> {
}
