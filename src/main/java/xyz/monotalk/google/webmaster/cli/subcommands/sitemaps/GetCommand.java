package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.WmxSitemap;
import java.io.IOException;
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
 * GetCommandクラス - サイトマップ取得コマンド。
 *
 * <p>このクラスは、Google Search Console APIを使用して、
 * 指定されたサイトのサイトマップを取得します。</p>
 */
@Component
public class GetCommand implements Command {

    /**
     * WebmastersファクトリーインスタンスDI用。
     */
    @Autowired
    protected WebmastersFactory factory;

    /**
     * サイトURLを設定します。
     *
     * @param siteUrl サイトのURL。
     */
    @Option(name = "-siteUrl", usage = "Site URL", metaVar = "<siteUrl>", required = true)
    protected String siteUrl;

    /**
     * サイトマップのフィードパスを設定します。
     *
     * @param feedpath サイトマップのフィードパス。
     */
    @Option(name = "-feedpath", usage = "Feed path", required = true)
    protected String feedpath;

    /**
     * 出力フォーマット。
     */
    @Option(name = "-format", usage = "Output format", metaVar = "[console or json]")
    protected Format format = Format.CONSOLE;

    /**
     * JSONファイルパス。
     */
    @Option(name = "-filePath", usage = "JSON file path", metaVar = "<filename>", depends = {"-format"})
    protected String filePath;

    /**
     * デフォルトコンストラクタ。
     */
    public GetCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * サイトマップ情報を取得します。
     *
     * @throws CommandLineInputOutputException API実行エラーが発生した場合。
     */
    @Override
    public void execute() {
        validateArguments();
        try {
            final Webmasters webmasters = getWebmastersClient();
            final WmxSitemap response = fetchSitemap(webmasters);
            ResponseWriter.writeJson(response, format, filePath);
        } catch (IOException e) {
            throw new CommandLineInputOutputException("API Error", e);
        }
    }
    
    /**
     * 引数を検証します。
     *
     * @throws CmdLineArgmentException 引数が無効な場合。
     */
    private void validateArguments() {
        if (siteUrl == null || siteUrl.isEmpty()) {
            throw new CmdLineArgmentException("Site URL must be specified");
        }
        if (feedpath == null || feedpath.isEmpty()) {
            throw new CmdLineArgmentException("Feed path must be specified");
        }
    }
    
    /**
     * Webmastersクライアントを取得します。
     *
     * @return Webmastersクライアント
     * @throws CommandLineInputOutputException クライアント作成に失敗した場合。
     */
    private Webmasters getWebmastersClient() {
        final Webmasters webmasters = factory.createClient();
        if (webmasters == null) {
            throw new CommandLineInputOutputException(new IOException("Failed to create Webmasters client"));
        }
        return webmasters;
    }
    
    /**
     * サイトマップを取得します。
     *
     * @param webmasters Webmastersクライアント
     * @return 取得したサイトマップ情報
     * @throws IOException API呼び出しに失敗した場合。
     */
    private WmxSitemap fetchSitemap(final Webmasters webmasters) throws IOException {
        final Webmasters.Sitemaps.Get request = webmasters.sitemaps().get(siteUrl, feedpath);
        return request.execute();
    }

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明。
     */
    @Override
    public String usage() {
        return "Gets information about a specific sitemap";
    }
}
