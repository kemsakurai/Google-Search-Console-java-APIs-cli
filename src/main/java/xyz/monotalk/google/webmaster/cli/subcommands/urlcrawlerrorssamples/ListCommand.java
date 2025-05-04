package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import static java.lang.System.out;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.UrlCrawlErrorsSample;
import com.google.api.services.webmasters.model.UrlCrawlErrorsSamplesListResponse;
import com.google.api.services.webmasters.model.UrlSampleDetails;
import java.io.IOException;
import java.util.List;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * ListCommand.
 */
@Component
public class ListCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    private String siteUrl = null;

    @Override
    public void execute() throws CmdLineIOException {
        try {
            Webmasters.Urlcrawlerrorssamples.List request = factory.create().urlcrawlerrorssamples().list("https://www.monotalk.xyz", "notFound", "web");
            UrlCrawlErrorsSamplesListResponse response = request.execute();
            out.println("response#toPrettyString() START>>>");
            out.println(response.toPrettyString());
            out.println("<<<END");

            out.println("responset#getUrlCrawlErrorSample(); START>>>");
            List<UrlCrawlErrorsSample> urlCrawlErrorSamples = response.getUrlCrawlErrorSample();
            int i = 0;
            for (UrlCrawlErrorsSample urlCrawlErrorsample : urlCrawlErrorSamples) {
                out.println("--------urlCrawlErrorsample:elem" + (++i) + "--------");
                out.println("pageUrl:" + urlCrawlErrorsample.getPageUrl());
                out.println("firstDetected:" + urlCrawlErrorsample.getFirstDetected());
                out.println("lastCrawled:" + urlCrawlErrorsample.getLastCrawled());
                out.println("responseCode:" + urlCrawlErrorsample.getResponseCode());
                UrlSampleDetails userDetails = urlCrawlErrorsample.getUrlDetails();
            }
            out.println("<<<END");
        } catch (IOException e) {
            throw new CmdLineIOException("API Error", e);
        }
    }

    @Override
    public String usage() {
        return "Lists a site's sample URLs for the specified crawl error category and platform.";
    }
}
