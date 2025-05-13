package xyz.monotalk.google.webmaster.cli.subcommands.sites;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * ListCommandのテストクラス。
 */
@RunWith(MockitoJUnitRunner.class)
public class ListCommandTest {

    /** テスト用のWebmastersFactoryモック。 */
    @Mock
    private WebmastersFactory factory;

    /** テスト用のWebmastersモック。 */
    @Mock
    private Webmasters webmasters;

    /** テスト用のWebmasters.Sitesモック。 */
    @Mock
    private Webmasters.Sites sites;

    /** テスト用のWebmasters.Sites.Listモック。 */
    @Mock
    private Webmasters.Sites.List sitesList;

    /**
     * サイト一覧の取得が正常に実行されることをテストします。
     *
     * @throws Exception テスト実行中に例外が発生した場合
     */
    @Test
    public void testExecute_正常系_サイト一覧が取得できる() throws Exception {
        // Given
        final SitesListResponse response = new SitesListResponse();
        when(factory.createClient()).thenReturn(webmasters);
        when(webmasters.sites()).thenReturn(sites);
        when(sites.list()).thenReturn(sitesList);
        when(sitesList.execute()).thenReturn(response);

        final ListCommand command = new ListCommand(factory);

        // When
        command.execute();

        // Then
        verify(factory).createClient();
        verify(webmasters).sites();
        verify(sites).list();
        verify(sitesList).execute();
    }

    /**
     * WebmastersFactoryがnullの場合、execute()を呼び出した時に例外がスローされることをテストします。
     */
    @Test(expected = IllegalStateException.class)
    public void testConstructor_異常系_factoryがnull() {
        // コンストラクターではなくexecuteメソッド実行時に例外が発生する
        final ListCommand command = new ListCommand(null);
        command.execute();
    }

    /**
     * APIコール中にIOExceptionが発生した場合のテストです。
     *
     * @throws Exception テスト実行中に例外が発生した場合
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testExecute_異常系_InputOutputExceptionが発生() throws Exception {
        // Given
        when(factory.createClient()).thenReturn(webmasters);
        when(webmasters.sites()).thenReturn(sites);
        when(sites.list()).thenReturn(sitesList);
        when(sitesList.execute()).thenThrow(IOException.class);

        final ListCommand command = new ListCommand(factory);

        // When
        command.execute();
    }
}