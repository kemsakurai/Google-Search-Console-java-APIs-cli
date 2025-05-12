package xyz.monotalk.google.webmaster.cli;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.webmasters.Webmasters;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import xyz.monotalk.google.webmaster.cli.test.CredentialFactory;
import xyz.monotalk.google.webmaster.cli.test.JsonFactoryProvider;
import xyz.monotalk.google.webmaster.cli.test.TransportFactory;

/**
 * WebmastersFactoryのユニットテストクラス。
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class WebmastersFactoryTest {

    /** テスト用のキーファイルパス。 */
    private static final String TEST_KEY_FILE = "test-key.json";

    /** モックされたTransportFactory。 */
    @Mock
    private TransportFactory transportFactory;

    /** モックされたJsonFactoryProvider。 */
    @Mock
    private JsonFactoryProvider jsonFactory;

    /** モックされたCredentialFactory。 */
    @Mock
    private CredentialFactory credentialFactory;

    /** モックされたNetHttpTransport。 */
    @Mock
    private NetHttpTransport httpTransport;

    /** モックされたGoogleCredentials。 */
    @Mock
    private GoogleCredentials credentials;

    /** モックされたGsonFactory。 */
    @Mock
    private GsonFactory gsonFactory;

    /** テスト対象のWebmastersFactory。 */
    private WebmastersFactory factory;

    /**
     * テストの前準備。
     *
     * @throws Exception セットアップ中の例外
     */
    @Before
    public void setUp() throws Exception {
        when(transportFactory.createTransport()).thenReturn(httpTransport);
        when(jsonFactory.get()).thenReturn(gsonFactory);
        when(credentialFactory.create()).thenReturn(credentials);

        factory = new TestWebmastersFactory();
        ReflectionTestUtils.setField(factory, "keyFileLocation", TEST_KEY_FILE);
    }

    /**
     * Webmastersインスタンスの正常生成テスト。
     *
     * @throws Exception テスト実行中の例外
     */
    @Test
    public void testCreateWebmastersInstance() throws Exception {
        // When
        final Webmasters result = factory.createClient();

        // Then
        assertNotNull("Webmastersインスタンスが生成されること", result);
        verify(transportFactory).createTransport();
        verify(jsonFactory).get();
        verify(credentialFactory).create();
    }

    /**
     * HttpTransport生成時の例外ハンドリングテスト。
     *
     * @throws Exception テスト実行中の例外
     */
    @Test(expected = IllegalStateException.class)
    public void testHttpTransportException() throws Exception {
        // Given - 一旦正常なモックを設定
        when(transportFactory.createTransport()).thenReturn(null);
        
        // テスト対象のWebmastersFactory匿名サブクラスを作成
        WebmastersFactory testFactory = new TestWebmastersFactory() {
            @Override
            protected NetHttpTransport createHttpTransport() throws GeneralSecurityException, IOException {
                throw new IOException("Transport error");
            }
        };
        ReflectionTestUtils.setField(testFactory, "keyFileLocation", TEST_KEY_FILE);

        // When
        testFactory.createClient();
    }

    /**
     * 認証情報生成時の例外ハンドリングテスト。
     *
     * @throws Exception テスト実行中の例外
     */
    @Test(expected = IllegalStateException.class)
    public void testCredentialException() throws Exception {
        // Given - 一旦正常なモックを設定
        when(credentialFactory.create()).thenReturn(credentials);

        // テスト対象のWebmastersFactory匿名サブクラスを作成
        WebmastersFactory testFactory = new TestWebmastersFactory() {
            @Override
            protected GoogleCredentials createCredential() throws IOException {
                throw new IOException("Credential error");
            }
        };
        ReflectionTestUtils.setField(testFactory, "keyFileLocation", TEST_KEY_FILE);

        // When
        testFactory.createClient();
    }

    /**
     * テスト用のWebmastersFactoryサブクラス。
     */
    @SuppressWarnings("PMD.TestClassWithoutTestCases")
    private class TestWebmastersFactory extends WebmastersFactory {
        @Override
        protected NetHttpTransport createHttpTransport() throws GeneralSecurityException, IOException {
            return transportFactory.createTransport();
        }

        @Override
        protected GsonFactory getJsonFactory() {
            return jsonFactory.get();
        }

        @Override
        protected GoogleCredentials createCredential() throws IOException {
            return credentialFactory.create();
        }
    }
}