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
|HttpMessageConverter|클라이언트에서 전송된 값을 하나의 aurment 로 수신하여 내부의 개별 값으로 사용|반환하고자 하는 개별 값을 하나의 집합 객체의 필드 정보로 지정하여 반환|

## MethodArgumentResolver

- **@VariableParam**   
  단일 값을 전송 받을 경우 사용하며, `name` 속성에 따라 클라이언트에서 전송된 전문의 해당 값으로 받아옵니다.   
  속성은 다음과 같은 사항으로 사용합니다.

  |속성|설명|기본값|필수사용여부|
  |---|---|---|:---:|
  |name or value|지정된 값에 대응하는 클라이언트 전문의 값을 판별 합니다.|없음|O|
  |required|클라이언트로 부터 전달된 전문의 값의 필수 여부를 지정합니다.|true|X|

- **@DataSetParam**   
  집합 개념의 값을 전송 받을 경우 사용하며, 속성 값은 @Variable 과 사용법이 동일 합니다.   
  값이 목록형으로 전송되었다 하더라도, argument type 이 단일 객체 타입으로 선언되어 있을 경우 단일 객체 타입으로 자동으로 받아집니다.   
  부분범위 조회를 위한 정보를 수신하기 위해서는 `Pagination` class type 의 argument 를 선언하여 사용 합니다.   


- Response result 처리   
  `NexacroResult.builder() ... build()` 형식으로 `variable`, `dataSet` 메서드의 chaining 사용을 통해 개별 값을 지정하여 반환 합니다.   
  `NexacroResult` 는 DTO class 가 아니며, ReturnValueResolver 를 통해 View 로 반환되므로, `@ResponseBody` 를 선언하지 않아야 합니다.


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

클라이언트에서 전송 된 값의 이름은 `@JsonAlias` 어노테이션을 사용하여 해당 field 에 대응하여 전환 됩니다.
만약 `@JsonAlias` 의 이름이 선언되어 있지 않으면 field name 으로 전환 됩니다.

- RequestBody 에 해당하는 method argument DTO
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment {
    // 클라이언트에서 "id" 라는 이름으로 전송된 값을 수용하며, field name 에 대응합니다.
    private String id;
    private String name;
    private int seq;
    // 클라이언트에서 "ds_paging" 라는 이름으로 전송된 값을 수용하며, field name 보다 우선 순위로 처리 됩니다.
    @JsonAlias("ds_paging")
    private Pagination pagination;
    @JsonAlias("ds_desktop")
    private Desktop desktop;
    private List<Laptop> laptops;
}
```

## Test Client
`prunus-nexacro-adaptor` 의 전문 송/수신을 테스트하기 위해서는 다음 절차를 통해 테스트 해 볼 수 있습니다.
* `prunus-nexacro-adaptor-example` 을 실행합니다.
    * 보통의 spring boot 의 application 실행 방법과 동일합니다.
* 본인의 환경에 맞는 [Postman](https://www.postman.com/downloads/) 을 다운로드 및 설치합니다.
* **client** 디렉토리의 `prunus-nexacro-adaptor.postman_collection.json` 파일을 Postman 에서 Import 하여 테스트 합니다.