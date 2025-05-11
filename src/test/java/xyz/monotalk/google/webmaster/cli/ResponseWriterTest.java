package xyz.monotalk.google.webmaster.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.api.client.json.GenericJson;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;

/**
 * ResponseWriterTestクラスは、ResponseWriterのテストを行います。
 * このクラスは、JSON形式のレスポンスの出力をテストするためのユニットテストを提供します。
 */
@RunWith(MockitoJUnitRunner.class)
public class ResponseWriterTest {

    /**
     * テスト用の一時フォルダ。
     */
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * モックされたレスポンスオブジェクト。
     */
    @Mock
    private GenericJson mockResponse;

    /**
     * モックされたロガー。
     */
    @Mock
    private Logger mockLogger;

    /**
     * テスト用の出力ストリーム。
     */
    private static final ByteArrayOutputStream OUT_CONTENT = new ByteArrayOutputStream();

    /**
     * 元の標準出力ストリーム。
     */
    private static final PrintStream ORIGINAL_OUT = System.out;

    /**
     * テスト用のJSONデータを生成するクラス。
     */
    private static final String TEST_JSON = "{\"key\": \"value\"}";

    /**
     * JSONデータのキーを指定します。
     */
    private static final String JSON_KEY = "key";
    
    /**
     * JSONデータの値を指定します。
     */
    private static final String JSON_VALUE = "value";
    
    /**
     * 予想されるJSONデータの形式を指定します。
     */
    private static final String EXPECTED_JSON = "{\"key\":\"value\"}";

    /**
     * JSON書き込みエラーメッセージ。
     */
    private static final String JSON_WRITE_ERR = "JSON形式でレスポンスが正しく書き込まれていません";

    /**
     * デフォルトコンストラクタ。
     * テストクラスの初期化を行います。
     */
    public ResponseWriterTest() {
        super();
    }

    /**
     * テスト前のセットアップを行います。
     * 標準出力をテスト用のストリームにリダイレクトします。
     *
     * @throws IOException 入出力例外が発生した場合。
     */
    @Before
    public void setUpStreams() throws IOException {
        System.setOut(new PrintStream(OUT_CONTENT));
        when(mockResponse.toPrettyString()).thenReturn(TEST_JSON);
    }

    /**
     * テスト後の後処理を行います。
     * 標準出力を元のストリームに戻します。
     */
    @After
    public void restoreStreams() {
        System.setOut(ORIGINAL_OUT);
    }

    /**
     * JSONテストデータ生成のための補助クラスです。
     */
    @SuppressWarnings("PMD.TestClassWithoutTestCases")
    public static final class TestJson extends GenericJson {

        /**
         * デフォルトコンストラクタ。
         */
        public TestJson() {
            super();
        }

        /**
         * 値を指定してTestJsonオブジェクトを生成します。
         *
         * @param value JSONデータの値
         */
        public TestJson(final String value) {
            super();
            // コンストラクタではオーバーライド可能なメソッド呼び出しを避けるためフィールド直接操作
            put(JSON_KEY, value);
        }

        /**
         * テスト用のJSONデータを生成します。
         *
         * @return テスト用のJSONデータ
         */
        public static String generateTestJson() {
            return EXPECTED_JSON;
        }
    }

    /**
     * JSON形式でレスポンスを出力するテストを共通化。
     * @param response レスポンスオブジェクト
     * @param format 出力フォーマット
     * @param filePath ファイルパス
     * @param expectedContent 期待される内容
     */
    private void assertJsonResponse(final GenericJson response, final Format format, final String filePath, final String expectedContent) {
        ResponseWriter.writeJson(response, format, filePath);
        if (format == Format.CONSOLE) {
            final String output = OUT_CONTENT.toString();
            assertTrue("コンソール出力が正しくありません", output.contains(expectedContent));
        } else if (format == Format.JSON) {
            try {
                final String content = new String(Files.readAllBytes(new File(filePath).toPath()));
                assertEquals("JSONファイル出力が正しくありません", expectedContent, content);
            } catch (IOException e) {
                throw new CommandLineInputOutputException("ファイル読み込み中にエラーが発生しました", e);
            }
        }
    }

