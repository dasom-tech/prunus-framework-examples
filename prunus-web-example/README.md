# prunus-web
`prunus-web`은 `@ControllerAdvice`를 통해 `BaseException` 및 `RequestMapping`에 대한 Valid Exception 처리를 포함하고 있습니다.
오류 응답에 대한 메세지 정보나 포멧 변경을 위한 `ExceptionMessageResolver` 인터페이스와 `ExceptionMessage` 추상클래스를 제공합니다.

## Features
* `@ControllerAdvice`를 통한 메세지 처리
* spring validation 메세지 처리 가이드

## 예외객체
`prunus-framework`은 예외처리를 위한 `BaseException` 객체를 제공합니다.
`BaseException`은 다국어정보(`I18nMessage`)를 포함하고 있어 `messageSource`와 연결됩니다.
사용자 예외객체가 필요한 경우 `BaseException`을 상속받아 생성할 수 있습니다.
```java
public class MyException extends BaseException {

    public MyException(String message) {
        super(message);
    }
}
```
### 인터페이스를 통한 예외처리 메세지 확장
예외 처리시 출력되는 메세지는 `ExceptionMessageResolver` 인터페이스를 통해 진행됩니다.
`ExceptionMessageResolver`는 여러개가 등록될수 있고 사용자 객체를 추가하지 않으면 기본으로 제공되는 `I18nMessageResolver`에서 처리됩니다.
인터페이스 내용은 아래와 같습니다. 
```java
public interface ExceptionMessageResolver {
  /**
   * 에러 메시지를 해결
   *
   * @param error 예외 객체
   * @return 에러 메시지
   */
  String resolve(WebRequest webRequest, Throwable error);

  /**
   * 매개 변수인 예외 객체를 통해 구현할 객체가 에러 메시지를
   * 처리할 수 있는지 판단
   *
   * @param error 예외 객체
   * @return 처리 여부
   */
  boolean supports(Throwable error);
}
```
### 사용자 예외객체
사용자 객체를 추가하고 싶은 경우 아래와 같이 `ExceptionMessageResolver`를 상속받은 `@Component`를 작성합니다.
```java
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyErrorMessageResolver implements ExceptionMessageResolver {
  @Override
  public String resolve(WebRequest webRequest, Throwable error) {
    return error.getMessage() + "(에러 메세지를 수정할 수 있습니다.)";
  }

  @Override
  public boolean supports(Throwable error) {
    return error instanceof MyException;
  }
}
```
작성한 `supports` 메소드의 기준에 포함되면 해당 객체에서 메세지를 처리하고
여러개의 `ExceptionMessageResolver` 구현체가 있는 경우 정의된 순서대로 `supports` 메소드를 검증합니다.
`@Order` Annotation 으로 순서를 지정할 수 있습니다. 

## 응답 메세지
`prunus-web`에서는 `text/html` 요청을 제외한 오류응답을 아래와 같은 형태로 표시합니다.
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
응답 메세지는 `server.error` properties 속성에 따라 달라지며 이는 `Spring`의 설정과 포멧이 동일합니다.
```json
server:
  error:
    include-exception: true
    include-message: always
    include-binding-errors: always
    include-stacktrace: always
```
### 응답 메세지 포맷 변경
메세지 형태를 변경하고 싶은 경우 `ExceptionMessage`를 상속한 `@Component`를 작성합니다.
`ExceptionMessage`는 위에서 설명한 `ExceptionMessageResolver` 인터페이스에서 처리되는 에러메세지를 포함하고 있고
`setExceptionMessage`메소드의 Override가 필요합니다. 아래 소스를 참고 바랍니다.
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
`isIncludeDefaultAttribute` 메소드를 `false`로 Override 하는 경우 `server.error` properties 속성과 관계없이
`ExceptionMessage`가 가지고 있는 `message`를 제외한 모든 항목의 출력 삭제됩니다.
위와 같이 `MyExceptionMessage`를 설정한 경우 오류메세지는 다음과 같이 출력됩니다.
```json
{
    "message": "오류메세지",
    "uri": "/exception"
}
```

## spring-validation 메세지 처리 가이드
`prunus-web`은 `spring-boot-starter-validation`을 포함하고 있습니다.
### 사용자 메세지 설정
유효성검사의 사용자 메세지를 설정할 경우 `{}`를 이용하여 `messageSource`에 접근할 수 있습니다.
```
@NotNull(message = "{prunus.i18n.valid.NotNll}")
@Size(min = 1, max = 3, message = "{prunus.i18n.valid.Size}")
```
### 기본 메세지 변경 방법
`@NotNull` 등의 유효성검사 Annotation의 기본메세지는 `hibernate-validator` 라이브러리에서 제공됩니다.
기본메세지를 변경하고 싶은 경우 아래 내용을 참고 바랍니다.
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

## 주의 사항
### @ControllerAdvice
`prunus-i18n` 에는 Spring의 `ResponseEntityExceptionHandler`를 상속 받아 에러를 처리하는 `@ControllerAdvice` 가 있습니다. 
이 `@ControllerAdvice` 는 `BaseException` 및 `RequestMapping`에 대한 Valid Exception 처리를 포함하고 있습니다. 
만약 `prunus-i18n` 이 처리하는 예외를 프로젝트 내부에서 다른 방식으로 처리하고자 원한다면
사용자 정의 `@ControllerAdvice` 생성하고 `@Order` Annotation 으로 `Ordered.LOWEST_PRECEDENCE` 보다 작은
수를 정의하여 `prunus-i18n` 의 `@ControllerAdvice` 보다 우선 순위를 높여 먼저 처리되게 할 수 있습니다. 
이렇게 하면 기본 에러 처리대신 사용자 정의 에러 처리가 가능합니다.

`prunus-i18n`에서 처리되는 예외는 `BaseException`과 아래 Validation 관련 예외입니다.
```
Valid Exception List
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

## Test Client
`prunus-i18n` 의 테스트를 위해서는 다음 절차를 통해 테스트 해 볼 수 있습니다.
* `prunus-i18n-example` 을 실행합니다.
  * 보통의 spring boot 의 application 실행 방법과 동일합니다. 
* 본인의 환경에 맞는 [Postman](https://www.postman.com/downloads/) 을 다운로드 및 설치합니다.
* **client** 디렉토리의 `prunus-i18n.postman_collection.json` 파일을 Postman 에서 Import 하여 테스트 합니다.