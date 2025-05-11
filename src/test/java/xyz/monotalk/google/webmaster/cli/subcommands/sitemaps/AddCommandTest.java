package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;
import xyz.monotalk.google.webmaster.cli.subcommands.sites.AddCommand;

/**
 * AddCommandTestクラス。
 *
 * <p>AddCommandのテストを実行します。</p>
 */
@RunWith(MockitoJUnitRunner.class)
public class AddCommandTest {

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
     * サイトマップ操作のモック。
     */
    @Mock
    private Webmasters.Sitemaps sitemaps;

    /**
     * サイトマップ送信操作のモック。
     */
    @Mock
    private Webmasters.Sitemaps.Submit submit;

    /**
     * テスト対象のコマンドインスタンス。
     */
    @InjectMocks
    private AddCommand command;

    /**
     * テストのセットアップを行います。
     *
     * @throws IOException モックのセットアップ中に例外が発生した場合
     */
    @Before
    public void setUp() throws IOException {
        initializeMocks();
    }

    /**
     * モックの初期化を行います。
     *
     * @throws IOException モックの初期化中に例外が発生した場合
     */
    private void initializeMocks() throws IOException {
        setupSitesApi();
        setupSitemapsApi();
    }

    /**
     * Sites APIのモックをセットアップします。
     *
     * @throws IOException モックのセットアップ中に例外が発生した場合
     */
    private void setupSitesApi() throws IOException {
        final Webmasters.Sites sites = mock(Webmasters.Sites.class);
        final Webmasters.Sites.Add add = mock(Webmasters.Sites.Add.class);

        when(factory.createClient()).thenReturn(webmasters);
        when(webmasters.sites()).thenReturn(sites);
        when(sites.add(SITE_URL)).thenReturn(add);
        doNothing().when(add).execute();
    }

    /**
     * Sitemaps APIのモックをセットアップします。
     *
     * @throws IOException モックのセットアップ中に例外が発生した場合
     */
    private void setupSitemapsApi() throws IOException {
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.submit(anyString(), anyString())).thenReturn(submit);
        doNothing().when(submit).execute();
    }

    /**
     * 正常系: コマンドが正しく実行されることを検証します。
     *
     * @throws IOException 入出力例外が発生した場合
     */
    @Test
    public void testExecute正常系() throws IOException {
        // Given
        configureCommand(SITE_URL, null);

        // When
        command.execute();

        // Then
        verifyApiCalls();
    }

    /**
     * 正常系: JSON形式で出力されることを検証します。
     *
     * @throws IOException 入出力例外が発生した場合
     */
    @Test
    public void testExecute正常系Jsonフォーマット出力() throws IOException {
        // Given
        configureCommand(SITE_URL, Format.JSON);

        // When
        command.execute();

        // Then
        verifyApiCalls();
    }

    /**
     * 正常系: サイトが正常に追加されることを検証します。
     *
     * @throws IOException 入出力例外が発生した場合
     */
    @Test
    public void testExecute正常系サイト追加成功() throws IOException {
        // Given
        configureCommand(SITE_URL, null);

        // When
        command.execute();

        // Then
        verifyBasicApiCalls();
    }

    /**
     * 異常系: サイトURLが指定されていない場合に例外が発生することを検証します。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteErrorSiteUrlNotSpecified() {
        // Given
        resetCommand();

        // When
        command.execute();
    }

    /**
     * 異常系: サイトマップURLが指定されていない場合に例外が発生することを検証します。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteErrorSitemapUrlNotSpecified() {
        // Given
        configureCommand("", null);

        // When
        command.execute();
    }

    /**
     * 正常系: 使用方法の説明文字列が返却されることを検証します。
     */
    @Test
    public void testUsage正常系説明文字列返却() {
        // Given
        final String expected = "Adds a site to Google Search Console.";

        // When
        final String actual = command.usage();

        // Then
        assertEquals("コマンドの説明文が正しいこと", expected, actual);
    }

    /**
     * コマンドを設定します。
     *
     * @param url サイトURL
     * @param format 出力フォーマット
     */
    private void configureCommand(final String url, final Format format) {
        ReflectionTestUtils.setField(command, "siteUrl", url);
        if (format != null) {
            ReflectionTestUtils.setField(command, "format", format);
        }
    }

    /**
     * コマンドをリセットします。
     */
    private void resetCommand() {
        command = new AddCommand();
        ReflectionTestUtils.setField(command, "factory", factory);
    }

    /**
     * 基本的なAPI呼び出しを検証します。
     *
     * @throws IOException 検証中に例外が発生した場合
     */
    private void verifyBasicApiCalls() throws IOException {
        verify(factory).createClient();
        verify(webmasters).sites();
        verify(webmasters).sitemaps();
    }

    /**
     * すべてのAPI呼び出しを検証します。
     *
     * @throws IOException 検証中に例外が発生した場合
     */
    private void verifyApiCalls() throws IOException {
        verifyBasicApiCalls();
        verify(sitemaps).submit(eq(SITE_URL), eq(SITE_URL + "/sitemap.xml"));
        verify(submit).execute();
    }
}
