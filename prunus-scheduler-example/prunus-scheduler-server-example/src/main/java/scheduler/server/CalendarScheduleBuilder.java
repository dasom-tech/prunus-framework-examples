package scheduler.server;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.ScheduleBuilder;
import org.quartz.Trigger;
import org.springframework.stereotype.Component;
import prunus.scheduler.domain.UserDefinedScheduleBuilder;

import java.util.Map;

/**
 * @author 조용상
 */
@Component
public class CalendarScheduleBuilder implements UserDefinedScheduleBuilder {
    @Override
    public ScheduleBuilder<? extends Trigger> getScheduleBuilder(Map<String, Object> jobData) {
        return CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                .withIntervalInMinutes(1);
    }

    @Override
    public boolean supports(Map<String, Object> jobData) {
        return true;
    }
}
