# 進捗状況

## 完了した作業
- `CommandLineInputOutputException`クラスの追加
- `FQCNBeanNameGenerator`（完全修飾クラス名Bean名ジェネレータ）の追加
- テストクラスの改善
  - サイトマップのListCommandTestを修正
  - サイト一覧のListCommandTestを修正
  - URLクロールエラーサンプルのListCommandTestを修正
- PMDルールファイル（ruleset.xml）の更新
  - 例外処理に関するルールの最適化
  - テストクラス用の適切な除外設定の追加
  - 命名規則のカスタマイズ

## 進行中の作業
- 修正したコードに対するテスト実行
- PMD違反の確認と解消

## 今後の予定
- 新たに追加したクラスのテスト作成
- 他のテストクラスの見直しと改善
- エラー処理の継続的な強化
- コード品質向上のための追加改善

## 既知の問題
- 一部のエラーメッセージがより具体的になる余地がある
- テストカバレッジの向上が必要なエリアがある可能性

## Java 8からJava 21へのアップグレード

### 2025-05-05 計画策定
- Java 8からJava 21へのアップグレード計画を策定
- アップグレードの各フェーズと実行ステップの詳細化
- 主要なリスクと軽減策の特定
- 必要なライブラリアップデートの調査と特定

#### 主な発見事項
- Spring Bootの更新（2.7.x → 3.2.x）が必要
  - Jakarta EEへの移行が伴う可能性あり
- Google API関連ライブラリの大幅なバージョンアップが必要
  - 現在: API Client 1.22.0 → 最新: 2.2.0
  - 現在: OAuth Client 1.22.0 → 最新: 1.34.1
  - 現在: HTTP Client 1.22.0 → 最新: 1.43.1
  - 現在: Webmasters API v3-rev22-1.22.0 → 最新: v3-rev20230829-2.0.0

#### 懸念事項
- Spring Boot 2から3への移行に伴う非互換性
- Google API Client 1.x系から2.x系へのアップグレードによるAPI変更
- テストコードの互換性維持

#### 次のステップ
- 開発環境にJava 21をインストール
- Gradleの更新と設定変更
- 依存関係のアップデートと互換性テスト

### 2025-05-06 環境準備とライブラリ更新
- JDK 21のインストールが完了（OpenJDK 21.0.7）
- システム環境設定の更新が完了
- Gradleは既にバージョン8.14で最新状態（Java 21対応済み）
- build.gradleファイルの更新：
  - Javaバージョン設定を8から21に変更
  - Spring Bootを3.2.3に更新
  - Google API依存関係を最新バージョンに更新

#### 互換性の問題点
- Google Search Console API（旧Webmasters API）のバージョンアップによる互換性の問題を発見
  - 最新版（v3-rev20190428-1.32.1）でいくつかのAPIモデルクラスが見つからない：
    - `UrlCrawlErrorsSample`
    - `UrlCrawlErrorsSamplesListResponse`
    - `UrlCrawlErrorsCountsQueryResponse`
  - 原因：Google API Clientの1.x系から2.x系への移行と、APIモデルの変更
  - 次のステップとして、これらのクラスの代替実装または修正が必要

#### 次のタスク
- 修正が必要なソースコードの詳細確認
- Google Search Console APIの新しいクラス構造の調査
- 互換性問題の解決方針策定

### 2025-05-07 API互換性調査
- Google Search Console API（旧Webmasters API）の互換性問題を詳細調査
  - URL Crawl Errors関連のAPIクラスが最新バージョンでは利用不可
  - 具体的な非互換クラス：`UrlCrawlErrorsSample`、`UrlCrawlErrorsSamplesListResponse`、`UrlCrawlErrorsCountsQueryResponse`
  - APIエンドポイントのドキュメントも404エラーを返すように（廃止または大幅変更の可能性）

#### 対応選択肢
1. **APIバージョンのダウングレード**
   - 最新のJava 21で古いバージョンのAPIを使用する方法を検討
   - Google API Clientの2.x系と古いWebmasters APIの互換性確保

2. **コードのリファクタリング**
   - URL Crawl Errors関連の機能を使用しない設計に変更
   - 他の利用可能なAPIで代替方法を検討

3. **Google Search API v1への移行検討**
   - 新しいSearch Console APIへの完全移行
   - 長期的な互換性のための抜本的な変更

#### 次のタスク
- Google API Client 2.x系での古いWebmasters APIの互換性テスト
- 代替APIや回避策の技術検証
- 最適な対応方針の決定と実装計画の策定

### ルール遵守状況
- メモリーバンクの適切な更新：調査結果と対応方針を記録
- 日本語での説明：すべての文書を日本語で記載
- 技術調査の徹底：API変更の詳細を調査し、互換性問題の根本原因を特定
- 複数の対応選択肢の提示：状況に応じた適切な方針選択肢を提示