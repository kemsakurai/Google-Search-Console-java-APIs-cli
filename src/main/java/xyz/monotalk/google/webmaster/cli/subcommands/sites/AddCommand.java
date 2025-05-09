package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * Google Search Console APIのサイト追加コマンドです。
 */
@Component
public class AddCommand implements Command {

    /** ロガーインスタンス。 */
    private static final Logger LOGGER = LoggerFactory.getLogger(AddCommand.class);

    /** WebmastersファクトリーインスタンスDI用。 */
    @Autowired private WebmastersFactory factory;

    /** サイトURL。 */
    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    private String siteUrl;

    /** 出力フォーマット。 */
    @Option(name = "-format", usage = "Output format [console or json]")
    private Format format = Format.CONSOLE;

    /** デフォルトコンストラクタ。 */
    public AddCommand() {
        // デフォルトコンストラクタ
    }

    /** サイトをGoogle Search Consoleに追加します。 */
    @Override
    public void execute() {
        try {
            if (siteUrl == null || siteUrl.isEmpty()) {
                throw new CmdLineArgmentException("Site URL is required.");
            }

            final Webmasters webmasters = factory.create();
            final Webmasters.Sites sites = webmasters.sites(); // キャッシュ
            if (sites == null) {
                throw new CommandLineInputOutputException("Webmasters API 'sites' service is unavailable.");
            }

            final Webmasters.Sites.Add add = sites.add(siteUrl);
            add.execute();

            // サイトマップ送信ロジックを追加
            final Webmasters.Sitemaps sitemaps = webmasters.sitemaps();
            if (sitemaps == null) {
                throw new CommandLineInputOutputException("Webmasters API 'sitemaps' service is unavailable.");
            }

            final Webmasters.Sitemaps.Submit submit = sitemaps.submit(siteUrl, siteUrl + "/sitemap.xml");
            submit.execute();

            if (format == Format.CONSOLE && LOGGER.isInfoEnabled()) {
                LOGGER.info("Successfully added site and submitted sitemap: {}", siteUrl);
            }
        } catch (CmdLineArgmentException e) {
            throw e; // 再スロー
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to add site or submit sitemap: {}", siteUrl, e);
            }
            throw new CommandLineInputOutputException("Failed to add site or submit sitemap: " + siteUrl, e);
        }
    }

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明
     */
    @Override
    public String usage() {
        return "Adds a site to Google Search Console.";
    }

    /**
     * サイトURLを設定します。
     *
     * @param siteUrl 設定するサイトURL
     */
    public void setSiteUrl(final String siteUrl) {
        this.siteUrl = siteUrl;
    }

    /**
     * 出力フォーマットを設定します。
     *
     * @param format 設定する出力フォーマット
     */
    public void setFormat(final Format format) {
        this.format = format;
    }
}
