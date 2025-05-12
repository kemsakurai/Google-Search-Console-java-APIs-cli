package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.json.GenericJson;

/**
 * テスト用ユーティリティクラス。
 * このクラスは、テストケースで使用される共通の機能を提供します。
 */
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
        if (response == null) {
            return "{}";
        }
        try {
            if (response instanceof String) {
                return (String) response;
            } else if (response instanceof GenericJson) {
                final GenericJson json = (GenericJson) response;
                if (json.isEmpty()) {
                    return "{}";
                }
                
                // GenericJsonのキーと値を取得して正しいJSON形式に整形
                StringBuilder sb = new StringBuilder("{");
                boolean first = true;
                
                for (String key : json.keySet()) {
                    // classInfoとfactoryはGenericJsonの内部フィールドなので除外
                    if ("classInfo".equals(key) || "factory".equals(key)) {
                        continue;
                    }
                    
                    if (!first) {
                        sb.append(',');
                    }
                    first = false;
                    
                    Object value = json.get(key);
                    sb.append('"').append(key).append('"').append(':');
                    
                    if (value == null) {
                        sb.append("null");
                    } else if (value instanceof String) {
                        sb.append('"').append(value).append('"');
                    } else if (value instanceof Number || value instanceof Boolean) {
                        sb.append(value);
                    } else {
                        sb.append('"').append(value).append('"');
                    }
                }
                
                sb.append('}');
                return sb.toString();
            }
            return response.toString();
        } catch (Exception e) {
            throw new RuntimeException("JSONへの変換に失敗しました", e);
        }
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