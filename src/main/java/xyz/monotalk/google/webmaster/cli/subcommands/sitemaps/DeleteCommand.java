package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

/**
 * DeleteCommandクラス - サイトマップ削除コマンド
 */
@Component
public class DeleteCommand implements Command {

    /**
     * ロガーインスタンス
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCommand.class);
    
    /**
     * WebmastersファクトリーインスタンスDI用
     */
    @Autowired protected WebmastersFactory factory;
    
    /**
     * サイトURL
     */
    @Option(name = "-siteUrl", usage = "Url of site", required = true) protected String siteUrl;
    
    /**
     * フィードパス
     */
    @Option(name = "-feedPath", usage = "Url of feedPath", required = true) protected String feedPath;

    /**
     * デフォルトコンストラクタ
     */
    public DeleteCommand() {
        // デフォルトコンストラクタ
    }

    @Override
    public void execute() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("START Delete.");
        }
        final Webmasters webmasters = factory.create();
        final Webmasters.Sitemaps.Delete delete;
        try {
            delete = webmasters.sitemaps().delete(siteUrl, feedPath);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        try {
            delete.execute();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Done.");
        }
    }

    @Override
    public String usage() {
        return "Deletes a sitemap from this site.";
    }
}
