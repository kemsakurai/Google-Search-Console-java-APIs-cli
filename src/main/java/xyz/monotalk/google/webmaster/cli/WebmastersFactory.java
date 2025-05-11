package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.WebmastersScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Google Webmasters APIクライアントを生成するファクトリクラスです。
 * Java 21の機能を活用して最適化されています。
 */
@Component
public class WebmastersFactory {

    /** 
     * アプリケーションのキーファイルの場所です。
     */
    @Value("${application.keyFileLocation}")
    private String keyFileLocation;

    /**
     * デフォルトコンストラクタです。
     */
    public WebmastersFactory() {
        // デフォルトコンストラクタ
    }
    
    /**
     * Google Webmasters APIクライアントを生成します。
     *
     * @return Google Webmasters APIクライアントのインスタンス。
     * @throws IllegalStateException クライアントの生成に失敗した場合。
     */
    public Webmasters createClient() {
        try {
            // クライアント生成ロジック
            return new Webmasters.Builder(
                createHttpTransport(), 
                getJsonFactory(), 
                new HttpCredentialsAdapter(createCredential()))
                    .setApplicationName("Search Console Cli")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Failed to create Webmasters client", e);
        }
    }

    /**
     * Google Webmasters APIクライアントを生成します。
     *
     * @return Webmasters APIクライアント
     * @throws GeneralSecurityException セキュリティ例外が発生した場合
     * @throws IOException 入出力例外が発生した場合
     */
    public Webmasters create() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        final GoogleCredentials credentials = GoogleCredentials
            .fromStream(Files.newInputStream(Paths.get("credentials.json")))
            .createScoped(Collections.singleton(WebmastersScopes.WEBMASTERS_READONLY));

        return new Webmasters.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
                .setApplicationName("Google-Search-Console-CLI")
                .build();
    }

    /**
     * HTTPトランスポートを作成します。
     *
     * @return 作成されたNetHttpTransportインスタンス
     * @throws GeneralSecurityException セキュリティ例外が発生した場合
     * @throws IOException 入出力例外が発生した場合
     */
    protected NetHttpTransport createHttpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    /**
     * JSONファクトリを取得します。
     *
     * @return JsonFactoryインスタンス
     */
    protected JsonFactory getJsonFactory() {
        return GsonFactory.getDefaultInstance();
    }

    /**
     * 認証情報を作成します。
     *
     * @return GoogleCredentialsインスタンス
     * @throws IOException 入出力例外が発生した場合
     */
    protected GoogleCredentials createCredential() throws IOException {
        // Java NIOのPaths.getとFiles.newInputStreamを使用
        return GoogleCredentials.fromStream(
                Files.newInputStream(Paths.get(keyFileLocation))
            ).createScoped(Collections.singleton(WebmastersScopes.WEBMASTERS));
    }
}
