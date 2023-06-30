package scheduler.server;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import prunus.scheduler.server.job.AuthHeader;

import java.util.Map;

/**
 * @author 조용상
 */
@Component
public class JwtAuthHeader implements AuthHeader {
    @Override
    public HttpHeaders getAuthHeader(Map<String, Object> jobData) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        return headers;
    }
}
