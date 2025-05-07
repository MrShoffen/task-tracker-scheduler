package org.mrshoffen.tasktracker.scheduler.quartz;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mrshoffen.tasktracker.commons.kafka.event.registration.RegistrationAttemptEvent;
import org.mrshoffen.tasktracker.scheduler.event.SchedulerEventPublisher;
import org.mrshoffen.tasktracker.scheduler.job.ExpireRegistrationAttemptJob;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuartzSchedulerService {

    private final Scheduler scheduler;

    private final SchedulerEventPublisher eventPublisher;


    @SneakyThrows
    public void scheduleRegistrationExpiration(RegistrationAttemptEvent event) {
        JobDetail job = JobBuilder.newJob(ExpireRegistrationAttemptJob.class)
                .withIdentity("registrationExpiration_" + event.getRegistrationId().toString())
                .usingJobData("registrationId", event.getRegistrationId().toString())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(Date.from(event.getValidUntil()))
                .build();

        log.info("Planning to delete unconfirmed registration with id {} in {}", event.getRegistrationId().toString(), event.getValidUntil());

        scheduler.scheduleJob(job, trigger);
    }

    @SneakyThrows
    public void cancelRegistrationExpiration(UUID userId) {
        scheduler.deleteJob(new JobKey("registrationExpiration_" + userId.toString()));
    }


}
