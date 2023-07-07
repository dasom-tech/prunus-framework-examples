# prunus-nexacro-adaptor

`prunus-nexacro-adaptor`는 nexacro 전문 형식 및 데이타 타입을 이용하여 클라이언트와 서버간의 통신을 용이하기 위한 기능을 제공합니다.
nexacro 전문 양식은 XML, JSON 의 포멧으로 사용되며, 서버에서 Deserialize 과정을 거쳐서 임의의 DTO 로 전환 됩니다.
하지만, 이중 JSON 은 nexacro 의 전용 포멧으로 구성되어 있으므로, 일반적인 객체를 표현하는 방식과는 많은 차이가 있습니다.
따라서 `prunus-nexacro-adaptor` 에서 제공되는 Parser 는 nexacro 전용 전문 형식과 함께 일반적인 JSON 포멧 송/수신의 기능을 제공합니다.   
단, 각각의 전문 형식의 타입의 구분을 위해서 반드시 전문 요청 시 http-request-header `Content-Type` 을 지정해야 합니다.   
 
- http-request-header Content-Type

|전문형식|Content-Type|
|---|---|
|nexacro XML|application/PlatformXml|
|nexacro JSON|application/PlatformJson|
|general JSON|application/json|

Content-Type 별 data 형식은 다음과 같습니다. (형식만 다를 뿐 동일한 데이타 정보 입니다.)
- application/PlatformXml
```xml
<?xml version="1.0" encoding="utf-8"?>
<Root xmlns="http://www.nexacroplatform.com/platform/dataset" ver="4000" >
     <Parameters>
          <Parameter id="id">paramId</Parameter>
          <Parameter id="name">paramName</Parameter>
          <Parameter id="seq">1</Parameter>
     </Parameters>
     <Dataset id="ds_paging">
          <ColumnInfo>
               <Column id="page" size="3" type="INT"/>
               <Column id="size" size="5" type="INT"/>
               <Column id="sort" size="50" type="STRING"/>
          </ColumnInfo>
          <Rows>
               <Row>
                    <Col id="page">1</Col>
                    <Col id="size">10</Col>
                    <Col id="sort">['id,ASC','displaySize,ASC']</Col>
               </Row>
          </Rows>
     </Dataset>
     <Dataset id="ds_desktop">
          <ColumnInfo>
               <Column id="id" size="20" type="STRING"/>
               <Column id="vendor" size="20" type="STRING"/>
          </ColumnInfo>
          <Rows>
               <Row>
                    <Col id="id">01</Col>
                    <Col id="vendor">samsung</Col>
               </Row>
          </Rows>
     </Dataset>
     <Dataset id="laptops">
          <ColumnInfo>
               <Column id="id" size="20" type="STRING"/>
               <Column id="vendor" size="20" type="STRING"/>
               <Column id="displaySize" size="2" type="INT"/>
          </ColumnInfo>
          <Rows>
               <Row>
                    <Col id="id">01</Col>
                    <Col id="vendor">lg</Col>
                    <Col id="displaySize">15</Col>
               </Row>
          </Rows>
     </Dataset>
</Root>
```

- application/PlatformJson
```json
{
  "Datasets": [
    {
      "ColumnInfo": {
        "Column": [
          {
            "size": "3",
            "id": "page",
            "type": "int"
          },
          {
            "size": "5",
            "id": "size",
            "type": "int"
          },
          {
            "size": "50",
            "id": "sort",
            "type": "string"
          }
        ]
      },
      "id": "ds_paging",
      "Rows": [
        {
          "page": 1,
          "size": 10,
          "sort": "['id,ASC','displaySize,ASC']"
        }
      ]
    },
    {
      "ColumnInfo": {
        "Column": [
          {
            "size": "32",
            "id": "id",
            "type": "string"
          },
          {
            "size": "32",
            "id": "vendor",
            "type": "string"
          }
        ]
      },
      "id": "ds_desktop",
      "Rows": [
        {
          "vendor": "samsung",
          "id": "0"
        }
      ]
    },
    {
      "ColumnInfo": {
        "Column": [
          {
            "size": "4",
            "id": "displaySize",
            "type": "int"
          },
          {
            "size": "32",
            "id": "id",
            "type": "string"
          },
          {
            "size": "32",
            "id": "vendor",
            "type": "string"
          }
        ]
      },
      "id": "laptops",
      "Rows": [
        {
          "vendor": "lg",
          "id": "0",
          "displaySize": "15"
        }
      ]
    }
  ],
  "Parameters": [
    {
      "id": "id",
      "type": "string",
      "value": "paramId"
    },
    {
      "id": "name",
      "type": "string",
      "value": "paramName"
    },
    {
      "id": "seq",
      "type": "int",
      "value": 1
    }
  ]
}
```

