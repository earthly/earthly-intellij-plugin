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
import dev.earthly.plugin.metadata.EarthlyFileType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EarthlyUtil {

    /**
     * Searches the entire project for Earthly language files with instances of the Earthly property with the given key.
     *
     * @param project current project
     * @param key     to check
     * @return matching properties
     */
    public static List<EarthlyProperty> findProperties(Project project, String key) {
        List<EarthlyProperty> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(EarthlyFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            EarthlyFile simpleFile = (EarthlyFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                EarthlyProperty[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, EarthlyProperty.class);
                if (properties != null) {
                    for (EarthlyProperty property : properties) {
                        if (key.equals(property.getKey())) {
                            result.add(property);
                        }
                    }
                }
            }
        }
        return result;
    }

    public static List<EarthlyProperty> findProperties(Project project) {
        List<EarthlyProperty> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(EarthlyFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            EarthlyFile simpleFile = (EarthlyFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                EarthlyProperty[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, EarthlyProperty.class);
                if (properties != null) {
                    Collections.addAll(result, properties);
                }
            }
        }
        return result;
    }

    /**
     * Attempts to collect any comment elements above the Earthly key/value pair.
     */
    public static @NotNull String findDocumentationComment(EarthlyProperty property) {
        List<String> result = new LinkedList<>();
        PsiElement element = property.getPrevSibling();
        while (element instanceof PsiComment || element instanceof PsiWhiteSpace) {
            if (element instanceof PsiComment) {
                String commentText = element.getText().replaceFirst("[!# ]+", "");
                result.add(commentText);
            }
            element = element.getPrevSibling();
        }
        return StringUtil.join(Lists.reverse(result), "\n ");
    }

}