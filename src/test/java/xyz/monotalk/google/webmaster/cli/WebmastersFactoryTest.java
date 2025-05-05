package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.webmasters.Webmasters;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@summary WebmastersFactoryのテストクラス。}
 */
@RunWith(MockitoJUnitRunner.class)
public class WebmastersFactoryTest {

    /**
     * {@summary テスト用のWebmastersFactoryサブクラス。}
     */
    private static class TestWebmastersFactory extends WebmastersFactory {
        private final TransportFactory transportFactory;
        private final JsonFactoryProvider jsonFactoryProvider;
        private final CredentialFactory credentialFactory;

        public TestWebmastersFactory(
                TransportFactory transportFactory,
                JsonFactoryProvider jsonFactoryProvider,
                CredentialFactory credentialFactory) {
            this.transportFactory = transportFactory;
            this.jsonFactoryProvider = jsonFactoryProvider;
            this.credentialFactory = credentialFactory;
        }

        @Override
        protected NetHttpTransport createHttpTransport() throws GeneralSecurityException, IOException {
            return (NetHttpTransport) transportFactory.createTransport();
        }

        @Override
        protected JacksonFactory getJsonFactory() {
            return jsonFactoryProvider.getJsonFactory();
        }

        @Override
        protected GoogleCredentials createCredential() throws IOException {
            return credentialFactory.createCredential();
        }
    }

    /**
     * {@summary HttpTransportを生成するファクトリインターフェース。}
     */
    interface TransportFactory {
        HttpTransport createTransport() throws GeneralSecurityException, IOException;
    }

    /**
     * {@summary JacksonFactoryを提供するインターフェース。}
     */
    interface JsonFactoryProvider {
        JacksonFactory getJsonFactory();
    }

    /**
     * {@summary GoogleCredentialsを生成するファクトリインターフェース。}
     */
    interface CredentialFactory {
        GoogleCredentials createCredential() throws IOException;
    }

    @Mock
    private TransportFactory mockTransportFactory;

    @Mock
    private JsonFactoryProvider mockJsonFactoryProvider;

    @Mock
    private CredentialFactory mockCredentialFactory;

    @Mock
    private NetHttpTransport mockHttpTransport;

    @Mock
    private GoogleCredentials mockCredential;

    @Mock
    private JacksonFactory mockJsonFactory;

    private WebmastersFactory factory;

    /**
     * {@summary テストの前準備。}
     */
    @Before
    public void setUp() throws Exception {
        when(mockTransportFactory.createTransport()).thenReturn(mockHttpTransport);
        when(mockJsonFactoryProvider.getJsonFactory()).thenReturn(mockJsonFactory);
        when(mockCredentialFactory.createCredential()).thenReturn(mockCredential);

        factory = new TestWebmastersFactory(mockTransportFactory, mockJsonFactoryProvider, mockCredentialFactory);
        ReflectionTestUtils.setField(factory, "keyFileLocation", "test-key.json");
    }

    /**
     * {@summary Webmastersインスタンスが正常に生成されることをテスト。}
     */
    @Test
    public void testCreate_shouldCreateWebmastersInstance() throws Exception {
        // When
        Webmasters result = factory.create();

        // Then
        assertNotNull("Webmastersインスタンスが生成されること", result);
        verify(mockTransportFactory).createTransport();
        verify(mockJsonFactoryProvider).getJsonFactory();
        verify(mockCredentialFactory).createCredential();
    }

    /**
     * {@summary HttpTransport生成時に例外が発生した場合のテスト。}
     */
    @Test(expected = IllegalStateException.class)
    public void testCreate_shouldThrowExceptionWhenTransportFails() throws Exception {
        // Given
        when(mockTransportFactory.createTransport()).thenThrow(new IOException("Transport error"));

        // When
        factory.create();
    }

    /**
     * {@summary 認証情報読み込み時に例外が発生した場合のテスト。}
     */
    @Test(expected = IllegalStateException.class)
    public void testCreate_shouldThrowExceptionWhenCredentialFails() throws Exception {
        // Given
        when(mockCredentialFactory.createCredential()).thenThrow(new IOException("Credential error"));

        // When
        factory.create();
    }
}