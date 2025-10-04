package com.eipl.amcs.base.service;

import com.eipl.amcs.base.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    List<Notification> findAll();

    List<Notification> save(List<Notification> list, String notificationInfo);

    List<Notification> update(List<Notification> list, String notificationInfo);

    Optional<Notification> findById(String notificationNo);

    void delete(String notificationNo, String notificationInfo);

    void delete(Notification notification, String notificationInfo);


}
