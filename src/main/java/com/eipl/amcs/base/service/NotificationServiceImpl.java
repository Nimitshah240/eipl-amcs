package com.eipl.amcs.base.service;

import com.eipl.amcs.base.Notification;
import com.eipl.amcs.base.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;



    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public List<Notification> findAll() {
        List<Notification> list = notificationRepository.findAll(Sort.by("createdAt").descending());
        log.info("Notifications findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public List<Notification> save(List<Notification> list, String notificationInfo) {
        for (Notification notification : list) {
            notificationRepository.save(notification);
        }
        return list;
    }

    @Override
    public List<Notification> update(List<Notification> list, String notificationInfo) {
        for (Notification notification : list) {
            notificationRepository.save(notification);
        }
        return list;
    }

    @Override
    public Optional<Notification> findById(String notificationNo) {
        return notificationRepository.findById(notificationNo);
    }

    @Override
    public void delete(String notificationNo, String notificationInfo) {
        notificationRepository.deleteById(notificationInfo);
    }

    @Override
    public void delete(Notification notification, String notificationInfo) {
        notificationRepository.delete(notification);
    }

}