package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * DeleteCommandクラス - サイトマップ削除コマンド。
 */
@Component
public class DeleteCommand implements Command {

    /**
     * ロガーインスタンス。
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCommand.class);
    
    /**
     * WebmastersファクトリインスタンスへのAutowired参照。
     */
    @Autowired
    private WebmastersFactory factory;

    /**
     * サイトのURL。
     */
    @Option(name = "-siteUrl", usage = "Url of site", required = true)
    protected String siteUrl;

    /**
     * サイトマップのフィードパス。
     */
    @Option(name = "-feedPath", usage = "Url of feedPath", required = true)
    protected String feedPath;
        
    /**
     * デフォルトコンストラクタ。
     */
    public DeleteCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * テスト用のコンストラクタ。
     *
     * @param factory WebmastersFactoryインスタンス
     */
    DeleteCommand(final WebmastersFactory factory) {
        this.factory = factory;
    }

    /**
     * サイトマップを削除します。
     *
     * @throws CommandLineInputOutputException 入出力操作中にエラーが発生した場合。
     */
    @Override
    public void execute() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("START Delete.");
        }
        final Webmasters webmasters = factory.createClient();
        try {
            final Webmasters.Sitemaps.Delete delete = webmasters.sitemaps().delete(siteUrl, feedPath);
            delete.execute();
        } catch (IOException e) {
            throw new CommandLineInputOutputException(e.getMessage(), e);
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Done.");
        }
    }

    @Override
    public String usage() {
        return "Deletes a sitemap from this site.";
    }

    /**
     * サイトURLを設定します。
     *
     * @param siteUrl サイトのURL。
     */
    public void setSiteUrl(final String siteUrl) {
        this.siteUrl = siteUrl;
    }

    /**
     * サイトマップのフィードパスを設定します。
     *
     * @param feedPath サイトマップのフィードパス。
     */
    public void setFeedPath(final String feedPath) {
        this.feedPath = feedPath;
    }
}
