package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import java.net.URI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * GetCommandTestクラスは、GetCommandのテストを行います。
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

    /**
     * テストのセットアップを行います。
     */
    @Before
    public void setUp() throws Exception {
        command = new GetCommand();
        command.factory = factory;
        command.siteUrl = new URI("https://example.com").toURL();
        command.feedpath = "sitemap.xml";

        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.get(anyString(), anyString())).thenReturn(getRequest);
    }

    // メソッド名を適切な形式に変更し、空行を追加
    /**
     * 正常系のテストを実行します。
     */
    @Test
    public void testExecuteValidCase() {
        // 実行
        command.execute();
        // 検証
        try {
            Mockito.verify(getRequest).execute();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 異常系のテストを実行します。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteInvalidCaseSiteUrlNotSpecified() throws Exception {
        command.siteUrl = null;
        command.execute();
    }

    /**
     * フィードパスが未指定の場合の異常系テスト。
     *
     * @throws Exception 例外が発生した場合。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteInvalidCaseFeedPathNotSpecified() throws Exception {
        command.feedpath = null;
        command.execute();
    }

    // メソッド名を適切な形式に変更
    /**
     * API呼び出しでIOExceptionが発生する異常系テスト。
     *
     * @throws Exception 例外が発生した場合。
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecuteWithApiIoException() throws Exception {
        when(getRequest.execute()).thenThrow(new IOException("API Error"));
        command.execute();
    }
}