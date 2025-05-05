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
 * SubmitCommandクラス - サイトマップ送信コマンド
 */
@Component
public class SubmitCommand implements Command {

    /**
     * ロガーインスタンス
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SubmitCommand.class);

    /**
     * WebmastersファクトリーインスタンスDI用
     */
    @Autowired
    private WebmastersFactory factory;

    /**
     * サイトURL
     */
    @Option(name = "-siteUrl", usage = "Site URL", metaVar = "<siteUrl>", required = true,
            handler = URLOptionHandler.class)
    protected URL siteUrl;

    /**
     * フィードパス
     */
    @Option(name = "-feedpath", usage = "Feed path", required = true)
    protected String feedpath;

    /**
     * デフォルトコンストラクタ
     */
    public SubmitCommand() {
        // デフォルトコンストラクタ
    }

    @Override
    public void execute() {
        // パラメータのバリデーション
        validateParameters();
        
        // 処理開始ログ出力
        logSubmissionStart();
        
        try {
            // Webmastersクライアントの作成
            final Webmasters webmasters = createWebmastersClient();
            
            // サイトマップの送信リクエスト実行
            submitSitemap(webmasters);
            
            // 成功ログ出力
            logSubmissionSuccess();
        } catch (IOException e) {
            // エラーログ出力と例外スロー
            handleException(e);
        }
    }

    /**
     * パラメータの妥当性を検証します
     * 
     * @throws CmdLineArgmentException パラメータが無効な場合
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
     * サイトマップ送信開始のログを出力します
     */
    private void logSubmissionStart() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Submitting sitemap {} for site {}", feedpath, siteUrl);
        }
    }

    /**
     * Webmastersクライアントを作成します
     * 
     * @return 作成されたWebmastersクライアント
     * @throws CommandLineInputOutputException クライアント作成に失敗した場合
     */
    private Webmasters createWebmastersClient() {
        final Webmasters webmasters = factory.create();
        if (webmasters == null) {
            throw new CommandLineInputOutputException(new IOException("Failed to create Webmasters client"));
        }
        return webmasters;
    }

    /**
     * サイトマップを送信します
     * 
     * 
@param webmasters Webmastersクライアント
     * @throws IOException API実行エラーが発生した場合
     */
    private void submitSitemap(final Webmasters webmasters) throws IOException {
        final Webmasters.Sitemaps.Submit request = webmasters.sitemaps().submit(siteUrl.toString(), feedpath);
        request.execute();
    }

    /**
     * サイトマップ送信成功のログを出力します
     */
    private void logSubmissionSuccess() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Sitemap submitted successfully");
        }
    }

    /**
     * 例外を処理します
     * 
     * 
@param exception 発生した例外
     * @throws CommandLineInputOutputException ラップされた例外
     */
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
