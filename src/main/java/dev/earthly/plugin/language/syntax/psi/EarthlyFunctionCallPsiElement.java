package dev.earthly.plugin.language.syntax.psi;

import com.intellij.lang.ASTNode;

public class EarthlyFunctionCallPsiElement extends EarthlyPsiElement {
  public EarthlyFunctionCallPsiElement(ASTNode node) {
    super(node);
  }
}

