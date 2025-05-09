package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

// インポート文を辞書順に並べ替え、STATICグループを正しい位置に移動
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
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
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;
import xyz.monotalk.google.webmaster.cli.subcommands.sites.AddCommand;

/**
 * AddCommandTestクラス。
 *
 * <p>AddCommandのテストを実行します。</p>
 */

@RunWith(MockitoJUnitRunner.class)
public class AddCommandTest {

    @Mock
    private WebmastersFactory factory;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Sitemaps sitemaps;

    @Mock
    private Webmasters.Sitemaps.Submit submit;

    @InjectMocks
    private AddCommand command;

    private static final String SITE_URL = "https://example.com";

    /**
     * テストのセットアップを行います。
     */
    @Before
    public void setUp() throws IOException {
        // サイト追加関連のモック設定
        Webmasters.Sites sites = mock(Webmasters.Sites.class);
        Webmasters.Sites.Add add = mock(Webmasters.Sites.Add.class);

        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sites()).thenReturn(sites);
        when(sites.add(SITE_URL)).thenReturn(add);
        doNothing().when(add).execute();

        // サイトマップ関連のモック設定
        when(webmasters.sitemaps()).thenReturn(sitemaps);
        when(sitemaps.submit(anyString(), anyString())).thenReturn(submit);
        doNothing().when(submit).execute();
    }

    /**
     * 正常系: コマンドが正しく実行されることを検証します。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Test
    public void testExecute正常系() throws IOException {
        // Given
        command.setSiteUrl(SITE_URL);

        // When
        command.execute();

        // Then
        verify(factory).create();
        verify(webmasters).sitemaps();
        verify(sitemaps).submit(eq(SITE_URL), eq(SITE_URL + "/sitemap.xml"));
        verify(submit).execute();
    }

    /**
     * 正常系: JSON形式で出力されることを検証します。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Test
    public void testExecute正常系Jsonフォーマット出力() throws IOException {
        // Given
        command.setSiteUrl(SITE_URL);
        command.setFormat(Format.JSON);

        // When
        command.execute();

        // Then
        verify(factory).create();
        verify(webmasters).sitemaps();
        verify(sitemaps).submit(eq(SITE_URL), eq(SITE_URL + "/sitemap.xml"));
        verify(submit).execute();
    }

    /**
     * 正常系: サイトが正常に追加されることを検証します。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Test
    public void testExecute正常系サイト追加成功() throws IOException {
        // Given
        command.setSiteUrl(SITE_URL);

        // When
        command.execute();

        // Then
        verify(factory).create();
        verify(webmasters).sites();
        verify(webmasters).sitemaps();
    }

    /**
     * 異常系: サイトURLが指定されていない場合に例外が発生することを検証します。
     *
     * @throws CmdLineArgmentException コマンドライン引数例外が発生した場合。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteErrorSiteUrlNotSpecified() {
        // Given
        command.setSiteUrl(null);

        // When
        command.execute();

        // Then
        // CmdLineArgmentExceptionがスローされることを期待
    }

    /**
     * 異常系: サイトマップURLが指定されていない場合に例外が発生することを検証します。
     *
     * @throws CmdLineArgmentException コマンドライン引数例外が発生した場合。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testExecuteErrorSitemapUrlNotSpecified() {
        // Given
        command.setSiteUrl("");

        // When
        command.execute();

        // Then
        // CmdLineArgmentExceptionがスローされることを期待
    }

    /**
     * 正常系: 使用方法の説明文字列が返却されることを検証します。
     */
    @Test
    public void testUsage正常系説明文字列返却() {
        // When
        String usage = command.usage();

        // Then
        assertEquals("Adds a site to Google Search Console.", usage);
    }
}
