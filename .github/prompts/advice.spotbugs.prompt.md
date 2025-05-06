## EI_EXPOSE_REP警告の修正方法

## 修正方法の基本原則
1. **防御的コピーの作成**：オブジェクトのコピーを返す
2. **不変オブジェクトの使用**：Immutableなデータ構造への変換
3. **現代的なAPIの採用**：`java.time`パッケージや不変コレクションの利用

### ケース1: 配列を扱う場合
```java
// 修正前
public class IntArrayExample {
    private int[] values;
    
    public IntArrayExample(int[] values) {
        this.values = values; // 危険: 元の配列への参照を保持
    }
    
    public int[] getValues() {
        return values; // 危険: 内部配列を直接公開
    }
}

// 修正後
public class SafeIntArrayExample {
    private int[] values;
    
    public SafeIntArrayExample(int[] values) {
        this.values = Arrays.copyOf(values, values.length); // 防御的コピー
    }
    
    public int[] getValues() {
        return Arrays.copyOf(values, values.length); // コピーを返却
    }
}
```

### ケース2: Listを扱う場合（Java 8以降）
```java
// 修正前
public class ListExample {
    private List items;
    
    public ListExample(List items) {
        this.items = items; // 危険: 変更可能なリストを直接保持
    }
    
    public List getItems() {
        return items; // 危険: 内部リストを直接公開
    }
}

// 修正後
public class SafeListExample {
    private List items;
    
    public SafeListExample(List items) {
        this.items = List.copyOf(items); // 不変リストに変換
    }
    
    public List getItems() {
        return new ArrayList<>(items); // 防御的コピー
    }
}
```

### ケース3: レコードクラスでの対応（Java 16+）
```java
// 修正前
public record DangerousRecord(List data) {}

// 修正後
public record SafeRecord(List data) {
    public SafeRecord {
        data = List.copyOf(data); // コンストラクタで不変化
    }
    
    public List data() {
        return new ArrayList<>(data); // コピーを返却
    }
}
```

### ケース4: Dateオブジェクトの扱い
```java
// 修正前（非推奨）
public class DateExample {
    private Date timestamp;
    
    public Date getTimestamp() {
        return timestamp; // 危険: Dateは可変
    }
}

// 修正後（java.time採用）
public class SafeDateTimeExample {
    private LocalDateTime timestamp;
    
    public SafeDateTimeExample(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp; // 安全: LocalDateTimeは不変
    }
}
```

## 高度な対策パターン
**Guavaライブラリを使用した例**:
```java
import com.google.common.collect.ImmutableList;

public class GuavaExample {
    private ImmutableList items;
    
    public GuavaExample(List items) {
        this.items = ImmutableList.copyOf(items);
    }
    
    public ImmutableList getItems() {
        return items; // 安全: 不変コレクション
    }
}
```

## 警告抑制が正当なケース
```java
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP",
    justification = "不変コレクションを使用しているため"
)
public List getImmutableData() {
    return Collections.unmodifiableList(internalList);
}
```

これらの修正パターンを適用することで、内部データの不変性を保証しつつ、オブジェクト指向の原則に沿った安全なコード設計が可能になります[1][3][5]。特に、Java 8以降で導入された`java.time`パッケージや`List.copyOf()`メソッドを活用することで、従来のDateクラスや手動でのコピー実装に比べて簡潔で安全なコードを記述できます[3][5]。

## VA_FORMAT_STRING_USES_NEWLINE の修正サンプル

**基本例**

```java
// 修正前
System.out.printf("Hello, %s!\n", name);

// 修正後
System.out.printf("Hello, %s!%n", name);
```

---

**文字列連結の例**

```java
// 修正前
String message = "User: " + user + "\nScore: " + score + "\n";
System.out.print(message);

// 修正後
String message = String.format("User: %s%nScore: %d%n", user, score);
System.out.print(message);
```

---

**複数行をループで連結する例**

```java
// 修正前
String result = "";
for (String line : lines) {
    result += line + "\n";
}
System.out.print(result);

// 修正後
StringBuilder result = new StringBuilder();
for (String line : lines) {
    result.append(line).append(System.lineSeparator());
}
System.out.print(result);
```

---

**String.formatを使う例**

```java
// 修正前
String formatted = String.format("ID: %d\nName: %s\n", id, name);

// 修正後
String formatted = String.format("ID: %d%nName: %s%n", id, name);
```

---

**Loggerなどでの例**

```java
// 修正前
logger.info("処理開始\nパラメータ: " + params + "\n");

// 修正後
logger.info(String.format("処理開始%nパラメータ: %s%n", params));
```

---

**まとめ**

- 文字列連結やループ内、`String.format`、Loggerなどでも `\n` の代わりに `%n` や `System.lineSeparator()` を使うことで、可搬性と可読性が向上します。
- 書式文字列では `%n`、文字列連結や `append` では `System.lineSeparator()` を使うのが推奨されます。

---
Perplexity の Eliot より: pplx.ai/share