//package com.mayreh.intellij.plugin.jsr310;
//
//import java.time.format.DateTimeFormatter;
//
//import org.jetbrains.annotations.Nls;
//import org.jetbrains.annotations.Nls.Capitalization;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider;
//import com.intellij.openapi.util.TextRange;
//import com.intellij.psi.PsiElement;
//import com.intellij.psi.PsiReference;
//import com.intellij.util.IncorrectOperationException;
//
//public class ThreetenPatternReference implements PsiReference, EmptyResolveMessageProvider {
//    private final PsiElement element;
//    private final String pattern;
//    private final TextRange textRange;
//
//    public ThreetenPatternReference(PsiElement element,
//                                    String pattern,
//                                    TextRange textRange) {
//        this.element = element;
//        this.pattern = pattern;
//        this.textRange = textRange;
//    }
//
//    @Override
//    public @NotNull PsiElement getElement() {
//        return element;
//    }
//
//    @Override
//    public @NotNull TextRange getRangeInElement() {
//        return textRange;
//    }
//
//    @Override
//    public @Nullable PsiElement resolve() {
//        try {
//            DateTimeFormatter.ofPattern(pattern);
//        } catch (IllegalArgumentException e) {
//            return null;
//        }
//        return element;
//    }
//
//    @Override
//    public @NotNull String getCanonicalText() {
//        return pattern;
//    }
//
//    @Override
//    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
//        return null;
//    }
//
//    @Override
//    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
//        return null;
//    }
//
//    @Override
//    public boolean isReferenceTo(@NotNull PsiElement element) {
//        return false;
//    }
//
//    @Override
//    public boolean isSoft() {
//        return false;
//    }
//
//    @Override
//    public @Nls(capitalization = Capitalization.Sentence) @NotNull String getUnresolvedMessagePattern() {
//        return "Invalid DateTimeFormatter pattern";
//    }
//}
