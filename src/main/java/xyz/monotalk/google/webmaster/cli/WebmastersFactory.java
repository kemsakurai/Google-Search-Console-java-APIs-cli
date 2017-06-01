package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
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

    @Value("${application.keyFileLocation}")
    private String keyFileLocation;

    /**
     * Create Webmasters instance.
     *
     * @return
     */
    public Webmasters create() {
        HttpTransport httpTransport;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleCredential credential;
        try {
            credential = GoogleCredential.fromStream(new FileInputStream(keyFileLocation)).createScoped(Collections.singleton(WebmastersScopes.WEBMASTERS));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        // Create a new authorized API client
        Webmasters service = new Webmasters.Builder(httpTransport, jsonFactory, credential).setApplicationName("Search Console Cli").build();
        return service;
    }
}
