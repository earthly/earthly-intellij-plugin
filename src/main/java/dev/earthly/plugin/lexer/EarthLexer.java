package dev.earthly.plugin.lexer;

import com.intellij.psi.tree.IElementType;
import dev.earthly.plugin.language.EarthTokenTypes;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EarthLexer extends com.intellij.lexer.LexerBase {

  private static final Pattern TARGET_PATTERN = Pattern.compile("^[\\w-]+:$");

  private enum Command {
    FROM("*--platform", "--allow-privileged"),
    RUN("--push", "--no-cache", "--entrypoint", "--privileged", "*--secret", "--ssh", "*--mount", "--interactive", "--interactive-keep"),
    COPY("--dir", "--keep-ts", "--keep-own", "--if-exists", "*--platform", "--allow-privileged"),
    ARG("--required"),
    SAVE_ARTIFACT("--keep-ts", "--keep-own", "--if-exists", "--force"),
    SAVE_IMAGE("--push", "*--cache-from", "--cache-hint"),
    BUILD("*--platform", "--allow-privileged", "*--build-arg"),
    VERSION("--use-copy-include-patterns", "--referenced-save-only", "--for-in", "--require-force-for-unsafe-saves", "--no-implicit-ignore", "--earthly-version-arg", "--shell-out-anywhere", "--use-registry-for-with-docker"),
    GIT_CLONE("*--branch", "--keep-ts"),
    FROM_DOCKERFILE("*-f", "*--build-arg", "*--target", "*--platform"),
    WITH_DOCKER("*--pull", "*--load", "*--compose", "*--service", "--allow-privileged"),
    IF("--privileged", "--ssh", "--no-cache", "*--mount", "*--secret"),
    ELSE(),
    ELSE_IF(),
    END(),
    FOR("*--sep", "--privileged", "--ssh", "--no-cache", "*--mount", "*--secret"),
    WAIT(),
    LOCALLY(),
    COMMAND(),
    DO("--allow-privileged"),
    IMPORT("--allow-privileged"),
    CMD(),
    LABEL(),
    EXPOSE(),
    ENV(),
    ENTRYPOINT(),
    VOLUME(),
    USER(),
    WORKDIR(),
    HEALTHCHECK("*--interval", "*--timeout", "*--start-period", "*--retries"),
    HOST();
    private Map<String, Option> optionMap;

    Command(String... options) {
      Map<String, Option> map = new HashMap<>();
      if (options != null) {
        for (String option : options) {
          String name;
          boolean hasArguments;
          if (option.startsWith("*")) {
            name = option.substring(1);
            hasArguments = true;
          } else {
            name = option;
            hasArguments = false;
          }
          map.put(name, new Option(name, hasArguments));
        }
      }
      this.optionMap = Collections.unmodifiableMap(map);
    }

    public Map<String, Option> getOptions() {
      return optionMap;
    }

    @Override
    public String toString() {
      return name().replace('_', ' ');
    }

    public static Command from(String str) {
      return valueOf(str.replace(' ', '_'));
    }

    public static class Option {

      private String name;
      private boolean hasArguments;

      public Option(String name) {
        this(name, false);
      }

      public Option(String name, boolean hasArguments) {
        this.name = name;
        this.hasArguments = hasArguments;
      }

      public boolean hasArguments() {
        return hasArguments;
      }

      public String getName() {
        return name;
      }
    }
  }

  // A trie helps searching for a prefix detecting at the current offset when a character is not valid

  private static final Trie<String, Command> COMMANDS = new PatriciaTrie<>();

  static {
    for (Command cmd : Command.values()) {
      COMMANDS.put(cmd.toString(), cmd);
    }
  }

  /**
   * Lexer states
   */
  private enum LexerState {
    DEFAULT,          // Line with only spaces before current position
    COMMAND,
    COMMAND_OPTION,
    COMMAND_OPTION_VALUE,
    COMMAND_ARGS,        // After a non option token is lexed

    TARGET_REF,
    COMMENT,          // After a # is encountered in DEFAULT state
    BAD_LINE          // Line is not tokenized after error is found
  }

  private CharSequence buffer;
  private int tokenStart, tokenEnd, endOffset, offset;
  private LexerState initialState, state;
  private IElementType tokenType;

  private Command command; // current command

  @Override
  public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
    this.buffer = buffer;
    this.tokenStart = startOffset;
    this.endOffset = Math.min(buffer.length() - 1, endOffset);
    this.offset = startOffset;
    this.initialState = LexerState.values()[initialState];
    this.state = this.initialState;
    advance();
  }

  @Override
  public int getState() {
    return this.initialState.ordinal();
  }

  @Override
  public @Nullable IElementType getTokenType() {
    return tokenType;
  }

  @Override
  public int getTokenStart() {
    return this.tokenStart;
  }

  @Override
  public int getTokenEnd() {
    return this.tokenEnd;
  }

  @Override
  public void advance() {
    this.initialState = state;
    try {
      this.tokenStart = offset;
      if (offset > endOffset) {
        this.tokenType = null;
        return;
      }
      while (offset <= endOffset) {
        char c = buffer.charAt(offset);
        if (c == '\n') {
          if (state == LexerState.DEFAULT) {
            this.tokenType = EarthTokenTypes.LINE_BREAK;
            offset++;
            return;
          } else {
            this.state = LexerState.DEFAULT;
            if (offset != tokenEnd) {
              return;
            } else {
              continue;
            }
          }
        }

        if (offset < endOffset && buffer.charAt(offset + 1) == '\n') {
          if (c == '\\') {
            if (offset < endOffset) {
              offset += 2;
              continue;
            }
          }
        }
        /*
         * DEFAULT STATE
         */
        if (state == LexerState.DEFAULT) {
          if (c == ' ') {
            this.tokenType = EarthTokenTypes.WHITE_SPACE;
            offset++;
            return;
          } else if (c == '#') {
            this.state = LexerState.COMMENT;
            this.tokenType = EarthTokenTypes.COMMENT;
            offset++;
          } else {
            String line = getRemainingLineAt(offset).trim();
            if (TARGET_PATTERN.matcher(line).matches()) {
              this.tokenType = EarthTokenTypes.TARGET;
              offset = offset + line.length();
              return;
            } else {
              state = LexerState.COMMAND;
            }
          }
          /*
           * COMMAND STATE
           */
        } else if (state == LexerState.COMMAND) {
          this.command = getCommand(offset);
          if (this.command == null) {
            this.state = LexerState.BAD_LINE;
            this.tokenType = EarthTokenTypes.BAD_CHARACTER;
          } else {
            offset = offset + command.toString().length();
            this.state = LexerState.COMMAND_OPTION;
            this.tokenType = EarthTokenTypes.COMMAND;
            return;
          }
          /*
           * COMMAND_OPTION STATE
           */
        } else if (state == LexerState.COMMAND_OPTION) {
          if (c == ' ') {
            this.tokenType = EarthTokenTypes.WHITE_SPACE;
            offset++;
            return;
          }
          String word = getRemainingCommandOptionAt(offset);
          if (word.startsWith("--")) {
            Command.Option option = command.getOptions().get(word);
            if (option != null) {
              this.tokenType = EarthTokenTypes.COMMAND_OPTION;
              this.offset += word.length();
              if (option.hasArguments) {
                this.state = LexerState.COMMAND_OPTION_VALUE;
              }
            } else {
              this.state = LexerState.BAD_LINE;
              this.tokenType = EarthTokenTypes.BAD_CHARACTER;
              continue;
            }
            return;
          } else {
            if (command == Command.FROM) {
              this.state = LexerState.TARGET_REF;
            } else if (command == Command.BUILD) {
              this.state = LexerState.TARGET_REF;
            } else if (command == Command.DO) {
              this.state = LexerState.TARGET_REF;
            } else {
              this.state = LexerState.COMMAND_ARGS;
            }
          }
          /*
           * COMMAND_OPTION_VALUE STATE
           */
        } else if (state == LexerState.COMMAND_OPTION_VALUE) {
          if (c == '=') {
            this.tokenType = EarthTokenTypes.COMMAND_OPTION_EQUALS;
            offset++;
            return;
          }
          if (c == ' ') {
            this.tokenType = EarthTokenTypes.WHITE_SPACE;
            offset++;
            return;
          }
          this.tokenType = EarthTokenTypes.COMMAND_OPTION_VALUE;
          String word = getCommandOptionValue(offset);
          if (word.contains("\n")) {
            this.state = LexerState.BAD_LINE;
            this.tokenType = EarthTokenTypes.BAD_CHARACTER;
            continue;
          }
          this.offset += word.length();
          this.state = LexerState.COMMAND_OPTION;
          return;
          /*
           * TARGET_REF STATE
           */
        } else if (state == LexerState.TARGET_REF) {
          if (c == ' ') {
            this.state = LexerState.COMMAND_ARGS;
            this.tokenType = EarthTokenTypes.WHITE_SPACE;
            offset++;
          } else {
            this.tokenType = EarthTokenTypes.TARGET_REF;
            String target = getRemainingWord(offset);
            offset += target.length();
          }
          return;
          /*
           * COMMENT STATE
           */
        } else if (state == LexerState.COMMAND_ARGS) {
          this.tokenType = EarthTokenTypes.COMMAND_ARG;
          if (command == Command.SAVE_ARTIFACT) {
            if (comesNext(" AS LOCAL")) {
              this.tokenType = EarthTokenTypes.WHITE_SPACE;
              offset++;
              return;
            } else if (comesNext("AS LOCAL") && buffer.charAt(offset - 1) == ' ') {
              this.tokenType = EarthTokenTypes.COMMAND;
              offset += 8;
              return;
            } else if (comesBefore("AS LOCAL") && buffer.charAt(offset) == ' ') {
              this.tokenType = EarthTokenTypes.WHITE_SPACE;
              offset++;
              return;
            }
          }
          offset++;
          /*
           * COMMENT STATE
           */
        } else if (state == LexerState.COMMENT) {
          offset++;
          /*
           * BAD_LINE STATE
           */
        } else if (state == LexerState.BAD_LINE) {
          offset++;
        }
      }
    } finally {
      this.tokenEnd = offset;
    }
  }

  private void processCommandArgs() {
    if (command == Command.FROM) {
      String targetRef = getRemainingWord(tokenStart);
      this.offset = tokenStart + targetRef.length();
      this.tokenType = EarthTokenTypes.TARGET_REF;
    }
  }

  private boolean comesBefore(String str) {
    if (offset - str.length() >= 0) {
      return str.equals(buffer.subSequence(offset - str.length(), offset).toString());
    }
    return false;
  }

  private boolean comesNext(String str) {
    if (offset + str.length() - 1 <= endOffset) {
      return str.equals(buffer.subSequence(offset, offset + str.length()).toString());
    }
    return false;
  }

  /**
   * Gets the longest command match at a given position
   */
  private Command getCommand(int start) {
    int pos = start;
    StringBuilder sb = new StringBuilder();
    String ret = null;
    while (pos <= endOffset) {
      char c = buffer.charAt(pos);
      sb.append(c);
      if (COMMANDS.prefixMap(sb.toString()).size() == 0) {
        break;
      } else {
        if (COMMANDS.containsKey(sb.toString())) {
          ret = sb.toString();
        }
      }
      pos++;
    }
    if (ret != null) {
      if (start + ret.length() <= endOffset) {
        char lastChar = buffer.charAt(start + ret.length());
        if ((lastChar == ' ' || lastChar == '\n')) {
          return Command.from(ret);
        }
      } else {
        return Command.from(ret);
      }
    }
    return null;
  }

  /**
   * Gets the remaining chars in the same line, from a given buffer position
   */
  private String getRemainingLineAt(int start) {
    int i = start;
    while (i <= endOffset) {
      char c = buffer.charAt(i);
      if (c == '\n') {
        break;
      }
      i++;
    }
    return buffer.subSequence(start, i).toString();
  }

  private String getCommandOptionValue(int start) {
    int i = start;
    int parenthesisCount = 0;
    boolean quoted = false;
    while (i <= endOffset) {
      char c = buffer.charAt(i);
      if (quoted) {
        if (c == '"' && buffer.charAt(i - 1) != '\\') {
          quoted = false;
        }
        if (c == '\n') {
          break;
        }
      } else {
        if (c == '(') {
          parenthesisCount++;
        } else if (c == ')') {
          parenthesisCount--;
        } else if (c == '"' && buffer.charAt(i - 1) != '\\') {
          quoted = true;
        }
        if (parenthesisCount == 0 && (c == ' ' || c < 32 || c > 126)) {
          break;
        }
      }
      i++;
    }
    return buffer.subSequence(start, i).toString();
  }

  private String getRemainingCommandOptionAt(int start) {
    return getRemainingWord(start);
  }

  private String getRemainingWord(int start) {
    int i = start;
    while (i < endOffset) {
      char c = buffer.charAt(i);
      if (c == '=' || c == ' ' || c < 32 || c > 126) {
        break;
      }
      i++;
    }
    return buffer.subSequence(start, i).toString();
  }

  @Override
  public @NotNull CharSequence getBufferSequence() {
    return buffer;
  }

  @Override
  public int getBufferEnd() {
    return endOffset;
  }
}
