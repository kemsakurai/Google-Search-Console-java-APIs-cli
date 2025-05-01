# Google Search Console Java APIs CLI

Google Search Console APIにアクセスするためのコマンドラインツールです。Spring Bootを使用して実装されています。

------------------------------------------------------------
## 特徴

- Google Search Console APIへのアクセスを提供
- Spring Bootベースのコマンドラインインターフェース
- 複数の出力形式（コンソール出力、JSONファイル）に対応
- OAuth2.0認証による安全なAPI呼び出し

------------------------------------------------------------
## 対応API
[Google APIs Explorer](https://developers.google.com/apis-explorer/#p/webmasters/v3/)

|コマンド|説明|
|:---|:----------|
|webmasters.searchanalytics.query|指定したフィルタやパラメータでデータをクエリします。定義した行キーでグループ化された0個以上の行を返します。1日以上の日付範囲を定義する必要があります。日付がグループ化の値の1つである場合、データのない日は結果リストから除外されます。|
|webmasters.sitemaps.delete|サイトからサイトマップを削除します。|
|webmasters.sitemaps.get|特定のサイトマップに関する情報を取得します。|
|webmasters.sitemaps.list|このサイトに送信されたサイトマップエントリを一覧表示します。|
|webmasters.sitemaps.submit|サイトにサイトマップを送信します。|
|webmasters.sites.add|ユーザーのSearch Consoleサイトセットにサイトを追加します。|
|webmasters.sites.delete|ユーザーのSearch Consoleサイトセットからサイトを削除します。|
|webmasters.sites.get|特定のサイトに関する情報を取得します。|
|webmasters.sites.list|ユーザーのSearch Consoleサイトを一覧表示します。|
|webmasters.urlcrawlerrorscounts.query|エラーカテゴリとプラットフォームごとのURLクロールエラー数の時系列を取得します。|
|webmasters.urlcrawlerrorssamples.get|サイトのサンプルURLに関するクロールエラーの詳細を取得します。|
|webmasters.urlcrawlerrorssamples.list|指定されたクロールエラーカテゴリとプラットフォームのサイトのサンプルURLを一覧表示します。|
|webmasters.urlcrawlerrorssamples.markAsFixed|提供されたサイトのサンプルURLを修正済みとしてマークし、サンプルリストから削除します。|

------------------------------------------------------------
## セットアップ

### 前提条件
- Java 8以上
- Gradle 7.0以上

### ビルド方法

```console
./gradlew clean build
```

### Google API認証情報の設定
1. [Google Cloud Console](https://console.cloud.google.com/)でプロジェクトを作成
2. Search Console APIを有効化
3. 認証情報（サービスアカウントキー）を作成しダウンロード
4. ダウンロードしたJSONキーファイルをプロジェクトディレクトリに配置

------------------------------------------------------------
## 使用方法

### 基本的なコマンド形式

```console
java -jar xyz.monotalk.google.webmaster.cli-0.0.1.jar --application.keyFileLocation={認証キーファイルパス} {コマンド名} [オプション]
```

### 出力フォーマット

多くのコマンドでは出力形式を指定できます：

- `-format console`: コンソールに出力（デフォルト）
- `-format json -filePath {ファイル名}`: 結果をJSONファイルとして保存

### 使用例

#### サイトマップ一覧の取得

```console
java -jar xyz.monotalk.google.webmaster.cli-0.0.1.jar --application.keyFileLocation=credentials.json webmasters.sitemaps.list -siteUrl https://www.example.com
```

レスポンス例:
```json
{
  "sitemap" : [ {
    "contents" : [ {
      "indexed" : "6",
      "submitted" : "20",
      "type" : "web"
    } ],
    "errors" : "0",
    "isPending" : false,
    "isSitemapsIndex" : false,
    "lastDownloaded" : "2017-10-19T19:17:56.817Z",
    "lastSubmitted" : "2017-10-04T22:47:39.579Z",
    "path" : "https://www.example.com/sitemap.xml",
    "type" : "sitemap",
    "warnings" : "50"
  } ]
}
```

#### サイトのリスト表示       

```console
java -jar xyz.monotalk.google.webmaster.cli-0.0.1.jar --application.keyFileLocation=service_account.json webmasters.sites.list
```

#### サイトマップの送信

```console
java -jar xyz.monotalk.google.webmaster.cli-0.0.1.jar --application.keyFileLocation=credentials.json webmasters.sitemaps.submit -siteUrl https://www.example.com -feedPath https://www.example.com/sitemap.xml
```

#### 検索アナリティクスデータの取得

```console
java -jar xyz.monotalk.google.webmaster.cli-0.0.1.jar --application.keyFileLocation=credentials.json webmasters.searchanalytics.query -siteUrl https://www.example.com -startDate 2025-01-01 -endDate 2025-04-30
```

#### URLクロールエラー情報の取得

```console
java -jar xyz.monotalk.google.webmaster.cli-0.0.1.jar --application.keyFileLocation=credentials.json webmasters.urlcrawlerrorscounts.query -siteUrl https://www.example.com -format json -filePath errors.json
```

------------------------------------------------------------
## プロジェクト構造

- `xyz.monotalk.google.webmaster.cli` - メインパッケージ
  - `CliApplication.java` - Spring Boot アプリケーションのエントリーポイント
  - `Command.java` - CLI コマンドインターフェース 
  - `WebmastersFactory.java` - Google Webmasters APIクライアント生成ファクトリ
  - `WebmastersCommandRunner.java` - コマンド実行ハンドラ
  - `Format.java` - 出力フォーマット設定用enum
  - `ResponseWriter.java` - レスポンス出力処理
  - サブコマンドは `xyz.monotalk.google.webmaster.cli.subcommands` パッケージ以下

------------------------------------------------------------
## ライセンス

Apache License 2.0

------------------------------------------------------------
## 参考資料

* [Google Search Console API ドキュメント](https://developers.google.com/webmaster-tools/search-console-api-original)
* [Spring Boot ドキュメント](https://docs.spring.io/spring-boot/docs/current/reference/html/)

