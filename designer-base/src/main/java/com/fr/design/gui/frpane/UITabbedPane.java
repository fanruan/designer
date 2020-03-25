package com.fr.design.gui.frpane;

import com.fr.design.dialog.FineJOptionPane;
import com.fr.general.ComparatorUtils;


import javax.swing.*;
import javax.swing.plaf.TabbedPaneUI;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 13-3-28
 * Time: 上午9:14
 */
public class UITabbedPane extends JTabbedPane{

    private boolean isClosable = false; //Tab是否可关闭
    private String classPath;  //panel对象的类名
    private String tabName;  //Tab名称
    private  int tabSize = 0;
    public UITabbedPane() {
        super();
    }

    public UITabbedPane(int tabPlacement) {
        super(tabPlacement);
    }

    public UITabbedPane(int tabPlacement, int tabLayoutPolicy) {
        super(tabPlacement, tabLayoutPolicy);
    }

    public UITabbedPane(boolean closable,String tabname,String classpath){
        super();
        setClosable(closable);
        setTabName(tabname);
        setClassPath(classpath);
    }

    /**
     * 添加tab
     * @param s tab名
     * @param component 组件
     */
    public void addTab(String s, Component component) {
        if(isClosable() && ComparatorUtils.equals(s, getTabName())){
            super.addTab(s + tabSize, component);
        }else{
            super.addTab(s, component);
        }
        tabSize++;
    }

    /**
     * 设置tab可关闭/添加
     * @param isClosable 是否可关闭/添加
     */
    public void setClosable(boolean isClosable){
        this.isClosable = isClosable;
    }

    /**
     * tab可关闭
     * @return 返回是否tab可关闭
     */
    public boolean isClosable(){
        return this.isClosable;
    }

    public void setClassPath(String classpath){
        this.classPath = classpath;
    }

    public String getClassPath(){
        return this.classPath;
    }

    public void setTabName(String tabname){
        this.tabName = tabname;
    }

    public String getTabName(){
        return this.tabName;
    }

    public void setTabSize(int tabsize){
        tabSize = tabsize;
    }

    public int getTabSize(){
        return tabSize;
    }
    @Override
    /**
     * 获取UI对象
     */
    public TabbedPaneUI getUI(){
        return new UITabbedPaneUI();
    }

    @Override
    /**
     * 更新UI
     */
    public void updateUI() {
        setUI(getUI());
    }

    /**
     * 删除tab，不能直接复写removeTabAt
     * @param i tab索引
     */
    public void doRemoveTab(int i){
        int re = FineJOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(UITabbedPane.this), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Sure_To_Delete")+ "?", com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove")
                , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (re == JOptionPane.OK_OPTION) {
            super.removeTabAt(i);
        }
    }
}
