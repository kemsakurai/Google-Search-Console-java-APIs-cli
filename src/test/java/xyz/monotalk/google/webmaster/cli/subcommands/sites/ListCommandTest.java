package xyz.monotalk.google.webmaster.cli.subcommands.sites;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ListCommandTest {

    @Mock
    private WebmastersFactory factory;

    @Mock
    private Webmasters webmasters;

    @Mock
    private Webmasters.Sites sites;

    @Mock
    private Webmasters.Sites.List request;

    @InjectMocks
    private ListCommand command;

    @Before
    public void setUp() throws IOException {
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.sites()).thenReturn(sites);
        when(sites.list()).thenReturn(request);
    }

    @Test
    public void testExecute_正常系() throws IOException {
        // テストデータの準備
        SitesListResponse response = new SitesListResponse();
        when(request.execute()).thenReturn(response);

        // 実行
        command.execute();

        // 検証
        verify(factory).create();
        verify(webmasters).sites();
        verify(sites).list();
        verify(request).execute();
    }

    @Test(expected = CmdLineIOException.class)
    public void testExecute_APIエラー発生時にスタックトレースが出力されること() throws IOException {
        when(request.execute()).thenThrow(new IOException("API Error"));
        command.execute();
    }

    @Test
    public void testUsage_使用方法の説明が取得できること() {
        String usage = command.usage();
        assertEquals("Lists the user's Search Console sites.", usage);
    }
}