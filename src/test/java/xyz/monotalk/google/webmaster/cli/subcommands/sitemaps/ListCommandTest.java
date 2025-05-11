package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitemapsListResponse;
import java.io.File;
import java.io.IOException;
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
import org.springframework.test.util.ReflectionTestUtils;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * サイトマップ一覧取得コマンドのテストクラス。
 */
@RunWith(MockitoJUnitRunner.class)
public class ListCommandTest {

    /**
     * テスト用のサイトURL定数。
     */
    private static final String TEST_SITE_URL = "https://example.com";

    /**
     * テスト用の一時フォルダ。
     */
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     * WebmastersFactoryのモック。
     */
    @Mock
    private WebmastersFactory factory;

    /**
     * ResponseWriterのモック。
     */
    @Mock
    private ResponseWriter responseWriter;

    /**
     * Webmastersクライアントのモック。
     */
    @Mock
    private Webmasters webmasters;

    /**
     * Webmasters.Sitemapsのモック。
     */
    @Mock
    private Webmasters.Sitemaps sitemaps;

    /**
     * Webmasters.Sitemaps.Listのモック。
     */
    @Mock
    private Webmasters.Sitemaps.List list;

    /**
     * サイトマップリストレスポンスのモック。
     */
    @Mock
    private SitemapsListResponse response;

    /**
     * テスト対象のコマンドインスタンス。
     */
    @InjectMocks
    private ListCommand command;

    /**
     * デフォルトコンストラクタ。
     */
    public ListCommandTest() {
        // 初期化処理
    }

    /**
     * テスト前の準備。
     *
     * @throws IOException 入出力例外が発生した場合
     */
    @Before
    public void setup() throws IOException {
        initializeMocks();
    }

    /**
     * モックの初期化を行います。
     *
     * @throws IOException モックの初期化中に例外が発生した場合
     */
    private void initializeMocks() throws IOException {
        when(factory.createClient()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.list(anyString())).thenReturn(list);
        when(list.execute()).thenReturn(response);
    }

    /**
     * コンソール出力モードでの正常系テスト。
     * サイトマップ一覧が正常に取得されコンソールに出力されることを検証。
     *
     * @throws IOException 入出力例外が発生した場合
     */
    @Test
    public void testExecute正常系コンソール出力成功() throws IOException {
        // Given
        configureCommand(Format.CONSOLE, null);

        // ResponseWriterの静的メソッドをモック化
        try (MockedStatic<ResponseWriter> mockedStatic = Mockito.mockStatic(ResponseWriter.class)) {
            // When
            command.execute();

            // Then
            verifyApiCalls();
            verifyResponseWriterCalls(mockedStatic, Format.CONSOLE, null);
        }
    }

    /**
     * JSONファイル出力モードでの正常系テスト。
     * サイトマップ一覧が正常に取得されJSONファイルに出力されることを検証。
     *
     * @throws IOException 入出力例外が発生した場合
     */
    @Test
    public void testExecute正常系Jsonファイル出力成功() throws IOException {
        // Given
        final File tempFile = temporaryFolder.newFile("output.json");
        configureCommand(Format.JSON, tempFile.getAbsolutePath());

        // ResponseWriterの静的メソッドをモック化
        try (MockedStatic<ResponseWriter> mockedStatic = Mockito.mockStatic(ResponseWriter.class)) {
            // When
            command.execute();

            // Then
            verifyApiCalls();
            verifyResponseWriterCalls(mockedStatic, Format.JSON, tempFile.getAbsolutePath());
        }
    }

    /**
     * API呼び出しで例外が発生した場合のテスト。
     * IOExceptionがCmdLineIOExceptionに変換されることを検証。
     *
     * @throws IOException 入出力例外が発生した場合
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute異常系Api呼び出し例外スロー() throws IOException {
        // Given
        configureCommand(Format.CONSOLE, null);
        when(list.execute()).thenThrow(new IOException("API Error"));

        // When
        command.execute();
    }

    /**
     * JSONフォーマット指定時にファイルパスが未指定の場合のテスト。
     * CmdLineArgmentExceptionが発生することを検証。
     *
     * @throws IOException 入出力例外が発生した場合
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecute異常系Jsonファイルパス未指定() throws IOException {
        // Given
        configureCommand(Format.JSON, null);

        // When
        command.execute();
    }

    /**
     * コマンドを設定します。
     *
     * @param format 出力フォーマット
     * @param filePath 出力ファイルパス（オプション）
     */
    private void configureCommand(final Format format, final String filePath) {
        ReflectionTestUtils.setField(command, "siteUrl", TEST_SITE_URL);
        ReflectionTestUtils.setField(command, "format", format);
        ReflectionTestUtils.setField(command, "filePath", filePath);
    }

    /**
     * API呼び出しを検証します。
     *
     * @throws IOException 検証中に例外が発生した場合
     */
    private void verifyApiCalls() throws IOException {
        verify(factory).createClient();
        verify(webmasters).sitemaps();
        verify(sitemaps).list(TEST_SITE_URL);
        verify(list).execute();
    }

    /**
     * ResponseWriterの呼び出しを検証します。
     *
     * @param mockedStatic モック化されたResponseWriter
     * @param format 出力フォーマット
     * @param filePath 出力ファイルパス（オプション）
     */
    private void verifyResponseWriterCalls(final MockedStatic<ResponseWriter> mockedStatic,
            final Format format, final String filePath) {
        mockedStatic.verify(() -> ResponseWriter.writeJson(
            eq(response),
            eq(format),
            eq(filePath != null ? filePath : "")
        ));
    }

    /**
     * usageメソッドのテスト。
     * 適切な説明文字列が返されることを検証。
     */
    @Test
    public void testUsage正常系説明文字列返却() {
        // Given
        final String expected = "Lists the sitemaps for a given site URL.";

        // When
        final String actual = command.usage();

        // Then
        assertEquals("コマンドの説明文が正しいこと", expected, actual);
    }
}