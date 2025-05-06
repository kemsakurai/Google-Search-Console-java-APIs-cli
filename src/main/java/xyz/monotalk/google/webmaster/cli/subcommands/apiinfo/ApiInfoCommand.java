package xyz.monotalk.google.webmaster.cli.subcommands.apiinfo;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.json.GenericJson;
import java.util.HashMap;
import java.util.Map;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;
import xyz.monotalk.google.webmaster.cli.model.ApiResponseHandler;
import xyz.monotalk.google.webmaster.cli.model.ApiResponseRecord;

/**
 * API情報を取得するコマンドクラス。
 * このクラスはWebmasters APIの情報を取得し、新しいレコードクラスを使用してレスポンスを処理します。
 */
@Component
public class ApiInfoCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiInfoCommand.class);

    private static final String API_DISCOVERY_URL = "https://webmasters.googleapis.com/$discovery/rest";

    @Autowired
    private WebmastersFactory factory;

    @Option(name = "-format", usage = "Output format (JSON or CONSOLE)", required = false)
    private Format format = Format.CONSOLE;

    @Option(name = "-filePath", usage = "Output file path for JSON format", required = false)
    private String filePath = null;

    @Option(name = "-verbose", usage = "Display verbose information", required = false)
    private boolean verbose = false;

    @Override
    public void execute() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Fetching API information...");
        }

        try {
            ApiResponseRecord<GenericJson> response = fetchApiInfo();

            if (response.isSuccess()) {
                GenericJson apiInfo = ApiResponseHandler.extractDataOrThrow(response);
                processApiInfo(apiInfo);
            } else {
                String errorMessage = response.getErrorMessage().orElse("Unknown error");
                throw new CmdLineArgmentException("Failed to get API info: " + errorMessage);
            }

        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Error fetching API info: {}", e.getMessage());
            }
            throw new CmdLineArgmentException("API info command failed: " + e.getMessage(), e);
        }
    }

    /**
     * API情報を取得します。
     *
     * @return API情報を含むレスポンスレコード
     * @throws Exception API呼び出し中にエラーが発生した場合
     */
    private ApiResponseRecord<GenericJson> fetchApiInfo() throws Exception {
        var client = factory.create();
        HttpRequestFactory requestFactory = client.getRequestFactory();
        GenericUrl url = new GenericUrl(API_DISCOVERY_URL);
        HttpRequest request = requestFactory.buildGetRequest(url);
        return ApiResponseHandler.handleJsonResponse(request.execute());
    }

    /**
     * 取得したAPI情報を処理します。
     *
     * @param apiInfo API情報のJSONオブジェクト
     */
    private void processApiInfo(GenericJson apiInfo) {
        Map<String, Object> result = new HashMap<>();
        result.put("apiName", apiInfo.get("name"));
        result.put("version", apiInfo.get("version"));
        result.put("title", apiInfo.get("title"));
        result.put("description", apiInfo.get("description"));

        if (verbose) {
            if (apiInfo.get("schemas") instanceof Map<?, ?> schemas) {
                result.put("schemas", schemas);
            }
            if (apiInfo.get("resources") instanceof Map<?, ?> resources) {
                result.put("resources", resources);
            }
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(
                """
                API Information retrieved successfully:
                Name: {}
                Version: {}
                Title: {}
                """,
                apiInfo.get("name"),
                apiInfo.get("version"),
                apiInfo.get("title")
            );
        }

        ResponseWriter.writeJson(result, format, filePath);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("API information output complete");
        }
    }

    @Override
    public String usage() {
        return """
            Retrieve Google Webmasters API information and metadata.
            This command demonstrates Java 21 features like records, pattern matching, and text blocks.
            """;
    }
}
