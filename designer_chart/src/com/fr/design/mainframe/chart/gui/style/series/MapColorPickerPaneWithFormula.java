package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.design.formula.TinyFormulaPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class MapColorPickerPaneWithFormula extends UIColorPickerPane4Map {

    protected ArrayList getTextFieldList(){
   		return new ArrayList<TinyFormulaPane>();
   	}

   	protected void setTextValue4Index(int index,String value){
   		((TinyFormulaPane)textFieldList.get(index)).getUITextField().setText(value);
   	}

    protected JComponent getNewTextFieldComponent(int i,String value){
        TinyFormulaPane textField = new TinyFormulaPane();
   		textField.setBounds(0, i * 2 * TEXTFIELD_HEIGHT, TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
   		textField.getUITextField().setText(value);
   		return textField;
   	}

    protected String getValue4Index(int i){
        return ((TinyFormulaPane)textFieldList.get(i)).getUITextField().getText();
   	}

    /**
   	 * 设置变化的背景颜色
   	 * */
   	protected void setBackgroundUIColor(int index,Color color) {
        ((TinyFormulaPane)textFieldList.get(index)).getUITextField().setBackgroundUIColor(color);
   	}

}