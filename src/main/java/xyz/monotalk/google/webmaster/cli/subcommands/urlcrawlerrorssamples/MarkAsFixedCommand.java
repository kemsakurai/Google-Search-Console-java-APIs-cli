package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import com.google.api.services.webmasters.Webmasters;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

/**
 * MarkAsFixedCommand
 */
@Component
public class MarkAsFixedCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Option(name = "-url", usage = "URL to mark as fixed", required = true)
    private String url;

    @Option(name = "-platform", usage = "Platform (web, mobile, smartphoneOnly)", required = true)
    private String platform = "web";

    @Option(name = "-category", usage = "Error category", required = true)
    private String category = "notFound";

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void execute() {
        if (url == null) {
            throw new CmdLineArgmentException("URL must be specified");
        }

        try {
            Webmasters.Urlcrawlerrorssamples.MarkAsFixed request = factory.create()
                .urlcrawlerrorssamples()
                .markAsFixed("https://www.monotalk.xyz", url, category, platform);
            request.execute();
        } catch (IOException e) {
            throw new CmdLineIOException(e);
        }
    }

    @Override
    public String usage() {
        return "指定されたサイトのサンプルURLを修正済みとしてマークし、サンプルリストから削除します。";
    }
}
