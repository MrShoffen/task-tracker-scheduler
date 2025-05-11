package org.mrshoffen.tasktracker.scheduler.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mrshoffen.tasktracker.commons.kafka.event.registration.RegistrationAttemptEvent;
import org.mrshoffen.tasktracker.commons.kafka.event.registration.RegistrationSuccessfulEvent;
import org.mrshoffen.tasktracker.scheduler.quartz.QuartzSchedulerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventListener {

    private final QuartzSchedulerService quartzScheduler;

    @KafkaListener(topics = RegistrationAttemptEvent.TOPIC)
    public void handleRegistrationAttempt(RegistrationAttemptEvent event) {
        log.info("Received event in topic {} - {}", RegistrationAttemptEvent.TOPIC, event);
        quartzScheduler.scheduleRegistrationExpiration(event);
    }

    @KafkaListener(topics = RegistrationSuccessfulEvent.TOPIC)
    public void handleRegistrationSuccess(RegistrationSuccessfulEvent event) {
        log.info("Received event in topic {} - {}", RegistrationSuccessfulEvent.TOPIC, event);
        quartzScheduler.cancelRegistrationExpiration(event.getRegistrationId());
  }
}
