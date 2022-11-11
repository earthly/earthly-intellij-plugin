package dev.earthly.plugin.metadata;

import com.intellij.lang.Language;

public class EarthlyLanguage extends Language {

  public static final EarthlyLanguage INSTANCE = new EarthlyLanguage();

  private EarthlyLanguage() {
    super("Earthly");
  }
}
