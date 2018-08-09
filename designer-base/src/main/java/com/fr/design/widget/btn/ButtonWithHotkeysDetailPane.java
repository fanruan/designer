package com.fr.design.widget.btn;

import java.awt.*;

import javax.swing.*;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleIconEditor;
import com.fr.form.ui.Button;

import com.fr.stable.StableUtils;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-15
 * Time   : 下午6:22
 */
public abstract class ButtonWithHotkeysDetailPane<T extends Button> extends ButtonDetailPane<T> {
    private UITextField hotkeysTextField;
    private UITextField buttonNameTextField;
    private AccessibleIconEditor iconPane;

    public ButtonWithHotkeysDetailPane() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(7, 7));
        JPanel advancePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double rowSize[] = {p, p, p, p};
        double columnSize[] = {p, f};
        JPanel labelPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        iconPane = new AccessibleIconEditor();
        labelPane.add(iconPane);
        Component[][] n_components = {
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Button-Type")), createButtonTypeComboBox()},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Button-Name")), buttonNameTextField = new UITextField()},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Button_Icon")), iconPane},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Button-Hotkeys")), hotkeysTextField = new UITextField()},
        };
        hotkeysTextField.setToolTipText(StableUtils.join(ButtonConstants.HOTKEYS, ","));
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(n_components, rowSize, columnSize, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        advancePane.add(panel, BorderLayout.NORTH);
        Component comp = createCenterPane();
        if(comp != null	) {
            advancePane.add(comp,BorderLayout.CENTER);
        }
        UIExpandablePane uiExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Advanced"), 280, 20, advancePane);
        this.add(uiExpandablePane);

    }

    protected abstract Component createCenterPane();

    @Override
    public void populate(T button) {
        if (button == null) {
            return;
        }
        iconPane.setValue(button.getIconName());
        buttonNameTextField.setText(button.getText());
        hotkeysTextField.setText(button.getHotkeys());
    }

    @Override
    public T update() {
        T button = createButton();
        button.setIconName((String)iconPane.getValue());
        button.setText(buttonNameTextField.getText());
        button.setHotkeys(hotkeysTextField.getText());
        return button;
    }
}