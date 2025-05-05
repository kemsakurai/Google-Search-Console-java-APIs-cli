/**
 * クロールエラーサンプルURLを修正済みとしてマークするためのコマンド実装です。
 * このコマンドは、Google Search Consoleで特定のURLを修正済みとしてマークし、
 * クロールエラーサンプルのリストから削除します。
 * 
 * 注: Google Search Console API変更により現在このAPIは利用できません
 * 
 * @author Ken Sakurai
 */

package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;


/**
 * URLクロールエラーサンプルを修正済みとしてマークするコマンド
 * 
 * 注: Google Search Console API変更により現在このAPIは利用できません
 */
@Component
public class MarkAsFixedCommand implements Command {

    /** ロガーインスタンス */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkAsFixedCommand.class);

    /** Webmasters APIクライアント生成ファクトリ */
    @Autowired
    private WebmastersFactory factory;

    /** 修正済みとしてマークするURL */
    @Option(name = "-url", usage = "URL to mark as fixed", required = true)
    private String url;

    /** プラットフォーム (web, mobile, smartphoneOnly) */
    @Option(name = "-platform", usage = "Platform (web, mobile, smartphoneOnly)", required = true)
    private String platform = "web";

    /** エラーカテゴリ */
    @Option(name = "-category", usage = "Error category", required = true)
    private String category = "notFound";

    /**
     * デフォルトコンストラクタ
     */
    public MarkAsFixedCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * URLを設定します
     * 
     * @param url 設定するURL
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * URLクロールエラーサンプルを修正済みとマークする処理を試みますが、現在のAPI互換性の問題により使用できないことを通知します。
     * 
     * Google Search Console API変更により、このAPIは利用できなくなりました。
     */
    @Override
    public void execute() {
        if (url == null) {
            throw new CmdLineArgmentException("URL must be specified");
        }

        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("URLクロールエラーサンプル 修正済みマークAPI は現在利用できません (非対応: URL={}, カテゴリ={}, プラットフォーム={})", 
                    url, category, platform);
        }
        
        final StringBuilder output = new StringBuilder(256);
        output.append("URLクロールエラーサンプル 修正済みマークAPI は現在利用できません。\n\n")
              .append("Google Search Console API の変更により、このAPIは廃止されました。\n")
              .append("Google Search Console ウェブインターフェースをご利用ください。\n")
              .append("https://search.google.com/search-console");

        try {
            ResponseWriter.writeJson(output.toString(), Format.CONSOLE, null);
        } catch (Exception e) {
            throw new CommandLineInputOutputException("レスポンス出力中にエラーが発生しました", e);
        }
    }

    /**
     * コマンドの使用方法を返します。
     * 
     * @return 使用方法の説明
     */
    @Override
    public String usage() {
        return "※非推奨※ 指定されたサイトのサンプルURLを修正済みとしてマークし、サンプルリストから削除します。";
    }
}
