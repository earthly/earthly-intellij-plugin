package dev.earthly.plugin.language;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import dev.earthly.plugin.metadata.EarthlyLanguage;

public class EarthTokenTypes {

  /**
   * Token types to be returned by the lexer
   */
  public static IElementType TARGET = new IElementType("TARGET", EarthlyLanguage.INSTANCE);

  public static IElementType TARGET_REF = new IElementType("TARGET_REF", EarthlyLanguage.INSTANCE);
  public static IElementType COMMAND = new IElementType("COMMAND", EarthlyLanguage.INSTANCE);
  public static IElementType COMMAND_OPTION = new IElementType("COMMAND_OPTION", EarthlyLanguage.INSTANCE);
  public static IElementType COMMAND_OPTION_VALUE = new IElementType("COMMAND_OPTION_VALUE", EarthlyLanguage.INSTANCE);
  public static IElementType COMMAND_OPTION_EQUALS = new IElementType("COMMAND_OPTION_EQUALS", EarthlyLanguage.INSTANCE);
  public static IElementType COMMAND_ARG = new IElementType("COMMAND_ARG", EarthlyLanguage.INSTANCE);
  public static IElementType COMMENT = new IElementType("COMMENT", EarthlyLanguage.INSTANCE);
  public static IElementType LINE_BREAK = new IElementType("LINE_BREAK", EarthlyLanguage.INSTANCE);
  public static IElementType WHITE_SPACE = TokenType.WHITE_SPACE;
  public static IElementType BAD_CHARACTER = TokenType.BAD_CHARACTER;
}
