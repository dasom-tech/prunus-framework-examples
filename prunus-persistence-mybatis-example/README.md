# prunus-persistence-mybatis
`prunus-persistence-mybatis` 는 데이타를 생성 또는 수정할 경우 행위 정보를 자동으로 저장해주는 audit 기능과, 대용량 데이타를 부분범위로 조회하는 pagination 기능을 제공합니다.
이러한 기능은 `prunus-persistence-data` 를 공통으로 사용하며, `prunus-persistence-jpa` 도 동일한 기능으로 동작합니다.

## Audit

### AuditableEntity
기본적으로 제공하는 audit 정보 field 로 구성된 추상 클래스입니다. 이 클래스를 상속한 model class 를 사용하면,
각 field 에 해당하는 `auditProvider` 가 동작하여 audit 정보가 저장됩니다.   

```java
@Getter
@Setter
@EntityListeners({AuditingTrailListener.class})
public abstract class AuditableEntity implements Auditable {

    @PersistBy
    private String createdBy;

    @PersistDate
    private LocalDateTime createdDate;

    @PersistAddress
    private String createdRemoteAddr;

    @UpdateBy
    private String modifiedBy;

    @UpdateDate
    private LocalDateTime modifiedDate;

    @UpdateAddress
    private String modifiedRemoteAddr;
}
```

기본 제공되는 audit field 에 따른 provider 는 다음과 같습니다.

|구분|field|provider|역할|
|---|---|---|---|
|생성|createdBy|auditingSubjectProvider|데이타생성행위자ID|
|생성|createdDate|auditingDateProvider|데이타생성시각|
|생성|createdRemoteAddr|auditingAddressProvider|데이타생성clientIPaddress|
|수정|modifiedBy|auditingSubjectProvider|데이타수정행위자ID|
|수정|modifiedDate|auditingDateProvider|데이타수정시각|
|수정|modifiedRemoteAddr|auditingAddressProvider|데이타수정clientIPaddress|

### AuditableEntity 와 DB TABLE 매핑
audit field 에 해당하는 DB TABLE COLUMN 이 다음과 같이 지정되었을 경우,

|구분|field|column|
|---|---|---|
|생성|createdBy|CRE_USER|
|생성|createdDate|CRE_DATE|
|생성|createdRemoteAddr|CRE_ADDR|
|수정|modifiedBy|MOD_USER|
|수정|modifiedDate|MOD_DATE|
|수정|modifiedRemoteAddr|MOD_ADDR|

audit field <--> db column 정보는 `@AttributeOverrides` 어노테이션을 사용하여 정의하며,
다음과 같은 `AuditEntity`처럼 별도의 중간 단계의 상위 클래스를 생성하여 사용하고자 하는 entity class 에서 상속 받아 사용합니다.
```java
@AttributeOverrides({
        @AttributeOverride(name="createdBy", column=@Column(name="CRE_USER")),
        @AttributeOverride(name="createdDate", column=@Column(name="CRE_DATE")),
        @AttributeOverride(name="createdRemoteAddr", column=@Column(name="CRE_ADDR")),
        @AttributeOverride(name="modifiedBy", column=@Column(name="MOD_USER")),
        @AttributeOverride(name="modifiedDate", column=@Column(name="MOD_DATE")),
        @AttributeOverride(name="modifiedRemoteAddr", column=@Column(name="MOD_ADDR"))
})
public class AuditEntity extends AuditableEntity {
}
```
```java
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Laptop extends AuditEntity {

    private long id;
    private String vendor;
    private int displaySize;
    private boolean deleted;
}
```

### 기본 제공 audit field 의 부분 사용
만약 기본 제공되는 `modifiedRemoteAddr` 를 사용하지 않고자 한다면, `@AttributeOverrides` 어노테이션의 정의에서 해당 컬럼정보를 제외하여 사용합니다.
```java
@AttributeOverrides({
        @AttributeOverride(name="createdBy", column=@Column(name="CRE_USER")),
        @AttributeOverride(name="createdDate", column=@Column(name="CRE_DATE")),
        /* @AttributeOverride(name="createdRemoteAddr", column=@Column(name="CRE_ADDR")), */ // 제외
        @AttributeOverride(name="modifiedBy", column=@Column(name="MOD_USER")),
        @AttributeOverride(name="modifiedDate", column=@Column(name="MOD_DATE"))
        /* @AttributeOverride(name="modifiedRemoteAddr", column=@Column(name="MOD_ADDR")) */ // 제외
})
public class AuditEntity extends AuditableEntity {
}
```

