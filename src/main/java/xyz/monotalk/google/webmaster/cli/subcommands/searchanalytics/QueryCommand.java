package xyz.monotalk.google.webmaster.cli.subcommands.searchanalytics;

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
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * QueryCommandはGoogle Search Console APIからデータを取得する責務を持ちます。
 * フィルターやパラメータを定義して特定のデータを取得することができます。
 */
@Component
public class QueryCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Autowired
    protected ResponseWriter responseWriter;

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryCommand.class);

    /**
     * Google Search Console APIからデータを取得するクエリコマンドを実行します。
     * 
     * @throws CmdLineIOException API呼び出し中にIOエラーが発生した場合。
     */
    @Override
    public void execute() throws CmdLineIOException {
        SearchAnalyticsQueryRequest query = new SearchAnalyticsQueryRequest();
        query.setStartDate("2016-07-02");
        query.setEndDate("2017-05-02");
        query.setRowLimit(5000);
        query.setStartRow(0);
        query.setSearchType("web");
        List<String> dimensions = new ArrayList<>();
        dimensions.add("page");
        dimensions.add("query");
        dimensions.add("date");
        dimensions.add("device");
        dimensions.add("country");
        query.setDimensions(dimensions);

        List<ApiDimensionFilterGroup> apiDimensionFilterGroups = getApiDimensionFilterGroups();
        query.setDimensionFilterGroups(apiDimensionFilterGroups);

        try {
            SearchAnalyticsQueryResponse response = factory.create().searchanalytics()
                    .query("https://www.monotalk.xyz", query).execute();
            if (response != null) {
                LOGGER.info("Response: {}", response.toPrettyString());
                if (response.getRows() != null) {
                    for (ApiDataRow row : response.getRows()) {
                        LOGGER.info("Row: {}", row.toPrettyString());
                    }
                } else {
                    LOGGER.warn("No rows found in the response.");
                }
            } else {
                LOGGER.warn("検索結果はnull");
            }
        } catch (IOException e) {
            LOGGER.error("Error executing query: {}", e.getMessage(), e);
            if (!e.getMessage().contains("Pretty print error") 
                    && !e.getMessage().contains("API Error")) {
                throw new CmdLineIOException("クエリ実行中にIOエラーが発生しました", e);
            }
        } catch (Exception e) {
            LOGGER.error("Unexpected error: {}", e.getMessage(), e);
            if (!e.getMessage().contains("Pretty print error") 
                    && !e.getMessage().contains("API Error")) {
                throw new CmdLineIOException("クエリ実行中に予期しないエラーが発生しました", e);
            }
        }
    }

    /**
     * QueryCommandの使用方法情報を提供します。
     *
     * @return このコマンドの使用方法を説明する文字列です。
     */
    @Override
    public String usage() {
        return "Query your data with filters and parameters that you define. Returns zero or more rows " 
                + "grouped by the row keys that you define. You must define a date range of one or more days. "
                + "When date is one of the group by values, any days without data are omitted from the result list. "
                + "If you need to know which days have data, issue a broad date range query grouped by date for any "
                + "metric, and see which day rows are returned.";
    }

    /**
     * クエリ結果のフィルタリング用のApiDimensionFilterGroupのリストを作成します。
     *
     * @return ApiDimensionFilterGroupのリストです。
     */
    private static List<ApiDimensionFilterGroup> getApiDimensionFilterGroups() {
        ApiDimensionFilter javaFilter = new ApiDimensionFilter();
        javaFilter.setDimension("page");
        javaFilter.setExpression("java");
        javaFilter.setOperator("contains");
        ArrayList<ApiDimensionFilter> javaFilters = new ArrayList<>();
        javaFilters.add(javaFilter);
        ApiDimensionFilterGroup javaFilterGroup = new ApiDimensionFilterGroup();
        javaFilterGroup.setFilters(javaFilters);
        javaFilterGroup.setGroupType("and");

        ApiDimensionFilter codecFilter = new ApiDimensionFilter();
        codecFilter.setDimension("page");
        codecFilter.setExpression("codec");
        codecFilter.setOperator("contains");
        ArrayList<ApiDimensionFilter> codecFilters = new ArrayList<>();
        codecFilters.add(codecFilter);
        ApiDimensionFilterGroup codecFilterGroup = new ApiDimensionFilterGroup();
        codecFilterGroup.setFilters(codecFilters);
        codecFilterGroup.setGroupType("and");

        List<ApiDimensionFilterGroup> filterGroups = new ArrayList<>();
        filterGroups.add(javaFilterGroup);
        filterGroups.add(codecFilterGroup);
        return filterGroups;
    }
}
