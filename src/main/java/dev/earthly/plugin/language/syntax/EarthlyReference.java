package dev.earthly.plugin.language.syntax;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import dev.earthly.plugin.language.syntax.psi.EarthlyPsiElement;
import dev.earthly.plugin.metadata.EarthlyIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

final class EarthlyReference extends PsiReferenceBase<EarthlyPsiElement> implements PsiPolyVariantReference {

    EarthlyReference(@NotNull EarthlyPsiElement element) {
        super(element, new TextRange(0, element.getTextLength()));
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        final List<PsiElement> properties = EarthlyUtil.findFunctionsByName(project, myElement);
        List<ResolveResult> results = new ArrayList<>();
        for (PsiElement property : properties) {
            if (property != myElement)
                results.add(new PsiElementResolveResult(property));
        }
        return results.toArray(new ResolveResult[0]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @Override
    public Object @NotNull [] getVariants() {
        Project project = myElement.getProject();
        final List<PsiElement> properties = EarthlyUtil.findFunctionsByName(project, myElement);
        List<LookupElement> variants = new ArrayList<>();
        for (final PsiElement property : properties) {
            if (property != myElement) {
                variants.add(LookupElementBuilder
                        .create(property).withIcon(EarthlyIcons.FILE)
                        .withTypeText(property.getContainingFile().getName())
                );
            }
        }
        return variants.toArray();
    }
}
