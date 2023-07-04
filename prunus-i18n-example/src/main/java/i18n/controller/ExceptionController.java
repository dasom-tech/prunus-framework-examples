package i18n.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prunus.core.message.BaseException;

@RestController
@RequestMapping("/exception")
public class ExceptionController {

    /**
     * spring 기본 exception 메세지 포멧 출력
     */
    @GetMapping
    public void exception() {
        throw new RuntimeException("오류확인");
    }


    /**
     * prunus exception 메세지 포멧 출력
     */
    @GetMapping("/prunus")
    public void exceptionPrunus() {
        throw new BaseException("오류확인");
    }

}
