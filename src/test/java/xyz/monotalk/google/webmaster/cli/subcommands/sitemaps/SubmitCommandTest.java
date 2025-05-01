package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubmitCommandTest {

    @Mock
    private WebmastersFactory factory;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Sitemaps sitemaps;

    @Mock
    private Webmasters.Sitemaps.Submit submitRequest;

    @InjectMocks
    private SubmitCommand command;

    @Before
    public void setUp() throws Exception {
        command = new SubmitCommand();
        command.siteUrl = new URL("http://example.com");
        command.feedpath = "sitemap.xml";
        ReflectionTestUtils.setField(command, "factory", factory);

        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.submit(anyString(), anyString())).thenReturn(submitRequest);
    }

    @Test
    public void testExecute_正常系() throws Exception {
        doNothing().when(submitRequest).execute();
        command.execute();
        verify(submitRequest).execute();
    }

    @Test(expected = CmdLineArgmentException.class)
    public void testExecute_異常系_siteUrlがnull() throws Exception {
        command.siteUrl = null;
        command.execute();
    }

    @Test(expected = CmdLineArgmentException.class)
    public void testExecute_異常系_feedpathがnull() throws Exception {
        command.feedpath = null;
        command.execute();
    }

    @Test(expected = CmdLineIOException.class)
    public void testExecute_異常系_API呼び出しでIOException() throws Exception {
        when(submitRequest.execute()).thenThrow(new IOException("API Error"));
        command.execute();
    }

    @Test
    public void testUsage_正常系() {
        assertEquals("Submits a sitemap for this site.", command.usage());
    }
}