package edu.bu.cs673.stockportfolio.service.portfolio.scheduledjobs;

import org.fissore.slf4j.FluentLogger;
import org.fissore.slf4j.FluentLoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class JobDaemonConfiguration implements JobConfiguration {

    private static final FluentLogger LOGGER = FluentLoggerFactory.getLogger(JobDaemonConfiguration.class);
    private final JobManager jobManager;

    public JobDaemonConfiguration(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    /**
     * Configures the schedule for checking if the US Market is open.
     */
    @Override
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void scheduleByFixedRate() {
        LOGGER.info().log("Configuring the market data job schedule");
        jobManager.startSchedule();
    }
}