# prunus-multidatasource
prunus-multidatasource 는 Spring framework 기반으로 Multi DataSource 를 빠르게 통합하기 위한 모듈입니다.

## Feature
- 데이터베이스에 민감한 구성 정보 암호화 지원
- 데이터베이스의 스키마 및 데이터의 독립적인 초기화 지원
- 지연(Lazy) 로딩 지원
- P6spy, JNDI 등 구성 요소 통합 솔루션 제공
- SpEL(Spring Expression Language) 을 사용하여 동적 데이터 소스를 선택하는 솔루션 제공. SpEL 외 Http Session, Header 지원.
- 데이터 소스 중첩 전환 지원 (서비스A >>> 서비스B >>> 서비스C)
- 로컬 트랜잭션 솔루션 제공

## Setup
1. 사용자 프로젝트에 `prunus-multidatasource` 모듈을 추가하세요.
```groovy
implementation 'sdcit.prunus:prunus-multidatasource'
```

2. application.yml 설정

아래 설정 내용은 예시입니다. [Properties](#Properties) 섹션을 참고하여 프로젝트 환경에 맞게 구성하세요.
```yaml
prunus:
  multi-datasource:
    primary: student
    strict: true
    p6spy:
      enabled: true
    datasources:
      student: # 데이터소스 이름
        driver-class-name: org.h2.Driver
        url: jdbc:h2:mem:student
        username: sa
        p6spy: true
        init:
          schema: schema-student.sql # 데이터소스를 생성하면서 생성할 스키마가 있을 경우에 사용합니다.
          data: data-studnent.sql # 스키마 생성 뿐 아니라 초기 데이터 입력이 필요할 경우 사용합니다.
      teacher: # 데이터소스 이름
        driver-class-name: org.h2.Driver
        url: jdbc:h2:mem:teacher
        username: sa
        p6spy: true
        init:
          schema: schema-teacher.sql
spring:
  jpa:
    hibernate:
      ddl-auto: none
    open-in-view: false # JPA 를 사용하는 프로젝트는 반드시 open-in-view 속성을 false 로 지정하세요. 
```

## Properties
|이름|설명|기본값|
|---|---|---|
|prunus.multi-datasource.enabled|멀티데이터 소스 활성화 여부|true|
|prunus.multi-datasource.primary|기본 데이터 소스의 이름|primary|
|prunus.multi-datasource.strict|엄격 모드가 true 일 경우 일치하는 데이터 소스가 없으면 예외를 발생시키고 false 일 경우에는 primary 로 설정된 데이터 소스가 사용됩니다|false|
|prunus.multi-datasource.private-key|데이터 소스 구성을 복호화하기 위한 개인 키| |
|prunus.multi-datasource.datasources|데이터 소스 이름과 해당 데이터 소스 속성을 매핑한 맵| |
|prunus.multi-datasource.datasources.pool-name|Connection 풀 이름| |
|prunus.multi-datasource.datasources.type|데이터 소스 타입| |
|prunus.multi-datasource.datasources.driver-class-name|JDBC 드라이버 클래스 이름| |
|prunus.multi-datasource.datasources.url|데이터베이스 URL| |
|prunus.multi-datasource.datasources.username|데이터베이스 로그인 username| |
|prunus.multi-datasource.datasources.password|데이터베이스 로그인 password| |
|prunus.multi-datasource.datasources.jndi-name|데이터 소스를 JNDI를 사용할 경우 JNDI 이름| |
|prunus.multi-datasource.datasources.p6spy|P6Spy 사용 여부|false|
|prunus.multi-datasource.datasources.lazy|데이터소스 지연 로드 여부. true 일 경우에는 데이터 소스가 필요할 때 데이터베이스 연결을 합니다|false|
|prunus.multi-datasource.datasources.private-key|데이터 소스 구성을 복호화하기 위한 개인 키| |
|prunus.multi-datasource.datasources.hikari|HikariCP 구성 상세 설정. 상세한 내용은 [HikariCP 매뉴얼](https://github.com/brettwooldridge/HikariCP#frequently-used) 을 참고하세요| |
|prunus.multi-datasource.datasources.init|데이터베이스 초기화 설정| |
|prunus.multi-datasource.datasources.init.schema|스키마 스크립트 파일| |
|prunus.multi-datasource.datasources.init.data|데이터 스크립트 파일| |
|prunus.multi-datasource.datasources.init.continue-on-error|데이터베이스 초기화 때 오류 발생 시 계속 실행할지 여부|true|
|prunus.multi-datasource.datasources.init.separator|스크립트 구분자|;|
|prunus.multi-datasource.p6spy|P6Spy 구성 상세 설정| |
|prunus.multi-datasource.p6spy.enabled|Enables logging JDBC events. `prunus.multi-datasource.datasources` 하위 설정의 p6spy 가 true 이여도 해당 값을 false로 설정하면 P6Spy 는 비활성화 됩니다. |false|
|prunus.multi-datasource.p6spy.multiline|Enables multiline output|true|
|prunus.multi-datasource.p6spy.logging|Logging to use for logging queries|slf4j|
|prunus.multi-datasource.p6spy.log-file|Name of log file to use (only with logging=file)|spy2.log|
|prunus.multi-datasource.p6spy.log-format|Custom log format| |
|prunus.multi-datasource.p6spy.tracing|Tracing related properties| |
|prunus.multi-datasource.p6spy.custom-appender-class|Class file to use (only with logging=custom). The class must implement com.p6spy.engine.spy.appender.FormattedLogger| |
|prunus.multi-datasource.p6spy.log-filter|Log filtering related properties.| |
|prunus.multi-datasource.hikari|HikariCP 구성 상세 설정. `prunus.multi-datasource.datasources.hikari` 설정의 전역 설정으로 Runtime 에서 전역 설정의 설정과 Merge 되어 설정됩니다.| |
|prunus.multi-datasource.aop|멀티데이터 소스 AOP 설정| |
|prunus.multi-datasource.aop.enabled|멀티데이터 소스 AOP 활성화 여부|true|
|prunus.multi-datasource.aop.order|멀티데이터 소스 AOP의 우선순위|Integer.MIN_VALUE|
|prunus.multi-datasource.aop.allowed-public-only|public 메서드에 대해서만 멀티데이터 소스 AOP 적용 여부|true|

## 기능 설명
### 데이터베이스에 민감한 구성 정보 암호화 지원
데이터소스를 구성할 때 데이터베이스 URL, Username, Password 와 같은 민감한 정보를 암호화하고 복호화하는 기능을 제공합니다.

`sdcit.prunus:prunus-multidatasource` 모듈을 의존하면 개인의 환경마다 조금씩 차이는 있지만 보통 User Home directory 하위에 .gradle 디렉토리에   
.gradle/caches/modules-2/files-2.1/sdcit.prunus/prunus-fileupload/2.1.0-SNAPSHOT/8788bbef6b1351f0a0172996237e50b79ed64b83/prunus-fileupload-2.1.0-SNAPSHOT.jar
와 같은 형태로 `prunus-fileupload-2.1.0-SNAPSHOT.jar` 라이브러리가 존재합니다.

prunus-fileupload-2.1.0-SNAPSHOT.jar 라이브러리를 통해서 암호화를 할 수 있습니다.

먼저, prunus-multidatasource 모듈에서 암호화는 RSA 방식으로 공개키로 문자열을 암호화하고 개인키로 복호화합니다.
prunus-multidatasource 내부에는 이미 공개키/개인키를 가지고 있어 prunus-multidatasource 공개키/개인키가 없더라도 암호화할 수 있습니다.

하지만 프로젝트 내부에서 별도로 생성한 공개키/개인키를 바탕으로 암복화하고 싶을 경우 prunus-multidatasource CLI 를 통해서 키를 생성할 수 있습니다.

1. .gradle 디렉토리 하위에서 `prunus-fileupload-[version].jar` 파일이 있는 디렉토리로 Console (CMD) 에서 이동합니다.
2. 다음 명령을 통해 공개키/개인키를 생성합니다.
```shell
java -jar prunus-multidatasource-2.1.0-SNAPSHOT.jar keygen 512
```
3. 명령을 실행하면 다음과 같이 공개키와 개인키가 생성됩니다.
```
PublicKey=MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAM8xmttJwnTxEKBzDcOIyj1YByqc0AV8b/PRuDICzlePlc31EsgFjagGuth1pIuusUrQjATJiEyKm5FRxNvqJ+ECAwEAAQ==
PrivateKey=MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAzzGa20nCdPEQoHMNw4jKPVgHKpzQBXxv89G4MgLOV4+VzfUSyAWNqAa62HWki66xStCMBMmITIqbkVHE2+on4QIDAQABAkAGobAyzt1xHAKj2g7hmGwdssa28dI0LL+SEp82RZH4ai6tAfj2XmsOJFCSzdMYuCFNVb9ML6r/liJZAVppVN+5AiEA0wR5TFuYabRelpLYo9tyWFeT3k9mx2iCgnYGDuwxnckCIQD7XHdHRjnsGDl35wrmK1WAPKetu1676r45diHQfRplWQIgU9dINo2CDiWrG1p3Fwue7/jD+KVFqd8dU6Z/G7i+sfkCIQCsu0IMbKnpIEx9pPvMaAL9eLERpizXndzSx+c8ynuVIQIgdoicyEnH0qkZKB6oevzG8BSv0TAs+oZqg/IEtmO674I=
```
4. 발급된 공개키로 다음 명령을 실행하여 암호화 합니다. 데이터소스 연결에 필요한 username 을 암호환 한다고 한다면 다음과 같이 매개변수를 전달합니다.
```shell
java -jar prunus-multidatasource-2.1.0-SNAPSHOT.jar enc -k MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAM8xmttJwnTxEKBzDcOIyj1YByqc0AV8b/PRuDICzlePlc31EsgFjagGuth1pIuusUrQjATJiEyKm5FRxNvqJ+ECAwEAAQ== -t chotire
```
`enc` 명령 이후 `-k` 는 위에서 발급받은 공개키 (PublicKey) 를 전달하고 `-t` 에 암호화할 문자열을 입력합니다.

5. 명령을 실행하면 다음과 같은 암호화 문자열이 출력됩니다.
```
cipherText=sDdbZ9khxZJQXfRwCYvzlXcquKfdpPCUKAV5a+0XTd55/gHvpc0o8aIQ5DazU7Pc53jgYzP5rADh/PCgi1bjPw==
```
cipherText= 이후가 암호화된 문자열로 암호화된 문자열로 다음과 같이 prunus-multidatasource 의 구성을 할 수 있습니다.
암호화된 문자열은 반드시 **ENC(암호문)** 형식으로 구성해야 합니다.

```yaml
prunus:
  multi-datasource:
    primary: student
    # 발급받은 개인키 (PrivateKey) 를 설정하여 prunus-multidatasource 가 복호화할 수 있도록 합니다.
    private-key: MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAzzGa20nCdPEQoHMNw4jKPVgHKpzQBXxv89G4MgLOV4+VzfUSyAWNqAa62HWki66xStCMBMmITIqbkVHE2+on4QIDAQABAkAGobAyzt1xHAKj2g7hmGwdssa28dI0LL+SEp82RZH4ai6tAfj2XmsOJFCSzdMYuCFNVb9ML6r/liJZAVppVN+5AiEA0wR5TFuYabRelpLYo9tyWFeT3k9mx2iCgnYGDuwxnckCIQD7XHdHRjnsGDl35wrmK1WAPKetu1676r45diHQfRplWQIgU9dINo2CDiWrG1p3Fwue7/jD+KVFqd8dU6Z/G7i+sfkCIQCsu0IMbKnpIEx9pPvMaAL9eLERpizXndzSx+c8ynuVIQIgdoicyEnH0qkZKB6oevzG8BSv0TAs+oZqg/IEtmO674I=
    strict: true
    p6spy:
      enabled: true
    datasources:
      student:
        driver-class-name: org.h2.Driver
        url: jdbc:h2:mem:student
        # 암호화된 문자열 적용, ENC(암호문) 는 url, username, password 항목만 지원합니다.
        username: ENC(sDdbZ9khxZJQXfRwCYvzlXcquKfdpPCUKAV5a+0XTd55/gHvpc0o8aIQ5DazU7Pc53jgYzP5rADh/PCgi1bjPw==)
        p6spy: true
        init:
          schema: schema-student.sql
      teacher:
        driver-class-name: org.h2.Driver
        url: jdbc:h2:mem:teacher
        username: sa
        p6spy: true
        init:
          schema: schema-teacher.sql
```

프로젝트 내부의 공개키/개인키가 불필요하고 prunus-multidatasource 가 제공하는 기본 키를 기반으로 암호화하려면 `enc` 명령에서 `-k` 
옵션을 빼고 실행하면 됩니다.
```shell
java -jar prunus-multidatasource-2.1.0-SNAPSHOT.jar enc -t chotire
```
prunus-multidatasource 가 제공하는 키로 암호화했을 경우 `prunus.multi-datasource.private-key` 프로퍼티를 추가할 필요가 없습니다.

### 데이터베이스의 스키마 및 데이터의 독립적인 초기화 지원
Spring framework 의 데이터소스가 지원하는 초기화 기능 그대로 prunus-multidatasource 는 다중 데이터소스에 적합하게 
초기화 기능을 지원합니다.
아래 표와 같은 설정 내용을 갖습니다. 

|이름|설명|기본값|
|---|---|---|
|prunus.multi-datasource.datasources.init.schema|스키마 스크립트 파일| |
|prunus.multi-datasource.datasources.init.data|데이터 스크립트 파일| |
|prunus.multi-datasource.datasources.init.continue-on-error|데이터베이스 초기화 때 오류 발생 시 계속 실행할지 여부|true|
|prunus.multi-datasource.datasources.init.separator|스크립트 구분자|;|

```yaml
prunus:
  multi-datasource:
    datasources:
      student:
        driver-class-name: org.h2.Driver
        url: jdbc:h2:mem:student
        username: sa
        init:
          schema: schema-student.sql
          data: schema-data.sql
          continue-on-error: true
          separator: ;
```

> schema, data 스크립트 파일 schema.sql, data.sql 이름은 되도록 사용하지 마시기 바랍니다. 
> schema.sql, data.sql 이름은 Spring framework 데이터소스 초기화의 default 이름으로 해당 파일명이 존재할 경우
> > Spring framework 의 데이터소스가 prunus-multidatasource 보다 먼저 초기화를 실행합니다.
> > 그럼에도 schema.sql, data.sql 이름을 사용하고 싶을 경우에는 application.yml 에 다음과 같이 설정하여 spring 의 초기화 기능을 비활성화 하세요.

```yaml
spring:
  sql:
    init:
      enabled: false
```

### 지연(Lazy) 로딩 지원
데이터소스의 "Lazy 로딩"은 애플리케이션이 시작될 때 D데이터소스를 초기화하는 것이 아니라, 
필요한 시점에서 데이터소스를 실제로 로드하도록 하는 방법입니다. 이는 애플리케이션 시작 시간을 단축하고, 
리소스를 효율적으로 사용할 수 있는 장점이 있습니다.

```yaml
prunus:
  multi-datasource:
    datasources:
      student:
        driver-class-name: org.h2.Driver
        url: jdbc:h2:mem:student
        username: sa
      teacher:
        driver-class-name: org.h2.Driver
        url: jdbc:h2:mem:teacher
        username: sa
        lazy: true # Lazy 로딩
```

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.7.9)

INFO 62507 --- [           main] datasource.DataSourceApplication         : Starting DataSourceApplication using Java 16.0.2 on CHO-MacBook-Pro.local with PID 62507 (/Users/yongsang/Development/workspace/java/prunus-framework-examples/prunus-multidatasource-example/out/production/classes started by yongsang in /Users/yongsang/Development/workspace/java/prunus-framework-examples)
INFO 62507 --- [           main] datasource.DataSourceApplication         : No active profile set, falling back to 1 default profile: "default"
INFO 62507 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
INFO 62507 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 29 ms. Found 2 JPA repository interfaces.
INFO 62507 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'prunus.multi-datasource-prunus.datasource.spring.boot.autoconfigure.MultiDataSourceProperties' of type [prunus.datasource.spring.boot.autoconfigure.MultiDataSourceProperties] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
INFO 62507 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'prunus.datasource.spring.boot.autoconfigure.MultiDataSourceAutoConfiguration' of type [prunus.datasource.spring.boot.autoconfigure.MultiDataSourceAutoConfiguration$$EnhancerBySpringCGLIB$$b01f251] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
INFO 62507 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'dataSourceNameDiscoverer' of type [prunus.datasource.discoverer.HttpHeaderDiscoverer] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
INFO 62507 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'prunus.core.CoreAutoConfiguration' of type [prunus.core.CoreAutoConfiguration] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
INFO 62507 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
INFO 62507 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
INFO 62507 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.71]
INFO 62507 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
INFO 62507 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 884 ms

