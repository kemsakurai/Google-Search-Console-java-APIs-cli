package xyz.monotalk.google.webmaster.cli.subcommands.apiinfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.model.ApiResponseHandler;
import xyz.monotalk.google.webmaster.cli.model.ApiResponseRecord;

/**
 * APIレスポンスを処理するクラス。
 */
@Component
public class ApiResponseProcessor {

    /**
     * ロガーインスタンス。
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseProcessor.class);

    /**
     * デフォルトコンストラクタ。
     */
    public ApiResponseProcessor() {
        // デフォルトの初期化処理
    }

    /**
     * APIレスポンスを処理します。
     *
     * @param response APIレスポンス
     * @param format 出力フォーマット
     * @param filePath 出力ファイルパス
     */
    public void processResponse(final ApiResponseRecord<?> response, final Format format, final String filePath) {
        if (response.isSuccess()) {
            final Object apiInfo = ApiResponseHandler.extractDataOrThrow(response);
            processApiInfo(apiInfo, format, filePath);
        } else {
            final String errorMessage = response.getErrorMessage().orElse("Unknown error");
            throw new CmdLineArgmentException("Failed to get API info: " + errorMessage);
        }
    }

    private void processApiInfo(final Object apiInfo, final Format format, final String filePath) {
        final Map<String, Object> result = new ConcurrentHashMap<>();
        result.put("apiInfo", apiInfo);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("API情報を処理中...");
        }

        ResponseWriter.writeJson(result, format, filePath);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("API情報の出力が完了しました。");
        }
    }
}