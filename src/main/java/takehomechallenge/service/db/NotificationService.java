package takehomechallenge.service.db;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import takehomechallenge.model.Notification;
import takehomechallenge.model.strategy.ChannelStrategyFactory;
import takehomechallenge.model.strategy.IChannelStrategy;
import takehomechallenge.repository.INotificationRepository;
import takehomechallenge.service.INotificationService;

import java.util.List;

@Service
public class NotificationService implements INotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final INotificationRepository notificationRepository;
    private final ChannelStrategyFactory strategyFactory;


    public NotificationService(INotificationRepository notificationRepository, ChannelStrategyFactory strategyFactory) {
        this.notificationRepository = notificationRepository;
        this.strategyFactory = strategyFactory;
    }


    @Transactional
    @Override
    public Notification createAndSend(Notification n) {
        log.info(" Creating notification for user: {}", n.getUserEmail());

        Notification saved = notificationRepository.save(n);
        log.info("notification created with id: {}", saved.getId());


        sendNotificationAsync(saved);

        return saved;

    }

    @Override
    @Async
    public void sendNotificationAsync(Notification n) {

        log.info(" ASYNC starting notification send in background thread: {}",
                Thread.currentThread().getName());

        try{
            n.markAsSending();
            notificationRepository.save(n);
            log.info("ASYNC notification {} marked as sending", n.getId());

            // get strategy for channel

            IChannelStrategy strategy = strategyFactory.getStrategy(n.getChannel());
            log.info(" ASYNC using strategy: {} for channel : {}",
                    strategy.getClass().getSimpleName(), n.getChannel());

            strategy.send(n);

            // mark ass sent
            n.markAsSent();
            notificationRepository.save(n);

            log.info("ASYNC notification {} marked as sent", n.getId());

        } catch (Exception ex) {
            log.error(" ASYNC notification {} marked as failed", n.getId(), ex);
            n.markAsFailed();
            notificationRepository.save(n);
        }

        log.info("[ASYNC] Background thread finished: {}",
                Thread.currentThread().getName());
    }

    @Override
    public List<Notification> findAllByUserEmail(String email) {
        log.info(" Finding all notifications by user email: {}", email);

        List<Notification> notifications = notificationRepository.findByUserEmail(email);
        log.info("found {} notifications for user: {}", notifications.size(), email);

        return notifications;
    }

    @Override
    public Notification findByIdAndUserEmail(Long id, String userEmail) {
        log.info("🔍 Finding notification {} for user: {}", id, userEmail);

        return notificationRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> {
                    log.warn("Notification {} not found for user: {}", id, userEmail);
                    return new IllegalArgumentException(
                            "Notification not found or you don't have permission to access it"
                    );
                });
    }

    @Override
    public Notification update(Long id, String title, String content, String userEmail) {
        log.info("✏ Updating notification {} for user: {}", id, userEmail);

        // 1. Buscar y verificar permisos
        Notification notification = findByIdAndUserEmail(id, userEmail);

        // 2. Actualizar campos permitidos
        notification.updateTitle(title);
        notification.updateContent(content);

        // 3. Guardar
        Notification updated = notificationRepository.save(notification);

        log.info(" Notification {} updated successfully", id);
        return updated;
    }

    @Override
    public void delete(Long id, String userEmail) {

        log.info("🗑️ Deleting notification {} for user: {}", id, userEmail);

        // 1. Buscar y verificar permisos
        Notification notification = findByIdAndUserEmail(id, userEmail);

        // 2. Eliminar
        notificationRepository.delete(notification);

        log.info("Notification {} deleted successfully", id);
    }
}
