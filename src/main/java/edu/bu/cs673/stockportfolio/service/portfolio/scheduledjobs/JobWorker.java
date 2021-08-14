package edu.bu.cs673.stockportfolio.service.portfolio.scheduledjobs;

import org.fissore.slf4j.FluentLogger;
import org.fissore.slf4j.FluentLoggerFactory;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.stereotype.Service;

@Service
public class JobWorker {

    private static final FluentLogger LOGGER = FluentLoggerFactory.getLogger(JobWorker.class);
    private final ScheduledAnnotationBeanPostProcessor postProcessor;

    public JobWorker(ScheduledAnnotationBeanPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }

    public void startJob(JobConfiguration bean, String scheduledTaskName) {
        postProcessor.postProcessAfterInitialization(bean, scheduledTaskName);
    }

    public void stopJob(JobConfiguration bean, String scheduledTaskName) {
        postProcessor.postProcessBeforeDestruction(bean, scheduledTaskName);
    }
}
