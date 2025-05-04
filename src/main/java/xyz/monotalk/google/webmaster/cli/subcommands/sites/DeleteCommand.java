package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;

/**
 * サイトを削除するコマンドです。
 */
@Component
public class DeleteCommand implements Command {

    @Override
    public void execute() throws Exception {
        // 実装
    }

    @Override
    public String usage() {
        return "Delete site";
    }
}
