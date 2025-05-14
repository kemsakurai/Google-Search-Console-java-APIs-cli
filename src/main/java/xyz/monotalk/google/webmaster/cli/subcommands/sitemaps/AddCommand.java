package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * サイトマップを追加するコマンドクラス。
 */
@Component
public class AddCommand implements Command {

    /** ロガーインスタンス。 */
    private static final Logger LOGGER = LoggerFactory.getLogger(AddCommand.class);

    /** サイトURL。 */
    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    protected String siteUrl;

    /** フィードパス。 */
    @Option(name = "-feedPath", usage = "Feed path", required = true)
    protected String feedPath;

    /** 出力フォーマット。 */
    @Option(name = "-format", usage = "Output format")
    private Format format = Format.CONSOLE;

    /** 出力ファイルパス。 */
    @Option(name = "-filePath", usage = "Output file path")
    private String filePath;

    /** WebmastersFactoryインスタンス。 */
    private final WebmastersFactory factory;

    /**
     * コンストラクタ。
     *
     * @param factory WebmastersFactoryインスタンス
     */
    public AddCommand(final WebmastersFactory factory) {
        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        validateArguments();

        try {
            submitSitemap();
        } catch (IOException e) {
            throw new CommandLineInputOutputException("Failed to submit sitemap", e);
        }
    }

    /**
     * 引数を検証します。
     *
     * @throws CmdLineArgmentException 引数が無効な場合
     */
    private void validateArguments() {
        if (siteUrl == null || feedPath == null) {
            throw new CmdLineArgmentException("SiteURL and FeedPath must be specified");
        }
        if (format == Format.JSON && filePath == null) {
            throw new CmdLineArgmentException("File path must be specified when using JSON format");
        }
    }

    /**
     * サイトマップを送信します。
     *
     * @throws IOException APIコール中にエラーが発生した場合
     */
    private void submitSitemap() throws IOException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Submitting sitemap {} for site {}", feedPath, siteUrl);
        }

        final Webmasters webmasters = factory.createClient();
        webmasters.sitemaps().submit(siteUrl, siteUrl).execute();

        // APIはvoidを返すため、成功メッセージを生成して返す
        final Response response = new Response("Successfully submitted sitemap " + feedPath);
        ResponseWriter.writeJson(response, format, filePath);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public String usage() {
        return "Adds a sitemap to Google Search Console.";
    }
    
    /**
     * レスポンスメッセージを格納する内部クラス。
     */
    private static class Response {
        /** レスポンスメッセージ。 */
        private final String message;

        /**
         * デフォルトコンストラクタ。
         * デフォルト（パッケージプライベート）アクセス修飾子を使用しています。
         *
         * @param message レスポンスメッセージ
         */
        /* default */ Response(final String message) {
            this.message = message;
        }

        /**
         * メッセージを取得します。
         *
         * @return メッセージ
         */
        public String getMessage() {
            return message;
        }
    }
}
