package xyz.monotalk.google.webmaster.cli;

import javax.annotation.Nullable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.stereotype.Component;

/**
 * FullyQualifiedClassNameBeanNameGeneratorクラス - 完全修飾クラス名を使用したBean名生成。
 *
 * <p>このクラスは、Spring FrameworkのBean名生成において、
 * 完全修飾クラス名を使用します。</p>
 */
@Component
public class FullyQualifiedClassNameBeanNameGenerator implements BeanNameGenerator {

    /**
     * デフォルトコンストラクタ。
     */
    public FullyQualifiedClassNameBeanNameGenerator() {
        // デフォルトの初期化処理
    }

    /**
     * 指定されたBeanDefinitionから完全修飾クラス名をBean名として返します。
     *
     * @param definition Bean定義。
     * @param registry Beanレジストリ。
     * @return 完全修飾クラス名、Bean定義がnullの場合は空文字列。
     */
    @Override
    public String generateBeanName(final BeanDefinition definition,
            @Nullable final BeanDefinitionRegistry registry) {
        if (definition == null) {
            return "";
        }
        final String beanClassName = definition.getBeanClassName();
        return beanClassName != null ? beanClassName : definition.toString();
    }
}
