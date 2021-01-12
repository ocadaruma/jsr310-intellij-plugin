package com.mayreh.intellij.plugin.jsr310;

import org.jetbrains.annotations.NotNull;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;

public class DateTimePatternFile extends PsiFileBase {
    public DateTimePatternFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, DateTimePattern.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return DateTimePatternFileType.INSTANCE;
    }
}
