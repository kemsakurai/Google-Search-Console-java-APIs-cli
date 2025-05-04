package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * サイト一覧取得コマンドのテストクラス
 */
@RunWith(MockitoJUnitRunner.class)
public class ListCommandTest {

    @Mock
    private WebmastersFactory factory;

    @Mock
    private ResponseWriter responseWriter;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Sites sites;

    @Mock
    private Webmasters.Sites.List request;

    @Mock
    private SitesListResponse response;

    @InjectMocks
    private ListCommand command;

    @Before
    public void setUp() throws IOException {
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sites()).thenReturn(sites);
        when(sites.list()).thenReturn(request);
        when(request.execute()).thenReturn(response);
        
        // ResponseWriterのモックセットアップ
        doNothing().when(responseWriter).writeJson(any(), any(Format.class), anyString());
    }

    /**
     * 正常系のテスト
     * サイト一覧が正常に取得されることを検証
     */
    @Test
    public void testExecute_WithValidParameters_ShouldReturnSitesList() throws IOException {
        // 実行
        command.execute();

        // 検証
        verify(factory).create();
        verify(webmasters).sites();
        verify(sites).list();
        verify(request).execute();
        verify(responseWriter).writeJson(eq(response), any(Format.class), anyString());
    }

    /**
     * API呼び出しでエラーが発生した場合のテスト
     * IOExceptionがCmdLineIOExceptionとしてスローされることを確認
     */
    @Test(expected = CmdLineIOException.class)
    public void testExecute_WhenApiCallFails_ShouldThrowCmdLineIOException() throws IOException {
        // 準備
        when(request.execute()).thenThrow(new IOException("API Error"));
        
        // 実行
        command.execute();
    }

    /**
     * usage()メソッドのテスト
     * 説明文が正しく返されることを検証
     */
    @Test
    public void testUsage_ShouldReturnCorrectDescription() {
        // 実行
        String usage = command.usage();
        
        // 検証
        assertEquals("Lists the user's Search Console sites.", usage);
    }
}