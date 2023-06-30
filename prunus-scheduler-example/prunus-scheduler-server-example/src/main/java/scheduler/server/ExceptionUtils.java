package scheduler.server;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author 조용상
 */
public class ExceptionUtils {
    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
