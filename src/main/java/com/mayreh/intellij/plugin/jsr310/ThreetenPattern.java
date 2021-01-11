package com.mayreh.intellij.plugin.jsr310;

import com.intellij.lang.InjectableLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;

public final class ThreetenPattern extends Language implements InjectableLanguage {
    public static final ThreetenPattern INSTANCE = new ThreetenPattern();

    private ThreetenPattern() {
        super("JSR310Pattern");
    }

    @Override
    public boolean isCaseSensitive() {
        return true;
    }

    @Override
    public LanguageFileType getAssociatedFileType() {
        return ThreetenPatternFileType.INSTANCE;
    }
}
