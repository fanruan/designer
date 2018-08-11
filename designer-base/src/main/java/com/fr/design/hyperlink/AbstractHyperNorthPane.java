package com.fr.design.hyperlink;

import com.fr.base.Utils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.FRGUIPaneFactory;

import com.fr.js.Hyperlink;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AbstractHyperNorthPane<T extends Hyperlink> extends BasicBeanPane<T> {
    public static final int NEW_WINDOW = 0;
    public static final int DIALOG = 1;
    public static final int SELF = 2;
    public static final int DEFAULT_H_VALUE = 400;
    public static final int DEFAULT_V_VALUE = 600;

    private JPanel headerPane;
    private UIComboBox targetFrameComboBox;

    private UINumberField heightTextFiled;
    private UINumberField widthTextFiled;


    public AbstractHyperNorthPane() {
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        headerPane = this.setHeaderPanel();
        this.add(headerPane, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
        targetFrameComboBox = new UIComboBox(getTargetFrames());
        targetFrameComboBox.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                return this;
            }
        });
        JPanel targetFramePanel = new JPanel();
        targetFramePanel.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Link_Opened_In")));
        targetFramePanel.add(targetFrameComboBox);
        targetFrameComboBox.setEditable(true);
        targetFrameComboBox.setPreferredSize(new Dimension(100, 20));

        final JPanel newWindowConfPane = new JPanel();
        newWindowConfPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Height") + ": "));
        heightTextFiled = new UINumberField();
        heightTextFiled.setText(String.valueOf(DEFAULT_H_VALUE));
        heightTextFiled.setPreferredSize(new Dimension(40, 20));
        newWindowConfPane.add(heightTextFiled);
        newWindowConfPane.add(new UILabel("  " + com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Width") + ": "));
        widthTextFiled = new UINumberField();
        widthTextFiled.setText(String.valueOf(DEFAULT_V_VALUE));
        widthTextFiled.setPreferredSize(new Dimension(40, 20));
        newWindowConfPane.add(widthTextFiled);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(targetFramePanel, BorderLayout.WEST);
        centerPanel.add(newWindowConfPane, BorderLayout.EAST);
        newWindowConfPane.setVisible(false);

        centerPane.add(centerPanel);
        targetFrameComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newWindowConfPane.setVisible(DIALOG == targetFrameComboBox.getSelectedIndex());
            }
        });

        this.add(this.setFootPanel(), BorderLayout.SOUTH);
    }

    protected String[] getTargetFrames() {
        return new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Hyperlink_New_Window"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Dialog"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Self_Window")};
    }

    protected abstract JPanel setHeaderPanel();

    protected abstract JPanel setFootPanel();

    protected abstract void populateSubHyperlinkBean(T link);

    public UIComboBox getTargetFrameComboBox() {
        return targetFrameComboBox;
    }

    public void setTargetFrameComboBox(UIComboBox targetFrameComboBox) {
        this.targetFrameComboBox = targetFrameComboBox;
    }

    public UINumberField getHeightTextFiled() {
        return heightTextFiled;
    }

    public void setHeightTextFiled(UINumberField heightTextFiled) {
        this.heightTextFiled = heightTextFiled;
    }

    public UINumberField getWidthTextFiled() {
        return widthTextFiled;
    }

    public void setWidthTextFiled(UINumberField widthTextFiled) {
        this.widthTextFiled = widthTextFiled;
    }

    @Override
    public void populateBean(T link) {
        String name = link.getTargetFrame();
        targetFrameComboBox.setSelectedIndex(HyperlinkTargetFrame.convert(name));
        heightTextFiled.setText(String.valueOf(link.getHeight() == 0 ? DEFAULT_H_VALUE : link.getHeight()));
        widthTextFiled.setText(String.valueOf(link.getWidth() == 0 ? DEFAULT_V_VALUE : link.getWidth()));
        populateSubHyperlinkBean(link);
    }

    protected abstract T updateSubHyperlinkBean();

    protected abstract void updateSubHyperlinkBean(T t);

    @Override
    public T updateBean() {
        T link = updateSubHyperlinkBean();

        updateBean(link);

        return link;
    }

    public void updateBean(T link) {
        updateSubHyperlinkBean(link);
        link.setTargetFrame(HyperlinkTargetFrame.parse(targetFrameComboBox.getSelectedIndex()).getName());
        link.setHeight(Utils.objectToNumber(heightTextFiled.getText(), false).intValue());
        link.setWidth(Utils.objectToNumber(widthTextFiled.getText(), false).intValue());
    }

}