package dev.earthly.plugin.language.syntax;

import com.intellij.codeInsight.generation.actions.CommentByLineCommentAction;
import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase4;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase;
import dev.earthly.plugin.language.syntax.parser.EarthlyParserDefinition;
import dev.earthly.plugin.metadata.EarthlyFileType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class EarthlyCommenterTest extends LightPlatformCodeInsightFixture4TestCase {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void prepareTempDir() throws Exception {
        tempFolder.create();
        System.setProperty("earthly.test_dir", tempFolder.getRoot().getAbsolutePath());
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }

    @Test
    public void testCommenter() {
        myFixture.configureByText(EarthlyFileType.INSTANCE, "TEST_<caret>ME:");
        CommentByLineCommentAction commentAction = new CommentByLineCommentAction();
        commentAction.actionPerformedImpl(getProject(), myFixture.getEditor());
        myFixture.checkResult("#TEST_ME:");
        commentAction.actionPerformedImpl(getProject(), myFixture.getEditor());
        myFixture.checkResult("TEST_ME:");
    }
}
