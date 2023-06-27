package i18n.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prunus.core.message.MessageSourceHolder;

@RestController
@RequestMapping("/message")
public class MessageController {

    /**
     * messageSource에 등록된 다국어 메세지를 보여줌니다.
     * @return 메세지
     */
    @GetMapping
    public String getMessage() {
        // PrunusMessage 객체를 이용해서 messageSource에 접근할수 있습니다.
        return MessageSourceHolder.getMessage("error.test");
    }
}
