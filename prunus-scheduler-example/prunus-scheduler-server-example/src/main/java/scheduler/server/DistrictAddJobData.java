package scheduler.server;

import org.springframework.stereotype.Component;
import prunus.scheduler.domain.JobId;
import prunus.scheduler.server.job.AddableJobData;

import java.util.Map;

/**
 * @author 조용상
 */
@Component
public class DistrictAddJobData implements AddableJobData {
    @Override
    public void add(JobId jobId, Map<String, Object> jobData) {
        jobData.put("district", "suji-gu");
    }
}
