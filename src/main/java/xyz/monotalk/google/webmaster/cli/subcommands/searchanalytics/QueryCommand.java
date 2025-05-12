package xyz.monotalk.google.webmaster.cli.subcommands.searchanalytics;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.ApiDimensionFilter;
import com.google.api.services.webmasters.model.ApiDimensionFilterGroup;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryRequest;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * Search Analyticsのクエリを実行するコマンドクラス。
 */
@Component
public class QueryCommand implements Command {

    /** ロガーインスタンス。 */
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryCommand.class);

    /** サイトURL。 */
    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    private String siteUrl;

    /** 開始日。 */
    @Option(name = "-startDate", usage = "Start date (yyyy-MM-dd)", required = true)
    private String startDate;

    /** 終了日。 */
    @Option(name = "-endDate", usage = "End date (yyyy-MM-dd)", required = true)
    private String endDate;

    /** 出力フォーマット。 */
    @Option(name = "-format", usage = "Output format")
    private Format format = Format.CONSOLE;

    /** 出力ファイルパス。 */
    @Option(name = "-filePath", usage = "Output file path")
    private String filePath;

    /** WebmastersFactoryインスタンス。 */
    private final WebmastersFactory factory;

    /**
     * コンストラクタ。
     *
     * @param factory WebmastersFactoryインスタンス
     */
    public QueryCommand(final WebmastersFactory factory) {
        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        validateArguments();

        try {
            final Webmasters webmasters = factory.createClient();
            final SearchAnalyticsQueryRequest request = createRequest();
            final SearchAnalyticsQueryResponse response = executeRequest(webmasters, request);
            ResponseWriter.writeJson(response, format, filePath);
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to execute search analytics query", e);
            }
            throw new CommandLineInputOutputException("Failed to execute search analytics query", e);
        }
    }

    /**
     * 引数を検証します。
     *
     * @throws CmdLineArgmentException 引数が無効な場合
     */
    private void validateArguments() {
        if (StringUtils.isBlank(siteUrl)) {
            throw new CmdLineArgmentException("Site URL must be specified");
        }
        if (StringUtils.isBlank(startDate)) {
            throw new CmdLineArgmentException("Start date must be specified");
        }
        if (StringUtils.isBlank(endDate)) {
            throw new CmdLineArgmentException("End date must be specified");
        }
        if (format == Format.JSON && StringUtils.isBlank(filePath)) {
            throw new CmdLineArgmentException("File path must be specified when using JSON format");
        }
    }

    /**
     * Search Analyticsクエリリクエストを作成します。
     *
     * @return 作成されたリクエスト
     */
    private SearchAnalyticsQueryRequest createRequest() {
        return new SearchAnalyticsQueryRequest()
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setDimensions(Collections.singletonList("query"))
                .setDimensionFilterGroups(Collections.singletonList(
                        new ApiDimensionFilterGroup()
                                .setGroupType("and")
                                .setFilters(new ArrayList<ApiDimensionFilter>())));
    }

    /**
     * Search Analyticsクエリを実行します。
     *
     * @param webmasters Webmastersクライアント
     * @param request クエリリクエスト
     * @return クエリレスポンス
     * @throws IOException APIコール中にエラーが発生した場合
     */
    private SearchAnalyticsQueryResponse executeRequest(
            final Webmasters webmasters,
            final SearchAnalyticsQueryRequest request) throws IOException {
        return webmasters.searchanalytics().query(siteUrl, request).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String usage() {
        return "Executes a Search Analytics query for your site.";
    }
}
