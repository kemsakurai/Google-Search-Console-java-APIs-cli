package xyz.monotalk.google.webmaster.cli;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Bootアプリケーションのエントリーポイントです。
 */
@SpringBootApplication
@ComponentScan(nameGenerator = FullyQualifiedClassNameBeanNameGenerator.class)
public class CliApplication {

    /**
     * アプリケーションのエントリーポイントです。
     *
     * @param args コマンドライン引数。
     */
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CliApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        SpringApplication.run(CliApplication.class, args);
    }
}
