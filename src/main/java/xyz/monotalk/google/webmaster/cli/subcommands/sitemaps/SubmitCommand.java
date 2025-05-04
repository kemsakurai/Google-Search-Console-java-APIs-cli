package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import java.net.URL;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.URLOptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * サイトマップを送信するコマンドです。
 */
@Component
public class SubmitCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Option(name = "-siteUrl", usage = "Site URL", required = true,
            handler = URLOptionHandler.class)
    private URL siteUrl = null;

    @Option(name = "-feedpath", usage = "Feed path", required = true)
    private String feedpath = null;

    @Override
    public void execute() throws Exception {
        try {
            Webmasters webmasters = factory.create();
            Webmasters.Sitemaps.Submit submit = webmasters.sitemaps().submit(
                    siteUrl.toString(), feedpath);
            submit.execute();
        } catch (IOException e) {
            throw new CmdLineIOException("API Error", e);
        }
    }

    @Override
    public String usage() {
        return "Submits a sitemap for a site";
    }
}
