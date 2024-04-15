package dev.earthly.plugin.language.syntax.commenter;

import static org.junit.jupiter.api.Assertions.*;

import com.intellij.lang.ParserDefinition;
import com.intellij.testFramework.ParsingTestCase;
import dev.earthly.plugin.language.syntax.parser.EarthlyParserDefinition;
import org.jetbrains.annotations.NotNull;

public class EarthlyCommenterTest extends ParsingTestCase {
    protected EarthlyCommenterTest() {
        super("", "earthly", new EarthlyParserDefinition());
    }

    public void testCommenter() {
        myFixture.configureByText(EarthlyCommenter.INSTANCE, "<caret>website = https://en.wikipedia.org/");
        CommentByLineCommentAction commentAction = new CommentByLineCommentAction();
        commentAction.actionPerformedImpl(getProject(), myFixture.getEditor());
        myFixture.checkResult("#website = https://en.wikipedia.org/");
        commentAction.actionPerformedImpl(getProject(), myFixture.getEditor());
        myFixture.checkResult("website = https://en.wikipedia.org/");
    }
}