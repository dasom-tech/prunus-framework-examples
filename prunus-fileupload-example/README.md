# prunus-fileupload
`prunus-fileupload` 는 파일 업로드뿐 아니라 다운로드 기능도 함께 제공합니다.   
업로드된 파일의 메타데이터 (원본 파일명, 파일 사이즈, 저장 위치 등) 는 기본적으로 데이터베이스를 사용하여 저장합니다.

데이터베이스 DDL 스크립 는 제공되는 `prunus-fileupload-[version].jar` 내 schema 디렉토리에 데이터베이스 Vendor 별로 제공하므로 해당 스크립트를 실행하여 테이블을 먼저 생성해줘야 합니다.

`prunus-fileupload` 가 메타데이터를 저장할 때 Audit 컬럼의 (생성자, 생성일, 생성자 아이피 등) 데이터를 저장하기 위해서 
**prunus-persistence-data** 를 의존하고 있으며 사용자가 직접 prunus-persistence-data 모듈의 `AuditingAware` 인터페이스를 구현하여
현재 파일을 업로드한 사용자의 정보를 제공해줘야 합니다.   
자세한 `AuditingAware` 구현 방법은  `prunus-persistence-mybatis` 또는 `prunus-persistence-jpa` 매뉴얼 참고바랍니다.

## prunus-fileupload schema
PRUNUS_FILE_BUCKET

|컬렴명|컬럼 한글명|컬럼 타입|NOT NULL|PK|비고|
|---|---|---|:---:|:---:|---|
|FILE_BUCKET_ID|파일 아이디|VARCHAR2(36)|O|O|업로드 파일의 그룹을 식별하는 UUID|
|BACK_NUMBER|파일 식별 아이디|VARCHAR2(8)|O|O|파일 그룹의 개별 파일을 식별하는 랜덤 문자열 8 characters|
|ORIGINAL_FILENAME|원본 파일명|VARCHAR2(100)|O| | |
|CHANGED_FILENAME|변경 파일명|VARCHAR2(36)|O| | |
|EXTENSION|파일 확장자|VARCHAR2(20)| | | |
|SIZE|파일 사이즈|BIGINT|O| | |
|LOCATION|저장 위치|VARCHAR2(200)|O| | |
|DELETED|삭제 여부|BOOLEAN|O| | |
|CREATED_BY|생성자 아이디|VARCHAR2(30)| | |Audit 컬럼|
|CREATED_DATE|생성일시|DATETIME| | |Audit 컬럼|
|CREATED_REMOTE_ADDR|생성자 아이피|VARCHAR2(30)| | |Audit 컬럼|
|MODIFIED_BY|수정자 아이디|VARCHAR2(30)| | |Audit 컬럼|
|MODIFIED_DATE|수정일시|DATETIME| | |Audit 컬럼|
|MODIFIED_REMOTE_ADDR|수정자 아이피|VARCHAR2(30)| | |Audit 컬럼|

```sql
CREATE TABLE PRUNUS_FILE_BUCKET (
    FILE_BUCKET_ID VARCHAR2(36),
    BACK_NUMBER VARCHAR2(8),
    ORIGINAL_FILENAME VARCHAR2(100) NOT NULL,
    CHANGED_FILENAME VARCHAR2(36) NOT NULL,
    EXTENSION VARCHAR2(20),
    SIZE BIGINT NOT NULL,
    LOCATION VARCHAR2(200) NOT NULL,
    DELETED BOOLEAN NOT NULL,
    CREATED_BY VARCHAR2(30),
    CREATED_DATE DATETIME,
    CREATED_REMOTE_ADDR VARCHAR2(30),
    MODIFIED_BY VARCHAR2(30),
    MODIFIED_DATE DATETIME,
    MODIFIED_REMOTE_ADDR VARCHAR2(30),

    PRIMARY KEY (FILE_BUCKET_ID, BACK_NUMBER)
);
```

## Features
* 단일 및 멀티 파일 업로드
* 파일 다운로드 및 멀티 파일 zip 압축 파일 다운로드

