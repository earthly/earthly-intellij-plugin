package dev.earthly.plugin.language.syntax.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.EmptyLexer;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.codeInsight.CommentUtilCore;
import dev.earthly.plugin.language.syntax.highlighting.EarthlyHighlightingLexer;
import dev.earthly.plugin.language.syntax.highlighting.EarthlyTokenSets;
import dev.earthly.plugin.language.syntax.lexer.EarthlyElementType;
import dev.earthly.plugin.metadata.EarthlyLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.textmate.TextMateLanguage;
import org.jetbrains.plugins.textmate.TextMateService;
import org.jetbrains.plugins.textmate.language.TextMateLanguageDescriptor;
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateScope;

public class EarthlyParserDefinition implements ParserDefinition {
    private final static IFileElementType FILE_ELEMENT_TYPE = new IFileElementType("Earthly", EarthlyLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new EarthlyHighlightingLexer();
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new EarthlyParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE_ELEMENT_TYPE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return TokenSet.WHITE_SPACE;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return EarthlyTokenSets.COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        System.out.println("Tworze element " + node + " ---> " + CommentUtilCore.isComment(node));
        return new EarthlyPsiElement(node.getElementType());
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new EarthlyFile(viewProvider);
    }

    @Override
    public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}
