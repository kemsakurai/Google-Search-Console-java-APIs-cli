package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;

/**
 * サイト情報を取得するコマンドです。
 */
@Component
public class GetCommand implements Command {
    
    /**
     * デフォルトコンストラクタ
     */
    public GetCommand() {
        // デフォルトコンストラクタ
    }
    
    @Override
    public void execute() {
        // 実装
    }

    @Override
    public String usage() {
        return "Get site information";
    }
}
