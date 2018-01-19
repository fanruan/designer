package com.fr.design.designer.treeview;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.fr.base.FRContext;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteBodyLayout;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.form.ui.Widget;

public class ComponentTreeModel implements TreeModel {

    private ArrayList<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
    private Component root;
    private FormDesigner designer;

    private final int ABSOLUTE_AS_BODY_NOT_FOUND = -1;


    public ComponentTreeModel(FormDesigner designer, Component root) {
        this.designer = designer;
        this.root = root;
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent != null && parent instanceof XLayoutContainer) {
        	XLayoutContainer xlayout = (XLayoutContainer) parent;
        	XCreator creator = xlayout.getXCreator(index);
            //绝对布局作为body的时候不显示自适应布局父层
            int absoluteBodyIndex = getAbsoluteBodyIndex(creator);
            if (absoluteBodyIndex > ABSOLUTE_AS_BODY_NOT_FOUND){
                return creator.getComponent(absoluteBodyIndex);
            }
        	return creator.getXCreator();
        }
        return null;
    }
    /**
     * 设置根节点
     * 
     * @param root 根节点
     */
    public void setRoot(Component root){
    	this.root=root;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent != null && parent instanceof XLayoutContainer) {
        	XLayoutContainer xlayout = (XLayoutContainer) parent;
        	return xlayout.getShowXCreatorCount();
        }

        return 0;
    }

    /**
     * 是否为叶子节点
     * @param node 对象
     * @return 是则返回true
     */
    @Override
    public boolean isLeaf(Object node) {
        if (node != null && node instanceof XCreator) {
            return ((XCreator) node).isComponentTreeLeaf();
        }
        return true;
    }

    /**
     * 树节点值改变
     * @param path 树结构路径
     * @param newValue 新值
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        Object lastObject = path.getLastPathComponent();
        if (lastObject != newValue) {
            if (newValue instanceof String) {
                // Change varaible name
                XCreator component = (XCreator) lastObject;
                Widget wgt = ((XWidgetCreator)component).toData();
                wgt.setWidgetName((String)newValue);
				designer.getEditListenerTable().fireCreatorModified(component, DesignerEvent.CREATOR_EDITED);
            }
            TreeModelEvent event = new TreeModelEvent(this, path.getPath());
            fireEvent("treeNodesChanged", event);
        }
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent != null && parent instanceof XLayoutContainer) {
        	return ((XLayoutContainer)parent).getIndexOfChild(child);
        }
        return -1;
    }
    
    /**
     * 添加树事件
     * @param l 事件
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    /**
     * 删除树事件
     * @param l 事件
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        if (listeners.contains(l)) {
            listeners.remove(l);
        }
    }

    protected void fireEvent(String eventName, TreeModelEvent evt) {
        try {
            Method m = TreeModelListener.class.getMethod(eventName, TreeModelEvent.class);

            for (TreeModelListener listener : listeners) {
                m.invoke(listener, evt);
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
    }

    private int getAbsoluteBodyIndex(XCreator xCreator){
        //绝对布局作为body，父层是自适应布局，找到绝对布局位于父层的index
        int index = ABSOLUTE_AS_BODY_NOT_FOUND;
        if (xCreator.acceptType(XWFitLayout.class)){
            XWFitLayout bodyFitLayout = (XWFitLayout)xCreator;
            for (int i = 0;i < bodyFitLayout.getXCreatorCount();i++){
                //类型是绝对布局并且还是body
                if (bodyFitLayout.getXCreator(i).acceptType(XWAbsoluteBodyLayout.class)){
                    index = i;
                }
            }
        }
        return index;
    }
}