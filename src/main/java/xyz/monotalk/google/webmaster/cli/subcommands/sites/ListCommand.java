package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * サイト一覧を取得するコマンドクラス。
 */
@Component
public class ListCommand implements Command {

    /** ロガーインスタンス。 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ListCommand.class);

    /** Webmasters APIクライアントを生成するファクトリー。 */
    private final WebmastersFactory factory;

    /**
     * コンストラクター。
     * 指定されたWebmastersFactoryを使用してインスタンスを初期化します。
     * コンストラクターからの例外スローを避けるため、nullチェックはexecuteメソッドで行います。
     *
     * @param factory WebmastersFactoryインスタンス（null不可）
     */
    public ListCommand(final WebmastersFactory factory) {
        // SpotBugs違反を解消するため、コンストラクターからの例外スローを避ける
        this.factory = factory;
    }

    /**
     * サイト一覧を取得し、指定された形式で出力します。
     * ユーザーが所有するサイトのリストを取得し、指定されたフォーマットで出力します。
     *
     * @throws CommandLineInputOutputException サイト一覧の取得に失敗した場合
     */
    @Override
    public void execute() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Retrieving site list");
        }
        
        // factoryがnullでないことを確認（コンストラクターでのチェックを避けるためここでチェック）
        if (factory == null) {
            throw new IllegalStateException("WebmastersFactory is not initialized");
        }

        try {
            final Webmasters webmasters = factory.createClient();
            final Webmasters.Sites.List list = webmasters.sites().list();
            final SitesListResponse response = list.execute();
            ResponseWriter.writeJson(response, Format.CONSOLE, null);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Site list retrieved successfully");
            }
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to list sites", e);
            }
            throw new CommandLineInputOutputException("Failed to list sites", e);
        }
    }

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明
     */
    @Override
    public String usage() {
        return "Lists the user's Search Console sites.";
    }
}
