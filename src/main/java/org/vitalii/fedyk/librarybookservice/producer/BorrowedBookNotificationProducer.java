package org.vitalii.fedyk.librarybookservice.producer;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.librarybookservice.dto.BorrowedBookNotificationDto;

import static org.vitalii.fedyk.librarybookservice.constant.TopicConstants.BORROWED_BOOK_NOTIFICATION;

@Service
@AllArgsConstructor
public class BorrowedBookNotificationProducer {
    private KafkaTemplate<String, BorrowedBookNotificationDto> kafkaTemplate;

    public void send(final BorrowedBookNotificationDto borrowedBookNotificationDto) {
        kafkaTemplate.send(BORROWED_BOOK_NOTIFICATION, borrowedBookNotificationDto);
    }
}
