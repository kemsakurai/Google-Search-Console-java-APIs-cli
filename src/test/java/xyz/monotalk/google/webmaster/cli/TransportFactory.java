package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.http.HttpTransport;
import java.io.IOException;

/**
 * HttpTransportを生成するファクトリインターフェース。
 * テスト用のみに使用されます。
 */
@FunctionalInterface
public interface TransportFactory {
    /**
     * HttpTransportを生成します。
     *
     * @return 生成されたHttpTransportインスタンス
     * @throws IOException 入出力例外が発生した場合
     */
    HttpTransport createTransport() throws IOException;
}