- application/json
```json
{
    "id": "20230405",
    "name": "computer",
    "seq": 1,
    "ds_paging" : [
      {
        "page": 1,
        "size": 10,
        "sort": "['id,ASC','displaySize,ASC']"
      }
      ],
    "ds_desktop": [
      {
        "id": "000001",
        "vendor": "samsung"
      }
      ],
    "laptops": [
      {
        "id": "000002",
        "vendor": "lg",
        "displaySize": 15
      }
      ]
}
```

http-request 으로 요청된 정보는 Controller method 에 선언된 argument 로 변환되며,
해당 argument class type 의 객체 정보로 Deserialize 과정을 거쳐서 DTO java class 로 사용됩니다.   
`prunus-nexacro-adaptor` 에서는 이러한 http-request 요청 정보를 Controller 로 전달 받기 위한 2가지 방법을 제공하며, 다음과 같은 특징이 있습니다.

|메세지 수신 방식|Request 수신|Response 반환|
|---|---|---|
|MethodArgumentResolver|클라이언트에서 전송된 각각의 값을 개별 argument 로 사용|반환하고자 하는 개별 값을 variable, dataset 구분으로 조합하여 NexacroResult 타입으로 반환|
|HttpMessageConverter|클라이언트에서 전송된 값을 하나의 argument 로 수신하여 내부의 개별 값으로 사용|반환하고자 하는 개별 값을 하나의 집합 객체의 필드 정보로 지정하여 반환|

## MethodArgumentResolver

- **@VariableParam**   
  단일 값을 전송 받을 경우 사용하며, `name` 속성에 따라 클라이언트에서 전송된 전문의 해당 값으로 받아옵니다.   
  속성은 다음과 같은 사항으로 사용합니다.

  |속성|설명|기본값|필수사용여부|
  |---|---|---|:---:|
  |name or value|지정된 값에 대응하는 클라이언트 전문의 값을 이름으로 판별 합니다.|없음|O|
  |required|클라이언트로 부터 전달된 전문의 값의 필수 여부를 지정합니다.|true|X|

- **@DataSetParam**   
  집합 개념의 값을 전송 받을 경우 사용하며, 속성 값은 @VariableParam 과 동일하게 사용합니다.   
  값이 목록형(List&lt;E&gt;) 으로 전송되었다 하더라도, argument type 이 단일 객체 타입(E) 으로 선언되어 있을 경우는, 특별하게 발생되는 예외 없이 타입이 (E) 형태로 동일함을 인지하고, 단일 값으로 자동으로 수신 됩니다.   
  부분범위 조회를 위한 정보를 수신하기 위해서는 `Pagination` class type 의 argument 를 선언하여 사용 합니다.   


- Response result 처리   
  `NexacroResult.builder() ... build()` 형식으로 `variable`, `dataSet` 메서드의 chaining 사용을 통해 개별 값을 지정하여 반환 합니다.   
  `NexacroResult` 는 DTO class 가 아니며, ReturnValueResolver 를 통해 View 로 반환하기 위한 반환 전용 클래스 입니다.
   따라서, Controller Class 에 해당하는 @RestController 와 Method 에 해당하는 `@ResponseBody` 어노테이션를 선언하지 않아야 합니다.


