package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

/**
 * サイト一覧取得コマンドのテストクラス。
 */
@RunWith(MockitoJUnitRunner.class)
public class ListCommandTest {

    @Mock
    private WebmastersFactory factory;

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
    }

    /**
     * 正常系のテスト。
     * サイト一覧が正常に取得されることを検証。
     */
    @Test
    public void testExecute_WithValidParameters_ShouldReturnSitesList() throws IOException, CommandLineInputOutputException {
        // ResponseWriterの静的メソッドをモック化
        try (MockedStatic<ResponseWriter> mockedStatic = Mockito.mockStatic(ResponseWriter.class)) {
            // 実行
            command.execute();

            // 検証
            verify(factory).create();
            verify(webmasters).sites();
            verify(sites).list();
            verify(request).execute();

            // 静的メソッドの呼び出しを検証
            mockedStatic.verify(() -> ResponseWriter.writeJson(eq(response), eq(Format.CONSOLE), eq(null)));
        }
    }

    /**
     * API呼び出しでエラーが発生した場合のテスト。
     * IOExceptionがCommandLineInputOutputExceptionとしてスローされることを確認。
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute_WhenApiCallFails_ShouldThrowCmdLineIOException() throws IOException, CommandLineInputOutputException {
        // 準備
        when(request.execute()).thenThrow(new IOException("API Error"));

        // 実行
        command.execute();
    }

    /**
     * usage()メソッドのテスト。
     * 説明文が正しく返されることを検証。
     */
    @Test
    public void testUsage_ShouldReturnCorrectDescription() {
        // 実行
        String usage = command.usage();
        // 検証
        assertEquals("Lists the user's Search Console sites.", usage);
    }
}