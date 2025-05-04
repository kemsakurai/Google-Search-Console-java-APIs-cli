package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * DeleteCommand. サイトマップを削除するコマンドです。
 */
@Component
public class DeleteCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCommand.class);

    @Autowired
    private final WebmastersFactory factory;

    @Option(name = "-siteUrl", usage = "Url of site", required = true)
    private String siteUrl;

    @Option(name = "-feedPath", usage = "Url of feedPath", required = true)
    private String feedPath;

    /**
     * コンストラクタ.
     */
    public DeleteCommand() {
        this.factory = null; // Spring DIのために必要なnull初期化
    }

    /**
     * サイトマップを削除します。
     * 
     * @throws CommandLineInputOutputException 入出力操作中にエラーが発生した場合。
     * @throws CmdLineIOException API要求の実行中にエラーが発生した場合。
     */
    @Override
    public void execute() throws CommandLineInputOutputException, CmdLineIOException {
        LOGGER.info("START Delete.");
        Webmasters webmasters = factory.create();
        try {
            Webmasters.Sitemaps.Delete delete = webmasters.sitemaps().delete(siteUrl, feedPath);
            delete.execute();
        } catch (IOException e) {
            throw new CmdLineIOException(e.getMessage(), e);
        }
        LOGGER.info("Done.");
    }

    @Override
    public String usage() {
        return "Deletes a sitemap from this site.";
    }

    /**
     * サイトURLを設定します。
     * 
     * @param siteUrl
     *            サイトのURL。
     */
    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    /**
     * サイトマップのフィードパスを設定します。
     * 
     * @param feedPath
     *            サイトマップのフィードパス。
     */
    public void setFeedPath(String feedPath) {
        this.feedPath = feedPath;
    }
}
