package dev.earthly.plugin.language.syntax;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import dev.earthly.plugin.language.syntax.psi.EarthlyPsiElement;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EarthlyCompletionContributor extends CompletionContributor {
    EarthlyCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().withParent(EarthlyPsiElement.class),
                new CompletionProvider<>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        final PsiElement element = parameters.getPosition().getParent();
                        final Project project = element.getProject();

                        String key = element.getText().replace(CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED, "");
                        if (StringUtils.isEmpty(key))
                            return;
                        List<PsiElement> matched = EarthlyUtil.findAllFunctions(project, (EarthlyPsiElement) element);
                        for (PsiElement potential : matched) {
                            if (potential.getText().startsWith(key))
                                resultSet.addElement(LookupElementBuilder.create(potential.getText()));
                        }
                    }
                }
        );
    }
}
