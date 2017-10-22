package xyz.monotalk.google.webmaster.cli;

/**
 * Command interface
 */
public interface Command {
    /**
     * Main Method
     */
    void execute();

    /**
     * Usage
     * @return
     */
    String usage();

}
