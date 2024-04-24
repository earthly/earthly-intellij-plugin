package dev.earthly.plugin.language.syntax.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EarthlyPsiElement extends ASTWrapperPsiElement implements PsiNameIdentifierOwner {
    public EarthlyPsiElement(ASTNode node) {
        super(node);
    }

    @Nullable
    @NlsSafe
    public String getName() {
        return getText();
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return getNode().getPsi();
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        PsiFile fileFromText = PsiFileFactory.getInstance(getProject()).createFileFromText("test.earth", getLanguage(), name + ":");
        ASTNode firstChildNode = getNode().getFirstChildNode();
        ASTNode newKeyNode = fileFromText.getFirstChild().getFirstChild().getNode();
        if (firstChildNode == null) {
            getNode().addChild(newKeyNode);
        }
        else {
            getNode().replaceChild(firstChildNode, newKeyNode);
        }
        return this;
    }

    @Override
    public PsiReference[] getReferences() {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this);
    }
}
