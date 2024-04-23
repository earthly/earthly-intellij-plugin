package dev.earthly.plugin.language.syntax;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import dev.earthly.plugin.language.syntax.parser.EarthlyFile;
import dev.earthly.plugin.language.syntax.psi.*;
import dev.earthly.plugin.metadata.EarthlyFileType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EarthlyUtil {

    /**
     * Searches the entire project for Earthly language files with instances of the Earthly functions with the given name.
     *
     * @param project current project
     * @param element to check
     * @return matching functions
     */
    public static List<PsiElement> findFunctions(Project project, EarthlyPsiElement element) {
        String key = element.getText();
        Class<? extends EarthlyPsiElement> lookFor = getLookFor(element);
        List<PsiElement> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(EarthlyFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            EarthlyFile file = (EarthlyFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (file != null) {
                List<PsiElement> functions = PsiTreeUtil.getChildrenOfTypeAsList(file, lookFor);
                for (PsiElement fun : functions) {
                    if (fun.getText().equals(key)) {
                        result.add(fun);
                    }
                }
            }
        }
        return result;
    }

    @NotNull
    private static Class<? extends EarthlyPsiElement> getLookFor(EarthlyPsiElement element) {
        if (element instanceof EarthlyFunctionPsiElement) {
            return EarthlyFunctionCallPsiElement.class;
        } else if (element instanceof EarthlyFunctionCallPsiElement) {
                return EarthlyFunctionPsiElement.class;
        } else if (element instanceof EarthlyTargetPsiElement) {
            return EarthlyTargetCallPsiElement.class;
        } else {
            return EarthlyTargetPsiElement.class;
        }
    }
}