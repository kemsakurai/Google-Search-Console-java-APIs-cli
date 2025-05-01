package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

/**
 * DeleteCommand
 */
@Component
public class DeleteCommand implements Command {

    @Autowired protected WebmastersFactory factory;
    @Option(name = "-siteUrl", usage = "Url of site", required = true) protected String siteUrl = null;
    @Option(name = "-feedPath", usage = "Url of feedPath", required = true) protected String feedPath = null;

    @Override
    public void execute() {
        System.out.println("START Delete.");
        Webmasters webmasters = factory.create();
        Webmasters.Sitemaps.Delete delete;
        try {
            delete = webmasters.sitemaps().delete(siteUrl, feedPath);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        try {
            delete.execute();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        System.out.println("Done.");
    }

    @Override
    public String usage() {
        return "Deletes a sitemap from this site.";
    }
}
