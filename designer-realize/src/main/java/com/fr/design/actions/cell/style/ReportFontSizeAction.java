/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell.style;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JComponent;

import com.fr.base.Style;
import com.fr.base.Utils;
import com.fr.base.core.StyleUtils;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.stable.AssistUtils;


/**
 * Font size.
 */
public class ReportFontSizeAction extends AbstractStyleAction {
	
	private static final int MAX_FONT_SIZE = 100;
	
	public ReportFontSizeAction(ElementCasePane t) {
		super(t);
		
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Font_Size"));
    }

	/**
	 * 应用选中的样式
	 * 
	 * @param style 之前的样式
	 * @param defStyle 选中的样式
	 * 
	 * @return 更改后的样式
	 * 
	 * @date 2015-1-22-下午4:54:00
	 * 
	 */
	public Style executeStyle(Style style, Style defStyle) {
        Object object = this.getValue(UIComboBox.class.getName());
        if (object != null && object instanceof UIComboBox) {
        	Object value = ((UIComboBox) object).getSelectedItem();
        	float selectedFontSize;
        	selectedFontSize = Utils.round5(Float.parseFloat(value.toString()));

            if (style.getFRFont().getSize() == defStyle.getFRFont().getSize()) {
            	style = StyleUtils.setReportFontSize(style, defStyle.getFRFont().getSize());
            }
            if (AssistUtils.equals(selectedFontSize, style.getFRFont().getSize())){
                return style;
            }
            style = StyleUtils.setReportFontSize(style, selectedFontSize);
        }

        return style;
    }

    public void setFontSize(float size) {
        Object object = this.getValue(UIComboBox.class.getName());
        if (object != null && object instanceof UIComboBox) {
            UIComboBox comboBox = (UIComboBox) object;
            //先和以前的Font Size比较.
            if (ComparatorUtils.equals(comboBox.getSelectedItem(), size)) {
                return;
            }

            //设置新Font Size
            comboBox.removeActionListener(this);
            comboBox.setSelectedItem(size + "");
            comboBox.addActionListener(this);
        }
    }

    /**
	 * 创建工具栏组件
	 * 
	 * @return 组件
	 * 
	 * @date 2015-1-22-下午4:53:29
	 * 
	 */
	public JComponent createToolBarComponent() {
        Object object = this.getValue(UIComboBox.class.getName());
        if (object == null || !(object instanceof UIComboBox)) {
            Vector<Integer> integerList = new Vector<Integer>();
            for (int i = 1; i < MAX_FONT_SIZE; i++) {
                integerList.add(i);
            }

            UIComboBox itemComponent = new UIComboBox(integerList);
            this.putValue(UIComboBox.class.getName(), itemComponent);
            itemComponent.setMinimumSize(new Dimension(50, 20));
            itemComponent.setPreferredSize(new Dimension(50, 20));
            itemComponent.setEnabled(this.isEnabled());
            itemComponent.addActionListener(this);
            //需求字体大小可编辑
            itemComponent.setEditable(true);

            return itemComponent;
        }

        return (JComponent) object;
    }

    /**
     * 更新样式
     * 
     * @param style 样式
     * 
     */
    @Override
	public void updateStyle(Style style) {
        if (style == null) {
            return;
        }
        FRFont frFont = style.getFRFont();
        if (frFont == null) {
            return;
        }
        setFontSize(Utils.round5(frFont.getSize2D()));
    }
}