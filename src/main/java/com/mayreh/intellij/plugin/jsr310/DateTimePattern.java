package com.mayreh.intellij.plugin.jsr310;

import com.intellij.lang.InjectableLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;

public final class DateTimePattern extends Language implements InjectableLanguage {
    public static final DateTimePattern INSTANCE = new DateTimePattern();

    private DateTimePattern() {
        super("JSR-310 DateTimePattern");
    }

    @Override
    public boolean isCaseSensitive() {
        return true;
    }

    @Override
    public LanguageFileType getAssociatedFileType() {
        return DateTimePatternFileType.INSTANCE;
    }
}
