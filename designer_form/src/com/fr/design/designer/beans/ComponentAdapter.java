package com.fr.design.designer.beans;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.designer.creator.PropertyGroupPane;

/**
 * 组件适配器接口
 * 主要目的是为具体组件提供特殊设计行为
 */
public interface ComponentAdapter {

    /**
     * 在组件选择面板上选择了组件类型后，在设计界面上跟随鼠标移动用来代表当前要添加组件的图形
     * 一般使用组件自身的图形代替。
     *
     * @param component 要添加的组件
     * @param g 当前设计器的图形上下文对象
     */
    void paintComponentMascot(Graphics g);

    /**
     * 当鼠标在此设计组件上右键点击时，该方法根据上下文和组件类型提供弹出响应的菜单
     *
     * @param 引发弹出菜单的鼠标事件
     *
     * @return 弹出菜单
     */
    JPopupMenu getContextPopupMenu(MouseEvent e);

    /**
     * 为当前组件创建描述属性表的model, 分组返回
     * @return BeanPropertyModel
     */
    ArrayList<GroupModel> getXCreatorPropertyModel();

    /**
     * 为当前组件创建描述各属性pane, 分组返回
     * @return ArrayList<PropertyGroupPane>
     */
    ArrayList<PropertyGroupPane> getXCreatorPropertyPane();

	/**
     * 提供双击设计器的编辑器
     * @param bean 鼠标双击的被设计组件
     * @return 被设计的编辑器
     */
    public DesignerEditor<? extends JComponent> getDesignerEditor();
    
    /**
     * 实例化组件的适配器后，在这儿进行初始化
     */
    void initialize();    
}