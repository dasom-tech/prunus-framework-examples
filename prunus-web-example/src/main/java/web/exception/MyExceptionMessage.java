package web.exception;

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
        /**
         * Default : true
         * true 설정시 Spring 설정(application.yml의 server.error)에 따라 메세지 표시
         * false 설정시 Spring 설정을 무시하고 message만 표시
         */
        return false;
    }
}
