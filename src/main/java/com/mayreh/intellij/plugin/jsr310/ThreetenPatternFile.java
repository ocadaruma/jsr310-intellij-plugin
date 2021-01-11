package com.mayreh.intellij.plugin.jsr310;

import org.jetbrains.annotations.NotNull;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;

public class ThreetenPatternFile extends PsiFileBase {
    public ThreetenPatternFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ThreetenPattern.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return ThreetenPatternFileType.INSTANCE;
    }
}
