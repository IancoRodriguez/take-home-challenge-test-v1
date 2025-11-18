package takehomechallenge.model.strategy;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class RealEmailSender implements IEmailSender{

    private final Logger log = LoggerFactory.getLogger(RealEmailSender.class);
    private final JavaMailSender mailSender;

    public RealEmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;

    }


    @Override
    public void send(String to, String subject, String htmlContent) {
        log.info("📧 [REAL] Sending email via SMTP...");
        log.info("📧 To: {}", to);
        log.info("📧 Subject: {}", subject);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);

            log.info("✅ [REAL] Email sent successfully via SMTP!");

        } catch (Exception e) {
            log.error("❌ [REAL] Failed to send email: {}", e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
