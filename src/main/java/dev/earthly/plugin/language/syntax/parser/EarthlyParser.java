package dev.earthly.plugin.language.syntax.parser;

import com.intellij.indentation.AbstractIndentParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import dev.earthly.plugin.language.syntax.highlighting.EarthlyTokenSets;
import dev.earthly.plugin.language.syntax.lexer.EarthlyElementType;

class EarthlyParser extends AbstractIndentParser implements PsiParser {

    @Override
    protected void parseRoot(IElementType root) {
        myBuilder.setDebugMode(true);
        PsiBuilder.Marker mark = myBuilder.mark();
        while (!myBuilder.eof()) {
            IElementType tokenType = myBuilder.getTokenType();
            assert tokenType != null;
            if (tokenType.equals(EarthlyTokenSets.FUNCTION)) {
                parseFunction();
            } else if (tokenType.equals(EarthlyTokenSets.FUNCTION_CALL)) {
                parseSimple(tokenType);
            } else if (tokenType.equals(EarthlyTokenSets.TARGET)) {
                parseSimple(tokenType);
            } else if (tokenType.equals(EarthlyTokenSets.TARGET_CALL)) {
                parseSimple(tokenType);
            } else if (tokenType.equals(EarthlyTokenSets.BASE_CALL)) {
                parseSimple(tokenType);
            } else {
                myBuilder.advanceLexer();
            }

        }
        mark.done(root);
    }

    private void parseFunction() {
        PsiBuilder.Marker function = myBuilder.mark();
        myBuilder.advanceLexer();
        function.done(EarthlyTokenSets.FUNCTION);
        expect(EarthlyTokenSets.FUNCTION_DOTS, "Function need to end with :");
    }
    private void parseSimple(IElementType type) {
        PsiBuilder.Marker function = myBuilder.mark();
        myBuilder.advanceLexer();
        function.done(type);
    }

    @Override
    protected IElementType getIndentElementType() {
        return EarthlyTokenSets.INDENT;
    }

    @Override
    protected IElementType getEolElementType() {
        return EarthlyTokenSets.EOL;
    }
}
