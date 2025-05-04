package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

/**
 * サイト一覧を取得するコマンド
 */
@Component
public class ListCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Autowired
    private ResponseWriter responseWriter;

    /**
     * サイト一覧を取得し、指定された形式で出力します。
     *
     * @throws CmdLineIOException API実行エラーが発生した場合
     */
    @Override
    public void execute() throws CmdLineIOException {
        try {
            Webmasters webmasters = factory.create();
            Webmasters.Sites.List list = webmasters.sites().list();
            SitesListResponse response = list.execute();
            responseWriter.writeJson(response, Format.CONSOLE, null);
        } catch (IOException e) {
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
