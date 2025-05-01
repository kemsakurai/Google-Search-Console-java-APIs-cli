package xyz.monotalk.google.webmaster.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CmdLineIOExceptionTest {

    @Test
    public void testConstructor_正常系_原因例外が正しく設定される() {
        // Given
        Throwable cause = new RuntimeException("原因となる例外");

        // When
        CmdLineIOException exception = new CmdLineIOException(cause);

        // Then
        assertEquals(cause, exception.getCause());
    }
}