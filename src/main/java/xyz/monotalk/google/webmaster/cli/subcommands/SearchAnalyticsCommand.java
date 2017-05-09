package xyz.monotalk.google.webmaster.cli.subcommands;

import com.google.api.services.webmasters.model.*;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * SearchAnalyticsExample
 */
public class SearchAnalyticsCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Option(name = "-id", usage = "ID to delete")
    private String id = "setme";

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
            out.println(searchAnalyticsQueryResponse.toPrettyString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("<<<END");

        for (ApiDataRow row : searchAnalyticsQueryResponse.getRows()) {
            try {
                out.println(row.toPrettyString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
