# prunus-framework
prunus-framework는 Spring Boot를 확장하여 개발된 모듈들을 통해 프로젝트에서 반복적인 설정 작업과 자주 사용되는 
기능들을 자동으로 구성하는 역할을 수행합니다. 이를 통해 개발자는 기본적인 설정과 공통 기능들에 대한 
개발 시간과 노력을 크게 줄일 수 있습니다. 또한, 프로젝트에서 일관된 구조와 기능을 제공하여 개발의 일관성과 
유지보수의 편의성을 높여줍니다.

## prunus-framework 개발 환경
prunus-framework는 Java 11을 기반으로 개발되었습니다. 
개발에는 Spring Boot 프레임워크와 함께 사용됩니다. 따라서 개발 환경은 Java 11 JDK, Spring Boot, 
그리고 각 모듈에 필요한 의존성 라이브러리들이 필요합니다. 빌드 도구는 Gradle 권고합니다.
Maven 으로도 개발 환경을 구성할 수 있지만 그 과정이 Gradle 비해 복잡하고 효율이 떨어집니다.

## prunus-framework 모듈
### prunus-fileupload
prunus-fileupload 모듈은 파일 업로드와 다운로드를 위한 API를 제공합니다. 
개발자는 이 모듈을 사용하여 파일 업로드와 다운로드 기능을 쉽고 간편하게 구현할 수 있습니다. 
또한, 파일 유형의 제한, 파일 크기 제한, 보안 등 다양한 설정 옵션을 제공하여 안전하고 효과적인 파일 관리를 할 수 있습니다.

### prunus-i18n
prunus-i18n 모듈은 다국어 지원을 위한 기능을 제공합니다. 
이 모듈은 메시지 번들을 관리하고 애플리케이션에서 해당 메시지를 적절하게 선택하여 사용할 수 있도록 도와줍니다. 
개발자는 간단한 설정을 통해 다국어 지원을 구현하고, 다양한 언어에 대한 메시지를 관리할 수 있습니다.

### prunus-multidatasource
prunus-multidatasource 모듈은 다중 데이터 소스 관리와 트랜잭션 기능을 제공합니다. 
이 모듈을 사용하면 개발자는 여러 개의 데이터베이스나 데이터 소스를 효율적으로 관리할 수 있습니다. 
각각의 데이터 소스는 독립적인 설정으로 구성되며, 트랜잭션 처리를 자동으로 관리하여 데이터 일관성을 유지합니다.

### prunus-net
prunus-net 모듈은 SSH와 FTP를 이용한 파일 전송/다운로드, 이름 변경, 명령 실행 등의 기능을 제공합니다. 
개발자는 이 모듈을 사용하여 SSH나 FTP 프로토콜을 활용한 파일 관리 및 명령 실행을 수행할 수 있습니다. 
또한, 파일 전송 과정에서 보안과 인증을 강화하기 위한 다양한 옵션을 제공합니다.

### prunus-nexacro-adaptor
prunus-nexacro-adaptor 모듈은 nexacro와 Spring 컨트롤러를 통합하는 기능을 제공합니다.
이 모듈을 사용하면 개발자는 nexacro 플랫폼과 Spring 기반의 웹 애플리케이션을 통합하여 개발할 수 있습니다.
Nexacro의 다양한 UI 컴포넌트와 Spring의 강력한 서버 사이드 기능을 조합하여 사용할 수 있습니다.

### prunus-persistence-jpa
prunus-persistence-jpa 모듈은 JPA(Java Persistence API)의 Audit 기능을 확장하여 제공합니다.
개발자는 이 모듈을 사용하여 데이터 변경 이력을 추적하고, 감사 로그를 생성할 수 있습니다. 
이를 통해 데이터 변경 이력을 추적하여 추후에 데이터 복구나 감사 추적 등의 작업에 도움을 줍니다.

### prunus-persistence-mybatis
prunus-persistence-mybatis 모듈은 JPA의 Audit 기능을 그대로 사용할 수 있는 환경을 제공합니다.
개발자는 이 모듈을 사용하여 MyBatis를 활용하면서도 JPA를 동시에 사용하는 강력한 기능을 사용할 수 있습니다.
이 모듈은 Audit 기능 외에도 Pagination 기능 등을 제공하여 데이터베이스 관련 작업을 보다 효율적으로 처리할 수 있습니다.

### prunus-scheduler-server
prunus-scheduler-server 모듈은 Quartz Job Scheduler를 확장하여 다양한 기능을 제공합니다.
개발자는 이 모듈을 사용하여 Job 등록, 수정, 삭제, 즉시 실행 등의 작업을 쉽게 관리할 수 있습니다.
이 모듈은 복잡한 작업 스케줄링의 관리에 적합하며, 분산 환경에서의 안전한 스케줄링 작업을 지원합니다.

### prunus-scheduler-client
prunus-scheduler-client 모듈은 prunus-scheduler-server의 클라이언트 역할을 수행하며,
Job별 파일 로깅 기능을 제공합니다. 개발자는 이 모듈을 사용하여 prunus-scheduler-server에서 실행 중인 
스케줄링 Job 로그를 간편하게 확인하고, 분석할 수 있습니다. 이를 통해 작업 실행 상태와 결과를 실시간으로 모니터링할 수 있습니다.

### prunus-web
prunus-web 모듈은 웹 애플리케이션 개발에 필요한 다양한 기능을 제공합니다.
이 모듈은 Global ControllerAdvice 같은 공통 기능을 제공하여 예외 처리, 요청 로깅 등을 일관된 방식으로 처리할 수 있습니다. 
또한, 웹 기능을 강화하기 위한 기능들도 포함되어 있습니다.

### prunus-gradle-plugin
prunus-gradle-plugin은 Gradle 프로젝트에서 prunus-framework를 적용하기 위한 플러그인입니다. 
이 플러그인을 사용하면 개발자는 Gradle 빌드 스크립트에서 간편하게 prunus-framework 모듈들을 추가하고 구성할 수 있습니다.
이를 통해 프로젝트 구성의 편의성과 일관성을 높일 수 있습니다.

## prunus-framework 향후 계획
prunus-framework는 앞으로 cache, messaging service, websocket, security 등과 같은 
다양한 기능을 추가로 개발할 예정입니다. 이를 통해 개발자는 더욱 다양한 상황에 맞춰 프로젝트를 구성하고 활용할 수 있을 것입니다.
예를 들어, cache 모듈은 데이터베이스 조회 결과나 계산 결과 등을 캐싱하여 성능을 향상시키는 기능을 제공할 것입니다.

## prunus-biz 프로젝트
prunus-biz 프로젝트는 prunus-framework 외에도 비즈니스 공통 API를 개발할 예정입니다.
이 프로젝트를 통해 공통 코드, 메뉴, 사용자, 권한 관리 등의 비즈니스 기능을 효율적으로 개발하고 활용할 수 있을 것입니다.
prunus-biz 프로젝트는 prunus-framework와 연계하여 전체적인 애플리케이션 개발 및 운영에 대한 효율성을 높일 것입니다.
