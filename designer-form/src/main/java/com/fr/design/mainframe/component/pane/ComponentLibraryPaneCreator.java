package com.fr.design.mainframe.component.pane;

import com.fr.design.fun.ComponentLibraryPaneProcessor;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DockingView;
import com.fr.design.mainframe.component.ComponentLibraryManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * created by Harrison on 2020/03/22
 **/
public class ComponentLibraryPaneCreator {
    
    private ComponentLibraryPaneProcessor processor;
    
    public ComponentLibraryPaneCreator() {
        
        processor = ComponentLibraryManager.selectPaneProcessor();
    }
    
    public static ComponentLibraryPaneCreator getNew() {
    
        return new ComponentLibraryPaneCreator();
    }
    
    public JPanel create(DockingView dockingView) {
    
        JPanel componentLibPanel = createComponentLibPanel();
        processor.parentView(dockingView);
        processor.parentPane(componentLibPanel);
        
        JPanel menuPanel = createMenuPanel();
        componentLibPanel.add(menuPanel, BorderLayout.NORTH);
        
        UIScrollPane showPane = processor.createShowPanel(false);
        componentLibPanel.add(showPane);
        return componentLibPanel;
    }
    
    private JPanel createComponentLibPanel() {
        
        JPanel reuWidgetPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        reuWidgetPanel.setBorder(null);
        return reuWidgetPanel;
    }
    
    /**
     * 初始化菜单栏面板
     */
    private JPanel createMenuPanel() {
        
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        menuPanel.setBorder(BorderFactory.createEmptyBorder(3, 10, 10, 15));
        
        JPanel menuPanelNorthPane = processor.createMenuNorthPane();
        menuPanel.add(menuPanelNorthPane, BorderLayout.NORTH);
        
        UIComboBox menuPanelComboBox = processor.createMenuComBox();
        menuPanel.add(menuPanelComboBox, BorderLayout.CENTER);
        return menuPanel;
    }
}
