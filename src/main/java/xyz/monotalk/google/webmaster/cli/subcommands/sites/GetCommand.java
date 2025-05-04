package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import xyz.monotalk.google.webmaster.cli.Command;

/**
 * GetCommandクラス - 特定のサイト情報を取得するコマンド
 */
public class GetCommand implements Command {
    
    /**
     * デフォルトコンストラクタ
     */
    public GetCommand() {
        // デフォルトコンストラクタ
    }
    
    @Override
    public void execute() {
        throw new UnsupportedOperationException("TODO not implemented..");
    }
    @Override
    public String usage() {
        return "Retrieves information about specific site.";
    }
}
