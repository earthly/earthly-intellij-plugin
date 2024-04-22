package dev.earthly.plugin.language.syntax;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import dev.earthly.plugin.language.syntax.parser.EarthlyFile;
import dev.earthly.plugin.language.syntax.psi.EarthlyFunctionCallPsiElement;
import dev.earthly.plugin.language.syntax.psi.EarthlyFunctionPsiElement;
import dev.earthly.plugin.language.syntax.psi.EarthlyPsiElement;
import dev.earthly.plugin.metadata.EarthlyFileType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
        Class<? extends EarthlyPsiElement> lookFor = element instanceof EarthlyFunctionPsiElement ? EarthlyFunctionCallPsiElement.class : EarthlyFunctionPsiElement.class;
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
}