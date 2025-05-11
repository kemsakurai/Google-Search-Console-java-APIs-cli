package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * サイト一覧取得コマンドのテストクラス。
 */
@RunWith(MockitoJUnitRunner.class)
public class ListCommandTest {

    /**
     * WebmastersFactoryのモックオブジェクト。
     */
    @Mock
    private WebmastersFactory factory; // WebmastersFactoryのモックオブジェクト

    /**
     * Webmastersのモックオブジェクト。
     */
    @Mock
    private Webmasters webmasters; // Webmastersのモックオブジェクト

    /**
     * Webmasters.Sitesのモックオブジェクト。
     */
    @Mock
    private Webmasters.Sites sites; // Webmasters.Sitesのモックオブジェクト

    /**
     * Webmasters.Sites.Listのモックオブジェクト。
     */
    @Mock
    private Webmasters.Sites.List request; // Webmasters.Sites.Listのモックオブジェクト

    /**
     * SitesListResponseのモックオブジェクト。
     */
    @Mock
    private SitesListResponse response;

    /**
     * ListCommandのテスト対象オブジェクト。
     */
    @InjectMocks
    private ListCommand command; // テスト対象のListCommandオブジェクト

    /**
     * 重複リテラルを定数化し、Law of Demeter違反を解消します。
     */
    private static final String EXAMPLE_URL = "https://example.com";

    /**
     * 重複リテラルを定数化し、コードの可読性を向上させます。
     */
    private static final String TEST_SITE_URL = "https://example.com"; // テスト用のサイトURL

    /**
     * テスト前のセットアップ処理。
     *
     * @throws IOException 入出力例外が発生した場合
     * @throws GeneralSecurityException セキュリティ例外が発生した場合
     */
    @Before
    public void setUp() throws IOException, GeneralSecurityException {
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sites()).thenReturn(sites);
        when(sites.list()).thenReturn(request);
    }

    /**
     * テスト用のローカル変数をfinalに変更し、PMD警告を解消します。
     */
    @Test
    public void testExecute_ValidResponse_Success() throws IOException {
        // Arrange
        final SitesListResponse response = new SitesListResponse();
        when(request.execute()).thenReturn(response);

        // Act
        final ListCommand command = new ListCommand();
        command.setFactory(factory);
        command.execute();

        // Assert
        verify(request).execute();
    }

    /**
     * IOExceptionがスローされる場合のテスト。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute_IOException_ThrowsException() throws IOException {
        // Arrange
        when(request.execute()).thenThrow(new IOException("Test IOException"));

        // Act
        final ListCommand command = new ListCommand();
        command.setFactory(factory);
        command.execute();
    }

    /**
     * Law of Demeter 違反を解消するため、間接的なアクセスを削除します。
     */
    @Test
    public void testExecute_NullResponse_Success() throws IOException {
        // Arrange
        when(request.execute()).thenReturn(null);

        // Act
        final ListCommand command = new ListCommand();
        command.setFactory(factory);
        command.execute();

        // Assert
        verify(request).execute();
    }

    /**
     * usageメソッドの説明文字列が正しく返されることを検証します。
     */
    @Test
    public void testUsage正常系説明文字列返却() {
        // 実行
        final String usage = command.usage();
        // 検証
        assertEquals("usage説明が正しくありません", "Lists the user's Search Console sites.", usage);
    }

    /**
     * ListCommandの実行テスト。
     *
     * @throws GeneralSecurityException セキュリティ例外が発生した場合。
     * @throws IOException 入出力例外が発生した場合。
     */
    @Test
    public void testListCommandExecution() throws GeneralSecurityException, IOException {
        // Given
        final ListCommand command = new ListCommand();
        command.setFactory(factory);

        final Webmasters webmasters = Mockito.mock(Webmasters.class);
        when(factory.create()).thenReturn(webmasters);

        // When
        command.execute();

        // Then
        verify(factory).create();
    }
}