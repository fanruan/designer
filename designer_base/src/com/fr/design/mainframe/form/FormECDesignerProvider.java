package com.fr.design.mainframe.form;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.form.FormElementCaseProvider;

public interface FormECDesignerProvider {
	
	public static final String XML_TAG = "FormElementCaseDesigner";

    /**
     * 选中目标的 对应Menu
     * @return 返回MenuDef数组.
     */
	public MenuDef[] menus4Target();

    /**
     * 获取焦点
     */
    public void requestFocus() ;

    /**
     * 工具栏菜单字体字号等
     * @return   工具栏菜单数组
     */
	public ToolBarDef[] toolbars4Target();

    /**
     * 表单的报表块的工具按钮复制剪切那行
     * @return 工具按钮
     */
    public JComponent[] toolBarButton4Form();

    /**
     * 模板菜单
     * @return    返回菜单
     */
	public ShortCut[] shortcut4TemplateMenu();

    /**
     *当前正在编辑的elementcase
     * @return   当前正在编辑的elementcase
     */
	public FormElementCaseProvider getEditingElementCase();

    /**
     *  右上角属性表
     * @return     属性面板
     */
	public JComponent getEastDownPane();

    /**
     *   右下角控件树或者扩展属性
     * @return    属性面板
     */
	public JComponent getEastUpPane();

    /**
     * 获取当前ElementCase的缩略图
     * @param elementCaseContainerSize 缩略图的大小
     * @return    图
     */
	public BufferedImage getElementCaseImage(Dimension elementCaseContainerSize);

}