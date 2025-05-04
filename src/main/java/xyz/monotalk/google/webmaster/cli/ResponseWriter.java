package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.json.GenericJson;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * ResponseWriter
 */
public final class ResponseWriter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseWriter.class);

    private ResponseWriter() {
        // インスタンス化を防止するためのプライベートコンストラクタ
    }

    /**
     * レスポンスを出力します。
     * formatがJSONの場合は、指定されたファイルに書き込みます。
     * formatがCONSOLEの場合は、コンソールに出力します。
     * formatがnullの場合は、CmdLineArgmentExceptionをスローします。
     * formatがJSONで、pathがnullまたは空文字の場合は、CmdLineArgmentExceptionをスローします。
     * @param response 出力するレスポンスオブジェクト
     * @param format 出力フォーマット
     * @param path 出力先のファイルパス
     * @throws CmdLineIOException 入出力エラーが発生した場合
     */
    public void writeJson(final Object response, final Format format, final String path) throws CmdLineIOException {
        if (format == null) {
            throw new CmdLineArgmentException("Format must be specified");
        }
        final String jsonString;
        try {
            if (response == null) {
                jsonString = "{}";
            } else if (response instanceof GenericJson) {
                jsonString = ((GenericJson) response).toPrettyString();
            } else {
                jsonString = response.toString();
            }
        } catch (IOException e) {
            throw new CmdLineIOException("Failed to convert response to JSON", e);
        }
        if (format == Format.JSON) {
            if (path == null || path.trim().isEmpty()) {
                throw new CmdLineArgmentException("For JSON format, filepath is mandatory.");
            }
            writeToFile(path, jsonString);
        } else if (format == Format.CONSOLE) {
            logger.info(jsonString);
        } else {
            throw new CmdLineArgmentException("Unsupported format: " + format);
        }
    }

    private static void writeToFile(final String path, final String content) throws CmdLineIOException {
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
}
