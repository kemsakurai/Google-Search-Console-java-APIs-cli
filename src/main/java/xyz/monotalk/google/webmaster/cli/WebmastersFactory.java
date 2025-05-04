package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.WebmastersScopes;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Google Webmasters APIクライアントを生成するファクトリクラスです。
 */
@Component
public class WebmastersFactory {

    @Value("${application.keyFileLocation}")
    private String keyFileLocation;

    /**
     * Webmastersクライアントを生成します。
     *
     * @return Webmastersクライアントのインスタンス。
     * @throws CommandLineInputOutputException クライアント生成中にエラーが発生した場合。
     */
    public Webmasters create() throws CommandLineInputOutputException {
        HttpTransport httpTransport;
        try {
            httpTransport = createHttpTransport();
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Failed to create HTTP transport: " + e.getMessage(),
                    e);
        }

        JsonFactory jsonFactory = getJsonFactory();
        GoogleCredential credential;
        try {
            credential = createCredential();
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to create Google credentials: " + e.getMessage(), e);
        }

        // Create a new authorized API client
        return new Webmasters.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Search Console Cli").build();
    }

    /**
     * Google APIと通信するためのHTTPトランスポートを作成します。
     *
     * @return 信頼されたNetHttpTransportインスタンス。
     * @throws GeneralSecurityException セキュリティエラーが発生した場合。
     * @throws IOException I/Oエラーが発生した場合。
     */
    protected NetHttpTransport createHttpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    /**
     * JSONの解析と生成のためのデフォルトJSONファクトリを取得します。
     *
     * @return JacksonFactoryインスタンス。
     */
    protected JacksonFactory getJsonFactory() {
        return JacksonFactory.getDefaultInstance();
    }

    /**
     * API要求を認証するためのGoogle認証情報を作成します。
     *
     * @return 必要なスコープを持つGoogleCredentialインスタンス。
     * @throws IOException キーファイルの読み込み中にエラーが発生した場合。
     */
    protected GoogleCredential createCredential() throws IOException {
        return GoogleCredential.fromStream(new FileInputStream(keyFileLocation))
                .createScoped(Collections.singleton(WebmastersScopes.WEBMASTERS));
    }
}
