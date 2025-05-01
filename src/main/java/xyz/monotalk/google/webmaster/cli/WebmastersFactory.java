package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.WebmastersScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * WebmastersFactory
 */
@Component
public class WebmastersFactory {

    @Value("${application.keyFileLocation}") private String keyFileLocation;

    /**
     * Create Webmasters instance.
     *
     * @return Webmastersインスタンス
     */
    public Webmasters create() {
        HttpTransport httpTransport;
        try {
            httpTransport = createHttpTransport();
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException(e);
        }

        JsonFactory jsonFactory = getJsonFactory();
        GoogleCredential credential;
        try {
            credential = createCredential();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        // Create a new authorized API client
        return new Webmasters.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Search Console Cli")
                .build();
    }

    /**
     * HTTPトランスポートを作成します
     */
    protected NetHttpTransport createHttpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    /**
     * JSONファクトリを取得します
     */
    protected JacksonFactory getJsonFactory() {
        return JacksonFactory.getDefaultInstance();
    }

    /**
     * 認証情報を作成します
     */
    protected GoogleCredential createCredential() throws IOException {
        return GoogleCredential
                .fromStream(new FileInputStream(keyFileLocation))
                .createScoped(Collections.singleton(WebmastersScopes.WEBMASTERS));
    }
}
