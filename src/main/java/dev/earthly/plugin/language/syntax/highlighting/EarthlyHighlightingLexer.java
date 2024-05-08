package dev.earthly.plugin.language.syntax.highlighting;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Interner;
import dev.earthly.plugin.language.syntax.lexer.EarthlyElementType;
import dev.earthly.plugin.metadata.EarthlyFileType;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.jetbrains.plugins.textmate.bundles.Bundle;
import org.jetbrains.plugins.textmate.bundles.VSCBundle;
import org.jetbrains.plugins.textmate.language.TextMateLanguageDescriptor;
import org.jetbrains.plugins.textmate.language.syntax.TextMateSyntaxTable;
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateElementType;
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateHighlightingLexer;
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateScope;
import org.jetbrains.plugins.textmate.plist.CompositePlistReader;
import org.jetbrains.plugins.textmate.plist.Plist;
import org.jetbrains.plugins.textmate.plist.PlistReader;

public class EarthlyHighlightingLexer extends TextMateHighlightingLexer {

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
      InputStream grammar = EarthlyHighlightingLexer.class.getClassLoader().getResourceAsStream("earthfile.tmLanguage.json");
      Plist plist = plistReader.read(grammar);
      CharSequence scopeName = syntaxTable.loadSyntax(plist, interner);
      return new TextMateLanguageDescriptor(scopeName, syntaxTable.getSyntax(scopeName));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
