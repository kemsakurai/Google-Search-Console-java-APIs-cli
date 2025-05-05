package xyz.monotalk.google.webmaster.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CommandLineInputOutputExceptionTest {

    @Test
    public void testConstructor_正常系_原因例外が正しく設定される() {
        // Given
        Throwable cause = new RuntimeException("原因となる例外");
        String message = "IOエラーが発生しました";

        // When
        
        CommandLineInputOutputException exception = new CommandLineInputOutputException(message, cause);

        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}