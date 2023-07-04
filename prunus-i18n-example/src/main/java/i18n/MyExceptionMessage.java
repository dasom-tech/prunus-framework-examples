package i18n;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import prunus.web.exception.ExceptionMessage;

@Getter @Setter
@Component
public class MyExceptionMessage extends ExceptionMessage {

    private String uri;

    @Override
    protected void setExceptionMessage(WebRequest webRequest, Exception exception) {
        this.uri = ((ServletWebRequest) webRequest).getRequest().getRequestURI();
    }

    @Override
    protected boolean isIncludeDefaultAttribute() {
        return false;
    }
}
