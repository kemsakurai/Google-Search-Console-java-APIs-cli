package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * URLクロールエラーのサンプルをリストするコマンドです。
 * 注: Google Search Console API変更により現在このAPIは利用できません
 * 
 */
@Component
public class ListCommand implements Command {

    /** ロガーインスタンス。 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ListCommand.class);

    /** Webmasters APIクライアント生成ファクトリ。 */
    @Autowired private WebmastersFactory factory;

    /** サイトURL。 */
    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    private String siteUrl;

    /** エラーカテゴリ。 */
    @Option(name = "-category", usage = "Error category", required = true)
    private String category;

    /** プラットフォーム。 */
    @Option(name = "-platform", usage = "Platform", required = true)
    private String platform;

    /** デフォルトコンストラクタ。 */
    public ListCommand() {
        // デフォルトコンストラクタ
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
     * エラーカテゴリを設定します。
     *
     * @param category 設定するエラーカテゴリ
     */
    public void setCategory(final String category) {
        this.category = category;
    }

    /**
     * プラットフォームを設定します。
     *
     * @param platform 設定するプラットフォーム
     */
    public void setPlatform(final String platform) {
        this.platform = platform;
    }

    /**
     * URLクロールエラーサンプルの取得を試みますが、現在のAPI互換性の問題により使用できないことを通知します。
     * Google Search Console API変更により、このAPIは利用できなくなりました。
     * 追加の情報を提供するために、エラーメッセージを改善しました。
     * 
     */
    @Override
    public void execute() {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(
                    "URLクロールエラーサンプル API は現在利用できません (非対応: サイト={}, カテゴリ={}, プラットフォーム={})",
                    siteUrl,
                    category,
                    platform);
        }

        final StringBuilder output = new StringBuilder(256);
        output.append("URLクロールエラーサンプル API は現在利用できません。\n\n")
                .append("Google Search Console API の変更により、このAPIは廃止されました。\n")
                .append("Google Search Console ウェブインターフェースをご利用ください。\n")
                .append("https://search.google.com/search-console");

        ResponseWriter.writeJson(output.toString(), Format.CONSOLE, null);
    }

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明
     */
    @Override
    public String usage() {
        return "※非推奨※ 指定されたクロールエラーカテゴリとプラットフォームのサンプルURLを一覧表示します。";
    }
}
