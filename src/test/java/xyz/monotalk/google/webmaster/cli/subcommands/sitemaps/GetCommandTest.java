package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import java.io.IOException;
import java.net.URL;

import com.google.api.services.webmasters.Webmasters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

/**
 * GetCommandクラスのテストクラス。
 */
@RunWith(MockitoJUnitRunner.class)
public class GetCommandTest {

    @Mock
    private WebmastersFactory factory;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Sitemaps sitemaps;

    @Mock
    private Webmasters.Sitemaps.Get getRequest;

    private GetCommand command;

    @Before
    public void setUp() throws Exception {
        command = new GetCommand();
        command.factory = factory;
        command.siteUrl = new URL("https://example.com");
        command.feedpath = "sitemap.xml";

        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.get(anyString(), anyString())).thenReturn(getRequest);
    }

    /**
     * サイトURLが未指定の場合の異常系テスト。
     * @throws Exception 例外が発生した場合
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecute異常系サイトURL未指定() throws Exception {
        command.siteUrl = null;
        command.execute();
    }

    /**
     * フィードパスが未指定の場合の異常系テスト。
     * @throws Exception 例外が発生した場合
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecute異常系フィードパス未指定() throws Exception {
        command.feedpath = null;
        command.execute();
    }

    /**
     * API呼び出しでIOExceptionが発生する異常系テスト。
     * @throws Exception 例外が発生した場合
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute異常系API呼び出しIOException() throws Exception {
        when(getRequest.execute()).thenThrow(new IOException("API Error"));
        command.execute();
    }
}