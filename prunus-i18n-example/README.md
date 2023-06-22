# prunus-i18n
`prunus-i18n` 은 `spring`의 다국어 지원을 간편하게 사용할 수 있도록 지원합니다.
프로젝트에서 사용하는 `messageSource`와 `prunus-framework`에서 사용하는
`messageSource`를 통합하여 정상적인 메세지가 표시되도록 합니다.

`prunus-i18n` 은 `BaseException` 기본 예외객체를 제공하고 `@ControllerAdvice`를 통해 다국어 메세지를 처리합니다.

## Features
* MessageSource 통합
* PrunusMessage 제공
* Locale 변경 API 제공
* ControllerAdvice를 통한 메세지 처리
* spring validation 메세지 처리 가이드

## Properties
|이름|설명|기본값|
|---|---|---|
|prunus.i18n.default-locale|시스템에서 사용되는 기본 로케일|ko|
|prunus.i18n.locale.path|로케일 변경 경로|/locale|
|prunus.i18n.locale.parameter-name|로케일 변경 파라메터명|lang|
|prunus.i18n.locale-resolve-type|LocaleResolver 생성 타입(session,cookie,fixed,accept_header)|session|
|prunus.i18n.cookie-name|쿠키명(locale-resolve-type이 cookie인 경우)|prunus.LOCALE|
|prunus.i18n.cookie-max-age|쿠키 유효시간(단위:초, -1인 경우 브라우저를 닫을때 쿠키삭제)|-1|
|prunus.i18n.cookie-path|쿠키 경로(Path를 지정하면 해당하는 Path와 하위 Path에서 참조)|/|
|prunus.i18n.cookie-domain|쿠키 도메인||
|prunus.i18n.cookie-secure|쿠키 보안 여부|false|

## 설정
`prunus-i18n` 은 Spring Boot 에서 자동생성되는 messageSource를 기준으로 통합됩니다.
`spring.messages` 설정만으로 사용이 가능하며 `prunus.i18n` 설정과 조합하여
MessageSource, LocaleResolver, Locale API 가 자동 생성됩니다.
`messageSource`를 사용하지 않는 경우 `prunus-fromework`에서 사용하는 `messageSource`가 생성됩니다.

`perperties` 파일 사용시 아래와 같이 간단이 파일 위치만 지정하면 설정이 완료됩니다.
```yaml
spring:
  messages:
    basename: messages/message,messages/valid  # properties 파일 위치(validation 파일 포함)
```
`messageSource`는 spring 기본설정인 `ResourceBundleMessageSource`가 사용됩니다.
사용자가 직접 `messageSource`를 작성하는 경우 `AbstractResourceBasedMessageSource`를 상속한
Component를 등록해 주시면 됩니다.
```java
public class DatabaseMessageSource extends AbstractResourceBasedMessageSource {
  ...
}
or
public class DatabaseMessageSource extends ReloadableResourceBundleMessageSource {
  ...
}
or
public class DatabaseMessageSource extends ResourceBundleMessageSource {
  ...
}
```

## PrunusMessage
`PrunusMessage`는 static method를 통해 messageSource, messageSourceAccessor 주입 없이
바로 `messageSource`를 사용가능 하도록 합니다.
```
PrunusMessage.getMessage("prunus.error"));
PrunusMessage.getMessage("prunus.error", Locale.KOREA));
```

## Locale API
prunus.i18n.locale' 설정을 통해 로케일 변경 API 를 제공합니다.
### 로케일 변경(Default)
```http request
POST /locale
Content-Type: application/x-www-form-urlencoded
Parameters: lang
```
설정을 통해 endpoint, parameter-name을 변경할 수 있습니다.

## 예외객체
`prunus-framework`는 예외처리를 위한 `BaseException` 객체를 제공합니다.
`BaseException`은 `I18nMessage`를 포함하고 있어 다국어 정보를 포함할 수 있습니다.

