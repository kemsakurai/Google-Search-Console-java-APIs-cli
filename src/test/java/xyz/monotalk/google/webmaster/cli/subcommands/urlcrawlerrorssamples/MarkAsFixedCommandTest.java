package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.io.IOException;

/**
 * URLクロールエラーサンプルを修正済みとしてマークするコマンドのテストクラス
 * （非推奨API対応バージョン）
 */
@RunWith(MockitoJUnitRunner.class)
public class MarkAsFixedCommandTest {

    @Mock
    private WebmastersFactory factory;
    
    @Mock
    private Logger logger;

    @InjectMocks
    private MarkAsFixedCommand command;

    @Before
    public void setUp() {
        // URLを設定
        command.setUrl("https://example.com/404");
    }

    /**
     * URLがnullの場合のテスト
     * CmdLineArgmentExceptionがスローされることを確認
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecute_URLNull_ShouldThrowCmdLineArgmentException() {
        // Given
        command.setUrl(null);
        
        // When
        command.execute();
    }

    /**
     * 非推奨API対応コマンドのテスト
     * 警告メッセージが正しく表示されることを検証
     */
    @Test
    public void testExecute_WithValidUrl_ShouldShowDeprecationMessage() throws CommandLineInputOutputException {
        // ResponseWriter.writeJsonをモック化
        try (MockedStatic<ResponseWriter> mockedStatic = Mockito.mockStatic(ResponseWriter.class)) {
            // When
            command.execute();

            // Then
            // 静的メソッドの呼び出しを検証（警告メッセージが表示されること）
            mockedStatic.verify(() -> 
                ResponseWriter.writeJson(argThat(message -> 
                    message.toString().contains("URLクロールエラーサンプル 修正済みマークAPI は現在利用できません")), 
                    eq(Format.CONSOLE), 
                    eq(null))
            );
        }
    }

    /**
     * ResponseWriterでエラーが発生した場合のテスト
     * CommandLineInputOutputExceptionがスローされることを確認
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute_WhenResponseWriterThrowsException_ShouldThrowCommandLineInputOutputException() throws CommandLineInputOutputException {
        // ResponseWriter.writeJsonをモック化してエラーをスロー
        try (MockedStatic<ResponseWriter> mockedStatic = Mockito.mockStatic(ResponseWriter.class)) {
            mockedStatic.when(() -> ResponseWriter.writeJson(anyString(), eq(Format.CONSOLE), eq(null)))
                .thenThrow(new CommandLineInputOutputException("テスト例外"));

            // When
            command.execute();
        }
    }

    /**
     * usage()メソッドのテスト
     * 説明文に「非推奨」が含まれることを検証
     */
    @Test
    public void testUsage_ShouldIncludeDeprecatedNotice() {
        // When
        String usage = command.usage();

        // Then
        assertTrue("説明文に「非推奨」が含まれること", usage.contains("非推奨"));
    }
}