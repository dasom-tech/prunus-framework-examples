# prunus-scheduler-server
**prunus-scheduler-server** 는 **Quartz Job Scheduler** 를 기반으로 확장한 잡 스케줄러 서버입니다.
prunus-scheduler-server 는 Quartz Job Scheduler 을 확장한 서버이므로 당연하게도 Quartz Job Scheduler Job 데이터를 영속적으로 관리하기 
위한 Quartz 테이블이 필요합니다.

Quartz Job Scheduler 의 관리 테이블 생성 스크립트는 `quartz-[version].jar` 내 `org.quartz.impl.jdbcjobstore` 패키지 내
데이터베이스 Vendor 별로 제공하므로 해당 스크립트를 실행하여 테이블을 먼저 생성해줘야 합니다.

## Feature
- Job Schedule 생성, 수정, 삭제
- Job Schedule 즉시 실행
- 실시간 Scheduling 변경
- 원격 Job 호출 (단, prunus-scheduler-client 모듈 사용)
- 원격 Job 에러 Handling

## Setup
1. Quartz Job Scheduler   
   Quartz Job Scheduler 의 관리 테이블을 생성해주세요.
2. prunus-scheduler-server 모듈 의존   
   사용자 프로젝트에 `prunus-scheduler-server` 을 추가하세요.
```groovy
implementation 'sdcit.prunus:prunus-scheduler-server'
```
3. application.yml Quartz 설정   
   기본적인 Quartz 관련 설정을 다음과 같이 사용자 환경에 맞게 설정합니다. 
```yaml
spring:
   datasource:
      driver-class-name: org.h2.Driver # 사용자 환경에 맞게 수정
      url: jdbc:h2:tcp://localhost/~/prunus/db/scheduler-server # 사용자 환경에 맞게 수정
      username: sa
   quartz:
      job-store-type: jdbc # Quartz의 JobStore 유형
      jdbc:
         initialize-schema: never # 데이터베이스 스키마 초기화 여부 (always, never, embedded)
      properties:
         org.quartz.jobStore.class: org.springframework.scheduling.quartz.LocalDataSourceJobStore # JobStore 클래스 (LocalDataSourceJobStore 사용)
```

## Properties
|이름|설명|기본값|
|---|---|---|
|prunus.scheduler.server.path|scheduler server api url 패스|/scheduler|

## 구성 환경
prunus-scheduler-server 에는 `RestClientJob` 는 `org.quartz.Job` 인터페이스르 구현한 Job 클래스가 있습니다.
RestClientJob 은 원격 서버에 존재하는 REST API를 호출하는 단순한 Job 클래스입니다. 

보통 Quartz 를 사용한 프로젝트 환경에서는 다수의 Job 클래스가 존재하지만 prunus-scheduler-server 에서는 RestClientJob 
클래스가 원격 REST API 를 호출할 정보 (URL, Method, Parameter) 정보를 가지고 REST API 를 호출합니다.

필요하다면 사용자가 정의한 Job 클래스를 등록하여 얼마든지 다른 역할을 수행할 수도 있습니다. 

## REST API
### Job Schedule 생성 및 수정 - POST: /scheduler
Job Schedule 생성 및 수정 API 는 동일합니다. 전달된 Job Schedule 정보가 Quartz Scheduler 에 없다면
Job Schedule 을 생성하고 이미 있다면 기존 정보를 수정합니다.

