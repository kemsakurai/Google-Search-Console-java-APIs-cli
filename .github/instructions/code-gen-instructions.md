# コード生成指示

## 言語設定
**重要**: すべての出力は日本語で行ってください。コメントやドキュメンテーションも日本語で記述してください。

## コード規約

### 全般
- Java 8の機能を使用してください
- Spring BootとSpring DIパターンに従ってください
- 例外処理は適切にキャッチして処理してください
- すべてのパブリックメソッドにはJavadocを追加してください

### 命名規則
- クラス名: パスカルケース（例: `GoogleApiClient`）
- メソッド名: キャメルケース（例: `executeQuery`）
- 変数名: キャメルケース（例: `siteUrl`）
- 定数: 大文字のスネークケース（例: `DEFAULT_TIMEOUT`）

### 構造
- サブコマンドクラスは `xyz.monotalk.google.webmaster.cli.subcommands` パッケージに配置してください
- すべてのコマンドは `Command` インターフェースを実装してください
- 新しいサブコマンドは適切なサブパッケージに配置してください

## コマンド実装パターン

新しいサブコマンドを実装する場合は:

1. `Command` インターフェースを実装する
2. `execute()` メソッドにコマンドのロジックを実装する
3. `usage()` メソッドにコマンドの説明を記述する
4. 必要なパラメータは `@Option` アノテーションを使って定義する
5. 適切な例外処理を実装する

例:
```java
@Component
public class ExampleCommand implements Command {

    @Autowired private WebmastersFactory factory;
    
    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    private String siteUrl = null;

    @Override
    public void execute() {
        // コマンドのロジック実装
    }

    @Override
    public String usage() {
        return "Example command description";
    }
}
```

## エラー処理
- 具体的なエラーメッセージを提供してください
- カスタム例外 `CmdLineArgmentException` と `CmdLineIOException` を適宜使用してください
- 外部APIの例外は適切にキャッチし、ユーザーフレンドリーなエラーメッセージに変換してください

## API連携
- Google APIとの連携には `WebmastersFactory` を使用してください
- レスポンスの処理には `ResponseWriter` を使用してください

## 出力フォーマット
- 出力フォーマットは `Format` enumを使用して制御してください
- コンソール出力とJSON出力の両方をサポートしてください