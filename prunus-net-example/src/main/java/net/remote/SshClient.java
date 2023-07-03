package net.remote;

import org.springframework.core.io.ClassPathResource;
import prunus.net.remote.ConnectionInfo;
import prunus.net.remote.RemoteChannel;
import prunus.net.remote.SecureRemoteChannel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class SshClient {
    private File filesDir = null;
    private File downloadDir = null;
    private ConnectionInfo connectionInfo = null;

    public SshClient() throws Exception {
        init();
    }

    public void put() {
        RemoteChannel channel = new SecureRemoteChannel(connectionInfo);
        try {
            channel.put(downloadDir.getPath(), new String[] {new File(filesDir, "NewJeans.jpg").getPath()} );
        } finally {
            channel.close();
        }
    }

    public void get() {
        RemoteChannel channel = new SecureRemoteChannel(connectionInfo);
        try {
            // 테스트를 위해 임의의 파일을 먼저 sftp 에 업로드
            channel.put(downloadDir.getPath(), new String[] {new File(filesDir, "NewJeans.jpg").getPath()} );
            // 리턴 받은 outputStreams 을 서버에 원하는 위치에 파일로 write 하면 됩니다.
            ByteArrayOutputStream[] outputStreams = channel.get(downloadDir.getPath(), new String[] {"NewJeans.jpg"});
        } finally {
            channel.close();
        }
    }

    public void rename() {
        RemoteChannel channel = new SecureRemoteChannel(connectionInfo);
        try {
            // 테스트를 위해 임의의 파일을 먼저 sftp 에 업로드
            channel.put(downloadDir.getPath(), new String[] {new File(filesDir, "NewJeans.jpg").getPath()} );
            channel.rename(downloadDir.getPath() + "/NewJeans.jpg", downloadDir.getPath() + "/abc.jpg");
        } finally {
            channel.close();
        }
    }

    public void exec() {
        RemoteChannel channel = new SecureRemoteChannel(connectionInfo);
        try {
            String script = new File(filesDir, "print.sh").getPath();
            String output = channel.exec(script);
            System.out.println("output = " + output);
        } finally {
            channel.close();
        }
    }

    // 테스트 환경 구성
    private void init() throws Exception {
        // 사용자 환경에 맞는 정보를 입력하세요.
        connectionInfo = ConnectionInfo.builder()
                .hostname("localhost")
                .port(22)
                .username("yongsang")
                .password("xxx")
                .build();

        filesDir = new ClassPathResource("files").getFile();
        try {
            downloadDir = new ClassPathResource("download").getFile();
        } catch (FileNotFoundException e) {
            downloadDir = new File(filesDir.getParent(), "download");
            downloadDir.mkdir();
        }
    }

    // 테스트 환경 정리
    public void cleanup() throws Exception {
        for (File file : Objects.requireNonNull(downloadDir.listFiles())) {
            file.delete();
        }
    }

    public static void main(String[] args) throws Exception {
        SshClient client = new SshClient();
        client.put();
        client.get();
        client.rename();
        client.exec();

        // 테스트 환경 정리
        client.cleanup();
    }
}
