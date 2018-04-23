package com.fr.design.chart.series.SeriesCondition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpinnerNumberModel;

import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.chart.base.ChartEquationType;
import com.fr.chart.base.ConditionTrendLine;
import com.fr.design.chart.comp.BorderAttriPane;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

public class ConditionTrendLinePane extends BasicBeanPane<ConditionTrendLine> {
	private static final long serialVersionUID = 3867164332100351117L;

	private ConditionTrendLine editing;
	
	private UITextField nameLabel;
	
	private BorderAttriPane linePane;
	
	private UIRadioButton linearButton;
	private UIRadioButton polynomialButton;
	private UIRadioButton logButton;
	private UIRadioButton exponentButton;
	private UIRadioButton powerButton;
	private UIRadioButton maButton;
	private UIBasicSpinner maSpinner;
	
	private UITextField forwardLabel;
	private UITextField backwardLabel;
	
	public ConditionTrendLinePane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		JPanel pane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		this.add(pane, BorderLayout.NORTH);
		
		JPanel namePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		pane.add(namePane);
		namePane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[]{"Chart_TrendLine" , "WF-Name"}), null));
		namePane.add(new UILabel(Inter.getLocText(new String[]{"Define", "WF-Name"})));
		namePane.add(nameLabel = new UITextField("", 6));
		
		pane.add(linePane = new BorderAttriPane());
		linePane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[]{"Chart_Trend", "Line-Style"}), null));
		
		JPanel typePane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		pane.add(typePane);
		
		typePane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[]{"Chart_Trend", "Type"}), null));
		
		JPanel buttonPane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
		typePane.add(buttonPane);
		
		buttonPane.add(exponentButton = new UIRadioButton(Inter.getLocText("Chart_Exponent")));
		buttonPane.add(linearButton = new UIRadioButton(Inter.getLocText("Chart_Linear")));
		buttonPane.add(logButton = new UIRadioButton(Inter.getLocText("Chart_Log")));
		buttonPane.add(polynomialButton = new UIRadioButton(Inter.getLocText("Chart_Polynomial")));
		buttonPane.add(powerButton = new UIRadioButton(Inter.getLocText("Chart_Power")));
		
		JPanel maPane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
		typePane.add(maPane);
		
		maPane.add(maButton = new UIRadioButton(Inter.getLocText("Chart_Move_Average")));
		maPane.add(new UILabel(Inter.getLocText("cycle") + ":"));
		maPane.add(maSpinner = new UIBasicSpinner(new SpinnerNumberModel(2, 1, 999, 1)));
		maSpinner.setEnabled(false);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(exponentButton);
		bg.add(linearButton);
		bg.add(logButton);
		bg.add(polynomialButton);
		bg.add(powerButton);
		bg.add(maButton);
		
		pane.add(initExtendsPane());
		
		initListeners();
	}
	
	private JPanel initExtendsPane() {
		JPanel extendsPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();		// 前推倒推设置
		extendsPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[]{"Chart_Trend", "Forecast"}), null));
		
		extendsPane.add(new UILabel(Inter.getLocText("Chart_TrendLine_Forward") + ":"));
		extendsPane.add(forwardLabel = new UITextField("0", 5));
		extendsPane.add(new UILabel(Inter.getLocText("cycle")));
		extendsPane.add(new UILabel(Inter.getLocText("Chart_TrendLine_Backward") + ":"));
		extendsPane.add(backwardLabel = new UITextField("0", 5));
		extendsPane.add(new UILabel(Inter.getLocText("cycle")));
		
		return extendsPane;
	}
	
	private void initListeners() {
		exponentButton.addActionListener(listener);
		linearButton.addActionListener(listener);
		logButton.addActionListener(listener);
		polynomialButton.addActionListener(listener);
		powerButton.addActionListener(listener);
		
		maButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(maButton.isSelected()) {
					maSpinner.setEnabled(true);
				}
			}
		});
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Chart_TrendLine");
	}
	
	ActionListener listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() instanceof JRadioButton) {
				JRadioButton selectedButton = (JRadioButton)e.getSource();
				if(selectedButton.isSelected()) {
					maSpinner.setEnabled(false);
				}
			}
		}
	};

    @Override
	public void populateBean(ConditionTrendLine ob) {
    	editing = ob;
    	
		nameLabel.setText(ob.getLine().getTrendLineName());
		
		forwardLabel.setText(Integer.toString(ob.getForward()));
		backwardLabel.setText(Integer.toString(ob.getBackward()));
		
		if (ob.getLine() != null) {
			linePane.setLineStyle(ob.getLine().getLineStyleInfo().getAttrLineStyle().getLineStyle());
			Color trendLineColor = ob.getLine().getLineStyleInfo().getAttrLineColor().getSeriesColor();
			trendLineColor = (trendLineColor == null ? new Color(128,128,128) : trendLineColor);
			linePane.setLineColor(trendLineColor);

            ChartEquationType current = ob.getLine().getEquation();
			
			maSpinner.setEnabled(false);

            switch (current){
                case EXPONENT:
                    exponentButton.setSelected(true);
                    break;
                case LINEAR:
                    linearButton.setSelected(true);
                    break;
                case LOG:
                    logButton.setSelected(true);
                    break;
                case POLYNOMIAL:
                    polynomialButton.setSelected(true);
                    break;
                case POWER:
                    powerButton.setSelected(true);
                    break;
                case MA:
                    maButton.setSelected(true);
                    maSpinner.setEnabled(true);
                    maSpinner.setValue(ob.getLine().getMoveAverage());
                    break;
            }

		}
	}

    @Override
	public ConditionTrendLine updateBean() {
    	updateBean(editing);
		
		return editing;
	}
    
    public void updateBean(ConditionTrendLine trendLine) {
    	try {
			maSpinner.commitEdit();
		} catch (ParseException e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}

		trendLine.getLine().setTrendLineName(nameLabel.getText());
		
		trendLine.setForward(Utils.string2Number(forwardLabel.getText()).intValue());
		trendLine.setBackward(Utils.string2Number(backwardLabel.getText()).intValue());
		
		if(trendLine.getLine() != null) {
			if (exponentButton.isSelected()) {
				trendLine.getLine().setEquation(ChartEquationType.EXPONENT);
			} else if (linearButton.isSelected()) {
				trendLine.getLine().setEquation(ChartEquationType.LINEAR);
			}
			else if (logButton.isSelected()) {
				trendLine.getLine().setEquation(ChartEquationType.LOG);
			}
			else if (polynomialButton.isSelected()) {
				trendLine.getLine().setEquation(ChartEquationType.POLYNOMIAL);
			}
			else if (powerButton.isSelected()) {
				trendLine.getLine().setEquation(ChartEquationType.POWER);
			}
			else if (maButton.isSelected()) {
				trendLine.getLine().setEquation(ChartEquationType.MA);
				trendLine.getLine().setMoveAverage(
						Utils.string2Number(Utils.objectToString(maSpinner.getValue())).intValue());
			}
			
			trendLine.getLine().getLineStyleInfo().getAttrLineColor().setSeriesColor(linePane.getLineColor());
			trendLine.getLine().getLineStyleInfo().getAttrLineStyle().setLineStyle(linePane.getLineStyle());
		}
    }
}