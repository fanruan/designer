package com.fr.design.mainframe.cell.settingpane;

import com.fr.base.Style;
import com.fr.design.constants.UIConstants;
import com.fr.design.mainframe.cell.settingpane.style.StylePane;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @author zhou
 * @since 2012-5-11下午3:59:39
 */
public class CellStylePane extends AbstractCellAttrPane {
    private StylePane stylePane;

    @Override
    public JPanel createContentPane() {
        JPanel content = new JPanel(new BorderLayout());
        stylePane = new StylePane();
        content.add(stylePane, BorderLayout.CENTER);
        stylePane.setBorder(UIConstants.CELL_ATTR_PRESENTBORDER);
        stylePane.addPredefinedChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                attributeChanged();
            }
        });
        stylePane.addCustomTabChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                adjustValues();// 里面的Tab切换后要及时调整滚动条,因为一些界面可能不需要滚动条
            }
        });
        return content;
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(new CellStylePane().createContentPane(), BorderLayout.CENTER);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(290, 400);
        jf.setVisible(true);
    }

    @Override
    public String getIconPath() {
//		return "com/fr/design/images/m_format/cell.png";
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Style");
    }


    @Override
    public void updateBean(TemplateCellElement cellElement) {
        cellElement.setStyle(stylePane.updateBean());
    }

    @Override
    public void updateBeans() {
        if (stylePane.getSelectedIndex() == 1) {
            Style s = stylePane.updateBean();
            TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
            int cellRectangleCount = cs.getCellRectangleCount();
            for (int rect = 0; rect < cellRectangleCount; rect++) {
                Rectangle cellRectangle = cs.getCellRectangle(rect);
                for (int j = 0; j < cellRectangle.height; j++) {
                    for (int i = 0; i < cellRectangle.width; i++) {
                        int column = i + cellRectangle.x;
                        int row = j + cellRectangle.y;
                        TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
                        if (cellElement == null) {
                            cellElement = new DefaultTemplateCellElement(column, row);
                            elementCase.addCellElement(cellElement);
                        }
                        cellElement.setStyle(s);
                    }
                }
            }
        } else {
            TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
            int cellRectangleCount = cs.getCellRectangleCount();
            for (int rect = 0; rect < cellRectangleCount; rect++) {
                Rectangle cellRectangle = cs.getCellRectangle(rect);
                for (int j = 0; j < cellRectangle.height; j++) {
                    for (int i = 0; i < cellRectangle.width; i++) {
                        int column = i + cellRectangle.x;
                        int row = j + cellRectangle.y;
                        TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
                        if (cellElement == null) {
                            cellElement = new DefaultTemplateCellElement(column, row);
                            elementCase.addCellElement(cellElement);
                        }
                        Style style = cellElement.getStyle();
                        if (style == null) {
                            style = Style.DEFAULT_STYLE;

                        }
                        style = stylePane.updateStyle(style);
                        cellElement.setStyle(style);
                    }
                }
            }
            stylePane.updateBorder();// border必须特别处理
        }
    }

    @Override
    protected void populateBean() {
        stylePane.populateBean(cellElement.getStyle());
        stylePane.dealWithBorder(elementCasePane);
    }

    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Style");
    }

    public void setSelectedByIds(int level, String... id) {
        stylePane.setSelctedByName(id[level]);
    }

}
