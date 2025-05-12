package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
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
     * テスト対象のAddCommand。
     */
    private AddCommand command;

    /**
     * テスト用のサイトURL。
     */
    private static final String TEST_SITE_URL = "https://example.com";

    /**
     * テストのセットアップを行います。
     *
     * @throws IOException 入出力例外が発生した場合。
     * @throws GeneralSecurityException セキュリティ関連の例外が発生した場合。
     * @throws CmdLineException コマンドライン引数の解析に失敗した場合。
     */
    @Before
    public void setUp() throws IOException, GeneralSecurityException, CmdLineException {
        command = new AddCommand(factory);
        when(factory.createClient()).thenReturn(webmasters);
        when(webmasters.sites()).thenReturn(sites);
        when(sites.add(TEST_SITE_URL)).thenReturn(add);
        
        String[] args = {"-siteUrl", TEST_SITE_URL};
        new CmdLineParser(command).parseArgument(args);
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
        // When
        String actual = command.usage();

        // Then
        assertEquals("Adds a site to Google Search Console.", actual);
    }
}