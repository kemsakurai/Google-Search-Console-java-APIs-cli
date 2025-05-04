package xyz.monotalk.google.webmaster.cli;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Google Search Console CLI アプリケーションのエントリーポイントクラスです。
 * このクラスはSpring Bootアプリケーションを起動し、コマンドライン引数を処理します。
 */
@SpringBootApplication
@ComponentScan(nameGenerator = FullyQualifiedClassNameBeanNameGenerator.class)
public class CliApplication {
    
    /**
     * インスタンス化を防ぐためのプライベートコンストラクタです。
     * このクラスはユーティリティクラスとして機能し、静的メソッドのみを提供します。
     */
    public CliApplication() {
        // インスタンス化防止のためのコンストラクタ
    }
    
    /**
     * アプリケーションのエントリーポイントです。
     * Spring Bootアプリケーションを初期化し、起動します。
     *
     * @param args コマンドライン引数
     */
    public static void main(final String[] args) {
        final SpringApplication springApplication = new SpringApplication(CliApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        SpringApplication.run(CliApplication.class, args);
    }
}
