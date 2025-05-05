package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

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

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * サイトマップ送信コマンドのテストクラス
 */
@RunWith(MockitoJUnitRunner.class)
public class SubmitCommandTest {

    /**
     * WebmastersFactoryのモック
     */
    @Mock
    private WebmastersFactory factory;

    /**
     * Webmastersのモック
     */
    @Mock
    private Webmasters webmasters;

    /**
     * Sitemapsのモック
     */
    @Mock
    private Webmasters.Sitemaps sitemaps;

    /**
     * Submit requestのモック
     */
    @Mock
    private Webmasters.Sitemaps.Submit submitRequest;

    /**
     * テスト対象のコマンド
     */
    @InjectMocks
    private SubmitCommand command;

    /**
     * テスト前の準備
     */
    @Before
    public void setUp() throws Exception {
        command = new SubmitCommand();
        command.siteUrl = new URL("http://example.com");
        command.feedpath = "sitemap.xml";
        ReflectionTestUtils.setField(command, "factory", factory);

        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.submit(anyString(), anyString())).thenReturn(submitRequest);
    }

    /**
     * 正常系: サイトマップの送信が成功するケース
     */
    @Test
    public void testExecuteSuccess() throws Exception {
        doNothing().when(submitRequest).execute();
        command.execute();
        verify(submitRequest).execute();
    }

    /**
     * 異常系: サイトURLがnullのケース
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteFailureWithNullSiteUrl() throws Exception {
        command.siteUrl = null;
        command.execute();
    }

    /**
     * 異常系: feedpathがnullのケース
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteFailureWithNullFeedpath() throws Exception {
        command.feedpath = null;
        command.execute();
    }

    /**
     * 異常系: API呼び出しでIOExceptionが発生するケース
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecuteFailureWithIOException() throws Exception {
        when(submitRequest.execute()).thenThrow(new IOException("API Error"));
        command.execute();
    }

    /**
     * 正常系: usageメソッドが正しい説明文を返すこと
     */
    @Test
    public void testUsageReturnsCorrectDescription() {
        assertEquals("説明文が一致すること", "Submits a sitemap for this site.", command.usage());
    }
}