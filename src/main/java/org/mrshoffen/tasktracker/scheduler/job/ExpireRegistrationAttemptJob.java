package org.mrshoffen.tasktracker.scheduler.job;

import org.mrshoffen.tasktracker.scheduler.event.SchedulerEventPublisher;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class ExpireRegistrationAttemptJob implements Job {

    @Autowired
    private SchedulerEventPublisher eventPublisher;

    @Override
    public void execute(JobExecutionContext context) {
        String userId = context.getJobDetail().getJobDataMap().getString("registrationId");

        eventPublisher.publishRegistrationFailedEvent(userId);
    }
}
