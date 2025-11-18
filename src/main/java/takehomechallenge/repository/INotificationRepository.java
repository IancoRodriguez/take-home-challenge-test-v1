package takehomechallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import takehomechallenge.model.Notification;

import java.util.List;
import java.util.Optional;

public interface INotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserEmail(String email);
    Optional<Notification> findByIdAndUserEmail(Long id, String userEmail);
}
