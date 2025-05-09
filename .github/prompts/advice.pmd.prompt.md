## PMD警告「CommentRequired: Field comments are required」の修正方法

**概要**  
この警告は、クラスやフィールドなどにJavaDocコメントが不足している場合にPMDが出すものです。特に`CommentRequired`ルールが有効な場合、各フィールドやクラスに説明コメント（JavaDoc）が必要とされます[1][2]。

---

**修正手順**

- **1. 該当箇所を特定する**
  - PMDのレポートやIDEの警告表示から、コメントが不足しているフィールドやクラスを確認します。

- **2. JavaDocコメントを追加する**
  - クラスやフィールドの直前に、JavaDoc形式（`/** ... */`）でコメントを記述します。
  - 例：

    ```java
    /**
     * ユーザーの年齢を表します。
     */
    private int age;
    ```

    ```java
    /**
     * ユーザー情報を管理するクラス。
     */
    public class User {
        // ...
    }
    ```

- **3. 再度ビルド・チェックを行う**
  - コメントを追加後、再度PMDを実行し、警告が消えていることを確認します[1][2]。

---

**補足**

- JavaDocコメントは、`/** ... */`の形式で書きます。通常の`//`や`/* ... */`ではなく、JavaDoc用のコメントを使う必要があります。
- コメントの内容は、フィールドやクラスの役割・用途を簡潔に説明するものにしてください。
- PMDの設定によっては、コメントの有無だけでなく内容や形式にもルールがある場合があります。プロジェクトの`pmd.xml`や`build.gradle`でルールセットを確認しましょう[1][2]。

## PMD "AvoidCatchingGenericException" の修正例

**ルールの概要**  
`AvoidCatchingGenericException`は、`Exception`、`RuntimeException`、`NullPointerException`などの汎用的な例外をcatchすることを避けるべきとするPMDのルールです。これらをcatchすると、本来意図しない例外まで握りつぶしてしまい、バグの発見やデバッグが困難になるためです[2][1]。

**NG例（違反例）**

```java
public void downCastPrimitiveType() {
    try {
        System.out.println(" i [" + i + "]");
    } catch(Exception e) { // NG: 汎用的なExceptionのcatch
        e.printStackTrace();
    } catch(RuntimeException e) { // NG: RuntimeExceptionのcatch
        e.printStackTrace();
    } catch(NullPointerException e) { // NG: NullPointerExceptionのcatch
        e.printStackTrace();
    }
}
```


**OK例（修正例）**

catch句では、発生しうる具体的な例外型を明示的に指定します。  
例えば、`NumberFormatException`や`IOException`など、実際に発生する可能性のある例外のみをcatchします。

```java
public void downCastPrimitiveType() {
    try {
        System.out.println(" i [" + i + "]");
    } catch (NumberFormatException e) { // OK: 具体的な例外型をcatch
        // 適切なエラーハンドリング
        e.printStackTrace();
    } catch (IOException e) { // OK: 具体的な例外型をcatch
        // 適切なエラーハンドリング
        e.printStackTrace();
    }
    // 必要な例外型のみcatchする
}
```


**ポイント**

- どうしても`Exception`などのcatchが必要な場合（例：サードパーティAPIの制約など）は、catchした後に適切なログ出力や再throwを検討してください。ただし、設計上は極力避けるべきです。
- 例外の粒度を細かくし、catchする例外型を限定することで、バグの早期発見や保守性向上につながります。

**まとめ**

「汎用的な例外（Exception/RuntimeException/NullPointerExceptionなど）をcatchしない。catchする場合は、発生しうる具体的な例外型を明示する」ことが、このルールの修正方針です。

## PMD警告「CommentRequired: Public method and constructor comments are required」の修正方法

**概要**  
この警告は、PMDの`CommentRequired`ルールにより、**publicなメソッドやコンストラクタにJavadocコメントが必要**であることを示しています。Javadocコメントが不足している場合、この警告が表示されます。

---

**修正方法**

- **1. 該当するpublicメソッド・コンストラクタを特定する**  
  PMDのレポートやIDEの警告表示から、コメントが不足している箇所を確認します。

- **2. Javadocコメントを追加する**  
  publicメソッドやコンストラクタの直前に、`/** ... */`形式のJavadocコメントを記述します。  
  例：

    ```java
    /**
     * ユーザー名を取得します。
     * @return ユーザー名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * デフォルトコンストラクタ。
     */
    public User() {
        // 初期化処理
    }
    ```

- **3. コメントの内容**  
  - メソッドの場合：処理内容や戻り値、引数の説明を記述します。
  - コンストラクタの場合：用途や特記事項があれば記載します。特に理由がなければ「デフォルトコンストラクタ」など簡単な説明でもOKです。

- **4. 再チェック**  
  コメント追加後、再度PMDを実行し、警告が消えていることを確認します。

## CheckStyle「JavadocParagraph」警告への対応方法

「`<p> タグの次の行には空行を入れてください。 [JavadocParagraph]`」というCheckStyleの警告は、**Javadoc内で段落（`<p>`タグ）を使う際に、適切な空行（空白行）が入っていない**ことが原因です。この警告を解消するには、**各段落の直前に必ず1行の空行を入れる**必要があります。

---

### 正しいJavadocの書き方例

#### NG例（警告が出る）

```java
/**
 * 説明文
 * <p>詳細説明
 */
```

#### OK例（警告が出ない）

```java
/**
 * 説明文
 *
 * <p>詳細説明
 */
```
- `<p>`タグの前に**空行**（`*` のみの行）が必要です。

---

### ポイントまとめ

- **1つ目の段落**の後に `<p>` タグで新しい段落を始める場合、**その直前に必ず空行を入れる**[1][5]。
- `<p>` タグの直後には**空行は不要**。すぐに本文を書き始めてOKです。
- すべての段落間に**空行1行**を入れることで、CheckStyleの警告が消えます。

---

### 参考（公式ドキュメントより）

> There is one blank line between each of two paragraphs. Each paragraph but the first has `<p>` immediately before the first word, with no space ...  
> By default, the check will report a violation if there is no empty line before or whitespace after the `<p>` tag[1][5]。

---

### まとめ

- **`<p>`タグの前に空行を入れる**ことで警告は解消します。
- 例：
    ```java
    /**
     * 概要
     *
     * <p>詳細
     */
    ```

このルールを守ってJavadocを書き直してください。
