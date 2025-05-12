package xyz.monotalk.google.webmaster.cli.subcommands.searchanalytics;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.ApiDataRow;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryRequest;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * QueryCommandクラスの単体テスト。
 */
@RunWith(MockitoJUnitRunner.class)
public class QueryCommandTest {

    /** 空のレスポンス。 */
    private static final String RESPONSE_EMPTY = "{}";

    /** 標準出力。 */
    private static final PrintStream STANDARD_OUT = System.out;

    /** WebmastersFactoryのモック。 */
    @Mock
    private WebmastersFactory factory;

    /** QueryCommandのインスタンス。 */
    @InjectMocks
    private QueryCommand queryCommand;

    /** Webmastersのモック。 */
    @Mock
    private Webmasters webmasters;

    /** Searchanalyticsのモック。 */
    @Mock
    private Webmasters.Searchanalytics searchanalytics;

    /** 検索クエリのモック。 */
    @Mock
    private Webmasters.Searchanalytics.Query searchQuery;

    /** ResponseWriterのモック。 */
    @Mock
    private ResponseWriter responseWriter;

    /** テスト出力をキャプチャするためのストリーム。 */
    private ByteArrayOutputStream outputContent;

    /**
     * テスト前のセットアップ処理。
     *
     * @throws IOException モックのセットアップ中に例外が発生した場合
     * @throws GeneralSecurityException セキュリティ例外が発生した場合
     */
    @Before
    public void setUp() throws IOException, GeneralSecurityException {
        initializeOutputStream();
        setupMocks();
        initializeQueryCommand();
    }

    /**
     * 出力ストリームの初期化。
     */
    private void initializeOutputStream() {
        outputContent = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(outputContent, false, StandardCharsets.UTF_8);
        System.setOut(printStream);
    }

    /**
     * モックのセットアップ。
     *
     * @throws IOException モックのセットアップ中に例外が発生した場合
     * @throws GeneralSecurityException セキュリティ例外が発生した場合
     */
    private void setupMocks() throws IOException, GeneralSecurityException {
        when(factory.createClient()).thenReturn(webmasters);
        when(webmasters.searchanalytics()).thenReturn(searchanalytics);
        when(searchanalytics.query(anyString(), any(SearchAnalyticsQueryRequest.class))).thenReturn(searchQuery);
        final SearchAnalyticsQueryResponse response = spy(new SearchAnalyticsQueryResponse());
        when(searchQuery.execute()).thenReturn(response);
    }

    /**
     * QueryCommandの初期化。
     */
    private void initializeQueryCommand() {
        try {
            java.lang.reflect.Field startDateField = QueryCommand.class.getDeclaredField("startDate");
            java.lang.reflect.Field endDateField = QueryCommand.class.getDeclaredField("endDate");
            java.lang.reflect.Field siteUrlField = QueryCommand.class.getDeclaredField("siteUrl");
            
            startDateField.setAccessible(true);
            endDateField.setAccessible(true);
            siteUrlField.setAccessible(true);
            
            startDateField.set(queryCommand, "2020-01-01");
            endDateField.set(queryCommand, "2020-01-31");
            siteUrlField.set(queryCommand, "https://example.com");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize QueryCommand", e);
        }
    }

    /**
     * テスト後のクリーンアップ処理。
     */
    @After
    public void tearDown() {
        System.setOut(STANDARD_OUT);
    }

    /**
     * 空の検索結果を取得した場合のテスト。
     *
     * @throws IOException モックのセットアップ中に例外が発生した場合
     */
    @Test
    public void testQueryCommand_EmptyResponse() throws IOException {
        // 実行
        queryCommand.execute();

        // 検証
        String output = outputContent.toString(StandardCharsets.UTF_8).trim();
        assertTrue("空のレスポンスが期待通り出力されていません",
                output.contains(RESPONSE_EMPTY) || output.equals(RESPONSE_EMPTY));
    }

    /**
     * 検索結果が含まれている場合のテスト。
     *
     * @throws IOException モックのセットアップ中に例外が発生した場合
     */
    @Test
    public void testQueryCommand_WithResults() throws IOException {
        final SearchAnalyticsQueryResponse response = spy(new SearchAnalyticsQueryResponse());
        final ApiDataRow row = new ApiDataRow();
        row.setClicks(100.0);
        row.setImpressions(1000.0);
        row.setCtr(0.1);
        row.setPosition(1.5);
        response.setRows(Arrays.asList(row));
        when(searchQuery.execute()).thenReturn(response);

        queryCommand.execute();

        assertTrue("検索結果が期待通り出力されていません",
            outputContent.toString(StandardCharsets.UTF_8).length() > 0);
    }
}