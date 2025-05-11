package xyz.monotalk.google.webmaster.cli;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * テスト専用のユーティリティクラス。
 */
public final class TestUtils {

    private TestUtils() {
        // インスタンス化を防止
    }

    /**
     * ResponseWriterのprivateメソッドconvertToJsonStringを呼び出すためのユーティリティメソッド。
     *
     * @param response JSONに変換するオブジェクト
     * @return JSON形式の文字列
     */
    public static String invokeConvertToJsonString(final Object response) {
        try {
            final java.lang.reflect.Method method = ResponseWriter.class.getDeclaredMethod("convertToJsonString", Object.class);
            return (String) method.invoke(null, response);
        } catch (final NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            throw new IllegalStateException("Failed to invoke convertToJsonString", e);
        }
    }

    /**
     * TestUtilsクラスのテストケース。
     */
    @Test
    public void testInvokeConvertToJsonString() {
        // Given
        final Object testObject = new Object() {
            @Override
            public String toString() {
                return "{\"key\":\"value\"}";
            }
        };

        // When
        final String jsonString = invokeConvertToJsonString(testObject);

        // Then
        assertNotNull("JSON文字列がnullであってはなりません", jsonString);
        assertEquals("JSON文字列が期待値と一致しません", "{\"key\":\"value\"}", jsonString);
    }
}