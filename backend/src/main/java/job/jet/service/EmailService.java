package job.jet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String emailAddress, String token) {
        String link = "http://localhost:8080/auth/verif/" + token;

        String subject = "Verify Your Email";
        String content = "Dear User,\n\n"
                + "Thank you for registering. Please click the link below to verify your email:\n"
                + link + ".\n\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "Best regards,\n"
                + "JobJet.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailAddress);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
