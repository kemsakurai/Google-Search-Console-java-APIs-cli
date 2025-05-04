package xyz.monotalk.google.webmaster.cli.subcommands.searchanalytics;

import static java.lang.System.out;

import com.google.api.services.webmasters.model.ApiDataRow;
import com.google.api.services.webmasters.model.ApiDimensionFilter;
import com.google.api.services.webmasters.model.ApiDimensionFilterGroup;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryRequest;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * QueryCommandクラス - Search Analyticsデータのクエリを実行するコマンド。
 */
@Component
public class QueryCommand implements Command {

    /**
     * WebmastersファクトリーインスタンスDI用。
     */
    @Autowired private WebmastersFactory factory;
    
    /**
     * ロガーインスタンス。
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryCommand.class);

    /**
     * デフォルトコンストラクタ
     */
    public QueryCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * Google Search Console APIからデータを取得するクエリコマンドを実行します。
     * 
     * @throws CmdLineIOException API呼び出し中にIOエラーが発生した場合。
     */
    @Override
    public void execute() {
        // クエリリクエストの作成
        final SearchAnalyticsQueryRequest query = createSearchAnalyticsQueryRequest();
        
        // APIリクエストの実行
        final SearchAnalyticsQueryResponse response = executeApiRequest(query);
        
        // レスポンス全体の出力
        printFullResponse(response);
        
        // 個別行データの出力
        printRowsData(response);
    }

    @Override
    public String usage() {
        return "Query your data with filters and parameters that you define. "
             + "Returns zero or more rows grouped by the row keys that you define. "
             + "You must define a date range of one or more days. "
             + "When date is one of the group by values, any days without data are omitted from the result list. "
             + "If you need to know which days have data, issue a broad date range query "
             + "grouped by date for any metric, and see which day rows are returned.";
    }

    /**
     * 検索分析クエリリクエストを作成します
     *
     * @return 設定済みの検索分析クエリリクエスト
     */
    private SearchAnalyticsQueryRequest createSearchAnalyticsQueryRequest() {
        final SearchAnalyticsQueryRequest query = new SearchAnalyticsQueryRequest();
        
        // 基本パラメータの設定
        query.setStartDate("2016-07-02");
        query.setEndDate("2017-05-02");
        query.setRowLimit(5000);
        query.setStartRow(0);
        query.setSearchType("web");
        
        // ディメンションの設定
        final List<String> dimensions = new ArrayList<>();
        dimensions.add("page");
        dimensions.add("query");
        dimensions.add("date");
        dimensions.add("device");
        dimensions.add("country");
        query.setDimensions(dimensions);
        
        // フィルターグループの設定
        final List<ApiDimensionFilterGroup> filterGroups = getApiDimensionFilterGroups();
        query.setDimensionFilterGroups(filterGroups);
        
        // 集計タイプの設定（コメントアウト状態）
        // query.setAggregationType("auto");
        // query.setAggregationType("byPage");
        // query.setAggregationType("byProperty");
        
        return query;
    }

    /**
     * 検索分析APIリクエストを実行します
     *
     * @param query 検索分析クエリリクエスト
     * @return 検索分析クエリレスポンス（エラー時はnull）
     */
    private SearchAnalyticsQueryResponse executeApiRequest(final SearchAnalyticsQueryRequest query) {
        try {
            return factory.create().searchanalytics().query("https://www.monotalk.xyz", query).execute();
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("API実行エラー: {}", e.getMessage(), e);
            }
            return new SearchAnalyticsQueryResponse();
        }   
    }

    /**
     * レスポンス全体を出力します
     *
     * @param response 検索分析クエリレスポンス
     */
    private void printFullResponse(final SearchAnalyticsQueryResponse response) {
        out.println("searchAnalyticsQueryResponse#.toPrettyString() START>>>");
        try {
            if (response != null) {
                out.println(response.toPrettyString());
            } else {
                LOGGER.warn("検索結果はnull");
            }
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("レスポンス出力エラー: {}", e.getMessage(), e);
            }
        }
        out.println("<<<END");
    }

    /**
     * レスポンスの各行データを出力します
     *
     * @param response 検索分析クエリレスポンス
     */
    private void printRowsData(final SearchAnalyticsQueryResponse response) {
        if (response == null || response.getRows() == null) {
            return;
        }
        
        for (final ApiDataRow row : response.getRows()) {
            try {
                out.println(row.toPrettyString());
            } catch (IOException e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("行データ出力エラー: {}", e.getMessage(), e);
                }
            }
        }
    }

    /**
     * API次元フィルターグループのリストを取得。
     *
     * @return API次元フィルターグループのリスト
     */
    private static List<ApiDimensionFilterGroup> getApiDimensionFilterGroups() {
        final ApiDimensionFilter javaFilter = new ApiDimensionFilter();
        javaFilter.setDimension("page");
        javaFilter.setExpression("java");
        javaFilter.setOperator("contains");
        final List<ApiDimensionFilter> javaFilters = new ArrayList<>();
        javaFilters.add(javaFilter);
        final ApiDimensionFilterGroup javaFilterGroup = new ApiDimensionFilterGroup();
        javaFilterGroup.setFilters(javaFilters);
        javaFilterGroup.setGroupType("and");
        final ApiDimensionFilter codecFilter = new ApiDimensionFilter();
        codecFilter.setDimension("page");
        codecFilter.setExpression("codec");
        codecFilter.setOperator("contains");
        final List<ApiDimensionFilter> pythonFilters = new ArrayList<>();
        pythonFilters.add(codecFilter);
        final ApiDimensionFilterGroup pythonFilterGroup = new ApiDimensionFilterGroup();
        pythonFilterGroup.setFilters(pythonFilters);
        pythonFilterGroup.setGroupType("and");
        final List<ApiDimensionFilterGroup> filterGroups = new ArrayList<>();
        filterGroups.add(javaFilterGroup);
        filterGroups.add(pythonFilterGroup);
        return filterGroups;
    }
}
