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
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * DeleteCommandのテストクラス。
 */
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

    /**
     * テスト前の準備を行います。
     *
     * @throws IOException 入出力例外が発生した場合
     */
    @Before
    public void setup() throws IOException {
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.delete(anyString(), anyString())).thenReturn(delete);
    }

    /**
     * 正常系: サイトマップが削除されるケースをテストします。
     *
     * @throws CommandLineInputOutputException コマンドライン入出力例外が発生した場合
     * @throws IOException 入出力例外が発生した場合
     */
    @Test
    public void testExecute正常系サイトマップ削除成功() throws CommandLineInputOutputException, IOException {
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

    /**
     * 異常系: API呼び出しで例外が発生するケースをテストします。
     *
     * @throws IOException 入出力例外が発生した場合
     * @throws CommandLineInputOutputException コマンドライン入出力例外が発生した場合
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute異常系Api呼び出し例外発生() throws IOException, CommandLineInputOutputException {
        // Given
        command.setSiteUrl("https://example.com");
        command.setFeedPath("sitemap.xml");
        when(delete.execute()).thenThrow(new IOException("API Error"));

        // When
        command.execute();
    }

    /**
     * usageメソッドの説明文字列が正しく返却されることをテストします。
     */
    @Test
    public void testUsage正常系説明文字列返却() {
        // When
        String usage = command.usage();

        // Then
        assertEquals("Deletes a sitemap from this site.", usage);
    }
}