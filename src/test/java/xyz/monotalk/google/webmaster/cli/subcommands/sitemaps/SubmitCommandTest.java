package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * SubmitCommandTestクラス。
 *
 * <p>SubmitCommandのテストを実行します。</p>
 */
@RunWith(MockitoJUnitRunner.class)
public class SubmitCommandTest {

    /**
     * テスト用のサイトURL定数。
     */
    private static final String SITE_URL = "https://example.com";

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
     * Webmasters.Sitemaps.Submitのモック。
     */
    @Mock
    private Webmasters.Sitemaps.Submit submit;

    /**
     * Webmasters.Sitemaps.Submit リクエストのモック。
     */
    @Mock
    private Webmasters.Sitemaps.Submit submitRequest;

    /**
     * テスト対象のコマンドインスタンス。
     */
    @InjectMocks
    private SubmitCommand command;

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
        command = new SubmitCommand();
        ReflectionTestUtils.setField(command, "factory", factory);
        configureCommand(SITE_URL, "sitemap.xml");
    }

    /**
     * モックのセットアップを行います。
     *
     * @throws IOException モックのセットアップ中に例外が発生した場合
     */
    private void setupMocks() throws IOException {
        when(factory.createClient()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.submit(eq(SITE_URL), eq(SITE_URL + "/sitemap.xml"))).thenReturn(submit);
        doNothing().when(submit).execute();
    }

    /**
     * コマンドを設定します。
     *
     * @param url サイトURL
     * @param feedPath フィードパス
     */
    private void configureCommand(final String url, final String feedPath) {
        try {
            final URI uri = new URI(url);
            ReflectionTestUtils.setField(command, "siteUrl", uri.toURL());
            ReflectionTestUtils.setField(command, "feedpath", feedPath);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("不正なURIフォーマット: " + url, e);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("不正なURLフォーマット: " + url, e);
        }
    }

    /**
     * 正常系: コマンドが正しく実行されることを検証します。
     *
     * @throws IOException I/O例外が発生した場合
     */
    @Test
    public void testExecute正常系() throws IOException {
        // Given
        configureCommand(SITE_URL, "sitemap.xml");

        // When
        command.execute();

        // Then
        verifyApiCalls();
    }

    /**
     * 異常系: コマンド実行時に例外が発生する場合を検証します。
     *
     * @throws IOException I/O例外が発生した場合
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute異常系() throws IOException {
        // Given
        when(factory.createClient())
            .thenThrow(new CommandLineInputOutputException("Mocked CommandLineInputOutputException"));

        // When
        command.execute();
    }

    /**
     * 異常系: サイトURLが設定されていないケース。
     *
     * @throws IOException I/O例外が発生した場合
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteErrorSiteUrlNotSet() throws IOException {
        resetSiteUrl();
        command.execute();
    }

    /**
     * 異常系: フィードパスが設定されていないケース。
     *
     * @throws IOException I/O例外が発生した場合
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteErrorFeedpathNotSet() throws IOException {
        resetFeedPath();
        command.execute();
    }

    /**
     * 正常系: usageメソッドが正しい説明文を返すこと。
     */
    @Test
    public void testUsageReturnsCorrectDescription() {
        final String expected = "Submits a sitemap for this site.";
        final String actual = command.usage();
        assertEquals("コマンドの説明文が正しいこと", expected, actual);
    }

    /**
     * APIの呼び出しを検証します。
     *
     * @throws IOException I/O例外が発生した場合
     */
    private void verifyApiCalls() throws IOException {
        verify(factory).createClient();
        verify(webmasters).sitemaps();
        verify(sitemaps).submit(eq(SITE_URL), eq(SITE_URL + "/sitemap.xml"));
        verify(submit).execute();
    }

    /**
     * サイトURLをリセットします。
     */
    private void resetSiteUrl() {
        command = new SubmitCommand();
        ReflectionTestUtils.setField(command, "factory", factory);
        command.feedpath = "sitemap.xml";
    }

    /**
     * フィードパスをリセットします。
     */
    private void resetFeedPath() {
        command = new SubmitCommand();
        ReflectionTestUtils.setField(command, "factory", factory);
        try {
            final URI uri = new URI(SITE_URL);
            ReflectionTestUtils.setField(command, "siteUrl", uri.toURL());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("不正なURIフォーマット: " + SITE_URL, e);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("不正なURLフォーマット: " + SITE_URL, e);
        }
    }
}