package takehomechallenge.service;


import takehomechallenge.model.Notification;

import java.util.List;

public interface INotificationService {
    Notification createAndSend(Notification n);
    void sendNotificationAsync(Notification n);
    List<Notification> findAllByUserEmail(String email);
    Notification findByIdAndUserEmail(Long id, String userEmail);
    Notification update(Long id, String title, String content, String userEmail);
    void delete(Long id, String userEmail);
}
