package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitemapsListResponse;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

/**
 * SiteMapsListCommand
 */
@Component
public class SiteMapsListCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Option(name = "-siteUrl", usage = "Url of site", required = true)
    private String siteUrl = null;

    @Override
    public void execute() {
        Webmasters webmasters = factory.create();
        Webmasters.Sitemaps.List siteMaps;
        try {
            siteMaps = webmasters.sitemaps().list(siteUrl);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        SitemapsListResponse response;
        try {
            response = siteMaps.execute();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        try {
            System.out.println(response.toPrettyString());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String usage() {
        return null;
    }
}
