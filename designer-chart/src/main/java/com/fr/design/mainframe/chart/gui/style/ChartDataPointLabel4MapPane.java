package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.Utils;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.ChartConstants;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.FRFont;

import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 地图 系列属性 标签内容界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-6-5 下午02:35:27
 */
public class ChartDataPointLabel4MapPane extends ChartDatapointLabelPane {
	public ChartDataPointLabel4MapPane(ChartStylePane parent) {
		this.parent = parent;
		
		isLabelShow = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Label"));
		isCategory = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Name"));
		isValue = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Region_Value"));
        valueFormatButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Format"));

        isValuePercent = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Value_Percent"));
        valuePercentFormatButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Format"));

        divideComoBox = new UIComboBox(ChartConstants.DELIMITERS);
        textFontPane = new ChartTextAttrPane();

        initFormatListener();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        double[] columnSize = { p, f };
        double[] rowSize2 = { p,p,p,p,p,p,p};

        Component[][] components = new Component[9][3];

        JPanel panel = null;

        components[1] = new Component[]{isCategory, null};

        components[3] = new Component[]{isValue,valueFormatButton};
        components[4] = new Component[]{isValuePercent,valuePercentFormatButton} ;

        JPanel delimiterPane = new JPanel(new BorderLayout(LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM));
        delimiterPane.add(new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Delimiter_Symbol")), BorderLayout.WEST);
        delimiterPane.add(divideComoBox, BorderLayout.CENTER);

        components[5] = new Component[]{delimiterPane,null};
        components[6] = new Component[]{textFontPane, null};
        labelPane = TableLayoutHelper.createTableLayoutPane(components,rowSize2,columnSize);

        double[] row = {p,p};
        double[] col = {LayoutConstants.CHART_ATTR_TOMARGIN, f};
        panel = TableLayoutHelper.createTableLayoutPane(new Component[][]{
                new Component[]{isLabelShow,null},new Component[]{null, labelPane}}, row, col);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER) ;

        isLabelShow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxUse();
            }
        });
	}
	
	public void populate(AttrContents attrContents) {
		isLabelShow.setSelected(true);
		String dataLabel = attrContents.getSeriesLabel();
		if (dataLabel != null) {
			for (int i = 0; i < ChartConstants.DELIMITERS.length; i++) {
				String delimiter = ChartConstants.DELIMITERS[i];
				if (divideComoBox != null && dataLabel.contains(delimiter)) {
					divideComoBox.setSelectedItem(delimiter);
					break;
				}
			}
			// 以前的换行符 ${BR}
			if (divideComoBox != null && dataLabel.contains(ChartConstants.BREAKLINE_PARA)) {
				divideComoBox.setSelectedItem(ChartConstants.DELIMITERS[3]);
			}

			if (isCategory != null) {
				isCategory.setSelected(dataLabel.contains(ChartConstants.CATEGORY_PARA));
			}
			if(isValue != null) {
				isValue.setSelected(dataLabel.contains(ChartConstants.VALUE_PARA));
			}
			if(isValuePercent != null) {
				isValuePercent.setSelected(dataLabel.contains(ChartConstants.PERCENT_PARA));
			}
		} else {
			if (isCategory != null) {
				isCategory.setSelected(false);
			}
			if (isValue != null) {
				isValue.setSelected(false);
			}
			if (isValuePercent != null) {
				isValuePercent.setSelected(false);
			}
		}

		valueFormat = attrContents.getFormat();
		percentFormat = attrContents.getPercentFormat();

        if(textFontPane != null) {
			textFontPane.populate(attrContents.getTextAttr());
        }
	}
	
	public AttrContents update() {
		if(!isLabelShow.isSelected()) {
			return null;
		}
		AttrContents attrContents = new AttrContents();
		String contents = StringUtils.EMPTY;
		String delString = Utils.objectToString(divideComoBox.getSelectedItem());
		if (delString.contains(ChartConstants.DELIMITERS[3])) {
			delString = ChartConstants.BREAKLINE_PARA;
		} else if (delString.contains(ChartConstants.DELIMITERS[SPACE])) {
			delString = StringUtils.BLANK;
		}
		if ((isCategory != null && isCategory.isSelected())) {
			contents += ChartConstants.CATEGORY_PARA + delString;
		}
		if (isValue != null && isValue.isSelected()) {
			contents += ChartConstants.VALUE_PARA + delString;
		}
		if (isValuePercent != null && isValuePercent.isSelected()) {
			contents += ChartConstants.PERCENT_PARA + delString;
		}
		if (contents.contains(delString)) {
			contents = contents.substring(0, contents.lastIndexOf(delString));
		}
		
		attrContents.setSeriesLabel(contents);
		if(valueFormat != null) {
			attrContents.setFormat(valueFormat);
		}
		if(percentFormat != null) {
			attrContents.setPercentFormat(percentFormat);
		}

        if(textFontPane != null){
            attrContents.setTextAttr(textFontPane.update());
        }

		updatePercentFormatpane();
		return attrContents;
	}

}