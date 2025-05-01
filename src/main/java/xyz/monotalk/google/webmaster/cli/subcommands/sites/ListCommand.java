package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

/**
 * サイト一覧を取得するコマンド
 */
@Component
public class ListCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Override
    public void execute() throws CmdLineIOException {
        Webmasters webmasters = factory.create();
        try {
            SitesListResponse response = webmasters.sites().list().execute();
            ResponseWriter.writeJson(response, Format.CONSOLE, null);
        } catch (IOException e) {
            throw new CmdLineIOException("Failed to execute API request: " + e.getMessage(), e);
        }
    }

    @Override
    public String usage() {
        return "Lists the user's Search Console sites.";
    }
}
