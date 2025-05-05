package xyz.monotalk.google.webmaster.cli;

import java.util.Arrays;
import java.util.Locale;

/**
 * 出力フォーマットを定義するenumです。
 * Java 21の機能を活用した拡張メソッドを含みます。
 */
public enum Format {
    /**
     * JSON形式の出力です。
     */
    JSON("json"),

    /**
     * CSV形式の出力です。
     */
    CSV("csv"),

    /**
     * コンソール形式の出力です。
     */
    CONSOLE("console");
    
    /**
     * このフォーマットの別名
     */
    private final String alias;
    
    /**
     * Formatコンストラクタ
     *
     * @param alias このフォーマットの別名
     */
    Format(final String alias) {
        this.alias = alias;
    }
    
    /**
     * フォーマットの別名を取得します。
     *
     * @return フォーマットの別名
     */
    public String getAlias() {
        return alias;
    }
    
    /**
     * 指定された名前または別名に一致するFormatを検索します。
     * Java 21のOptionalのパターンマッチング機能を使用します。
     *
     * @param name 検索する名前または別名
     * @return 一致するFormat、または一致しない場合はnull
     * @throws CmdLineArgmentException 無効なフォーマット名が指定された場合
     */
    public static Format fromString(final String name) {
        if (name == null || name.isBlank()) {
            throw new CmdLineArgmentException("Format name cannot be null or empty");
        }
        
        // 名前を正規化
        final String normalizedName = name.trim().toLowerCase(Locale.ENGLISH);
        
        // Java 21のOptionalのパターンマッチングを使用
        return Arrays.stream(values())
                .filter(format -> 
                    format.name().toLowerCase(Locale.ENGLISH).equals(normalizedName) || 
                    format.getAlias().equalsIgnoreCase(normalizedName))
                .findFirst()
                .orElseThrow(() -> 
                    new CmdLineArgmentException("Invalid format: " + name + 
                    ". Valid options are: " + Arrays.toString(values())));
    }
    
    /**
     * 指定されたフォーマットがJSONフォーマットかどうかを確認します。
     * Java 21のswitch式のパターンマッチングを使用します。
     *
     * @param format 確認するフォーマット
     * @return JSONフォーマットの場合はtrue
     */
    public static boolean isJsonFormat(final Format format) {
        return switch(format) {
            case JSON -> true;
            default -> false;
        };
    }
    
    /**
     * 指定されたフォーマットがコンソール出力用かどうかを確認します。
     *
     * @param format 確認するフォーマット
     * @return コンソール出力用の場合はtrue
     */
    public static boolean isConsoleOutput(final Format format) {
        return switch(format) {
            case CONSOLE -> true;
            default -> false;
        };
    }
    
    /**
     * フォーマットの文字列表現を返します。
     * 
     * @return フォーマットの文字列表現
     */
    @Override
    public String toString() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
