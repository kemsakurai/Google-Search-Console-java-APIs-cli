package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;

/**
 * サイト情報を取得するコマンドクラス。
 *
 * <p>このクラスは、指定されたサイトの情報を取得し、適切なフォーマットで出力します。</p>
 */
@Component
public class GetCommand implements Command {

    /**
     * デフォルトコンストラクタ。
     */
    public GetCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * サイト情報を取得する処理を実行します。
     *
     * <p>現在は未実装です。</p>
     */
    @Override
    public void execute() {
        // 実装予定
    }

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明
     */
    @Override
    public String usage() {
        return "Get site information";
    }
}
