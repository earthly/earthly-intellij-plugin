package dev.earthly.plugin.language.syntax.commenter;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

final class EarthlyCommenter implements Commenter {

    @Override
    public String getLineCommentPrefix() {
        System.out.println("Biore line preffix");
        return "#";
    }

    @Override
    public String getBlockCommentPrefix() {
        return "/*1*";
    }

    @Nullable
    @Override
    public String getBlockCommentSuffix() {
        return "*2*/";
    }

    @Nullable
    @Override
    public String getCommentedBlockCommentPrefix() {
        return null;
    }

    @Nullable
    @Override
    public String getCommentedBlockCommentSuffix() {
        return null;
    }
}