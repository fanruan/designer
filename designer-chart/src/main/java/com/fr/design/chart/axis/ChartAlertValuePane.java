package com.fr.design.chart.axis;

import com.fr.base.BaseFormula;
import com.fr.base.Utils;
import com.fr.chart.chartattr.ChartAlertValue;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.AlphaPane;
import com.fr.design.style.FRFontPane;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;

import com.fr.stable.Constants;
import com.fr.stable.CoreConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 图表 坐标轴 警戒线值 编辑界面. (默认 位置居左居右)
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-5-22 上午09:49:54
 */
public class ChartAlertValuePane extends BasicBeanPane<ChartAlertValue> {
	private static final int DE_FONT = 9;
	private static final double ALPH_PER = 100;
	
	private UITextField textField;
	private LineComboBox lineCombo;
	private ColorSelectBox colorBox;
	private AlphaPane alphaPane;
	
	private UITextField contentField;
	private UIComboBox fontNameBox;
	private UIComboBox fontSizeBox;
	
	private UIRadioButton leftButton;
	private UIRadioButton rightButton;
	
	public ChartAlertValuePane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		JPanel pane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		this.add(pane, BorderLayout.CENTER);
		
		JPanel alertLinePane =FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		pane.add(alertLinePane);
		
		alertLinePane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alert_Line_Setting")));
		
		JPanel valuePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		alertLinePane.add(valuePane);
		
		valuePane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alert_Value") + ":"));
		
		textField = new UITextField();
		textField.setColumns(4);
		valuePane.add(textField);
		
		textField.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				showFormulaPane();
			}
		});
		
		textField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				e.consume();
				showFormulaPane();
			}
		});

		JPanel lineStylePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		alertLinePane.add(lineStylePane);
		
		lineStylePane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Line_Style") + ":"));
		lineCombo = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
		lineStylePane.add(lineCombo);
		
		JPanel lineColorPane =FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		alertLinePane.add(lineColorPane);
		
		lineColorPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alert_Line_Color") + ":"));
		colorBox = new ColorSelectBox(80);
		lineColorPane.add(colorBox);
		
		alphaPane = new AlphaPane();
		alertLinePane.add(alphaPane);
		
		JPanel tipPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		pane.add(tipPane);
		
		tipPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alert_Tip") + ":", null));
		
		JPanel centerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		tipPane.add(centerPane);
		
		fontNameBox = new UIComboBox();
		fontNameBox.setPreferredSize(new Dimension(80,20));
		
		fontNameBox.addItem("SimSun"); // TODO 这边字体中没有在列表内
		String[] names = Utils.getAvailableFontFamilyNames4Report();
		for(int i = 0; i < names.length; i++) {
			fontNameBox.addItem(names[i]);
		}
		
		fontSizeBox = new UIComboBox();
		fontSizeBox.setPreferredSize(new Dimension(80,20));
		Integer[] sizes = FRFontPane.Font_Sizes;
		for(int i = 0; i < sizes.length; i++) {
			fontSizeBox.addItem(sizes[i]);
		}
		
		double t = TableLayout.FILL;
		double[] rowSize = {t, t, t, t, t};
		double[] columnSize = {0.1, 0.2, 0.5, 0.2};
		Component[][] components= {
				{null, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Content") + ":"),  contentField = new UITextField(3)},
				{null, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Font") + ":"),  fontNameBox},
				{null, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Font_Size") + ":"),  fontSizeBox},
				{null, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Layout") + ": "), leftButton = new UIRadioButton(getLeftName())},
				{null,    null, 			rightButton = new UIRadioButton(getRightName())},
				
		};
		JPanel tablePane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
		centerPane.add(tablePane);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(leftButton);
		bg.add(rightButton);
		
		leftButton.setSelected(true);
	}
	
	protected String getLeftName() {// 居左 居右
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_At_The_Left");
	}
	
	protected String getRightName() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_At_The_Right");
	}
	
	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alert_Line");
	}
	
	private void showFormulaPane() {
		final UIFormula formulaPane = FormulaFactory.createFormulaPane();
		formulaPane.populate(BaseFormula.createFormulaBuilder().build(textField.getText()));
		formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(ChartAlertValuePane.this), new DialogActionAdapter(){
			public void doOk() {
				BaseFormula formula = formulaPane.update();
				textField.setText(Utils.objectToString(formula));
			}
		}).setVisible(true);
	}

    @Override
	public void populateBean(ChartAlertValue ob) {
    	//兼容以前设置的虚线
    	if (ob.getLineStyle().getLineStyle() != Constants.LINE_NONE
    		&& ob.getLineStyle().getLineStyle() != Constants.LINE_THIN
    		&& ob.getLineStyle().getLineStyle() != Constants.LINE_MEDIUM
    		&& ob.getLineStyle().getLineStyle() != Constants.LINE_THICK) {
    		ob.getLineStyle().setLineStyle(Constants.LINE_THIN);
    	}
		textField.setText(Utils.objectToString(ob.getAlertValueFormula()));
		lineCombo.setSelectedLineStyle(ob.getLineStyle().getLineStyle());
		colorBox.setSelectObject(ob.getLineColor().getSeriesColor());
		alphaPane.populate((int)(ob.getAlertLineAlpha() * ALPH_PER));
		
		contentField.setText(ob.getAlertContent());
		fontNameBox.setSelectedItem(ob.getAlertFont().getName());
		fontSizeBox.setSelectedItem(ob.getAlertFont().getSize());
		
		if(ob.getAlertPosition() == Constants.LEFT) {
			leftButton.setSelected(true);
		} else {
			rightButton.setSelected(true);
		}
	}

    @Override
	public ChartAlertValue updateBean() {
		ChartAlertValue chartAlertValue = new ChartAlertValue();
		
		updateBean(chartAlertValue);
		return chartAlertValue;
	}
    
    public void updateBean(ChartAlertValue chartAlertValue) {

		chartAlertValue.setAlertValueFormula(BaseFormula.createFormulaBuilder().build(textField.getText()));
		chartAlertValue.getLineColor().setSeriesColor(colorBox.getSelectObject());
		chartAlertValue.getLineStyle().setLineStyle(lineCombo.getSelectedLineStyle());
		chartAlertValue.setAlertLineAlpha(alphaPane.update());
		chartAlertValue.setAlertContent(contentField.getText());
		
		String fontName = Utils.objectToString(fontNameBox.getSelectedItem());
		int fontSize = DE_FONT; 
		Number number = Utils.objectToNumber(fontSizeBox.getSelectedItem(), true);
		if(number != null) {
			fontSize = number.intValue();
		}
		chartAlertValue.setAlertFont(FRFont.getInstance(fontName, Font.PLAIN, fontSize));
		
		if(leftButton.isSelected()) {
			chartAlertValue.setAlertPosition(Constants.LEFT);
		} else if(rightButton.isSelected()) {
			chartAlertValue.setAlertPosition(Constants.RIGHT);
		}
    }
}