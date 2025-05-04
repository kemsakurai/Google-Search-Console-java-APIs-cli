package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitemapsListResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.kohsuke.args4j.CmdLineException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * ListCommandのテストクラス
 */
@RunWith(MockitoJUnitRunner.class)
public class ListCommandTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private WebmastersFactory factory;

    @Mock
    private ResponseWriter responseWriter;

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

    /**
     * テスト前の準備
     */
    @Before
    public void setup() throws IOException {
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.list(anyString())).thenReturn(list);
        when(list.execute()).thenReturn(response);
    }

    /**
     * コンソール出力モードでの正常な実行をテスト
     */
    @Test
    public void testExecute_shouldOutputToConsoleSuccessfully() throws Exception {
        // Given
        command.siteUrl = new URL("https://example.com");
        command.format = Format.CONSOLE;

        // When
        command.execute();

        // Then
        verify(factory).create();
        verify(webmasters).sitemaps();
        verify(sitemaps).list("https://example.com");
        verify(list).execute();
    }

    /**
     * JSONファイル出力モードでの正常な実行をテスト
     */
    @Test
    public void testExecute_shouldOutputToJsonFileSuccessfully() throws Exception {
        // Given
        File tempFile = temporaryFolder.newFile("output.json");
        command.siteUrl = new URL("https://example.com");
        command.format = Format.JSON;
        command.filePath = tempFile.getAbsolutePath();

        // When
        command.execute();

        // Then
        verify(factory).create();
        verify(webmasters).sitemaps();
        verify(sitemaps).list("https://example.com");
        verify(list).execute();
    }

    /**
     * API呼び出し時の例外処理をテスト
     */
    @Test(expected = CmdLineIOException.class)
    public void testExecute_shouldThrowExceptionWhenApiCallFails() throws Exception {
        // Given
        command.siteUrl = new URL("https://example.com");
        when(list.execute()).thenThrow(new IOException("API Error"));

        // When
        command.execute();
    }

    /**
     * JSONフォーマット指定時にファイルパスが未指定の場合の例外処理をテスト
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecute_shouldThrowExceptionWhenFilePathNotSpecified() throws Exception {
        // Given
        command.siteUrl = new URL("https://example.com");
        command.format = Format.JSON;
        command.filePath = null;

        // When
        command.execute();
    }

    /**
     * usage()メソッドが適切な説明文を返すかテスト
     */
    @Test
    public void testUsage_shouldReturnCorrectDescription() {
        // When
        String usage = command.usage();

        // Then
        assertEquals("サイトに登録されているサイトマップのリストを取得します。サイトマップインデックスが指定されている場合は、そのインデックスファイルに含まれるサイトマップも含まれます。", usage);
    }
}