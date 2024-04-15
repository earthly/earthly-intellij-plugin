package dev.earthly.plugin.language.syntax.highlighting;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import dev.earthly.plugin.language.syntax.lexer.EarthlyElementType;
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateScope;

import java.util.concurrent.ConcurrentHashMap;

public class EarthlyTokenSets {
    private static ConcurrentHashMap<TextMateScope, EarthlyElementType> cache = new ConcurrentHashMap<>();

    //    TokenSet IDENTIFIERS = TokenSet.create(SimpleTypes.KEY);
//
    static IElementType EMPTY = mapToType(TextMateScope.EMPTY);
    static TextMateScope ROOT = new TextMateScope("source.earthfile", TextMateScope.EMPTY);
    static TextMateScope ROOT1 = new TextMateScope(null, null);

    static TextMateScope scope1 = ROOT.add("comment.line.earthfile");
    static IElementType COMMENT1 = mapToType(scope1);
    static TextMateScope scope2 = ROOT.add("comment.line.number-sign.earthfile");
    static IElementType COMMENT2 = mapToType(scope2);
    static TextMateScope scope3 = scope2.add("punctuation.definition.comment.earthfile");
    static IElementType COMMENT3 = mapToType(scope3);

    public static TokenSet COMMENTS = TokenSet.create(COMMENT1, COMMENT2, COMMENT3);

    public static EarthlyElementType mapToType(TextMateScope scope) {
        EarthlyElementType earthlyElementType = cache.get(scope);
        if (earthlyElementType != null) {
//      System.out.println("Returning cached " + scope);
            return earthlyElementType;
        }

        EarthlyElementType ret = new EarthlyElementType(scope);
        System.out.println("Putting into cache " + scope);
        cache.put(scope, ret);
        return ret;
    }
}
