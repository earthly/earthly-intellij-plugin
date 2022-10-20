package dev.earthly.plugin.metadata;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;

public class EarthlyFileType extends LanguageFileType {

  public static final EarthlyFileType INSTANCE = new EarthlyFileType();

  private EarthlyFileType() {
    super(EarthlyLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "Earthfile";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Earthfile";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return EarthlyIcons.FILE;
  }
}
