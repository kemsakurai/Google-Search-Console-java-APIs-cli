package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.UrlCrawlErrorsSample;
import com.google.api.services.webmasters.model.UrlCrawlErrorsSamplesListResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ListCommandTest {

    @Mock
    private WebmastersFactory factory;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Urlcrawlerrorssamples urlcrawlerrorssamples;

    @Mock
    private Webmasters.Urlcrawlerrorssamples.List request;

    @InjectMocks
    private ListCommand command;

    @Before
    public void setUp() throws IOException {
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.urlcrawlerrorssamples()).thenReturn(urlcrawlerrorssamples);
        when(urlcrawlerrorssamples.list(anyString(), anyString(), anyString())).thenReturn(request);
    }

    @Test
    public void testExecute_Success() throws IOException {
        // Given
        UrlCrawlErrorsSamplesListResponse response = new UrlCrawlErrorsSamplesListResponse();
        UrlCrawlErrorsSample sample = new UrlCrawlErrorsSample();
        sample.setPageUrl("https://example.com/error");
        response.setUrlCrawlErrorSample(Arrays.asList(sample));
        
        when(request.execute()).thenReturn(response);

        // When
        command.execute();

        // Then
        verify(factory).create();
        verify(webmasters).urlcrawlerrorssamples();
        verify(urlcrawlerrorssamples).list(eq("https://www.monotalk.xyz"), eq("notFound"), eq("web"));
        verify(request).execute();
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_APIError() throws IOException {
        // Given
        when(request.execute()).thenThrow(new IOException("API Error"));

        // When
        command.execute();

        // Then - should throw RuntimeException
    }

    @Test
    public void testUsage() {
        // When
        String usage = command.usage();

        // Then
        assert usage != null && !usage.isEmpty() : "Usage string should not be empty";
    }
}