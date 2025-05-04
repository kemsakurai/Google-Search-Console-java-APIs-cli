package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;

/**
 * DeleteCommandクラス - サイトをGoogle Search Consoleから削除するコマンド
 */
@Component
public class DeleteCommand implements Command {
    
    /**
     * デフォルトコンストラクタ
     */
    public DeleteCommand() {
        // デフォルトコンストラクタ
    }
    
    @Override
    public void execute() {
        // 実装
    }

    @Override
    public String usage() {
        return "Delete site";
    }
}
