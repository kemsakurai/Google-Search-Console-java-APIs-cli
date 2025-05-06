package xyz.monotalk.google.webmaster.cli.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * API応答を表すレコードクラス。
 * Java 21のレコード機能を使用して、不変のデータ構造をコンパクトに表現します。
 * このレコードはAPI呼び出しのメタデータと結果を保持します。
 *
 * @param status       応答ステータス（成功、失敗など）
 * @param statusCode   HTTPステータスコード
 * @param timestamp    応答タイムスタンプ
 * @param data         応答データ（存在する場合）
 * @param headers      レスポンスヘッダー
 * @param errorMessage エラーメッセージ（存在する場合）
 * @param <T>          応答データの型
 */
public record ApiResponseRecord<T>(
    ResponseStatus status,
    int statusCode,
    LocalDateTime timestamp,
    T data,
    Map<String, List<String>> headers,
    String errorMessage) {

    /**
     * API応答のステータスを表す列挙型。
     */
    public enum ResponseStatus {
    SUCCESS,
    ERROR,
    REDIRECT,
    NOT_FOUND,
    UNAUTHORIZED,
    RATE_LIMITED
    }

    /**
     * API応答レコードのビルダークラス。
     *
     * @param <T> 応答データの型
     */
    public static class Builder<T> {
    private ResponseStatus status = ResponseStatus.SUCCESS;
    private int statusCode = 200;
    private LocalDateTime timestamp = LocalDateTime.now();
    private T data = null;
    private Map<String, List<String>> headers = Map.of();
    private String errorMessage = null;

    /**
     * ステータスを設定します。
     *
     * @param status 応答ステータス
     * @return このビルダー
     */
    public Builder<T> status(ResponseStatus status) {
        this.status = status;
        return this;
    }

    /**
     * HTTPステータスコードを設定します。
     *
     * @param statusCode HTTPステータスコード
     * @return このビルダー
     */
    public Builder<T> statusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    /**
     * タイムスタンプを設定します。
     *
     * @param timestamp 応答タイムスタンプ
     * @return このビルダー
     */
    public Builder<T> timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * 応答データを設定します。
     *
     * @param data 応答データ
     * @return このビルダー
     */
    public Builder<T> data(T data) {
        this.data = data;
        return this;
    }

    /**
     * レスポンスヘッダーを設定します。
     *
     * @param headers レスポンスヘッダー
     * @return このビルダー
     */
    public Builder<T> headers(Map<String, List<String>> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * エラーメッセージを設定します。
     *
     * @param errorMessage エラーメッセージ
     * @return このビルダー
     */
    public Builder<T> errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * ApiResponseRecordを構築します。
     *
     * @return 構築されたApiResponseRecord
     */
    public ApiResponseRecord<T> build() {
        return new ApiResponseRecord<>(status, statusCode, timestamp, data, headers, errorMessage);
    }
    }

    /**
     * 新しいビルダーインスタンスを作成します。
     *
     * @param <T> 応答データの型
     * @return 新しいビルダーインスタンス
     */
    public static <T> Builder<T> builder() {
    return new Builder<>();
    }

    /**
     * 成功応答を作成するための簡易ファクトリメソッド。
     *
     * @param data 応答データ
     * @param <T>  データの型
     * @return 成功応答
     */
    public static <T> ApiResponseRecord<T> success(T data) {
    return new Builder<T>()
        .status(ResponseStatus.SUCCESS)
        .statusCode(200)
        .data(data)
        .build();
    }

    /**
     * エラー応答を作成するための簡易ファクトリメソッド。
     *
     * @param errorMessage エラーメッセージ
     * @param statusCode   HTTPステータスコード
     * @param <T>          データの型
     * @return エラー応答
     */
    public static <T> ApiResponseRecord<T> error(String errorMessage, int statusCode) {
    return new Builder<T>()
        .status(ResponseStatus.ERROR)
        .statusCode(statusCode)
        .errorMessage(errorMessage)
        .build();
    }

    /**
     * 応答が成功かどうかを返します。
     *
     * @return 成功の場合はtrue
     */
    public boolean isSuccess() {
    return status == ResponseStatus.SUCCESS && statusCode >= 200 && statusCode < 300;
    }

    /**
     * エラーメッセージを取得します（存在する場合）。
     *
     * @return エラーメッセージを含むOptional
     */
    public Optional<String> getErrorMessage() {
    return Optional.ofNullable(errorMessage);
    }

    /**
     * データを取得します（存在する場合）。
     *
     * @return データを含むOptional
     */
    public Optional<T> getData() {
    return Optional.ofNullable(data);
    }
}