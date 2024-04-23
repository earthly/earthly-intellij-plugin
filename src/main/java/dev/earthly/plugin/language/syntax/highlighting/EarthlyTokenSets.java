package dev.earthly.plugin.language.syntax.highlighting;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import dev.earthly.plugin.language.syntax.parser.EarthLexer;
import dev.earthly.plugin.language.syntax.psi.EarthlyFunctionCallPsiElement;
import dev.earthly.plugin.language.syntax.psi.EarthlyFunctionPsiElement;
import dev.earthly.plugin.language.syntax.psi.EarthlyPsiElement;
import dev.earthly.plugin.metadata.EarthlyLanguage;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;

public class EarthlyTokenSets {

    public static final TokenSet COMMENTS =
            PSIElementTypeFactory.createTokenSet(
                    EarthlyLanguage.INSTANCE,
                    EarthLexer.COMMENT);

    public static final TokenSet WHITESPACE =
            PSIElementTypeFactory.createTokenSet(
                    EarthlyLanguage.INSTANCE,
                    EarthLexer.WS);

    public static final TokenSet STRING =
            PSIElementTypeFactory.createTokenSet(
                    EarthlyLanguage.INSTANCE,
                    EarthLexer.Atom);
    
    public static class Factory {
        public static EarthlyPsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();
            if (!(type instanceof TokenIElementType myType))
                return new EarthlyPsiElement(node);
            int ttype = myType.getANTLRTokenType();
            if (ttype == EarthLexer.Function) {
                return new EarthlyFunctionPsiElement(node);
            } else if (ttype == EarthLexer.FUNCTION) {
                return new EarthlyFunctionCallPsiElement(node);
            }
            return new EarthlyPsiElement(node);
            // Handle unknown type
//            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}
