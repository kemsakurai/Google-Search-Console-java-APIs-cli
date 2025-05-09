package xyz.monotalk.google.webmaster.cli;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * CmdLineArgmentExceptionクラスのテストクラス。
 */
public class CmdLineArgmentExceptionTest {

    /**
     * コンストラクタのテスト。
     * メッセージが正しく設定されることを確認します。
     */
    @Test
    public void testConstructor_正常系_メッセージが正しく設定される() {
        // Given
        String message = "テストエラーメッセージ";

        // When
        CmdLineArgmentException exception = new CmdLineArgmentException(message);

        // Then
        assertEquals(message, exception.getMessage());
    }
}
