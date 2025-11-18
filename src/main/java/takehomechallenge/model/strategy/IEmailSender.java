package takehomechallenge.model.strategy;

public interface IEmailSender {
    void send(String to, String subject, String htmlContent);
}
