package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitemapsListResponse;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * ListCommandクラス - サイトマップ一覧を取得するコマンド
 */
@Component
public class ListCommand implements Command {

    /**
     * ロガーインスタンス
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ListCommand.class);

    /**
     * WebmastersファクトリーインスタンスDI用
     */
    @Autowired
    private WebmastersFactory factory;

    /**
     * サイトURL
     */
    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    protected String siteUrl;

    /**
     * 出力フォーマット
     */
    @Option(name = "-format", usage = "Output format", required = false)
    protected Format format = Format.CONSOLE;

    /**
     * 出力ファイルパス
     */
    @Option(name = "-filePath", usage = "Output file path", required = false)
    protected String filePath;

    /**
     * デフォルトコンストラクタ
     */
    public ListCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * サイトマップ一覧を取得し、指定された形式で出力します。
     *
     * @throws CmdLineIOException API実行エラーが発生した場合
     */
    @Override
    public void execute() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Starting site {} sitemap list command.", siteUrl);
        }
        try {
            final Webmasters webmasters = factory.create();
            final Webmasters.Sitemaps.List list = webmasters.sitemaps().list(siteUrl);
            final SitemapsListResponse response = list.execute();
            
            // コンソール出力の場合、空の文字列をfilePathとして渡す
            final String outputPath = (format == Format.CONSOLE && (filePath == null || filePath.isEmpty())) 
                ? "" : filePath;
                
            ResponseWriter.writeJson(response, format, outputPath);
            
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Command completed successfully.");
            }
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("API execution failed", e);
            }
            throw new CmdLineIOException("API execution failed: " + e.getMessage(), e);
        }
    }

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明
     */
    @Override
    public String usage() {
        return "Lists the sitemaps for a given site URL.";
    }
}
