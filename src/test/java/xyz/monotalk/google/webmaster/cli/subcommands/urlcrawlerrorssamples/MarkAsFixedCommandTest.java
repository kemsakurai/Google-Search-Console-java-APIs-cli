package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import com.google.api.services.webmasters.Webmasters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MarkAsFixedCommandTest {

    @Mock
    private WebmastersFactory factory;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Urlcrawlerrorssamples urlcrawlerrorssamples;

    @Mock
    private Webmasters.Urlcrawlerrorssamples.MarkAsFixed markAsFixed;

    @InjectMocks
    private MarkAsFixedCommand command;

    @Before
    public void setUp() throws Exception {
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.urlcrawlerrorssamples()).thenReturn(urlcrawlerrorssamples);
        when(urlcrawlerrorssamples.markAsFixed(anyString(), anyString(), anyString(), anyString())).thenReturn(markAsFixed);
        doNothing().when(markAsFixed).execute();
    }

    @Test
    public void testExecute_Success() throws Exception {
        // Given
        command.setUrl("https://example.com/404");

        // When
        command.execute();

        // Then
        verify(factory).create();
        verify(webmasters).urlcrawlerrorssamples();
        verify(urlcrawlerrorssamples).markAsFixed("https://www.monotalk.xyz", "https://example.com/404", "notFound", "web");
        verify(markAsFixed).execute();
    }

    @Test(expected = CmdLineArgmentException.class)
    public void testExecute_URLNull() throws Exception {
        command.setUrl(null);
        command.execute();
    }

    @Test(expected = CmdLineIOException.class)
    public void testExecute_APIError() throws Exception {
        command.setUrl("https://example.com/404");
        doThrow(new IOException("API Error")).when(markAsFixed).execute();
        command.execute();
    }

    @Test
    public void testUsage() {
        assertEquals("指定されたサイトのサンプルURLを修正済みとしてマークし、サンプルリストから削除します。", command.usage());
    }
}