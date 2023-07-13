# prunus-web
`prunus-web`은 `@ControllerAdvice`를 이용하여 오류메세지에 대한 내용과 형태를 관리 합니다.
`prunus-web`에서 관리되는 `ExceptionHandler` 범위는 `BaseException`을 상속한 예외객체와 `@RequestMapping`과 관련된 예외객체들 입니다.
Project에서 오류메세지에 대한 내용을 변경하고 싶은 경우 제공되는 `ExceptionMessageResolver`와 `ExceptionMessage`인터페이스를 이용하여 변경할 수 있습니다.

## @RequestMapping 관련 Exception
```text
- ConstraintViolationException.class
- HttpRequestMethodNotSupportedException.class
- HttpMediaTypeNotSupportedException.class
- HttpMediaTypeNotAcceptableException.class
- MissingPathVariableException.class
- MissingServletRequestParameterException.class
- ServletRequestBindingException.class
- ConversionNotSupportedException.class
- TypeMismatchException.class
- HttpMessageNotReadableException.class
- HttpMessageNotWritableException.class
- MethodArgumentNotValidException.class
- MissingServletRequestPartException.class
- BindException.class
- NoHandlerFoundException.class
- AsyncRequestTimeoutException.class
```

## BaseException
`BaseException`은 `prunus-framework`에서 제공하는 기본예외객체 입니다.
`I18nMessage`객체를 포함하고 있어 오류 메세지에 대한 다국어처리가 가능하고
사용자 예외객체를 작성할 경우 `BaseException`객체를 상속받아 `prunus-web`의 오류응답 대상에 포함시킬 수 있습니다.
```java
public class MyException extends BaseException {

    public MyException(String message) {
        super(message);
    }
}
```

## 오류 메세지
`prunus-web`의 기본 오류메세지는 `Spring`과 동일합니다.
`server.error` properties 속성에 따라 아래와 같은 형태로 표시합니다.
```json
{
  "timestamp": "2023-07-04T00:25:36.567+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "exception": "java.lang.RuntimeException",
  "trace": "java.lang.RuntimeException: ...",
  "message": "오류메세지",
  "path": "/exception"
}
```
### server.error properties 속성
```properties
server:
  error:
    include-exception: true  # default: false
    include-message: always  # default: never
    include-binding-errors: always  # default: never
    include-stacktrace: never  # default: never
```
## 오류 메세지 형태 변경
메세지 형태를 변경하고 싶은 경우 `ExceptionMessage`를 상속한 `@Component` Bean 객체를 생성합니다.
생성한 객체의 맴버변수가 오류메세지 항목에 포함되고 `setExceptionMessage`메소드를 반드시 `Override`하여 맴버변수 값을 설정해 주어야 합니다.
`ExceptionMessage`가 가지고 있는 멤버변수를 응답메세지에서 제외하고 싶은 경우 `isIncludeDefaultAttribute`메소드 리턴값을 `false`로 `Override`하면
message 항목을 제외한 모든 항목이 응답메세지에서 표시되지 않습니다. 이때 Spring의 `server.error` properties 속성은 무시됩니다.
```java
@Getter @Setter
@Component
public class MyExceptionMessage extends ExceptionMessage {

  private String uri;

  @Override
  protected void setExceptionMessage(WebRequest webRequest, Exception exception) {
    this.uri = ((ServletWebRequest) webRequest).getRequest().getRequestURI();
  }

  @Override
  protected boolean isIncludeDefaultAttribute() {
    return false;
  }
}
```
위와 같이 `MyExceptionMessage` Bean객체를 생성하면 오류메세지는 아래와 같은 형태로 출력됩니다.
```json
{
    "message": "오류메세지",
    "uri": "/exception"
}
```

## 오류메세지의 "message" text 변경
오류메세지의 "message" 항목은 `BaseException`객체를 기준으로 해당 예외객체의 "message"내용이 표시되고
내용을 변경하고 싶은 경우 `ExceptionMessageResolver`인터페이스를 통해 처리가 가능합니다.
`ExceptionMessageResolver`의 경우 여러개 등록이 가능하고 해당 객체의 `supports` 메소드를 `Override`하여 처리가능한 조건을 설정합니다.
여러개의 `Resolver`이 있는 경우 `@Order` Annotation에 의해 우선순위가 결정되고 `supports` 메소드 조건에 만족하면 나머지는 무시됩니다.
아래 예제는 `MyException`이라는 사용자예외를 등록하고 해당 예외가 발생한 경우 출력되는 메세지를 변경하는 부분입니다.
```java
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class MyExceptionMessageResolver implements ExceptionMessageResolver {
    @Override
    public String resolve(WebRequest webRequest, Throwable error) {
        return error.getMessage() + "(에러 메세지를 수정할 수 있습니다)";
    }

    @Override
    public boolean supports(Throwable error) {
        return error instanceof MyException;
    }
}
```

