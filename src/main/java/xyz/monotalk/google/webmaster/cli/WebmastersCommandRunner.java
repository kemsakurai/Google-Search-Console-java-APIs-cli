package xyz.monotalk.google.webmaster.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * コマンド実行ハンドラクラスです。
 */
@Component
public class WebmastersCommandRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(WebmastersCommandRunner.class);

    private final ApplicationContext context;
    private final Environment environment;

    /**
     * WebmastersCommandRunnerを作成します。
     *
     * @param context アプリケーションコンテキスト。
     * @param environment 環境変数。
     */
    public WebmastersCommandRunner(ApplicationContext context, Environment environment) {
        this.context = context;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        if (args == null || args.length < 1) {
            printUsage();
            return;
        }

        String command = args[0];
        Command bean = findCommand(command);
        if (bean == null) {
            logger.error("Command not found: {}", command);
            printUsage();
            return;
        }

        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);
        parseOption(bean, commandArgs);
        try {
            bean.execute();
        } catch (Exception e) {
            logger.error("Command failed: {}", e.getMessage(), e);
        }
    }

    /**
     * コマンドを検索します。
     *
     * @param commandName コマンド名。
     * @return 該当するコマンド。見つからない場合はnull。
     */
    protected Command findCommand(String commandName) {
        Map<String, Command> commands = context.getBeansOfType(Command.class);
        if (commands.isEmpty()) {
            logger.warn("No command found.");
            return null;
        }

        for (Command command : commands.values()) {
            String className = command.getClass().getSimpleName();
            String packageName = command.getClass().getPackage().getName();
            if (packageName.contains(commandName) && className.equals("ListCommand")) {
                return command;
            }
        }

        for (Command command : commands.values()) {
            String className = command.getClass().getSimpleName();
            if (className.equals(commandName + "Command")) {
                return command;
            }
        }
        return null;
    }

    /**
     * コマンドラインオプションをパースします。
     *
     * @param bean コマンドインスタンス。
     * @param args コマンドライン引数。
     */
    protected void parseOption(Command bean, String[] args) {
        try {
            CmdLineParser parser = new CmdLineParser(bean);
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            logger.error("Failed to parse options: {}", e.getMessage());
        }
    }

    /**
     * 使用方法を表示します。
     */
    protected void printUsage() {
        HashMap<String, List<Pair<String, String>>> commandMap = new HashMap<>();

        for (Map.Entry<String, Command> entry : context.getBeansOfType(Command.class).entrySet()) {
            String className = entry.getValue().getClass().getSimpleName();
            if (className.endsWith("Command")) {
                className = className.substring(0, className.lastIndexOf("Command"));
            }
            String packageName = entry.getValue().getClass().getPackage().getName();
            String commandGroup = packageName.substring(packageName.lastIndexOf(".") + 1);

            if (!commandMap.containsKey(commandGroup)) {
                commandMap.put(commandGroup, new ArrayList<>());
            }
            commandMap.get(commandGroup).add(Pair.of(className, entry.getValue().usage()));
        }

        System.out.println("Usage: <command> [options...]");
        System.out.println("Available commands:");
        for (Map.Entry<String, List<Pair<String, String>>> entry : commandMap.entrySet()) {
            System.out.println();
            System.out.println(" " + entry.getKey() + ":");
            for (Pair<String, String> command : entry.getValue()) {
                System.out.println("   " + command.getLeft() + ": " + command.getRight());
            }
        }
    }
}