## Properties
|이름|설명|기본값|
|---|---|---|
|prunus.fileupload.path|파일 업로드/다운로드 URL 경로|/files|
|prunus.fileupload.root-location|업로드된 파일이 저장될 루트 경로|{user.home}/prunus/upload|
|prunus.fileupload.allowed-file-types|업로드 허용할 파일 형식(확장자), 기본값은 모든 확장자 허용|*|
|prunus.fileupload.enabled-hard-delete|업로드된 파일 삭제시 실제 DB 및 파일을 삭제할지 여부. false 로 설정하면 PRUNUS_FILE_BUCKET 테이블의 DELETED 컬럼에 true로 업데이트하고 업로드된 파일은 삭제하지 않음|true|

`prunus-fileupload` 는 Spring Boot 를 사용하여 구현되어 있습니다. 
그러므로 Spring Boot 의 업로드 관련 Properties 의 영향을 받고 Spring Boot 의 `servlet.multipart` 설정 내용을 어느정도
이해하고 있어야 `prunus-fileupload` 를 적절히 사용할 수 있지만 다음 설정 내용 정도만 이해하고 적용한다면 큰 무리가 없습니다.

```yaml
spring:
  servlet:
    multipart:
      file-size-threshold: 0B # 값을 조정하여 메모리 사용량과 디스크 I/O 사이의 균형을 조절할 수 있습니다. 예를 들어, 1MB로 설정된 경우, 1MB보다 큰 파일은 임시 디렉토리에 저장되고, 1MB보다 작은 파일은 메모리에 저장됩니다. 이 설정을 사용하면 메모리 사용량과 디스크 I/O 사이의 트레이드오프를 조정하여 파일 업로드 성능을 향상시킬 수 있습니다. 기본값은 0B 입니다.
      location: /Users/yongsang/temp # 업로드된 파일의 임시 저장 경로
      max-file-size: 10MB # 업로드 파일의 개별 파일의 크기 제한을 설정할 수 있어요. 10MB로 설정된 경우, 10MB보다 큰 파일은 업로드할 수 없습니다.
      max-request-size: 100MB # 전체 요청의 최대 허용 크기를 나타냅니다. 이 값은 요청에 포함된 모든 파일의 총 크기 제한을 설정하는 데 사용됩니다. 예를 들어, 100MB로 설정된 경우, 모든 파일의 총 크기가 100MB보다 큰 경우에는 업로드할 수 없습니다.
```

## REST API
`prunus-fileupload` 업로드 및 다운로드는 REST API 를 통해서 기능을 제공합니다. REST API 사용하여 
프로젝트 환경에 맞는 화면을 구성하여 자유롭게 기능 구현을 할 수 있습니다.

### 업로드 API
#### 파일 업로드 - POST: /files
반드시 HTTP Method **POST**, Content-Type **multipart/form-data** 형태로 요청을 보내야 합니다.
파일은 단건 또는 여러개의 파일을 업로드 할 수 있으며 `spring.servlet.multipart.max-file-size` 와
`spring.servlet.multipart.max-request-size` 의 제약을 받습니다.
성공적으로 업로드가 되었다면 다음과 같은 응답 메시지를 받을 수 있습니다.
```json
[
    {
        "id": "d937a225-6c10-4dd3-a2d0-b9f8a9c149f2",
        "backNumber": "zUmndFbT",
        "extension": "jpg",
        "size": 676079,
        "filename": "NewJeans.jpg"
    }
]
```
업로드는 단일 및 멀티 파일 업로드를 지원하므로 한 개의 파일을 업로드했다 하더라도 응답 메시지는 **Array** 로 응답하므로 유의하여 
응답 메시지를 사용하세요.

#### 업로드 파일 조회 - GET: /files/{id}
업로드된 파일을 조회합니다.

