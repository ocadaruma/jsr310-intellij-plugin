package com.mayreh.intellij.plugin.jsr310;

import javax.swing.Icon;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;

public final class ThreetenPatternFileType extends LanguageFileType {
    public static final ThreetenPatternFileType INSTANCE = new ThreetenPatternFileType();

    private ThreetenPatternFileType() {
        super(ThreetenPattern.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "JSR310Pattern";
    }

    @Override
    public @NotNull @Nls(capitalization = Capitalization.Sentence) String getDescription() {
        return "JSR310Pattern";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "jsr310";
    }

    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.Any_type;
    }
}