// 애플리케이션 시작과 함께 데이터소스가 생성
INFO 62507 --- [           main] com.zaxxer.hikari.HikariDataSource       : student - Starting...
INFO 62507 --- [           main] com.zaxxer.hikari.HikariDataSource       : student - Start completed.
DEBUG 62507 --- [           main] prunus.datasource.MultiDataSource        : Switching to the primary datasource
DEBUG 62507 --- [           main] prunus.datasource.MultiDataSource        : Switching to the primary datasource
INFO 62507 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
INFO 62507 --- [           main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 5.6.15.Final
DEBUG 62507 --- [ent housekeeper] com.zaxxer.hikari.pool.HikariPool        : student - Pool stats (total=0, active=0, idle=0, waiting=0)
DEBUG 62507 --- [ent housekeeper] com.zaxxer.hikari.pool.HikariPool        : student - Fill pool skipped, pool is at sufficient level.
INFO 62507 --- [           main] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {5.1.2.Final}
DEBUG 62507 --- [           main] prunus.datasource.MultiDataSource        : Switching to the primary datasource
DEBUG 62507 --- [onnection adder] com.zaxxer.hikari.pool.HikariPool        : student - Added connection conn1: url=jdbc:h2:mem:x user=SA
INFO 62507 --- [           main] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.H2Dialect
INFO 62507 --- [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
INFO 62507 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
INFO 62507 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
INFO 62507 --- [           main] datasource.DataSourceApplication         : Started DataSourceApplication in 2.14 seconds (JVM running for 2.514)
INFO 62507 --- [nio-8080-exec-2] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
INFO 62507 --- [nio-8080-exec-2] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
INFO 62507 --- [nio-8080-exec-2] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
DEBUG 62507 --- [nio-8080-exec-2] d.a.MultiDataSourceAnnotationInterceptor : DataSource switching target method [datasource.service.TeacherService.save]
DEBUG 62507 --- [nio-8080-exec-2] prunus.datasource.MultiDataSource        : Switching to the DataSource named [teacher]

// 애플리케이션 시작 이후 API 호출로 데이터소스가 필요할 때 데이터소스 생성
INFO 62507 --- [nio-8080-exec-2] com.zaxxer.hikari.HikariDataSource       : teacher - Starting...
INFO 62507 --- [nio-8080-exec-2] com.zaxxer.hikari.HikariDataSource       : teacher - Start completed.
DEBUG 62507 --- [onnection adder] com.zaxxer.hikari.pool.HikariPool        : teacher - Added connection conn3: url=jdbc:h2:mem:teacher user=SA
```

> Lazy 로딩은 JNDI 에서는 지원하지 않습니다.

### P6spy, JNDI 등 구성 요소 통합 솔루션 제공
P6spy는 실행되는 SQL 쿼리를 모니터링하고, 로깅하는 라이브러리입니다. JDBC 로깅만으로는 PrepareStatement 바인딩 변수 확인이 어렵고
까다로워 보통 프로젝트에는 jdbc4j, p6spy 같은 전용 SQL 로깅 라이브러리를 사용하는데 prunus-multidatasource 는 P6spy 와 통합하여
별로의 환경 구성없이 간편하게 SQL 로깅을 할 수 있습니다.
```yaml
prunus:
  multi-datasource:
    p6spy:
      enabled: true # 전역 설정으로 하위 datasources 들이 p6spy 활성화 하였더라도 전역 설정으로 활성화/비활성화 할 수 있습니다.
    datasources:
      student:
        driver-class-name: org.h2.Driver
        url: jdbc:h2:mem:student
        username: sa
        p6spy: true # 개별 데이터소스 마다 p6spy 의 기능을 on/off 할 수 있습니다.
      teacher:
        driver-class-name: org.h2.Driver
        url: jdbc:h2:mem:teacher
        username: sa
        p6spy: true
```
P6spy 설정이 적용되면 다음과 같은 로그를 볼 수 있습니다.
```
INFO 62548 --- [nio-8080-exec-1] p6spy : #1688623477971 | took 3ms | statement | connection 1| url jdbc:h2:mem:student
insert into student (id, age, name) values (default, ?, ?)
insert into student (id, age, name) values (default, 8, 'cirilo');
```

전통적인 On-premise 환경의 개발, 운영 서버의 경우에는 HikariCP 와 같은 전문 데이터베이스 Connection Pool 대신
상용 WAS 가 제공하는 데이터소스를 JNDI 를 통해서 사용하는 게 일반적입니다.

prunus-multidatasource 는 JNDI 이름을 설정하는 것만으로도 데이터소스 구성이 가능합니다.
```yaml
prunus:
  multi-datasource:
    datasources:
      student:
        jndi-name: ds_student
      teacher:
        jndi-name: ds_teacher
```

### (Spring Expression Language) 을 사용하여 동적 데이터 소스를 선택하는 솔루션 제공. SpEL 외 Http Session, Header 지원.
해당 섹션에서는 지금까지 prunus-multidatasource 에서 다중 데이터소스를 어떻게 구성하는지에 대해 다루었다면 실제 소스에서는 여러개의 데이터소스 중
프로그램마다 어떤 데이터소스를 사용하게 할 것인지에 대해 설명합니다.

`@DataSourceName` Annotation 은 데이터소스 이름을 지정하기 위한 Annotation 입니다. 해당 Annotation 은 클래스 또는 메서드에 적용할 수 있습니다.
클래스 또는 메서드에 애너테이션을 적용하여 데이터소스의 이름을 지정할 수 있습니다.

```java
// 클래스에 데이터소스 이름 지정
@DataSourceName("student")
public class MyClass {
    ...
}

// 메서드에 데이터소스 이름 지정
public class MyService {
    @DataSourceName("teacher")
    public void myMethod() {
        ...
    }
}
```

클래스 레벨에 @DataSourceName Annotation 을 지정하면 해당 클래스에 정의된 모든 메서드는 클래스에 정의한 데이터소스를 사용합니다.
만약 클래스와 메서드 모두에 @DataSourceName Annotation 을 지정하면 @DataSourceName Annotation 을 지정한
메서드는 메서드가 정의한 데이터소스를 사용하고 지정하는 않은 메서드는 클래스에 정의한 데이터소스를 사용합니다.

```java

// 클래스 레벨에서 teacher 데이터소스 정의
@DataSourceName("teacher")
public class MyService {
    // 메서드가 정의한 student 데이터소스 사용
    @DataSourceName("student")
    public void myMethod1() {}

    // teacher 데이터소스 사용
    public void myMethod2() {}
}
```

데이터소스를 선택하는 방법은 SpEL, Http Session. Header 를 사용하여 동적으로 선택할 수 있습니다.

#### SpEL
SpEL(Spring Expression Language)은 Spring 프레임워크에서 제공하는 표현 언어입니다. 
SpEL은 문자열 형식으로 작성된 표현식을 사용하여 Spring 애플리케이션에서 프로퍼티, 메서드, 변수 등에 접근하고 
조작하는 기능을 제공합니다

> prunus-multidatasource 의 @DataSourceName Annotation 동적 데이터소스 선택 문자열은 반드시 `#` 으로 시작되어야 합니다.

```java
public class DataSourceSelector {
    private String ds;
    private String nickname;
    
    public String getDs() {
        return ds;
    }
    
    public void setDs(String ds) {
        this.ds = ds;
    }
}

@Service
public class StudentService {
    @DataSourceName("#selector.ds")
    public List<Student> findAllBySpel(DataSourceSelector selector) {
        return repository.findAll();
    }
}

@RestController
public class StudentController {
    private final StudentService service;
    
    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Student> findAllBySpel(DataSourceSelector selector) {
        selector.setDs("student");
        return service.findAllBySpel(selector);
    }
}
```
위 코드에서 `@DataSourceName("#selector.ds")` @DataSourceName Annotation 이 findAllBySpel 메서드의
selector 객체의 ds 멤버변수에 데이터소스 이름이 있음을 정의하고 있습니다.

StudentController findAllBySpel 메서드에서 데이터소스 이름을 `selector.setDs("student");` 로 지정했으므로
StudentService findAllBySpel 메서드는 **student** 데이터소스가 선택되고 사용됩니다.

#### Http Header
prunus-multidatasource 가 Http Header 내 특정 이름의 값을 대상으로 동적으로 데이터소스를 선택합니다.

```java
@Service
public class StudentService {
    @DataSourceName("#header.ds")
    public List<Student> findAllByHeader() {
        return repository.findAll();
    }
}
```

Http 요청 시 Header 이름 `ds` 의 value 를 평가하여 데이터소스를 동적으로 선택합니다. 

#### Http Session
Http Header 와 같이 Http 요청 객체 (HttpServletRequest) 를 기반으로 하지만 그것이 Session 이라는 것의 차이만 있습니다.
로그인 사용마다 사용해야 할 데이터소스가 다를 경우 요긴하게 사용할 수 있습니다.

```java
@Service
public class StudentService {
    @DataSourceName("#session.ds")
    public List<Student> findAllByHeader() {
        return repository.findAll();
    }
}
```

Http Session 객체의 attribute 이름 `ds` 의 value 를 평가하여 데이터소스를 동적으로 선택합니다.

> SpEL, Header, Session `ds` 이름은 예시일 뿐 어떤 이름이든 상관없이 사용가능합니다. 프로젝트 환경에 적절한 이름을 사용하세요.

#### 데이터소스 이름 Hard coding
지금까지의 데이터소스 선택은 동적인 선택이였습니다. 동적인 선택은 보통 프로젝트에서 특별한 요구사항을 만족시킬 때 
사용되고 대부분은 프로그램 개발 시에 이미 데이터소스가 이미 정해진 경우가 많습니다.

다음 코드와 같이 데이터소스 이름을 선언적 방식으로 지정할 수 있습니다. 선언적 방식으로 지정할 때는 **`#` Prefix 가 없음**을 유의 해주세요.

```java
@Service
@DataSourceName("student")
public class StudentService {
    public List<Student> findAllByHeader() {
        return repository.findAll();
    }
}
```

### 로컬 트랜잭션 솔루션 제공
JDBC(Java Database Connectivity) 에는 XA 드라이버라는 분산 트랜잭션을 지원하기 위해 사용되는 JDBC 드라이버가 있습니다. 
XA는 "eXtended Architecture"의 약자로, 여러 개의 리소스(데이터베이스, 메시지 큐 등)를 하나의 트랜잭션으로 관리하기 위한 표준 규약입니다.

XA 드라이버는 분산 트랜잭션 처리를 위한 기능을 제공합니다. 이 드라이버를 사용하면 여러 데이터베이스나 다른 리소스들 간에 
트랜잭션을 동기화하고, 트랜잭션의 원자성(Atomicity)과 일관성(Consistency)을 보장할 수 있습니다. 
일반적으로 XA 드라이버는 데이터베이스 제공 업체에서 제공되며, 해당 데이터베이스에 맞는 XA 드라이버를 사용해야 합니다.

prunus-multidatasource 는 XA 드라이버의 분산 트랜잭션은 아니지만 prunus-multidatasource 가 관리하는 댜중 데이터소스의
트랜젝션을 `@LocalTransaction` Annotation 을 통해 하나의 트랜잭션으로 관리하는 솔루션을 제공합니다.

@LocalTransaction Annotation 은 Spring framework 의 @Transaction 개념과 동일합니다. 다만 Spring 은 단일 데이터소스의
트랜젝션 관리만 되는 반면 prunus-multidatasource 는 그 이름과 같이 다중 데이터소스 트랜젝션 관리를 제공합니다.

@LocalTransaction Annotation 은 Spring 의 그것과 마찬가지로 Propagation 속성을 사용하여 메서드 간의 
트랜잭션 전파 동작을 지정할 수 있습니다. Propagation은 메서드가 이미 실행 중인 트랜잭션이 있는 경우 어떻게 
동작해야 하는지를 결정합니다.

|Propagation|설명|
|---|---|
|REQUIRED|기본값이며, 메서드가 실행되는 동안 이미 진행 중인 트랜잭션이 있으면 해당 트랜잭션에 참여합니다. 없으면 새로운 트랜잭션을 시작합니다.|
|REQUIRES_NEW|항상 새로운 트랜잭션을 시작합니다. 이미 진행 중인 트랜잭션이 있으면 일시 중단시키고 새로운 트랜잭션을 시작합니다. 이전 트랜잭션은 현재 메서드가 완료될 때까지 잠시 보류됩니다.|
|SUPPORTS|이미 진행 중인 트랜잭션이 있으면 해당 트랜잭션에 참여합니다. 없으면 트랜잭션 없이 메서드를 실행합니다. 즉, 트랜잭션이 있는 경우에만 참여하고, 없는 경우에는 트랜잭션 없이 실행합니다.|
|NOT_SUPPORTED|메서드 실행 시 트랜잭션을 일시 중단시키고, 트랜잭션 없이 실행합니다. 이미 진행 중인 트랜잭션이 있더라도 참여하지 않습니다.|
|NEVER|메서드 실행 시 트랜잭션을 금지합니다. 이미 진행 중인 트랜잭션이 있으면 예외가 발생합니다.|
|MANDATORY|이미 진행 중인 트랜잭션이 있으면 해당 트랜잭션에 참여합니다. 하지만 트랜잭션이 없는 경우 예외를 발생시킵니다.|
|NESTED|현재 트랜잭션이 있는 경우, 중첩된 트랜잭션을 시작합니다. 중첩된 트랜잭션은 독립적으로 커밋 또는 롤백될 수 있습니다. 부모 트랜잭션에 영향을 주지 않으며, 부모 트랜잭션 내에서 롤백될 경우 중첩된 트랜잭션도 롤백됩니다.|

```java
@Service
public class SchoolService {
    private final StudentService studentService;
    private final TeacherService teacherService;
    
    @LocalTransactional
    public Pair saveWithTx(Pair pair) {
        studentService.save(pair.getStudent());
        teacherService.saveWithTx(pair.getTeacher());
        "".substring(2);
        return pair;
    }
}

@Service
@DataSourceName("teacher")
public class TeacherService {
    private final TeacherRepository repository;

    @LocalTransactional(propagation = LocalPropagation.REQUIRES_NEW)
    public Teacher saveWithTx(Teacher teacher) {
        return repository.save(teacher);
    }
}
```

위 코드의 시나리오는 Student 와 Teacher 를 저장 후 `"".substring(2);` 코드를 통해 일부러 예외를 발생시켜 
트랜잭션 롤백되는 상황을 연출한 코드입니다.

SchoolService.saveWithTx 메서드는 `@LocalTransactional` 으로 트랜잭션을 시작하고
TeacherService.saveWithTx 메서드는 `@LocalTransactional(propagation = LocalPropagation.REQUIRES_NEW)`
새로운 트랜잭센을 생성하고 시작합니다.

트랜잭션 롤백의 예상 결과는 Student 는 저장은 롤백되고 Teacher 저장은 커밋되는 것입니다.
1. SchoolService.saveWithTx 메서드는 `@LocalTransactional` Default propagation 인 `REQUIRED` 로 트랜잭션 시작
2. Student 와 Teacher 저장 성공
3. 저장은 성공 했지만 그 후 예외 발생
4. TeacherService.saveWithTx 메서드는 `@LocalTransactional(propagation = LocalPropagation.REQUIRES_NEW)`
   으로 새로운 트랜잭션을 시작했으므로 커밋
5. SchoolService.saveWithTx 메서드 내에서 예외 발생으로 SchoolService.saveWithTx 메서드에서 시작한 트랜잭션은 롤백

만약 예외가 SchoolService.saveWithTx 메서드가 아닌 TeacherService.saveWithTx 메서드에서 예외가 발생했다면
TeacherService 에서 발생한 예외가 SchoolService 까지 전파되어 Student, Teacher 저장은 모두 롤백 됩니다.

#### 커밋과 롤백 로그
```
// SchoolService.saveWithTx REQUIRED 트랜젝션 시작
// 트랜잭션 아이디 [32672244-5c81-4294-a2d6-2bf2b27d00fc]
DEBUG 64976 --- [nio-8080-exec-2] p.datasource.tx.TransactionTemplate      : Creating new transaction with name [datasource.service.SchoolService.saveWithTx]: REQUIRED
DEBUG 64976 --- [nio-8080-exec-2] prunus.datasource.tx.TransactionUtils    : Start local transaction [32672244-5c81-4294-a2d6-2bf2b27d00fc]
DEBUG 64976 --- [nio-8080-exec-2] prunus.datasource.MultiDataSource        : Switching to the primary datasource
DEBUG 64976 --- [nio-8080-exec-2] p.datasource.tx.ConnectionProxyFactory   : switching jdbc connection [prunus.datasource.tx.ConnectionProxy@aa0300b0] to manual commit
DEBUG 64976 --- [nio-8080-exec-2] d.a.MultiDataSourceAnnotationInterceptor : DataSource switching target method [datasource.service.TeacherService.saveWithTx]

// TeacherService.saveWithTx REQUIRED_NEW 로 새로운 트렌잭션 시작
// 트랜잭션 아이디 9e88c865-9157-4078-9dde-e71dcab4cdc8
// 새로운 트랜젝션이 시작되었으므로 SchoolService.saveWithTx 에서 시작된 트랜잭션 보류 [32672244-5c81-4294-a2d6-2bf2b27d00fc]
INFO 64976 --- [nio-8080-exec-2] p.datasource.tx.TransactionTemplate      : Suspending current transaction, xid = 32672244-5c81-4294-a2d6-2bf2b27d00fc
DEBUG 64976 --- [nio-8080-exec-2] p.datasource.tx.TransactionTemplate      : Creating new transaction with name [datasource.service.TeacherService.saveWithTx]: REQUIRES_NEW
DEBUG 64976 --- [nio-8080-exec-2] prunus.datasource.tx.TransactionUtils    : Start local transaction [9e88c865-9157-4078-9dde-e71dcab4cdc8]
DEBUG 64976 --- [nio-8080-exec-2] prunus.datasource.MultiDataSource        : Switching to the DataSource named [teacher]
DEBUG 64976 --- [nio-8080-exec-2] p.datasource.tx.ConnectionProxyFactory   : switching jdbc connection [prunus.datasource.tx.ConnectionProxy@af90cbda] to manual commit

// SchoolService.saveWithTx 예외 발생
// 트랜잭션 아이디 [9e88c865-9157-4078-9dde-e71dcab4cdc8] 커밋 - TeacherService.saveWithTx
// 트랜잭션 아이디 [32672244-5c81-4294-a2d6-2bf2b27d00fc] 롤백 - SchoolService.saveWithTx
DEBUG 64976 --- [nio-8080-exec-2] prunus.datasource.tx.TransactionUtils    : Commit local transaction [9e88c865-9157-4078-9dde-e71dcab4cdc8]
DEBUG 64976 --- [nio-8080-exec-2] prunus.datasource.tx.TransactionUtils    : Rollback local transaction [32672244-5c81-4294-a2d6-2bf2b27d00fc]
ERROR 64976 --- [nio-8080-exec-2] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.StringIndexOutOfBoundsException: begin 2, end 0, length 0] with root cause
```

> prunus-multidatasource 의 `@LocalTransactional` 테스트되고 그 기능이 잘 동작하지만 아직 실무에서 충분한 검증이 충분히 이루어지지 않았습니다. 
> **`@LocalTransactional` 는 Spring 의 `@Transactional` Annotation 으로 요구사항을 충족 시키기 어려운 비즈니스에서만 사용할 것을 권고합니다.**
>> #### _**일반적인 상황에서는 Spring 의 `@Transactional` Annotation 만으로도 충분히 요구사항을 만족시킬 수 있습니다.**_