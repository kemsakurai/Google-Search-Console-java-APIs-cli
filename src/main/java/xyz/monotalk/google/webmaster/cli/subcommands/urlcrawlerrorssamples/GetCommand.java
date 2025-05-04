package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * GetCommand
 */
@Component
public class GetCommand implements Command {

    /** Webmasters APIクライアント生成ファクトリ */
    @Autowired
    private WebmastersFactory factory;
    
    /**
     * デフォルトコンストラクタ
     */
    public GetCommand() {
        // デフォルトコンストラクタ
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("TODO not implemented..");
    }

    @Override
    public String usage() {
        return "Retrieves details about crawl errors for a site's sample URL.";
    }
}
