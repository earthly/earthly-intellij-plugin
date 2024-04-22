package dev.earthly.plugin.language.syntax.parser;

import com.intellij.indentation.AbstractIndentParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import dev.earthly.plugin.language.syntax.highlighting.EarthlyTokenSets;

class EarthlyParser extends AbstractIndentParser implements PsiParser {

    @Override
    protected void parseRoot(IElementType root) {
        myBuilder.setDebugMode(true);
        PsiBuilder.Marker mark = myBuilder.mark();
        while (!myBuilder.eof()) {
            IElementType tokenType = myBuilder.getTokenType();
            if (tokenType == EarthlyTokenSets.FUNCTION) {
                parseFunction();
            } else if (tokenType == EarthlyTokenSets.FUNCTION_CALL) {
                PsiBuilder.Marker function = myBuilder.mark();
                myBuilder.advanceLexer();
                function.done(EarthlyTokenSets.FUNCTION_CALL);
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

    @Override
    protected IElementType getIndentElementType() {
        return EarthlyTokenSets.INDENT;
    }

    @Override
    protected IElementType getEolElementType() {
        return EarthlyTokenSets.EOL;
    }
}
