package xyz.monotalk.google.webmaster.cli;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * {@summary CliApplicationのテストクラス。}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CliApplicationTest {

    /**
     * アプリケーションコンテキストを管理するためのフィールド。
     */
    @Autowired
    private ApplicationContext context;

    /**
     * デフォルトコンストラクタ。
     */
    public CliApplicationTest() {
        // 初期化処理が必要な場合はここに記述
    }

    /**
     * アプリケーションコンテキストが正しく起動することをテストします。
     */
    @Test
    public void testContext_shouldLoadApplicationContextSuccessfully() {
        assertNotNull("アプリケーションコンテキストがnullです", context);
    }

    /**
     * 必要なBeanが登録されていることをテストします。
     */
    @Test
    public void testBeanConfiguration_shouldRegisterRequiredBeans() {
        assertNotNull("WebmastersFactoryが登録されていません", context.getBean(WebmastersFactory.class));
        assertNotNull("WebmastersCommandRunnerが登録されていません", context.getBean(WebmastersCommandRunner.class));
    }

    /**
     * アプリケーションが正常に起動できることをテストします。
     */
    @Test
    public void testMain_shouldStartApplicationWithoutException() {
        CliApplication.main(new String[]{});
        assertNotNull("アプリケーションが起動しませんでした", context);
    }
}