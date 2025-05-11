package xyz.monotalk.google.webmaster.cli;

import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;

/**
 * GoogleCredentialsを生成するファクトリインターフェース。
 * テスト用のみに使用されます。
 */
@FunctionalInterface
public interface CredentialFactory {
    /**
     * GoogleCredentialsを生成します。
     *
     * @return 生成されたGoogleCredentialsインスタンス
     * @throws IOException 認証情報の読み込み中に例外が発生した場合
     */
    GoogleCredentials createCredential() throws IOException;
}
