package i18n.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prunus.core.message.BaseException;
import prunus.core.message.I18nMessage;

@RestController
@RequestMapping("/exception")
public class ExceptionController {

    /**
     * prunus i18n exception 메세지 포멧 출력
     * messageSource를 통한 다국어 메세지
     */
    @GetMapping
    public void exceptionI18n() {
        throw new BaseException(new I18nMessage("error.test", new String[] {"테스트"}));
    }

}
