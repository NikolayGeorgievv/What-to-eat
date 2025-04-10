package whattoeat.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whattoeat.app.model.RegistrationVerificationToken;

@Repository
public interface RegistrationVerificationTokenRepository extends JpaRepository<RegistrationVerificationToken, Long> {
    RegistrationVerificationToken findByToken(String token);
}
