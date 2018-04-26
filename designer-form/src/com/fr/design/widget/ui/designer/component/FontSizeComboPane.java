package com.fr.design.widget.ui.designer.component;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.Vector;

/**
 * Created by kerry on 2017/10/23.
 */
public class FontSizeComboPane extends JPanel{
    private static final int MAX_FONT_SIZE = 100;
    private UIComboBox comboBox;
    public FontSizeComboPane(){
        initComponent();
    }
    public void initComponent(){
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        Vector<Integer> integerList = new Vector<Integer>();
        for (int i = 1; i < MAX_FONT_SIZE; i++) {
            integerList.add(i);
        }
        comboBox = new UIComboBox(integerList);
        comboBox.setEditable(true);
        this.add(comboBox, BorderLayout.CENTER);
    }

    public int getValue() {
        return Integer.valueOf(comboBox.getSelectedItem().toString());
    }

    public void setValue(int fontSize) {
        comboBox.setSelectedItem(fontSize);
    }


}
