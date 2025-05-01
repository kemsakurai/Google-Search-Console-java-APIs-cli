package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitemapsListResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.kohsuke.args4j.CmdLineException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListCommandTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private WebmastersFactory factory;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Sitemaps sitemaps;

    @Mock
    private Webmasters.Sitemaps.List list;

    @Mock
    private SitemapsListResponse response;

    @InjectMocks
    private ListCommand command;

    @Before
    public void setup() throws IOException {
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.list(anyString())).thenReturn(list);
        when(list.execute()).thenReturn(response);
    }

    @Test
    public void testExecute_正常系_コンソール出力() throws Exception {
        // Given
        command.siteUrl = new URL("https://example.com");
        command.format = Format.CONSOLE;

        // When
        command.execute();

        // Then
        verify(factory).create();
        verify(webmasters).sitemaps();
        verify(sitemaps).list("https://example.com");
        verify(list).execute();
    }

    @Test
    public void testExecute_正常系_JSONファイル出力() throws Exception {
        // Given
        File tempFile = temporaryFolder.newFile("output.json");
        command.siteUrl = new URL("https://example.com");
        command.format = Format.JSON;
        command.filePath = tempFile.getAbsolutePath();

        // When
        command.execute();

        // Then
        verify(factory).create();
        verify(webmasters).sitemaps();
        verify(sitemaps).list("https://example.com");
        verify(list).execute();
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_異常系_API呼び出しで例外が発生() throws Exception {
        // Given
        command.siteUrl = new URL("https://example.com");
        when(list.execute()).thenThrow(new IOException("API Error"));

        // When
        command.execute();
    }

    @Test(expected = CmdLineException.class)
    public void testExecute_異常系_ファイルパスがJSONフォーマット時に未指定() throws Exception {
        // Given
        command.siteUrl = new URL("https://example.com");
        command.format = Format.JSON;
        command.filePath = null;

        // When
        command.execute();
    }

    @Test
    public void testUsage_正常系_説明文字列が返却される() {
        // When
        String usage = command.usage();

        // Then
        assertEquals("Lists the sitemaps-entries submitted for this site, or included in the sitemap index file (if sitemapIndex is specified in the request).", usage);
    }
}