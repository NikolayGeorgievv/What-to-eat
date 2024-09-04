package whattoeat.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whattoeat.app.model.UserRoleEntity;

@Repository
public interface RolesRepository extends JpaRepository<UserRoleEntity, Long> {
}
