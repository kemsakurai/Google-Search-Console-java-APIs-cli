package xyz.monotalk.google.webmaster.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * CmdLineArgmentExceptionTestクラスは、CmdLineArgmentExceptionのテストを行います。
 */
public class CmdLineArgmentExceptionTest {

    /**
     * デフォルトコンストラクタ。
     */
    public CmdLineArgmentExceptionTest() {
        // 初期化処理が必要な場合はここに記述
    }

    /**
     * コンストラクタのテスト。
     * メッセージが正しく設定されることを確認します。
     */
    @Test
    public void testConstructor_正常系_メッセージが正しく設定される() {
        // Given
        final String message = "テストエラーメッセージ";

        // When
        final CmdLineArgmentException exception = new CmdLineArgmentException(message);

        // Then
        assertEquals("メッセージが正しく設定されていません", message, exception.getMessage());
    }

    /**
     * サンプルテストメソッド。
     */
    @Test
    public void testSample() {
        assertNotNull("サンプルテストが成功しました", new CmdLineArgmentException("テスト"));
    }
}
