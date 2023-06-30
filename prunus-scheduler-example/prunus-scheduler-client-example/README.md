# prunus-scheduler-client
**prunus-scheduler-client** 는 **prunus-scheduler-server** 와 통신하는 client 입니다.
prunus-scheduler-server 서버는 자체적으로 Job Schedule 의 생성, 수정, 삭제, 즉시 실행 등의 REST API 를 제공하지만
prunus-scheduler-server 서버는 독립적으로 실행되는 모듈로 보통 클라이언트와 다른 IP 또는 도메인을 갖게됩니다.

IP 또는 도메인이 다르다는 것은 다른 서버에 존재하는 클라이언트가 prunus-scheduler-server 의 REST API 에 접근할 때
**Cross-Origin Resource Sharing (CORS)** 가 발생할 수 있음을 의미합니다.

CORS 제약을 회피하게 위해 prunus-scheduler-client 모듈이 prunus-scheduler-server 의 REST API 와 통신을
대신하게 합니다.

prunus-scheduler-client 는 단순히 prunus-scheduler-server 의 클라이언트 기능 뿐아니라 다양한 기능을 제공합니다.

## Feature
- prunus-scheduler-server REST API 클라이언트
- Job API 로깅
- Job API 별 파일 로깅

## Setup
1. 사용자 프로젝트에 `prunus-scheduler-client` 모듈을 추가하세요.
```groovy
implementation 'sdcit.prunus:prunus-scheduler-client'
implementation 'org.springframework.boot:spring-boot-starter-web'
```
2. application.yml 설정
```yaml
prunus:
  scheduler:
    client:
      server: # prunus-scheduler-server REST API URI
        schema: http
        host: localhost
        port: 8080
        path: /schedule
    job:
      logging:
        path: /Users/yongsang/prunus/job # Job API 별 파일 로깅 경로
  web:
    content-caching:
      enabled: true # scheduler client 에서 HttpServletRequest body 를 읽으므로 web.content-caching.enabled 옵션을 활성화해야 합니다.
```
3. logback-spring.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xml>
<configuration>
    <!-- prunus-scheduler-client 가 제공하는 logback-job-base.xml 를 include 합니다. -->
    <include resource="logback-job-base.xml" />

    <!-- 프로젝트 환경에 맞는 로그 레벨을 정의하세요.  -->
    <logger name="prunus" level="DEBUG" />
    <logger name="scheduler.client" level="DEBUG" />
</configuration>
```

## Properties
|이름|설명|기본값|
|---|---|---|
|prunus.scheduler.client.path|scheduler client API url 패스|/schedule|
|prunus.scheduler.client.server.schema|REST API 서버 http or https 스키마| |
|prunus.scheduler.client.server.localhost|REST API 서버 도메인 or IP| |
|prunus.scheduler.client.server.port|REST API 서버 port| |
|prunus.scheduler.client.server.path|REST API 서버 path|/schedule|
|prunus.scheduler.job.logging.path|Job API 별 파일 로깅 경로| |

## REST API
prunus-scheduler-client 제공하는 REST API 는 prunus-scheduler-server 의 REST API 를 대신하는 것으로
기본적으로 prunus-scheduler-server 의 REST API 와 동일합니다.

REST API 의 자세한 내용은 prunus-scheduler-server 의 [REST API](../prunus-scheduler-server-example/README.md#REST API) 섹션을 참고 바랍니다.

## 구성 환경
prunus-scheduler-client 는 prunus-scheduler-server 의 REST API 클라이언트 역할을 하지만
prunus-scheduler-server 입장에서 봤을 때는 prunus-scheduler-server 가 호출할 Job API 는 prunus-scheduler-client
모듈이 포함된 프로젝트 입니다.

즉, client, server 모듈은 상황에 따라 각각 모듈의 클라이언트의 역할을 합니다.

prunus-scheduler-client 는 Job API 가 실행될 때 Job API 의 실행시간, 파라미터, 예외(Exception) 등의 정보를 기록합니다.
prunus-scheduler-client 가 프로젝트 내 다양한 REST API 들 중 어떤 것이 Job API 인지 식별하기 위해 
`prunus.scheduler.logging.annotation.JobApi` Annotation 을 제공합니다.

prunus-scheduler-server 에 의해 호출될 Job API 에 `@JobApi` Annotation 을 추가하면
prunus-scheduler-client 는 Job API 의 실행시간, 파라미터, 예외(Exception) 등의 정보를 기록하여
`prunus.scheduler.logging.JobLogEntryRecorder` 구현체에 전달합니다.

다음과 같이 일반적인 Spring Controller 에 `@JobApi` Annotation 을 추가하기만 하면 됩니다.
```java
@RestController
public class VacuumCleanerController {
    @JobApi
    @PostMapping("/cleanup")
    public String startVacuuming(@RequestBody User user) throws Exception {
        return "completed";
    }
}
```

## 인터페이스 클래스
- `prunus.scheduler.logging.JobLogEntryRecorder`: Job API 실행 결과 제공
- `prunus.scheduler.logging.file.JobLogFilenameCreator`: Job API 별 파일 로깅 시 로그 파일 이름 생성

### JobLogEntryRecorder
`JobLogEntryRecorder` 는 Spring Controller 에 `@JobApi` 를 식별해 Job API 의 실행시간, 파라미터, 예외(Exception) 등의
정보를 사용자가 직접 Handling 할 수 있게 기능을 제공하는 인터페이스 클래스입니다. 

JobLogEntryRecorder 는 프로젝트에서 Job API 의 로그를 데이터베이스 따로 기록 관리해야 할 경우 요긴하게 사용할 수 있습니다.
JobLogEntryRecorder 구현한 구현체는 **Spring Bean 으로 등록**해야 합니다.

```java
package prunus.scheduler.logging;

