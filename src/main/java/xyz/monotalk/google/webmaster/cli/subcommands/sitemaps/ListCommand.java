package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitemapsListResponse;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.URLOptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.monotalk.google.webmaster.cli.*;

import java.io.IOException;
import java.net.URL;

/**
 * ListCommand
 */
public class ListCommand implements Command {

    @Autowired protected WebmastersFactory factory;

    @Option(name = "-siteUrl", usage = "Site URL", metaVar = "<siteUrl>", required = true,
            handler = URLOptionHandler.class)
    protected URL siteUrl = null;

    @Option(name = "-format", usage = "Output format", metaVar = "[console or json]")
    protected Format format = Format.CONSOLE;

    @Option(name = "-filePath", usage = "JSON file path", metaVar = "<filename>", depends = {"-format"})
    protected String filePath = null;

    @Override
    public void execute() throws CmdLineException {
        if (Format.JSON.equals(format) && filePath == null) {
            throw new CmdLineException(null, "For JSON format, filepath is mandatory.");
        }

        Webmasters.Sitemaps.List request;
        try {
            request = factory.create().sitemaps().list(siteUrl.toString());
        } catch (IOException e) {
            throw new CmdLineIOException(e);
        }

        SitemapsListResponse response;
        try {
            response = request.execute();
        } catch (IOException e) {
            throw new CmdLineIOException(e);
        }

        ResponseWriter.writeJson(response, format, filePath);
    }

    @Override
    public String usage() {
        return "Lists the sitemaps-entries submitted for this site, or included in the sitemap index file (if sitemapIndex is specified in the request).";
    }
}
