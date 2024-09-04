package whattoeat.app.model;

import jakarta.persistence.*;
import whattoeat.app.model.enums.UserRoleEnum;

@Table(name = "roles")
@Entity
public class UserRoleEntity extends BaseEntity {

    @Column
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }
}
