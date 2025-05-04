package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import xyz.monotalk.google.webmaster.cli.Command;

/**
 * DeleteCommandクラス - サイトをGoogle Search Consoleから削除するコマンド
 */
public class DeleteCommand implements Command {
    
    /**
     * デフォルトコンストラクタ
     */
    public DeleteCommand() {
        // デフォルトコンストラクタ
    }
    
    @Override
    public void execute() {
        throw new UnsupportedOperationException("TODO not implemented..");
    }
    @Override
    public String usage() {
        return "Removes a site from the set of the user's Search Console sites.";
    }
}
