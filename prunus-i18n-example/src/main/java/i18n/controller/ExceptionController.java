package i18n.controller;

import i18n.config.MyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prunus.core.PrunusMessage;

@RestController
@RequestMapping("/exception")
public class ExceptionController {

    /**
     * 사용자 예외 테스트
     */
    @GetMapping
    public String exceptionTest() {
        throw new MyException("예외 발생!!");
    }
}
