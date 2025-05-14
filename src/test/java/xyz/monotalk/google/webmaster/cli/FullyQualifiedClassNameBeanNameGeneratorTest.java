package xyz.monotalk.google.webmaster.cli;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * FullyQualifiedClassNameBeanNameGeneratorのテストクラス。
 */
public class FullyQualifiedClassNameBeanNameGeneratorTest {

    /**
     * デフォルトコンストラクタ。
     */
    public FullyQualifiedClassNameBeanNameGeneratorTest() {
        // 初期化処理
    }

    /**
     * Bean名が正しく生成されることをテストします。
     */
    @Test
    public void testGenerateBeanName_正常系_正しいBean名が生成される() {
        // Given
        final FullyQualifiedClassNameBeanNameGenerator generator = new FullyQualifiedClassNameBeanNameGenerator();
        final String className = "xyz.monotalk.google.webmaster.cli.TestClass";
        final GenericBeanDefinition definition = (GenericBeanDefinition) BeanDefinitionBuilder
                .genericBeanDefinition(className)
                .getBeanDefinition();
        final BeanDefinitionRegistry registry = mock(BeanDefinitionRegistry.class);

        // When
        final String beanName = generator.generateBeanName(definition, registry);

        // Then
        assertEquals("Bean名が正しく生成されていません", className, beanName);
    }

    /**
     * Bean定義のクラス名がnullの場合のテスト。
     */
    @Test
    public void testGenerateBeanName_異常系_クラス名がnull() {
        // Given
        final FullyQualifiedClassNameBeanNameGenerator generator = new FullyQualifiedClassNameBeanNameGenerator();
        final GenericBeanDefinition definition = new GenericBeanDefinition();
        final BeanDefinitionRegistry registry = mock(BeanDefinitionRegistry.class);

        // When
        final String beanName = generator.generateBeanName(definition, registry);

        // Then
        assertEquals("クラス名がnullの場合は空文字列を返すべき", "", beanName);
    }
}