package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;
import java.net.URL;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GetCommandTest {

    @Mock
    private WebmastersFactory factory;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Sitemaps sitemaps;

    @Mock
    private Webmasters.Sitemaps.Get getRequest;

    private GetCommand command;

    @Before
    public void setUp() throws Exception {
        command = new GetCommand();
        command.factory = factory;
        command.siteUrl = new URL("https://example.com");
        command.feedpath = "sitemap.xml";

        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.get(anyString(), anyString())).thenReturn(getRequest);
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

    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute_API呼び出しでIOException() throws Exception {
        when(getRequest.execute()).thenThrow(new IOException("API Error"));
        command.execute();
    }
}