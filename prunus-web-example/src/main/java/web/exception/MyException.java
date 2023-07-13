package web.exception;

import prunus.core.message.BaseException;

import java.util.Locale;

public class MyException extends BaseException {

    public MyException(String code, Object[] parameters) {
        super(code, parameters);
    }
    public MyException(String code, Object[] parameters, Locale locale) {
        super(code, parameters, locale);
    }
}
