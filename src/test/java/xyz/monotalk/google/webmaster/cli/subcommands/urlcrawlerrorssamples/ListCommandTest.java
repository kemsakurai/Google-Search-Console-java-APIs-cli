package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * URLクロールエラーサンプル一覧取得コマンドのテストクラス。
 * （非推奨API対応バージョン）。
 */
@RunWith(MockitoJUnitRunner.class)
public class ListCommandTest {

    /**
     * WebmastersFactoryのモックインスタンス。
     */
    @Mock
    private WebmastersFactory factory;

    /**
     * Loggerのモックインスタンス。
     */
    @Mock
    private Logger logger;

    /**
     * テスト対象のコマンドインスタンス。
     */
    @InjectMocks
    private ListCommand command;

    /**
     * テストのセットアップを行います。
     */
    @Before
    public void setUp() {
        // テスト対象コマンドの初期設定
        command.setSiteUrl("https://www.monotalk.xyz");
        command.setCategory("notFound");
        command.setPlatform("web");
    }

    /**
     * 非推奨API対応コマンドのテスト。
     * 警告メッセージが正しく表示されることを検証します。
     */
    @Test
    public void testExecute_WithDeprecatedApi_ShouldShowWarningMessage() {
        // ResponseWriter.writeJsonをモック化
        try (MockedStatic<ResponseWriter> mockedStatic = mockStatic(ResponseWriter.class)) {
            // When
            command.execute();

            // Then
            // 静的メソッドの呼び出しを検証（警告メッセージが表示されること）
            mockedStatic.verify(() -> ResponseWriter.writeJson(argThat(message -> 
                message.toString().contains("URLクロールエラーサンプル API は現在利用できません")), 
                eq(Format.CONSOLE), 
                eq(null)
            ));
        }
    }

    /**
     * ResponseWriterでエラーが発生した場合のテスト。
     * CommandLineInputOutputExceptionがスローされることを確認します。
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute_WhenResponseWriterThrowsException_ShouldThrowCommandLineInputOutputException() {
        // ResponseWriter.writeJsonをモック化してエラーをスロー
        try (MockedStatic<ResponseWriter> mockedStatic = mockStatic(ResponseWriter.class)) {
            mockedStatic.when(() -> ResponseWriter.writeJson(anyString(), eq(Format.CONSOLE), eq(null)))
                .thenThrow(new CommandLineInputOutputException("テスト例外"));

            // When
            command.execute();
        }
    }

    /**
     * usage()メソッドのテスト。
     * 説明文が正しく返されることを検証します。
     */
    @Test
    public void testUsage_ShouldReturnValidDescription() {
        // When
        final String usage = command.usage();

        // Then
        assertNotNull("説明文がnullであってはならない", usage);
        assertFalse("説明文は空であってはならない", usage.isEmpty());
        assertTrue("説明文に「非推奨」が含まれること", usage.contains("非推奨"));
    }
}