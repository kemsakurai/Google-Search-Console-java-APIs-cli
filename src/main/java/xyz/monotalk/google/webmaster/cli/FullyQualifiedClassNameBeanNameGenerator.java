package xyz.monotalk.google.webmaster.cli;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * 完全修飾クラス名をBean名として使用するBeanNameGeneratorです。
 */
public class FullyQualifiedClassNameBeanNameGenerator extends AnnotationBeanNameGenerator {

    /**
     * Bean名を生成します。
     *
     * @param definition Bean定義。
     * @return 完全修飾クラス名をBean名として返します。
     */
    @Override
    protected String buildDefaultBeanName(BeanDefinition definition) {
        return definition.getBeanClassName();
    }
}