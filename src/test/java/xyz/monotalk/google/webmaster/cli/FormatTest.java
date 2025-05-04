package xyz.monotalk.google.webmaster.cli;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Formatクラスのテスト
 */
public class FormatTest {

    /**
     * Format列挙型のvaluesメソッドが正しい値を返すことをテスト
     */
    @Test
    public void testFormatEnum_shouldReturnAllValues() {
        // When
        Format[] formats = Format.values();

        // Then
        assertEquals(3, formats.length);
        assertEquals(Format.JSON, formats[0]);
        assertEquals(Format.CSV, formats[1]);
        assertEquals(Format.CONSOLE, formats[2]);
    }

    /**
     * valueOf操作が正しく動作することをテスト
     */
    @Test
    public void testFormatEnum_shouldResolveValueByName() {
        // When & Then
        assertEquals(Format.JSON, Format.valueOf("JSON"));
        assertEquals(Format.CONSOLE, Format.valueOf("CONSOLE"));
    }

    /**
     * 不正な値でvalueOf操作を行った場合に例外が発生することをテスト
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatEnum_shouldThrowExceptionForInvalidValue() {
        // When
        Format.valueOf("INVALID");
    }

    /**
     * 列挙型の各値が正しい名前と順序を持つことをテスト
     */
    @Test
    public void testFormatEnum_shouldHaveCorrectNamesAndOrdinals() {
        // Then
        assertEquals("JSON", Format.JSON.name());
        assertEquals("CONSOLE", Format.CONSOLE.name());
        assertEquals(0, Format.JSON.ordinal());
        assertEquals(2, Format.CONSOLE.ordinal());
    }
}