package dev.earthly.plugin.language.syntax.lexer;

import com.intellij.psi.tree.IElementType;
import dev.earthly.plugin.metadata.EarthlyLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateElementType;
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateScope;

import java.util.concurrent.ConcurrentHashMap;

public class EarthlyElementType extends IElementType {
  private final TextMateScope myScope;

  public EarthlyElementType(@NotNull TextMateScope scope) {
    super(scope.toString(), EarthlyLanguage.INSTANCE);
    myScope = scope;
  }

  @NotNull
  public TextMateScope getScope() {
    return myScope;
  }

  @Override
  public int hashCode() {
    return getScope().hashCode();
  }

  @Override
  public String toString() {
    return myScope.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return ((EarthlyElementType)o).getScope().equals(getScope());
  }
}
