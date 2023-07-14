# prunus-i18n
`prunus-i18n`은 `Spring`의 다국어 지원을 간편하게 사용할 수 있도록 `messageSource`, `localeResolver`등의 자동구성을 제공합니다.
사용자가 `messageSource`를 등록할 경우 `prunus-framework`에서 사용하는 `messageSource`와 자동으로 통합되며
`MessageSourceHolder` 객체를 통해 편하게 사용할 수 있습니다.

## Features
* MessageSource 통합
* MessageSourceHolder
* Locale 변경 API 제공

## Properties
|이름|설명|기본값|
|---|---|---|
|prunus.i18n.default-locale|시스템에서 사용되는 기본 로케일|ko|
|prunus.i18n.locale.path|로케일 변경 API 경로|/locale|
|prunus.i18n.locale.parameter-name|로케일 변경 파라메터명|lang|
|prunus.i18n.locale-resolve-type|LocaleResolver 생성 타입(session,cookie,fixed,accept_header)|session|
|prunus.i18n.cookie-name|쿠키명(locale-resolve-type이 cookie인 경우)|prunus.LOCALE|
|prunus.i18n.cookie-max-age|쿠키 유효시간(단위:초, -1인 경우 브라우저를 닫을때 쿠키삭제)|-1|
|prunus.i18n.cookie-path|쿠키 경로(Path를 지정하면 해당하는 Path와 하위 Path에서 참조)|/|
|prunus.i18n.cookie-domain|쿠키 도메인||
|prunus.i18n.cookie-secure|쿠키 보안 여부|false|

## 구성방법
`prunus-i18n`은 `Spring Boot`에서 자동생성되는 `messageSource`를 기준으로 통합되며
`messageSource`로 사용할 소스파일(properties) 위치만 `spring.messages.basename`에 지정하면 바로 사용이 가능합니다.
자동구성을 통해 `MessageSourceHolder`, `ResourceBundleMessageSource`, `SessionLocaleResolver`, `I18nController`등이 bean으로 등록됩니다.
```yaml
spring:
  messages:
    basename: messages/message,messages/valid  # properties 파일 위치(validation 파일 포함)
```
### MessageSource 변경
`messageSource`는 spring 기본설정인 `ResourceBundleMessageSource`가 사용됩니다.
`properties`타입 이외의 다른 유형을 사용할 경우 아래와 같은 형태로 `messageSource`를 구현하고
`@Component` Bean으로 등록하여 사용할 수 있습니다.
```java
@Component
public class DatabaseMessageSource extends AbstractResourceBasedMessageSource {
  ...
}
or
@Component
public class DatabaseMessageSource extends ReloadableResourceBundleMessageSource {
  ...
}
or
@Component
public class DatabaseMessageSource extends ResourceBundleMessageSource {
  ...
}
```
### LocaleResolver 변경
`Spring Boot`에서는 4가지(`FixedLocaleResolver`, `AcceptHeaderLocaleResolver`, `SessionLocaleResolver`, `CookieLocaleResolver`)
`localeResolver`를 제공하고 `prunus-i18n` 기본구성으로 `SessionLocaleResolver`가 등록됩니다.
`prunus.i18n.locale-resolve-type` properties 설정으로 변경이 가능합니다.

### Locale 변경 API
사용자의 로케일 변경 API 를 제공합니다.
`prunus` properties 설정을 통해 `endpoint`, `parameter-name`을 변경할 수 있습니다.
기본 설정은 자동구성된 경우 아래와 같은 요청으로 `locale`을 변경할 수 있습니다.
```http request
POST /locale
Content-Type: application/json
body: { "lang" : "ko" }
```

## MessageSourceHolder
코드작성시 `prunus-i18n`에서 제공하는 `MessageSourceHolder`의 static method를 통해
`messageSource`나 `messageSourceAccessor` 주입 없이 사용이 가능합니다.
```java
MessageSourceHolder.getMessage("prunus.error"));
MessageSourceHolder.getMessage("prunus.error", new Object[]{}, Locale.KOREA));
```

## Test Client
`prunus-i18n` 의 테스트를 위해서는 다음 절차를 통해 테스트 해 볼 수 있습니다.
* `prunus-i18n-example` 을 실행합니다.
  * 보통의 spring boot 의 application 실행 방법과 동일합니다. 
* 본인의 환경에 맞는 [Postman](https://www.postman.com/downloads/) 을 다운로드 및 설치합니다.
* **client** 디렉토리의 `prunus-i18n.postman_collection.json` 파일을 Postman 에서 Import 하여 테스트 합니다.