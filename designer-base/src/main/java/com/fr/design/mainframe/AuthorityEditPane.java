package com.fr.design.mainframe;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;


import javax.swing.*;
import java.awt.*;

/**
 * Author : daisy
 * Date: 13-9-12
 * Time: 下午6:21
 */
public abstract class AuthorityEditPane extends JPanel {
    protected static final int TOP_GAP = 11;
    protected static final int LEFT_GAP = 4;
    protected static final int RIGHT_GAP = 10;
    protected static final int ALIGNMENT_GAP = -3;
    protected static final int LEFT_CHECKPANE = 3;

    protected UILabel type = null;
    protected UILabel name = null;
    protected JPanel checkPane = null;
    private TargetComponent target;
    private JPanel typePane;
    private JPanel namePane;

    public AuthorityEditPane(TargetComponent target) {
        this.target = target;
        setLayout(new BorderLayout());
        type = new UILabel();
        typePane = new JPanel(new BorderLayout());
        typePane.add(type, BorderLayout.CENTER);
        type.setBorder(BorderFactory.createEmptyBorder(0,LEFT_GAP,0,0));
        typePane.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        name = new UILabel();
        namePane = new JPanel(new BorderLayout());
        namePane.add(name, BorderLayout.CENTER);
        name.setBorder(BorderFactory.createEmptyBorder(0,LEFT_GAP,0,0));
        namePane.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        checkPane = new JPanel();
        checkPane.setLayout(new BorderLayout());
//		this.add(layoutText(), BorderLayout.WEST);
//		this.add(layoutPane(), BorderLayout.CENTER);
        this.add(centerPane(), BorderLayout.NORTH);
        this.setBorder(BorderFactory.createEmptyBorder(TOP_GAP, LEFT_GAP, 0, RIGHT_GAP));
    }

    private JPanel centerPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(" " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Type") + "        ", SwingConstants.LEFT), typePane},
                new Component[]{new UILabel(" " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_WF_Name") + "        ", SwingConstants.LEFT), namePane},
                new Component[]{checkPane, null},
        };

        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_SMALL, LayoutConstants.VGAP_MEDIUM);
    }

    /**
     * 更新权限编辑面板的具体内容：类型、名称、权限面板
     */
    public void populateDetials() {
        populateType();
        populateName();
        checkPane.removeAll();
        populateCheckPane();
        checkPane.setBorder(BorderFactory.createEmptyBorder(0, LEFT_CHECKPANE, 0, 0));
    }

    public abstract void populateType();

    public abstract void populateName();

    public abstract JPanel populateCheckPane();

}