## 주의 사항
### @ControllerAdvice
`prunus-web`의 `@ControllerAdvice`는 `BaseException` 및 `@RequestMapping`에 대한 `Exception` 메세지 처리를 포함하고 있습니다.
만약 `prunus-web`에서 처리하는 예외를 프로젝트 내부에서 다른 방식으로 처리하고자 원한다면
사용자 정의 `@ControllerAdvice` 생성하고 `@Order` Annotation 으로 우선 순위를 높여 먼저 처리되게 할 수 있습니다.
이렇게 하면 기본 에러 처리대신 사용자 정의 에러 처리가 가능합니다.

## Test Client
`prunus-i18n` 의 테스트를 위해서는 다음 절차를 통해 테스트 해 볼 수 있습니다.
* `prunus-i18n-example` 을 실행합니다.
  * 보통의 spring boot 의 application 실행 방법과 동일합니다.
* 본인의 환경에 맞는 [Postman](https://www.postman.com/downloads/) 을 다운로드 및 설치합니다.
* **client** 디렉토리의 `prunus-i18n.postman_collection.json` 파일을 Postman 에서 Import 하여 테스트 합니다.


# spring-validation 메세지 처리 가이드
`prunus-web`은 `spring-boot-starter-validation`을 포함하고 있습니다.
## 사용자 메세지 설정
유효성검사의 사용자 메세지를 설정할 경우 `{}`를 이용하여 `messageSource`에 접근할 수 있습니다.
```
@NotNull(message = "{prunus.i18n.valid.NotNll}")
@Size(min = 1, max = 3, message = "{prunus.i18n.valid.Size}")
```
## 기본 메세지 변경 방법
`@NotNull` 등의 유효성검사 `Annotation`의 기본메세지는 `hibernate-validator`라이브러리에서 제공되고 변경하고 싶은 경우 아래 내용을 참고 바랍니다.
* Resource 폴더에 ValidationMessages.properties를 생성합니다.
* ValidationMessages.properties에 변경하고 싶은 코드를 작성합니다.
* hibernate-validator에서 제공하는 전체 메세지 코드는 아래와 같습니다.
```
javax.validation.constraints.AssertFalse.message
javax.validation.constraints.AssertTrue.message
javax.validation.constraints.DecimalMax.message
javax.validation.constraints.DecimalMin.message
javax.validation.constraints.Digits.message
javax.validation.constraints.Email.message
javax.validation.constraints.Future.message
javax.validation.constraints.FutureOrPresent.message
javax.validation.constraints.Max.message
javax.validation.constraints.Min.message
javax.validation.constraints.Negative.message
javax.validation.constraints.NegativeOrZero.message
javax.validation.constraints.NotBlank.message
javax.validation.constraints.NotEmpty.message
javax.validation.constraints.NotNull.message
javax.validation.constraints.Null.message
javax.validation.constraints.Past.message
javax.validation.constraints.PastOrPresent.message
javax.validation.constraints.Pattern.message
javax.validation.constraints.Positive.message
javax.validation.constraints.PositiveOrZero.message
javax.validation.constraints.Size.message

org.hibernate.validator.constraints.CreditCardNumber.message
org.hibernate.validator.constraints.Currency.message
org.hibernate.validator.constraints.EAN.message
org.hibernate.validator.constraints.Email.message
org.hibernate.validator.constraints.ISBN.message
org.hibernate.validator.constraints.Length.message
org.hibernate.validator.constraints.CodePointLength.message
org.hibernate.validator.constraints.LuhnCheck.message
org.hibernate.validator.constraints.Mod10Check.message
org.hibernate.validator.constraints.Mod11Check.message
org.hibernate.validator.constraints.ModCheck.message
org.hibernate.validator.constraints.NotBlank.message
org.hibernate.validator.constraints.NotEmpty.message
org.hibernate.validator.constraints.ParametersScriptAssert.message
org.hibernate.validator.constraints.Range.message
org.hibernate.validator.constraints.ScriptAssert.message
org.hibernate.validator.constraints.UniqueElements.message
org.hibernate.validator.constraints.URL.message

org.hibernate.validator.constraints.time.DurationMax.message
org.hibernate.validator.constraints.time.DurationMin.message
```