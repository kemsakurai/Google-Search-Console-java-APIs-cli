package xyz.monotalk.google.webmaster.cli;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import xyz.monotalk.google.webmaster.cli.subcommands.sites.ListCommand;

/**
 * WebmastersCommandRunnerTestクラス。
 * WebmastersCommandRunnerのテストを実行します。
 */
@RunWith(MockitoJUnitRunner.class)
public class WebmastersCommandRunnerTest {

    /** サイトリストコマンド。 */
    private static final String CMD_SITES_LIST = "webmasters.sites.list";

    /** テスト用のコマンドライン引数。 */
    private static final String[] TEST_ARGS = {"arg1", "arg2"};

    /** テスト対象のWebmastersCommandRunnerインスタンス。 */
    @InjectMocks
    private WebmastersCommandRunner runner;

    /** Spring ApplicationContext。 */
    @Mock
    private ApplicationContext context;

    /** モックされたAutowireCapableBeanFactory。 */
    @Mock
    private AutowireCapableBeanFactory beanFactory;

    /** モックされたWebmastersFactory。 */
    @Mock
    private WebmastersFactory webmastersFactory;

    /** モックされたWebmastersクライアント。 */
    @Mock
    private Webmasters webmasters;

    /** モックされたWebmasters.Sites。 */
    @Mock
    private Webmasters.Sites sites;

    /** モックされたWebmasters.Sites.List。 */
    @Mock
    private Webmasters.Sites.List sitesList;

    /** モックされたSitesListResponse。 */
    @Mock
    private SitesListResponse response;

    /**
     * テスト前のセットアップ処理。
     */
    @Before
    public void setUp() {
        try {
            setupMockBehavior();
        } catch (final IOException ex) {
            fail("セットアップ中にIO例外が発生しました: " + ex.getMessage());
        }
    }

    /**
     * モックの振る舞いを設定します。
     *
     * @throws IOException モック設定中に発生する可能性のある例外
     */
    private void setupMockBehavior() throws IOException {
        when(context.getAutowireCapableBeanFactory()).thenReturn(beanFactory);
        setupBeanFactoryBehavior();
        setupWebmastersClientBehavior();
    }

    /**
     * BeanFactoryの振る舞いを設定します。
     */
    private void setupBeanFactoryBehavior() {
        doAnswer(invocation -> {
            final Object bean = invocation.getArgument(0);
            if (bean instanceof ListCommand) {
                final ListCommand listCommand = (ListCommand) bean;
                listCommand.setFactory(webmastersFactory);
            }
            return null;
        }).when(beanFactory).autowireBean(any());
    }

    /**
     * WebmastersClientの振る舞いを設定します。
     *
     * @throws IOException モック設定中に発生する可能性のある例外
     */
    private void setupWebmastersClientBehavior() throws IOException {
        when(webmastersFactory.createClient()).thenReturn(webmasters);
        when(webmasters.sites()).thenReturn(sites);
        when(sites.list()).thenReturn(sitesList);
    }

    /**
     * サイトリストを取得するテスト。
     *
     * @throws Exception テスト実行中に発生する可能性のある例外
     */
    @Test
    public void testRunWithCommandSuccessful_SitesList() throws Exception {
        // Given
        final String[] args = {CMD_SITES_LIST};
        setupSitesListResponse();
        assertNotNull("応答オブジェクトがnullです", response);

        // When
        runner.run(args);

        // Then
        verifySitesListApiCalls();
    }

    /**
     * サイトリストレスポンスのモック設定を行います。
     *
     * @throws IOException モック設定中に発生する可能性のある例外
     */
    private void setupSitesListResponse() throws IOException {
        when(webmasters.sites()).thenReturn(sites);
        when(sites.list()).thenReturn(sitesList);
        when(sitesList.execute()).thenReturn(response);
    }

    /**
     * サイトリストAPIの呼び出しを検証します。
     *
     * @throws IOException 検証中に発生する可能性のある例外
     */

    /**
     * サイトリストAPIの呼び出しを検証します。
     *
     * @throws IOException 検証中に発生する可能性のある例外
     */
    private void verifySitesListApiCalls() throws IOException {
        verify(webmasters).sites();
        verify(sites).list();
        verify(sitesList).execute();
    }

    /**
     * nullコマンドのテスト。
     *
     * @throws Exception テスト実行中に発生する可能性のある例外
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testNullCommand() throws Exception {
        runner.run((String[]) null);
    }

    /**
     * モックコマンドの実行テスト。
     *
     * @throws Exception テスト実行中に発生する可能性のある例外
     */
    @Test
    public void testMockCommandExecution() throws Exception {
        // Given
        final Command mockCommand = mock(Command.class);
        when(context.getBean(CMD_SITES_LIST)).thenReturn(mockCommand);

        // When
        runner.run(CMD_SITES_LIST);

        // Then
        verify(mockCommand).execute();
    }

    /**
     * 未初期化コマンドの実行テスト。
     *
     * @throws Exception テスト実行中に発生する可能性のある例外
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testUninitializedCommand() throws Exception {
        // Given
        runner.setCommand(null);

        // When
        runner.run(TEST_ARGS);

        // Then: expect CmdLineArgmentException
    }
}