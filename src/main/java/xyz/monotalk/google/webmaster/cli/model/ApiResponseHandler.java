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
 *
 * <p>このクラスは、Google APIからのレスポンスを
 * ApiResponseRecordに変換するためのメソッドを提供します。</p>
 */
public final class ApiResponseHandler {

    /**
     * ログ出力用のロガーインスタンス。
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseHandler.class);

    private ApiResponseHandler() {
        // インスタンス化を防止するためのプライベートコンストラクタ
    }

    /**
     * HttpResponseをApiResponseRecordに変換します。
     *
     * <p>指定されたHttpResponseを処理し、
     * ApiResponseRecordオブジェクトを生成します。</p>
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
     * <p>このメソッドは、HttpResponseを処理し、
     * 指定された関数を使用してデータを変換します。</p>
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
     * <p>このメソッドは、HttpResponseを処理し、
     * GenericJson形式のデータを返します。</p>
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
     * <p>このメソッドは、元のヘッダーマップを処理し、
     * 必要な形式に変換します。</p>
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
     * <p>このメソッドは、与えられたオブジェクトを
     * 文字列のリストに変換します。</p>
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
     * <p>このメソッドは、コレクション内の各要素を
     * 文字列リストに変換して追加します。</p>
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
     * <p>このメソッドは、配列内の各要素を
     * 文字列リストに変換して追加します。</p>
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
     * @param exception HttpResponseException
     * @return エラー応答レコード
     */
    private static <T> ApiResponseRecord<T> getErrorResponse(final HttpResponseException exception) {
        final ApiResponseRecord.ResponseStatus status = determineResponseStatus(exception.getStatusCode());
        final Map<String, List<String>> headers = extractHeaders(exception.getHeaders());

        // エラーレスポンスの作成
        return ApiResponseRecord.<T>builder()
                .status(status)
                .statusCode(exception.getStatusCode())
                .timestamp(LocalDateTime.now())
                .headers(headers)
                .errorMessage(exception.getMessage())
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
        // フィールド名の変更に対応
        if (response.status() != ApiResponseRecord.ResponseStatus.SUCCESS) {
            throw new CommandLineInputOutputException(
                response.getErrorMessage().orElse(
                    "API error: " + response.status() + " (" + response.code() + ")"
                )
            );
        }

        if (response.data() == null) {
            throw new CommandLineInputOutputException("API response contains no data");
        }

        return response.data();
    }
}
