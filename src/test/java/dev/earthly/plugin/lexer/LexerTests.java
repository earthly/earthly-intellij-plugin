package dev.earthly.plugin.lexer;

import com.intellij.psi.tree.IElementType;
import dev.earthly.plugin.util.AnsiCode;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Assert;

public class LexerTests {

  private static void test(String resource, boolean checkExpected) throws IOException {
    AnsiCode.setActive(true);
    EarthLexer lexer = new EarthLexer();
    String code = new String(LexerTests.class.getClassLoader().getResourceAsStream(resource).readAllBytes());
      lexer.start(code);
    Map<IElementType, Integer> counters = new LinkedHashMap<>();
    IElementType tokenType;
    StringBuilder sb = new StringBuilder();
    while ((tokenType = lexer.getTokenType()) != null) {
      sb.append("[" + tokenType + "->]" + code.substring(lexer.getTokenStart(), lexer.getTokenEnd()));
      System.out.print(AnsiCode.YELLOW + "[" + tokenType + "->]" + AnsiCode.RESET + code.substring(lexer.getTokenStart(), lexer.getTokenEnd()));
      Integer counter = counters.get(tokenType);
      if (counter == null) {
        counter = 0;
      }
      counters.put(tokenType, counter+1);
      lexer.advance();
    }
    if(checkExpected) {
      String expected = new String(LexerTests.class.getClassLoader().getResourceAsStream(resource + ".expected").readAllBytes());
      Assert.assertEquals(expected, sb.toString());
    }
  }

  @org.junit.jupiter.api.Test
  public void testGlobal() throws IOException {
    test("Earthfile-global", true);
  }

  @org.junit.jupiter.api.Test
  public void testComments() throws IOException {
    test("Earthfile-comments", true);
  }

  @org.junit.jupiter.api.Test
  public void testManual() throws IOException {
    test("Earthfile-manual", false);
  }
}