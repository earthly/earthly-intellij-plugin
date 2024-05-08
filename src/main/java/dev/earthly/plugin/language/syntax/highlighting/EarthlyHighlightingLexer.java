package dev.earthly.plugin.language.syntax.highlighting;

import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Interner;
import org.jetbrains.plugins.textmate.language.TextMateLanguageDescriptor;
import org.jetbrains.plugins.textmate.language.syntax.TextMateSyntaxTable;
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateElementType;
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateHighlightingLexer;
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateScope;
import org.jetbrains.plugins.textmate.plist.CompositePlistReader;
import org.jetbrains.plugins.textmate.plist.Plist;
import org.jetbrains.plugins.textmate.plist.PlistReader;

import java.io.InputStream;
import java.io.BufferedInputStream;

public class EarthlyHighlightingLexer extends TextMateHighlightingLexer {

  private final static String EARTHFILE_DESC = "earthfile.tmLanguage.json";

  public EarthlyHighlightingLexer() {
    super(getTextMateLanguageDescriptor(), 20000);
  }

  public IElementType getTokenType() {
    TextMateElementType tokenType = (TextMateElementType) super.getTokenType();
    if (tokenType == null) {
      return null;
    }
    TextMateScope scope = tokenType.getScope();
    return EarthlyTokenSets.mapToType(scope);
  }

  private static TextMateLanguageDescriptor getTextMateLanguageDescriptor() {
    try {
      TextMateSyntaxTable syntaxTable = new TextMateSyntaxTable();
      Interner<CharSequence> interner = Interner.createWeakInterner();
      PlistReader plistReader = new CompositePlistReader();
      Plist plist = plistReader.read(getLanguageGrammar());
      CharSequence scopeName = syntaxTable.loadSyntax(plist, interner);
      return new TextMateLanguageDescriptor(scopeName, syntaxTable.getSyntax(scopeName));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static InputStream getLanguageGrammar() {
    return new BufferedInputStream(EarthlyHighlightingLexer.class.getClassLoader().getResourceAsStream(EARTHFILE_DESC));
  }
}
