package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.UrlCrawlErrorsSample;
import com.google.api.services.webmasters.model.UrlCrawlErrorsSamplesListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;
import org.kohsuke.args4j.Option;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;

import java.io.IOException;

/**
 * URLクロールエラーサンプルを一覧表示するコマンド
 */
@Component
public class ListCommand implements Command {

    /** Webmasters APIクライアント生成ファクトリ */
    @Autowired
    private WebmastersFactory factory;
    
    /** 応答出力処理 */
    @Autowired
    private ResponseWriter responseWriter;

    /** サイトURL */
    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    private String siteUrl;

    /** エラーカテゴリ */
    @Option(name = "-category", usage = "Error category", required = true)
    private String category;

    /** プラットフォーム */
    @Option(name = "-platform", usage = "Platform", required = true)
    private String platform;
    
    /**
     * デフォルトコンストラクタ
     */
    public ListCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * URLクロールエラーサンプルを取得し、出力します。
     * 
     * Google APIは具象型を使用しているため、一部の警告は抑制します。
     * インターフェースを使用するよう変更すると、
     * Google APIとの互換性に問題が生じる可能性があります。
     */
    @Override
    @SuppressWarnings({
        "PMD.AvoidUsingImplementationTypes", // Google API具象型のため警告抑制
        "PMD.ConsecutiveAppendsShouldReuse", // String連結方法に関する警告抑制
        "PMD.AvoidConcatenatingNonLiterals",  // 非リテラル値の連結に関する警告抑制
        "PMD.ForeignMethodInvocation"         // 外部オブジェクトのメソッド呼び出しに関する警告抑制
    })
    public void execute() {
        try {
            final Webmasters.Urlcrawlerrorssamples.List request = factory.create().urlcrawlerrorssamples().list(siteUrl, category, platform);
            final UrlCrawlErrorsSamplesListResponse response = request.execute();
            
            final StringBuilder output = new StringBuilder(256);
            for (final UrlCrawlErrorsSample sample : response.getUrlCrawlErrorSample()) {
                output.append("Page URL: ").append(sample.getPageUrl()).append('\n')
                      .append("First Detected: ").append(sample.getFirstDetected()).append('\n')
                      .append("Last Crawled: ").append(sample.getLastCrawled()).append('\n')
                      .append("Response Code: ").append(sample.getResponseCode()).append("\n\n");
            }
        
            responseWriter.writeJson(output.toString(), Format.CONSOLE, null);
        } catch (IOException e) {
            throw new CmdLineIOException("URLクロールエラーサンプルの一覧取得に失敗しました", e);
        }
    }

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明
     */
    @Override
    public String usage() {
        return "指定されたクロールエラーカテゴリとプラットフォームのサンプルURLを一覧表示します。";
    }
}
