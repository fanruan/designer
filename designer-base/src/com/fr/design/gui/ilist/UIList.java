package com.fr.design.gui.ilist;

import com.fr.data.core.db.TableProcedure;
import com.fr.design.gui.controlpane.UINameableListCellRenderer;
import com.fr.design.gui.itooltip.UIToolTip;
import com.fr.design.mainframe.JTemplate;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-5-22
 * Time: 上午11:07
 * To change this template use File | Settings | File Templates.
 */
public class UIList extends JList {
    private Icon icon;

    public UIList() {
        super();
    }

    public UIList(ListModel dataModel) {
        super(dataModel);
    }

    public UIList(Object[] listData) {
        super(listData);
    }

    public UIList(Vector<?> listData) {
        super(listData);
    }

    public String getToolTipText(MouseEvent event) {
        int index = locationToIndex(event.getPoint());
        icon = new ImageIcon();
        if (index != -1) {
            Object value = getModel().getElementAt(index);
            ListCellRenderer renderer = getCellRenderer();
            Component rendererComp = renderer.getListCellRendererComponent(this, value, index, true, false);
            // UINameableListCellRenderer 继承自JPanel，无法强转为JLabel，直接返回即可
            if (rendererComp instanceof UINameableListCellRenderer) {
                return null;
            }
            if (rendererComp.getPreferredSize().width > getVisibleRect().width) {
                String tips = (rendererComp instanceof JComponent) ? ((JComponent) rendererComp).getToolTipText() : null;
                if (tips == null) {
                    if (value instanceof JTemplate) {
                        tips = ((JTemplate) value).getEditingFILE().getName();
                        icon = ((JTemplate) value).getEditingFILE().getIcon();
                    } else if (value instanceof ListModelElement || value instanceof TableProcedure) {
                        tips = ((JLabel) rendererComp).getText();
                        icon = ((JLabel) rendererComp).getIcon();
                    }
                }
                return tips;
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public Point getToolTipLocation(MouseEvent event) {
        int index = locationToIndex(event.getPoint());
        if (index != -1 && StringUtils.isNotEmpty(getToolTipText(event))) {
            Rectangle cellBounds = getCellBounds(index, index);
            return new Point(cellBounds.x - 2, cellBounds.y - 1);
        }
        return null;
    }

    public JToolTip createToolTip() {
        UIToolTip tip = new UIToolTip(icon);
        tip.setComponent(this);
        tip.setOpaque(false);
        return tip;
    }
}