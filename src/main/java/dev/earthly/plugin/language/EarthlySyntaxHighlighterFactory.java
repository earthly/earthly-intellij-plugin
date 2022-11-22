package dev.earthly.plugin.language;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.Interner;
import dev.earthly.plugin.metadata.EarthlyFileType;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.jetbrains.annotations.NotNull;

public class EarthlySyntaxHighlighterFactory extends SyntaxHighlighterFactory {

  @NotNull
  @Override
  public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
    return new EarthlySyntaxHighlighter();
  }


  private static TextMate getTextMateLanguageDescriptor() {
    try {
      Bundle earthlyBundle = new VSCBundle("earthly", getBundlePath().getAbsolutePath());
      TextMateSyntaxTable syntaxTable = new TextMateSyntaxTable();
      Interner<CharSequence> interner = Interner.createWeakInterner();
      PlistReader plistReader = new CompositePlistReader();
      Plist plist = plistReader.read(earthlyBundle.getGrammarFiles().stream().findFirst().get());
      CharSequence scopeName = syntaxTable.loadSyntax(plist, interner);
      TextMateLanguageDescriptor textMateLanguageDescriptor = new TextMateLanguageDescriptor(scopeName, syntaxTable.getSyntax(scopeName));
      System.out.println(textMateLanguageDescriptor);


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
      IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(PluginId.getId("dev.earthly.earthly-intellij-plugin"));
      String version = plugin.getVersion();
      File bundleDirectory = new File(plugin.getPluginPath() + "/bundles/" + version);
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
