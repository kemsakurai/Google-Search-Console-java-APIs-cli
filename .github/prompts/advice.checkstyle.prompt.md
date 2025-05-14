CheckStyleの警告対応時は、以下のアドバイスを参考にしてください。

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

## AbbreviationAsWordInNameの修正方法

「'testExecuteApiIOException' 中の略語が含む大文字の数は '1' 以下にしてください。」という指摘は、**メソッド名や変数名に含まれる略語（例: API, IO, HTTP など）の大文字が多すぎる**ため、命名規則に従って**略語内の大文字を1つ以下に抑える**よう修正する必要がある、という意味です。

### Javaの命名規則と略語の扱い

- Javaのメソッド名は**camelCase（キャメルケース）**で記述し、先頭は小文字、単語の区切りは大文字にします。
- 略語や頭字語（API, IO, HTTPなど）は**すべて大文字で書かず、先頭のみ大文字にする**のが推奨されます（例: Api, Io, Http）。

### 修正例

#### Before（修正前）

```java
testExecuteApiIOException
```
この場合、「API」と「IO」がそれぞれ大文字2文字ずつ含まれています。

#### After（修正後）

```java
testExecuteApiIoException
```
- 「API」→「Api」
- 「IO」→「Io」

このように、**略語も通常の単語と同じく、先頭のみ大文字にして他は小文字**にします。

### 一般的な命名例

| 略語 | NG例（大文字が多い） | OK例（大文字1つ） |
|---|---|---|
| API | getAPIResponse | getApiResponse |
| IO  | handleIOException | handleIoException |
| URL | fetchURLData | fetchUrlData |

### まとめ

- **略語もcamelCaseで書き、先頭のみ大文字にする**
- 「testExecuteApiIOException」は「testExecuteApiIoException」と修正する
- こうすることで、略語内の大文字が1つ以下となり、命名規則に合致します

## 120文字超過のCheckStyle警告への対応方法

**CheckStyleの「120文字を超えている」警告**は、1行のコードが設定された最大文字数（多くの場合120文字）を超えた場合に発生します[1][5]。

---

### 修正方法

**1行が120文字を超えないように、適切な位置で改行**してください。  
Javaのメソッド宣言の場合、以下のように各引数やthrows句ごとに改行するのが一般的です。

#### 修正前

```java
public void testWriteJsonIoエラー発生時に例外スロー() throws CmdLineArgmentException, CommandLineInputOutputException, IOException {
```

#### 修正後（例）

```java
public void testWriteJsonIoエラー発生時に例外スロー()
        throws CmdLineArgmentException,
               CommandLineInputOutputException,
               IOException {
```

- メソッド名の後で改行し、`throws` 句の例外ごとに改行します。
- インデントは4スペースまたはプロジェクトの規約に合わせてください。

---

### ポイント

- **可読性を保ちながら**、120文字以内に収めるように改行します。
- 例外が多い場合は、1つずつ縦に並べると見やすくなります。
- ほかの長い行（コメントや文字列リテラルなど）も同様に改行して対応します。

---

### 備考

- タブ幅の設定などによってCheckStyleのカウントが異なる場合があるため、**エディタのタブ幅とCheckStyleの設定を揃える**ことも重要です。
- 設定で最大文字数を変更したい場合は、CheckStyleの`LineLength`プロパティで調整可能です。

---

このように**適切な位置で改行**することで、CheckStyleの警告を解消できます。
