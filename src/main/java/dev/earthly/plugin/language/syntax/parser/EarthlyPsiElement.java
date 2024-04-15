package dev.earthly.plugin.language.syntax.parser;

import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.tree.IElementType;

class EarthlyPsiElement extends CompositePsiElement {
  protected EarthlyPsiElement(IElementType type) {
    super(type);
  }
}
