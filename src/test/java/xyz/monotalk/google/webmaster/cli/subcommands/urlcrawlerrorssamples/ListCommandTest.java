package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.UrlCrawlErrorsSample;
import com.google.api.services.webmasters.model.UrlCrawlErrorsSamplesListResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
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
    private Webmasters webmasters;

    @Mock
    private Webmasters.Urlcrawlerrorssamples urlcrawlerrorssamples;

    @Mock
    private Webmasters.Urlcrawlerrorssamples.List request;

    @InjectMocks
    private ListCommand command;

    @Before
    public void setUp() throws IOException {
        // テスト対象コマンドの初期設定
        command.setSiteUrl("https://www.monotalk.xyz");
        command.setCategory("notFound");
        command.setPlatform("web");
        
        // モックの設定
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.urlcrawlerrorssamples()).thenReturn(urlcrawlerrorssamples);
        when(urlcrawlerrorssamples.list(anyString(), anyString(), anyString())).thenReturn(request);
    }

    /**
     * 正常系のテスト
     * URLクロールエラーサンプルが正常に取得されることを検証
     */
    @Test
    public void testExecute_WithValidParameters_ShouldReturnErrorSamples() throws IOException, CmdLineIOException {
        // Given
        UrlCrawlErrorsSamplesListResponse response = new UrlCrawlErrorsSamplesListResponse();
        UrlCrawlErrorsSample sample = new UrlCrawlErrorsSample();
        sample.setPageUrl("https://example.com/error");
        response.setUrlCrawlErrorSample(Arrays.asList(sample));
        
        when(request.execute()).thenReturn(response);

        // ResponseWriter.writeJsonをモック化
        try (MockedStatic<ResponseWriter> mockedStatic = Mockito.mockStatic(ResponseWriter.class)) {
            // When
            command.execute();

            // Then
            verify(factory).create();
            verify(webmasters).urlcrawlerrorssamples();
            verify(urlcrawlerrorssamples).list(eq("https://www.monotalk.xyz"), eq("notFound"), eq("web"));
            verify(request).execute();
            
            // 静的メソッドの呼び出しを検証
            mockedStatic.verify(() -> 
                ResponseWriter.writeJson(anyString(), eq(Format.CONSOLE), eq(null)));
        }
    }

    /**
     * API呼び出しでエラーが発生した場合のテスト
     * IOExceptionがCmdLineIOExceptionとしてスローされることを確認
     */
    @Test(expected = CmdLineIOException.class)
    public void testExecute_WhenApiCallFails_ShouldThrowCmdLineIOException() throws IOException, CmdLineIOException {
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