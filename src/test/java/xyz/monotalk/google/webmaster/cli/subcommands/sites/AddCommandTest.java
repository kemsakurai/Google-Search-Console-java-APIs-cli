package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * AddCommandのテストクラス。
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

    /**
     * テストのセットアップを行います。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Before
    public void setUp() throws IOException {
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sites()).thenReturn(sites);
        when(sites.add(TEST_SITE_URL)).thenReturn(add);
        command.setSiteUrl(TEST_SITE_URL);
    }

    /**
     * 異常系: API実行時に例外が発生する場合のテスト。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecuteWithApiException() throws IOException {
        // Given
        when(add.execute()).thenThrow(new IOException("API Error"));

        // When
        command.execute();
    }

    /**
     * 正常系: 使用方法の説明文字列が返却されることを検証します。
     */
    @Test
    public void testUsageReturnsExpectedDescription() {
        // Given
        String expected = "Adds a site to Google Search Console.";

        // When
        String actual = command.usage();

        // Then
        assertEquals(expected, actual);
    }
}