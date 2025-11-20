package com.gs.api.repository;

import com.gs.api.model.Notification;
import com.gs.api.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientId(Long userId);

    List<Notification> findByRecipientIdAndStatus(Long userId, NotificationStatus status);
}
