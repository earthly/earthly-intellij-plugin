package dev.earthly.plugin.util;

public enum AnsiCode {

  BLACK("\u001B[30m"),
  RED("\u001B[31m"),
  GREEN("\u001B[32m"),
  YELLOW("\u001B[33m"),
  BLUE("\u001B[34m"),
  GRAY("\u001B[90m"),
  PURPLE("\u001B[35m"),
  CYAN("\u001B[36m"),
  WHITE("\u001B[37m"),
  BG_GREEN("\u001b[42m"),
  BG_BLACK("\u001b[40m"),
  BG_RED("\u001b[41m"),
  BG_YELLOW("\u001b[43m"),
  BG_BLUE("\u001b[44m"),
  BG_MAGENTA("\u001b[45m"),
  BG_CYAN("\u001b[46m"),
  BG_WHITE("\u001b[47m"),
  RESET("\u001B[0m"),
  BOLD("\u001b[1m"),
  FAINT("\u001b[2m"),
  UNDERLINED("\u001b[4m"),
  BLINK("\u001b[5m"),
  REVERSED("\u001b[7m"),
  INVISIBLE("\u001b[8m"),
  END_OF_LINE("\u001b[K"),
  MOVE_TO_TOP("\u001b[0;0f"),
  MOVE_CURSOR_UP("\u001b[1A"),
  MOVE_CURSOR_DOWN("\u001b[1B"),
  NO_WRAP("\u001b[?7l"),
  WRAP("\u001b[?7h"),
  CLEAR("\u001b[2J");

  private static final ThreadLocal<Boolean> ACTIVE = new ThreadLocal<Boolean>();

  public static void setActive(boolean active) {
    ACTIVE.set(active);
  }

  public static boolean isActive() {
    Boolean value = ACTIVE.get();
    return value != null && value;
  }

  private final String code;

  AnsiCode(String code) {
    this.code = code;
  }

  public String getCode() {
    if (isActive()) {
      return code;
    } else {
      return "";
    }
  }

  @Override
  public String toString() {
    return getCode();
  }
}