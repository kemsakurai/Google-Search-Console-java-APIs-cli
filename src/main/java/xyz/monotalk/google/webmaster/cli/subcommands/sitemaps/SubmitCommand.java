package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import java.net.URL;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.URLOptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * サイトマップを送信するコマンドクラス。
 */
@Component
public class SubmitCommand implements Command {
    /**
     * ロガーインスタンス。
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SubmitCommand.class);

    /**
     * Webmasters APIクライアント生成ファクトリ。
     */
    @Autowired
    private WebmastersFactory factory;

    /**
     * サイトURLを指定します。
     * このURLはGoogle Search Consoleで管理されているサイトのURLである必要があります。
     */
    @Option(
            name = "-siteUrl",
            usage = "Site URL",
            metaVar = "<siteUrl>",
            required = true,
            handler = URLOptionHandler.class)
    protected URL siteUrl;

    /**
     * サイトマップのフィードパス。
     * サイトマップの相対パスまたは完全なURLを指定します。
     */
    @Option(name = "-feedpath", usage = "Feed path", required = true)
    protected String feedpath;

    /**
     * デフォルトコンストラクタ。
     */
    public SubmitCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * サイトマップ送信コマンドを実行します。
     *
     * <p>このコマンドは、指定されたサイトのサイトマップをGoogle Search Consoleに送信します。</p>
     *
     * @throws CommandLineInputOutputException 入出力エラーが発生した場合。
     */
    @Override
    public void execute() {
        validateParameters();
        logSubmissionStart();

        try {
            final Webmasters webmasters = createWebmastersClient();
            submitSitemap(webmasters);
            logSubmissionSuccess();
        } catch (IOException e) {
            handleException(e);
        }
    }

    /**
     * サイトマップ送信のためのパラメータを検証します。
     *
     * @throws CmdLineArgmentException 必須パラメータが不足している場合。
     */
    private void validateParameters() {
        if (siteUrl == null) {
            throw new CmdLineArgmentException("Site URL is required");
        }
        if (feedpath == null) {
            throw new CmdLineArgmentException("Feed path is required");
        }
    }

    /**
     * サイトURLを設定します。
     *
     * @param siteUrl サイトのURL。
     */
    public void setSiteUrl(URL siteUrl) {
        this.siteUrl = siteUrl;
    }

    /**
     * WebmastersFactoryを設定します。
     *
     * @param factory WebmastersFactoryのインスタンス
     */
    public void setFactory(WebmastersFactory factory) {
        this.factory = factory;
    }

    /**
     * サイトURLを設定します。
     */
    private void logSubmissionStart() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Submitting sitemap {} for site {}", feedpath, siteUrl);
        }
    }

    /**
     * サイトマップのURLを設定します。
     *
     * @param sitemapUrl サイトマップのURL。
     */
    private Webmasters createWebmastersClient() {
        final Webmasters webmasters = factory.create();
        if (webmasters == null) {
            throw new CommandLineInputOutputException(new IOException("Failed to create Webmasters client"));
        }
        return webmasters;
    }

    private void submitSitemap(final Webmasters webmasters) throws IOException {
        // 修正: siteUrlとfeedpathを結合して完全なURLを生成
        final String sitemapUrl = siteUrl.toString() + "/" + feedpath;
        final Webmasters.Sitemaps.Submit request = webmasters.sitemaps().submit(siteUrl.toString(), sitemapUrl);
        request.execute();
    }

    /**
     * サイトマップを送信します。
     *
     * @throws CommandLineInputOutputException API実行エラーが発生した場合。
     */
    private void logSubmissionSuccess() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Sitemap submitted successfully");
        }
    }

    private void handleException(final IOException exception) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Failed to submit sitemap", exception);
        }
        throw new CommandLineInputOutputException(exception.getMessage(), exception);
    }

    @Override
    public String usage() {
        return "Submits a sitemap for this site.";
    }
}
