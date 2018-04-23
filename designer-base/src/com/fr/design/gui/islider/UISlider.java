package com.fr.design.gui.islider;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 13-3-28
 * Time: 下午1:30
 */
public class UISlider extends JSlider {

    public UISlider(){
       super();
    }

    public UISlider(int orientation){
       super(orientation);
    }

    public UISlider(int min, int max){
       super(min,max);
    }

    public UISlider(int min, int max, int value){
      super(min,max,value);
    }

    public UISlider(int orientation, int min, int max, int value){
       super(orientation,min,max,value);
    }

    public UISlider(BoundedRangeModel brm) {
       super(brm);
    }
}