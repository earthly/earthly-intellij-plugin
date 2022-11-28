package dev.earthly.plugin.language.syntax.highlighting;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.DataStorage;
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.EditorHighlighterProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import dev.earthly.plugin.language.syntax.lexer.EarthlyLexerDataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EarthlyEditorHighlighterProvider implements EditorHighlighterProvider {

    @Override
    public EditorHighlighter getEditorHighlighter(@Nullable Project project,
        @NotNull FileType fileType,
        @Nullable VirtualFile virtualFile,
        @NotNull EditorColorsScheme colors) {
      return new EarthlyEditorHighlighter(new EarthlySyntaxHighlighterFactory().getSyntaxHighlighter(project,virtualFile), colors);
    }

    private static final class EarthlyEditorHighlighter extends LexerEditorHighlighter {
      private EarthlyEditorHighlighter(@Nullable SyntaxHighlighter highlighter, @NotNull EditorColorsScheme colors) {

        super(highlighter != null ? highlighter : new EarthlyHighlighter(null), colors);
      }

      @NotNull
      @Override
      protected DataStorage createStorage() {
        return new EarthlyLexerDataStorage();
      }
    }
  }
