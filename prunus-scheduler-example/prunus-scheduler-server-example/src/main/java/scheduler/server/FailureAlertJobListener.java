package scheduler.server;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

/**
 * @author 조용상
 */
@Component
public class FailureAlertJobListener implements JobListener {
    @Override
    public String getName() {
        return "FailureAlertJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        if (jobException != null) {
            String stackTrace = ExceptionUtils.getStackTrace(jobException);
            System.out.println("stackTrace = " + stackTrace);
        }
    }
}
