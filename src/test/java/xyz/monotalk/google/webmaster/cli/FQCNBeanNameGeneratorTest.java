package xyz.monotalk.google.webmaster.cli;

import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import static org.junit.Assert.assertEquals;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import static org.mockito.Mockito.mock;

/**
 * FullyQualifiedClassNameBeanNameGeneratorのテストクラス。
 */
public class FQCNBeanNameGeneratorTest {

    /**
     * 完全修飾クラス名がbean名として正しく生成されることをテスト。
     */
    @Test
    public void testGenerateBeanName_shouldReturnFullyQualifiedClassName() {
        // Given
        FullyQualifiedClassNameBeanNameGenerator generator = new FullyQualifiedClassNameBeanNameGenerator();
        String className = "xyz.monotalk.google.webmaster.cli.TestClass";
        GenericBeanDefinition definition = (GenericBeanDefinition) BeanDefinitionBuilder
                .genericBeanDefinition(className)
                .getBeanDefinition();
        BeanDefinitionRegistry registry = mock(BeanDefinitionRegistry.class);
        String beanName = generator.generateBeanName(definition, registry);
        // Then
        assertEquals(className, beanName);
    }
}