package xyz.monotalk.google.webmaster.cli;

import org.kohsuke.args4j.CmdLineException;

/**
 * Command interface
 */
public interface Command {
    /**
     * Main Method
     */
    void execute() throws CmdLineException;

    /**
     * Usage
     * @return
     */
    String usage();

}
