package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import java.net.URI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

    @Mock
    private WebmastersFactory factory;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Sitemaps sitemaps;

    @Mock
    private Webmasters.Sitemaps.Submit submit;

    @Mock
    private Webmasters.Sitemaps.Submit submitRequest;

    @InjectMocks
    private SubmitCommand command;

    private static final String SITE_URL = "https://example.com";

    /**
     * テストのセットアップを行います。
     */
    @Before
    public void setUp() throws Exception {
        command = new SubmitCommand();
        command.setFactory(factory);
        command.siteUrl = new URI(SITE_URL).toURL();
        command.feedpath = "sitemap.xml";

        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.submit(eq(SITE_URL), eq(SITE_URL + "/sitemap.xml"))).thenReturn(submit);
        doNothing().when(submit).execute();
    }

    /**
     * 正常系: コマンドが正しく実行されることを検証します。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Test
    public void testExecute正常系() throws IOException {
        // Given
        command.setSiteUrl(URI.create(SITE_URL).toURL());

        // When
        command.execute();

        // Then
        verify(factory).create();
        verify(webmasters).sitemaps();
        verify(sitemaps).submit(eq(SITE_URL), eq(SITE_URL + "/sitemap.xml"));
        verify(submit).execute();
    }

    /**
     * 異常系: コマンド実行時に例外が発生する場合を検証します。
     *
     * @throws Exception 例外が発生した場合。
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute異常系() throws Exception {
        // Given
        when(factory.create()).thenThrow(new CommandLineInputOutputException("Mocked CommandLineInputOutputException"));

        // When
        command.execute();

        // Then
        // CommandLineInputOutputExceptionがスローされることを確認
    }

    /**
     * 異常系: サイトURLがnullのケース。
     *
     * @throws Exception 例外が発生した場合。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteErrorSiteUrlNull() throws Exception {
        command.siteUrl = null;
        command.execute();
    }

    /**
     * 異常系: feedpathがnullのケース。
     *
     * @throws Exception 例外が発生した場合。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteErrorFeedpathNull() throws Exception {
        command.feedpath = null;
        command.execute();
    }

    /**
     * 正常系: usageメソッドが正しい説明文を返すこと。
     */
    @Test
    public void testUsageReturnsCorrectDescription() {
        assertEquals("Submits a sitemap for this site.", command.usage());
    }
}