Http 요청은 application/json 형태로 보내야하며 Payload 는 다음과 같습니다.
```json
{
   "jobDefinition":{
      "name":"chotire",
      "groupName":"prunus",
      "jobClass":"prunus.scheduler.server.job.RestClientJob",
      "durability":true,
      "description":null,
      "jobData":{
         "age":18
      }
   },
   "triggerDefinition":{
      "name":"tname",
      "groupName":"prunus",
      "startAt":1687934642308,
      "endAt":null,
      "priority":5,
      "jobData":{
         "birthDay": "10/11"
      },
      "simpleSchedule":{
         "interval":5,
         "repeatCount":-1
      },
      "cronSchedule":null
   },
   "jobUri":"http://localhost:8089",
   "httpMethod":"POST",
   "enabled":true
}
```
|Property| | |설명|기본값|필수여부|
|---|---|---|---|---|:---:|
|jobDefinition| | |Job Schedule 에서 Job 정의| |O|
| |name| |Job 이름| |O|
| |groupName| |Job group 이름|prunus| |
| |jobClass| |Quartz 가 실행할 Job 클래스|prunus.scheduler.server.job.RestClientJob|O|
| |durability| |Quartz Job 은 스케줄에 따라 실행되는데, 스케줄에 의해 실행되지 않는 시점에서는 Job이 Job Store에서 제거될 수 있습니다. 그러나 durability 를 true 로 설정하면 Quartz Job이 스케줄에 의한 실행이 없더라도 Job이 계속해서 Job Store에 유지됩니다.|true| |
| |description| |Job 설명| | |
| |jobData| |Job 실행 시 사용할 데이터를 저장합니다. RestClientJob 에서는 jobData 를 Http 파라미터로 변환하여 원격 REST API 호출합니다. jobData 는 Map 이므로 자유롭게 데이터 정의하여 사용하면 됩니다.| | |
|triggerDefinition| | |Job Schedule 에서 Schedule 정의| |O|
| |name| |Trigger 이름| |O|
| |groupName| |Trigger group 이름|prunus| |
| |startAt| |Trigger 시작 시간. 만약 startAt 과 CronSchedule 을 함께 정의했다면 Quartz는 다음과 같은 규칙에 따라 startAt과 CronScheduleBuilder 사이의 충돌을 처리합니다. <ol><li>startAt이 크론 표현식의 시작 시간보다 늦게 설정된 경우:<ul><li>startAt이 크론 표현식의 시작 시간보다 늦게 설정된 경우, Quartz는 startAt을 무시하고 크론 표현식에 따라 스케줄을 시작</li></ul></li><li>startAt이 크론 표현식의 시작 시간보다 이전에 설정된 경우:<ul><li>startAt이 크론 표현식의 시작 시간보다 이전에 설정된 경우, Quartz는 startAt에서 정의된 시간에 스케줄을 시작하고 그 이후에는 크론 표현식에 따라 스케줄을 정의</li></ul></li></ol>|Trigger 가 등록되는 현재시간 (Long 타입의 Date 값 전달)|O|
| |endAt| |Trigger의 종료 시간을 설정하는 데 사용됩니다. endAt 지정된 Date 객체의 시간에 Trigger가 종료되고 종료 시간 이후에는 Trigger가 실행되지 않음|(Long 타입의 Date 값 전달)| |
| |jobData| |Job 실행 시 사용할 데이터를 저장합니다. jobDefinition 와 jobData 같은 개념으로 jobData 가 Job 또는 Trigger 객체에 있느냐의 차이입니다. RestClientJob 에서는 Job에 있는 jobData 만을 사용해 Http 파라미터를 생성합니다. RestClientJob 사용 시에는 크게 의미없는 속성입니다.| | |
| |simpleSchedule| |simpleSchedule 은 몇초 마다 Job을 실행할 것인지 정의합니다.| | |
| | |interval|Job 실행할 간격. **단위는 '초' 입니다.**| | |
| | |repeatCount|몇 번을 반복할지 정의합니다. '-1' 은 영원히 반복합니다.| | |
| |cronSchedule| |'초 (Seconds)  분 (Minutes)  시 (Hours)  일 (Day of month)  월 (Month)  요일 (Day of week)' 형태의 cron expression 을 정의하여 스케줄링합니다.| | |
| | |cronExpression|0 0 8 * * MON-FRI(주중 아침 8시에 실행) 형태의 expression| | |
|jobUri| | |원격 REST API Job 의 URI 경로| |O|
|httpMethod| | |원격 REST API Job 의 의 Http method|POST|O|
|enabled| | |false 로 설정하면 Job은 등록되지만 Trigger 등록을 하지 않기 때문에 Job 실행이 중지됩니다.|true|O|

simpleSchedule, cronSchedule 은 필수 값은 아니지만 둘 중 하나는 정의되어야 합니다.
만약 simpleSchedule, cronSchedule 두 속성 다 null 전달이되면 prunus-scheduler-server 내부에서는
`prunus.scheduler.domain.UserDefinedScheduleBuilder` 구현체를 호출하여 사용자 정의 ScheduleBuilder 를
사용합니다.

