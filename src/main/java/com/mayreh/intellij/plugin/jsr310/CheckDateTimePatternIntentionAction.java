package com.mayreh.intellij.plugin.jsr310;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;

public class CheckDateTimePatternIntentionAction implements IntentionAction {
    @Override
    public @Nls(capitalization = Capitalization.Sentence) @NotNull String getText() {
        return "Check DateTimeFormatter pattern";
    }

    @Override
    public @NotNull @Nls(capitalization = Capitalization.Sentence) String getFamilyName() {
        return getText();
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return file.getLanguage().isKindOf(ThreetenPattern.INSTANCE);
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file)
            throws IncorrectOperationException {
        // noop
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }
}
