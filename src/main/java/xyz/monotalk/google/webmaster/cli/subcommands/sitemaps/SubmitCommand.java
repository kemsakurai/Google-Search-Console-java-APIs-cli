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

    private static final Logger LOGGER = LoggerFactory.getLogger(SubmitCommand.class);

    @Autowired
    private WebmastersFactory factory;

    @Option(
            name = "-siteUrl",
            usage = "Site URL",
            metaVar = "<siteUrl>",
            required = true,
            handler = URLOptionHandler.class)
    protected URL siteUrl;

    @Option(name = "-feedpath", usage = "Feed path", required = true)
    protected String feedpath;

    public SubmitCommand() {
        // デフォルトコンストラクタ
    }

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

    private void validateParameters() {
        if (siteUrl == null) {
            throw new CmdLineArgmentException("Site URL is required");
        }
        if (feedpath == null) {
            throw new CmdLineArgmentException("Feed path is required");
        }
    }

    private void logSubmissionStart() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Submitting sitemap {} for site {}", feedpath, siteUrl);
        }
    }

    private Webmasters createWebmastersClient() {
        final Webmasters webmasters = factory.create();
        if (webmasters == null) {
            throw new CommandLineInputOutputException(new IOException("Failed to create Webmasters client"));
        }
        return webmasters;
    }

    private void submitSitemap(final Webmasters webmasters) throws IOException {
        final Webmasters.Sitemaps.Submit request = webmasters.sitemaps().submit(siteUrl.toString(), feedpath);
        request.execute();
    }

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
