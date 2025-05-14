package xyz.monotalk.google.webmaster.cli;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * CommandLineInputOutputExceptionのテストクラス。
 */
public class CommandLineInputOutputExceptionTest {

    // コンストラクタにコメントを追加
    /**
     * デフォルトコンストラクタ。
     */
    public CommandLineInputOutputExceptionTest() {
        // 初期化処理
    }

    /**
     * コンストラクタのテスト。
     * 原因例外が正しく設定されることを確認します。
     */
    @Test
    public void testConstructor_正常系_原因例外が正しく設定される() {
        // Given
        final Throwable cause = new RuntimeException("原因となる例外");
        final String message = "IOエラーが発生しました";

        // When
        final CommandLineInputOutputException exception = new CommandLineInputOutputException(message, cause);

        // Then
        assertEquals("メッセージが正しく設定されていません", message, exception.getMessage());
        assertEquals("原因例外が正しく設定されていません", cause, exception.getCause());
    }
}