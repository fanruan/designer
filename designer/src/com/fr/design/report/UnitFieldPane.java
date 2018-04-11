/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.report;

import java.awt.Dimension;

import javax.swing.JFormattedTextField;

import com.fr.common.inputevent.InputEventBaseOnOS;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.OperatingSystem;
import com.fr.stable.unit.CM;
import com.fr.stable.unit.INCH;
import com.fr.stable.unit.MM;
import com.fr.stable.unit.UNIT;

/**
 * UnitFieldPane
 */
public class UnitFieldPane extends JPanel {
    private static final int TEXT_FIELD_COLUMNS = 4;
    private static final int TEXT_FIELD_COLUMNS_WINDOWS = 6;

    private UIBasicSpinner valueSpinner;
    private JFormattedTextField textField;
    
    private UnitLabel unitLable;
    private int unitType = Constants.UNIT_MM;
    
    public static class UnitLabel extends UILabel {
    	private int preferredHeight;
    	public UnitLabel(int unitType, int preferredHeight) {
    		super();

    		this.preferredHeight = preferredHeight;
    		
    		setUnitType(unitType);
    	}
    	
    	public void setUnitType(int unitType) {
            if (unitType == Constants.UNIT_CM) {
                this.setText(Inter.getLocText("FR-Designer_Unit_CM"));
            } else if (unitType == Constants.UNIT_INCH) {
                this.setText(Inter.getLocText("FR-Designer_Unit_INCH"));
            } else {
                this.setText(Inter.getLocText("FR-Designer_Unit_MM"));
            }

            //ajust the heigt of unitLabel.
            Dimension unitDimension = new Dimension(this.getPreferredSize().width,
            		preferredHeight);
            this.setMinimumSize(unitDimension);
            this.setMinimumSize(unitDimension);
            this.setSize(unitDimension);
            this.setPreferredSize(unitDimension);
    	}
    }

    public UnitFieldPane(int unitType) {
        this.setLayout(FRGUIPaneFactory.createBoxFlowLayout());
        
        this.unitType = unitType;

        valueSpinner = new UIBasicSpinner(new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, 1.0));
        textField = ((JSpinner.DefaultEditor) valueSpinner.getEditor()).getTextField();
        textField.setColumns(OperatingSystem.isWindows() ? TEXT_FIELD_COLUMNS_WINDOWS : TEXT_FIELD_COLUMNS);
        InputEventBaseOnOS.addBasicEditInputMap(textField);
        this.add(valueSpinner);
        unitLable = new UnitLabel(unitType, valueSpinner.getPreferredSize().height);
        this.add(unitLable);
    }
    
    public JFormattedTextField getTextField() {
		return textField;
	}

	public void setUnitType(int unitType) {
    	unitLable.setUnitType(unitType);
    	this.unitType = unitType;
    }

    public UNIT getUnitValue() {
        if (unitType == Constants.UNIT_CM) {
			return new CM(((Number) valueSpinner.getValue()).floatValue());
		} else if (unitType == Constants.UNIT_INCH) {
            return new INCH(((Number) valueSpinner.getValue()).floatValue());
        } else {
            return new MM(((Number) valueSpinner.getValue()).floatValue());
        }
    }

    public void setUnitValue(UNIT value) {
    	if (unitType == Constants.UNIT_CM) {
    		valueSpinner.setValue(new Float(value.toCMValue4Scale2()));
		} else if (unitType == Constants.UNIT_INCH) {
			valueSpinner.setValue(new Float(value.toINCHValue4Scale3()));
        } else {
        	valueSpinner.setValue(new Float(value.toMMValue4Scale2()));
        }
    }
}