package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
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
 * サイトマップ削除コマンドのテストクラス。
 */
@RunWith(MockitoJUnitRunner.class)
public class DeleteCommandTest {

    /**
     * テスト用のサイトURL。
     */
    private static final String TEST_SITE_URL = "https://example.com";

    /**
     * テスト用のフィードパス。
     */
    private static final String TEST_FEED_PATH = "sitemap.xml";

    /**
     * エラーメッセージの定数。
     */
    private static final String MSG_USAGE = "usageメソッドが正しい説明を返していません";
    
    /**
     * サイトマップ削除成功メッセージ。
     */
    private static final String MSG_DELETION = "Sitemap deleted successfully.";

    /**
     * WebmastersFactoryのモックインスタンス。
     */
    @Mock
    private WebmastersFactory factory;

    /**
     * Webmastersのモックインスタンス。
     */
    @Mock
    private Webmasters webmasters;

    /**
     * Webmasters.Sitemapsのモックインスタンス。
     */
    @Mock
    private Webmasters.Sitemaps sitemaps;

    /**
     * Webmasters.Sitemaps.Deleteのモックインスタンス。
     */
    @Mock
    private Webmasters.Sitemaps.Delete delete;

    /**
     * テスト対象のコマンドインスタンス。
     */
    @InjectMocks
    private DeleteCommand command;

    /**
     * テスト前の準備を行います。
     *
     * @throws IOException テストの準備中に発生する可能性のある例外
     */
    @Before
    public void setup() throws IOException {
        when(factory.createClient()).thenReturn(webmasters);
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.delete(anyString(), anyString())).thenReturn(delete);
    }

    /**
     * サイトマップ削除の正常系テスト。
     *
     * @throws IOException APIリクエスト時に発生する可能性のある例外
     */
    @Test
    public void testExecuteSuccess() throws IOException {
        // Given
        command.setSiteUrl(TEST_SITE_URL);
        command.setFeedPath(TEST_FEED_PATH);

        // When
        command.execute();

        // Then
        verify(factory).createClient();
        verify(webmasters).sitemaps();
        verify(sitemaps).delete(TEST_SITE_URL, TEST_FEED_PATH);
        verify(delete).execute();
    }

    /**
     * API呼び出しで例外が発生した場合のテスト。
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecuteErrorApiCallException() {
        // Given
        command.setSiteUrl(TEST_SITE_URL);
        command.setFeedPath(TEST_FEED_PATH);
        try {
            when(delete.execute()).thenThrow(new IOException("API Error"));
        } catch (IOException e) {
            fail("モックの設定中に予期しない例外が発生しました: " + e.getMessage());
        }

        // When
        command.execute();
    }

    /**
     * usageメソッドのテスト。
     */
    @Test
    public void testUsageReturnsCorrectDescription() {
        // When
        final String usage = command.usage();

        // Then
        assertEquals(MSG_USAGE, "Deletes a sitemap from this site.", usage);
    }

    /**
     * サイトマップ削除の成功検証テスト。
     *
     * @throws IOException APIリクエスト時に発生する可能性のある例外
     */
    @Test
    public void testExecuteDeletesSitemapSuccessfully() throws IOException {
        // Given
        command.setSiteUrl(TEST_SITE_URL);

        // When
        command.execute();

        // Then
        assertEquals("サイトマップ削除の結果が期待値と一致しません", MSG_DELETION, MSG_DELETION);
    }
}