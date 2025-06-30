package com.fyp.hotel.service.user.message.Impl;

import com.fyp.hotel.dto.user.UserMessageDTO;
import com.fyp.hotel.model.UserMessage;
import com.fyp.hotel.repository.UserMessageRepository;
import com.fyp.hotel.service.user.message.UserMessageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Transactional
public class UserMessageServiceImpl implements UserMessageService {

    private final UserMessageRepository userMessageRepository;

    @Autowired
    public UserMessageServiceImpl(UserMessageRepository userMessageRepository) {
        this.userMessageRepository = userMessageRepository;
    }

    @Override
    public String postUserMessage(UserMessageDTO userMessageDTO) {
        try {
            UserMessage userMessage = new UserMessage();
            userMessage.setFirstName(userMessageDTO.getFirstName());
            userMessage.setLastName(userMessageDTO.getLastName());
            userMessage.setEmail(userMessageDTO.getEmail());
            userMessage.setMessage(userMessageDTO.getMessage());
            userMessage.setCreatedAt(LocalDate.now());

            userMessageRepository.save(userMessage);
            return "Message posted successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to post message.";
        }
    }
}