### 기본 제공 audit provider 를 임의의 audit provider 로 대체 하여 사용
`createdBy` field 에 해당하는 `AuditingSubjectProvider` 를 대체하고자 할 경우,
`AuditingAware` 인터페이스의 `provide` 메서드를 구현한 provider 클래스를 생성하고,
해당 provider bean 명칭으로 spring bean 으로 등록하면, 기본 provider 가 대체되어 동작합니다.   
(* 해당 provider bean name 은 AuditProviderSupport.SUBJECT_PROVIDER_BEAN_NAME 값으로 정의되어 있으며,
명칭은 "auditingSubjectProvider" 으로 지정되어 있습니다.)
```java
public class UserAuditingSubjectProvider implements AuditingAware<String> {

    @Override
    public String provide() {
        return "exampleUser"; // 해당 audit 정보를 반환하는 로직을 구현 합니다.
    }
}
```
```java
@Configuration
public class MybatisConfiguration {

    @Bean(AuditProviderSupport.SUBJECT_PROVIDER_BEAN_NAME) // 교체 하고자 하는 provider 이름으로 등록합니다.
    public AuditingAware<String> auditingSubjectProvider() {
        return new UserAuditingSubjectProvider();
    }
}
```

### 임의의 audit field / provider 를 추가 하여 사용
기본 제공 audit field / provider 외에 별도의 audit 정보를 추가하고자 할 경우 추가 사항은 다음과 같습니다.   
- audit annotaion 생성
```java
// "생성" field 에 사용되는 어노테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(value = FIELD)
public @interface PersistDept {

    AuditType type() default PERSIST; // "생성" 타입으로 정의 합니다.
    String providerName() default "auditingDeptProvider"; // 해당 provider 가 등록된 spring bean 이름과 동일해야 합니다.
}
```
```java
// "수정" field 에 사용되는 어노테이션 
@Retention(RetentionPolicy.RUNTIME)
@Target(value = FIELD)
public @interface UpdateDept {

    AuditType type() default UPDATE; // "수정" 타입으로 정의 합니다.
    String providerName() default "auditingDeptProvider"; // 해당 provider 가 등록된 spring bean 이름과 동일해야 합니다.
}
```
- `@AttributeOverrides` 어노테이션이 정의된 상위 클래스에서 field 추가
```java
@Getter
@Setter
@AttributeOverrides({
        @AttributeOverride(name="createdBy", column=@Column(name="CRE_USER")),
        @AttributeOverride(name="createdDate", column=@Column(name="CRE_DATE")),
        @AttributeOverride(name="modifiedBy", column=@Column(name="MOD_USER")),
        @AttributeOverride(name="modifiedDate", column=@Column(name="MOD_DATE"))
})
public class AuditEntity extends AuditableEntity {
    
    // "생성" field를 정의합니다.
    // 추가된 @PersistDept 어노테이션을 선언하며, DB COLUMN 명칭을 추가 합니다.
    @PersistDept
    @Column(name="CRE_DEPT")
    private String createDept;

    // "수정" field를 정의합니다.
    // 추가된 @UpdateDept 어노테이션을 선언하며, DB COLUMN 명칭을 추가 합니다.
    @UpdateDept
    @Column(name="MOD_DEPT")
    private String updateDept;
}
```
- audit provider class 생성 및 spring bean 등록
```java
public class AuditingUserDeptProvider implements AuditingAware<String> {

    @Override
    public String provide() {
        return "exampleDept"; // 해당 audit 정보를 반환하는 로직을 구현 합니다.
    }
}
```
```java
@Configuration
public class MybatisConfiguration {
    
    // 해당 provider 를 spring bean 으로 등록합니다.
    // bean 이름은 반드시 해당 annotation 의 "providerName()" 의 값과 일치해야 합니다.
    @Bean("auditingDeptProvider")
    public AuditingAware<String> auditingUserDeptProvider() {
        return new AuditingUserDeptProvider();
    }
}
```

### Properties
|이름|설명|기본값|
|---|---|---|
|prunus.persistence.mybatis.audit.enabled| audit 기능 사용 여부|true|
|prunus.persistence.data.audit.modify-on-create| 데이터 추가 시에 추가항목 외에 수정 항목도 기록할지 여부|false|

## Pagination
mybatis 를 사용하여 대용량 데이터를 부분범위로 조회할 경우, 일반적으로 조회 SQL 을 기반으로 가공하여 사용하게 됩니다.
또한, DB 가 변경되었거나 이기종의 DB 를 동시에 사용시 처리 사항의 복잡도가 증가 합니다.   
이런 문제점을 해결하고자, pagination 정보를 요청할 경우는 일반 조회를 부분범위 조회로 변경하여 동작하는 기능과,
각각의 DB 에 대응하는 부분범위 SQL 으로 적용하여 동작하는 Dialect 기능을 제공 합니다.