사용자 예외객체가 필요한 경우 `BaseException`을 상속받아 생성할 수 있습니다.
```java
public class MyException extends BaseException {

    public MyException(String message) {
        super(message);
    }
}
```

### 인터페이스를 통한 예외처리 메세지 확장
예외 처리시 출력되는 메세지는 'ErrorMessageResolver' 인터페이스를 통해 진행됩니다.
```java
public interface ErrorMessageResolver {
  /**
   * 에러 메시지를 해결
   *
   * @param error 예외 객체
   * @return 에러 메시지
   */
  String resolveErrorMessage(WebRequest webRequest, Throwable error);

  /**
   * 매개 변수인 예외 객체를 통해 구현할 객체가 에러 메시지를
   * 처리할 수 있는 여부
   *
   * @param error 예외 객체
   * @return 처리 여부
   */
  boolean supports(Throwable error);
}
```
사용자가 `ErrorMessageResolver`를 구현하지 않는 경우 `prunus-i18n` 내에 있는 `BaseErrorMessageResolver`가 대신합니다.
예외객체의 다국어 정보를 출력하고 없는 경우 예외객체의 기본 메세지를 출력합니다.

사용자 객체를 추가하고 싶은 경우 아래와 같이 `ErrorMessageResolver`를 상속받은 Component를 작성합니다.
```java
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyErrorMessageResolver implements ErrorMessageResolver {
  @Override
  public String resolveErrorMessage(WebRequest webRequest, Throwable error) {
    return error.getMessage() + "(에러 메세지를 수정할 수 있습니다.)";
  }

  @Override
  public boolean supports(Throwable error) {
    return error instanceof MyException;
  }
}
```
작성한 `supports()` 메소드의 기준에 포함되면 해당 객체에서 메세지를 처리하고
그렇치 않으면 `BaseErrorMessageResolver`에서 처리됩니다.
여러 `ErrorMessageResolver` 구현체가 있는 경우 순서대로 처리하므로
`@Order` Annotation 으로 순서를 지정해야 합니다. 

## 응답 메세지
`prunus-i18n`은 `application/json` 요청의 오류응답을 아래와 같은 형태로 표시합니다.
```json
{
    "message": "오류메세지",
    "dateTime": "2011-06-22T13:14:39.7505613",
    "requestUri": "/valid",
    "requestMethod": "POST"
}
```
응답 메세지 형태를 변경하고 싶은 경우 `ErrorObject`를 상속한 Component를 작성합니다.
`ErrorObject`는 `ErrorMessageResolver`에서 처리되는 에러메세지를 포함하고 있고
`createErrorObject()`메소드 `Override`가 필요합니다. 아래 소스를 참고 바랍니다.
```java
@Getter
@Component
public class MyErrorObject extends ErrorObject {
    private String uri;

    @Override
    protected void createErrorMessageObject(WebRequest webRequest, Exception exception) {
        this.uri = ((ServletWebRequest) webRequest).getRequest().getRequestURI();
    }
}

```

## spring-validation 메세지 처리 가이드
`prunus-i18n`은 `spring-boot-starter-validation`을 포함하고 있습니다.
### 유효성검사의 메세지는 `{}`를 이용하여 messageSource에 접근할 수 있습니다.
```
@NotNull(message = "{prunus.i18n.valid.NotNll}")
@Size(min = 1, max = 3, message = "{prunus.i18n.valid.Size}")
```
### 유효성검사의 기본 메세지 변경 방법
* Resource 폴더에 validationMessages.properties를 생성합니다.
* validationMessages.properties에 변경하고 싶은 코드를 작성합니다.
* hibernate-validator에서 제공하는 메세지 코드는 아래와 같습니다.
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
`prunus-i18n` 에는 spring의 `ResponseEntityExceptionHandler`를 상속 받아 에러를 처리하는 `@ControllerAdvice` 가 있습니다. 
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