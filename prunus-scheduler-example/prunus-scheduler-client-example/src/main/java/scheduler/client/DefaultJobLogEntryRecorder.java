package scheduler.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import prunus.scheduler.logging.JobLogEntry;
import prunus.scheduler.logging.JobLogEntryRecorder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 조용상
 */
@Slf4j
@Component
public class DefaultJobLogEntryRecorder implements JobLogEntryRecorder {
    @Override
    public void onJobStarted(JobLogEntry entry, HttpServletRequest request) {
        // 프로젝트 환경에 맞게 로그 정보를 데이터베이스에 저장하세요...
        log.debug("JobLogEntry: {}", entry);
    }

    @Override
    public void onJobFinished(JobLogEntry entry, HttpServletRequest request) {
        // 프로젝트 환경에 맞게 로그 정보를 데이터베이스에 저장하세요...
        log.debug("JobLogEntry: {}", entry);
    }
}
