package com.fr.design.widget.btn;

import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.IconDefinePane;
import com.fr.form.ui.Button;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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
    private IconDefinePane iconPane;
    private AttributeChangeListener listener;


    public ButtonWithHotkeysDetailPane() {
        initComponents();
    }

    private void initComponents() {
//        creator.
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
//        JPanel advancedPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Advanced"));
        JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
//        advancedPane.setPreferredSize(new Dimension(600, 341));
        JPanel attrPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        attrPane.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double rowSize[] = {p, p, p, p, p, p, p};
        double columnSize[] = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 3}, {1, 1}, {1, 1}};
        JPanel labelPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        iconPane = new IconDefinePane();
        labelPane.add(iconPane);
        Component[][] n_components = {
                {new UILabel(Inter.getLocText("FR-Designer_Button-Name") + ":"), buttonNameTextField = new UITextField()},
                {new UILabel("背景" + ":"), new UITextField()},
                {new UILabel("字体" + ":"), new UITextField()},
                {new UILabel("图标" + ":"), new UITextField()},
                {new UILabel(Inter.getLocText("FR-Designer_Button-Hotkeys") + ":"), hotkeysTextField = new UITextField()}
        };
        buttonNameTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
//                creator.getWidget().set

            }
        });
        hotkeysTextField.setToolTipText(StableUtils.join(ButtonConstants.HOTKEYS, ","));
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(n_components, rowSize, columnSize, rowCount, 10, 8);
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        jPanel.add(panel, BorderLayout.CENTER);
        UIExpandablePane advancedPane = new UIExpandablePane("高级", 280, 20, jPanel);
        this.add(advancedPane);
    }

    //add By kerry
    public void addAttributeChangeListener(AttributeChangeListener listener) {
        this.listener = listener;
    }


    protected abstract Component createCenterPane();

    @Override
    public void populate(Button button) {
        if (button == null) {
            return;
        }
        iconPane.populate(button.getIconName());
        buttonNameTextField.setText(button.getText());
        hotkeysTextField.setText(button.getHotkeys());
    }

    @Override
    public T update() {
        T button = createButton();
        button.setIconName(iconPane.update());
        button.setText(buttonNameTextField.getText());
        button.setHotkeys(hotkeysTextField.getText());
        return button;
    }


    public void updateBean(Button ob) {
        ob.setIconName(iconPane.update());
        ob.setText(buttonNameTextField.getText());
        ob.setHotkeys(hotkeysTextField.getText());
    }
}