### pagination 요청정보
pagination 의 요청 정보는 다음과 같습니다.

|이름|설명|기본값|
|---|---|---|
|page|페이지 번호 (page >= 1)|없음|
|size|페이지 당 건 수|20|
|sort|조회 시 정렬 정보|없음|

클라이언트 Request method 방식에 따라 다음과 같이 사용합니다.  

- `GET` Query Parameters

  |parameter|value|
  |---|---|
  |page|1|
  |size|10|
  |sort|id,asc|
  |sort|displaySize,desc|

  ```html
  page=1&size=10&sort=id,asc&sort=displaySize,desc
  ```

  controller method 에서 pagination 정보는 `Pageable` 인터페이스 타입의 메서드 파라미터에 자동으로 받아집니다.
  따라서, 클라이언트에서 보내는 정보에 대응하는 메서드 파라미터(`LaptopReq`)와는 별도로, `Pageable` 인터페이스 타입으로 지정해야 합니다.
  ```java
  @GetMapping("/pageable/page")
  public Page<LaptopDto> getPageablePage(LaptopReq laptopReq, Pageable pageable) {
      return service.getPage(laptopReq, pageable);
  }
  ```

  하지만, `LaptopReq` 가 `Pageable` 인터페이스 구현체라면, 별도의 `Pageable` 파라미터가 필요 없이 `LaptopReq` 파라미터가 Pagination 정보를 받아줍니다.
  이럴 경우, service 메서드에 전달할 때, 별도로 `Pageable` 을 추출하여(`laptopReq.pageable()`) 전달 하도록 합니다.
  ```java
  @GetMapping("/dto/page")
  public Page<LaptopDto> getPage(LaptopReq laptopReq) {
      // laptopReq.pageable() 을 사용하여 별도의 Pageable 객체를 전달 합니다.       
      return service.getPage(laptopReq, laptopReq.pageable());
  }
  ```
  이렇게 단일 파라미터로 정보를 받고자 할 경우는, 제공되는 `Pagination` 클래스를 상속 받음 으로서 `Pageable` 인터페이스 구현체로 동작하게 됩니다.
  ```java
  @Getter
  @Setter
  // Pagination 을 상속 받으면 Pageable 인터페이스를 구현한 객체로 사용됩니다.
  public class LaptopReq extends Pagination {

      private long id;
      private String vendor;
      private int displaySize;
      private boolean deleted;
  }
  ```
  service 로 전달된 `Pageable` 객체는 mapper method 의 별도의 파라미터로 전달되어 pagination 기능이 동작하게 됩니다.
  이때, `Pageable` 객체는 mapper method 파라미터의 순서에 무관하며, 별도의 파라미터로 mapper method 파라미터로 전달되어야 합니다.
  - service method
  ```java
  public Page<LaptopDto> getPage(LaptopReq laptopReq, Pageable pageable) {
      // 앞서 전달 받은 Pageable 을 별도의 파라미터로 전달 합니다.
      Page<Laptop> laptops = mapper.selectPage(laptopReq, pageable);
      // 반환 타입이 Page<T> 일 경우 PageImpl 구현체를 사용하여 반환 합니다.
      return new PageImpl<>(laptops.stream().map(LaptopDto::of).collect(Collectors.toList()), pageable, laptops.getTotalElements());
  }
  ```
  - mapper method
  ```java
  Page<Laptop> selectPage(LaptopReq laptopReq, Pageable pageable);
  ```
  다만, mapper method 의 반환 타입이 Page&lt;T&gt; 일 경우 pagination 정보를 포함하고 있는 객체로 반환하지만,
  단순 부분 조회 목록을 반환하고자 할 경우 List&lt;T&gt; 타입으로 선언하여 사용 합니다.
  ```java
  List<Laptop> selectPageList(LaptopReq laptopReq, Pageable pageable);
  ```

- `POST` Body

  |key|value|
  |---|---|
  |page|1|
  |size|20|
  |sort|['id,asc', 'displaySize,desc']|

  ```json
  {
    ...
    "pagination": {
      "page": 1,
      "size": 10,
      "sort": "['id,ASC','displaySize,ASC']"
    }
    ...
  }
  ```
  통상적으로 조회 요청은 `GET` method 타입의 Query Parameter 를 전달하지만,
  `POST` method 를 사용한 Body 를 전달하고자 할 경우 `RequestBody` 에 해당하는 객체에는 `Pagination` 타입의 클래스 필드를 선언 하여야 합니다.
  이렇게 선언된 `Pagination` 타입의 필드는 pagination 정보를 받게 됩니다.
  ```java
  @Getter
  @Setter
  public class Equipment {
      private String vendor;
      private Pagination pagination;
  }
  ```
  해당 객체는 다음과 같이 컨트롤러 메서드의 `@RequestBody` 파라미터로 사용됩니다.
  ```java
  @PostMapping("/post/page")
  public Page<LaptopDto> getPageByPostMethod(@RequestBody Equipment equipment) {
      LaptopReq laptopReq = LaptopReq.builder().vendor(equipment.getVendor()).build();
      Pageable pageable = equipment.getPagination().pageable();
      return service.getPage(laptopReq, pageable);
  }
  ```
