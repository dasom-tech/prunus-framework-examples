package web.exception;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import prunus.core.message.BaseException;
import prunus.web.exception.ExceptionMessage;
import prunus.web.exception.ExceptionMessageResolver;
import prunus.web.exception.MessageResolvedResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class AControllerAdvice extends MessageResolvedResponseEntityExceptionHandler implements Ordered {
    public AControllerAdvice(List<ExceptionMessageResolver> messageResolvers, BasicErrorController basicErrorController, ExceptionMessage errorMessageObject, ServerProperties serverProperties) {
        super(basicErrorController, messageResolvers, errorMessageObject, serverProperties);
    }
    @ExceptionHandler({BaseException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleErrorException(BaseException exception, WebRequest request) {
        ExceptionMessage exceptionMessage = getExceptionMessage(request, exception, HttpStatus.INTERNAL_SERVER_ERROR);
        return this.isHtmlType(request) ? createExceptionView(request, exceptionMessage.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR) :
                handleExceptionInternal(exception, exceptionMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

