package dev.earthly.plugin.language.syntax;

import com.intellij.testFramework.ParsingTestCase;
import dev.earthly.plugin.language.syntax.parser.EarthlyParserDefinition;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class EarthlyCommenterTest extends ParsingTestCase {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    public EarthlyCommenterTest() {
        super("", "earthly", new EarthlyParserDefinition());
    }

    public void setUp() throws Exception {
        super.setUp();
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

    @Override
    protected boolean includeRanges() {
        return true;
    }

    public void testCommenter() {
//        myFixture.configureByText(EarthlyCommenter.INSTANCE, "<caret>website = https://en.wikipedia.org/");
//        CommentByLineCommentAction commentAction = new CommentByLineCommentAction();
//        commentAction.actionPerformedImpl(getProject(), myFixture.getEditor());
//        myFixture.checkResult("#website = https://en.wikipedia.org/");
//        commentAction.actionPerformedImpl(getProject(), myFixture.getEditor());
//        myFixture.checkResult("website = https://en.wikipedia.org/");
    }
}