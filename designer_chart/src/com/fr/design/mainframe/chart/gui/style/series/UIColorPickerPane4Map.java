package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.ChartConstants;
import com.fr.design.style.color.ColorCell;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.style.color.ColorSelectPane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class UIColorPickerPane4Map extends UIColorPickerPane {

    protected ColorSelectBox getColorSelectBox(){
  			return new ColorSelectBox4Map(100);
  	}

    private class ColorSelectPane4Map extends ColorSelectPane {
   		public void initCenterPaneChildren(JPanel centerPane) {
   			JPanel menuColorPane1 = new JPanel();
   			centerPane.add(menuColorPane1);
   			menuColorPane1.setLayout(new GridLayout(5, 8, 5, 5));
   			for (int i = 0; i < ChartConstants.MAP_COLOR_ARRAY.length; i++) {
   				menuColorPane1.add(new ColorCell(ChartConstants.MAP_COLOR_ARRAY[i], this));
   			}
   			centerPane.add(Box.createVerticalStrut(5));
   			centerPane.add(new JSeparator());
   		}

        protected Color[] getColorArray(){
            return ChartConstants.MAP_COLOR_ARRAY;
        }
   	}

   	private class ColorSelectBox4Map extends ColorSelectBox{
   		public ColorSelectBox4Map(int preferredWidth){
   			super(preferredWidth);
   		}

   		protected ColorSelectPane getColorSelectPane(){
   			return new ColorSelectPane4Map();
   		}
   	}


}