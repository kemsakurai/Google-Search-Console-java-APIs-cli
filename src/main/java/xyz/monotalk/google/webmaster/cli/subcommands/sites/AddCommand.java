package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * サイトを追加するコマンドクラス。
 */
@Component
public class AddCommand implements Command {

    /**
     * サイトURL。
     */
    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    private String siteUrl;

    /**
     * 出力フォーマット。
     */
    @Option(name = "-format", usage = "Output format")
    private Format format = Format.CONSOLE;

    /**
     * 出力ファイルパス。
     */
    @Option(name = "-filePath", usage = "Output file path")
    private String filePath;

    /**
     * WebmastersFactoryインスタンス。
     */
    @Autowired
    private WebmastersFactory factory;

    /**
     * デフォルトコンストラクタ。
     */
    public AddCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        validateArguments();
        try {
            addSite();
        } catch (IOException e) {
            throw new CommandLineInputOutputException("Failed to add site: " + e.getMessage(), e);
        }
    }

    /**
     * 引数を検証します。
     */
    private void validateArguments() {
        validateSiteUrl();
        validateFormat();
    }

    /**
     * サイトURLを検証します。
     */
    private void validateSiteUrl() {
        if (StringUtils.isBlank(siteUrl)) {
            throw new CmdLineArgmentException("Site URL is required");
        }
        if (!isValidUrl(siteUrl)) {
            throw new CmdLineArgmentException("Invalid site URL format");
        }
    }

    /**
     * 出力フォーマットを検証します。
     */
    private void validateFormat() {
        if (format == Format.JSON && StringUtils.isBlank(filePath)) {
            throw new CmdLineArgmentException("File path is required for JSON format");
        }
    }

    /**
     * URLの形式を検証します。
     *
     * @param url 検証するURL
     * @return URLが有効な場合はtrue
     */
    private boolean isValidUrl(final String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    /**
     * サイトを追加します。
     *
     * @throws IOException APIコール中にエラーが発生した場合
     */
    private void addSite() throws IOException {
        final Webmasters service = factory.createClient();
        service.sites().add(siteUrl).execute();
        
        // APIはvoidを返すため、成功メッセージを生成して返す
        final Response response = new Response("Successfully added site: " + siteUrl);
        ResponseWriter.writeJson(response, format, filePath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String usage() {
        return "Adds a new site to your Google Search Console account.";
    }

    /**
     * レスポンスメッセージを格納する内部クラス。
     */
    private static class Response {
        /**
         * レスポンスメッセージ。
         */
        private final String message;

        /**
         * デフォルトコンストラクタ。
         * デフォルトのパッケージプライベートアクセス修飾子を使用します。
         *
         * @param message レスポンスメッセージ
         */
        /* package */ Response(final String message) {
            this.message = message;
        }

        /**
         * レスポンスメッセージを文字列として返します。
         *
         * @return レスポンスメッセージ
         */
        @Override
        public String toString() {
            return message;
        }
    }
}
