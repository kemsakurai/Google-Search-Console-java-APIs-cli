package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.WmxSitemap;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

/**
 * SiteMapsGetCommand
 */
public class SiteMapsGetCommand implements Command {

    @Autowired
    private WebmastersFactory factory;
    @Option(name = "-siteUrl", usage = "Url of site", required = true)
    private String siteUrl = null;
    @Option(name = "-feedPath", usage = "Url of feedPath", required = true)
    private String feedPath = null;

    @Override
    public void execute() {
        Webmasters webmasters = factory.create();
        Webmasters.Sitemaps.Get get;
        try {
            get = webmasters.sitemaps().get(siteUrl, feedPath);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        WmxSitemap wmxSitemap;
        try {
            wmxSitemap = get.execute();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        try {
            System.out.println(wmxSitemap.toPrettyString());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
