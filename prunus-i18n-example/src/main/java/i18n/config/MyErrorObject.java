package i18n.config;

import lombok.Getter;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import prunus.i18n.web.ErrorObject;

/**
 * 에러 응답 메세지를 변경하는 경우 작성
 * @Component 어노테이션을 선언하면 적용됨.(현재는 기본 응답 메세지 설정)
 */
@Getter
public class MyErrorObject extends ErrorObject {
    private String uri;

    @Override
    protected void createErrorMessageObject(WebRequest webRequest, Exception exception) {
        this.uri = ((ServletWebRequest) webRequest).getRequest().getRequestURI();
    }
}
