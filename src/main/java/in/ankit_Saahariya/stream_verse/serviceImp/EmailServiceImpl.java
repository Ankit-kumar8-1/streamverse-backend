package in.ankit_Saahariya.stream_verse.serviceImp;

import in.ankit_Saahariya.stream_verse.exception.EmailNotVerifiedException;
import in.ankit_Saahariya.stream_verse.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;



@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);


    private final JavaMailSender mailSender;
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${app.backend.url}")
    private String backendUrl;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;


    @Override
    public void sendVerificationEmail(String toEmail, String token) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Stream_Verse - Verify your Email");
            String verificationLink =
                    backendUrl + "/api/v1.1/auth/verify-email?token=" + token +
                            "&email=" + URLEncoder.encode(toEmail, StandardCharsets.UTF_8);


            String emailBody =
                    "Welcome to Stream_verse,\n\n" +
                            "Thank you for registering with Stream_Verse.\n" +
                            "Please verify your account by clicking the link below:\n\n" +
                            verificationLink + "\n\n" +
                            "This link is valid for a limited time. If you did not create this account, please ignore this email.\n\n" +
                            "Best Regards,\n" +
                            "Team Stream_Verse";

            message.setText(emailBody);
            mailSender.send(message);
            logger.info("Verification email sent to {}",toEmail);

        }catch (Exception ex){
            logger.error("failed to send verification email to {}: {}",toEmail,ex.getMessage());
            throw new EmailNotVerifiedException("Failed to send verification email");
        }
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String token) {

        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Stream_verse - Password Reset");
            String resetLink = backendUrl
                    + "/api/v1.1/auth/reset-password?token="
                    + URLEncoder.encode(token, StandardCharsets.UTF_8);

            String emailBody=
                    "Hi,\n\n" +
                            "We received a request to reset your Stream_Verse account password.\n\n" +
                            "Please click the link below to reset your password:\n\n" +
                            resetLink + "\n\n" +
                            "This link is valid for a limited time. Please do not share this link with anyone.\n\n" +
                            "If you did not request this, please ignore this email.\n\n" +
                            "Best Regards,\n" +
                            "Team Stream_Verse";

            message.setText(emailBody);
            mailSender.send(message);

            logger.info("Password reset email sent to {}", toEmail);

        }catch (Exception ex){
            logger.error("Failed to send password reset email to {}: {}",toEmail,ex.getMessage(),ex);
            throw new RuntimeException("Failed to send password reset email");
        }
    }
}
