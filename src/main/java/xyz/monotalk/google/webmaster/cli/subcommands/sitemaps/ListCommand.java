package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitemapsListResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
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
 * サイトマップ一覧を取得するコマンドクラス。
 */
@Component
public class ListCommand implements Command {

    /** ロガーインスタンス。 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ListCommand.class);

    /** WebmastersFactoryインスタンス。 */
    private final WebmastersFactory factory;

    /** サイトURL。 */
    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    private String siteUrl;

    /** 出力フォーマット。 */
    @Option(name = "-format", usage = "Output format")
    private Format format = Format.CONSOLE;

    /** 出力ファイルパス。 */
    @Option(name = "-filePath", usage = "Output file path")
    private String filePath;

    /**
     * コンストラクタ。
     *
     * @param factory WebmastersFactoryインスタンス
     */
    public ListCommand(final WebmastersFactory factory) {
        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Retrieving sitemap list for site: {}", siteUrl);
        }

        validateSiteUrl();
        validateFormat();

        try {
            final Webmasters webmasters = factory.createClient();
            final Webmasters.Sitemaps.List list = webmasters.sitemaps().list(siteUrl);
            final SitemapsListResponse response = list.execute();
            ResponseWriter.writeJson(response, format, filePath);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Sitemap list retrieved successfully");
            }
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to list sitemaps", e);
            }
            throw new CommandLineInputOutputException("Failed to list sitemaps", e);
        }
    }

    /**
     * サイトURLの妥当性を検証します。
     */
    private void validateSiteUrl() {
        if (StringUtils.isBlank(siteUrl)) {
            throw new CmdLineArgmentException("Site URL must be specified");
        }
    }

    /**
     * 出力フォーマットの妥当性を検証します。
     */
    private void validateFormat() {
        if (format == Format.JSON && StringUtils.isBlank(filePath)) {
            throw new CmdLineArgmentException("File path must be specified when using JSON format");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String usage() {
        return "Lists the sitemaps for a given site URL.";
    }
}
