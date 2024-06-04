package dev.earthly.plugin.language.syntax;

import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class EarthlyNamesValidator implements NamesValidator {
    private static final Predicate<String> TARGET_PATTERN = Pattern.compile("^[a-z][a-zA-Z0-9.\\-]*$").asMatchPredicate();
    private static final Predicate<String> FUNCTION_PATTERN = Pattern.compile("^[A-Z][a-zA-Z0-9._]*$").asMatchPredicate();
    @Override
    public boolean isKeyword(@NotNull String name, Project project) {
        List<String> keywords = List.of("base");
        return keywords.contains(name);
    }

    @Override
    public boolean isIdentifier(@NotNull String name, Project project) {
        final int len = name.length();
        if (len == 0) return false;
        if (isKeyword(name, project))
            return false;
        return TARGET_PATTERN.test(name) || FUNCTION_PATTERN.test(name);
    }
}
