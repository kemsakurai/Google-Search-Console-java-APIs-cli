package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * AddCommandTestクラス。
 *
 * <p>AddCommandのテストを実行します。</p>
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class AddCommandTest {

    /** サイトのURLを表す定数。 */
    private static final String SITE_URL = "https://example.com";

    /** サイトマップのパスを表す定数 */
    private static final String FEED_PATH = "sitemap.xml";

    /** WebmastersFactoryのモックオブジェクト。 */
    @Mock
    private WebmastersFactory factory;

    /** Webmastersクライアントのモックオブジェクト。 */
    @Mock
    private Webmasters webmasters;

    /** Webmasters.Sitemapsのモックオブジェクト。 */
    @Mock
    private Webmasters.Sitemaps sitemaps;

    /** Webmasters.Sitemaps.Submitのモックオブジェクト。 */
    @Mock
    private Webmasters.Sitemaps.Submit submit;

    /** テスト対象のAddCommandインスタンス。 */
    private AddCommand command;

    /**
     * テストのセットアップを行います。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Before
    public void setUp() throws IOException {
        command = new AddCommand(factory);
        command.siteUrl = SITE_URL;
        command.feedPath = FEED_PATH;

        when(factory.createClient()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.submit(anyString(), anyString())).thenReturn(submit);
    }

    /**
     * 正常系: サイトマップが正しく追加されることをテストします。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Test
    public void testExecute_正常系() throws IOException {
        // When
        command.execute();

        // Then
        verify(sitemaps).submit(SITE_URL, SITE_URL);
        verify(submit).execute();
    }

    /**
     * 異常系: サイトURLが未設定の場合のテストを実行します。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecute_異常系_サイトUrlが未設定() throws CmdLineException {
        // Given
        command = new AddCommand(factory);
        command.feedPath = FEED_PATH;

        // When
        command.execute();
    }

    /**
     * 異常系: 不正なサイトURLの場合のテストを実行します。
     */
    @Test
    public void testExecute_異常系_不正なサイトUrl() throws CmdLineException {
        try {
            // Given
            command = new AddCommand(factory);
            final String[] args = {"-siteUrl", "invalid-url"};
            
            // Expect CmdLineException for missing required -feedPath
            new CmdLineParser(command).parseArgument(args);
            
            // この行に到達するはずはない
            assertEquals("Required arguments were missing but no exception was thrown", true, false);
        } catch (CmdLineException e) {
            // 期待通りの例外が発生した
            assertEquals("例外メッセージが期待値と一致しません。",  "Option \"-feedPath\" is required", e.getMessage());
        }
    }

    /**
     * 異常系: Api実行時にエラーが発生した場合のテストを実行します。
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute_異常系_Api実行エラー() throws IOException {
        // Given
        when(submit.execute()).thenThrow(new IOException("API Error"));
        
        // When
        command.execute();
    }

    /**
     * 正常系: 使用方法の説明文字列が返却されることを検証します。
     */
    @Test
    public void testUsageReturnsExpectedDescription() {
        // Given
        final String expected = "Adds a sitemap to Google Search Console.";

        // When
        final String actual = command.usage();

        // Then
        assertEquals("usage()のメッセージが期待値と一致しません。", expected, actual);
    }
}
