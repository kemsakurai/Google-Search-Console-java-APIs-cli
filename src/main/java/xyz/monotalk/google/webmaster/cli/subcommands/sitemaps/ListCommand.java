package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitemapsListResponse;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.URLOptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;
import java.net.URL;

/**
 * ListCommand
 */
@Component
public class ListCommand implements Command {

    @Autowired private WebmastersFactory factory;

    @Option(name = "-siteUrl", usage = "Url of site", metaVar = "<siteUrl>", required = true,
            handler = URLOptionHandler.class) private URL siteUrl = null;

    @Option(name = "-format", usage = "Format of output ",
            metaVar = "[console or json]") private Format format = Format.CONSOLE;

    @Option(name = "-filePath", usage = "File name of json ", metaVar = "<filename>",
            depends = {"-format"}) private String filePath = null;

    @Override
    public void execute() throws CmdLineException {
        Webmasters webmasters = factory.create();
        Webmasters.Sitemaps.List siteMaps;
        try {
            siteMaps = webmasters.sitemaps().list(siteUrl.toString());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        SitemapsListResponse response;
        try {
            response = siteMaps.execute();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        ResponseWriter.writeJson(response, format, filePath);
    }


    @Override
    public String usage() {
        return "Lists the sitemaps-entries submitted for this site, or included in the sitemap index file (if sitemapIndex is specified in the request).";
    }
}
