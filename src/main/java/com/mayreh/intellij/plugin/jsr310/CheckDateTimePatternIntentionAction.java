package com.mayreh.intellij.plugin.jsr310;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.codeInsight.intention.impl.QuickEditAction;
import com.intellij.codeInsight.intention.impl.QuickEditHandler;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;

public class CheckDateTimePatternIntentionAction extends QuickEditAction implements Iconable {
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
        if (editor.getUserData(CheckDateTimePatternForm.DATE_TIME_PATTERN_EDITOR) != null) {
            return false;
        }
        Pair<PsiElement, TextRange> rangePair = getRangePair(file, editor);
        if (rangePair != null && rangePair.first != null) {
            return rangePair.first.getLanguage().isKindOf(ThreetenPattern.INSTANCE);
        }
        PsiFile baseFile = InjectedLanguageManager.getInstance(project).getTopLevelFile(file);
        return baseFile != null && baseFile.getLanguage().isKindOf(ThreetenPattern.INSTANCE);
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file)
            throws IncorrectOperationException {
        PsiFile baseFile = InjectedLanguageManager.getInstance(project).getTopLevelFile(file);
        if (baseFile == null || !baseFile.getLanguage().isKindOf(ThreetenPattern.INSTANCE)) {
            super.invoke(project, editor, file);
            return;
        }
        JComponent component = createBalloonComponent(file);
        if (component != null) {
            QuickEditHandler.showBalloon(editor, file, component);
        }
    }

    @Override
    protected @Nullable JComponent createBalloonComponent(@NotNull PsiFile file) {
        Project project = file.getProject();
        Document document = PsiDocumentManager.getInstance(project).getDocument(file);
        if (document != null) {
            return new CheckDateTimePatternForm(file, document).getRootPanel();
        }
        return null;
    }

    @Override
    protected boolean isShowInBalloon() {
        return true;
    }

    @Override
    public Icon getIcon(int flags) {
        //noinspection ConstantConditions
        return ThreetenPattern.INSTANCE.getAssociatedFileType().getIcon();
    }
}
