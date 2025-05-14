package xyz.monotalk.google.webmaster.cli.subcommands.apiinfo;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.json.GenericJson;
import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;
import xyz.monotalk.google.webmaster.cli.model.ApiResponseHandler;
import xyz.monotalk.google.webmaster.cli.model.ApiResponseRecord;

/**
 * API情報を取得するクラス。
 */
@Component
public class ApiInfoFetcher {

    /** Google APIのディスカバリーURL。 */
    private static final String API_DISCOVERY_URL = "https://www.googleapis.com/discovery/v1/apis";

    /** Webmasters APIクライアント生成ファクトリ。 */
    @Autowired
    private WebmastersFactory factory;

    /**
     * デフォルトコンストラクタ。
     */
    public ApiInfoFetcher() {
        // デフォルトコンストラクタ
    }

    /**
     * API情報を取得します。
     *
     * @return API情報を含むレスポンスレコード
     * @throws IOException API呼び出し中にエラーが発生した場合
     */
    public ApiResponseRecord<GenericJson> fetchApiInfo() throws IOException {
        final Webmasters client = factory.createClient();
        final HttpRequestFactory requestFactory = createRequestFactory(client);
        final HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(API_DISCOVERY_URL));
        requestFactory.getInitializer().initialize(request);
        return ApiResponseHandler.handleJsonResponse(request.execute());
    }

    private HttpRequestFactory createRequestFactory(final Webmasters client) {
        return client.getRequestFactory();
    }
}