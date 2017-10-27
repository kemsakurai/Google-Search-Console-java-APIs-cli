package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import org.springframework.beans.factory.annotation.Autowired;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * MarkAsFixedCommand
 */
public class MarkAsFixedCommand implements Command {

    @Autowired private WebmastersFactory factory;

    @Override
    public void execute() {
        throw new UnsupportedOperationException("TODO not implemented..");
    }

    @Override
    public String usage() {
        return "Marks the provided site's sample URL as fixed, and removes it from the samples list.";
    }
}
