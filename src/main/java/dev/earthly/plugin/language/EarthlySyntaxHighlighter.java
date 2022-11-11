package dev.earthly.plugin.language;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import dev.earthly.plugin.lexer.EarthLexer;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class EarthlySyntaxHighlighter extends SyntaxHighlighterBase {

  public static final TextAttributesKey TARGET_REF = createTextAttributesKey("TARGET_REF", DefaultLanguageHighlighterColors.METADATA);
  public static final TextAttributesKey TARGET = createTextAttributesKey("EARTHLY_TARGET", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
  public static final TextAttributesKey COMMAND = createTextAttributesKey("EARTHLY_COMMAND", DefaultLanguageHighlighterColors.KEYWORD);

  public static final TextAttributesKey COMMAND_OPTION = createTextAttributesKey("EARTHLY_COMMAND_OPTION", DefaultLanguageHighlighterColors.KEYWORD);
  public static final TextAttributesKey COMMAND_OPTION_VALUE = createTextAttributesKey("EARTHLY_COMMAND_OPTION_VALUE", DefaultLanguageHighlighterColors.STRING);

  public static final TextAttributesKey COMMENT = createTextAttributesKey("EARTHLY_COMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
  public static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("EARTHLY_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);


  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new EarthLexer();
  }

  @Override
  public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
    TextAttributesKey key;
    if (tokenType == EarthTokenTypes.COMMAND) {
      key = COMMAND;
    } else if (tokenType == EarthTokenTypes.COMMAND_OPTION) {
      key = COMMAND_OPTION;
    } else if (tokenType == EarthTokenTypes.COMMAND_OPTION_VALUE) {
      key = COMMAND_OPTION_VALUE;
    } else if (tokenType == EarthTokenTypes.TARGET) {
      key = TARGET;
    } else if (tokenType == EarthTokenTypes.TARGET_REF) {
      key = TARGET_REF;
    } else if (tokenType == EarthTokenTypes.BAD_CHARACTER) {
      key = BAD_CHARACTER;
    } else if (tokenType == EarthTokenTypes.COMMENT) {
      key = COMMENT;
    } else {
      return new TextAttributesKey[0];
    }
    return new TextAttributesKey[]{key};
  }

}