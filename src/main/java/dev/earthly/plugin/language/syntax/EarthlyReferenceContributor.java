package dev.earthly.plugin.language.syntax;

import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import dev.earthly.plugin.language.syntax.psi.EarthlyPsiElement;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;

final class EarthlyReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(psiElement(EarthlyPsiElement.class),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                           @NotNull ProcessingContext context) {
                        return new PsiReference[]{new EarthlyReference((EarthlyPsiElement) element)};
                    }
                });
    }
}