    /**
     * JSON形式で正しくレスポンスが書き込まれることをテストします。
     */
    @Test
    public void testWriteJson_正常系_JSON形式でレスポンスが書き込まれる() {
        // Given
        final String expectedJson = EXPECTED_JSON;

        // When
        final GenericJson json = new GenericJson();
        json.set(JSON_KEY, JSON_VALUE);
        final String result = TestUtils.invokeConvertToJsonString(json);

        // Then
        assertEquals(JSON_WRITE_ERR, expectedJson, result);
    }

    /**
     * コンソール出力が正しいことをテストします。
     */
    @Test
    public void testWriteJson正常系コンソール出力() {
        assertJsonResponse(mockResponse, Format.CONSOLE, null, TEST_JSON);
    }

    /**
     * JSONファイル出力が正しいことをテストします。
     * @throws IOException ファイル操作中に発生する可能性のある例外
     */
    @Test
    public void testWriteJson正常系Jsonファイル出力() throws IOException {
        // Given
        final File outputFile = tempFolder.newFile("test-output.json");
        final String filePath = outputFile.getAbsolutePath();
        when(mockResponse.toPrettyString()).thenReturn(TEST_JSON);

        // When & Then
        assertJsonResponse(mockResponse, Format.JSON, filePath, TEST_JSON);
    }

    /**
     * JSONフォーマットでファイルパスが未指定の場合のテストです。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testWriteJson異常系Jsonフォーマットでファイルパス未指定() {
        ResponseWriter.writeJson(mockResponse, Format.JSON, null);
    }

    /**
     * JSONファイル書き込みエラーのテストです。
     * @throws IOException ファイル操作中に発生する可能性のある例外
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testWriteJson異常系Jsonファイル書き込みエラー() throws IOException {
        // 読み取り専用の一時ディレクトリを作成
        final Set<PosixFilePermission> readOnlyPerms =
            PosixFilePermissions.fromString("r-xr-xr-x");
        final FileAttribute<Set<PosixFilePermission>> attrs =
            PosixFilePermissions.asFileAttribute(readOnlyPerms);
        final Path readOnlyDir = Files.createTempDirectory("readonly", attrs);

        // 一時ディレクトリ内のファイルパスを生成
        final String filePath = readOnlyDir.resolve("test.json").toString();
        ResponseWriter.writeJson(mockResponse, Format.JSON, filePath);
    }

    /**
     * 不正なフォーマット指定のテストです。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testWriteJson異常系不正なフォーマット指定() {
        ResponseWriter.writeJson(mockResponse, null, null);
    }

    /**
     * 空のオブジェクト出力テストです。
     * @throws Exception テスト中に発生する可能性のある例外
     */
    @Test
    public void testWriteJson正常系空のオブジェクト出力() throws Exception {
        final GenericJson emptyJson = new GenericJson();
        assertJsonResponse(emptyJson, Format.CONSOLE, null, "{}");
    }

    /**
     * 権限なしディレクトリへの書き込みテストです。
     * @throws Exception テスト中に発生する可能性のある例外
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testWriteJson異常系権限なしディレクトリへの書き込み() throws Exception {
        final Set<PosixFilePermission> perms =
            PosixFilePermissions.fromString("r--r--r--");
        final FileAttribute<Set<PosixFilePermission>> attr =
            PosixFilePermissions.asFileAttribute(perms);
        final Path readOnlyDir = Files.createTempDirectory("readonly", attr);
        final File outputFile = new File(readOnlyDir.toFile(), "test.json");

        try {
            ResponseWriter.writeJson(mockResponse, Format.JSON,
                outputFile.getAbsolutePath());
        } finally {
            Files.walk(readOnlyDir)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        // ログ出力を追加してエラーを記録
                        if (mockLogger.isErrorEnabled()) {
                            mockLogger.error("ファイル削除中にエラーが発生しました: {}", e.getMessage(), e);
                        }
                    }
                });
        }
    }

    /**
     * 大きなJSONオブジェクト出力のテストです。
     * @throws Exception テスト中に発生する可能性のある例外
     */
    @Test
    public void testWriteJson正常系大きなJsonオブジェクト出力() throws Exception {
        // Given
        final StringBuilder largeValue = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            largeValue.append("value").append(i);
        }
        final TestJson largeJson = new TestJson(largeValue.toString());

