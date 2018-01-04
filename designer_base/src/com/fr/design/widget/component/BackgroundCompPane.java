package com.fr.design.widget.component;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleImgBackgroundEditor;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/6.
 */
public abstract class BackgroundCompPane<T extends Widget> extends BasicPane {
    protected UIButtonGroup backgroundHead;
    protected AccessibleImgBackgroundEditor initalBackgroundEditor;
    protected AccessibleImgBackgroundEditor overBackgroundEditor;
    protected AccessibleImgBackgroundEditor clickBackgroundEditor;
    private JPanel panel;

    public BackgroundCompPane() {
        initComponent();
    }

    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        UILabel headLabel = createUILable();
        initalBackgroundEditor = new AccessibleImgBackgroundEditor();
        overBackgroundEditor = new AccessibleImgBackgroundEditor();
        clickBackgroundEditor = new AccessibleImgBackgroundEditor();
        String [] titles = new String[]{Inter.getLocText("FR-Designer_DEFAULT"), Inter.getLocText("FR-Designer_Custom")};

        double f = TableLayout.FILL;
        final double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1},{1, 1},{1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Background-Initial")), initalBackgroundEditor},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Background-Over")), overBackgroundEditor},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Background-Click")), clickBackgroundEditor},
        };
        panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W0, IntervalConstants.INTERVAL_L1);
        panel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_L5, 0, 0));
        backgroundHead = new UIButtonGroup(titles);
        JPanel headPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                new Component[]{headLabel, backgroundHead}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W3, IntervalConstants.INTERVAL_L1);

        this.add(headPane, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);

    }

    protected UILabel createUILable(){
        return new UILabel(Inter.getLocText("FR-Designer_Background"));
    }

    public void update(T e){
    }

    public void populate(T e){
    }

    public void switchCard(){
        panel.setVisible(backgroundHead.getSelectedIndex() == 1);
    }

}
