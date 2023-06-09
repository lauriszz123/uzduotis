package com.laurynas.uzduotis.services;

import com.laurynas.uzduotis.api.models.MessageModel;
import com.laurynas.uzduotis.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void createMessage(long userId, String text) {
        MessageModel message = new MessageModel(userId, text);
        messageRepository.save(message);
    }

    public List<MessageModel> getAllMessages() {
        return messageRepository.findAll();
    }
}
