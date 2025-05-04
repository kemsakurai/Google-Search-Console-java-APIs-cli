package xyz.monotalk.google.webmaster.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CmdLineArgmentExceptionTest {

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
