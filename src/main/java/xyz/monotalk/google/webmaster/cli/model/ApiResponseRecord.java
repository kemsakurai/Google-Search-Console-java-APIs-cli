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
 * @param code   HTTPステータスコード
 * @param time    応答タイムスタンプ
 * @param data         応答データ（存在する場合）
 * @param hdrs      レスポンスヘッダー
 * @param err エラーメッセージ（存在する場合）
 * @param <T>          応答データの型
 */
public record ApiResponseRecord<T>(
    ResponseStatus status,
    int code,
    LocalDateTime time,
    T data,
    Map<String, List<String>> hdrs,
    String err) {

    /**
     * API応答レコードのコンストラクタ。
     *
     * @param status 応答ステータス
     * @param code HTTPステータスコード
     * @param time 応答タイムスタンプ
     * @param data 応答データ
     * @param hdrs レスポンスヘッダー
     * @param err エラーメッセージ
     */
    public ApiResponseRecord(final ResponseStatus status, final int code, 
                             final LocalDateTime time, final T data, 
                             final Map<String, List<String>> hdrs, final String err) {
        this.status = status;
        this.code = code;
        this.time = time;
        this.data = data;
        this.hdrs = hdrs != null ? Map.copyOf(hdrs) : Map.of();
        this.err = err;
    }

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
        /**
         * 応答ステータスを表します。
         */
        private ResponseStatus responseStatus;

        /**
         * HTTPステータスコードを表します。
         */
        private int httpStatusCode;

        /**
         * 応答のタイムスタンプを表します。
         */
        private LocalDateTime responseTimestamp;

        /**
         * 応答データを格納します。
         */
        private T responseData;

        /**
         * レスポンスヘッダーを格納します。
         */
        private Map<String, List<String>> responseHeaders;

        /**
         * エラーメッセージを格納します。
         */
        private String errorMsg;

        /**
         * ステータスを設定します。
         *
         * @param responseStatus 応答ステータス
         * @return このビルダー
         */
        public Builder<T> status(final ResponseStatus responseStatus) {
            this.responseStatus = responseStatus;
            return this;
        }

        /**
         * HTTPステータスコードを設定します。
         *
         * @param httpStatusCode HTTPステータスコード
         * @return このビルダー
         */
        public Builder<T> statusCode(final int httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
            return this;
        }

        /**
         * タイムスタンプを設定します。
         *
         * @param responseTimestamp 応答タイムスタンプ
         * @return このビルダー
         */
        public Builder<T> timestamp(final LocalDateTime responseTimestamp) {
            this.responseTimestamp = responseTimestamp;
            return this;
        }

        /**
         * 応答データを設定します。
         *
         * @param responseData 応答データ
         * @return このビルダー
         */
        public Builder<T> data(final T responseData) {
            this.responseData = responseData;
            return this;
        }

        /**
         * レスポンスヘッダーを設定します。
         *
         * @param responseHeaders レスポンスヘッダー
         * @return このビルダー
         */
        public Builder<T> headers(final Map<String, List<String>> responseHeaders) {
            this.responseHeaders = responseHeaders != null ? Map.copyOf(responseHeaders) : Map.of();
            return this;
        }

        /**
         * エラーメッセージを設定します。
         *
         * @param errorMessage エラーメッセージ
         * @return このビルダー
         */
        public Builder<T> errorMessage(final String errorMessage) {
            this.errorMsg = errorMessage;
            return this;
        }

        /**
         * ApiResponseRecordを構築します。
         *
         * @return 構築されたApiResponseRecord
         */
        public ApiResponseRecord<T> build() {
            return new ApiResponseRecord<>(
                responseStatus, 
                httpStatusCode, 
                responseTimestamp, 
                responseData, 
                responseHeaders, 
                errorMsg
            );
        }

        /**
         * 応答ステータスを設定します。
         *
         * @param responseStatus 応答ステータス
         */
        public void setResponseStatus(final ResponseStatus responseStatus) {
            this.responseStatus = responseStatus;
        }

        /**
         * HTTPステータスコードを設定します。
         *
         * @param httpStatusCode HTTPステータスコード
         */
        public void setHttpStatusCode(final int httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
        }

        /**
         * 応答のタイムスタンプを設定します。
         *
         * @param responseTimestamp 応答のタイムスタンプ
         */
        public void setResponseTimestamp(final LocalDateTime responseTimestamp) {
            this.responseTimestamp = responseTimestamp;
        }

        /**
         * 応答データを設定します。
         *
         * @param responseData 応答データ
         */
        public void setResponseData(final T responseData) {
            this.responseData = responseData;
        }

        /**
         * レスポンスヘッダーを設定します。
         *
         * @param responseHeaders レスポンスヘッダー
         */
        public void setResponseHeaders(final Map<String, List<String>> responseHeaders) {
            this.responseHeaders = responseHeaders != null ? Map.copyOf(responseHeaders) : Map.of();
        }

        /**
         * エラーメッセージを設定します。
         */
        public void setErrorMessage(final String errorMessage) {
            this.errorMsg = errorMessage;
        }
    }

    /**
     * API応答レコードのビルダーを作成します。
     *
     * @param <T> 応答データの型
     * @return 新しいビルダーインスタンス
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * 成功応答を作成します。
     *
     * @param data 応答データ
     * @param <T> データの型
     * @return 成功応答
     */
    public static <T> ApiResponseRecord<T> success(final T data) {
        return new Builder<T>()
            .status(ResponseStatus.SUCCESS)
            .statusCode(200)
            .data(data)
            .build();
    }

    /**
     * エラー応答を作成します。
     *
     * @param err エラーメッセージ
     * @param code HTTPステータスコード
     * @param <T> データの型
     * @return エラー応答
     */
    public static <T> ApiResponseRecord<T> error(final String err, final int code) {
        return new Builder<T>()
            .status(ResponseStatus.ERROR)
            .statusCode(code)
            .errorMessage(err)
            .build();
    }

    /**
     * 応答が成功かどうかを返します。
     *
     * @return 成功の場合はtrue
     */
    public boolean isSuccess() {
        return status == ResponseStatus.SUCCESS && code >= 200 && code < 300;
    }

    /**
     * エラーメッセージを取得します（存在する場合）。
     *
     * @return エラーメッセージを含むOptional
     */
    public Optional<String> getErrorMessage() {
        return Optional.ofNullable(err);
    }

    /**
     * データを取得します（存在する場合）。
     *
     * @return データを含むOptional
     */
    public Optional<T> getData() {
        return Optional.ofNullable(data);
    }

    /**
     * レスポンスヘッダーを取得します。
     *
     * @return レスポンスヘッダーのマップ
     */
    public Map<String, List<String>> getHeaders() {
        return hdrs != null ? Map.copyOf(hdrs) : Map.of();
    }

    /**
     * HTTPステータスコードを取得します。
     *
     * @return HTTPステータスコード
     */
    public int getStatusCode() {
        return code;
    }

    /**
     * 応答のタイムスタンプを取得します。
     *
     * @return 応答のタイムスタンプ
     */
    public LocalDateTime getTimestamp() {
        return time;
    }

    /**
     * 応答ステータスを取得します。
     *
     * @return 応答ステータス
     */
    public ResponseStatus getStatus() {
        return status;
    }

    /**
     * API応答レコードの詳細を取得します。
     *
     * @return 応答の詳細情報
     */
    public String getDetails() {
        return "Status: " + status + ", Code: " + code + ", Time: " + time;
    }
}