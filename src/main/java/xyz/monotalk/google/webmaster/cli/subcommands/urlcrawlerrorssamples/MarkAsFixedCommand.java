/**
 * クロールエラーサンプルURLを修正済みとしてマークするためのコマンド実装です。
 * このコマンドは、Google Search Consoleで特定のURLを修正済みとしてマークし、
 * クロールエラーサンプルのリストから削除します。
 * 
 * @author Ken Sakurai
 */

package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;


/**
 * URLクロールエラーサンプルを修正済みとしてマークするコマンド
 */
@Component
public class MarkAsFixedCommand implements Command {

    /** Webmasters APIクライアント生成ファクトリ */
    @Autowired
    private WebmastersFactory factory;

    /** 修正済みとしてマークするURL */
    @Option(name = "-url", usage = "URL to mark as fixed", required = true)
    private String url;

    /** プラットフォーム (web, mobile, smartphoneOnly) */
    @Option(name = "-platform", usage = "Platform (web, mobile, smartphoneOnly)", required = true)
    private static final String PLATFORM = "web";

    /** エラーカテゴリ */
    @Option(name = "-category", usage = "Error category", required = true)
    private static final String CATEGORY = "notFound";

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

    @Override
    public void execute() throws CmdLineIOException, CmdLineArgmentException {
        if (url == null) {
            throw new CmdLineArgmentException("URL must be specified");
        }

        try {
            final Webmasters.Urlcrawlerrorssamples errorSamples = factory.create()
                .urlcrawlerrorssamples();
            errorSamples.markAsFixed("https://www.monotalk.xyz", url, CATEGORY, PLATFORM)
                .execute();
        } catch (IOException e) {
            throw new CmdLineIOException(e.getMessage(), e);
        }
    }

    @Override
    public String usage() {
        return "指定されたサイトのサンプルURLを修正済みとしてマークし、サンプルリストから削除します。";
    }
}
