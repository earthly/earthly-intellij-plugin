package dev.earthly.plugin.language.syntax;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import dev.earthly.plugin.language.syntax.psi.EarthlyPsiElement;
import dev.earthly.plugin.metadata.EarthlyIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

final class EarthlyLineMarkerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element,
                                            @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        if (!(element instanceof EarthlyPsiElement eapsi)) {
            return;
        }

        Project project = element.getProject();
        final List<PsiElement> functions = EarthlyUtil.findFunctionsByName(project, eapsi);

        if (!functions.isEmpty()) {
            NavigationGutterIconBuilder<PsiElement> builder =
                    NavigationGutterIconBuilder.create(EarthlyIcons.FILE)
                            .setTargets(functions)
                            .setTooltipText("Navigate to Earthly language property");
            result.add(builder.createLineMarkerInfo(element));
        }
    }
}
