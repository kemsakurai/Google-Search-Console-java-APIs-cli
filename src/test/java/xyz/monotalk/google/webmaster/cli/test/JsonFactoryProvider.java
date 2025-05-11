package xyz.monotalk.google.webmaster.cli.test;

import com.google.api.client.json.gson.GsonFactory;

/**
 * テスト用のGsonFactoryを提供するインターフェース。
 */
@FunctionalInterface
public interface JsonFactoryProvider {
    
    /**
     * GsonFactoryを取得します。
     *
     * @return GsonFactoryのインスタンス
     */
    GsonFactory get();
}
