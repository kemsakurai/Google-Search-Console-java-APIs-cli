package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import java.io.IOException;
import java.net.URL;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.URLOptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.WmxSitemap;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * GetCommandクラス - 特定のサイトマップ情報を取得するコマンド
 */
@Component
public class GetCommand implements Command {

    /**
     * WebmastersファクトリーインスタンスDI用
     */
    @Autowired
    protected WebmastersFactory factory;

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
     * 出力フォーマット
     */
    @Option(name = "-format", usage = "Output format", metaVar = "[console or json]")
    protected Format format = Format.CONSOLE;

    /**
     * JSONファイルパス
     */
    @Option(name = "-filePath", usage = "JSON file path", metaVar = "<filename>", depends = {"-format"})
    protected String filePath;

    /**
     * デフォルトコンストラクタ
     */
    public GetCommand() {
        // デフォルトコンストラクタ
    }

    @Override
    public void execute() throws CmdLineException {
        if (siteUrl == null) {
            throw new CmdLineArgmentException("Site URL is required");
        }
        if (feedpath == null) {
            throw new CmdLineArgmentException("Feed path is required");
        }

        try {
            final Webmasters webmasters = factory.create();
            if (webmasters == null) {
                throw new CmdLineIOException(new IOException("Failed to create Webmasters client"));
            }
            
            final Webmasters.Sitemaps.Get request = webmasters.sitemaps().get(siteUrl.toString(), feedpath);
            final WmxSitemap response = request.execute();
            ResponseWriter.writeJson(response, format, filePath);
        } catch (IOException e) {
            throw new CmdLineIOException(e);
        }
    }

    @Override
    public String usage() {
        return "Gets information about a specific sitemap.";
    }
}