        assertJsonResponse(largeJson, Format.CONSOLE, null, "value0");
        assertTrue("大きなJsonオブジェクト出力が正しくありません", OUT_CONTENT.toString().contains("value999"));
    }

    /**
     * IOエラー発生時の例外スローテストです。
     * @throws Exception テスト中に発生する可能性のある例外
     */
    @Test(expected = CommandLineInputOutputException.class)
    public void testWriteJsonIoエラー発生時に例外スロー() throws Exception {
        // Given
        final GenericJson mockResponse = mock(GenericJson.class);
        when(mockResponse.toPrettyString()).thenThrow(new IOException("IO Error"));

        // When
        ResponseWriter.writeJson(mockResponse, Format.CONSOLE, null);
    }

    /**
     * ファイルパスがJSONフォーマット時に未指定の場合のテストです。
     * @throws Exception テスト中に発生する可能性のある例外
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testWriteJson異常系ファイルパスがJsonフォーマット時に未指定() throws Exception {
        // Given
        final GenericJson mockResponse = mock(GenericJson.class);
        when(mockResponse.toPrettyString()).thenReturn("test data");

        // When
        ResponseWriter.writeJson(mockResponse, Format.JSON, null);
    }

    /**
     * フォーマットが未指定の場合のテストです。
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testWriteJson異常系フォーマットが未指定() {
        ResponseWriter.writeJson(mockResponse, null, null);
    }

    /**
     * JSON形式でレスポンスが正しく書き込まれるかをテストします。
     */
    @Test
    public void testWriteJson正常系() {
        // Given
        final GenericJson json = new GenericJson();
        json.set(JSON_KEY, JSON_VALUE);
        
        // When
        final String result = TestUtils.invokeConvertToJsonString(json);
        
        // Then
        assertEquals(JSON_WRITE_ERR, EXPECTED_JSON, result);
    }

    /**
     * 関連するテストを統合したテストです。
     * @throws Exception テスト中に発生する可能性のある例外
     */
    @Test
    public void testWriteJson統合テスト() throws Exception {
        // Given
        final GenericJson json = new GenericJson();
        json.set(JSON_KEY, JSON_VALUE);

        // When
        final String result = TestUtils.invokeConvertToJsonString(json);

        // Then
        assertEquals(JSON_WRITE_ERR, EXPECTED_JSON, result);

        // コンソール出力のテスト
        assertJsonResponse(json, Format.CONSOLE, null, EXPECTED_JSON);

        // ファイル出力のテスト
        final File outputFile = tempFolder.newFile("test-output.json");
        final String filePath = outputFile.getAbsolutePath();
        assertJsonResponse(json, Format.JSON, filePath, EXPECTED_JSON);
    }

    /**
     * 改良版の統合テストです。
     * @throws IOException ファイル操作中に発生する可能性のある例外
     */
    @Test(expected = CmdLineArgmentException.class)
    public void testWriteJson統合テスト_改良版() throws IOException {
        // Given
        final GenericJson json = new GenericJson();
        json.set(JSON_KEY, JSON_VALUE);

        // When
        final String result = TestUtils.invokeConvertToJsonString(json);

        // Then
        assertEquals(JSON_WRITE_ERR, EXPECTED_JSON, result);

        // コンソール出力のテスト
        assertJsonResponse(json, Format.CONSOLE, null, EXPECTED_JSON);

        // ファイル出力のテスト
        final File outputFile = tempFolder.newFile("test-output.json");
        final String filePath = outputFile.getAbsolutePath();
        assertJsonResponse(json, Format.JSON, filePath, EXPECTED_JSON);

        // 異常系テスト
        ResponseWriter.writeJson(json, null, null);
    }

    /**
     * 正常系の統合テストです。
     * @throws Exception テスト中に発生する可能性のある例外
     */
    @Test
    public void testWriteJson正常系統合テスト() throws Exception {
        // Given
        final GenericJson json = new GenericJson();
        json.set(JSON_KEY, JSON_VALUE);

        // When
        final String result = TestUtils.invokeConvertToJsonString(json);

        // Then
        assertEquals(JSON_WRITE_ERR, EXPECTED_JSON, result);
    }
}