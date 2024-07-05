package dev.earthly.plugin.language.syntax.lexer;

import com.intellij.openapi.editor.ex.util.DataStorage;
import com.intellij.openapi.editor.ex.util.ShortBasedStorage;
import com.intellij.psi.tree.IElementType;
import dev.earthly.plugin.language.syntax.highlighting.EarthlyTokenSets;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.textmate.language.syntax.lexer.TextMateScope;

public class EarthlyLexerDataStorage extends ShortBasedStorage {

  private final Object2IntMap<EarthlyElementType> tokenTypeMap;
  private final List<EarthlyElementType> tokenTypes;

  public EarthlyLexerDataStorage() {
    this(new Object2IntOpenHashMap<>(), new ArrayList<>());
  }

  private EarthlyLexerDataStorage(@NotNull Object2IntMap<EarthlyElementType> tokenTypeMap,
      @NotNull List<EarthlyElementType> tokenTypes) {
    super();
    this.tokenTypeMap = tokenTypeMap;
    this.tokenTypes = tokenTypes;
  }

  private EarthlyLexerDataStorage(short @NotNull [] data,
      @NotNull Object2IntMap<EarthlyElementType> tokenTypeMap,
      @NotNull List<EarthlyElementType> tokenTypes) {
    super(data);
    this.tokenTypeMap = tokenTypeMap;
    this.tokenTypes = tokenTypes;
  }

  @Override
  public int packData(IElementType tokenType, int state, boolean isRestartableState) {
    if (tokenType instanceof EarthlyElementType) {
      synchronized (tokenTypeMap) {
        if (tokenTypeMap.containsKey(tokenType)) {
          return tokenTypeMap.getInt(tokenType) * (isRestartableState ? 1 : -1);
        }
        int data = tokenTypes.size() + 1;
        tokenTypes.add((EarthlyElementType) tokenType);
        tokenTypeMap.put((EarthlyElementType) tokenType, data);
        return isRestartableState ? data : -data;
      }
    }
    return 0;
  }

  @Override
  public IElementType unpackTokenFromData(int data) {
    return data != 0 ? tokenTypes.get(Math.abs(data) - 1) : EarthlyTokenSets.EMPTY;
  }

  @Override
  public DataStorage copy() {
    return new EarthlyLexerDataStorage(myData, tokenTypeMap, tokenTypes);
  }

  @Override
  public DataStorage createStorage() {
    return new EarthlyLexerDataStorage(tokenTypeMap, tokenTypes);
  }
}

