package com.mayreh.intellij.plugin.jsr310;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;

public class ThreetenPatternParserDefinition implements ParserDefinition {
    public static final IElementType ENTIRE_TEXT = new IElementType("PATTERN_TEXT", ThreetenPattern.INSTANCE);
    public static final IFileElementType FILE = new IFileElementType(ThreetenPattern.INSTANCE);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new ThreetenPatternLexer();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new ThreetenPatternParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull TokenSet getWhitespaceTokens() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return new ThreetenPatternElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new ThreetenPatternFile(viewProvider);
    }

    static class ThreetenPatternLexer extends LexerBase {
        private int start;
        private int end;
        private CharSequence buffer;

        @Override
        public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
            this.buffer = buffer;
            this.start = startOffset;
            this.end = endOffset;
        }

        @Override
        public int getState() {
            return 0;
        }

        @Override
        public @Nullable IElementType getTokenType() {
            return start >= end ? null : ENTIRE_TEXT;
        }

        @Override
        public int getTokenStart() {
            return start;
        }

        @Override
        public int getTokenEnd() {
            return end;
        }

        @Override
        public void advance() {
            start = end;
        }

        @Override
        public @NotNull CharSequence getBufferSequence() {
            return buffer;
        }

        @Override
        public int getBufferEnd() {
            return end;
        }
    }

    static class ThreetenPatternParser implements PsiParser {
        @Override
        public @NotNull ASTNode parse(@NotNull IElementType root,
                                      @NotNull PsiBuilder builder) {
            Marker rootMarker = builder.mark();
            builder.advanceLexer();
            rootMarker.done(root);
            return builder.getTreeBuilt();
        }
    }

    static class ThreetenPatternElement extends ASTWrapperPsiElement {
        ThreetenPatternElement(@NotNull ASTNode node) {
            super(node);
        }
    }
}