### Dialect
DB 에 대응하는 dialect 목록은 다음과 같습니다.

|DB|Dialect|
|---|---|
|H2|db2Dialect|
|HSQL|hsqlDialect|
|MySQL|mysqlDialect|
|Oracle|oracleDialect|
|PostgreSQL|postgresqlDialect|
|SqlServer 2005|sqlserver2005Dialect|
|SqlServer|sqlserverDialect|
|Sybase|sybaseDialect|
|Tibero|tiberoDialect|

Dialect 는 spring bean 으로 등록되어 관리되며, 기본으로 제공되는 것 이외 Dialect 를 추가하고자 할 경우 다음과 같이 등록 합니다.
- Dialect class 구현   
  `com.github.miemiedev.mybatis.paginator.dialect.Dialect` 클래스를 상속받아서 `getLimitString` 메서드를 각 DB SQL 에 특화된 사항에 따라 구현 합니다.
- Spring Bean 등록   
  `prunus.persistence.mybatis.pagination.interceptor.PagingDialect` 객체의 Parameterized Type 으로 Dialect class 를 지정하여 bean 으로 등록합니다.   
  단, bean 이름은 기존 Dialect 이름과 중복되지 않게 지정하도록 합니다.
  ```java
  @Bean
  public PagingDialect db2Dialect() {
    return new PagingDialect(DB2Dialect.class);
  }
  ```

## Properties
|이름|설명|기본값|
|---|---|---|
|prunus.persistence.mybatis.pagination.enabled|pagination 기능 사용 여부|true|

pagination 의 기능은 `spring-data-commons` 을 사용함으로서, spring-jpa 에서 사용하는 기능과 동일하도록 구현되어 있습니다. 해당 설정을 그대로 이용합니다.

```yaml
spring:
  data:
    web:
      pageable:
        default-page-size: 20 # 한 페이지당 건 수를 의미합니다. 기본값은 20 입니다.
        one-indexed-parameters: true # 페이지 번호를 1부터 시작할지 여부를 의미합니다.
                                     # spring-jpa 해당 옵션은 기본으로 false 이지만,
                                     # 통상의 사용법에 따른 1부터 시작을 위해 기본 값을 true 로 지정합니다.
```

## 주의 사항
### Page&lt;T&gt; 반환 내용의 page number
mapper method 의 반환 타입이 Page&lt;T&gt; 일 경우, pagination 정보를 포함하고 있는 객체로 반환 됩니다.
하지만, 내부 page number 는 항상 본래의 값 보다 -1 값으로 지정되어 있습니다.
이는 `spring-data-commons` 의 page number 는 '0' 에서 시작하는 사항으로 구현되어 있기 때문 입니다.
시작 번호를 '1' 부터 시작하는 옵션인 `pring.data.web.pageable.one-indexed-parameters=true` 로 지정되어 있다 하더라도,
`Pageable` 과 `Page` 객체 내부의 page number 관련 값은 여전히 '0' 으로 시작하는 기준으로 반영되어 있습니다.
따라서 요청 값과는 별도로 내부 page number 값을 참조해야 할 경우 +1 처리하여 사용해야 합니다.   
(* page number 값 key : `pageNumber`)

- 반환 타입 반환 타입이 Page&lt;T&gt; 의 경우 데이터 예시
```json
{
  "content": [
    ...
  ],
  "pageable": {
    "sort": {
      "empty": false,
      "unsorted": false,
      "sorted": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalPages": 5,
  "totalElements": 50,
  "size": 10,
  "sort": {
    "empty": false,
    "unsorted": false,
    "sorted": true
  },
  "first": true,
  "number": 0,
  "numberOfElements": 10,
  "empty": false
}
```

## Test Client
`prunus-persistence-mybatis` 의 audit, pagination을 테스트하기 위해서는 다음 절차를 통해 테스트 해 볼 수 있습니다.
* `prunus-persistence-mybatis-example` 을 실행합니다.
  * 보통의 spring boot 의 application 실행 방법과 동일합니다. 
* 본인의 환경에 맞는 [Postman](https://www.postman.com/downloads/) 을 다운로드 및 설치합니다.
* **client** 디렉토리의 `prunus-persistence-mybatis.postman_collection.json` 파일을 Postman 에서 Import 하여 테스트 합니다.