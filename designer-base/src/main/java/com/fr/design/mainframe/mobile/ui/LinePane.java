package com.fr.design.mainframe.mobile.ui;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.layout.VerticalFlowLayout;
import com.fr.design.style.color.NewColorSelectBox;
import com.fr.general.cardtag.mobile.LineDescription;
import com.fr.stable.CoreConstants;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class LinePane extends JPanel {
    private LineComboBox lineStyle;
    private NewColorSelectBox lineColor;
    private EventListenerList lineChangeListener = new EventListenerList();

    public LinePane() {
        init();
    }

    private void init() {
        this.setLayout(new VerticalFlowLayout(FlowLayout.CENTER, 0, 10));
        this.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 20));
        UILabel lineLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_FRFont_Line_Style"));
        UILabel colorLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Colors"));
        lineLabel.setPreferredSize(new Dimension(55, 20));
        colorLabel.setPreferredSize(new Dimension(55, 20));

        lineStyle = new LineComboBox(CoreConstants.UNDERLINE_STYLE_ARRAY);
        lineStyle.setPreferredSize(new Dimension(152, 20));
        lineColor = new NewColorSelectBox(137);
        lineStyle.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                fireLineStateChanged();
            }
        });
        lineColor.addSelectChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                fireLineStateChanged();
            }
        });
        this.add(TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{lineLabel, lineStyle}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM));
        this.add(TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{colorLabel, lineColor}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_MEDIUM));
    }

    public LineDescription update() {
        LineDescription lineDescription = new LineDescription();
        lineDescription.setColor(lineColor.getSelectObject());
        lineDescription.setLineStyle(lineStyle.getSelectedLineStyle());
        return lineDescription;
    }

    public void populate(LineDescription lineDescription) {
        lineStyle.setSelectedLineStyle(lineDescription.getLineStyle());
        lineColor.setSelectObject(lineDescription.getColor());
    }


    /**
     * 添加监听
     *
     * @param changeListener 监听列表
     */
    public void addLineChangeListener(ChangeListener changeListener) {
        lineChangeListener.add(ChangeListener.class, changeListener);
    }

    /**
     * 移除监听
     * Removes an old ColorChangeListener.
     *
     * @param changeListener 监听列表
     */
    public void removeLineChangeListener(ChangeListener changeListener) {
        lineChangeListener.remove(ChangeListener.class, changeListener);
    }

    /**
     * 颜色状态改变
     */
    public void fireLineStateChanged() {
        Object[] listeners = lineChangeListener.getListenerList();
        ChangeEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (e == null) {
                    e = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }
}
