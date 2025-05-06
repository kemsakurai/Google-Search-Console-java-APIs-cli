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

---
Perplexity の Eliot より: pplx.ai/share