package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.json.GenericJson;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * レスポンス出力処理を提供するユーティリティクラス
 */
public final class ResponseWriter {

    /**
     * ロギング用のロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseWriter.class);

    private ResponseWriter() {
        // インスタンス化を防止するためのプライベートコンストラクタ
    }

    /**
     * レスポンスを出力します。
     * フォーマットに応じて適切な出力先に書き込みを行います。
     * @param response 出力するレスポンスオブジェクト
     * @param format 出力フォーマット
     * @param path 出力先のファイルパス
     * @throws CmdLineIOException 入出力エラーが発生した場合
     */
    public static void writeJson(final Object response, final Format format, final String path) {
        validateFormat(format);
        
        final String jsonString;
        try {
            jsonString = convertToJsonString(response);
        } catch (IOException e) {
            throw new CmdLineIOException("Failed to convert response to JSON", e);
        }
        routeOutput(jsonString, format, path);
    }

    /**
     * フォーマットの検証を行います。
     * 
     * @param format 検証する出力フォーマット
     * @throws CmdLineArgmentException フォーマットが無効な場合
     */
    private static void validateFormat(final Format format) {
        if (format == null) {
            throw new CmdLineArgmentException("Format must be specified");
        }
    }

    /**
     * レスポンスオブジェクトをJSON文字列に変換します。
     * 
     * @param response 変換対象のレスポンスオブジェクト
     * @return JSON形式の文字列
     * @throws IOException JSON変換中にエラーが発生した場合
     */
    private static String convertToJsonString(final Object response) throws IOException {
        final String result;
        
        if (response == null) {
            result = "{}";
        } else if (response instanceof GenericJson) {
            result = ((GenericJson) response).toPrettyString();
        } else {
            result = response.toString();
        }
        return result;
    }

    /**
     * 指定されたパスにコンテンツを書き込みます。
     * 親ディレクトリが存在しない場合は作成します。
     * 
     * @param path 書き込み先のファイルパス
     * @param content 書き込むコンテンツ
     * @throws CmdLineIOException ファイル操作中にエラーが発生した場合
     */
    private static void writeToFile(final String path, final String content) {
        final File file = new File(path);
        final File parentDir = file.getParentFile();

        try {
            if (parentDir != null && !parentDir.exists()) {
                FileUtils.forceMkdir(parentDir);
            }
            FileUtils.write(file, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CmdLineIOException(e);
        }
    }

    /**
     * 指定されたフォーマットに従ってJSON文字列を出力します。
     * 
     * @param jsonString 出力するJSON文字列
     * @param format 出力フォーマット
     * @param path JSONフォーマットの場合の出力先ファイルパス
     * @throws CmdLineIOException ファイル操作中にエラーが発生した場合
     */
    @SuppressWarnings("incomplete-switch")
    private static void routeOutput(final String jsonString, final Format format, final String path) {
        switch (format) {
            case JSON:
                validateJsonPath(path);
                writeToFile(path, jsonString);
                break;
            case CONSOLE:
                LOGGER.info(jsonString);
                break;
        }
    }

    /**
     * JSONフォーマット出力時のパスを検証します。
     * 
     * @param path 検証する出力先ファイルパス
     * @throws CmdLineArgmentException パスが無効な場合
     */
    private static void validateJsonPath(final String path) {
        if (StringUtils.isBlank(path)) {
            throw new CmdLineArgmentException("For JSON format, filepath is mandatory.");
        }
    }
}
