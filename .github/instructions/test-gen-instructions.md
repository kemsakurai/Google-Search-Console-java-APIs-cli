# テスト生成指示

## 言語設定
**重要**: すべての出力は日本語で行ってください。コメントやドキュメンテーションも日本語で記述してください。

## テスト規約

### 全般
- JUnit 4を使用してください
- テスト用のモックには Mockitoを使用してください
- Spring Boot テストフレームワークを活用してください
- テストは独立して実行できるようにしてください
- デフォルトではテスト環境の標準設定を使用してください

### テストクラス命名規則
- クラス名: テスト対象のクラス名+`Test`サフィックス（例: `WebmastersFactoryTest`）
- メソッド名: `test[テスト対象機能]_[テスト条件]_[期待結果]`のフォーマット
  - 例: `testExecute_ValidSiteUrl_ReturnsSuccess`

### テスト構成
- テストは Given-When-Then パターンに従って構成してください
  - **Given**: テストの前提条件をセットアップ
  - **When**: テスト対象の機能を実行
  - **Then**: 期待される結果を検証
- 各テストは単一の機能/動作にフォーカスしてください
- 前提条件は明示的に記載してください

## モックの使用

以下のようにMockitoを使用してください:

```java
@RunWith(MockitoJUnitRunner.class)
public class ExampleCommandTest {

    @Mock
    private WebmastersFactory factory;
    
    @InjectMocks
    private ExampleCommand command;
    
    @Test
    public void testExecute_ValidParameters_Success() {
        // Given
        command.setSiteUrl("https://example.com");
        Webmasters webmasters = Mockito.mock(Webmasters.class);
        Webmasters.Searchanalytics searchAnalytics = Mockito.mock(Webmasters.Searchanalytics.class);
        
        when(factory.create()).thenReturn(webmasters);
        when(webmasters.searchanalytics()).thenReturn(searchAnalytics);
        
        // When
        command.execute();
        
        // Then
        verify(factory).create();
        // その他の検証
    }
}
```

## 例外のテスト

例外のテストには以下のアプローチを使用してください:

```java
@Test(expected = CmdLineArgmentException.class)
public void testExecute_InvalidParameter_ThrowsException() {
    // テストコード
}
```

または、より詳細な例外の検証:

```java
@Test
public void testExecute_InvalidParameter_ThrowsExceptionWithMessage() {
    try {
        // テスト対象コード実行
        fail("例外が発生しませんでした");
    } catch (CmdLineArgmentException e) {
        assertEquals("期待されるエラーメッセージ", e.getMessage());
    }
}
```

## パラメータ化テスト

複数のケースをテストする場合は、パラメータ化テストを使用してください:

```java
@RunWith(Parameterized.class)
public class ParameterizedTest {
    
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { "param1", expectedResult1 },
            { "param2", expectedResult2 }
        });
    }
    
    // テストコード
}
```

## 統合テスト

統合テストには `@SpringBootTest` アノテーションを使用してください:

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class IntegrationTest {
    // テストコード
}
```