package com.fr.plugin.chart.designer.component;

import com.fr.design.DesignerEnvManager;
import com.fr.design.constants.KeyWords;
import com.fr.design.gui.autocomplete.AutoCompletion;
import com.fr.design.gui.autocomplete.BasicCompletion;
import com.fr.design.gui.autocomplete.DefaultCompletionProvider;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.syntax.ui.rsyntaxtextarea.RSyntaxTextArea;
import com.fr.design.gui.syntax.ui.rsyntaxtextarea.SyntaxConstants;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartHtmlLabel;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mitisky on 16/2/19.
 */
public class VanChartHtmlLabelPane extends JPanel{

    private static final long serialVersionUID = -5512128966013558611L;
    private static final int JS_HEIGHT = 100;

    private RSyntaxTextArea contentTextArea;
    private UIToggleButton useHtml;

    private UICheckBox isCustomWidth;
    private UITextField customWidth;
    private UICheckBox isCustomHeight;
    private UITextField customHeight;

    private VanChartStylePane parent;

    public void setCustomFormatterText(String text){
        contentTextArea.setText(text);
    }

    public void setParent(VanChartStylePane parent) {
        this.parent = parent;
    }

    public VanChartHtmlLabelPane() {
        useHtml = new UIToggleButton(Inter.getLocText("Plugin-ChartF_Html"));
        JPanel widthAndHeightPane = createWidthAndHeightPane();
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p, p, p};

        Component[][] components  = new Component[][]{
                new Component[]{createJSContentPane()},
                new Component[]{useHtml},
                new Component[]{widthAndHeightPane}
        };

        JPanel contentPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        this.setLayout(new BorderLayout());
        this.add(contentPane, BorderLayout.CENTER);
    }

    private JComponent createJSContentPane() {
        contentTextArea = new RSyntaxTextArea();
        contentTextArea.setCloseCurlyBraces(true);
        contentTextArea.setLineWrap(true);
        contentTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        contentTextArea.setCodeFoldingEnabled(true);
        contentTextArea.setAntiAliasingEnabled(true);
        contentTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fireJSChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fireJSChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fireJSChange();
            }
        });

        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        for (String key : KeyWords.JAVASCRIPT) {
            provider.addCompletion(new BasicCompletion(provider, key));
        }

        AutoCompletion ac = new AutoCompletion(provider);
        String shortCuts = DesignerEnvManager.getEnvManager().getAutoCompleteShortcuts();

        ac.setTriggerKey(KeyStroke.getKeyStroke(shortCuts.replace("+", "pressed")));
        ac.install(contentTextArea);

        return new UIScrollPane(contentTextArea){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, JS_HEIGHT);
            }
        };
    }

    private void fireJSChange() {
        if(parent != null){
            parent.attributeChanged();
        }
    }

    protected JPanel createWidthAndHeightPane() {
        isCustomWidth = new UICheckBox(Inter.getLocText("Plugin-ChartF_Custom_Width"));
        customWidth = new UITextField(6);
        isCustomHeight = new UICheckBox(Inter.getLocText("Plugin-ChartF_Custom_Height"));
        customHeight = new UITextField(6);

        isCustomWidth.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                checkWidth();
            }
        });
        isCustomHeight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkHeight();
            }
        });
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { p, f };
        double[] rowSize = { p, p};

        Component[][] components  = new Component[][]{
                new Component[]{isCustomWidth, customWidth},
                new Component[]{isCustomHeight, customHeight},
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private void checkWidth() {
        customWidth.setEnabled(isCustomWidth.isSelected());
    }

    private void checkHeight() {
        customHeight.setEnabled(isCustomHeight.isSelected());
    }

    private void checkBoxUse() {
        checkHeight();
        checkWidth();
    }

    public void populate(VanChartHtmlLabel htmlLabel){
        if(htmlLabel == null){
            return;
        }
        setCustomFormatterText(htmlLabel.getCustomText());
        useHtml.setSelected(htmlLabel.isUseHtml());
        populateWidthAndHeight(htmlLabel);
    }

    protected void populateWidthAndHeight(VanChartHtmlLabel htmlLabel) {
        isCustomWidth.setSelected(htmlLabel.isCustomWidth());
        customWidth.setText(htmlLabel.getWidth());
        isCustomHeight.setSelected(htmlLabel.isCustomHeight());
        customHeight.setText(htmlLabel.getHeight());
        checkBoxUse();
    }

    public void update(VanChartHtmlLabel htmlLabel) {
        if(htmlLabel == null){
            return;
        }
        htmlLabel.setCustomText(contentTextArea.getText());
        htmlLabel.setUseHtml(useHtml.isSelected());
        updateWidthAndHeight(htmlLabel);
    }

    protected void updateWidthAndHeight(VanChartHtmlLabel htmlLabel) {
        htmlLabel.setCustomWidth(isCustomWidth.isSelected());
        htmlLabel.setWidth(customWidth.getText());
        htmlLabel.setCustomHeight(isCustomHeight.isSelected());
        htmlLabel.setHeight(customHeight.getText());
    }
}
