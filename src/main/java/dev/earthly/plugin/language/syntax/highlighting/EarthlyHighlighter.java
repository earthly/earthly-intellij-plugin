package dev.earthly.plugin.language.syntax.highlighting;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import dev.earthly.plugin.language.syntax.lexer.EarthlyElementType;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class EarthlyHighlighter extends SyntaxHighlighterBase {

  private static final TextAttributesKey[] DEFAULT = new TextAttributesKey[0];
  private static final TextAttributesKey[] TARGET_REF = new TextAttributesKey[]{createTextAttributesKey("TARGET_REF", DefaultLanguageHighlighterColors.METADATA)};
  private static final TextAttributesKey[] TARGET = new TextAttributesKey[]{createTextAttributesKey("EARTHLY_TARGET", DefaultLanguageHighlighterColors.INSTANCE_FIELD)};
  private static final TextAttributesKey[] OTHER_REF = new TextAttributesKey[]{createTextAttributesKey("EARTHLY_TARGET", DefaultLanguageHighlighterColors.CLASS_REFERENCE)};
  private static final TextAttributesKey[] EXPOSE_PORT = new TextAttributesKey[]{createTextAttributesKey("EXPOSE_PORT", DefaultLanguageHighlighterColors.NUMBER)};
  private static final TextAttributesKey[] COMMENT = new TextAttributesKey[]{createTextAttributesKey("EARTHLY_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)};
  private static final TextAttributesKey[] STRING = new TextAttributesKey[]{createTextAttributesKey("EARTHLY_STRING", DefaultLanguageHighlighterColors.STRING)};
  private static final TextAttributesKey[] OPERATION_SIGN = new TextAttributesKey[]{createTextAttributesKey("OPERATION_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN)};
  private static final TextAttributesKey[] PARENTHESES = new TextAttributesKey[]{createTextAttributesKey("PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)};

  private static final TextAttributesKey[] KEYWORD = new TextAttributesKey[]{createTextAttributesKey("KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)};

  private static final TextAttributesKey[] VARIABLE = new TextAttributesKey[]{createTextAttributesKey("VARIABLE", DefaultLanguageHighlighterColors.INSTANCE_METHOD)};

  private static final Map<String, TextAttributesKey[]> COLOR_MAP = new HashMap<>();

  static {
    COLOR_MAP.put("comment.line.earthfile", COMMENT);
    COLOR_MAP.put("comment.line.number-sign.earthfile", COMMENT);
    COLOR_MAP.put("constant.numeric-port.earthfile", EXPOSE_PORT);
    COLOR_MAP.put("entity.name.class.target.earthfile", TARGET);
    COLOR_MAP.put("entity.name.function.function.earthfile", TARGET_REF);
    COLOR_MAP.put("entity.name.function.call-name.earthfile", TARGET_REF);
    COLOR_MAP.put("entity.name.type.base-image.earthfile", TARGET_REF);
    COLOR_MAP.put("entity.name.function.call-target.earthfile", OTHER_REF);
    COLOR_MAP.put("entity.name.type.target.earthfile", TARGET_REF);
    COLOR_MAP.put("entity.name.variable.artifact.earthfile", TARGET_REF);
    COLOR_MAP.put("entity.name.variable.target.earthfile", TARGET_REF);
    COLOR_MAP.put("keyword.operator.assignment.earthfile", OPERATION_SIGN);
    COLOR_MAP.put("keyword.operator.flag.earthfile", OPERATION_SIGN);
    COLOR_MAP.put("keyword.operator.shell.earthfile", OPERATION_SIGN);
    COLOR_MAP.put("punctuation.definition.comment.earthfile", COMMENT);
    COLOR_MAP.put("punctuation.definition.variable.begin.earthfile", PARENTHESES);
    COLOR_MAP.put("punctuation.definition.variable.end.earthfile", PARENTHESES);
    COLOR_MAP.put("punctuation.definition.string.begin.earthfile", STRING);
    COLOR_MAP.put("punctuation.definition.string.end.earthfile", STRING);
    COLOR_MAP.put("string.quoted.double.earthfile", STRING);
    COLOR_MAP.put("string.quoted.single.earthfile", STRING);
    COLOR_MAP.put("keyword.other.special-method.earthfile", KEYWORD);
    COLOR_MAP.put("variable.other.earthfile", VARIABLE);
  }

  private final EarthlyHighlightingLexer myLexer;

  public EarthlyHighlighter(EarthlyHighlightingLexer lexer) {
    myLexer = lexer;
  }

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return myLexer;
  }

  @Override
  public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
    if (tokenType instanceof EarthlyElementType) {
      EarthlyElementType type = (EarthlyElementType) tokenType;
      TextAttributesKey[] ret = COLOR_MAP.get(type.getScope().getScopeName());
      if (ret != null) {
        return ret;
      }
    }
    return DEFAULT;
  }
}