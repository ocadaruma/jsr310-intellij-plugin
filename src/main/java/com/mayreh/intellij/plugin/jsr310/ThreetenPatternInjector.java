//package com.mayreh.intellij.plugin.jsr310;
//
//import org.jetbrains.annotations.Nls;
//import org.jetbrains.annotations.Nls.Capitalization;
//import org.jetbrains.annotations.NotNull;
//
//import com.intellij.openapi.util.TextRange;
//import com.intellij.psi.PsiElement;
//import com.intellij.psi.PsiReference;
//import com.intellij.psi.injection.ReferenceInjector;
//import com.intellij.util.ProcessingContext;
//
//public class ThreetenPatternInjector extends ReferenceInjector {
//    @Override
//    public PsiReference @NotNull [] getReferences(@NotNull PsiElement element,
//                                                  @NotNull ProcessingContext context,
//                                                  @NotNull TextRange range) {
//        return new PsiReference[]{
//                new ThreetenPatternReference(element, range.substring(element.getText()), range)
//        };
//    }
//
//    @Override
//    public @NotNull String getId() {
//        return "jsr310-pattern-reference";
//    }
//
//    @Override
//    public @NotNull @Nls(capitalization = Capitalization.Title) String getDisplayName() {
//        return "DateTimeFormatter pattern";
//    }
//}
