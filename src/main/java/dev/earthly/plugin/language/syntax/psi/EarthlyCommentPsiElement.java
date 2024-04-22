package dev.earthly.plugin.language.syntax.psi;

import com.intellij.lang.ASTNode;

public class EarthlyCommentPsiElement extends EarthlyPsiElement {
  public EarthlyCommentPsiElement(ASTNode node) {
    super(node);
  }
}

