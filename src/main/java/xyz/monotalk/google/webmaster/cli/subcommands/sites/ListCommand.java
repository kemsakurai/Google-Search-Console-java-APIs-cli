package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
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

    /**
     * Webmasters APIクライアントを生成するファクトリー。
     */
    private final WebmastersFactory factory;

    /**
     * コンストラクタ。
     *
     * @param factory WebmastersFactoryインスタンス
     */
    public ListCommand(final WebmastersFactory factory) {
        this.factory = factory;
    }

    /**
     * サイト一覧を取得し、指定された形式で出力します。
     *
     * <p>このメソッドは、Google Search Console APIを使用して、
     * ユーザーが所有するサイトのリストを取得し、指定されたフォーマットで出力します。
     *
     * @throws CommandLineInputOutputException サイト一覧の取得に失敗した場合
     */
    @Override
    public void execute() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Retrieving site list");
        }

        try {
            final Webmasters webmasters = factory.create();
            final Webmasters.Sites.List list = webmasters.sites().list();
            final SitesListResponse response = list.execute();
            ResponseWriter.writeJson(response, Format.CONSOLE, null);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Site list retrieved successfully");
            }
        } catch (IOException | GeneralSecurityException e) {
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
