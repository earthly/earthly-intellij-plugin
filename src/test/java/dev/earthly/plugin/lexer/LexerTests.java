package dev.earthly.plugin.lexer;

import com.intellij.psi.tree.IElementType;
import dev.earthly.plugin.util.AnsiCode;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Assert;

public class LexerTests {

  @org.junit.jupiter.api.Test
  public void regressionTest() throws IOException {
    AnsiCode.setActive(true);
    EarthLexer lexer = new EarthLexer();
    String code = new String(LexerTests.class.getClassLoader().getResourceAsStream("Earthfile0").readAllBytes());
    lexer.start(code);
    Map<IElementType, Integer> counters = new LinkedHashMap<>();
    IElementType tokenType;
    while ((tokenType = lexer.getTokenType()) != null) {
      System.out.print(AnsiCode.YELLOW + "[" + tokenType + "->]" + AnsiCode.RESET + code.substring(lexer.getTokenStart(), lexer.getTokenEnd()));
      Integer counter = counters.get(tokenType);
      if (counter == null) {
        counter = 0;
      }
      counters.put(tokenType, counter+1);
      lexer.advance();
    }
    System.out.println();
    Assert.assertEquals("{COMMAND=104, WHITE_SPACE=378, COMMAND_ARG=93, LINE_BREAK=134, COMMENT=5, TARGET=13, COMMAND_OPTION=22, COMMAND_OPTION_VALUE=22, COMMAND_OPTION_EQUALS=5}",counters.toString());
  }
}