package xyz.monotalk.google.webmaster.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

    @Autowired
    private ApplicationContext context;

    /**
     * {@summary アプリケーションコンテキストが正しく起動することをテスト。}
     */
    @Test
    public void testContext_shouldLoadApplicationContextSuccessfully() {
        assertNotNull(context);
    }

    /**
     * {@summary 必要なBeanが登録されていることをテスト。}
     */
    @Test
    public void testBeanConfiguration_shouldRegisterRequiredBeans() {
        assertNotNull(context.getBean(WebmastersFactory.class));
        assertNotNull(context.getBean(WebmastersCommandRunner.class));
    }

    /**
     * {@summary アプリケーションが正常に起動できることをテスト。}
     */
    @Test
    public void testMain_shouldStartApplicationWithoutException() {
        CliApplication.main(new String[]{});
        assertTrue(true); // アプリケーションが例外なく起動できることを確認
    }
}