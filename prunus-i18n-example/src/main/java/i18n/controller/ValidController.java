package i18n.controller;

import i18n.dto.I18nReq;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import prunus.core.PrunusMessage;

import javax.validation.Valid;

@RestController
@RequestMapping("/valid")
public class ValidController {

    /**
     * I18nReq 객체의 유효성을 검사합니다.
     * @param i18nReq 요청정보
     * @return 요청정보
     */
    @PostMapping
    public I18nReq validationTest(@RequestBody @Valid I18nReq i18nReq) {
        // 유효성 검사를 통과하면 요청정보를 출력합니다.
        return i18nReq;
    }
}