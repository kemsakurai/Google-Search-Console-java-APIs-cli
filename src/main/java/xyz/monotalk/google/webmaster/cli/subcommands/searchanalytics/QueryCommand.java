package xyz.monotalk.google.webmaster.cli.subcommands.searchanalytics;

import com.google.api.services.webmasters.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * QueryCommand
 */
public class QueryCommand implements Command {

    @Autowired private WebmastersFactory factory;

    @Override
    public void execute() {
        SearchAnalyticsQueryRequest query = new SearchAnalyticsQueryRequest();
        query.setStartDate("2016-07-02");
        query.setEndDate("2017-05-02");
        query.setRowLimit(5000);
        query.setStartRow(0);
        query.setSearchType("web");
        List<String> dimentions = new ArrayList<String>();
        dimentions.add("page");
        dimentions.add("query");
        dimentions.add("date");
        dimentions.add("device");
        dimentions.add("country");
        query.setDimensions(dimentions);
        // ----------------------------
        // AgregationType
        // -----------------
        // query.setAggregationType("auto");
        // query.setAggregationType("byPage");
        // query.setAggregationType("byProperty");

        List<ApiDimensionFilterGroup> apiDimensionFilterGroups = getApiDimensionFilterGroups();
        query.setDimensionFilterGroups(apiDimensionFilterGroups);

        SearchAnalyticsQueryResponse searchAnalyticsQueryResponse = null;
        try {
            searchAnalyticsQueryResponse = factory.create().searchanalytics().query("https://www.monotalk.xyz", query).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("searchAnalyticsQueryResponse#.toPrettyString() START>>>");
        try {
            if (searchAnalyticsQueryResponse != null) {
                out.println(searchAnalyticsQueryResponse.toPrettyString());
            } else {
                out.println("検索結果はnullです");
            }
        } catch (IOException e) {
            out.println("Pretty print error: " + e.getMessage());
            e.printStackTrace();
        }
        out.println("<<<END");

        if (searchAnalyticsQueryResponse != null && searchAnalyticsQueryResponse.getRows() != null) {
            for (ApiDataRow row : searchAnalyticsQueryResponse.getRows()) {
                try {
                    out.println(row.toPrettyString());
                } catch (IOException e) {
                    out.println("Row pretty print error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String usage() {
        return "Query your data with filters and parameters that you define. Returns zero or more rows grouped by the row keys that you define. You must define a date range of one or more days. When date is one of the group by values, any days without data are omitted from the result list. If you need to know which days have data, issue a broad date range query grouped by date for any metric, and see which day rows are returned.";
    }

    private static List<ApiDimensionFilterGroup> getApiDimensionFilterGroups() {
        ApiDimensionFilter javaFilter = new ApiDimensionFilter();
        javaFilter.setDimension("page");
        javaFilter.setExpression("java");
        javaFilter.setOperator("contains");
        ArrayList<ApiDimensionFilter> javaFilters = new ArrayList<>();
        javaFilters.add(javaFilter);
        ApiDimensionFilterGroup apiDimensionJavaFilterGroup = new ApiDimensionFilterGroup();
        apiDimensionJavaFilterGroup.setFilters(javaFilters);
        apiDimensionJavaFilterGroup.setGroupType("and");
        ApiDimensionFilter codecFilter = new ApiDimensionFilter();
        codecFilter.setDimension("page");
        codecFilter.setExpression("codec");
        codecFilter.setOperator("contains");
        ArrayList<ApiDimensionFilter> pythonFilters = new ArrayList<>();
        pythonFilters.add(codecFilter);
        ApiDimensionFilterGroup apiDimensionPythonFilterGroup = new ApiDimensionFilterGroup();
        apiDimensionPythonFilterGroup.setFilters(pythonFilters);
        apiDimensionPythonFilterGroup.setGroupType("and");
        List<ApiDimensionFilterGroup> apiDimensionFilterGroups = new ArrayList<>();
        apiDimensionFilterGroups.add(apiDimensionJavaFilterGroup);
        apiDimensionFilterGroups.add(apiDimensionPythonFilterGroup);
        return apiDimensionFilterGroups;
    }
}
