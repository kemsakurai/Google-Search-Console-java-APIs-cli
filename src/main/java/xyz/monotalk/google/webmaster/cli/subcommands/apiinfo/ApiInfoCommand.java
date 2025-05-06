package xyz.monotalk.google.webmaster.cli.subcommands.apiinfo;

import java.io.IOException;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.model.ApiResponseRecord;

/**
 * API情報を取得するコマンドクラス。
 * このクラスはWebmasters APIの情報を取得し、新しいレコードクラスを使用してレスポンスを処理します。
 */
@Component
public class ApiInfoCommand implements Command {
    /**
     * ロガーインスタンス。
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiInfoCommand.class);

    /**
     * 出力フォーマットを指定します。
     * JSONまたはCONSOLEのいずれかを選択できます。
     */
    @Option(name = "-format", usage = "Output format (JSON or CONSOLE)", required = false)
    private Format format;

    /**
     * JSON形式の出力先ファイルパスを指定します。
     * 指定しない場合は標準出力に出力されます。
     */
    @Option(name = "-filePath", usage = "Output file path for JSON format", required = false)
    private String filePath;

    /**
     * 詳細情報を表示するかどうかを指定します。
     * trueの場合、詳細なログが出力されます。
     */
    @Option(name = "-verbose", usage = "Display verbose information", required = false)
    private boolean verbose;

    /**
     * API情報を取得するためのフェッチャー。
     */
    @Autowired
    private ApiInfoFetcher apiInfoFetcher;

    /**
     * APIレスポンスを処理するためのプロセッサ。
     */
    @Autowired
    private ApiResponseProcessor responseProcessor;

    /**
     * デフォルトコンストラクタ。
     */
    public ApiInfoCommand() {
        // デフォルトの初期化処理
    }

    @Override
    public void execute() {
        try {
            final ApiResponseRecord<?> response = apiInfoFetcher.fetchApiInfo();
            responseProcessor.processResponse(response, format, filePath);
        } catch (IOException e) {
            handleIoException(e);
        } catch (IllegalArgumentException e) {
            handleIllegalArgumentException(e);
        } catch (IllegalStateException e) {
            handleIllegalStateException(e);
        }
    }

    private void handleIoException(final IOException ioException) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Error fetching API info: {}", ioException.getMessage());
        }
        throw new CmdLineArgmentException("API info command failed due to an I/O error: " + ioException.getMessage(), ioException);
    }

    private void handleIllegalArgumentException(final IllegalArgumentException exception) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Illegal argument error: {}", exception.getMessage());
        }
        throw new CmdLineArgmentException("Illegal argument encountered: " + exception.getMessage(), exception);
    }

    private void handleIllegalStateException(final IllegalStateException exception) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Illegal state error: {}", exception.getMessage());
        }
        throw new CmdLineArgmentException("Illegal state encountered: " + exception.getMessage(), exception);
    }

    @Override
    public String usage() {
        return """
            Retrieve Google Webmasters API information and metadata.
            This command demonstrates Java 21 features like records,
            pattern matching, and text blocks.
            """;
    }
}
