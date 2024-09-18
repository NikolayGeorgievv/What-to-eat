package whattoeat.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whattoeat.app.model.CustomRecipeFromUsers;

@Repository
public interface CustomRecipeFromUsersRepository extends JpaRepository<CustomRecipeFromUsers, Long> {
}
