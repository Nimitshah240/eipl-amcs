package com.eipl.amcs.base.repository;

import com.eipl.amcs.base.Notification;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends BaseRepository<Notification, String> {

    @Override
    List<Notification> findAll(Sort sort);

}