- Controller method
```java
@PostMapping("/resolver")
public NexacroResult resolverNormal(
        // 단일 값이며, name 속성은 필수로 지정 하여 "id" 라는 명칭으로 판별 합니다. null 일 경우 예외를 발생합니다.
        @VariableParam("id") String id,
        @VariableParam("name") String name,
        // 단일 값이며, name 속성은 필수로 지정 하여 "seq" 라는 명칭으로 판별 합니다. null 일 경우를 허용합니다.
        @VariableParam(name="seq", required = false) int seq,
        // 집합 값이며, name 속성은 필수로 지정 하여 "laptops" 라는 명칭으로 판별 합니다. null 일 경우 예외를 발생합니다.
        @DataSetParam("laptops") List<Laptop> laptops,
        // 집합 값이며, name 속성은 지정 하여 "ds_desktop" 라는 명칭으로 판별 합니다. null 일 경우를 허용합니다.
        @DataSetParam(name="ds_desktop", required = false) Desktop desktop,
        // 집합 값의 사용법과 동일하며, 부분범위 조회를 위한 정보를 받아올 경우 Pagination 타입으로 사용 합니다.
        @DataSetParam("ds_paging") Pagination pagination) {
    Pageable pageable = pagination.pageable();
    Equipment equipment = service.getEquipment(id, name, seq, desktop, laptops, pageable);
    return NexacroResult.builder()
            .variable("id", equipment.getId())
            .variable("name", equipment.getName())
            .dataSet("ds_paging", equipment.getPagination())
            .dataSet("ds_desktop", equipment.getDesktop())
            .dataSet("laptops", equipment.getLaptops())
            .build();
}
```

## HttpMessageConverter
일반적인 POST method 방식을 통해 Body 를 전송하는 경우의 사용법을 따르며,
단일 Request Body 전송의 규칙에 따라서 method argument 도 단일의 객체로 선언하여 정보를 수신합니다.
따라서 클라이언트에서 전송된 정보는 method argument class field 단위 개별적인 값으로 전달 됩니다.   
만약 부분범위 조회 정보를 수신 할 경우, field type 을 Pagination 으로 선언하여 전달 받습니다.
MethodArgumentResolver 방식과는 달리, HttpMessageConverter 는 DTO 객체를 반환하므로,
Controller Class 에 해당하는 `@RestController`, 혹은 Method 에 해당하는 `@ResponseBody` 어노테이션를 선언하여 사용합니다.

- Controller method
```java
@ResponseBody
@PostMapping("/converter")
// @RequestBody 어노테이션으로 선언된 Equipment class 타입의 단일 argument 로 모든 정보를 수신 합니다.
public Equipment converterNormal(@RequestBody Equipment equipment) {
    // 부분범위 조회 정보는 Equipment 내의 Pagination field 값을 사용 합니다.
    Pageable pageable = equipment.getPagination().pageable();
    return service.getEquipment(equipment, pageable);
}
```

클라이언트에서 전송 된 값은 MethodArgumentResolver 사용법과 동일 합니다.   
단일 값은 `@VariableParam`, 집합 값은 `@DataSetParam` 어노테이션을 사용하여 해당 field 에 대응하여 전환 됩니다.
만약 해당 어노테이션이 선언되어 있지 않을 경우 field name 으로 전송 된 값과 매칭 됩니다.   
단, 일반 json 포멧의 전문 전송 시 `@JsonAlias` 를 사용하여 전송 값에 해당하는 이름을 사용하여 수신 받습니다.

