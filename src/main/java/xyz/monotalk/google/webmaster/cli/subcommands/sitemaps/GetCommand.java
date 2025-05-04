package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.WmxSitemap;
import java.io.IOException;
import java.net.URL;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.URLOptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * サイトマップ情報を取得するコマンドです。
 */
@Component
public class GetCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Autowired
    private ResponseWriter responseWriter;

    @Option(name = "-siteUrl", usage = "Site URL", required = true,
            handler = URLOptionHandler.class)
    private URL siteUrl = null;

    @Option(name = "-feedpath", usage = "Feed path", required = true)
    private String feedpath = null;

    @Option(name = "-format", usage = "Output format [console or json]")
    private Format format = Format.CONSOLE;

    @Option(name = "-filePath", usage = "Output file path", depends = {"-format"})
    private String filePath = null;

    @Override
    public void execute() throws Exception {
        try {
            Webmasters webmasters = factory.create();
            Webmasters.Sitemaps.Get get = webmasters.sitemaps().get(
                    siteUrl.toString(), feedpath);
            WmxSitemap response = get.execute();
            responseWriter.writeJson(response, format, filePath);
        } catch (IOException e) {
            throw new CmdLineIOException("API Error", e);
        }
    }

    @Override
    public String usage() {
        return "Gets information about a specific sitemap";
    }
}
