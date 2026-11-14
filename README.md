# Spring Pizza Order

Spring BootとOracle AI Database 26ai Freeを使用して構築した、学習用のピザ注文管理Webアプリケーションです。

本アプリケーションは、リレーショナルデータベースを使用した業務システム（OLTP）の実装を学ぶことを目的としています。

また、Qiita Advent Calendar 2026のデータ基盤構築ハンズオンにおいて、データ収集元となる業務システムとして使用しています。

## 1. システム概要

Spring BootによるWebアプリケーションとOracle Databaseを使用し、ピザの商品管理、顧客管理、注文登録、注文履歴の参照などを行います。

注文データはOracle Databaseに保存され、別途構築したデータ基盤により、データ収集・加工・分析の対象となります。

### 主な機能

- ピザ商品の一覧表示・詳細表示
- ピザ商品の登録・編集・有効化・無効化
- 顧客情報の登録・編集・有効化・無効化
- 注文登録・注文内容確認
- 注文履歴・注文詳細の表示
- 注文ステータスの変更
- 注文に関連する支払情報の記録

※ 外部の決済サービスとの実際の連携は実装していません。

## 2. 使用技術

| 分類 | 使用技術 |
| --- | --- |
| 言語 | Java 25 |
| Webフレームワーク | Spring Boot 4.1.1 |
| テンプレートエンジン | Thymeleaf |
| データアクセス | Spring Data JPA / Hibernate |
| 入力検証 | Jakarta Bean Validation |
| データベース | Oracle AI Database 26ai Free |
| ビルドツール | Maven（Maven Wrapper同梱） |
| テスト | JUnit / Mockito |

検証環境では、Windows 11 + WSL2 + Docker Desktop上でOracle AI Database 26ai Freeを実行しています。

## 3. システム構成

```text
Webブラウザ
    |
    v
Spring Boot
    |
    v
Oracle AI Database 26ai Free
    |
    v
Airbyte
    |
    v
SeaweedFS (Parquet)
    |
    v
DuckDB + dbt
    |
    v
Metabase
```

本リポジトリは、このうちSpring Bootアプリケーション部分を管理しています。

## 4. データベース構成

Oracle Databaseの`PIZZA`スキーマに、以下の5テーブルを使用しています。

| テーブル | 用途 |
| --- | --- |
| `CUSTOMERS` | 顧客マスタ |
| `PIZZAS` | ピザ商品マスタ |
| `ORDERS` | 注文ヘッダー |
| `ORDER_ITEMS` | 注文明細 |
| `PAYMENTS` | 注文に関連する支払情報 |

主なリレーションは以下のとおりです。

- `CUSTOMERS` 1 : N `ORDERS`
- `ORDERS` 1 : N `ORDER_ITEMS`
- `PIZZAS` 1 : N `ORDER_ITEMS`
- `ORDERS` 1 : N `PAYMENTS`

`ORDER_ITEMS`には、注文時点の商品名・単価・数量・小計を保存しています。

これにより、注文後にピザ商品マスタの名称や価格が変更されても、過去の注文時点の情報を保持できます。

## 5. 実行環境と起動方法

### 前提条件

- Java 25
- Oracle AI Database 26ai Free
- Oracle Database内の`PIZZA`スキーマと必要なテーブル
- Maven Wrapperを実行できる環境

本リポジトリには、データベースのDDLおよび初期データ投入用スクリプトは含まれていません。

アプリケーションを起動する前に、Oracle Databaseと`PIZZA`スキーマを準備する必要があります。

### データベース接続設定

`src/main/resources/application.properties`では、以下の接続設定を使用しています。

```properties
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/FREEPDB1
spring.datasource.username=pizza
spring.datasource.password=${PIZZA_DB_PASSWORD}
```

検証環境の接続先は以下のとおりです。

| 項目 | 値 |
| --- | --- |
| ホスト | `localhost` |
| ポート | `1521` |
| サービス名 | `FREEPDB1` |
| DBユーザー | `pizza` |
| スキーマ | `PIZZA` |

パスワードはソースコードには保存せず、環境変数`PIZZA_DB_PASSWORD`から取得します。

### Linux / WSLでの環境変数設定

パスワードをシェルのコマンド履歴に直接記録しない場合は、以下のように入力できます。

```bash
read -rsp 'PIZZA_DB_PASSWORD: ' PIZZA_DB_PASSWORD; echo; export PIZZA_DB_PASSWORD
```

### Windows / PowerShellでの環境変数設定

```powershell
$env:PIZZA_DB_PASSWORD = Read-Host "PIZZA_DB_PASSWORD"
```

IntelliJ IDEAを使用する場合は、Run/Debug ConfigurationのEnvironment variablesに`PIZZA_DB_PASSWORD`を設定することもできます。

### アプリケーションの起動

Linux / WSLの場合：

```bash
./mvnw spring-boot:run
```

Windowsの場合：

```powershell
.\mvnw.cmd spring-boot:run
```

起動後、Webブラウザから以下の画面にアクセスできます。

| 画面 | URL |
| --- | --- |
| ピザ商品一覧 | http://localhost:8080/pizzas |
| 注文履歴 | http://localhost:8080/orders |
| ピザ商品管理 | http://localhost:8080/admin/pizzas |
| 顧客管理 | http://localhost:8080/admin/customers |

## 6. テスト

注文ステータス遷移などの単体テストにJUnitとMockitoを使用しています。

```bash
./mvnw test
```

Spring BootのApplicationContextを起動するテストは、実行環境への依存があるため現在は無効化しています。

## 7. データ基盤ハンズオンとの関係

本アプリケーションに登録した注文データを、以下の構成でデータ収集・加工・分析しています。

```text
Oracle
  ↓
Airbyte
  ↓
SeaweedFS（Parquet）
  ↓
DuckDB + dbt
  ↓
Metabase
```

データ基盤側のDocker Compose、dbtモデル、BI環境などは別リポジトリで管理しています。

## 8. 注意事項

本アプリケーションは、個人の学習・技術検証を目的として作成したものです。

以下のような、本番環境で一般に必要となる機能をすべて実装したものではありません。

- ユーザー認証・認可
- 管理画面へのアクセス制御
- 外部決済サービスとの連携
- 本番運用を前提とした監視・ログ管理
- インターネット公開を前提としたセキュリティ対策

そのため、本アプリケーションをそのままインターネットへ公開して使用することは想定していません。

## 9. 関連記事

Qiita Advent Calendar 2026向けに、Windows PC 1台でOracleからBIまでのデータ基盤を構築する過程を紹介する予定です。

記事公開後、こちらにリンクを追加します。

## 10. ライセンス

本プロジェクトはMITライセンスで公開しています。
詳細は[LICENSE](LICENSE)をご覧ください。
