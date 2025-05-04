package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * URLクロールエラーサンプルを取得するコマンドです。
 */
@Component
public class GetCommand implements Command {

    /** Webmasters APIクライアント生成ファクトリ */
    @Autowired
    private WebmastersFactory factory;
    
    /**
     * デフォルトコンストラクタ
     */
    public GetCommand() {
        // デフォルトコンストラクタ
    }

    @Override
    public void execute() throws Exception {
        // 実装
    }

    @Override
    public String usage() {
        return "Get URL crawler error samples";
    }
}