public interface JobLogEntryRecorder {
    /**
     * Job 이 시작될 때 호출
     *
     * @param entry Job 로그 항목
     * @param request Http 요청 객체
     */
    void onJobStarted(JobLogEntry entry, HttpServletRequest request);

    /**
     * Job 이 완료될 때 호출
     *
     * @param entry Job 로그 항목
     * @param request Http 요청 객체
     */
    void onJobFinished(JobLogEntry entry, HttpServletRequest request);
}
```
JobLogEntryRecorder 인터페이스에 전달되는`prunus.scheduler.logging.JobLogEntry` 클래스는 다음과 같은 정보를 담고 있습니다.
```java
package prunus.scheduler.logging;

public class JobLogEntry {
    // Job 식별 아이디
    private final JobId jobId;
    // Quartz 스케줄러에서 발행하는 고유한 실행 중인 Job 인스턴스 ID 
    private final String fireInstanceId;
    // Job 시작 시간
    private final LocalDateTime startTime;
    // Job 파라미터 (JobData 를 Http 파라미터로 표현)
    private final String jobParameter;
    // jobParameter 를 Map 으로 제공
    private final Map<String, Object> payload;
    // Job 별 로그 파일 위치
    private final String logFile;
    // Job 실행 성공 여부
    private boolean success;
    // Job 종료 시간
    private LocalDateTime endTime;
    // Job 실행 시 예외가 발생했다면 발생한 예외의 stack trace
    private String stackTrace;
}
```
JobLogEntry 객체를 통해 프로젝트 환경에 맞는 Job 로그 추적 시스템을 만들 수 있습니다. Job 별 로그 파일의 정보도 전달되므로 
실패로 끝난 Job 의 stack trace 도 사용자에게 보여줄 수 있는 환경도 구현 가능합니다.

## JobLogFilenameCreator
prunus-scheduler-client 는 Job API 마다 별도 파일로의 로깅을 지원합니다.
JobLogFilenameCreator 인터페이스 클래스는 Job 별 로그 파일 이름을 제공하는 인터페이스 클래스입니다.
prunus-scheduler-client 내부에는 이미 JobLogFilenameCreator 구현한 클래스가 있어 따로 구현할 필요는 없지만 
프로젝트만의 로그 파일 이름이 필요할 경우 인터페이스 클래스를 구현하고 **Spring Bean 으로 등록**하면 내부 구현체 대신
사용자가 구현한 클래스를 사용합니다.

```java
package prunus.scheduler.logging.file;

// prunus-scheduler-client 내부의 JobLogFilenameCreator 구현체
public class DefaultJobLogFilenameCreator implements JobLogFilenameCreator {
    private static final String DEFAULT_EXTENSION = ".log";

    @Override
    public String getName(JobId jobId, String fireInstanceId, Map<String, Object> payload) {
        return getBaseName(jobId, fireInstanceId, payload) + getExtension();
    }

    @Override
    public String getBaseName(JobId jobId, String fireInstanceId, Map<String, Object> payload) {
        return "job-" + jobId.getJobKey().getName() + "-" + fireInstanceId;
    }

    @Override
    public String getExtension() {
        return DEFAULT_EXTENSION;
    }
}
```
Job 은 그 성격상 사용자의 요청에 의해 실행되지 않고 Background 에서 스스로 특정 시간마다 실행되며
또 실행 시간이 오래 걸릴 수 있는 만큼 장애가 발생할 경우 장애 원인을 빠르게 파악하기 위해 로그를 충분히 남겨야 합니다.

Job 로그를 분석할 때 해당 Job 이외의 다른 로그가 썩여있다면 로그 분석이 어렵기 때문에 Job 별 로깅 기능을 사용하면 
좀 더 쉽게 로그를 분석할 수 있습니다.

Job 별 파일 로깅 기능을 활성화 하기 위해서는 위에서도 일부 설명 되었지만 application.yml 과 logback-spring.xml 이 필요합니다.

먼저 로그 파일이 남을 경로를 지정합니다.
```yaml
prunus:
  scheduler:
    job:
      logging:
        path: /Users/yongsang/prunus/job # Job 별 파일 로깅 경로
  web:
    content-caching:
      enabled: true # Job 로깅을 위해 HttpServletRequest body 를 여러번 읽어야 하므로 해당 옵션을 활성화해야 합니다.
```
다음으로 logback 설정을 위해 logback-spring.xml 에 아래 설정을 구성하세요.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xml>
<configuration>
    <!-- prunus-scheduler-client 가 제공하는 logback-job-base.xml 를 include 합니다. -->
    <include resource="logback-job-base.xml" />

    <!-- 프로젝트 환경에 맞는 로그 레벨을 정의하세요.  -->
    <logger name="prunus" level="DEBUG" />
    <logger name="scheduler.client" level="DEBUG" />
</configuration>
```
