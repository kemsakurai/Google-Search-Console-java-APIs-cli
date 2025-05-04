package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCommandTest {

    @Mock
    private WebmastersFactory factory;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Sitemaps sitemaps;

    @Mock
    private Webmasters.Sitemaps.Delete delete;

    @InjectMocks
    private DeleteCommand command;

    @Before
    public void setup() throws IOException {
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.delete(anyString(), anyString())).thenReturn(delete);
    }

    @Test
    public void testExecute_正常系_サイトマップが削除される() throws CommandLineInputOutputException, IOException {
        // Given
        command.setSiteUrl("https://example.com");
        command.setFeedPath("sitemap.xml");

        // When
        command.execute();

        // Then
        verify(factory).create();
        verify(webmasters).sitemaps();
        verify(sitemaps).delete("https://example.com", "sitemap.xml");
        verify(delete).execute();
    }

    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute_異常系_API呼び出しで例外が発生() throws IOException, CommandLineInputOutputException {
        // Given
        command.setSiteUrl("https://example.com");
        command.setFeedPath("sitemap.xml");
        when(delete.execute()).thenThrow(new IOException("API Error"));

        // When
        command.execute();
    }

    @Test
    public void testUsage_正常系_説明文字列が返却される() {
        // When
        String usage = command.usage();

        // Then
        assertEquals("Deletes a sitemap from this site.", usage);
    }
}