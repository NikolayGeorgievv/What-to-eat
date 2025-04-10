package whattoeat.app.service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import whattoeat.app.model.RegistrationVerificationToken;
import whattoeat.app.model.User;
import whattoeat.app.repository.RegistrationVerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class MailService {
    @Value("${mailtrap}")
    private String TOKEN;
    private final RegistrationVerificationTokenRepository verificationTokenRepository;

    public MailService(RegistrationVerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }


    public void sendRegisterActivationEmail(User user) {
        final MailtrapConfig config = new MailtrapConfig.Builder()
                .sandbox(true)
                .inboxId(3570930L)
                .token(TOKEN)
                .build();

        final MailtrapClient client = MailtrapClientFactory.createMailtrapClient(config);

        String token = createVerificationToken(user);
        String activationLink = "http://localhost:8080/activate?token=" + token;


        final MailtrapMail mail = MailtrapMail.builder()
                .from(new Address("whatTo@eat.com", "Email Activation Name"))
                .to(List.of(new Address(user.getEmail())))
                .subject("Email Activation Subject")
                .text("Activate your email here: " + activationLink)
                .category("Email Activation Category")
                .build();

        try {
            client.send(mail);
        } catch (Exception e) {
            System.out.println("Caught exception : " + e);
        }
    }

    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        RegistrationVerificationToken verificationToken = new RegistrationVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // expires in 24 hours

        verificationTokenRepository.saveAndFlush(verificationToken);

        return token;
    }

    public RegistrationVerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public void deleteVerifiedToken(RegistrationVerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }
}
