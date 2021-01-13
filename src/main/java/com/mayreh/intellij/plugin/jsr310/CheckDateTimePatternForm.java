package com.mayreh.intellij.plugin.jsr310;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;

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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.psi.PsiFile;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
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
    private final JBTextArea result;
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

        pattern = new EditorTextField(document, project, DateTimePatternFileType.INSTANCE) {
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

        result = new JBTextArea(2, 40);
        result.setLineWrap(true);
        result.setWrapStyleWord(true);
        result.setOpaque(false);
        result.setEditable(false);
        result.setBorder(JBUI.Borders.empty(UIUtil.DEFAULT_VGAP / 2, UIUtil.DEFAULT_HGAP / 2));

        rootPanel = new JPanel() {
            @Override
            public void addNotify() {
                super.addNotify();

                IdeFocusManager.getGlobalInstance().requestFocus(dateTimeClazz, true);

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
                dateTimeClazz.addItemListener(e -> update());

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
                if (StringUtil.isEmpty(sampleText.getText())) {
                    setResult("");
                } else if (StringUtil.isNotEmpty(pattern.getText())) {
                    try {
                        DateTimeFormatter parsedPattern = DateTimeFormatter.ofPattern(pattern.getText());
                        try {
                            String text = sampleText.getText();
                            DateTimeClazz selectedClazz = (DateTimeClazz) dateTimeClazz.getSelectedItem();
                            String resultText = null;
                            switch (selectedClazz) {
                                case LocalDate:
                                    resultText = LocalDate.parse(text, parsedPattern).toString();
                                    break;
                                case LocalDateTime:
                                    resultText = LocalDateTime.parse(text, parsedPattern).toString();
                                    break;
                                case ZonedDateTime:
                                    resultText = ZonedDateTime.parse(text, parsedPattern).toString();
                                    break;
                            }
                            if (resultText != null) {
                                setResult(resultText);
                            }
                        } catch (DateTimeParseException e) {
                            setErrorResult(e.getMessage());
                        }
                    } catch (IllegalArgumentException e) {
                        setResult("");
                    }
                }
                rootPanel.revalidate();
                Balloon balloon = JBPopupFactory.getInstance().getParentBalloonFor(rootPanel);
                if (balloon != null && !balloon.isDisposed()) {
                    balloon.revalidate();
                }
            }

            private void setErrorResult(String text) {
                result.setForeground(SimpleTextAttributes.ERROR_ATTRIBUTES.getFgColor());
                result.setText(text);
                result.setCaretPosition(0);
            }

            private void setResult(String text) {
                result.setForeground(SimpleTextAttributes.REGULAR_ATTRIBUTES.getFgColor());
                result.setText(text);
                result.setCaretPosition(0);
            }
        };
        rootPanel.setBorder(JBUI.Borders.empty(UIUtil.DEFAULT_VGAP, UIUtil.DEFAULT_HGAP));

        int preferredWidth = Math.max(JBUIScale.scale(250), sampleText.getPreferredSize().width);
        sampleText.setPreferredWidth(preferredWidth);
        pattern.setPreferredWidth(preferredWidth);

        JPanel clazzSelectPanel = new JPanel(new GridBagLayout());
        new GridBagBuilder(clazzSelectPanel)
                .add(0, 0, dateTimeClazz)
                .add(1, 0, new JBLabel(".parse"));

        JPanel inputsPanel = new JPanel(new GridBagLayout());
        new GridBagBuilder(inputsPanel)
                .add(0, 0, new JBLabel("   Text :"), GridBagConstraints.LINE_END)
                .add(1, 0, sampleText)
                .add(0, 1, new JBLabel("Pattern :"), GridBagConstraints.LINE_END)
                .add(1, 1, pattern);

        JBScrollPane resultPane = new JBScrollPane(
                result,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        resultPane.getViewport().setOpaque(false);
        resultPane.setOpaque(false);
        resultPane.setBorder(BorderFactory.createTitledBorder("Result"));

        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.PAGE_AXIS));
        rootPanel.add(clazzSelectPanel);
        rootPanel.add(inputsPanel);
        rootPanel.add(resultPane);
    }

    /**
     * Helper class to layout components at ease
     */
    private static class GridBagBuilder {
        private final JPanel panel;
        private final GridBagConstraints constraints;

        private GridBagBuilder(JPanel panel) {
            this.panel = panel;
            constraints = new GridBagConstraints();
            constraints.insets = JBUI.insets(UIUtil.DEFAULT_VGAP / 2, UIUtil.DEFAULT_HGAP / 2);
        }

        private GridBagBuilder add(int x, int y, JComponent component) {
            return add(x, y, component, GridBagConstraints.CENTER);
        }

        private GridBagBuilder add(int x, int y, JComponent component, int anchor) {
            constraints.gridx = x;
            constraints.gridy = y;
            constraints.anchor = anchor;
            panel.add(component, constraints);
            return this;
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
