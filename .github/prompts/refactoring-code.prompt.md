# リファクタリングガイド：Javaプログラムの体質改善

## 目次

1. [リファクタリングの概要](#リファクタリングの概要)
2. [コードの不吉な臭い](#コードの不吉な臭い)
3. [基本的なリファクタリング手法](#基本的なリファクタリング手法)
4. [パターン指向リファクタリング](#パターン指向リファクタリング)
5. [テスト駆動リファクタリング](#テスト駆動リファクタリング)
6. [リファクタリングの実践手順](#リファクタリングの実践手順)

## リファクタリングの概要

リファクタリングとは、ソフトウェアの外部的な振る舞いを保ったままで、内部の構造を改善する作業を指します[1][8]。その目的は、コードの可読性向上、保守性の向上、拡張性の向上、バグの発見しやすさの向上などです。

### なぜリファクタリングが必要か

- コードの理解・修正が容易になる
- バグを見つけやすくなる
- プログラミングの速度が向上する
- 技術的負債を減らせる
- ソフトウェアの設計を改善できる

リファクタリングは一時的に開発速度を落とすように見えますが、長期的には開発速度を向上させる投資です[3]。

## コードの不吉な臭い

コードの臭い（Code Smells）とは、より深刻な問題を示唆する可能性のあるコード上の兆候です[17]。以下に主要なコードの臭いを示します：

### クラスレベルの臭い
- **重複コード（Duplicated Code）** - 同じコードが複数の場所に存在する[11][17]
- **大きなクラス（Large Class）** - 一つのクラスが多すぎる責務を持っている[11][14]
- **データクラス（Data Class）** - データだけを持ち、振る舞いを持たないクラス[11]
- **拒絶された遺産（Refused Bequest）** - 継承したメソッドを適切に利用していない[17]

### メソッドレベルの臭い
- **長いメソッド（Long Method/Function）** - 一つのメソッドが長すぎる[11][14]
- **条件の複雑さ（Conditional Complexity）** - 複雑な条件分岐がある[3]
- **プリミティブへの執着（Primitive Obsession）** - オブジェクトではなくプリミティブ型を多用する[11][14]
- **過剰なパラメータ（Too Many Parameters）** - メソッドのパラメータが多すぎる[17]

### アプリケーションレベルの臭い
- **発散する変更（Divergent Change）** - 一つの変更が多くの場所に影響する[11][14]
- **散弾銃手術（Shotgun Surgery）** - 一つの変更を複数のクラスに適用する必要がある[17]
- **機能の羨望（Feature Envy）** - メソッドが他のクラスのデータに過度に関心を持つ[11]
- **不適切な関係（Inappropriate Intimacy）** - クラス間の結合が強すぎる[3]

## 基本的なリファクタリング手法

### メソッドの構成
- **メソッドの抽出（Extract Method）** - コードの一部を別のメソッドに切り出す[3]
- **メソッドのインライン化（Inline Method）** - メソッド呼び出しをメソッド本体で置き換える[3]
- **一時変数のインライン化（Inline Temp）** - 一時変数を式で置き換える[3]
- **説明用変数の導入（Introduce Explaining Variable）** - 複雑な式を変数に置き換える[3]

### オブジェクト間での特性の移動
- **メソッドの移動（Move Method）** - メソッドを別のクラスに移動する[3]
- **フィールドの移動（Move Field）** - フィールドを別のクラスに移動する[3]
- **クラスの抽出（Extract Class）** - 一つのクラスから新しいクラスを作成する[3]
- **クラスのインライン化（Inline Class）** - クラスを他のクラスに統合する[3]

### データの再編成
- **自己カプセル化フィールド（Self Encapsulate Field）** - フィールドへの直接アクセスをゲッター/セッターに置き換える[3]
- **オブジェクトによるデータ値の置き換え（Replace Data Value with Object）** - データ値をオブジェクトに置き換える[3]
- **シンボリック定数によるマジックナンバーの置き換え（Replace Magic Number with Symbolic Constant）** - マジックナンバーを定数に置き換える[3]

### 条件記述の単純化
- **条件記述の分解（Decompose Conditional）** - 複雑な条件式を分解する[3]
- **ガード節による入れ子条件の置き換え（Replace Nested Conditional with Guard Clauses）** - 入れ子条件をガード節に置き換える[3]
- **ポリモーフィズムによる条件記述の置き換え（Replace Conditional with Polymorphism）** - 条件分岐をポリモーフィズムに置き換える[3]

## パターン指向リファクタリング

パターン指向リファクタリングは、既存のコードにデザインパターンを適用して改善する手法です[5]。

### 主要なパターン指向リファクタリング

#### 生成パターン
- **コンストラクタをCreation Methodsで置き換える（Replace Constructors with Creation Methods）** - コンストラクタをより表現力のあるファクトリメソッドに置き換える[6][9]
- **Factory Methodによるコンストラクタの置き換え（Replace Constructor with Factory Method）** - 適切なオブジェクト生成のためにファクトリメソッドを導入する[4][9]
- **クラスをファクトリでカプセル化（Encapsulate Classes with Factory）** - クラスの生成をファクトリでカプセル化する[6]

#### 単純化パターン
- **条件ディスパッチャをCommandで置き換える（Replace Conditional Dispatcher with Command）** - 条件分岐をCommandパターンに置き換える[6]
- **条件ロジックをStrategyで置き換える（Replace Conditional Logic with Strategy）** - 条件ロジックをStrategyパターンに置き換える[6]
- **状態を変更する条件をStateパターンで置き換える（Replace State-Altering Conditionals with State）** - 状態変更条件をStateパターンに置き換える[6]

#### 汎用化パターン
- **Template Methodの形成（Form Template Method）** - 似たアルゴリズムからTemplate Methodを作成する[3][6]
- **インターフェースの統一（Unify Interfaces）** - 似た操作のインターフェースを統一する[6]
- **Adapterでインターフェースを統一（Unify Interfaces with Adapter）** - 異なるインターフェースをAdapterで統一する[6]

## テスト駆動リファクタリング

リファクタリングを安全に行うためには、適切なテストが不可欠です[1][3]。

### テスト駆動リファクタリングの手順

1. リファクタリングする前に包括的なテストを作成する
2. 小さな変更を行う
3. 各変更後にテストを実行する
4. テストが通ったら次の変更に進む
5. テストに失敗したら直前の変更を元に戻す

### テストの種類

- **単体テスト** - 個々のメソッドやクラスの動作を検証
- **統合テスト** - コンポーネント間の相互作用を検証
- **機能テスト** - 機能全体の動作を検証

## リファクタリングの実践手順

### 1. コードの臭いを特定する

```java
// コードの臭いの例: 長いメソッド
public void processOrder(Order order) {
    // 注文の検証
    if (order.getCustomerId() == null) {
        throw new IllegalArgumentException("顧客IDが必要です");
    }
    if (order.getItems() == null || order.getItems().isEmpty()) {
        throw new IllegalArgumentException("注文アイテムが必要です");
    }
    
    // 在庫の確認
    boolean allItemsAvailable = true;
    for (OrderItem item : order.getItems()) {
        if (inventory.getStock(item.getProductId())  processors;
    
    public TaskService() {
        processors = new HashMap<>();
        processors.put(TaskType.EMAIL, new EmailTaskProcessor());
        processors.put(TaskType.SMS, new SmsTaskProcessor());
        processors.put(TaskType.NOTIFICATION, new NotificationTaskProcessor());
    }
    
    public void processTask(Task task) {
        TaskProcessor processor = processors.get(task.getType());
        if (processor != null) {
            processor.process(task);
        } else {
            throw new UnsupportedTaskTypeException("未対応のタスクタイプです: " + task.getType());
        }
    }
}
```

### リファクタリングのベストプラクティス

1. **小さなステップで進める** - 一度に大きな変更を行わない[2][3]
2. **テストを頻繁に実行する** - 各変更後にテストを実行する[3]
3. **変更をすぐにコミットする** - 小さな変更ごとにコミットする
4. **目的を明確にする** - リファクタリングの目的を明確にする
5. **コードレビューを活用する** - 他の開発者からのフィードバックを得る[3]
6. **リファクタリングとバグ修正を混在させない** - リファクタリングは単独で行う[3]
7. **最もシンプルな解決策を選ぶ** - 過度に複雑な設計を避ける[2][3]

このドキュメントは、マーティン・ファウラーの「リファクタリング」およびジョシュア・ケリーエブスキーの「パターン指向リファクタリング入門」の内容に基づいています。実際のリファクタリングは、対象のコードやプロジェクトの状況に応じて適切に判断してください。

## 参考文献

- マーティン・ファウラー『リファクタリング 既存のコードを安全に改善する（第2版）』オーム社, 2019年
- ジョシュア・ケリーエブスキー『パターン指向リファクタリング入門 ソフトウェア設計を改善する27の作法』日経BP社, 2005年
