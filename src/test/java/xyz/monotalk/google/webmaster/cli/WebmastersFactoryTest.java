package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.webmasters.Webmasters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WebmastersFactoryTest {

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
        protected GoogleCredential createCredential() throws IOException {
            return credentialFactory.createCredential();
        }
    }

    interface TransportFactory {
        HttpTransport createTransport() throws GeneralSecurityException, IOException;
    }

    interface JsonFactoryProvider {
        JacksonFactory getJsonFactory();
    }

    interface CredentialFactory {
        GoogleCredential createCredential() throws IOException;
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
    private GoogleCredential mockCredential;

    @Mock
    private JacksonFactory mockJsonFactory;

    private WebmastersFactory factory;

    @Before
    public void setUp() throws Exception {
        when(mockTransportFactory.createTransport()).thenReturn(mockHttpTransport);
        when(mockJsonFactoryProvider.getJsonFactory()).thenReturn(mockJsonFactory);
        when(mockCredentialFactory.createCredential()).thenReturn(mockCredential);

        factory = new TestWebmastersFactory(mockTransportFactory, mockJsonFactoryProvider, mockCredentialFactory);
        ReflectionTestUtils.setField(factory, "keyFileLocation", "test-key.json");
    }

    @Test
    public void testCreate_正常系_Webmastersインスタンスが生成される() throws Exception {
        // When
        Webmasters result = factory.create();

        // Then
        assertNotNull("Webmastersインスタンスが生成されること", result);
        verify(mockTransportFactory).createTransport();
        verify(mockJsonFactoryProvider).getJsonFactory();
        verify(mockCredentialFactory).createCredential();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreate_異常系_HTTPTransport生成時の例外() throws Exception {
        // Given
        when(mockTransportFactory.createTransport()).thenThrow(new IOException("Transport error"));

        // When
        factory.create();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreate_異常系_認証情報読み込み時の例外() throws Exception {
        // Given
        when(mockCredentialFactory.createCredential()).thenThrow(new IOException("Credential error"));

        // When
        factory.create();
    }
}