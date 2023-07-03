package scheduler.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prunus.scheduler.logging.annotation.JobApi;

/**
 * @author 조용상
 */
@RestController
@Slf4j
public class VacuumCleanerController {
    @JobApi
    @PostMapping("/cleanup")
    public String startVacuuming(@RequestBody User user) throws Exception {
        log.info("cleaning...");
        Thread.sleep(7000);
        log.info("completed...");
        return "completed";
    }
}
