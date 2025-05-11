package xyz.monotalk.google.webmaster.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Formatクラスのテストクラス。
 */
public class FormatTest {

    // フィールドにコメントを追加
    /**
     * エラーメッセージを格納する定数。
     */
    private static final String ERROR_MESSAGE = "Formatの値が正しくありません";

    // クラスにデフォルトコンストラクタを追加
    /**
     * デフォルトコンストラクタ。
     */
    public FormatTest() {
        // 初期化処理
    }

    /**
     * Format列挙型のvaluesメソッドが正しい値を返すことをテスト。
     */
    @Test
    public void testFormatEnum_shouldReturnAllValues() {
        // When
        final Format[] formats = Format.values();

        // Then
        assertEquals("Formatの値の数が正しくありません", 3, formats.length);
        assertEquals(ERROR_MESSAGE, Format.JSON, formats[0]);
        assertEquals(ERROR_MESSAGE, Format.CSV, formats[1]);
        assertEquals(ERROR_MESSAGE, Format.CONSOLE, formats[2]);
    }

    /**
     * valueOf操作が正しく動作することをテスト。
     */
    @Test
    public void testFormatEnum_shouldResolveValueByName() {
        // When & Then
        assertEquals(ERROR_MESSAGE, Format.JSON, Format.valueOf("JSON"));
        assertEquals(ERROR_MESSAGE, Format.CONSOLE, Format.valueOf("CONSOLE"));
    }

    /**
     * 不正な値でvalueOf操作を行った場合に例外が発生することをテスト。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatEnum_shouldThrowExceptionForInvalidValue() {
        // When
        Format.valueOf("INVALID");
    }

    /**
     * 列挙型の各値が正しい名前と順序を持つことをテスト。
     */
    @Test
    public void testFormatEnum_shouldHaveCorrectNamesAndOrdinals() {
        // Then
        assertEquals("Formatの名前が正しくありません", "JSON", Format.JSON.name());
        assertEquals("Formatの名前が正しくありません", "CONSOLE", Format.CONSOLE.name());
        assertEquals("Formatの順序が正しくありません", 0, Format.JSON.ordinal());
        assertEquals("Formatの順序が正しくありません", 2, Format.CONSOLE.ordinal());
    }

    /**
     * Formatの値が正しく取得できることをテストします。
     */
    @Test
    public void testGetValues_正常系_値が正しく取得できる() {
        // Given
        final Format[] formats = Format.values();

        // When & Then
        assertNotNull("Formatの値がnullです", formats);
        assertTrue("Formatの値が空です", formats.length > 0);
    }

    /**
     * FormatのtoStringが正しく動作することをテストします。
     */
    @Test
    public void testToString_正常系_文字列が正しく返される() {
        // Given
        final Format format = Format.JSON;

        // When
        final String result = format.toString();

        // Then
        assertEquals("FormatのtoStringが正しく動作していません", "JSON", result);
    }
}