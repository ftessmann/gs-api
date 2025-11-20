package com.gs.api.service;

import com.gs.api.dto.response.NotificationResponseDTO;
import com.gs.api.model.Notification;
import com.gs.api.enums.NotificationStatus;
import com.gs.api.model.User;
import com.gs.api.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public List<NotificationResponseDTO> getMyNotifications(NotificationStatus status) {
        User authUser = authenticatedUserService.getAuthenticatedUser();

        List<Notification> notifications = (status == null)
                ? notificationRepository.findByRecipientId(authUser.getId())
                : notificationRepository.findByRecipientIdAndStatus(authUser.getId(), status);

        return notifications.stream().map(this::toResponseDTO).toList();
    }

    public NotificationResponseDTO markAsRead(Long id) {
        User authUser = authenticatedUserService.getAuthenticatedUser();

        Notification notification = notificationRepository.findById(id)
                .filter(n -> n.getRecipient().getId().equals(authUser.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));

        notification.setStatus(NotificationStatus.READ);
        notification.setReadAt(Instant.now());

        notificationRepository.save(notification);

        return toResponseDTO(notification);
    }

    private NotificationResponseDTO toResponseDTO(Notification n) {
        return NotificationResponseDTO.builder()
                .id(n.getId())
                .title(n.getTitle())
                .description(n.getDescription())
                .metric(n.getMetric())
                .measuredValue(n.getMeasuredValue())
                .thresholdValue(n.getThresholdValue())
                .status(n.getStatus())
                .createdAt(n.getCreatedAt())
                .readAt(n.getReadAt())
                .sectorId(n.getSector().getId())
                .sectorName(n.getSector().getName())
                .build();
    }
}
