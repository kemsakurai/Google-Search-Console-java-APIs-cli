package xyz.monotalk.google.webmaster.cli.test;

import com.google.auth.oauth2.GoogleCredentials;

/**
 * テスト用の認証情報を生成するインターフェース。
 */
@FunctionalInterface
public interface CredentialFactory {
    
    /**
     * 認証情報を生成します。
     *
     * @return 生成された認証情報
     */
    GoogleCredentials create();
}
