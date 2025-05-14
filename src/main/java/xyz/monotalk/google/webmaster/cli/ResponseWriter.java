package xyz.monotalk.google.webmaster.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.GenericJson;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * レスポンス出力処理を提供するユーティリティクラスです。
 */
public final class ResponseWriter {
    /**
     * デフォルトコンストラクタ。
     * テスト用にアクセス修飾子を変更。
     */
    private ResponseWriter() {
        throw new AssertionError("インスタンス化は禁止されています。");
    }

    /**
     * レスポンスを出力します。
     *
     * @param response 出力するレスポンスオブジェクト。
     * @param format 出力フォーマット。
     * @param path 出力先のファイルパス。
     * @throws CommandLineInputOutputException 入出力エラーが発生した場合。
     * @throws CmdLineArgmentException フォーマットまたはパスが無効な場合。
     */
    public static void writeJson(final Object response, final Format format, final String path) {
        // フォーマットの検証
        validateFormat(format);
        
        // JSONフォーマットでファイルパスのバリデーション
        if (format == Format.JSON) {
            validateJsonPath(path);
        }
        final String jsonString = getJsonString(response);
        routeOutput(jsonString, format, path);
    }

    /**
     * レスポンスオブジェクトをJSON文字列に変換します。
     *
     * @param response レスポンスオブジェクト
     * @return JSON文字列
     * @throws CommandLineInputOutputException 変換中にエラーが発生した場合
     */
    public static String getJsonString(final Object response) {
        if (response == null) {
            return "{}";
        }
        if (response instanceof GenericJson) {
            return convertGenericJsonToString((GenericJson) response);
        }
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new CommandLineInputOutputException("JSONの変換に失敗しました: " + e.getMessage(), e);
        }
    }

    /**
     * GenericJsonオブジェクトをJSON文字列に変換します。
     *
     * @param json 変換するGenericJsonオブジェクト
     * @return JSON文字列
     * @throws CommandLineInputOutputException 変換中にエラーが発生した場合
     */
    private static String convertGenericJsonToString(final GenericJson json) {
        if (json == null || json.isEmpty()) {
            return "{}";
        }
        // toString()メソッドを呼び出し、テストケースでの例外をスローさせるために利用
        return json.toString();
    }

    /**
     * フォーマットの検証を行います。
     *
     * @param format 検証する出力フォーマット。
     * @throws CmdLineArgmentException フォーマットが無効な場合。
     */
    private static void validateFormat(final Format format) {
        if (format == null) {
            throw new CmdLineArgmentException("Format must be specified");
        }
    }

    /**
     * 出力処理を振り分けます。
     *
     * @param jsonString JSON文字列
     * @param format 出力フォーマット
     * @param path 出力先のファイルパス
     * @throws CommandLineInputOutputException 入出力エラーが発生した場合
     */
    @SuppressWarnings("PMD.SystemPrintln")
    private static void routeOutput(final String jsonString, final Format format, final String path) {
        if (format == Format.CONSOLE) {
            System.out.println(jsonString);
        } else if (format == Format.JSON) {
            validateJsonPath(path);
            try {
                FileUtils.writeStringToFile(new File(path), jsonString, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new CommandLineInputOutputException("Failed to write JSON to file: " + path, e);
            }
        } else {
            throw new CmdLineArgmentException("Unsupported format: " + format);
        }
    }

    /**
     * JSONフォーマット出力時のパスを検証します。
     *
     * @param path 検証する出力先ファイルパス。
     * @throws CmdLineArgmentException パスが無効な場合。
     */
    private static void validateJsonPath(final String path) {
        if (StringUtils.isBlank(path)) {
            throw new CmdLineArgmentException("For JSON format, filepath is mandatory.");
        }
    }
}
