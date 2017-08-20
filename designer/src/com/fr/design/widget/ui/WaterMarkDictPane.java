package com.fr.design.widget.ui;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UIPropertyTextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.WaterMark;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class WaterMarkDictPane extends JPanel {

    private UIPropertyTextField waterMarkTextField;

    public WaterMarkDictPane() {
        this.setLayout(new BorderLayout());

        waterMarkTextField = new UIPropertyTextField();

        UILabel emptyLabel = new UILabel();
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_WaterMark")), emptyLabel, waterMarkTextField},
        };
        double[] rowSize = {p};
        double[] columnSize = {p, p, f};
        int[][] rowCount = {{1, 1}};
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        this.add(panel, BorderLayout.CENTER);
    }

    public void populate(WaterMark waterMark) {
        this.waterMarkTextField.setText(waterMark.getWaterMark());
    }

    public void addInputKeyListener(KeyListener kl) {
        this.waterMarkTextField.addKeyListener(kl);
    }

    public void removeInputKeyListener(KeyListener kl) {
        this.waterMarkTextField.removeKeyListener(kl);
    }

    public void update(WaterMark waterMark) {
        waterMark.setWaterMark(this.waterMarkTextField.getText());
    }

    public void setWaterMark(String waterMark) {
        this.waterMarkTextField.setText(waterMark);
    }

    public String getWaterMark() {
        return this.waterMarkTextField.getText();
    }

}