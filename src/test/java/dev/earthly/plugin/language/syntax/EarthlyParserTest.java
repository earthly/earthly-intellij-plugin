package dev.earthly.plugin.language.syntax;

import com.intellij.testFramework.ParsingTestCase;
import dev.earthly.plugin.language.syntax.parser.EarthlyParserDefinition;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EarthlyParserTest extends ParsingTestCase {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    public EarthlyParserTest() {
        super("", "earthly", new EarthlyParserDefinition());
    }

    @Before
    public void prepareTempDir() throws Exception {
        tempFolder.create();
        System.setProperty("earthly.test_dir", tempFolder.getRoot().getAbsolutePath());
    }

    @Test
    public void testParsingTestData() {
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }
}