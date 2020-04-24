package com.fr.design.fun;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.mainframe.DockingView;
import com.fr.stable.fun.mark.Immutable;

import javax.swing.JPanel;

/**
 * 组件库面板处理器
 *
 * created by Harrison on 2020/03/16
 **/
public interface ComponentLibraryPaneProcessor extends Immutable {
    
    String XML_TAG = "ComponentLibraryPaneProcessor";
    
    int CURRENT_LEVEL = 1;
    
    /**
     * 创建展示面板
     *
     * @param isEdit 是否可以编辑
     * @return 展示面板
     */
    UIScrollPane createShowPanel(boolean isEdit);
    
    /**
     * 创建菜单的上部面板
     *
     * @return 面板
     */
    JPanel createMenuNorthPane();
    
    /**
     * 创建复选框
     *
     * @return 复选框
     */
    UIComboBox createMenuComBox();
    
    void parentView(DockingView dockingView);
    
    /**
     * 父面板
     *
     * @param panel 面板
     */
    void parentPane(JPanel panel);
    
    /**
     * 创建完成
     */
    void complete();
    
}
