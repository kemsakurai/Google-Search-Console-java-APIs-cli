package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.UrlCrawlErrorsSample;
import com.google.api.services.webmasters.model.UrlCrawlErrorsSamplesListResponse;
import com.google.api.services.webmasters.model.UrlSampleDetails;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;
import java.util.List;

import static java.lang.System.out;

/**
 * ListCommand
 */
public class ListCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Override
    public void execute() {
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
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String usage() {
        return null;
    }
}
