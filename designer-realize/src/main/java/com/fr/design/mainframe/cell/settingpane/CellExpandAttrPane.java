package com.fr.design.mainframe.cell.settingpane;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.expand.ExpandLeftFatherPane;
import com.fr.design.expand.ExpandUpFatherPane;
import com.fr.design.expand.SortExpandAttrPane;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.Constants;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;

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
    private CellExpandExtraAttrPane extraPane;


    /**
     * @return content panel
     */
    public JPanel createContentPane() {
        String[] nameArray = {Toolkit.i18nText("Fine-Design_Report_ExpandD_Not_Expand"), Toolkit.i18nText("Fine-Design_Report_Utils_Top_To_Bottom"), Toolkit.i18nText("FIne-Design_Report_Utils_Left_To_Right")};
        Icon[][] iconArray = {
                {IOUtils.readIcon("/com/fr/design/images/expand/none16x16.png"), IOUtils.readIcon("/com/fr/design/images/expand/none16x16_selected@1x.png")},
                {IOUtils.readIcon("/com/fr/design/images/expand/vertical.png"), IOUtils.readIcon("/com/fr/design/images/expand/vertical_selected@1x.png")},
                {IOUtils.readIcon("/com/fr/design/images/expand/landspace.png"), IOUtils.readIcon("/com/fr/design/images/expand/landspace_selected@1x.png")}
        };
        Byte[] valueArray = {Constants.NONE, Constants.TOP_TO_BOTTOM, Constants.LEFT_TO_RIGHT};
        expandDirectionButton = new UIButtonGroup<Byte>(iconArray, valueArray);
        expandDirectionButton.setAllToolTips(nameArray);
        leftFatherPane = new ExpandLeftFatherPane();
        rightFatherPane = new ExpandUpFatherPane();
        horizontalExpandableCheckBox = new UICheckBox(Toolkit.i18nText("Fine-Design_Report_ExpandD_Horizontal_Extendable"));
        verticalExpandableCheckBox = new UICheckBox(Toolkit.i18nText("Fine-Design_Report_ExpandD_Vertical_Extendable"));
        sortAfterExpand = new SortExpandAttrPane();
        initAllNames();
        return layoutPane();
    }

    private void initAllNames() {
        expandDirectionButton.setGlobalName(Toolkit.i18nText("Fine-Design_Report_ExpandD_Expand_Direction"));
        leftFatherPane.setGlobalName(Toolkit.i18nText("Fine-Design_Report_LeftParent"));
        rightFatherPane.setGlobalName(Toolkit.i18nText("Fine-Design_Report_ExpandD_Up_Father_Cell"));
        horizontalExpandableCheckBox.setGlobalName(Toolkit.i18nText("Fine-Design_Report_ExpandD_Expandable"));
        verticalExpandableCheckBox.setGlobalName(Toolkit.i18nText("Fine-Design_Report_ExpandD_Expandable"));
    }

    private JPanel layoutPane() {
        layoutPane = new JPanel(new BorderLayout());
        basicPane = new JPanel();
        seniorPane = new JPanel();
        basicPane = new UIExpandablePane(Toolkit.i18nText("Fine-Design_Report_Basic"), 223, 24, basicPane());
        seniorPane = new UIExpandablePane(Toolkit.i18nText("Fine-Design_Report_Advanced"), 223, 24, seniorPane());
        layoutPane.add(basicPane, BorderLayout.NORTH);
        layoutPane.add(seniorPane, BorderLayout.CENTER);

        extraPane = CellExpandExtraAttrPane.getInstance();

        JPanel content = new JPanel(new BorderLayout());
        content.add(layoutPane, BorderLayout.NORTH);
        content.add(extraPane, BorderLayout.CENTER);
        return content;
    }

    private JPanel basicPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        UILabel direction = new UILabel(Toolkit.i18nText("Fine-Design_Report_ExpandD_Expand_Direction"), SwingConstants.LEFT);
        JPanel directionPane = new JPanel(new BorderLayout());
        directionPane.add(direction, BorderLayout.NORTH);
        UILabel left = new UILabel(Toolkit.i18nText("Fine-Design_Report_LeftParent"), SwingConstants.LEFT);
        JPanel leftPane = new JPanel(new BorderLayout());
        leftPane.add(left, BorderLayout.NORTH);
        UILabel up = new UILabel(Toolkit.i18nText("Fine-Design_Report_ExpandD_Up_Father_Cell"), SwingConstants.LEFT);
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
        UILabel expendSort = new UILabel(Toolkit.i18nText("Fine-Design_Report_Expend_Sort"), SwingConstants.LEFT);
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
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}};
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

        extraPane.populate(cellElement);
    }


    @Override
    public String getIconPath() {
//		return "com/fr/design/images/expand/cellAttr.gif";
        return Toolkit.i18nText("Fine-Design_Report_Expand");
    }


    @Override
    public void updateBean(TemplateCellElement cellElement) {
        CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
        if (cellExpandAttr == null) {
            cellExpandAttr = new CellExpandAttr();
            cellElement.setCellExpandAttr(cellExpandAttr);
        }
        if (ComparatorUtils.equals(getGlobalName(), Toolkit.i18nText("Fine-Design_Report_ExpandD_Expand_Direction"))) {
            cellExpandAttr.setDirection(expandDirectionButton.getSelectedItem());
        }
        if (ComparatorUtils.equals(getGlobalName(), Toolkit.i18nText("Fine-Design_Report_LeftParent"))) {
            this.leftFatherPane.update(cellExpandAttr);
        }
        if (ComparatorUtils.equals(getGlobalName(), Toolkit.i18nText("Fine-Design_Report_ExpandD_Up_Father_Cell"))) {
            this.rightFatherPane.update(cellExpandAttr);
        }


        // extendable
        if (ComparatorUtils.equals(getGlobalName(), Toolkit.i18nText("Fine-Design_Report_ExpandD_Expandable"))) {
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

        if (ComparatorUtils.equals(getGlobalName(), Toolkit.i18nText("Fine-Design_Basic_ExpandD_Sort_After_Expand"))) {
            sortAfterExpand.update(cellExpandAttr);
        }

        extraPane.update(cellElement);
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
     * @return title
     */
    public String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Report_ExpandD_Expand_Attribute");
    }


}