prunus-scheduler-server 의 simpleSchedule 은 '초' 단위의 interval 만 지원하므로
분, 시간 단위와 같은 같은 Schedule 이 필요할 경우 또는 Quartz 에서 지원하는 다른 Schedule 등은
`prunus.scheduler.domain.UserDefinedScheduleBuilder` 인퍼페이스를 구현하여 Bean 으로 등록허여 
프로젝트 환경에 맞는 ScheduleBuilder 를 사용할 수 있습니다.

하지만 대부분의 프로젝트에서는 simpleSchedule, cronSchedule 두 개의 Schedule 로 대부분의 요구사항을 만족 시킬 수 있습니다.

API 요청의 응답 형태는 다음과 같습니다.
```json
{
    "jobKey": {
        "name": "chotire",
        "group": "prunus"
    },
    "triggerKey": {
        "name": "tname",
        "group": "prunus"
    }
}
```
Job Schedule 을 식별할 수 있는 키 값이 응답으로 전달되므로 사용자는 이 정보를 잘 저장해 놓았다가 Job Schedule 관리 시 사용할 수 있습니다.

### Job Schedule 삭제 - DELETE: /scheduler
Job Schedule 생성 및 수정 마찬가지로 Http 요청은 application/json 형태로 보내야하며 Payload 는 다음과 같습니다.
```json
{
    "jobKey": {
        "name": "chotire",
        "group": "prunus"
    },
    "triggerKey": {
        "name": "tname",
        "group": "prunus"
    }
}
```
Job Schedule 생성 시 응답로드 전달된 JobId 를 전달하면 됩니다.
JobId 프로젝트 내에서 잘 저장되고 관리되어야 Job Schedule 관리가 가능합니다. 

### Job Schedule 즉시 실행 - POST: /scheduler/immediate
Schedule 에 의해 Job 이 실행되지만 Job 의 에러 또는 장애 상황에서 때로는 Job 을 수동으로 즉시 실행이 필요할 경우가 있습니다.

Job Schedule 즉시 실행을 위한 Http 요청은 application/json 형태로 보내야하며 Payload 는 다음과 같습니다.
```json
{
   "jobId": {
      "jobKey": {
         "name": "chotire",
         "group": "prunus"
      },
      "triggerKey": {
         "name": "tname",
         "group": "prunus"
      }
   },
   "additionalData": {
      "address": "suji-gu"
   }
}
```
|Property| | |설명|기본값|필수여부|
|---|---|---|---|---|:---:|
|jobId| | |즉시 실행할 Job ID| | |
| |jobKey| |Job Key| | |
| | |name|Job name| |O|
| | |group|Job group| |O|
| |triggerKey| |Trigger Key| | |
| | |name|Trigger name| |O|
| | |group|Trigger group| |O|
|additionalData| | |즉시 실행 시 전달할 JobData| | |

## 인터페이스 클래스 및 확장
- `prunus.scheduler.server.job.AddableJobData`: Job 호출 시 추가적인 JobData 제공
- `prunus.scheduler.server.job.AuthHeader`: 원격 Job REST API 호출 시 인증 헤더 제공
- `prunus.scheduler.server.job.RestClientExceptionHandler`: 원격 Job REST API 호출 에러 시 에러 Handling
- `prunus.scheduler.domain.UserDefinedScheduleBuilder`: 사용자 정의 ScheduleBuilder
- `org.quartz.Job`: `org.quartz.Job` 을 구현하여 프로젝트 환경에 맞는 Job 클래스 구현
- `org.quartz.JobListener`: `org.quartz.Job` 실행 리스너

### AddableJobData
`AddableJobData` 는 prunus-scheduler-server 가 원격 REST API Job 호출 전에 사용자가 추가적인 데이터를 생성할 수 있도록 제공하빈다.
prunus-scheduler-server Job Schedule 생성 시 **jobData** 속성을 통해 Http 파라미터로 REST API Job 에 전달하는데
이 때 전달되는 데이터는 그 성격상 정적인 데이일 수 밖에 없습니다.
Job 이 실행되기 직전 동적인 데이터 전달이 필요할 경우 AddableJobData 인터페이스를 구현하여 Bean 으로 등록하면
prunus-scheduler-server 에 의해 호출됩니다.

