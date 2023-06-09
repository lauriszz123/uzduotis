package com.laurynas.uzduotis.repository;

import com.laurynas.uzduotis.api.models.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageModel, Long> {
    List<MessageModel> findByUserId(Long userId);
}
