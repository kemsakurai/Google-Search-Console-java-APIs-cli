package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorscounts;

import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * URLクロールエラー数を取得するコマンド
 * 
 * 注: Google Search Console API変更により現在このAPIは利用できません
 */
@Component
public class QueryCommand implements Command {

    /**
     * ロガーインスタンス
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryCommand.class);

    /**
     * Webmasters APIクライアント生成ファクトリ
     */
    @Autowired
    private WebmastersFactory factory;

    /**
     * サイトURL。
     */
    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    private String siteUrl;

    /**
     * 出力フォーマット。
     */
    @Option(name = "-format", usage = "Output format", required = false)
    private Format format = Format.CONSOLE;

    /**
     * 出力ファイルパス。
     */
    @Option(name = "-filePath", usage = "Output file path", required = false)
    private String filePath;
    
    /**
     * デフォルトコンストラクタ。
     */
    public QueryCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * URLクロールエラー数の取得を試みますが、現在のAPI互換性の問題により使用できないことを通知します。
     * 
     * Google Search Console API変更により、このAPIは利用できなくなりました。
     */
    @Override
    public void execute() {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("URLクロールエラー数 API は現在利用できません (非対応: サイト={})", siteUrl);
        }
        
        final StringBuilder output = new StringBuilder(256);
        output.append("URLクロールエラー数 API は現在利用できません。\n\n")
              .append("Google Search Console API の変更により、このAPIは廃止されました。\n")
              .append("Google Search Console ウェブインターフェースをご利用ください。\n")
              .append("https://search.google.com/search-console");

        try {
            ResponseWriter.writeJson(output.toString(), format, filePath);
            
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("URLクロールエラー数API非対応メッセージを出力しました");
            }
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("レスポンス出力中にエラーが発生しました", e);
            }
            throw new CommandLineInputOutputException("レスポンス出力中にエラーが発生しました", e);
        }
    }

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明。
     */
    @Override
    public String usage() {
        return "※非推奨※ URLクロールエラー数をカテゴリとプラットフォーム別に時系列で取得します。";
    }
}
