package xyz.monotalk.google.webmaster.cli;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CliApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testContext_正常系_アプリケーションコンテキストが正しく起動する() {
        assertNotNull(context);
    }

    @Test
    public void testBeanConfiguration_正常系_必要なBeanが登録されている() {
        assertNotNull(context.getBean(WebmastersFactory.class));
        assertNotNull(context.getBean(WebmastersCommandRunner.class));
    }

    @Test
    public void testMain_正常系_アプリケーションが起動できる() {
        CliApplication.main(new String[]{});
        assertTrue(true); // アプリケーションが例外なく起動できることを確認
    }
}