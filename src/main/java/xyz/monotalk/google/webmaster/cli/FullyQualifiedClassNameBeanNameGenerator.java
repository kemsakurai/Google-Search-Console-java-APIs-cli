package xyz.monotalk.google.webmaster.cli;

import javax.annotation.Nullable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * 完全修飾クラス名をBean名として使用するBeanNameGeneratorです。
 */
public class FullyQualifiedClassNameBeanNameGenerator extends AnnotationBeanNameGenerator {

    /**
     * デフォルトコンストラクタ
     */
    public FullyQualifiedClassNameBeanNameGenerator() {
        super();
    }

    /**
     * Bean名を生成します。
     *
     * @param definition Bean定義。
     * @return 完全修飾クラス名をBean名として返します。
     */
    @Override
    @Nullable
    protected String buildDefaultBeanName(final BeanDefinition definition) {
        if (definition == null) {
            throw new IllegalArgumentException("BeanDefinition must not be null");
        }
        return definition.getBeanClassName();
    }
}