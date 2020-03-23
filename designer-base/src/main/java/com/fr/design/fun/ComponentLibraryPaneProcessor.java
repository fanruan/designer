package com.fr.design.fun;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.mainframe.DockingView;
import com.fr.stable.fun.mark.Immutable;

import javax.swing.JPanel;

/**
 * created by Harrison on 2020/03/16
 **/
public interface ComponentLibraryPaneProcessor extends Immutable {
    
    String XML_TAG = "ParameterExpandablePaneUIProvider";
    
    int CURRENT_LEVEL = 1;
    
    UIScrollPane createShowPanel(boolean isEdit);
    
    JPanel createMenuNorthPane();
    
    UIComboBox createMenuComBox();
    
    void parentView(DockingView dockingView);
    
    void parentPane(JPanel panel);
    
    void complete();
    
}
