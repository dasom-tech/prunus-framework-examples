package web.exception;

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
public class BControllerAdvice extends MessageResolvedResponseEntityExceptionHandler implements Ordered {
    public BControllerAdvice(List<ExceptionMessageResolver> messageResolvers, BasicErrorController basicErrorController, ExceptionMessage errorMessageObject) {
        super(basicErrorController, messageResolvers, errorMessageObject);
    }
    @ExceptionHandler({BaseException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleErrorException(BaseException exception, WebRequest request) {
        ExceptionMessage exceptionMessage = getExceptionMessage(request, exception);
        return this.isJsonType(request) ? handleExceptionInternal(exception, exceptionMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request) :
                createExceptionView(request, exceptionMessage.getMessage());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