#### 업로드 파일 삭제 (단건 및 멀티)- DELETE: /files/{id}/{backNumbers}   
업로드 파일의 삭제 요청 API 로 HTTP Method **DELETE** 로 API 를 요청해야 합니다. 삭제 하고자 하는 파일의 id, backnumber 를 URL 경로에 넣고 요청합니다.   

즉, 위 업로드 후 응답으로 받은 메지시로 삭제한다고 가정할 때 `files/d937a225-6c10-4dd3-a2d0-b9f8a9c149f2/zUmndFbT`
와 같은 형태로 API 요청하면 됩니다.   
만약 5개의 파일 중 2개의 파일을 삭제하고 싶다면 ',' comma 를 구분자로 여러개의 파일을 삭제할 수 있습니다.   
`files/d937a225-6c10-4dd3-a2d0-b9f8a9c149f2/zUmndFbT,Awei59L`

응답 메시지는 따로 없으며 HTTP 상태 코드가 200이면 정상적으로 삭제된 것으로 판단하면 됩니다.

#### 업로드 파일 삭제 (전체)- DELETE: /files/{id}
backnumber 없이 업로드 파일의 그룹을 나타내는 id 만 URL 에 포함하여 API 를 요청하면 id 에 해당하는 모든 파일을 삭제할 수 있습니다.   
마찬가지로 응답 메시지는 따로 없으며 HTTP 상태 코드가 200이면 정상적으로 삭제된 것으로 판단하면 됩니다.

### 다운로드 API
#### 다운로드 (단건 및 멀티) - GET /files/{id}/{backNumbers}/download
업로드 파일 다운로드 요청 API 는 단건과 멀티가 통합되어 있습니다.    
단건의 경우는 원본 파일명으로 다운로드되는 반면 여러개의 파일 다운로드는 zip 으로 압축하여 다운로드합니다.

zip 파일로 압축하여 다운로드 하는 경우 zip 파일의 이름이 필요할 수 있는데 **filename=걸그룹.zip** 와 같이 `filename` 파라미터로 파일명을 지정할 수 있습니다.   
만약 filename 파라미터를 전달하지 않았을 경우에는 'yyyyMMddHHmmss' 날짜 및 시간을 기반으로 파일 이름으로 다운로드 합니다.

* filename 파라미터가 있는 경우: 파라미터 값대로 zip 파일로 압축하여 다운로드
* filename 파라미터가 없는 경우: 20230616174119.zip 형식으로 압축하여 다운로드

## 인터페이스 및 확장
`prunus-fileupload` 파일 업로드, 삭제 등의 과정에서 인터페이스를 제공하여 프로젝트 환경에 맞는 구현과 확장을 유연하게 할 수 있는 시스템을 제공합니다.

### FileHandler
`FileHandler` 인터페이스는 업로드되는 파일의 이름 변경, 저장 경로, 업로드 가능 파일인지를 검사하는 기능을 제공합니다.
필요하다면 아래 인터페이스 규격에 맞게 구현체를 구현한 다음 Spring Bean 으로 등록하면 자동으로 prunus-fileupload 가 
사용자가 구현한 `FileHandler` 를 호출하여 동작합니다.

```java
package prunus.fileupload.storage;

public interface FileHandler {
    /**
     * <p>업로드 파일명이 중복되지 않도록 파일명 변경.</p>
     *
     * @param file 업로드 파일
     * @param params 요청 파라미터
     * @return 파일명
     */
    String rename(MultipartFile file, Map<String, String[]> params);

    /**
     * <p>파일 저장 경로</p>
     * 
     * @param file 업로드 파일
     * @param params 요청 파라미터
     * @param props 파일 업로드 property
     * @return 파일 저장 경로
     */
    String getPath(MultipartFile file, Map<String, String[]> params, FileUploadProperties props);

    /**
     * <p>업로드 허용 가능한 파일인지 확인.</p>
     * <p>업로드 불가한 파일이 있을 경우 어떤 파일이 업로드 제한되었는지
     * {@link RestrictedFileTypeException} 예외 클래스에 메시지를 담아 예외 발생</p>
     *
     * @param files 업로드 파일
     * @param params 요청 파라미터
     * @param props 파일 업로드 property
     * @throws RestrictedFileTypeException 업로드 불가 파일이 있을 경우
     */
    void uploadable(List<MultipartFile> files, Map<String, String[]> params, FileUploadProperties props)
            throws RestrictedFileTypeException;
}
```

