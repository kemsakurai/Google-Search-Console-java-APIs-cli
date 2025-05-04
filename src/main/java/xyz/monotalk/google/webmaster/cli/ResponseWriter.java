package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.json.GenericJson;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * レスポンス出力処理を行うコンポーネントです。
 */
@Component
public class ResponseWriter {

    private Logger logger = LoggerFactory.getLogger(ResponseWriter.class);

    /**
     * ログ出力用のロガーを設定します。
     *
     * @param logger 設定するロガー。
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * JSONデータを出力します。
     *
     * @param response レスポンスデータ。
     * @param format 出力フォーマット。
     * @param filePath ファイル出力時のパス。
     * @throws CmdLineArgmentException 引数が不正な場合。
     * @throws CommandLineInputOutputException 出力処理でエラーが発生した場合。
     */
    public void writeJson(GenericJson response, Format format, String filePath)
            throws CmdLineArgmentException, CommandLineInputOutputException {
        String json;
        try {
            json = response.toPrettyString();
        } catch (IOException e) {
            throw new CommandLineInputOutputException(
                    "Failed to convert to JSON: " + e.getMessage(), e);
        }
        writeJson(json, format, filePath);
    }

    /**
     * JSON文字列を出力します。
     *
     * @param response レスポンス文字列。
     * @param format 出力フォーマット。
     * @param filePath ファイル出力時のパス。
     * @throws CmdLineArgmentException 引数が不正な場合。
     * @throws CommandLineInputOutputException 出力処理でエラーが発生した場合。
     */
    public void writeJson(String response, Format format, String filePath)
            throws CmdLineArgmentException, CommandLineInputOutputException {
        if (format == null) {
            throw new CmdLineArgmentException("Format must not be null");
        }

        switch (format) {
          case CONSOLE:
              logger.info(response);
              break;
          case JSON:
              if (filePath == null) {
                  throw new CmdLineArgmentException(
                          "File path must be specified when format is JSON");
              }
              writeToFile(response, filePath);
              break;
          default:
              throw new CmdLineArgmentException("Unsupported format: " + format);
        }
    }

    /**
     * データをファイルに書き込みます。
     *
     * @param response 書き込むデータ。
     * @param filePath 出力先のファイルパス。
     * @throws CommandLineInputOutputException ファイル操作でエラーが発生した場合。
     */
    private void writeToFile(String response, String filePath)
            throws CommandLineInputOutputException {
        File file = new File(filePath);
        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(response);
            writer.flush();
        } catch (IOException e) {
            throw new CommandLineInputOutputException("Failed to write to file: " + e.getMessage(),
                    e);
        }
    }
}
