package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * GetCommandTestクラスは、GetCommandのテストを行います。
 */
@RunWith(MockitoJUnitRunner.class)
public class GetCommandTest {

    /**
     * ロガーインスタンス。
     */
    private static final Logger LOGGER = Logger.getLogger(GetCommandTest.class.getName());

    /**
     * テスト用のサイトURL定数。
     */
    private static final String TEST_SITE_URL = "https://example.com";

    /**
     * テスト用のフィードパス定数。
     */
    private static final String TEST_FEED_PATH = "sitemap.xml";

    /**
     * WebmastersFactoryのモック。
     */
    @Mock
    private WebmastersFactory factory;

    /**
     * Webmastersクライアントのモック。
     */
    @Mock
    private Webmasters webmasters;

    /**
     * Webmasters.Sitemapsのモック。
     */
    @Mock
    private Webmasters.Sitemaps sitemaps;

    /**
     * Webmasters.Sitemaps.Getのモック。
     */
    @Mock
    private Webmasters.Sitemaps.Get getRequest;

    /**
     * テスト対象のコマンドインスタンス。
     */
    private GetCommand command;

    /**
     * テストのセットアップを行います。
     *
     * @throws IOException モックのセットアップ中に例外が発生した場合
     */
    @Before
    public void setUp() throws IOException {
        initializeCommand();
        setupMocks();
    }

    /**
     * コマンドの初期化を行います。
     *
     * @throws IOException 初期化中に例外が発生した場合
     */
    private void initializeCommand() throws IOException {
        command = new GetCommand();
        ReflectionTestUtils.setField(command, "factory", factory);
        setSiteUrlSafely(TEST_SITE_URL);
        setFeedPath(TEST_FEED_PATH);
    }

    /**
     * モックのセットアップを行います。
     *
     * @throws IOException モックのセットアップ中に例外が発生した場合
     */
    private void setupMocks() throws IOException {
        when(factory.createClient()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.get(anyString(), anyString())).thenReturn(getRequest);
    }

    /**
     * 正常系のテストを実行します。
     *
     * @throws IOException APIリクエスト時に発生する可能性のある例外
     * @throws CommandLineInputOutputException コマンドライン入出力で発生する可能性のある例外
     */
    @Test
    public void testExecuteValidCase() throws IOException {
        // When
        command.execute();

        // Then
        verify(getRequest).execute();
    }

    /**
     * サイトURLが未指定の場合の異常系テスト。
     *
     * @throws IOException I/O例外が発生した場合
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteInvalidCaseSiteUrlNotSpecified() throws IOException {
        resetCommand();
        command.execute();
    }

    /**
     * フィードパスが未指定の場合の異常系テスト。
     *
     * @throws IOException I/O例外が発生した場合
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteInvalidCaseFeedPathNotSpecified() throws IOException {
        command.feedpath = "";
        command.execute();
    }

    /**
     * API呼び出しでIOExceptionが発生する異常系テスト。
     *
     * @throws IOException I/O例外が発生した場合
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecuteWithApiIoException() throws IOException {
        // Given
        when(getRequest.execute()).thenThrow(new IOException("API Error"));

        // When
        command.execute();
    }

    /**
     * サイトURLを安全に設定します。
     *
     * @param url 設定するURL
     */
    private void setSiteUrlSafely(final String url) {
        try {
            final String siteUrl = URI.create(url).toURL().toString();
            ReflectionTestUtils.setField(command, "siteUrl", siteUrl);
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "サイトURLの設定中に例外が発生しました", e);
            }
        }
    }

    /**
     * フィードパスを設定します。
     *
     * @param path 設定するパス
     */
    private void setFeedPath(final String path) {
        command.feedpath = path;
    }

    /**
     * コマンドの状態をリセットします。
     */
    private void resetCommand() {
        command = new GetCommand();
        ReflectionTestUtils.setField(command, "factory", factory);
        command.feedpath = TEST_FEED_PATH;
    }
}