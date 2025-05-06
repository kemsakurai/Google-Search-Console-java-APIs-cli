package xyz.monotalk.google.webmaster.cli;

import javax.annotation.Nullable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.stereotype.Component;

/**
 * FQCNBeanNameGeneratorクラス - 完全修飾クラス名を使用したBean名生成。
 *
 * <p>このクラスは、Spring FrameworkのBean名生成において、
 * 完全修飾クラス名を使用します。</p>
 */
@Component
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class FQCNBeanNameGenerator implements BeanNameGenerator {

    /**
     * デフォルトコンストラクタ.
     */
    public FQCNBeanNameGenerator() {
        // デフォルトの初期化処理
    }

    /**
     * 指定されたBeanDefinitionから完全修飾クラス名をBean名として返します.
     *
     * 
@param definition Bean定義
     * 
@param registry Beanレジストリ
     * @return 完全修飾クラス名
     */
    @Override
    @Nullable
    public String generateBeanName(final BeanDefinition definition, 
        final BeanDefinitionRegistry registry) {
        if (definition == null) {
            throw new IllegalArgumentException("BeanDefinition must not be null");
        }
        return definition.getBeanClassName();
    }
}
