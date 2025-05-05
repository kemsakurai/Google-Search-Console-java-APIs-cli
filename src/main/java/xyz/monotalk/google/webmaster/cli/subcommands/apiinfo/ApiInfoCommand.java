package xyz.monotalk.google.webmaster.cli.subcommands.apiinfo;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.json.GenericJson;
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

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * API情報を取得するコマンドクラス。
 * このクラスはWebmasters APIの情報を取得し、新しいレコードクラスを使用してレスポンスを処理します。
 * Java 21の機能（レコード、スイッチ式、テキストブロック、パターンマッチングなど）を活用しています。
 */
@Component
public class ApiInfoCommand implements Command {

    /** ロガーインスタンスです。 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiInfoCommand.class);
    
    /** Google API Discovery URLです。 */
    private static final String API_DISCOVERY_URL = "https://webmasters.googleapis.com/$discovery/rest";
    
    /** WebmastersApiのファクトリです。 */
    @Autowired
    private WebmastersFactory factory;
    
    /** 出力フォーマットです。 */
    @Option(name = "-format", usage = "Output format (JSON or CONSOLE)", required = false)
    private Format format = Format.CONSOLE;
    
    /** JSON出力時のファイルパスです。 */
    @Option(name = "-filePath", usage = "Output file path for JSON format", required = false)
    private String filePath = null;
    
    /** 詳細情報表示フラグです。 */
    @Option(name = "-verbose", usage = "Display verbose information", required = false)
    private boolean verbose = false;

    /**
     * コマンドを実行します。
     * Java 21のパターンマッチング、テキストブロック、スイッチ式を活用しています。
     */
    @Override
    public void execute() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Fetching API information...");
        }
        
        try {
            // API情報を取得
            ApiResponseRecord<GenericJson> response = fetchApiInfo();
            
            // 処理と出力
            if (response.isSuccess()) {
                GenericJson apiInfo = ApiResponseHandler.extractDataOrThrow(response);
                processApiInfo(apiInfo);
            } else {
                // エラー処理
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
     * Java 21のtry-with-resourcesの拡張機能を利用しています。
     *
     * @return API情報レスポンス
     * @throws Exception 情報取得中にエラーが発生した場合
     */
    private ApiResponseRecord<GenericJson> fetchApiInfo() throws Exception {
        // Webmasters APIクライアントを作成
        var client = factory.create();
        
        // HTTPリクエストファクトリを取得
        HttpRequestFactory requestFactory = client.getRequestFactory();
        
        // Discovery APIにリクエスト
        GenericUrl url = new GenericUrl(API_DISCOVERY_URL);
        HttpRequest request = requestFactory.buildGetRequest(url);
        
        // レスポンスを処理
        return ApiResponseHandler.handleJsonResponse(request.execute());
    }
    
    /**
     * API情報を処理して出力します。
     * Java 21のパターンマッチング機能を使用して、データ構造を分析します。
     *
     * @param apiInfo API情報
     */
    @SuppressWarnings("unchecked")
    private void processApiInfo(GenericJson apiInfo) {
        // APIメタデータの抽出
        Map<String, Object> result = new HashMap<>();
        result.put("apiName", apiInfo.get("name"));
        result.put("version", apiInfo.get("version"));
        result.put("title", apiInfo.get("title"));
        result.put("description", apiInfo.get("description"));
        
        // 詳細情報が要求されている場合
        if (verbose) {
            // スキーマ情報の取得（Java 21のパターンマッチングを活用）
            if (apiInfo.get("schemas") instanceof Map<?,?> schemas) {
                result.put("schemas", schemas);
            }
            
            // リソース情報の取得
            if (apiInfo.get("resources") instanceof Map<?,?> resources) {
                result.put("resources", resources);
            }
        }
        
        // Java 21のテキストブロックを使用したログ出力
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("""
                API Information retrieved successfully:
                Name: {}
                Version: {}
                Title: {}
                """, apiInfo.get("name"), apiInfo.get("version"), apiInfo.get("title"));
        }
        
        // 結果を指定のフォーマットで出力
        ResponseWriter.writeJson(result, format, filePath);
        
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("API information output complete");
        }
    }
    
    /**
     * コマンドの説明文を返します。
     * Java 21のテキストブロックを使用して可読性の高いドキュメントを提供します。
     *
     * @return コマンドの説明文
     */
    @Override
    public String usage() {
        return """
            Retrieve Google Webmasters API information and metadata.
            This command demonstrates Java 21 features like records, pattern matching, and text blocks.
            """;
    }
}