- RequestBody 에 해당하는 method argument DTO
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment {
    // 클라이언트에서 "id" 라는 이름으로 전송된 값을 수용하며, field name 에 대응합니다.
    @VariableParam("id")
    private String id;
    private String name;
    private int seq;
    // 일반 json 포멧의 전문 전송 시 jackson 어노테이션을 사용 합니다.
    @JsonAlias("ds_paging")
    // 클라이언트에서 "ds_paging" 라는 이름으로 전송된 값을 수용하며, field name 보다 우선 순위로 처리 됩니다.
    @DataSetParam("ds_paging")
    private Pagination pagination;
    @DataSetParam("ds_desktop")
    private Desktop desktop;
    private List<Laptop> laptops;
}
```

## DataSetRowType
nexacro Grid 에서 사용하는 DataSet 은 클라이언트에서 사용자의 임의 변경에 의한 상태값이 저장됩니다.
변경된 값은 Row 단위로 지정되며, 변경 상태 값은 `일반`, `추가`, `수정`, `삭제` 의 종류로 구분됩니다.

|Content-Type|속성|일반|추가|수정|삭제|
|---|---|---|---|---|---|
|application/PlatformXml|&lt;Row type=""&gt;|normal|insert|update|delete|
|application/PlatformJson|&#95;RowType&#95;|N|I|U|D|
|application/json|rowType|0|1|2|3|

- application/PlatformXml
```xml
<?xml version="1.0" encoding="utf-8"?>
<Root xmlns="http://www.nexacroplatform.com/platform/dataset" ver="4000" >
  <Dataset id="laptops">
    <ColumnInfo>
      <Column id="id" size="20" type="STRING"/>
      <Column id="vendor" size="20" type="STRING"/>
      <Column id="displaySize" size="2" type="INT"/>
    </ColumnInfo>
    <Rows>
      <Row type="normal"> <!-- 일반 -->
        <Col id="id">01</Col>
        <Col id="vendor">lg</Col>
        <Col id="displaySize">15</Col>
      </Row>
      <Row type="insert"> <!-- 생성 -->
        <Col id="id">02</Col>
        <Col id="vendor">lg</Col>
        <Col id="displaySize">15</Col>
      </Row>
      <Row type="update"> <!-- 수정 -->
        <Col id="id">03</Col>
        <Col id="vendor">lg</Col>
        <Col id="displaySize">15</Col>
      </Row>
      <Row type="delete"> <!-- 삭제 -->
        <Col id="id">04</Col>
        <Col id="vendor">lg</Col>
        <Col id="displaySize">15</Col>
      </Row>
    </Rows>
  </Dataset>
</Root>
```

- application/PlatformJson
```json
{
  "version" : "1.0",
  "Datasets": [
    {
      "id": "laptops",
      "ColumnInfo": {
        "Column": [
          {
            "id": "id",
            "type": "string",
            "size": "32"
          },
          {
            "id": "vendor",
            "type": "string",
            "size": "32"
          },
          {
            "id": "displaySize",
            "type": "int",
            "size": "4"
          }
        ]
      },
      "Rows": [
        {
          "_RowType_":"N", /* 일반 */
          "id": "01",
          "vendor": "lg",
          "displaySize": "15"
        },
        {
          "_RowType_":"I", /* 생성 */
          "id": "02",
          "vendor": "lg",
          "displaySize": "15"
        },
        {
          "_RowType_":"U", /* 수정 */
          "id": "03",
          "vendor": "lg",
          "displaySize": "15"
        },
        {
          "_RowType_":"D", /* 삭제 */
          "id": "04",
          "vendor": "lg",
          "displaySize": "15"
        }
      ]
    }
  ]
}
```

- application/json
```json
{
  "laptops": [
    {
      "rowType": 0, /* 일반 */
      "id": "01",
      "vendor": "lg",
      "displaySize": 15
    },
    {
      "rowType": 1, /* 생성 */
      "id": "02",
      "vendor": "lg",
      "displaySize": 15
    },
    {
      "rowType": 2, /* 수정 */
      "id": "03",
      "vendor": "lg",
      "displaySize": 15
    },
    {
      "rowType": 3, /* 삭제 */
      "id": "04",
      "vendor": "lg",
      "displaySize": 15
    }
  ]
}
```

클라이언트에서 전송된 전문은 `prunus-nexacro-adaptor` 의 Deserialize 과정을 거쳐서 row 상태값을 발췌하여 영속적인 작업에 할당할 수 있습니다.
기본 데이타의 스키마로 구성된 DTO 이외의 상태정보를 수신하기 위해서 `DataSetRowType` 를 상속받아 사용합니다.
```java
@Getter
@Setter
public abstract class DataSetRowType implements DataSetRowTypeAccessor {
    // 상태값 수신에 대응하는 field 입니다.
    private int rowType;
}
```
```java
@Getter
@Setter
// DataSetRowType 을 상속받음으로서 rowType 의 정보를 갖게 됩니다.
public class Laptop extends DataSetRowType {
  private String id;
  private String vendor;
  private int displaySize;
}
```

`rowType` 은 `int` 타입이며 , `com.nexacro.java.xapi.data.DataSet` 클래스의 static 상수 값으로 비교하여 구분 합니다.

|구분|클래스 상수|타입 값|
|---|---|---|
|일반|DataSet.ROW_TYPE_NORMAL|0|
|추가|DataSet.ROW_TYPE_INSERTED|1|
|수정|DataSet.ROW_TYPE_UPDATED|2|
|삭제|DataSet.ROW_TYPE_DELETED|3|


```java
    @PostMapping("/resolver/save")
    public NexacroResult resolverSave(
            @DataSetParam("laptops") List<Laptop> laptops) {
        List<Laptop> normal = laptops.stream().filter(laptop -> laptop.getRowType() == DataSet.ROW_TYPE_NORMAL).collect(Collectors.toList());
        List<Laptop> inserted = laptops.stream().filter(laptop -> laptop.getRowType() == DataSet.ROW_TYPE_INSERTED).collect(Collectors.toList());
        List<Laptop> updated = laptops.stream().filter(laptop -> laptop.getRowType() == DataSet.ROW_TYPE_UPDATED).collect(Collectors.toList());
        List<Laptop> deleted = laptops.stream().filter(laptop -> laptop.getRowType() == DataSet.ROW_TYPE_DELETED).collect(Collectors.toList());
        return NexacroResult.builder()
                .dataSet("normal", normal)
                .dataSet("inserted", inserted)
                .dataSet("updated", updated)
                .dataSet("deleted", deleted)
                .build();
    }
