package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * DeleteCommandクラス - サイトをGoogle Search Consoleから削除するコマンド。
 */
@Component
public class DeleteCommand implements Command {

    /**
     * Webmasters APIクライアントを生成するファクトリー。
     */
    @Autowired private WebmastersFactory factory;

    /**
     * デフォルトコンストラクタ。
     */
    public DeleteCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * サイトを削除します。
     *
     * <p>このメソッドは、Google Search Console APIを使用して、
     * 指定されたサイトをユーザーのプロパティから削除します。
     *
     * @throws CommandLineInputOutputException サイトの削除に失敗した場合
     */
    @Override
    public void execute() {
        try {
            // Webmasters APIクライアントを生成
            final Webmasters webmasters = factory.createClient();
            // サイト削除ロジックを実装
            webmasters.sites().delete("example.com").execute();
        } catch (IOException e) {
            throw new CommandLineInputOutputException("Failed to delete site", e);
        }
    }

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明
     */
    @Override
    public String usage() {
        return "Deletes a site from the user's Search Console properties.";
    }
}
