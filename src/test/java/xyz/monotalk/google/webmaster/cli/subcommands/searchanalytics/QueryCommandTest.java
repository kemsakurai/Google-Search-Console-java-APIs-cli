package xyz.monotalk.google.webmaster.cli.subcommands.searchanalytics;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.ApiDataRow;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryRequest;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outContent));
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.searchanalytics()).thenReturn(searchanalytics);
        when(searchanalytics.query(eq("https://www.monotalk.xyz"), any(SearchAnalyticsQueryRequest.class))).thenReturn(query);

    }

    @Test
    public void testExecute_正常系_検索結果がある場合() throws IOException {
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

    @Test
    public void testExecute_正常系_検索結果が空の場合() throws IOException {
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

    @Test
    public void testExecute_異常系_toPrettyString実行時に例外が発生() throws IOException {
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

    @Test
    public void testUsage_正常系_説明文字列が返却される() {
        // When
        String usage = command.usage();

        // Then
        assertTrue(usage.contains("Query your data with filters and parameters"));
    }
}