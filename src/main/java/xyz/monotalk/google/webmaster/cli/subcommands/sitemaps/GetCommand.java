package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.WmxSitemap;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.URLOptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.*;

import java.io.IOException;
import java.net.URL;

/**
 * GetCommand
 */
@Component
public class GetCommand implements Command {

    @Autowired
    protected WebmastersFactory factory;

    @Option(name = "-siteUrl", usage = "Site URL", metaVar = "<siteUrl>", required = true,
            handler = URLOptionHandler.class)
    protected URL siteUrl = null;

    @Option(name = "-feedpath", usage = "Feed path", required = true)
    protected String feedpath = null;

    @Option(name = "-format", usage = "Output format", metaVar = "[console or json]")
    protected Format format = Format.CONSOLE;

    @Option(name = "-filePath", usage = "JSON file path", metaVar = "<filename>", depends = {"-format"})
    protected String filePath = null;

    @Override
    public void execute() throws CmdLineException {
        if (siteUrl == null) {
            throw new CmdLineArgmentException("Site URL is required");
        }
        if (feedpath == null) {
            throw new CmdLineArgmentException("Feed path is required");
        }

        try {
            Webmasters webmasters = factory.create();
            if (webmasters == null) {
                throw new CmdLineIOException(new IOException("Failed to create Webmasters client"));
            }
            
            Webmasters.Sitemaps.Get request = webmasters.sitemaps().get(siteUrl.toString(), feedpath);
            WmxSitemap response = request.execute();
            ResponseWriter.writeJson(response, format, filePath);
        } catch (IOException e) {
            throw new CmdLineIOException(e);
        }
    }

    @Override
    public String usage() {
        return "Gets information about a specific sitemap.";
    }
}
