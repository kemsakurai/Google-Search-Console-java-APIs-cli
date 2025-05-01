package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.URLOptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.*;

import java.io.IOException;
import java.net.URL;

/**
 * SubmitCommand
 */
@Component
public class SubmitCommand implements Command {

    @Autowired
    protected WebmastersFactory factory;

    @Option(name = "-siteUrl", usage = "Site URL", metaVar = "<siteUrl>", required = true,
            handler = URLOptionHandler.class)
    protected URL siteUrl = null;

    @Option(name = "-feedpath", usage = "Feed path", required = true)
    protected String feedpath = null;

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

            Webmasters.Sitemaps.Submit request = webmasters.sitemaps().submit(siteUrl.toString(), feedpath);
            request.execute();
        } catch (IOException e) {
            throw new CmdLineIOException(e);
        }
    }

    @Override
    public String usage() {
        return "Submits a sitemap for this site.";
    }
}
