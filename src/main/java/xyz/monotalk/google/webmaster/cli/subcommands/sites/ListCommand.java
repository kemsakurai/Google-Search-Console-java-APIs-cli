package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

/**
 * ListCommand
 */
@Component
public class ListCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Override
    public void execute() {
        Webmasters webmasters = factory.create();
        Webmasters.Sites.List request = null;
        try {
            request = webmasters.sites().list();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SitesListResponse siteList = null;
        try {
            siteList = request.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("-------------------------siteList");
        try {
            System.out.print(siteList.toPrettyString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("-------------------------siteList");
    }

    @Override
    public String usage() {
        return "Lists the user's Search Console sites.";
    }
}
