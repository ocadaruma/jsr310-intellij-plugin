package com.mayreh.intellij.plugin.jsr310;

import java.time.format.DateTimeFormatter;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;

public class DateTimePatternAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        try {
            DateTimeFormatter.ofPattern(element.getText());
        } catch (IllegalArgumentException e) {
            holder.newAnnotation(HighlightSeverity.ERROR, e.getMessage())
                  .range(element.getTextRange())
                  .create();
        }
    }
}
