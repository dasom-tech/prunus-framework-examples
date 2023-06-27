package web.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class ExceptionController {

    /**
     * 사용자 예외 발생
     */
    @GetMapping
    public String exception() {
        throw new ExampleException("prunus.web-example.occured-exception", new Object[]{});
    }
}
