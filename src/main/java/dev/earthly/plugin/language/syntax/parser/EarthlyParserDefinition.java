package dev.earthly.plugin.language.syntax.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import dev.earthly.plugin.language.syntax.highlighting.EarthlyTokenSets;
import dev.earthly.plugin.metadata.EarthlyLanguage;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;

public class EarthlyParserDefinition implements ParserDefinition {
    private final static IFileElementType FILE_ELEMENT_TYPE = new IFileElementType("Earthly", EarthlyLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new EarthlyANTLRLexerAdaptor();
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        final EarthParser parser = new EarthParser(null);
        return new ANTLRParserAdaptor(EarthlyLanguage.INSTANCE, parser) {
            @Override
            protected ParseTree parse(Parser parser, IElementType root) {
                // start rule depends on root passed in; sometimes we want to create an ID node etc...
                if (root instanceof IFileElementType) {
                    return ((EarthParser) parser).earthFile();
                }
                // let's hope it's an ID as needed by "rename function"
                return ((EarthParser) parser).earthFile();
            }
        };
//        return new EarthlyParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE_ELEMENT_TYPE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return TokenSet.EMPTY;
//        return EarthlyTokenSets.WHITESPACE;
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
//        return EarthlyTokenSets.STRING;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return EarthlyTokenSets.Factory.createElement(node);
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
