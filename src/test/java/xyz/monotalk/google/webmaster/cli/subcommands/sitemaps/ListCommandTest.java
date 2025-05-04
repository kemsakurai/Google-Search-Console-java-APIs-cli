package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitemapsListResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * サイトマップ一覧取得コマンドのテストクラス
 */
@RunWith(MockitoJUnitRunner.class)
public class ListCommandTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private WebmastersFactory factory;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Sitemaps sitemaps;

    @Mock
    private Webmasters.Sitemaps.List list;

    @Mock
    private SitemapsListResponse response;

    @InjectMocks
    private ListCommand command;

    @Before
    public void setup() throws IOException, CommandLineInputOutputException, CmdLineIOException, CmdLineArgmentException {
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.list(anyString())).thenReturn(list);
        when(list.execute()).thenReturn(response);
    }

    /**
     * コンソール出力モードでの正常系テスト
     * サイトマップ一覧が正常に取得されコンソールに出力されることを検証
     */
    @Test
    public void testExecute_WithConsoleFormat_ShouldSucceed() throws Exception {
        // Given
        command.siteUrl = new URL("https://example.com").toString();
        command.format = Format.CONSOLE;

        // ResponseWriterの静的メソッドをモック化
        try (MockedStatic<ResponseWriter> mockedStatic = Mockito.mockStatic(ResponseWriter.class)) {
            // When
            command.execute();

            // Then
            verify(factory).create();
            verify(webmasters).sitemaps();
            verify(sitemaps).list("https://example.com");
            verify(list).execute();
            
            // 静的メソッドの呼び出しを検証
            mockedStatic.verify(() -> ResponseWriter.writeJson(eq(response), eq(Format.CONSOLE), anyString()));
        }
    }

    /**
     * JSONファイル出力モードでの正常系テスト
     * サイトマップ一覧が正常に取得されJSONファイルに出力されることを検証
     */
    @Test
    public void testExecute_WithJsonFormat_ShouldOutputToFile() throws Exception {
        // Given
        File tempFile = temporaryFolder.newFile("output.json");
        command.siteUrl = new URL("https://example.com").toString();
        command.format = Format.JSON;
        command.filePath = tempFile.getAbsolutePath();

        // ResponseWriterの静的メソッドをモック化
        try (MockedStatic<ResponseWriter> mockedStatic = Mockito.mockStatic(ResponseWriter.class)) {
            // When
            command.execute();

            // Then
            verify(factory).create();
            verify(webmasters).sitemaps();
            verify(sitemaps).list("https://example.com");
            verify(list).execute();
            
            // 静的メソッドの呼び出しを検証
            mockedStatic.verify(() -> ResponseWriter.writeJson(eq(response), eq(Format.JSON), eq(tempFile.getAbsolutePath())));
        }
    }

    /**
     * API呼び出しで例外が発生した場合のテスト
     * IOExceptionがCmdLineIOExceptionに変換されることを検証
     */
    @Test(expected = CmdLineIOException.class)
    public void testExecute_WhenApiCallFails_ShouldThrowCmdLineIOException() throws Exception {
        // Given
        command.siteUrl = new URL("https://example.com").toString();
        command.format = Format.CONSOLE;
        when(list.execute()).thenThrow(new IOException("API Error"));

        // When
        command.execute();
    }

    /**
     * JSONフォーマット指定時にファイルパスが未指定の場合のテスト
     * CmdLineArgmentExceptionが発生することを検証
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecute_WithJsonFormatWithoutFilePath_ShouldThrowException() throws Exception {
        // Given
        command.siteUrl = new URL("https://example.com").toString();
        command.format = Format.JSON;
        command.filePath = null;

        // When
        command.execute();
    }

    /**
     * usageメソッドのテスト
     * 適切な説明文字列が返されることを検証
     */
    @Test
    public void testUsage_ShouldReturnCorrectDescription() {
        // When
        String usage = command.usage();

        // Then
        assertEquals("Lists the sitemaps for a given site URL.", usage);
    }
}