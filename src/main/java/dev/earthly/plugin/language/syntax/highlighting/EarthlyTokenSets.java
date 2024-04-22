package dev.earthly.plugin.language.syntax.highlighting;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import dev.earthly.plugin.language.syntax.lexer.EarthlyElementType;
import dev.earthly.plugin.language.syntax.psi.EarthlyCommentPsiElement;
import dev.earthly.plugin.language.syntax.psi.EarthlyFunctionCallPsiElement;
import dev.earthly.plugin.language.syntax.psi.EarthlyFunctionPsiElement;
import dev.earthly.plugin.language.syntax.psi.EarthlyPsiElement;
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateScope;

import java.util.concurrent.ConcurrentHashMap;

public class EarthlyTokenSets {
    private static final TextMateScope ROOT_SCOPE = new TextMateScope("source.earthfile", TextMateScope.EMPTY);
    private static final ConcurrentHashMap<String, EarthlyElementType> cache = new ConcurrentHashMap<>();

    public final static EarthlyElementType ROOT = mapToType(ROOT_SCOPE);

    public final static EarthlyElementType EMPTY = mapToType(TextMateScope.EMPTY);
    public final static EarthlyElementType FUNCTION = createMain("entity.name.function.function.earthfile");
    public final static EarthlyElementType FUNCTION_DOTS = createMain("entity.name.function.function-dots.earthfile");
    public final static EarthlyElementType FUNCTION_CALL = createMain("entity.name.function.call-name.earthfile");
    public final static EarthlyElementType COMMENT1 = createMain("comment.line.earthfile");
    public final static EarthlyElementType COMMENT2 = createMain("comment.line.number-sign.earthfile");
    public final static EarthlyElementType COMMENT_PUNCTUATION = createMain("comment.line.number-sign.earthfile", "punctuation.definition.comment.earthfile");

    public final static EarthlyElementType EOL = createMain("constant.eol.earthfile");
    public final static EarthlyElementType INDENT = createMain("constant.indent.earthfile");
    public final static EarthlyElementType BLOCK = createMain("entity.block");

    public final static TokenSet COMMENTS = TokenSet.create(COMMENT1, COMMENT2, COMMENT_PUNCTUATION);

    public static EarthlyElementType createMain(String... names) {
        TextMateScope add = ROOT_SCOPE;
        for (String name : names) {
            add = add.add(name);
        }
        return mapToType(add);
    }

    public static EarthlyElementType mapToType(TextMateScope scope) {
        EarthlyElementType earthlyElementType = cache.get(scope.toString());
        if (earthlyElementType != null) {
            return earthlyElementType;
        }

        EarthlyElementType ret = new EarthlyElementType(scope);
//        System.out.println("Putting into cache " + scope.toString());
        cache.put(scope.toString(), ret);
        return ret;
    }

    public static class Factory {
        public static EarthlyPsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();
            if (COMMENTS.contains(type)) {
                return new EarthlyCommentPsiElement(node);
            }
            if (type.equals(FUNCTION)) {
                return new EarthlyFunctionPsiElement(node);
            }
            if (type.equals(FUNCTION_CALL)) {
                return new EarthlyFunctionCallPsiElement(node);
            }
            return new EarthlyPsiElement(node);
        }
    }
}
