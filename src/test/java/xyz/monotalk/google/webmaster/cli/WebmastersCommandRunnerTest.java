package xyz.monotalk.google.webmaster.cli;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.test.util.ReflectionTestUtils;
import xyz.monotalk.google.webmaster.cli.subcommands.sites.ListCommand;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WebmastersCommandRunnerTest {

    @InjectMocks
    private WebmastersCommandRunner commandRunner;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Mock
    private WebmastersFactory mockWebmastersFactory;

    @Mock
    private Webmasters mockWebmasters;

    @Mock
    private Webmasters.Sites mockSites;

    @Mock
    private Webmasters.Sites.List mockSitesList;

    @Mock
    private SitesListResponse mockResponse;

    @Mock
    private Command mockCommand;

    @Before
    public void setUp() throws Exception {
        when(applicationContext.getAutowireCapableBeanFactory()).thenReturn(autowireCapableBeanFactory);
        when(mockWebmastersFactory.create()).thenReturn(mockWebmasters);
        when(mockWebmasters.sites()).thenReturn(mockSites);
        when(mockSites.list()).thenReturn(mockSitesList);
        when(mockSitesList.execute()).thenReturn(mockResponse);
        
        // ListCommandのWebmastersFactory注入をモック
        doAnswer(invocation -> {
            ListCommand command = (ListCommand) invocation.getArgument(0);
            ReflectionTestUtils.setField(command, "factory", mockWebmastersFactory);
            return command;
        }).when(autowireCapableBeanFactory).autowireBean(any(ListCommand.class));
    }

    @Test
    public void testRun_正常系_コマンド実行成功() throws Exception {
        // Given
        String[] args = {"webmasters.sites.list"};
        
        // When
        commandRunner.run(args);
        
        // Then
        verify(mockWebmastersFactory).create();
        verify(mockWebmasters).sites();
        verify(mockSites).list();
        verify(mockSitesList).execute();
    }

    @Test
    public void testRun_正常系_ヘルプ表示() throws Exception {
        // Given
        String[] args = {"-?"};
        
        // When
        commandRunner.run(args);
        
        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }

    @Test
    public void testRun_正常系_引数なしでヘルプ表示() throws Exception {
        // Given
        String[] args = {};
        
        // When
        commandRunner.run(args);
        
        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }

    @Test
    public void testRun_異常系_不正な引数() throws Exception {
        // Given
        String[] args = {"invalid.command"};
        
        // When
        commandRunner.run(args);
        
        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }

    @Test
    public void testRun_正常系_アプリケーション引数除外() throws Exception {
        // Given
        String[] args = {"webmasters.sites.list", "--application.keyFileLocation=test.json"};
        
        // When
        commandRunner.run(args);
        
        // Then
        verify(mockWebmastersFactory).create();
        verify(mockWebmasters).sites();
        verify(mockSites).list();
        verify(mockSitesList).execute();
    }

    @Test
    public void testRun_異常系_不正なアプリケーション引数() throws Exception {
        // Given
        String[] args = {"--invalid-argument"};
        
        // When
        commandRunner.run(args);
        
        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }

    @Test
    public void testRun_異常系_引数解析エラー() throws Exception {
        // Given
        String[] args = {"--invalid-format"};
        
        // When
        commandRunner.run(args);
        
        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }
}