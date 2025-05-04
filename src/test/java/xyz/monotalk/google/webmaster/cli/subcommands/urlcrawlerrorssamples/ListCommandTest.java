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
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * URLクロールエラーサンプル一覧取得コマンドのテストクラス
 */
@RunWith(MockitoJUnitRunner.class)
public class ListCommandTest {

    @Mock
    private WebmastersFactory factory;

    @Mock
    private ResponseWriter responseWriter;

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
        
        // ResponseWriterのモックセットアップ
        doNothing().when(responseWriter).writeJson(any(), any(Format.class), anyString());
    }

    /**
     * 正常系のテスト
     * URLクロールエラーサンプルが正常に取得されることを検証
     */
    @Test
    public void testExecute_WithValidParameters_ShouldReturnErrorSamples() throws IOException {
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
        verify(responseWriter).writeJson(eq(response), any(Format.class), anyString());
    }

    /**
     * API呼び出しでエラーが発生した場合のテスト
     * IOExceptionがCmdLineIOExceptionとしてスローされることを確認
     */
    @Test(expected = CmdLineIOException.class)
    public void testExecute_WhenApiCallFails_ShouldThrowCmdLineIOException() throws IOException {
        // Given
        when(request.execute()).thenThrow(new IOException("API Error"));

        // When
        command.execute();
    }

    /**
     * usage()メソッドのテスト
     * 説明文が正しく返されることを検証
     */
    @Test
    public void testUsage_ShouldReturnValidDescription() {
        // When
        String usage = command.usage();

        // Then
        assertNotNull("説明文がnullであってはならない", usage);
        assertTrue("説明文は空であってはならない", !usage.isEmpty());
    }
}