```
## ColumnInfo
nexacro client 에서 그리드에 바인드된 데이타셋의 옵션 중, `useclientlayout` 설정이 false 로 되어 있을 경우, 서버에서 응답 받은 전문의 `ColumnInfo` 정보에 따라서 그리드의 해더 정보가 결정 됩니다.   
만약, 조회 질의 결과가 없을 경우 데이타 영역은 없어도 grid 의 상태는 상관 없지만, `ColumnInfo` 정보가 없을 경우 데이타셋 바인드 정보를 의존하고 있는 그리드는 해더 값이 표시 되지 않습니다.
따라서 `ColumnInfo`는 데이타의 존재 여부와는 상관없이 필수적으로 표현되어야 합니다.   
`prunus-nexacro-adaptor` 은 이런 경우 MyBatis 의 SQL 조회 질의 구문을 기준으로 `ColumnInfo` 정보를 생성하여 상시 표시를 보장 합니다.

- columnInfo 정보 상시 표시 
```xml
<Root xmlns="http://www.nexacroplatform.com/platform/dataset">
  <Parameters>
    <Parameter id="ErrorCode" type="int">0</Parameter>
  </Parameters>
  <Dataset id="ds_exuser">
    <!-- 컬럼정보는 Data row 와는 상관없이 항상 표시 됩니다. -->
    <ColumnInfo>
      <Column id="createdBy" type="string" size="32"/>
      <Column id="name" type="string" size="32"/>
      <Column id="id" type="bigdecimal" size="8"/>
      <Column id="married" type="int" size="2"/>
    </ColumnInfo>
    <!-- 질의 결과 Data Row 가 없는 경우 -->
    <Rows>
    </Rows>
  </Dataset>
</Root>
```


## Properties
|이름|설명|기본값|
|---|---|---|
|prunus.nexacro.adaptor.argument-resolver.enabled|argument-resolver 기능 사용 여부|true|
|prunus.nexacro.adaptor.message-converter.enabled|message-converter 기능 사용 여부|true|

## Test Client
`prunus-nexacro-adaptor` 의 전문 송/수신을 테스트하기 위해서는 다음 절차를 통해 테스트 해 볼 수 있습니다.
* `prunus-nexacro-adaptor-example` 을 실행합니다.
    * 보통의 spring boot 의 application 실행 방법과 동일합니다.
* 본인의 환경에 맞는 [Postman](https://www.postman.com/downloads/) 을 다운로드 및 설치합니다.
* **client** 디렉토리의 `prunus-nexacro-adaptor.postman_collection.json` 파일을 Postman 에서 Import 하여 테스트 합니다.