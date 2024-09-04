package whattoeat.app.service.impl.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import whattoeat.app.model.UserRoleEntity;
import whattoeat.app.model.enums.UserRoleEnum;
import whattoeat.app.repository.RolesRepository;

import java.util.List;

import static whattoeat.app.model.enums.UserRoleEnum.ADMIN;


@Component
public class RolesRepositoryInit implements CommandLineRunner {

    private final RolesRepository rolesRepository;

    public RolesRepositoryInit(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (this.rolesRepository.count() == 0) {

            UserRoleEntity userRoleEntity = new UserRoleEntity();
            UserRoleEntity adminRoleEntity = new UserRoleEntity();

            adminRoleEntity.setRole(ADMIN);
            userRoleEntity.setRole(UserRoleEnum.USER);

            rolesRepository.saveAllAndFlush(List.of(userRoleEntity, adminRoleEntity));
        }
    }

}
