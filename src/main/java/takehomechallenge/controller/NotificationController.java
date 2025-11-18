package takehomechallenge.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import takehomechallenge.config.INotificationApi;
import takehomechallenge.dto.notification.CreateNotificationDto;
import takehomechallenge.dto.notification.NotificationResponseDto;
import takehomechallenge.dto.notification.UpdateNotificationDto;
import takehomechallenge.model.Notification;
import takehomechallenge.model.NotificationStatus;
import takehomechallenge.service.INotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController implements INotificationApi {
    @Override
    public ResponseEntity<?> findAll(Authentication authentication) {
        String userEmail = authentication.getName();

        List<Notification> not = notificationService.findAllByUserEmail(userEmail);

        List<NotificationResponseDto> response =  not.stream()
                .map(NotificationResponseDto::fromEntity)
                .collect(Collectors.toList());


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<?> findById(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();

        Notification not = notificationService.findByIdAndUserEmail(id, userEmail);

        NotificationResponseDto response =  NotificationResponseDto.fromEntity(not);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody UpdateNotificationDto dto,
                                    Authentication authentication) {
        String userEmail = authentication.getName();

        Notification updated = notificationService.update(
                id,
                dto.getTitle(),
                dto.getContent(),
                userEmail
        );

        NotificationResponseDto response =  NotificationResponseDto.fromEntity(updated);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();

        notificationService.delete(id, userEmail);

        return ResponseEntity.noContent().build();
    }

    private final INotificationService notificationService;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody CreateNotificationDto dto,
                                    Authentication authentication){
        String userEmail = authentication.getName();

        Notification notification = Notification.create(
                dto.getTitle(),
                dto.getContent(),
                dto.getChannel(),
                userEmail
        );

        Notification created = notificationService.createAndSend(notification);

        NotificationResponseDto responseDto = NotificationResponseDto.fromEntity(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }




}
