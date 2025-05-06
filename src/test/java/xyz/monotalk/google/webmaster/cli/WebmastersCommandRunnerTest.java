package xyz.monotalk.google.webmaster.cli;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitesListResponse;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner.Silent;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import xyz.monotalk.google.webmaster.cli.subcommands.sites.ListCommand;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * {@summary WebmastersCommandRunnerのテストクラス。}
 */
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

    /**
     * {@summary テスト前のセットアップ処理。}
     */
    @Before
    public void setUp() {
        when(applicationContext.getAutowireCapableBeanFactory()).thenReturn(autowireCapableBeanFactory);

        doAnswer(invocation -> {
            Object bean = invocation.getArgument(0);
            if (bean instanceof ListCommand) {
                ListCommand listCommand = (ListCommand) bean;
                ReflectionTestUtils.setField(listCommand, "factory", mockWebmastersFactory);
                // ResponseWriterフィールドは存在しないため設定は削除
            }
            return null;
        }).when(autowireCapableBeanFactory).autowireBean(any());

        when(applicationContext.getBean("webmasters.sites.list")).thenReturn(new ListCommand());
    }

    /**
     * {@summary コマンド実行が正常に成功するケースのテスト。}
     */
    @Test
    public void testRunWithCommandSuccessful() throws Exception {
        // Given
        String[] args = {"webmasters.sites.list"};
        try {
            when(mockWebmastersFactory.create()).thenReturn(mockWebmasters);
        } catch (CommandLineInputOutputException e) {
            fail("CommandLineInputOutputException should not occur: " + e.getMessage());
        }
        when(mockWebmasters.sites()).thenReturn(mockSites);
        try {
            when(mockSites.list()).thenReturn(mockSitesList);
        } catch (CommandLineInputOutputException e) {
            fail("CommandLineInputOutputException should not occur: " + e.getMessage());
        }
        try {
            when(mockSitesList.execute()).thenReturn(mockResponse);
        } catch (CommandLineInputOutputException e) {
            fail("CommandLineInputOutputException should not occur: " + e.getMessage());
        }

        // When
        try {
            commandRunner.run(args);
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        } catch (CommandLineInputOutputException e) {
            fail("CommandLineInputOutputException should not occur: " + e.getMessage());
        }

        // Then
        verify(mockWebmastersFactory).create();
        verify(mockWebmasters).sites();
        verify(mockSites).list();
        verify(mockSitesList).execute();
    }

    /**
     * {@summary ヘルプを表示するケースのテスト。}
     */
    @Test
    public void testRunWithHelpOption() throws Exception {
        // Given
        String[] args = {"-?"};

        // When
        try {
            commandRunner.run(args);
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        } catch (CommandLineInputOutputException e) {
            fail("CommandLineInputOutputException should not occur: " + e.getMessage());
        }

        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }

    /**
     * {@summary 引数なしでヘルプが表示されるケースのテスト。}
     */
    @Test
    public void testRunWithNoArgumentsShowsHelp() throws Exception {
        // Given
        String[] args = {};

        // When
        try {
            commandRunner.run(args);
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        } catch (CommandLineInputOutputException e) {
            fail("CommandLineInputOutputException should not occur: " + e.getMessage());
        }

        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }

    /**
     * {@summary 不正な引数が渡された場合のテスト。}
     */
    @Test
    public void testRunWithInvalidArgument() throws Exception {
        // Given
        String[] args = {"invalid.command"};

        // When
        try {
            commandRunner.run(args);
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        } catch (CommandLineInputOutputException e) {
            fail("CommandLineInputOutputException should not occur: " + e.getMessage());
        }

        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }

    /**
     * {@summary アプリケーション引数を除外するケースのテスト。}
     */
    @Test
    public void testRunExcludingApplicationArguments() throws Exception {
        // Given
        String[] args = {"webmasters.sites.list", "--application.keyFileLocation=test.json"};
        try {
            when(mockWebmastersFactory.create()).thenReturn(mockWebmasters);
        } catch (CommandLineInputOutputException e) {
            fail("CommandLineInputOutputException should not occur: " + e.getMessage());
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
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        } catch (CommandLineInputOutputException e) {
            fail("CommandLineInputOutputException should not occur: " + e.getMessage());
        }

        // Then
        verify(mockWebmastersFactory).create();
        verify(mockWebmasters).sites();
        verify(mockSites).list();
        verify(mockSitesList).execute();
    }

    /**
     * {@summary 不正なアプリケーション引数が渡された場合のテスト。}
     */
    @Test
    public void testRunWithInvalidApplicationArgument() throws Exception {
        // Given
        String[] args = {"--invalid-argument"};

        // When
        try {
            commandRunner.run(args);
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        } catch (CommandLineInputOutputException e) {
            fail("CommandLineInputOutputException should not occur: " + e.getMessage());
        }

        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }

    /**
     * {@summary 引数解析でエラーが発生した場合のテスト。}
     */
    @Test
    public void testRunWithArgumentParsingError() throws Exception {
        // Given
        String[] args = {"--invalid-format"};

        // When
        try {
            commandRunner.run(args);
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        } catch (CommandLineInputOutputException e) {
            fail("CommandLineInputOutputException should not occur: " + e.getMessage());
        }

        // Then
        verifyNoInteractions(mockWebmastersFactory);
    }
}