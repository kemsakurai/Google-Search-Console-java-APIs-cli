package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import com.google.api.services.webmasters.Webmasters;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * サイトマップ送信コマンドのテストクラス。
 */
@RunWith(MockitoJUnitRunner.class)
public class SubmitCommandTest {

    /**
     * WebmastersFactoryのモック。
     */
    @Mock
    private WebmastersFactory factory;

    /**
     * Webmastersのモック。
     */
    @Mock
    private Webmasters webmasters;

    /**
     * Sitemapsのモック。
     */
    @Mock
    private Webmasters.Sitemaps sitemaps;

    /**
     * Submit requestのモック。
     */
    @Mock
    private Webmasters.Sitemaps.Submit submitRequest;

    /**
     * テスト対象のコマンド。
     */
    @InjectMocks
    private SubmitCommand command;

    /**
     * テスト前の準備。
     */
    @Before
    public void setUp() throws MalformedURLException, IOException {
        command = new SubmitCommand();
        command.siteUrl = URI.create("http://example.com").toURL();
        command.feedpath = "sitemap.xml";
        ReflectionTestUtils.setField(command, "factory", factory);

        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.submit(anyString(), anyString())).thenReturn(submitRequest);
    }

    /**
     * 正常系: サイトマップの送信が成功するケース。
     */
    @Test
    public void testExecute正常系送信成功() throws Exception {
        doNothing().when(submitRequest).execute();
        command.execute();
        verify(submitRequest).execute();
    }

    /**
     * 異常系: サイトURLがnullのケース。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecute異常系サイトUrlNull() throws Exception {
        command.siteUrl = null;
        command.execute();
    }

    /**
     * 異常系: feedpathがnullのケース。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecute異常系FeedpathNull() throws Exception {
        command.feedpath = null;
        command.execute();
    }

    /**
     * 異常系: API呼び出しでIOExceptionが発生するケース。
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute異常系Api呼び出しIOException() throws Exception {
        when(submitRequest.execute()).thenThrow(new IOException("API Error"));
        command.execute();
    }

    /**
     * 正常系: usageメソッドが正しい説明文を返すこと。
     */
    @Test
    public void testUsage正常系説明文返却() {
        assertEquals("説明文が一致すること", "Submits a sitemap for this site.", command.usage());
    }
}