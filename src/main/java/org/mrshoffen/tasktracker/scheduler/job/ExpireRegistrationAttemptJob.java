package org.mrshoffen.tasktracker.scheduler.job;

import org.mrshoffen.tasktracker.commons.kafka.event.registration.RegistrationFailedEvent;
import org.mrshoffen.tasktracker.scheduler.event.SchedulerEventPublisher;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class ExpireRegistrationAttemptJob implements Job {
    @Override
    public void execute(JobExecutionContext context){
        String userId = context.getJobDetail().getJobDataMap().getString("registrationId");
        SchedulerEventPublisher publisher = (SchedulerEventPublisher) context.getJobDetail().getJobDataMap().get("publisher");

        publisher.publishRegistrationFailedEvent(userId);
    }
}
