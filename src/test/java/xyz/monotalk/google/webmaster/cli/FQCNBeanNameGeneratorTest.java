package xyz.monotalk.google.webmaster.cli;

import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import static org.junit.Assert.assertEquals;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class FQCNBeanNameGeneratorTest {

    @Test
    public void testBuildDefaultBeanName_正常系_完全修飾クラス名が返却される() {
        // Given
        FQCNBeanNameGenerator generator = new FQCNBeanNameGenerator();
        String className = "xyz.monotalk.google.webmaster.cli.TestClass";
        GenericBeanDefinition definition = (GenericBeanDefinition) BeanDefinitionBuilder
                .genericBeanDefinition(className)
                .getBeanDefinition();

        // When
        // レジストリ（BeanFactory）の生成
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        String beanName = generator.generateBeanName(definition, registry);

        // Then
        assertEquals(className, beanName);
    }
}