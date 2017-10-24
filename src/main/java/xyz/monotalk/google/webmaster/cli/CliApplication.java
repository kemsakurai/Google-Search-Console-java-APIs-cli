package xyz.monotalk.google.webmaster.cli;

import org.kohsuke.args4j.OptionHandlerRegistry;
import org.kohsuke.args4j.spi.EnumOptionHandler;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xyz.monotalk.google.webmaster.cli.subcommands.sitemaps.Format;

@SpringBootApplication
public class CliApplication {
    public static void main(String[] args) {
        // registerHandler
//        OptionHandlerRegistry.OptionHandlerFactory factory = (parser, o, setter) -> {
//            // infer the type
//            Class<?> t = setter.getType();
//            return new EnumOptionHandler(parser,o,setter,t);
//        };
//        OptionHandlerRegistry.getRegistry().registerHandler(Format.class, factory);

        SpringApplication springApplication = new SpringApplication(CliApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}
