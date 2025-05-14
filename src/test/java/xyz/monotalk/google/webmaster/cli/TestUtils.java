package xyz.monotalk.google.webmaster.cli;


/**
 * テスト用ユーティリティクラス。
 * このクラスは、テストケースで使用される共通の機能を提供します。
 */
@SuppressWarnings("PMD.TestClassWithoutTestCases")
public final class TestUtils {
    /** プライベートコンストラクタ。 */
    private TestUtils() {
        throw new AssertionError("インスタンス化は禁止されています。");
    }

    /**
     * ResponseWriterの出力を処理します。
     * このメソッドは、レスポンスをJSON形式の文字列に変換するために使用します。
     *
     * @param response JSONに変換するオブジェクト
     * @return JSON形式の文字列
     * @throws RuntimeException 変換に失敗した場合
     */
    public static String convertToJson(final Object response) {
        return ResponseWriter.getJsonString(response);
    }

    /**
     * ResponseWriterのconvertToJsonメソッドを呼び出すためのメソッド。
     * このメソッドは、下位互換性のために提供されています。
     *
     * @param response JSONに変換するオブジェクト
     * @return JSON形式の文字列
     * @deprecated 代わりに{@link #convertToJson(Object)}を使用してください。
     */
    @Deprecated
    public static String invokeConvertToJsonString(final Object response) {
        return convertToJson(response);
    }
}