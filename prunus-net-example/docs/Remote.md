# remote package
prunus-net 의 remote package 는 SSH, FTP 와 같은 원격 서버와의 파일 전송, 다운로드 및 명령 실행을 지원합니다.

## Feature
- SSH, FTP 파일 업로드
- SSH, FTP 파일 다운로드
- SSH, FTP 파일명 변경
- SSH, FTP 접속 테스트
- SSH 명령 실행 (FTP 미지원)

## Setup
prunus-net 모듈 의존
```groovy
implementation 'sdcit.prunus:prunus-net'
```

## API
원격 서버와의 통신은 `prunus.net.remote.RemoteChannel` 인터페이스 클래스가 대변하고 `prunus.net.remote.RemoteChannel`
인터페이스를 구현한 `SecureRemoteChannel` (SSH, SFTP) 와 `FtpRemoteChannel` 클래스를 제공합니다.

```java
package prunus.net.remote;

import java.io.ByteArrayOutputStream;

/**
 * <p>RemoteChannel 인터페이스는 원격 통신 채널을 나타내며, SSH나 FTP와 같은 원격 서버와의 파일 전송 및 명령 실행을 가능하게 한다.</p>
 * 이 인터페이스를 구현하는 클래스는 원격 통신 프로토콜을 처리하고, 원격 서버에서 필요한 작업을 수행해야 한다.
 */
public interface RemoteChannel {
    /**
     * 지정된 로컬 파일들을 원격 서버로 업로드.
     *
     * @param localFiles 업로드할 로컬 파일 경로의 배열
     * @throws RemoteChannelException 업로드 과정에서 오류가 발생한 경우
     */
    void put(String[] localFiles) throws RemoteChannelException;

    /**
     * 지정된 로컬 파일들을 원격 서버의 지정된 디렉토리로 업로드.
     *
     * @param remoteDir 파일을 업로드할 원격 디렉토리
     * @param localFiles 업로드할 로컬 파일 경로의 배열
     * @throws RemoteChannelException 업로드 과정에서 오류가 발생한 경우
     */
    void put(String remoteDir, String[] localFiles) throws RemoteChannelException;

    /**
     * 지정된 원격 파일들을 원격 서버에서 다운로드.
     *
     * @param remoteFiles 다운로드할 원격 파일 경로의 배열
     * @return 다운로드한 파일 내용을 나타내는 ByteArrayOutputStream 배열
     * @throws RemoteChannelException 다운로드 과정에서 오류가 발생한 경우
     */
    ByteArrayOutputStream[] get(String[] remoteFiles) throws RemoteChannelException;

    /**
     * 지정된 원격 파일들을 원격 서버의 지정된 디렉토리에서 다운로드.
     *
     * @param remoteDir 파일을 다운로드할 원격 디렉토리
     * @param remoteFiles 다운로드할 원격 파일 경로의 배열
     * @return 다운로드한 파일 내용을 나타내는 ByteArrayOutputStream 배열
     * @throws RemoteChannelException 다운로드 과정에서 오류가 발생한 경우
     */
    ByteArrayOutputStream[] get(String remoteDir, String[] remoteFiles) throws RemoteChannelException;

    /**
     * 원격 서버에서 파일의 이름을 변경.
     *
     * @param source 원본 파일 경로
     * @param dest 변경할 파일 경로
     * @throws RemoteChannelException 이름 변경 과정에서 오류가 발생한 경우
     */
    void rename(String source, String dest) throws RemoteChannelException;

    /**
     * 원격 서버에서 지정된 명령을 실행하고, 결과를 문자열로 반환.
     *
     * @param command 실행할 명령
     * @return 명령 실행 결과
     * @throws RemoteChannelException 명령 실행 과정에서 오류가 발생한 경우
     */
    String exec(String command) throws RemoteChannelException;

    /**
     * 원격 서버와의 연결을 테스트.
     *
     * @return 연결이 성공한 경우 true, 그렇지 않은 경우 false를 반환.
     * @throws RemoteChannelException 연결 테스트 과정에서 오류가 발생한 경우
     */
    boolean test() throws RemoteChannelException;

    /**
     * 원격 채널을 닫고 관련된 모든 리소스를 해제.
     *
     * @throws RemoteChannelException 채널 닫기 과정에서 오류가 발생한 경우
     */
    void close() throws RemoteChannelException;
}

```
`SecureRemoteChannel` 와 `FtpRemoteChannel` 클래스를 사용하기 위해서는 **원격 서버에 대한 연결 정보** `prunus.net.remote.ConnectionInfo` 객체를 
생성자로 전달해야 합니다.

```java
package prunus.net.remote;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

/**
 * ConnectionInfo 클래스는 원격 서버에 대한 연결 정보를 저장하는 클래스.
 */
@Getter
@Setter
public class ConnectionInfo {
    private String hostname;
    private int port;
    private String username;
    private String password;
    private Properties config;

    public ConnectionInfo() {}

    /**
     * ConnectionInfo 객체를 생성하는 빌더 패턴의 생성자.
     *
     * @param hostname 원격 서버의 호스트 이름
     * @param port 원격 서버의 포트 번호
     * @param username 원격 서버에 접속할 사용자 이름
     * @param password 원격 서버에 접속할 사용자 비밀번호
     * @param config 기타 구성 설정을 포함하는 Properties 객체
     */
    @Builder
    public ConnectionInfo(String hostname, int port, String username, String password, Properties config) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.config = config;
    }
}
```

자세한 사용 방법은 example 프로젝트의 `net.remote.SshClient`, `net.remote.FtpClient` 를 참고하세요.
