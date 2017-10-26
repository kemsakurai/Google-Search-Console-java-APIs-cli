package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorscounts;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.UrlCrawlErrorCount;
import com.google.api.services.webmasters.model.UrlCrawlErrorCountsPerType;
import com.google.api.services.webmasters.model.UrlCrawlErrorsCountsQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;
import java.util.List;

import static java.lang.System.out;

/**
 * QueryCommand
 */
public class QueryCommand implements Command {

    @Autowired private WebmastersFactory factory;

    @Override
    public void execute() {
        Webmasters.Urlcrawlerrorscounts.Query request = null;
        try {
            request = factory.create().urlcrawlerrorscounts().query("https://www.monotalk.xyz");
        } catch (IOException e) {
            e.printStackTrace();
        }
        UrlCrawlErrorsCountsQueryResponse response = null;
        try {
            response = request.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("response#toPrettyString() START>>>");
        try {
            out.println(response.toPrettyString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("<<<END");

        out.println("responset#getCountPerTypes(); START>>>");
        List<UrlCrawlErrorCountsPerType> urlCrawlErrorCountsPerTypes = response.getCountPerTypes();
        int i = 0;
        for (UrlCrawlErrorCountsPerType urlCrawlErrorCountsPerType : urlCrawlErrorCountsPerTypes) {
            out.println("--------urlCrawlErrorCountsPerType:elem" + (++i) + "--------");
            out.println("category:" + urlCrawlErrorCountsPerType.getCategory());
            out.println("platform:" + urlCrawlErrorCountsPerType.getPlatform());
            java.util.List<UrlCrawlErrorCount> urlCrawlErrorCounts = urlCrawlErrorCountsPerType.getEntries();
            int j = 0;
            for (UrlCrawlErrorCount urlCrawlErrorCount : urlCrawlErrorCounts) {
                out.println("----urlCrawlErrorCount:elem" + (++j) + "--------");
                out.println("count:" + urlCrawlErrorCount.getCount());
                out.println("timestamp:" + urlCrawlErrorCount.getTimestamp());
            }
        }
        out.println("<<<END");

    }

    @Override
    public String usage() {
        return null;
    }
}
