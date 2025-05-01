package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.net.URL;

@RunWith(MockitoJUnitRunner.class)
public class GetCommandTest {

    @Mock
    private WebmastersFactory factory;

    private GetCommand command;

    @Before
    public void setUp() throws Exception {
        command = new GetCommand();
        command.factory = factory;
        command.siteUrl = new URL("https://example.com");
        command.feedpath = "sitemap.xml";
    }

    @Test(expected = CmdLineArgmentException.class)
    public void testExecute_サイトURL未指定でエラー() throws Exception {
        command.siteUrl = null;
        command.execute();
    }

    @Test(expected = CmdLineArgmentException.class)
    public void testExecute_フィードパス未指定でエラー() throws Exception {
        command.feedpath = null;
        command.execute();
    }
}