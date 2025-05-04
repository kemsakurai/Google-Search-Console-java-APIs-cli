package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * サイトの一覧を取得するコマンドです。
 */

@Component
public class ListCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Autowired
    protected ResponseWriter responseWriter;

    /**
     * Search Console APIからサイト一覧を取得します。
     * 
     * @throws CommandLineInputOutputException 入出力操作中にエラーが発生した場合。
     * @throws CmdLineArgmentException 引数の検証中にエラーが発生した場合。
     * @throws CmdLineIOException API要求の実行中にエラーが発生した場合。
     */
    @Override
    public void execute() throws CommandLineInputOutputException, CmdLineArgmentException, CmdLineIOException {
        Webmasters webmasters = factory.create();
        try {
            SitesListResponse response = webmasters.sites().list().execute();
            responseWriter.writeJson(response, Format.CONSOLE, null);
        } catch (IOException e) {
            throw new CmdLineIOException("Failed to execute API request: " + e.getMessage(), e);
        }
    }

    @Override
    public String usage() {
        return "Lists the user's Search Console sites.";
    }
}
