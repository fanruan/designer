package com.fr.design.mainframe.cell.settingpane;

import com.fr.base.present.Present;
import com.fr.design.constants.UIConstants;
import com.fr.design.present.PresentPane;

import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author zhou
 * @since 2012-5-11下午5:24:35
 */
public class CellPresentPane extends AbstractCellAttrPane {
    private PresentPane presentPane;

    /**
     * 初始化面板
     *
     * @return 面板
     */
    public JPanel createContentPane() {
        presentPane = new PresentPane();
        JPanel content = new JPanel(new BorderLayout());
        content.add(presentPane, BorderLayout.CENTER);
        presentPane.setBorder(UIConstants.CELL_ATTR_PRESENTBORDER);
        presentPane.addTabChangeListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustValues();
            }
        });
        return content;
    }

    @Override
    public String getIconPath() {
//		return "com/fr/design/images/data/source/dataDictionary.png";
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Present");
    }

    @Override
    public void updateBean(TemplateCellElement cellElement) {
        cellElement.setPresent(presentPane.updateBean());
    }

    /**
     * 保存
     */
    public void updateBeans() {
        Present present = presentPane.updateBean();
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
                    cellElement.setPresent(present);
                }
            }
        }
    }

    @Override
    protected void populateBean() {
        //选中的所有单元格都有形态，属性表才会有内容，否则是初始值
        //主要是解决37664
        Present present = getSelectCellPresent();
        presentPane.populateBean(present);
    }

    private Present getSelectCellPresent() {
        TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
        //按住ctrl选中多个cell块
        int cellRectangleCount = cs.getCellRectangleCount();

        for (int rect = 0; rect < cellRectangleCount; rect++) {
            Rectangle cellRectangle = cs.getCellRectangle(rect);
            for (int j = 0; j < cellRectangle.height; j++) {
                for (int i = 0; i < cellRectangle.width; i++) {
                    int column = i + cellRectangle.x;
                    int row = j + cellRectangle.y;
                    TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
                    if (cellElement == null || cellElement.getPresent() == null) {
                        return null;
                    }
                }
            }
        }
        return cellElement.getPresent();
    }

    /**
     * 对话框标题
     *
     * @return 标题
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Present");
    }

    public void setSelectedByIds(int level, String... id) {
        presentPane.setSelectedByName(id[level]);
    }


}
