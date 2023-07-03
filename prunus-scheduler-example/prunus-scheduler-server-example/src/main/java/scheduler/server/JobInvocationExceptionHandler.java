package scheduler.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import prunus.scheduler.server.job.RestClientExceptionHandler;

/**
 * @author 조용상
 */
@Component
@Slf4j
public class JobInvocationExceptionHandler implements RestClientExceptionHandler {
    @Override
    public void handleException(HttpStatus status, HttpHeaders headers, String body) {
        log.error("JobInvocationException statusCode: {}, headers: {}, body: {}", status.value(), headers, body);
    }

    @Override
    public void handleException(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
    }
}
