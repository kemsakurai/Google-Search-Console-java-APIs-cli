package xyz.monotalk.google.webmaster.cli.subcommands;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitemapsListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

/**
 * SiteMapsCommand
 */
@Component
public class SiteMapsCommand implements Command {

    @Autowired
    private WebmastersFactory factory;


    @Override
    public void execute() {
        Webmasters webmasters = factory.create();
        Webmasters.Sitemaps.List siteMaps = null;
        try {
            siteMaps = webmasters.sitemaps().list("https://www.monotalk.xyz");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SitemapsListResponse response = null;
        try {
            response = siteMaps.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("-------------------------sitemaps");
        try {
            System.out.print(response.toPrettyString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("-------------------------sitemaps");
    }
}
