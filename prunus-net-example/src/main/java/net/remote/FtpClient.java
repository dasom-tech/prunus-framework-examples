package net.remote;

import org.springframework.core.io.ClassPathResource;
import prunus.net.remote.ConnectionInfo;
import prunus.net.remote.RemoteChannel;
import prunus.net.remote.FtpRemoteChannel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class FtpClient {
    private File filesDir = null;
    private File downloadDir = null;
    private ConnectionInfo connectionInfo = null;

    public FtpClient() throws Exception {
        init();
    }

    public void put() {
        RemoteChannel channel = new FtpRemoteChannel(connectionInfo);
        try {
            channel.put(downloadDir.getPath(), new String[] {new File(filesDir, "NewJeans.jpg").getPath()} );
        } finally {
            channel.close();
        }
    }

    public void get() {
        RemoteChannel channel = new FtpRemoteChannel(connectionInfo);
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
        RemoteChannel channel = new FtpRemoteChannel(connectionInfo);
        try {
            // 테스트를 위해 임의의 파일을 먼저 sftp 에 업로드
            channel.put(downloadDir.getPath(), new String[] {new File(filesDir, "NewJeans.jpg").getPath()} );
            channel.rename(downloadDir.getPath() + "/NewJeans.jpg", downloadDir.getPath() + "/abc.jpg");
        } finally {
            channel.close();
        }
    }

    public void exec() {
        RemoteChannel channel = new FtpRemoteChannel(connectionInfo);
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
        connectionInfo = ConnectionInfo.builder()
                .hostname("localhost")
                .port(21)
                .username("chotire")
                .password("1234")
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
        FtpClient client = new FtpClient();
        client.put();
        client.get();
        client.rename();
        
        try {
            // ftp 는 exec 메서드를 지원하지 않습니다.
            client.exec();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }

        // 테스트 환경 정리
        client.cleanup();
    }
}
