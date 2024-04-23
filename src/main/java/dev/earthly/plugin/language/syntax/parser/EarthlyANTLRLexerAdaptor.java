package dev.earthly.plugin.language.syntax.parser;

import dev.earthly.plugin.metadata.EarthlyLanguage;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;

public class EarthlyANTLRLexerAdaptor extends ANTLRLexerAdaptor {
    static {
        initializeElementTypeFactory();
    }

    public EarthlyANTLRLexerAdaptor() {
        super(EarthlyLanguage.INSTANCE, prepareLexer());
    }

    private static EarthLexer prepareLexer() {
        EarthLexer lexer = new EarthLexer(null);
        lexer.removeErrorListeners();
        return lexer;
    }
    public static void initializeElementTypeFactory() {
        PSIElementTypeFactory.defineLanguageIElementTypes(
                EarthlyLanguage.INSTANCE,
                EarthLexer.tokenNames,
                EarthParser.ruleNames
        );
    }
}