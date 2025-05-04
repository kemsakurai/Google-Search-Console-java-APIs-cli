package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.WebmastersScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Google Webmasters APIクライアント生成ファクトリ
 */
@Component
public class WebmastersFactory {

    /** 
     * アプリケーションのキーファイルの場所
     */
    @Value("${application.keyFileLocation}")
    private String keyFileLocation;

    /**
     * デフォルトコンストラクタ
     */
    public WebmastersFactory() {
        // デフォルトコンストラクタ
    }
    
    /**
     * Webmastersインスタンスを作成します。
     *
     * @return Webmastersクライアントのインスタンス。
     * @throws CommandLineInputOutputException クライアント生成中にエラーが発生した場合。
     */
    public Webmasters create() {
        final HttpTransport httpTransport;
        try {
            httpTransport = createHttpTransport();
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Failed to create HTTP transport: " + e.getMessage(),
                    e);
        }

        final GoogleCredential credential;
        try {
            credential = createCredential();
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to create Google credentials: " + e.getMessage(), e);
        }

        // Create a new authorized API client
        return new Webmasters.Builder(httpTransport, getJsonFactory(), credential)
                .setApplicationName("Search Console Cli")
                .build();
    }

    /**
     * HTTPトランスポートを作成します
     *
     * @return 作成されたNetHttpTransportインスタンス
     * @throws GeneralSecurityException セキュリティ例外が発生した場合
     * @throws IOException 入出力例外が発生した場合
     */
    protected NetHttpTransport createHttpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    /**
     * JSONファクトリを取得します
     *
     * @return JacksonFactoryインスタンス
     */
    protected JacksonFactory getJsonFactory() {
        return JacksonFactory.getDefaultInstance();
    }

    /**
     * 認証情報を作成します
     *
     * @return GoogleCredentialインスタンス
     * @throws IOException 入出力例外が発生した場合
     */
    protected GoogleCredential createCredential() throws IOException {
        return GoogleCredential.fromStream(
                Files.newInputStream(Paths.get(keyFileLocation))
            ).createScoped(Collections.singleton(WebmastersScopes.WEBMASTERS));
    }
}
