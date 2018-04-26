package com.fr.design.mainframe.form;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.fr.design.designer.TargetComponent;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.form.FormElementCaseProvider;

public interface FormECDesignerProvider {

    String XML_TAG = "FormElementCaseDesigner";

    /**
     * 选中目标的 对应Menu
     * @return 返回MenuDef数组.
     */
	MenuDef[] menus4Target();

    /**
     * 获取焦点
     */
    void requestFocus() ;

    /**
     * 工具栏菜单字体字号等
     * @return   工具栏菜单数组
     */
	ToolBarDef[] toolbars4Target();

    /**
     * 表单的报表块的工具按钮复制剪切那行
     * @return 工具按钮
     */
    JComponent[] toolBarButton4Form();

    /**
     * 模板菜单
     * @return    返回菜单
     */
	ShortCut[] shortcut4TemplateMenu();

    /**
     *当前正在编辑的elementcase
     * @return   当前正在编辑的elementcase
     */
	FormElementCaseProvider getEditingElementCase();

    /**
     *  右上角属性表
     * @return     属性面板
     */
	JComponent getEastDownPane();

    /**
     *   右下角控件树或者扩展属性
     * @return    属性面板
     */
	JComponent getEastUpPane();

    /**
     *   条件属性面板
     * @return    属性面板
     */
    JComponent getConditionAttrPane();

    /**
     *   超级链接面板
     *   @param jt 当前模板（JForm）
     * @return    属性面板
     */
    JComponent getHyperlinkPane(JTemplate jt);

    /**
     *   超级链接面板
     * @return    属性面板
     */
    TargetComponent getEditingElementCasePane();

    /**
     * 获取当前ElementCase的缩略图
     * @param elementCaseContainerSize 缩略图的大小
     * @return    图
     */
	BufferedImage getElementCaseImage(Dimension elementCaseContainerSize);

}