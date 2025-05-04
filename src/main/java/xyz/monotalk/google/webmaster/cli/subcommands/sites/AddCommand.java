package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;



/**
 * Google Search Console APIのサイト追加コマンドです。
 */
@Component
public class AddCommand implements Command {

    @Autowired 
    private WebmastersFactory factory;

    @Option(name = "-siteUrl", usage = "Site URL to add", required = true)
    private String siteUrl;

    @Option(name = "-format", usage = "Output format [console or json]")
    private Format format = Format.CONSOLE;

    @Override
    public void execute() throws CmdLineIOException {
        try {
            Webmasters webmasters = factory.create();
            Webmasters.Sites.Add request = webmasters.sites().add(siteUrl);
            request.execute();
            
            if (format == Format.CONSOLE) {
                System.out.println("Successfully added site: " + siteUrl);
            }
        } catch (IOException e) {
            throw new CmdLineIOException(e.getMessage(), e);
        }
    }

    @Override
    public String usage() {
        return "Adds a site to the set of the user's sites in Search Console.";
    }

    // For testing
    void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    void setFormat(Format format) {
        this.format = format;
    }
}
