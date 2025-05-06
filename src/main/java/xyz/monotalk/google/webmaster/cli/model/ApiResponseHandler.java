package xyz.monotalk.google.webmaster.cli.model;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.json.GenericJson;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;

/**
 * API応答を処理するユーティリティクラス。 
 * Java 21の機能を活用して、Google APIからのレスポンスをApiResponseRecordに変換します。
 */
public final class ApiResponseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseHandler.class);

    private ApiResponseHandler() {
        // インスタンス化を防止するためのプライベートコンストラクタ
    }

    /**
     * HttpResponseをApiResponseRecordに変換します。
     *
     * @param <T> 応答データの型。
     * @param response 処理するHttpResponse。
     * @param responseClass 応答データのクラス。
     * @return 変換されたApiResponseRecord。
     * @throws CommandLineInputOutputException APIレスポンスの処理中にエラーが発生した場合。
     */
    public static <T> ApiResponseRecord<T> handleResponse(
            final HttpResponse response, final Class<T> responseClass) {
        return handleResponse(response, r -> {
            try {
                return responseClass.cast(r.parseAs(responseClass));
            } catch (final IOException e) {
                throw new CommandLineInputOutputException("Failed to parse response: " + e.getMessage(), e);
            }
        });
    }

    /**
     * HttpResponseを指定された変換関数を使用してApiResponseRecordに変換します。
     *
     * @param <T> 応答データの型。
     * @param response 処理するHttpResponse。
     * @param converter レスポンスをデータ型に変換する関数。
     * @return 変換されたApiResponseRecord。
     * @throws CommandLineInputOutputException APIレスポンスの処理中にエラーが発生した場合。
     */
    public static <T> ApiResponseRecord<T> handleResponse(
            final HttpResponse response, final Function<HttpResponse, T> converter) {
        try {
            // ヘッダー情報の取得
            final Map<String, List<String>> headers = extractHeaders(response.getHeaders());

            // ステータスコードの取得
            final int statusCode = response.getStatusCode();

            // レスポンスの変換
            final T data = converter.apply(response);

            // レスポンスのクローズ
            response.disconnect();

            // 成功レスポンスの作成
            return ApiResponseRecord.<T>builder()
                    .status(ApiResponseRecord.ResponseStatus.SUCCESS)
                    .statusCode(statusCode)
                    .timestamp(LocalDateTime.now())
                    .data(data)
                    .headers(headers)
                    .build();

        } catch (final HttpResponseException e) {
            // HTTP応答エラーの処理
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("HTTP error occurred: {}", e.getMessage());
            }

            // エラーレスポンスの作成
            return getErrorResponse(e);

        } catch (final IOException e) {
            // I/Oエラーの処理
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("I/O error occurred: {}", e.getMessage());
            }

            throw new CommandLineInputOutputException(
                    "Failed to process API response: " + e.getMessage(), e);
        }
    }

    /**
     * HttpResponseをGenericJsonとしてApiResponseRecordに変換します。
     *
     * @param response 処理するHttpResponse。
     * @return 変換されたApiResponseRecord。
     * @throws CommandLineInputOutputException APIレスポンスの処理中にエラーが発生した場合。
     */
    public static ApiResponseRecord<GenericJson> handleJsonResponse(final HttpResponse response) {
        return handleResponse(response, r -> {
            try {
                return r.parseAs(GenericJson.class);
            } catch (final IOException e) {
                throw new CommandLineInputOutputException(
                        "Failed to parse JSON response: " + e.getMessage(), e);
            }
        });
    }

    /**
     * ヘッダーマップからヘッダー情報を抽出します。
     *
     * @param headerMap 元のヘッダーマップ
     * @return 処理済みのヘッダーマップ
     */
    private static Map<String, List<String>> extractHeaders(final Map<String, Object> headerMap) {
        final Map<String, List<String>> headers = new ConcurrentHashMap<>();

        if (headerMap == null) {
            return headers;
        }

        headerMap.forEach((key, values) -> {
            if (values != null) {
                headers.put(key, convertToStringList(values));
            }
        });

        return headers;
    }

    /**
     * オブジェクト値を文字列リストに変換します。
     *
     * @param values 変換するオブジェクト
     * @return 文字列のリスト
     */
    private static List<String> convertToStringList(final Object values) {
        final List<String> stringValues = new ArrayList<>();

        if (values instanceof Collection<?>) {
            processCollection((Collection<?>) values, stringValues);
        } else if (values instanceof Object[]) {
            processArray((Object[]) values, stringValues);
        } else {
            // 単一のオブジェクトの場合
            stringValues.add(values.toString());
        }

        return stringValues;
    }

    /**
     * コレクションの各要素を文字列リストに追加します。
     *
     * @param collection 処理するコレクション
     * @param stringValues 文字列を追加するリスト
     */
    private static void processCollection(
            final Collection<?> collection, final List<String> stringValues) {
        collection.forEach(value -> {
            if (value != null) {
                stringValues.add(value.toString());
            }
        });
    }

    /**
     * 配列の各要素を文字列リストに追加します。
     *
     * @param array 処理する配列。
     * @param stringValues 文字列を追加するリスト。
     */
    private static void processArray(final Object[] array, final List<String> stringValues) {
        for (final Object value : array) {
            if (value != null) {
                stringValues.add(value.toString());
            }
        }
    }

    /**
     * HttpResponseExceptionからエラー応答レコードを作成します。
     *
     * @param <T> 応答データの型
     * @param e HttpResponseException
     * @return エラー応答レコード
     */
    private static <T> ApiResponseRecord<T> getErrorResponse(final HttpResponseException e) {
        final ApiResponseRecord.ResponseStatus status = determineResponseStatus(e.getStatusCode());
        final Map<String, List<String>> headers = extractHeaders(e.getHeaders());

        // エラーレスポンスの作成
        return ApiResponseRecord.<T>builder()
                .status(status)
                .statusCode(e.getStatusCode())
                .timestamp(LocalDateTime.now())
                .headers(headers)
                .errorMessage(e.getMessage())
                .build();
    }

    /**
     * HTTPステータスコードに基づいてレスポンスステータスを決定します。
     *
     * @param statusCode HTTPステータスコード
     * @return 対応するレスポンスステータス
     */
    private static ApiResponseRecord.ResponseStatus determineResponseStatus(final int statusCode) {
        return switch (statusCode) {
            case 401, 403 -> ApiResponseRecord.ResponseStatus.UNAUTHORIZED;
            case 404 -> ApiResponseRecord.ResponseStatus.NOT_FOUND;
            case 429 -> ApiResponseRecord.ResponseStatus.RATE_LIMITED;
            case 301, 302, 307, 308 -> ApiResponseRecord.ResponseStatus.REDIRECT;
            default -> ApiResponseRecord.ResponseStatus.ERROR;
        };
    }

    /**
     * 応答データを抽出し、エラーがあれば例外を投げます。
     *
     * @param <T> データの型
     * @param response API応答レコード
     * @return 応答データ
     * @throws CommandLineInputOutputException エラー応答の場合
     */
    public static <T> T extractDataOrThrow(final ApiResponseRecord<T> response) {
        // エラー応答の場合は例外をスロー
        if (response.status() != ApiResponseRecord.ResponseStatus.SUCCESS) {
            throw new CommandLineInputOutputException(
                    response.errorMessage() != null
                            ? response.errorMessage()
                            : "API error: " + response.status() + " (" + response.statusCode() + ")");
        }

        // データがnullの場合は例外をスロー
        if (response.data() == null) {
            throw new CommandLineInputOutputException("API response contains no data");
        }

        // 成功応答の場合はデータを返す
        return response.data();
    }
}
