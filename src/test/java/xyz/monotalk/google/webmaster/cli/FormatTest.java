package xyz.monotalk.google.webmaster.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class FormatTest {

    @Test
    public void testFormatEnum_正常系_values取得() {
        // When
        Format[] formats = Format.values();

        // Then
        assertEquals(2, formats.length);
        assertEquals(Format.CONSOLE, formats[0]);
        assertEquals(Format.JSON, formats[1]);
    }

    @Test
    public void testFormatEnum_正常系_valueOf実行() {
        // When & Then
        assertEquals(Format.CONSOLE, Format.valueOf("CONSOLE"));
        assertEquals(Format.JSON, Format.valueOf("JSON"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatEnum_異常系_不正な値でvalueOf実行() {
        // When
        Format.valueOf("INVALID");
    }

    @Test
    public void testFormatEnum_正常系_enumの値が正しい() {
        // Then
        assertEquals("CONSOLE", Format.CONSOLE.name());
        assertEquals("JSON", Format.JSON.name());
        assertEquals(0, Format.CONSOLE.ordinal());
        assertEquals(1, Format.JSON.ordinal());
    }
}