package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorscounts;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.UrlCrawlErrorsCountsQueryResponse;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.URLOptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.monotalk.google.webmaster.cli.*;

import java.io.IOException;
import java.net.URL;

/**
 * QueryCommand
 */
public class QueryCommand implements Command {

    @Autowired private WebmastersFactory factory;

    @Option(name = "-siteUrl", usage = "Url of site", metaVar = "<siteUrl>", required = true,
            handler = URLOptionHandler.class) private URL siteUrl = null;

    @Option(name = "-format", usage = "Format of output ",
            metaVar = "[console or json]") private Format format = Format.CONSOLE;

    @Option(name = "-filePath", usage = "File name of json ", metaVar = "<filename>",
            depends = {"-format"}) private String filePath = null;

    @Override
    public void execute() {
        Webmasters.Urlcrawlerrorscounts.Query request;
        try {
            request = factory.create().urlcrawlerrorscounts().query(siteUrl.toString());
        } catch (IOException e) {
            throw new CmdLineIOException(e);
        }
        UrlCrawlErrorsCountsQueryResponse response;
        try {
            response = request.execute();
        } catch (IOException e) {
            throw new CmdLineIOException(e);
        }
        ResponseWriter.writeJson(response, format, filePath);
// -------------------------------
//  for each CountPerTypes...
// -------------------
//        List<UrlCrawlErrorCountsPerType> urlCrawlErrorCountsPerTypes = response.getCountPerTypes();
//        int i = 0;
//        for (UrlCrawlErrorCountsPerType urlCrawlErrorCountsPerType : urlCrawlErrorCountsPerTypes) {
//            out.println("--------urlCrawlErrorCountsPerType:elem" + (++i) + "--------");
//            out.println("category:" + urlCrawlErrorCountsPerType.getCategory());
//            out.println("platform:" + urlCrawlErrorCountsPerType.getPlatform());
//            java.util.List<UrlCrawlErrorCount> urlCrawlErrorCounts = urlCrawlErrorCountsPerType.getEntries();
//            int j = 0;
//            for (UrlCrawlErrorCount urlCrawlErrorCount : urlCrawlErrorCounts) {
//                out.println("----urlCrawlErrorCount:elem" + (++j) + "--------");
//                out.println("count:" + urlCrawlErrorCount.getCount());
//                out.println("timestamp:" + urlCrawlErrorCount.getTimestamp());
//            }
//        }
    }

    @Override
    public String usage() {
        return "Retrieves a time series of the number of URL crawl errors per error category and platform.";
    }
}
