package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import org.springframework.beans.factory.annotation.Autowired;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * GetCommand
 */
public class GetCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Override
    public void execute() {
        throw new UnsupportedOperationException("TODO not implemented..");
    }

    @Override
    public String usage() {
        return "Retrieves details about crawl errors for a site's sample URL.";
    }
}
