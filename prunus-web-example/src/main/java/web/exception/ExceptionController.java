package web.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prunus.core.message.BaseException;
import prunus.core.message.I18nMessage;

import java.util.Locale;

@RestController
@RequestMapping("/exception")
public class ExceptionController {

    /**
     * spring 기본 exception 메세지 포멧 출력
     * prunus-framework를 통하지 않고 처리됨
     */
    @GetMapping
    public void exception() {
        throw new RuntimeException("오류확인");
    }

    /**
     * prunus exception 메세지 포멧 출력
     * prunus-web 적용시 controllerAdvice를 통해 ExceptionMessage객체로 출력
     */
    @GetMapping("/prunus")
    public void exceptionPrunus() {
        throw new BaseException("오류확인");
    }

    /**
     * prunus i18n exception 메세지 포멧 출력
     * messageSource를 통한 다국어 메세지
     */
    @GetMapping("/i18n")
    public void exceptionI18n() {
        throw new BaseException(new I18nMessage("prunus.web-example.error", new String[] {"테스트"}));
    }

    /**
     * 사용자 예외 발생
     * 로케일 'en' 고정
     */
    @GetMapping("/custom")
    public String exceptionCustom() {
        throw new MyException("prunus.web-example.occured-exception", new Object[]{}, Locale.ENGLISH);
    }
}
