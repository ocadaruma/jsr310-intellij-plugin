package com.mayreh.intellij.plugin.jsr310;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.actions.IncrementalFindAction;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.psi.PsiFile;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

/**
 * Form to check DateTimeFormatter pattern
 */
public class CheckDateTimePatternForm {
    public static final Key<Boolean> DATE_TIME_PATTERN_EDITOR =
            Key.create("DATE_TIME_PATTERN_EDITOR");

    private enum DateTimeClazz {
        LocalDate,
        LocalDateTime,
        ZonedDateTime,
    }

    private final ComboBox<DateTimeClazz> dateTimeClazz;
    private final EditorTextField sampleText;
    private final EditorTextField pattern;
    private final JBLabel result;

    private final JPanel rootPanel;

    public CheckDateTimePatternForm(@NotNull PsiFile file,
                                    @NotNull Document document) {
        Project project = file.getProject();

        dateTimeClazz = new ComboBox<>(DateTimeClazz.values());
        sampleText = new EditorTextField("", project, PlainTextFileType.INSTANCE) {
            @Override
            protected EditorEx createEditor() {
                EditorEx editor = super.createEditor();
                editor.putUserData(IncrementalFindAction.SEARCH_DISABLED, true);
                editor.setEmbeddedIntoDialogWrapper(true);
                return editor;
            }

            @Override
            protected void updateBorder(@NotNull EditorEx editor) {
                setupBorder(editor);
            }
        };
        sampleText.setOneLineMode(false);

        pattern = new EditorTextField(document, project, ThreetenPatternFileType.INSTANCE) {
            @Override
            protected EditorEx createEditor() {
                EditorEx editor = super.createEditor();
                editor.putUserData(DATE_TIME_PATTERN_EDITOR, true);
                editor.putUserData(IncrementalFindAction.SEARCH_DISABLED, true);
                editor.setEmbeddedIntoDialogWrapper(true);
                return editor;
            }

            @Override
            protected void updateBorder(@NotNull EditorEx editor) {
                setupBorder(editor);
            }
        };
        pattern.setOneLineMode(false);

        result = new JBLabel();

        int preferredWidth = Math.max(JBUIScale.scale(250), sampleText.getPreferredSize().width);
        sampleText.setPreferredWidth(preferredWidth);
        pattern.setPreferredWidth(preferredWidth);

        rootPanel = new JPanel(new GridBagLayout()) {
            @Override
            public void addNotify() {
                super.addNotify();

                IdeFocusManager.getGlobalInstance().requestFocus(sampleText, true);

                registerFocusShortcut(dateTimeClazz, "shift TAB", pattern.getFocusTarget());
                registerFocusShortcut(dateTimeClazz, "TAB", sampleText.getFocusTarget());
                registerFocusShortcut(sampleText, "shift TAB", dateTimeClazz);
                registerFocusShortcut(sampleText, "TAB", pattern.getFocusTarget());
                registerFocusShortcut(pattern, "shift TAB", sampleText.getFocusTarget());
                registerFocusShortcut(pattern, "TAB", dateTimeClazz);

                DocumentListener documentListener = new DocumentListener() {
                    @Override
                    public void documentChanged(@NotNull DocumentEvent event) {
                        update();
                    }
                };
                sampleText.addDocumentListener(documentListener);
                pattern.addDocumentListener(documentListener);

                update();
            }

            private void registerFocusShortcut(JComponent prev, String shortcut, JComponent next) {
                AnAction action = new AnAction() {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        IdeFocusManager.findInstance().requestFocus(next, true);
                    }
                };
                action.registerCustomShortcutSet(CustomShortcutSet.fromString(shortcut), prev);
            }

            private void update() {
                result.setText(sampleText.getText());
                rootPanel.revalidate();
                Balloon balloon = JBPopupFactory.getInstance().getParentBalloonFor(rootPanel);
                if (balloon != null && !balloon.isDisposed()) {
                    balloon.revalidate();
                }
            }
        };
        rootPanel.setBorder(JBUI.Borders.empty(UIUtil.DEFAULT_VGAP, UIUtil.DEFAULT_HGAP));

        JPanel clazzSelectPanel = new JPanel(new GridBagLayout());
        new LayoutBuilder(clazzSelectPanel)
                .add(0, 0, dateTimeClazz)
                .add(1, 0, new JBLabel(".parse"));

        JPanel inputsPanel = new JPanel(new GridBagLayout());
        new LayoutBuilder(inputsPanel)
                .add(0, 0, new JBLabel("   Text :"))
                .add(1, 0, sampleText)
                .add(0, 1, new JBLabel("Pattern :"))
                .add(1, 1, pattern);

        JPanel resultPanel = new JPanel(new GridBagLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Result"));
        new LayoutBuilder(resultPanel)
                .add(0, 0, result);

        new LayoutBuilder(rootPanel)
                .add(0, 0, clazzSelectPanel)
                .add(0, 1, inputsPanel)
                .add(0, 2, resultPanel, GridBagConstraints.HORIZONTAL);
    }

    /**
     * Helper class to layout components at ease
     */
    private static class LayoutBuilder {
        private final JPanel panel;
        private final GridBagConstraints constraints;

        private LayoutBuilder(JPanel panel) {
            this.panel = panel;
            constraints = new GridBagConstraints();
            constraints.insets = JBUI.insets(UIUtil.DEFAULT_VGAP / 2, UIUtil.DEFAULT_HGAP / 2);
        }

        private LayoutBuilder add(int x, int y, JComponent component) {
            return add(x, y, component, GridBagConstraints.NONE);
        }

        private LayoutBuilder add(int x, int y, JComponent component, int fill) {
            constraints.gridx = x;
            constraints.gridy = y;
            constraints.fill = fill;
            panel.add(component, constraints);
            return this;
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
