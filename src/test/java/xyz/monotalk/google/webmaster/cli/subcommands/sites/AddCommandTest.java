package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * AddCommandのテストクラス
 */
@RunWith(MockitoJUnitRunner.class)
public class AddCommandTest {

    @Mock
    private WebmastersFactory factory;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Sites sites;

    @Mock
    private Webmasters.Sites.Add add;

    @InjectMocks
    private AddCommand command;

    private static final String TEST_SITE_URL = "https://example.com";

    @Before
    public void setUp() throws IOException {
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sites()).thenReturn(sites);
        when(sites.add(TEST_SITE_URL)).thenReturn(add);
        command.setSiteUrl(TEST_SITE_URL);
    }

    @Test
    public void testExecute_正常系_サイトが追加される() throws IOException {
        // When
        command.execute();

        // Then
        verify(factory).create();
        verify(webmasters).sites();
        verify(sites).add(TEST_SITE_URL);
        verify(add).execute();
    }

    @Test(expected = CmdLineIOException.class)
    public void testExecute_異常系_API実行時に例外発生() throws IOException {
        // Given
        when(add.execute()).thenThrow(new IOException("API Error"));

        // When
        command.execute();
    }

    @Test
    public void testExecute_正常系_JSONフォーマットで出力() throws IOException {
        // Given
        command.setFormat(Format.JSON);

        // When
        command.execute();

        // Then
        verify(factory).create();
        verify(webmasters).sites();
        verify(sites).add(TEST_SITE_URL);
        verify(add).execute();
    }

    @Test
    public void testUsage_正常系_説明文字列が返却される() {
        // Given
        String expected = "Adds a site to Google Search Console.";

        // When
        String actual = command.usage();

        // Then
        assertEquals(expected, actual);
    }
}