````java
package prunus.scheduler.server.job;

public interface AddableJobData {
    /**
     * 원격 REST API Job 호출 시 추가적인 JobData 생성
     * @param jobId 호출할 jobId
     * @param jobData 추가 데이터
     */
    void add(JobId jobId, Map<String, Object> jobData);
}
````

### AuthHeader
prunus-scheduler-server 의 RestClientJob 클래스가 원격 REST API Job 호출 시 REST API 에 접근하려면 인증이 필요한 경우가 있습니다.
prunus-scheduler-server 원격 REST API 인증 수단과 정보를 알 수 없으므로 인증은 사용자가 직접 구현해주어야 합니다.

보통 JWT 혹은 Session 을 사용하게 되는데 인증 방법이 다를 뿐 Http Header 입장에서는 크게 다를 것은 없습니다.
아래 인터페이스 클래스의 주석과 같이 적절한 Http 헤더를 구성하세요.

````java
package prunus.scheduler.server.job;

/**
 * <p>사용자 시스템의 인증 처리를 한다.
 * 사용자 인증 시스템이 JWT 이든 Session 이든 결국 Http header 를 어떻게 작성하느냐의 차이일 뿐 크게 차이는 없다.</p>
 * 일반적으로는 다음과 같은 차이로 스케줄러 서버가 사용자의 시스템에 접근하기 위한 인증 코드는 직접 구현하여 prunus-scheduler-server 에게 제공해줘야 한다.
 * <ul>
 *     <li>JWT: headers.add("Authorization", "Bearer {JWT 토큰}");</li>
 *     <li>Session: headers.add("Cookie", "JSESSIONID={세션 ID}");</li>
 * </ul>
 * 
 * @author 조용상
 */
public interface AuthHeader {
    /**
     * 원격 REST API Job 호출을 위한 인증 헤더를 제공
     * @param jobData 원격 REST API Job 호출 시 사용할 JobData
     * @return 인증 헤더
     */
    HttpHeaders getAuthHeader(Map<String, Object> jobData);
}
````

### RestClientExceptionHandler
RestClientJob 이 원격 REST API Job 호출 시 Http 상태코드 4xx, 5xx 에러 처리와 4xx, 5xx 에러 외 처리를 할 수 있습니다.
RestClientExceptionHandler 을 구현하여 REST API Job 에러 상황을 실시간으로 메일링, SMS 등에 전송할 수 있습니다.
RestClientExceptionHandler 을 구현할 경우 다른 인터페이스 구현 클래스들과 마찬가지로 Bean 으로 등록해야 합니다.

RestClientExceptionHandler 와 `org.quartz.JobListener` 의 `void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException)`
차이는 JobListener 는 Job 클래스의 `void execute(JobExecutionContext context) throws JobExecutionException`
메서드 수행 시 예외가 발생할 때 `jobWasExecuted` 메서드가 호출됩니다.

RestClientExceptionHandler 는 원격 REST API Job 호출 시 예외가 발생할 때 `void handleException(HttpStatus status, HttpHeaders headers, String body)`
또는 `void handleException(Throwable throwable)` 메서드가 호출됩니다.

```java
package prunus.scheduler.server.job;

public interface RestClientExceptionHandler {
    /**
     * Http 상태코드 4xx, 5xx 에러 handling
     * @param status http 상태 정보
     * @param headers http header
     * @param body http 응답
     */
    void handleException(HttpStatus status, HttpHeaders headers, String body);

    /**
     * Http 상태코드 4xx, 5xx 에러 외 발생하는 모든 에러 handling
     * @param throwable 예외 객체 (throwable 은 보통 대부분 HttpClientErrorException 이거나 HttpServerErrorException 이지만 아닐 경우도 있다)
     */
    void handleException(Throwable throwable);
}
```

### UserDefinedScheduleBuilder
위에서 설명한 것과 같이 prunus-scheduler-server 가 제공하는 simpleSchedule, cronSchedule Schedule 로
프로젝트 요구사항 만족이 안 될경우 사용자가 직접 ScheduleBuilder 를 정의할 수 있습니다.

UserDefinedScheduleBuilder 인터페이스를 구현할 경우 구현 클래스를 Bean으로 등록해주세요.

