## CheckStyle「'java.util.Map' のインポート文が辞書式順序になっていません。」修正サンプル  
### com, org などを含み、グループ間に改行を入れない場合

**修正前（辞書式順序になっていない例）**

```java
import com.example.util.Helper;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;
import javax.swing.JButton;
import org.apache.logging.log4j.Logger;
import com.example.App;
import java.util.List;
```

**修正後（グループ間の改行なし・辞書式順序）**

```java
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import com.example.App;
import com.example.util.Helper;
```

---

### ポイント

- インポート文は「java → javax → org → com」のグループ順に並べる
- 各グループ内もアルファベット順（辞書式順）にする
- グループ間に空行（改行）は入れない

このように並べることで、CheckStyleの「インポート文が辞書式順序になっていません」という警告を解消できます。

## CheckStyleのCustomImportOrder設定例に準拠したインポート例

この設定に従うと、インポートは以下のルールで並びます。

- 1グループ目：staticインポート（アルファベット順）
- 空行
- 2グループ目：サードパーティパッケージ（アルファベット順）
- 各グループ間は空行

### 例：正しいインポート順

```java
package com.example.project;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
```

**ポイント**

- `import static ...` で始まるstaticインポートが最初に、アルファベット順で並びます。
- 空行でグループを分けます。
- その後、`org.apache.commons.io.FileUtils` などのサードパーティパッケージ（`org.`, `com.` など）がアルファベット順で並びます。
- 標準Javaパッケージ（`java.*`, `javax.*`）のインポートはこの設定ではグループ指定されていないため、通常はエラーや警告になります。**この設定では標準パッケージのインポートは許可されていません**[1][5]。

### NG例（違反例）

```java
package com.example.project;

import org.apache.commons.io.FileUtils;
import static java.lang.Math.PI;
import org.slf4j.Logger;
import static java.lang.Math.abs;
```

- staticインポートが先頭でない
- グループ間に空行がない
- 各グループ内でアルファベット順になっていない

---

### 参考：設定の意味

- `sortImportsInGroupAlphabetically="true"`：各グループ内でインポートをアルファベット順に並べる
- `separateLineBetweenGroups="true"`：グループ間に空行を挿入
- `customImportOrderRules="STATIC###THIRD_PARTY_PACKAGE"`：staticインポート→サードパーティインポートの順
- `tokens="IMPORT, STATIC_IMPORT, PACKAGE_DEF"`：パッケージ宣言とインポート文を対象

---

### 補足

- 標準パッケージ（`java.*`, `javax.*`）のインポートを許可したい場合は、`customImportOrderRules`に`STANDARD_JAVA_PACKAGE`を追加してください[1][5]。

---
