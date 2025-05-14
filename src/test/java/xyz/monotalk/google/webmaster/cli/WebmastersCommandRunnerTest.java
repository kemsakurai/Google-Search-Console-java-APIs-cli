package xyz.monotalk.google.webmaster.cli;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
@RunWith(MockitoJUnitRunner.Silent.class)
public class WebmastersCommandRunnerTest {

    /** サイトリストコマンド。 */
    private static final String CMD_SITES_LIST = "webmasters.sites.list";

    /** テスト用のコマンドライン引数。 */
    private static final String[] TEST_ARGS = {CMD_SITES_LIST};

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

    /**
     * 各テストの前に実行される初期化メソッド。
     */
    @Before
    public void setUp() {
        when(context.getAutowireCapableBeanFactory()).thenReturn(beanFactory);
    }

    /**
     * 正常にコマンドが実行されることをテストします。
     *
     * @throws Exception テスト実行中に例外が発生した場合
     */
    @Test
    public void testRun_正常系_コマンドが実行される() throws Exception {
        // Given
        final ListCommand listCommand = new ListCommand(webmastersFactory) {
            // 実行だけのテストなので、内部で例外が発生しないようにオーバーライド
            @Override
            public void execute() {
                // テストのため、何もしない実装に置き換え
            }
        };
        when(beanFactory.createBean(any())).thenReturn(listCommand);

        // When
        runner.run(TEST_ARGS);

        // Then
        verify(beanFactory).createBean(any());
    }

    /**
     * 引数がnullの場合に例外がスローされることをテストします。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testRun_異常系_引数がnull() {
        runner.run((String[]) null);
    }

    /**
     * 空の引数配列の場合に例外がスローされることをテストします。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testRun_異常系_引数が空配列() {
        runner.run(new String[0]);
    }

    /**
     * 無効なコマンド形式の場合に例外がスローされることをテストします。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testRun_異常系_コマンド形式が無効() {
        // Given
        final String[] invalidArgs = {"invalid.command"};

        // When
        runner.run(invalidArgs);
    }
}