package xyz.monotalk.google.webmaster.cli.subcommands.searchanalytics;

// インポート文を辞書順に並べ替え、STATICグループを正しい位置に移動
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.ApiDataRow;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryRequest;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

// JavaDocコメントの句点を日本語の句点に修正
/**
 * QueryCommandのテストクラス。
 */
@RunWith(MockitoJUnitRunner.class)
public class QueryCommandTest {

    @Mock
    private WebmastersFactory factory;

    @InjectMocks
    private QueryCommand command;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Searchanalytics searchanalytics;

    @Mock
    private Webmasters.Searchanalytics.Query query;

    @Mock
    private ResponseWriter responseWriter;

    private ByteArrayOutputStream outContent;

    /**
     * テスト前のセットアップ処理。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Before
    public void setUp() throws IOException {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.searchanalytics()).thenReturn(searchanalytics);
        when(searchanalytics.query(eq("https://www.monotalk.xyz"), any(SearchAnalyticsQueryRequest.class))).thenReturn(query);
    }

    /**
     * 検索結果がある場合の正常系テスト。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Test
    public void testExecute正常系検索結果あり() throws IOException {
        // Given
        ApiDataRow row = Mockito.spy(new ApiDataRow());
        row.setClicks(100.0);
        row.setImpressions(1000.0);
        row.setCtr(0.1);
        row.setPosition(1.5);
        row.setKeys(Arrays.asList("page1", "query1", "2017-05-01", "mobile", "JP"));

        SearchAnalyticsQueryResponse response = Mockito.spy(new SearchAnalyticsQueryResponse());
        response.setRows(Arrays.asList(row));

        String responseJson = "{\"rows\":[{\"clicks\":100.0}]}";
        String rowJson = "{\"clicks\":100.0}";

        when(query.execute()).thenReturn(response);
        when(response.toPrettyString()).thenReturn(responseJson);
        when(row.toPrettyString()).thenReturn(rowJson);

        // When
        command.execute();

        // Then
        String output = outContent.toString();
        assertTrue("Response should be printed", output.contains(responseJson));
        assertTrue("Row should be printed", output.contains(rowJson));
    }

    /**
     * 検索結果が空の場合の正常系テスト。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Test
    public void testExecute正常系検索結果空() throws IOException {
        // Given
        SearchAnalyticsQueryResponse response = Mockito.spy(new SearchAnalyticsQueryResponse());
        when(query.execute()).thenReturn(response);
        when(response.toPrettyString()).thenReturn("{}");

        // When
        command.execute();

        // Then
        String output = outContent.toString();
        assertTrue(output.contains("{}"));
    }

    /**
     * toPrettyString実行時に例外が発生する異常系テスト。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Test
    public void testExecute異常系ToPrettyString例外発生() throws IOException {
        // Given
        SearchAnalyticsQueryResponse response = Mockito.spy(new SearchAnalyticsQueryResponse());
        when(query.execute()).thenReturn(response);
        when(response.toPrettyString()).thenThrow(new IOException("Pretty print error"));

        // When
        command.execute();

        // Then
        String output = outContent.toString();
        assertTrue("出力にPretty print errorが含まれていること", output.contains("Pretty print error"));
    }

    /**
     * usageメソッドの正常系テスト。
     */
    @Test
    public void testUsage正常系説明文字列返却() {
        // When
        String usage = command.usage();

        // Then
        assertTrue(usage.contains("Query your data with filters and parameters"));
    }
}