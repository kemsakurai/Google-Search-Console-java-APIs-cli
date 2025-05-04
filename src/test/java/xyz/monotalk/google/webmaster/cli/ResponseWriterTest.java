package xyz.monotalk.google.webmaster.cli;

import com.google.api.client.json.GenericJson;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.kohsuke.args4j.CmdLineException;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ResponseWriterTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Mock
    private GenericJson mockResponse;

    @Mock
    private Logger mockLogger;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private TestJson testJson;
    private ResponseWriter responseWriter;

    private static class TestJson extends GenericJson {
        @com.google.api.client.util.Key
        public String key;

        public TestJson(String value) {
            this.key = value;
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        responseWriter = new ResponseWriter();
        responseWriter.setLogger(mockLogger); // モックされた Logger を注入
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testWriteJson_正常系_コンソール出力() throws CmdLineArgmentException, CommandLineInputOutputException {
        // Given
        String response = "{\"test\": \"value\"}";
        Format format = Format.CONSOLE;

        // When
        responseWriter.writeJson(response, format, null);

        // Then
        verify(mockLogger).info(response);
    }

    @Test
    public void testWriteJson_正常系_JSONファイル出力() throws Exception {
        // Given
        File outputFile = tempFolder.newFile("test-output.json");
        String filePath = outputFile.getAbsolutePath();
        when(mockResponse.toPrettyString()).thenReturn("{\"test\": \"value\"}");

        // When
        responseWriter.writeJson(mockResponse, Format.JSON, filePath);

        // Then
        String content = new String(Files.readAllBytes(outputFile.toPath()));
        assertEquals("{\"test\": \"value\"}", content);
    }

    @Test(expected = CmdLineArgmentException.class)
    public void testWriteJson_異常系_JSONフォーマットでファイルパス未指定() throws CmdLineArgmentException, CommandLineInputOutputException {
        responseWriter.writeJson(mockResponse, Format.JSON, null);
    }

    @Test(expected = CommandLineInputOutputException.class)
    public void testWriteJson_異常系_JSONファイル書き込みエラー() throws Exception {
        // 読み取り専用の一時ディレクトリを作成
        Set<PosixFilePermission> readOnlyPerms = PosixFilePermissions.fromString("r-xr-xr-x");
        FileAttribute<Set<PosixFilePermission>> attrs = PosixFilePermissions.asFileAttribute(readOnlyPerms);
        Path readOnlyDir = Files.createTempDirectory("readonly", attrs);

        // 一時ディレクトリ内のファイルパスを生成
        String filePath = readOnlyDir.resolve("test.json").toString();
        responseWriter.writeJson(mockResponse, Format.JSON, filePath);
    }

    @Test(expected = CmdLineArgmentException.class)
    public void testWriteJson_異常系_不正なフォーマット指定() throws CmdLineArgmentException, CommandLineInputOutputException {
        responseWriter.writeJson(mockResponse, null, null);
    }

    @Test
    public void testWriteJson_正常系_空のオブジェクト出力() throws CmdLineArgmentException, CommandLineInputOutputException {
        // Given
        String response = "{}";
        Format format = Format.CONSOLE;

        // When
        responseWriter.writeJson(response, format, null);

        // Then
        verify(mockLogger).info(response);
    }

    @Test(expected = CommandLineInputOutputException.class)
    public void testWriteJson_異常系_権限なしディレクトリへの書き込み() throws Exception {
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("r--r--r--");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
        Path readOnlyDir = Files.createTempDirectory("readonly", attr);
        File outputFile = new File(readOnlyDir.toFile(), "test.json");
        
        try {
            responseWriter.writeJson(mockResponse, Format.JSON, outputFile.getAbsolutePath());
        } finally {
            Files.walk(readOnlyDir)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        // クリーンアップ中のエラーは無視
                    }
                });
        }
    }

    @Test
    public void testWriteJson_正常系_大きなJSONオブジェクト出力() throws Exception {
        // Given
        StringBuilder largeValue = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            largeValue.append("value").append(i);
        }
        TestJson largeJson = new TestJson(largeValue.toString());
        
        // When
        responseWriter.writeJson(largeJson, Format.CONSOLE, null);
        
        // Then
        // モックされたロガーが適切に呼ばれたことを検証する
        verify(mockLogger).info(org.mockito.ArgumentMatchers.contains("value0"));
    }

    @Test(expected = CommandLineInputOutputException.class)
    public void testWriteJson_IOエラー発生時に例外スロー() throws CmdLineArgmentException, CommandLineInputOutputException, IOException {
        // Given
        GenericJson mockResponse = mock(GenericJson.class);
        when(mockResponse.toPrettyString()).thenThrow(new IOException("IO Error"));

        // When
        responseWriter.writeJson(mockResponse, Format.CONSOLE, null);
    }

    @Test(expected = CmdLineArgmentException.class)
    public void testWriteJson_異常系_ファイルパスがJSONフォーマット時に未指定() throws CmdLineArgmentException, CommandLineInputOutputException, IOException {
        // Given
        GenericJson mockResponse = mock(GenericJson.class);
        when(mockResponse.toPrettyString()).thenReturn("test data");

        // When
        responseWriter.writeJson(mockResponse, Format.JSON, null);
    }

    @Test(expected = CmdLineArgmentException.class)
    public void testWriteJson_異常系_フォーマットが未指定() throws CmdLineArgmentException, CommandLineInputOutputException {
        responseWriter.writeJson(mockResponse, null, null);
    }
}