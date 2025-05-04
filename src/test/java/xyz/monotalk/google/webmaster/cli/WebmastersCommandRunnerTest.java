package xyz.monotalk.google.webmaster.cli;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner.Silent;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.test.util.ReflectionTestUtils;
import xyz.monotalk.google.webmaster.cli.subcommands.sites.ListCommand;

import java.io.IOException;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(Silent.class) // lenientモードを使用
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

    @Mock
    private ResponseWriter mockResponseWriter;

    @Before
    public void setUp() {
        when(applicationContext.getAutowireCapableBeanFactory()).thenReturn(autowireCapableBeanFactory);

        doAnswer(invocation -> {
            Object bean = invocation.getArgument(0);
            if (bean instanceof ListCommand) {
                ListCommand listCommand = (ListCommand) bean;
                ReflectionTestUtils.setField(listCommand, "factory", mockWebmastersFactory);
                ReflectionTestUtils.setField(listCommand, "responseWriter", mockResponseWriter);
            }
            return null;
        }).when(autowireCapableBeanFactory).autowireBean(any());

        when(applicationContext.getBean("webmasters.sites.list")).thenReturn(new ListCommand());
    }

    @Test
    public void testRun_正常系_コマンド実行成功() throws Exception {
        // Given
        String[] args = {"webmasters.sites.list"};
        try {
            when(mockWebmastersFactory.create()).thenReturn(mockWebmasters);
        } catch (CmdLineIOException e) {
            fail("CmdLineIOException should not occur: " + e.getMessage());
        }
        when(mockWebmasters.sites()).thenReturn(mockSites);
        try {
            when(mockSites.list()).thenReturn(mockSitesList);
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        }
        try {
            when(mockSitesList.execute()).thenReturn(mockResponse);
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        }

        // When
        try {
            commandRunner.run(args);
        } catch (Exception e) {
            fail("Exception should not occur: " + e.getMessage());
        }

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
        try {
            commandRunner.run(args);
        } catch (Exception e) {
            fail("Exception should not occur: " + e.getMessage());
        }

        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }

    @Test
    public void testRun_正常系_引数なしでヘルプ表示() throws Exception {
        // Given
        String[] args = {};

        // When
        try {
            commandRunner.run(args);
        } catch (Exception e) {
            fail("Exception should not occur: " + e.getMessage());
        }

        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }

    @Test
    public void testRun_異常系_不正な引数() throws Exception {
        // Given
        String[] args = {"invalid.command"};

        // When
        try {
            commandRunner.run(args);
        } catch (Exception e) {
            fail("Exception should not occur: " + e.getMessage());
        }

        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }

    @Test
    public void testRun_正常系_アプリケーション引数除外() throws Exception {
        // Given
        String[] args = {"webmasters.sites.list", "--application.keyFileLocation=test.json"};
        try {
            when(mockWebmastersFactory.create()).thenReturn(mockWebmasters);
        } catch (CmdLineIOException e) {
            fail("CmdLineIOException should not occur: " + e.getMessage());
        }
        when(mockWebmasters.sites()).thenReturn(mockSites);
        try {
            when(mockSites.list()).thenReturn(mockSitesList);
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        }
        try {
            when(mockSitesList.execute()).thenReturn(mockResponse);
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        }

        // When
        try {
            commandRunner.run(args);
        } catch (Exception e) {
            fail("Exception should not occur: " + e.getMessage());
        }

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
        try {
            commandRunner.run(args);
        } catch (Exception e) {
            fail("Exception should not occur: " + e.getMessage());
        }

        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }

    @Test
    public void testRun_異常系_引数解析エラー() throws Exception {
        // Given
        String[] args = {"--invalid-format"};

        // When
        try {
            commandRunner.run(args);
        } catch (Exception e) {
            fail("Exception should not occur: " + e.getMessage());
        }

        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }
}