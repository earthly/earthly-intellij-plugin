package dev.earthly.plugin.language.syntax.highlighting;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Interner;
import dev.earthly.plugin.language.syntax.lexer.EarthlyElementType;
import dev.earthly.plugin.metadata.EarthlyFileType;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
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
      Bundle earthlyBundle = new VSCBundle("earthly", getBundlePath().getAbsolutePath());
      TextMateSyntaxTable syntaxTable = new TextMateSyntaxTable();
      Interner<CharSequence> interner = Interner.createWeakInterner();
      PlistReader plistReader = new CompositePlistReader();
      Plist plist = plistReader.read(earthlyBundle.getGrammarFiles().stream().findFirst().get());
      CharSequence scopeName = syntaxTable.loadSyntax(plist, interner);
      return new TextMateLanguageDescriptor(scopeName, syntaxTable.getSyntax(scopeName));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static void deleteFile(File file) {
    File[] children = file.listFiles();
    if (children != null) {
      for (File child : children) {
        deleteFile(child);
      }
    }
    file.delete();
  }

  private static File getBundlePath() {
    try {
        String tempDir = System.getProperty("earthly.test_dir");
        File bundleDirectory;
        if (tempDir != null) {
            bundleDirectory = new File(tempDir, "bundle");
        } else {
            PluginId id = PluginId.getId("dev.earthly.earthly-intellij-plugin");
            IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(id);
            String version = plugin.getVersion();
            bundleDirectory = new File(plugin.getPluginPath() + "/bundles/" + version);
        }
      if (!bundleDirectory.exists()) {
        deleteFile(bundleDirectory.getParentFile());
        bundleDirectory.mkdirs();
        extract(new ZipInputStream(EarthlyFileType.class.getClassLoader().getResourceAsStream("vsc-bundle.zip")), bundleDirectory);
      }
      return bundleDirectory;
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  private static void extract(ZipInputStream zip, File target) throws IOException {
    try {
      ZipEntry entry;
      while ((entry = zip.getNextEntry()) != null) {
        File file = new File(target, entry.getName());
        if (!file.toPath().normalize().startsWith(target.toPath())) {
          throw new IOException("Bad zip entry");
        }
        if (entry.isDirectory()) {
          file.mkdirs();
          continue;
        }
        byte[] buffer = new byte[4096];
        file.getParentFile().mkdirs();
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        int count;
        while ((count = zip.read(buffer)) != -1) {
          out.write(buffer, 0, count);
        }
        out.close();
      }
    } finally {
      zip.close();
    }
  }
}