```java
package prunus.scheduler.domain;

public interface UserDefinedScheduleBuilder {
    /**
     * 사용자 정의 ScheduleBuilder 생성
     * @param jobData JobData
     * @return 사용자 정의 ScheduleBuilder
     */
    ScheduleBuilder<? extends Trigger> getScheduleBuilder(Map<String, Object> jobData);

    /**
     * UserDefinedScheduleBuilder 는 여러 개가 등록될 수 있으므로
     * UserDefinedScheduleBuilder 구현한 클래스에서는 사용자 정의 ScheduleBuilder 를 제공할 수 있는지의 여부를 제공해야 한다.
     * @param jobData JobData
     * @return 사용자 정의 ScheduleBuilder 제공 여부
     */
    boolean supports(Map<String, Object> jobData);
}
```

### Job
prunus-scheduler-server 가 제공하는 RestClientJob 클래스도 `org.quartz.Job` 을 구현하여 원격 REST API Job 
호출이 가능할 수 있도록 구현한 Job 구현 클래스일 뿐입니다.
RestClientJob 외 프로젝트에서 필요한 특별한 Job 이 있다면 `org.quartz.Job` 인터페이스를 구현하여 프로젝트 환경에 맞는
Job 클래스를 구현하세요.

Job 인터페이스 구현 클래스는 Bean 으로 등록할 필요는 없습니다. 다만 Job Schedule 생성 시 다음과 같이
**jobClass** 속성에 사용자가 만든 Job 클래스의 Full name 을 대신 전달하면 됩니다.

```json
"jobDefinition":{
   "name":"chotire",
   "groupName":"prunus",
   "jobClass":"사용자 클래스 이름을 넣어주세요.",
   .....
   ...
   ..
},
```

````java
package org.quartz;

public interface Job {
    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> when a <code>{@link Trigger}</code>
     * fires that is associated with the <code>Job</code>.
     * </p>
     * 
     * <p>
     * The implementation may wish to set a 
     * {@link JobExecutionContext#setResult(Object) result} object on the 
     * {@link JobExecutionContext} before this method exits.  The result itself
     * is meaningless to Quartz, but may be informative to 
     * <code>{@link JobListener}s</code> or 
     * <code>{@link TriggerListener}s</code> that are watching the job's 
     * execution.
     * </p>
     * 
     * @throws JobExecutionException
     *           if there is an exception while executing the job.
     */
    void execute(JobExecutionContext context) throws JobExecutionException;

}
````

### JobListener
JobListener 는 Job 클래스의 Lifecycle 에 참여하여 프로젝트 요구사항 구현할 수 있습니다.
다른 인터페이스 구현 클래스들과 마찬가지로 JobListener 구현이 필요한 경우 구현 클래스를 Bean 으로 등록하면
prunus-scheduler-server 자동으로 Quartz Job Listener 등록 합니다.

````java
package org.quartz;

public interface JobListener {
    /**
     * <p>
     * Get the name of the <code>JobListener</code>.
     * </p>
     */
    String getName();

    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> when a <code>{@link org.quartz.JobDetail}</code>
     * is about to be executed (an associated <code>{@link Trigger}</code>
     * has occurred).
     * </p>
     * 
     * <p>
     * This method will not be invoked if the execution of the Job was vetoed
     * by a <code>{@link TriggerListener}</code>.
     * </p>
     * 
     * @see #jobExecutionVetoed(JobExecutionContext)
     */
    void jobToBeExecuted(JobExecutionContext context);

    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> when a <code>{@link org.quartz.JobDetail}</code>
     * was about to be executed (an associated <code>{@link Trigger}</code>
     * has occurred), but a <code>{@link TriggerListener}</code> vetoed it's 
     * execution.
     * </p>
     * 
     * @see #jobToBeExecuted(JobExecutionContext)
     */
    void jobExecutionVetoed(JobExecutionContext context);

    
    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> after a <code>{@link org.quartz.JobDetail}</code>
     * has been executed, and be for the associated <code>Trigger</code>'s
     * <code>triggered(xx)</code> method has been called.
     * </p>
     */
    void jobWasExecuted(JobExecutionContext context,
            JobExecutionException jobException);

}
````

