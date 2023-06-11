package com.laurynas.uzduotis.services;

import com.laurynas.uzduotis.api.dto.response.MessageStatisticsResponseDTO;
import com.laurynas.uzduotis.api.models.MessageModel;
import com.laurynas.uzduotis.api.models.UserModel;
import com.laurynas.uzduotis.repository.MessageRepository;
import com.laurynas.uzduotis.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class StatisticsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    public MessageStatisticsResponseDTO getUserMessagesStatistics(String username) {
        UserModel user = userRepository.findByUsername(username);

        MessageStatisticsResponseDTO statistics = new MessageStatisticsResponseDTO();
        statistics.setUsername(user.getUsername());

        List<MessageModel> messages = messageRepository.findByUserId(user.getId());
        if (!messages.isEmpty()) {
            messages.sort((MessageModel message1, MessageModel message2) -> {
                long timestamp1 = message1.getTimestamp();
                long timestamp2 = message2.getTimestamp();

                return Long.compare(timestamp1, timestamp2);
            });

            int sumOfMessages = 0;
            for (MessageModel message : messages) {
                sumOfMessages += message.getMessage().length();
            }
            final int averageMessageSize = sumOfMessages / messages.size();

            statistics.setFirstMessageTimestamp(messages.get(0).getTimestamp());
            statistics.setLastMessageTimestamp(messages.get(messages.size() - 1).getTimestamp());
            statistics.setMessageCount(messages.size());
            statistics.setLastMessageText(messages.get(messages.size() - 1).getMessage());
            statistics.setAverageMessageLength(averageMessageSize);
        }

        return statistics;
    }
}
