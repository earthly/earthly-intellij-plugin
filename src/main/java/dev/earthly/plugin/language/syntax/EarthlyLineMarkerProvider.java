package dev.earthly.plugin.language.syntax;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import dev.earthly.plugin.language.syntax.highlighting.EarthlyTokenSets;
import dev.earthly.plugin.language.syntax.lexer.EarthlyElementType;
import dev.earthly.plugin.language.syntax.psi.EarthlyFunctionPsiElement;
import dev.earthly.plugin.language.syntax.psi.EarthlyPsiElement;
import dev.earthly.plugin.metadata.EarthlyIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

final class EarthlyLineMarkerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element,
                                            @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        System.out.println("In marker: " + element);
        if (!(element instanceof EarthlyFunctionPsiElement eapsi)) {
            return;
        }
        System.out.println("Have " + eapsi);

        Project project = element.getProject();
        String possibleProperties = element.getText();
        final List<PsiElement> functions = EarthlyUtil.findFunctions(project, eapsi);
        System.out.println("Have " + possibleProperties + " --> " + functions);

        if (!functions.isEmpty()) {
//             Add the property to a collection of line marker info
            NavigationGutterIconBuilder<PsiElement> builder =
                    NavigationGutterIconBuilder.create(EarthlyIcons.FILE)
                            .setTargets(functions)
                            .setTooltipText("Navigate to Earthly language property");
            result.add(builder.createLineMarkerInfo(element));
        }
    }

}