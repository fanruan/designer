package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.cardlayout.XWCardTagLayout;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.form.layout.FRBorderLayout;
import com.fr.general.ComparatorUtils;


import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Rectangle;

/**
 * cardMainBorderLayout适配器
 *
 * @author kerry
 * @date 2019/1/4
 */
public class FRCardMainBorderLayoutAdapter extends FRBorderLayoutAdapter {

    public FRCardMainBorderLayoutAdapter(XLayoutContainer container) {
        super(container);
    }

    /**
     * CardMainBorderLayout的title部分不能超出layout边界
     *
     * @param creator 组件
     */
    @Override
    public void fix(XCreator creator) {
        if (creator.acceptType(XWCardTagLayout.class)) {
            creator = (XCreator) creator.getParent();
        }
        boolean beyondBounds = calculateBeyondBounds(creator);
        if (!beyondBounds) {
            super.fix(creator);
        }
    }

    private boolean calculateBeyondBounds(XCreator creator) {
        FRBorderLayout layout = (FRBorderLayout) container.getFRLayout();
        Object constraints = layout.getConstraints(creator);
        Rectangle rectangle = creator.getBounds();
        //不能超出控件边界
        if (ComparatorUtils.equals(constraints, BorderLayout.NORTH) || ComparatorUtils.equals(constraints, BorderLayout.SOUTH)) {
            int containerHeight = container.getHeight();
            if (rectangle.height > containerHeight) {
                FineJOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Beyond_Tablayout_Bounds"));
                return true;
            }
        } else if (ComparatorUtils.equals(constraints, BorderLayout.EAST) || ComparatorUtils.equals(constraints, BorderLayout.WEST)) {
            int containerWidth = container.getWidth();
            if (rectangle.width > containerWidth) {
                FineJOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Beyond_Tablayout_Bounds"));
                return true;
            }
        }
        return false;
    }
}
