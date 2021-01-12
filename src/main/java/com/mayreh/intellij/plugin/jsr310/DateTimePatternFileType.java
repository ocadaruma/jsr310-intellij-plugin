package com.mayreh.intellij.plugin.jsr310;

import javax.swing.Icon;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;

public final class DateTimePatternFileType extends LanguageFileType {
    public static final DateTimePatternFileType INSTANCE = new DateTimePatternFileType();

    private DateTimePatternFileType() {
        super(DateTimePattern.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "JSR-310 DateTimePattern";
    }

    @Override
    public @NotNull @Nls(capitalization = Capitalization.Sentence) String getDescription() {
        return getName();
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
