package dev.earthly.plugin.language.syntax.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

class EarthlyParser implements PsiParser {
  @NotNull
  @Override
  public ASTNode parse(@NotNull IElementType root, PsiBuilder builder) {
    PsiBuilder.Marker mark = builder.mark();
    while (!builder.eof()) {
      builder.advanceLexer();
    }
    mark.done(root);
    return builder.getTreeBuilt();
  }
}
