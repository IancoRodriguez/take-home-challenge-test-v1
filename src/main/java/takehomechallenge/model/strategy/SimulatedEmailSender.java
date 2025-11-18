package takehomechallenge.model.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class SimulatedEmailSender implements IEmailSender{
    private static final Logger log = LoggerFactory.getLogger(SimulatedEmailSender.class);

    @Override
    public void send(String to, String subject, String htmlContent) {
        log.info("📧 ========================================");
        log.info("📧 [SIMULATION] Sending email...");
        log.info("📧 ========================================");
        log.info("📧 To: {}", to);
        log.info("📧 Subject: {}", subject);
        log.info("📧 Content Length: {} characters", htmlContent.length());
        log.info("📧 HTML Preview:");
        log.info("{}", htmlContent.substring(0, Math.min(200, htmlContent.length())) + "...");
        log.info("📧 ========================================");

        // Simular delay de red (500ms)
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Email sending interrupted", e);
        }

        log.info("✅ [SIMULATION] Email sent successfully!");
        log.info("📧 ========================================\n");
    }
}
