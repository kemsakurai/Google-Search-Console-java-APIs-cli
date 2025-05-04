package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

/**
 * サイト一覧を取得するコマンドクラス
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
     * デフォルトコンストラクタ
     */
    public ListCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * サイト一覧を取得し、指定された形式で出力します。
     *
     * @throws CmdLineIOException API実行エラーが発生した場合
     */
    @Override
    public void execute() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Retrieving site list");
        }
        
        try {
            final Webmasters webmasters = factory.create();
            final Webmasters.Sites.List list = webmasters.sites().list();
            final SitesListResponse response = list.execute();
            ResponseWriter.writeJson(response, Format.CONSOLE, null);
            
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Site list retrieved successfully");
            }
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to list sites", e);
            }
            throw new CmdLineIOException("Failed to list sites", e);
        }
    }

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明
     */
    @Override
    public String usage() {
        return "Lists the user's Search Console sites.";
    }
}
