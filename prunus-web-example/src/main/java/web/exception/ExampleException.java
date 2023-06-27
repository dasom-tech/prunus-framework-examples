package web.exception;

import prunus.core.message.BaseException;

public class ExampleException extends BaseException {

    public ExampleException(String code, Object[] parameters) {
        super(code, parameters);
    }
}
