package dev.earthly.plugin.language.syntax.highlighting;

import com.intellij.psi.tree.TokenSet;
import dev.earthly.plugin.language.syntax.lexer.EarthlyElementType;
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateScope;

import java.util.concurrent.ConcurrentHashMap;

public class EarthlyTokenSets {
    private static final TextMateScope ROOT = new TextMateScope("source.earthfile", TextMateScope.EMPTY);
    private static final ConcurrentHashMap<TextMateScope, EarthlyElementType> cache = new ConcurrentHashMap<>();

    public final static EarthlyElementType EMPTY = mapToType(TextMateScope.EMPTY);
    public final static EarthlyElementType COMMENT1 = createMain("comment.line.earthfile");
    public final static EarthlyElementType COMMENT2 = createMain("comment.line.number-sign.earthfile");
    public final static EarthlyElementType COMMENT_PUNCTUATION = createMain("punctuation.definition.comment.earthfile");

    public final static TokenSet COMMENTS = TokenSet.create(COMMENT1, COMMENT2, COMMENT_PUNCTUATION);

    public static EarthlyElementType createMain(String name) {
        return mapToType(ROOT.add(name));
    }

    public static EarthlyElementType mapToType(TextMateScope scope) {
        EarthlyElementType earthlyElementType = cache.get(scope);
        if (earthlyElementType != null) {
            return earthlyElementType;
        }

        EarthlyElementType ret = new EarthlyElementType(scope);
        System.out.println("Putting into cache " + scope);
        cache.put(scope, ret);
        return ret;
    }
}
