package dev.earthly.plugin.language.syntax;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import dev.earthly.plugin.language.syntax.highlighting.EarthlyHighlightingLexer;
import dev.earthly.plugin.language.syntax.highlighting.EarthlyTokenSets;
import dev.earthly.plugin.language.syntax.psi.EarthlyPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EarthlyFindUsagesProvider implements FindUsagesProvider {

    @Override
    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(new EarthlyHighlightingLexer(),
                EarthlyTokenSets.IDENTIFIERS,
                EarthlyTokenSets.COMMENTS,
                TokenSet.EMPTY);
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof EarthlyPsiElement;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        if (element instanceof EarthlyPsiElement) {
            return "earthly target";
        }
        return "";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof EarthlyPsiElement) {
            return "Earthly: " + element.getText();
        }
        return "";
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        if (element instanceof EarthlyPsiElement) {
            return element.getText();
        }
        return "";
    }

}