사용자가 `FileHandler` 를 구현하지 않는 경우 `prunus-fileupload` 내에 있는 `DefaultFileHandler` 가 대신합니다.
```java
public class DefaultFileHandler implements FileHandler {
    @Override
    public String rename(MultipartFile file, Map<String, String[]> params) {
        return RandomStringUtils.randomAlphanumeric(36);
    }

    @Override
    public String getPath(MultipartFile file, Map<String, String[]> params, FileUploadProperties props) {
        return props.getRootLocation();
    }

    @Override
    public void uploadable(List<MultipartFile> files, Map<String, String[]> params, FileUploadProperties props)
            throws RestrictedFileTypeException {
        List<String> allowedFileTypes = props.getAllowedFileTypes();
        if (allowedFileTypes.size() == 1 && allowedFileTypes.get(0).equals("*")) {
            return;
        }

        List<String> restricted = new ArrayList<>(files.size());
        for (MultipartFile file : files) {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            if (!allowedFileTypes.contains(extension)) {
                restricted.add(file.getOriginalFilename());
            }
        }

        if (!restricted.isEmpty()) {
            throw new RestrictedFileTypeException("Not allowed file type [" +
                    String.join(", ", restricted) + "]");
        }
    }
}
```

### FileUploadEventListener
`FileUploadEventListener` 인터페이스는 파일이 업로드된 직후 또는 업로드 실패 시 와 파일이 삭제될 때 사용자가 후처리를 할 수 있는 환경을 제공합니다.
`FileHandler` 와 마찬가지로 `FileUploadEventListener` 구현이 필요할 경우 인터페이스 구현체를 구현한 다음 Spring Bean 으로 등록하면 자동으로 `prunus-fileupload` 가 감지하여
업로드 성공 또는 실패 시 와 파일 삭제 시 상황에 맞는 메서드가 호출됩니다.

```java
package prunus.fileupload;

public interface FileUploadEventListener {
    /**
     * 업로드 완료 시 호출
     * @param event 이벤트 객체
     */
    void onUploadFinished(FileUploadEvent event);

    /**
     * 업로드 실패 시 호출
     * @param event 이벤트 객체
     */
    void onUploadFailed(FileUploadEvent event);

    /**
     * 업로드된 파일 삭제 시 호출
     * @param event 이벤트 객체
     */
    void onFileDeleted(FileUploadEvent event);
}

// ------

package prunus.fileupload;

@Getter
public class FileUploadEvent {
    /**
     * 업로드된 파일
     */
    private final List<Path> storedFiles;

    /**
     * 요청 파라미터
     */
    private final Map<String, String[]> params;

    /**
     * 업로드 실패 시 발생한 예
     */
    private final Exception e;

    public FileUploadEvent(List<Path> storedFiles, Map<String, String[]> params) {
        this(storedFiles, params, null);
    }

    public FileUploadEvent(List<Path> storedFiles, Map<String, String[]> params, Exception e) {
        this.storedFiles = storedFiles;
        this.params = params;
        this.e = e;
    }
}
```

## Test Client
`prunus-fileupload` 의 파일 업로드, 다운로드를 테스트하기 위해서는 다음 절차를 통해 테스트 해 볼 수 있습니다.
* `prunus-fileupload-example` 을 실행합니다.
  * 보통의 spring boot 의 application 실행 방법과 동일합니다. 
* 본인의 환경에 맞는 [Postman](https://www.postman.com/downloads/) 을 다운로드 및 설치합니다.
* **client** 디렉토리의 `prunus-fileupload.postman_collection.json` 파일을 Postman 에서 Import 하여 테스트 합니다.