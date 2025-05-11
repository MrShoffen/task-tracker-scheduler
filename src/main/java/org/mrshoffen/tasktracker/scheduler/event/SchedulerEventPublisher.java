package org.mrshoffen.tasktracker.scheduler.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mrshoffen.tasktracker.commons.kafka.event.registration.RegistrationFailedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerEventPublisher {

    private final KafkaTemplate<UUID, Object> kafkaTemplate;

    public void publishRegistrationFailedEvent(String userId) {
        UUID uuid = UUID.fromString(userId);
        RegistrationFailedEvent event = new RegistrationFailedEvent(uuid);

        kafkaTemplate.send(RegistrationFailedEvent.TOPIC, event.getRegistrationId(), event);
        log.info("Event published to kafka topic '{}' - {}", RegistrationFailedEvent.TOPIC, event);
    }

}
