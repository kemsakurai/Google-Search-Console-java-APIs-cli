# アクティブコンテキスト

## 現在の作業の焦点
- PMD違反修正とルールファイルの変更
- CLIコマンドテストの強化
- 例外処理の改善

## 最近の変更
- `CommandLineInputOutputException`クラスを導入し、コマンドライン入出力処理に関する例外を統一的に扱えるようにしました
- `FQCNBeanNameGenerator`クラスを追加し、SpringコンテキストでBean命名を改善しました
- サイトマップ、サイト、URLクロールエラー関連のListCommandTestを改善しました
  - ResponseWriterのモックを追加
  - 例外処理を改善
  - テストメソッド名を明確化
- PMDルールファイルを更新し、例外処理に関するルールを調整しました

## 次のステップ
- 修正したコードに対してPMDを実行し、違反が解消されたことを確認する
- 新たに追加したクラスのテストを作成する
- アプリケーション全体のテストを実行し、機能への影響がないことを確認する
- エラー処理をさらに強化し、より詳細なエラーメッセージを提供する

## アクティブな決定事項と考慮事項
- 例外クラスは用途に応じて適切に設計・分類する
  - `CmdLineArgmentException`: コマンドライン引数に関する例外
  - `CmdLineIOException`: 入出力処理に関する例外
  - `CommandLineInputOutputException`: より一般的な入出力処理例外
- テストクラスはGiven-When-Thenパターンを採用し、テストメソッド名は`test[テスト対象機能]_[テスト条件]_[期待結果]`の形式で命名する
- PMDルールは必要に応じて除外またはカスタマイズするが、コードの品質維持に必要なルールは保持する

## Java 8からJava 21へのアップグレード計画

### 概要
Google Search Console Java API CLI プロジェクトをJava 8からJava 21へ安全にマイグレーションし、保守性・セキュリティ・パフォーマンスを向上させる計画です。

### 移行フェーズ

#### フェーズ1：環境準備と互換性評価
1. **Java 21のインストールと構成**
   - JDK 21のインストール（Temurin OpenJDK 21推奨）
   - 開発環境の設定更新

2. **Gradleの設定更新**
   - Java 21との互換性のためのGradleバージョン更新（8.5以上推奨）
   - ビルドスクリプトのJava 21対応

3. **コードベース分析**
   - 非推奨APIの使用確認
   - モジュールパスの互換性確認
   - JDKの内部APIへの依存確認

#### フェーズ2：依存関係とライブラリのアップデート
1. **Spring Bootのアップグレード**
   - Spring Boot 3.2.x（Java 21対応）への更新
   - 関連設定とアノテーションの調整

2. **Google API関連ライブラリの更新**
   - 最新バージョンへのアップグレード
   - 互換性の検証
   - APIの変更に対する対応

3. **テストライブラリの更新**
   - JUnit 5への移行検討（オプション）
   - Mockitoの最新版対応

#### フェーズ3：コード修正とリファクタリング
1. **言語機能の最適化**
   - Java 21の新機能活用の検討
   - レコードクラスの適用（データ転送オブジェクトなど）
   - テキストブロックの導入（JSONテンプレートの改善）
   - パターンマッチングの導入（型チェックの簡略化）
   - シールドクラスの検討（APIモデル定義）

2. **非推奨API対応**
   - 非推奨メソッド/クラスの置き換え
   - 代替APIへの移行
   - 内部APIへの依存排除

3. **パフォーマンス最適化**
   - 仮想スレッド活用の検討
   - 並行処理の最適化
   - メモリ管理の最適化

#### フェーズ4：テスト・検証
1. **テストスイートの実行・修正**
   - 既存テストの修正・拡充
   - 新機能のテスト追加
   - API変更の検証

2. **エンドツーエンドテスト**
   - CLIコマンド全体の動作確認
   - Google APIとの連携確認
   - エラー処理の検証

3. **パフォーマンス評価**
   - メモリ使用量の評価
   - 応答時間の比較
   - スケーラビリティの検証

#### フェーズ5：CI/CD更新と文書化
1. **CI/CD設定の更新**
   - GitHub Actions設定の更新
   - Java 21環境のセットアップ
   - ビルド・テスト設定の更新

2. **静的解析ツールの更新**
   - PMD、Checkstyleの更新とルールセット調整
   - SpotBugsのJava 21対応確認
   - コード品質測定の再構築

3. **ドキュメントの更新**
   - システム要件の更新（Java 21必須の明記）
   - 新機能・変更点のドキュメント化
   - 開発環境セットアップ手順の更新

### リスクと軽減策

1. **互換性の問題**
   - **リスク**: Java 8と21の間のAPIやクラスの変更による問題
   - **対策**: 徹底的なテスト、段階的な導入、互換性ライブラリの検討

2. **外部依存関係の互換性**
   - **リスク**: 一部ライブラリがJava 21に対応していない可能性
   - **対策**: 事前の互換性確認、最新バージョンへの更新、代替ライブラリの検討

3. **パフォーマンスの変化**
   - **リスク**: GCの動作やJIT最適化の変化による予期せぬパフォーマンスへの影響
   - **対策**: ベンチマーク作成、パフォーマンス測定、チューニングの実施

4. **テストの課題**
   - **リスク**: アップグレードによるテストの失敗
   - **対策**: 段階的なテスト実行、失敗の体系的な修正、テストカバレッジの維持

### マイグレーションチェックリスト

- [ ] Java 21 JDKインストール
- [ ] Gradleの更新と設定変更
- [ ] Spring Bootの更新（2.7.x → 3.2.x）
- [ ] Google API Clientライブラリの更新
- [ ] 非推奨APIの特定と置換
- [ ] テストコードの更新
- [ ] CI/CD設定の更新
- [ ] 静的解析ツールの設定更新
- [ ] ドキュメントの更新
- [ ] 全機能の動作確認

### 主要な技術的変更点

1. **言語レベルの変更**
   ```gradle
   java {
       sourceCompatibility = JavaVersion.VERSION_21
       targetCompatibility = JavaVersion.VERSION_21
   }
   ```

2. **Spring Boot 3.xへの移行**
   ```gradle
   plugins {
       id 'org.springframework.boot' version '3.2.3'
       id 'io.spring.dependency-management' version '1.1.4'
   }
   ```

3. **Google APIライブラリの更新**
   ```gradle
   implementation 'com.google.api-client:google-api-client:2.2.0'
   implementation 'com.google.oauth-client:google-oauth-client:1.34.1'
   implementation 'com.google.http-client:google-http-client:1.43.1'
   implementation 'com.google.apis:google-api-services-webmasters:v3-rev20230829-2.0.0'
   ```

4. **テスト環境の更新（オプション）**
   ```gradle
   testImplementation 'org.junit.jupiter:junit-jupiter-api:5.10.1'
   testImplementation 'org.junit.jupiter:junit-jupiter-engine:5.10.1'
   testImplementation 'org.mockito:mockito-core:5.7.0'
   testImplementation 'org.mockito:mockito-junit-jupiter:5.7.0'
   ```