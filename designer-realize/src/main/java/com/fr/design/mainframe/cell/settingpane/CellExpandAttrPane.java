package com.fr.design.mainframe.cell.settingpane;

import com.fr.base.BaseUtils;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.expand.ExpandLeftFatherPane;
import com.fr.design.expand.ExpandUpFatherPane;
import com.fr.design.expand.SortExpandAttrPane;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.ComparatorUtils;

import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * 单元格扩展属性面板，是属性表面板的一个种类
 */
public class CellExpandAttrPane extends AbstractCellAttrPane {
    private static final int SENIOR_HORIZONTAL_GAP = 12;
    private static final int BASIC_HORIZONTAL_GAP = 20;
    private UIButtonGroup<Byte> expandDirectionButton;
    private ExpandLeftFatherPane leftFatherPane;
    private ExpandUpFatherPane rightFatherPane;
    private UICheckBox horizontalExpandableCheckBox;
    private UICheckBox verticalExpandableCheckBox;
    private SortExpandAttrPane sortAfterExpand;
    private JPanel layoutPane;
    private JPanel basicPane;
    private JPanel seniorPane;

    /**
     * @return
     */
    public JPanel createContentPane() {
        String[] nameArray = {com.fr.design.i18n.Toolkit.i18nText("ExpandD-Not_Expand"), com.fr.design.i18n.Toolkit.i18nText("Utils-Top_to_Bottom"), com.fr.design.i18n.Toolkit.i18nText("Utils-Left_to_Right")};
        Icon[][] iconArray = {
                {BaseUtils.readIcon("/com/fr/design/images/expand/none16x16.png"), BaseUtils.readIcon("/com/fr/design/images/expand/none16x16_selected@1x.png")},
                {BaseUtils.readIcon("/com/fr/design/images/expand/vertical.png"), BaseUtils.readIcon("/com/fr/design/images/expand/vertical_selected@1x.png")},
                {BaseUtils.readIcon("/com/fr/design/images/expand/landspace.png"), BaseUtils.readIcon("/com/fr/design/images/expand/landspace_selected@1x.png")}
        };
        Byte[] valueArray = {Constants.NONE, Constants.TOP_TO_BOTTOM, Constants.LEFT_TO_RIGHT};
        expandDirectionButton = new UIButtonGroup<Byte>(iconArray, valueArray);
        expandDirectionButton.setAllToolTips(nameArray);
        leftFatherPane = new ExpandLeftFatherPane();
        rightFatherPane = new ExpandUpFatherPane();
        horizontalExpandableCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("ExpandD-Horizontal_Extendable"));
        verticalExpandableCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("ExpandD-Vertical_Extendable"));
        sortAfterExpand = new SortExpandAttrPane();
        initAllNames();
        return layoutPane();
    }

    private void initAllNames() {
        expandDirectionButton.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_ExpandD_Expand_Direction"));
        leftFatherPane.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_LeftParent"));
        rightFatherPane.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_ExpandD_Up_Father_Cell"));
        horizontalExpandableCheckBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_ExpandD_Expandable"));
        verticalExpandableCheckBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_ExpandD_Expandable"));
    }

    private JPanel layoutPane() {
        layoutPane = new JPanel(new BorderLayout());
        basicPane = new JPanel();
        seniorPane = new JPanel();
        basicPane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Basic"), 223, 24, basicPane());
        seniorPane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 223, 24, seniorPane());
        layoutPane.add(basicPane, BorderLayout.NORTH);
        layoutPane.add(seniorPane, BorderLayout.CENTER);
        return layoutPane;
    }

    private JPanel basicPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        UILabel direction = new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_ExpandD_Expand_Direction"), SwingConstants.LEFT);
        JPanel directionPane = new JPanel(new BorderLayout());
        directionPane.add(direction, BorderLayout.NORTH);
        UILabel left = new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_LeftParent"), SwingConstants.LEFT);
        JPanel leftPane = new JPanel(new BorderLayout());
        leftPane.add(left, BorderLayout.NORTH);
        UILabel up = new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_ExpandD_Up_Father_Cell"), SwingConstants.LEFT);
        JPanel upPane = new JPanel(new BorderLayout());
        upPane.add(up, BorderLayout.NORTH);
        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{directionPane, expandDirectionButton},
                new Component[]{leftPane, leftFatherPane},
                new Component[]{upPane, rightFatherPane},
        };
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, BASIC_HORIZONTAL_GAP, LayoutConstants.VGAP_LARGE);
    }

    private JPanel seniorPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        UILabel expendSort = new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_ExpendSort"), SwingConstants.LEFT);
        JPanel expendSortPane = new JPanel(new BorderLayout());
        expendSortPane.add(expendSort, BorderLayout.NORTH);
        horizontalExpandableCheckBox.setBorder(UIConstants.CELL_ATTR_ZEROBORDER);
        verticalExpandableCheckBox.setBorder(UIConstants.CELL_ATTR_ZEROBORDER);
        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{horizontalExpandableCheckBox, null},
                new Component[]{verticalExpandableCheckBox, null},
                new Component[]{expendSortPane, sortAfterExpand},
        };
        double[] rowSize = {p, p, p, p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 3}, {1, 3}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, SENIOR_HORIZONTAL_GAP, LayoutConstants.VGAP_LARGE);
    }


    @Override
    protected void populateBean() {
        this.leftFatherPane.setElementCasePane(elementCasePane);
        this.rightFatherPane.setElementCasePane(elementCasePane);
        CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
        if (cellExpandAttr == null) {
            cellExpandAttr = new CellExpandAttr();
            cellElement.setCellExpandAttr(cellExpandAttr);
        }
        expandDirectionButton.setSelectedItem(cellExpandAttr.getDirection());
        this.leftFatherPane.populate(cellExpandAttr);
        this.rightFatherPane.populate(cellExpandAttr);
        switch (cellExpandAttr.getExtendable()) {
            case CellExpandAttr.Both_EXTENDABLE:
                horizontalExpandableCheckBox.setSelected(true);
                verticalExpandableCheckBox.setSelected(true);
                break;
            case CellExpandAttr.Vertical_EXTENDABLE:
                horizontalExpandableCheckBox.setSelected(false);
                verticalExpandableCheckBox.setSelected(true);
                break;
            case CellExpandAttr.Horizontal_EXTENDABLE:
                horizontalExpandableCheckBox.setSelected(true);
                verticalExpandableCheckBox.setSelected(false);
                break;
            default: {
                horizontalExpandableCheckBox.setSelected(false);
                verticalExpandableCheckBox.setSelected(false);
                break;
            }
        }

        sortAfterExpand.populate(cellExpandAttr);
    }


    @Override
    public String getIconPath() {
//		return "com/fr/design/images/expand/cellAttr.gif";
        return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Expand");
    }


    @Override
    public void updateBean(TemplateCellElement cellElement) {
        CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
        if (cellExpandAttr == null) {
            cellExpandAttr = new CellExpandAttr();
            cellElement.setCellExpandAttr(cellExpandAttr);
        }
        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("FR-Designer_ExpandD_Expand_Direction"))) {
            cellExpandAttr.setDirection(expandDirectionButton.getSelectedItem());
        }
        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("FR-Designer_LeftParent"))) {
            this.leftFatherPane.update(cellExpandAttr);
        }
        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("FR-Designer_ExpandD_Up_Father_Cell"))) {
            this.rightFatherPane.update(cellExpandAttr);
        }


        // extendable
        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("FR-Designer_ExpandD_Expandable"))) {
            if (horizontalExpandableCheckBox.isSelected()) {
                if (verticalExpandableCheckBox.isSelected()) {
                    cellExpandAttr.setExtendable(CellExpandAttr.Both_EXTENDABLE);
                } else {
                    cellExpandAttr.setExtendable(CellExpandAttr.Horizontal_EXTENDABLE);
                }
            } else {
                if (verticalExpandableCheckBox.isSelected()) {
                    cellExpandAttr.setExtendable(CellExpandAttr.Vertical_EXTENDABLE);
                } else {
                    cellExpandAttr.setExtendable(CellExpandAttr.None_EXTENDABLE);
                }
            }
        }

        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_ExpandD_Sort_After_Expand"))) {
            sortAfterExpand.update(cellExpandAttr);
        }

    }

    /**
     *
     */
    public void updateBeans() {
        TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
        int cellRectangleCount = cs.getCellRectangleCount();
        for (int rect = 0; rect < cellRectangleCount; rect++) {
            Rectangle cellRectangle = cs.getCellRectangle(rect);
            // 需要先行后列地增加新元素。
            for (int j = 0; j < cellRectangle.height; j++) {
                for (int i = 0; i < cellRectangle.width; i++) {
                    int column = i + cellRectangle.x;
                    int row = j + cellRectangle.y;
                    TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
                    if (cellElement == null) {
                        cellElement = new DefaultTemplateCellElement(column, row);
                        elementCase.addCellElement(cellElement);
                    }
                    updateBean(cellElement);
                }
            }
        }
    }

    /**
     * @return
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("ExpandD-Expand_Attribute");
    }


}
