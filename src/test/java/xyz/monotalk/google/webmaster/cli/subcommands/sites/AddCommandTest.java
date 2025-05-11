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

    /**
     * WebmastersFactoryのモック。
     */
    @Mock
    private WebmastersFactory factory;

    /**
     * Webmastersクライアントのモック。
     */
    @Mock
    private Webmasters webmasters;

    /**
     * Webmasters.Sitesのモック。
     */
    @Mock
    private Webmasters.Sites sites;

    /**
     * Webmasters.Sites.Addのモック。
     */
    @Mock
    private Webmasters.Sites.Add add;

    /**
     * AddCommandのインスタンス。
     */
    @InjectMocks
    private AddCommand command;

    /**
     * テスト用のサイトURL。
     */
    private static final String TEST_SITE_URL = "https://example.com";

    /**
     * デフォルトコンストラクタ。
     */
    public AddCommandTest() {
        // デフォルトの初期化処理
    }

    /**
     * テストのセットアップを行います。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Before
    public void setUp() throws IOException {
        when(factory.createClient()).thenReturn(webmasters);
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
        final String expected = "Adds a site to Google Search Console.";

        // When
        final String actual = command.usage();

        // Then
        assertEquals("使用方法の説明が正しくありません", expected, actual);
    }
}