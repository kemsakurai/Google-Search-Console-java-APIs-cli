package xyz.monotalk.google.webmaster.cli.test;

import com.google.api.client.http.javanet.NetHttpTransport;

/**
 * テスト用のHttpTransportを生成するインターフェース。
 */
@FunctionalInterface
public interface TransportFactory {
    
    /**
     * NetHttpTransportを生成します。
     *
     * @return 生成されたNetHttpTransport
     */
    NetHttpTransport createTransport();
}
