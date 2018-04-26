package com.fr.design.chart.series.SeriesCondition;

import java.awt.Color;
import java.awt.Dimension;

import com.fr.base.Utils;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.TextAttr;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.design.style.FRFontPane;
import com.fr.design.style.color.ColorSelectBox;

public class DataLabelStylePane extends BasicPane {
	private static final long serialVersionUID = 6762567785276287745L;
	
	private UIComboBox nameBox;
	private UIComboBox fontStyleBox;
	private UIComboBox sizeBox;
	private ColorSelectBox colorBox;
	
	public DataLabelStylePane() {
		this.initPane(true);
	}
	
	/**
	 * 是否支持字体颜色的设置
	 */
	public DataLabelStylePane(boolean isSurpportFontColor) {
		this.initPane(isSurpportFontColor);
	}
	
	private void initPane(boolean isSurpportFontColor) {
		this.setLayout(FRGUIPaneFactory.createBoxFlowLayout());
		
		this.add(nameBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report()));
		nameBox.setPreferredSize(new Dimension(80, 20));
		
		String[] styles = {
				Inter.getLocText("FRFont-plain"),
				Inter.getLocText("FRFont-bold"),
				Inter.getLocText("FRFont-italic"),
				Inter.getLocText("FRFont-bolditalic")};
		this.add(fontStyleBox = new UIComboBox(styles));
		fontStyleBox.setPreferredSize(new Dimension(80, 20));
		
		this.add(sizeBox = new UIComboBox(FRFontPane.Font_Sizes));
		sizeBox.setPreferredSize(new Dimension(80, 20));
		
		if (isSurpportFontColor) {
			this.add(colorBox = new ColorSelectBox(60));
		}
		
		// 默认字体选择.
		FRFont defaultFont = FRFont.getInstance();
		nameBox.setSelectedItem(defaultFont.getFontName());
		fontStyleBox.setSelectedIndex(defaultFont.getStyle());
		sizeBox.setSelectedItem(defaultFont.getSize());
		if(colorBox != null) {
			colorBox.setSelectObject(defaultFont.getForeground());
		}
	}
	
	@Override
	protected String title4PopupWindow() {
		return "Label";
	}
	
	public void populate(AttrContents seriesAttrContents) {
		if(seriesAttrContents == null) {
			return;
		}
		
		populate(seriesAttrContents.getTextAttr());
	}
	
	public void populate(TextAttr textAttr) {
		if (textAttr == null) {
			return;
		}
		FRFont frFont = textAttr.getFRFont();
		populate(frFont);
	}
	
	public void populate(FRFont frFont) {
		if(frFont == null) {
			return;
		}
		nameBox.setSelectedItem(frFont.getFamily());
		fontStyleBox.setSelectedIndex(frFont.getStyle());
		sizeBox.setSelectedItem(frFont.getSize());
		if(colorBox != null) {
			colorBox.setSelectObject(frFont.getForeground());
		}
	}
	
	public void update(AttrContents seriesAttrContents) {
		if(seriesAttrContents.getTextAttr() == null) {
			seriesAttrContents.setTextAttr(new TextAttr());
		}
		update(seriesAttrContents.getTextAttr());
	}
	
	public void update(TextAttr textAttr) {
		textAttr.setFRFont(getInstanceFont());
	}
	
	/**
	 * 获取当前面板中设置的文本字体样式
	 * @return FRFont
	 */
	public FRFont getInstanceFont() {
		float fs = Utils.objectToNumber(sizeBox.getSelectedItem(), false).floatValue();
		String name = Utils.objectToString(nameBox.getSelectedItem());
		Color fontColor = (colorBox != null) ? colorBox.getSelectObject() : Color.black;
		return FRFont.getInstance(name, fontStyleBox.getSelectedIndex(), fs, fontColor);
	}
}