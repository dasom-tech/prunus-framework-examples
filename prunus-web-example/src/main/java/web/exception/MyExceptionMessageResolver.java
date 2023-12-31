package web.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import prunus.web.exception.ExceptionMessageResolver;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class MyExceptionMessageResolver implements ExceptionMessageResolver {
    @Override
    public String resolve(WebRequest webRequest, Throwable error) {
        return error.getMessage() + "(에러 메세지를 수정할 수 있습니다)";
    }

    @Override
    public boolean supports(Throwable error) {
        return error instanceof MyException;
    }
}
