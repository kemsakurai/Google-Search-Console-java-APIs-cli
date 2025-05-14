package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.json.gson.GsonFactory;

/**
 * GsonFactoryを提供するプロバイダインターフェース。
 * テスト用のみに使用されます。
 */
@FunctionalInterface
public interface JsonFactoryProvider {
    /**
     * GsonFactoryを取得します。
     *
     * @return GsonFactoryインスタンス
     */
    GsonFactory getJsonFactory();
}
