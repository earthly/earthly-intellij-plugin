package dev.earthly.plugin.language.syntax;

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.util.ProcessingContext;
import dev.earthly.plugin.language.syntax.highlighting.EarthlyTokenSets;
import dev.earthly.plugin.language.syntax.psi.EarthlyFunctionCallPsiElement;
import dev.earthly.plugin.language.syntax.psi.EarthlyFunctionPsiElement;
import dev.earthly.plugin.language.syntax.psi.EarthlyPsiElement;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;

final class EarthlyReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.or(psiElement(EarthlyFunctionPsiElement.class), psiElement(EarthlyFunctionCallPsiElement.class)),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                           @NotNull ProcessingContext context) {
                        System.out.println("Trying to find reference for " + element + " context " + context);
                        return new PsiReference[]{new EarthlyReference((EarthlyPsiElement) element)};
                    }
                });
    }

}