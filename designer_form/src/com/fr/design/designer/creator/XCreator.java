/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.*;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.stable.StableUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author richer
 * @since 6.5.3 com.fr.base.listener.OB的设计组件
 * 
 */
public abstract class XCreator extends JPanel implements XComponent, XCreatorTools {

	protected static final Border DEFALUTBORDER = BorderFactory.createLineBorder(new Color(210, 210, 210), 1);
	public static final Dimension SMALL_PREFERRED_SIZE = new Dimension(80, 21);
	protected static final Dimension MIDDLE_PREFERRED_SIZE = new Dimension(80, 50);
	protected static final Dimension BIG_PREFERRED_SIZE = new Dimension(80, 80);
	// barry: 拖拽控件时，控件要恢复原始大小，就先把控件当前大小备份到这里。
	protected Dimension backupSize;
	protected XLayoutContainer backupParent;

	protected Widget data;
	protected JComponent editor;
	// XCreator加入到某些XLayoutContainer中时，能调整宽度或者高度
	private int[] directions;
	private Rectangle backupBound;

	public XCreator(Widget ob, Dimension initSize) {
		this.data = ob;

		this.initEditor();

		if (editor != null && editor != this) {
			this.setLayout(FRGUIPaneFactory.createBorderLayout());
			add(editor, BorderLayout.CENTER);
		}

		if (initSize.width == 0) {
			initSize.width = this.initEditorSize().width;
		}
		if (initSize.height == 0) {
			initSize.height = this.initEditorSize().height;
		}
		this.setPreferredSize(initSize);
		this.setSize(initSize);
		this.setMaximumSize(initSize);
		this.initXCreatorProperties();
	}

	public int[] getDirections() {
		return directions;
	}

	public void setDirections(int[] directions) {
		this.directions = directions;
	}

	/**
	 * 应用备份的大小
	 */
	public void useBackupSize() {
		if (this.backupSize != null) {
			setSize(this.backupSize);
		}
	}
	
	/**
	 * 备份当前大小
	 */
	public void backupCurrentSize() {
		this.backupSize = getSize();
	}

	public XLayoutContainer getBackupParent() {
		return backupParent;
	}

	public void setBackupParent(XLayoutContainer backupContainer) {
		this.backupParent = backupContainer;
	}

	/**
	 * 备份当前parent容器
	 */
	public void backupParent() {
		setBackupParent(XCreatorUtils.getParentXLayoutContainer(this));
	}
	
	/**
	 * 获取当前XCreator的一个封装父容器
	 * 
	 * @param widgetName 当前组件名
	 * 
	 * @return 封装的父容器
	 * 
	 *
	 * @date 2014-11-25-下午4:47:23
	 * 
	 */
	protected XLayoutContainer getCreatorWrapper(String widgetName){
		return new XWTitleLayout();
	}
	
	/**
	 * 将当前对象添加到父容器中
	 * 
	 * @param parentPanel 父容器组件
	 * 
	 *
	 * @date 2014-11-25-下午4:57:55
	 * 
	 */
	protected void addToWrapper(XLayoutContainer parentPanel, int width, int minHeight){			
		parentPanel.add(this, WTitleLayout.BODY);
	}
	
	/**
	 * 设置父容器的名字
	 * 
	 * @param parentPanel 当前父容器
	 * @param widgetName 当前控件名
	 * 
	 *
	 * @date 2014-11-27-上午9:47:00
	 * 
	 */
	protected void setWrapperName(XLayoutContainer parentPanel, String widgetName){
		parentPanel.toData().setWidgetName(widgetName);
	}
	
	/**
	 * 初始化当前组件的父容器
	 * 大体分为三种: Scale缩放型, Title标题型, Border自定义标题栏
	 * 
	 * @param minHeight 最小高度
	 * 
	 * @return 父容器
	 * 
	 *
	 * @date 2014-11-25-下午5:15:23
	 * 
	 */
	public XLayoutContainer initCreatorWrapper(int minHeight){
		XLayoutContainer parentPanel;
		String widgetName = this.toData().getWidgetName();
		parentPanel = this.getCreatorWrapper(widgetName);
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		parentPanel.setLocation(this.getX(), this.getY());
		parentPanel.setSize(width, height);
		setWrapperName(parentPanel, widgetName);
		this.setLocation(0, 0);
		this.addToWrapper(parentPanel, width, minHeight);
		LayoutUtils.layoutRootContainer(parentPanel);
		
		return parentPanel;
	}

	/**
	 * 初始化creator的属性值
	 */
	public void rebuid() {
		initXCreatorProperties();
	}

	/**
	 * 返回组件属性值
	 * @return 返回组件属性值
	 * @throws IntrospectionException 异常
	 */
	public abstract CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException;

	/**
	 * 生成creator对应的控件widget
	 * @return 控件widget
	 */
	public Widget toData() {
		return this.data;
	}

	protected abstract JComponent initEditor();

	/**
	 * 根据Widget的属性值初始化XCreator的属性值
	 */
	protected abstract void initXCreatorProperties();

	/**
	 * 返回XCreator的默认大小80x21
	 * @return 默认的最小大小
	 */
	public Dimension initEditorSize() {
		return SMALL_PREFERRED_SIZE;
	}

	protected String getIconName() {
        return "";
    }

	public String getIconPath() {
		return "/com/fr/web/images/form/resources/" + getIconName();
	}

	/**
	 * 返回组件默认名
	 * @return 组件类名(小写)
	 */
	public String createDefaultName() {
		String name = this.getClass().getSimpleName();
		return Character.toLowerCase(name.charAt(1)) + name.substring(2);
	}

	@Override
	public void setBounds(Rectangle bounds) {
		Dimension size = this.getMinimumSize();
		if (bounds.getWidth() < size.width) {
			bounds.width = size.width;
			//针对拖动，不大好。
			bounds.x = this.getX();
		}
		if (bounds.getHeight() < size.height) {
			bounds.height = size.height;
			bounds.y = this.getY();
		}
		super.setBounds(bounds);
	}

	public DesignerEditor<? extends JComponent> getDesignerEditor() {
		return null;
	}

	/**
	 * 根据权限编辑工具界面
	 * @param jform 表单容器
	 *@param formEditor 设计界面组件
	 *@return 工具界面
	 */
	public JComponent createToolPane(BaseJForm jform, FormDesigner formEditor) {
		if (!BaseUtils.isAuthorityEditing()) {
			if (isDedicateContainer()) {
				// 图表块和报表块由于控件树处不显示，但对应的属性表要显示，此处处理下
				XCreator child = ((XLayoutContainer) this).getXCreator(0);
				return child.createToolPane(jform, formEditor);
			}
			return WidgetPropertyPane.getInstance(formEditor);
		} else {
			//判断是不是布局，布局不支持权限编辑
			if (formEditor.isSupportAuthority()) {
				AuthorityPropertyPane authorityPropertyPane = new AuthorityPropertyPane(formEditor);
				authorityPropertyPane.populate();
				return authorityPropertyPane;
			}

			return new NoSupportAuthorityEdit();

		}

	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(0, 0);
	}
	
	/**
	 * 是否支持切换到报表界面编辑
	 * @return 是则返回true
	 */
	public boolean isReport(){
		return false;
	}
    
    /**
     * 该组件是否可以拖入参数面板
     * @return 是则返回true
     */
    public boolean canEnterIntoParaPane(){
        return true;
    }

    /**
     * 该组件是否可以拖入表单主体
     * @return 是则返回true
     */
    public boolean canEnterIntoAdaptPane(){
        return true;
    }

    /**
     * 该组件是否可以拖拽(表单中参数面板和自适应布局不可以拖拽)
     * @return 是则返回true
     */
    public boolean isSupportDrag(){
        return true;
    }

    public List<String> getAllXCreatorNameList(XCreator xCreator,  List<String> namelist){
        namelist.add(xCreator.toData().getWidgetName());
        return namelist;
    }

    /**
     * 是否有查询按钮
     * @param xCreator  控件或容器
     * @return  有无查询按钮
     */
    public boolean SearchQueryCreators(XCreator xCreator) {
        return false;
    }

	/**
	 * @return the backupBound
	 */
	public Rectangle getBackupBound() {
		return backupBound;
	}

	/**
	 * @param rec the backupBound to set
	 */
	public void setBackupBound(Rectangle rec) {
		this.backupBound = rec;
	}
	
	/**
	 * 控件树不显示此组件
	 * @param path 控件树list
	 */
	public void notShowInComponentTree(ArrayList<Component> path) {
		return;
	}
	
	/**
	 * 重置组件的名称
	 * @param name 名称
	 */
	public void resetCreatorName(String name) {
		toData().setWidgetName(name);
	}
	
	/**
	 * 返回编辑的子组件，scale为其内部组件
	 * @return 组件
	 */
	public XCreator getEditingChildCreator() {
		return this;
	}
	
	/**
	 * 返回对应属性表的组件，scale和title返回其子组件
	 * @return 组件
	 */
	public XCreator getPropertyDescriptorCreator() {
		return this;
	}
	
	/**
	 * 更新子组件的Bound; 没有不处理
	 * @param minHeight 最小高度
	 */
	public void updateChildBound(int minHeight) {
		return;
	}
	
	/**
	 * 是否作为控件树的叶子节点
	 * @return 是则返回true
	 */
	public boolean isComponentTreeLeaf() {
		return true;
	}
	
	/**
	 *  是否为sclae和title专属容器
	 * @return 是则返回true
	 */
	public boolean isDedicateContainer() {
		return false;
	}
	
	/**
     * 是否接收这种类型
     * @param acceptTypes 接收的类型
     * @return 接收指定的类型则返回true,否则返回false
     */
    public boolean acceptType(Class<?>... acceptTypes) {
        for (Class<?> type : acceptTypes) {
            if (StableUtils.classInstanceOf(this.getClass(), type)) {
                return true;
            }
        }
        return false;
    }

	/**
	 * 是否组件要缩放(自适应里部分组件需要, 如数字、文本、下拉框、下拉复选框、密码、下拉树、下拉复选树、日期)
	 * 
	 * @return 是则返回true
	 */
	public boolean shouldScaleCreator() {
		return false;
	}
	
	/**
	 * 是否支持标题样式
	 * @return 默认false
	 */
	public boolean hasTitleStyle() {
		return false;
	}
	
	/**
	 * 响应点击事件
	 * 
	 * @param editingMouseListener 鼠标点击，位置处理器
	 * @param e 鼠标点击事件
	 */
	public void respondClick(EditingMouseListener editingMouseListener,MouseEvent e){
		FormDesigner designer = editingMouseListener.getDesigner();
		SelectionModel selectionModel = editingMouseListener.getSelectionModel();

		if (e.getClickCount() <= 1) {
			selectionModel.selectACreatorAtMouseEvent(e);
		}

		if (editingMouseListener.stopEditing()) {
			if (this != designer.getRootComponent()) {
				ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
				editingMouseListener.startEditing(this, adapter.getDesignerEditor(), adapter);
			}
		}
	}
	
	/**
	 * 删除相关组件
	 * 
	 * @param creator 当前组件
	 * @param designer 表单设计器
	 *
	 */
	public void deleteRelatedComponent(XCreator creator,FormDesigner designer){
		return;
	}
	
	/**
	 * 选择相关组件
	 * 
	 * @param creator 当前组件
	 * 
	 */
	public void seleteRelatedComponent(XCreator creator){
		return;
	}
	
	/**
	 * 返回组件
	 * @return
	 * String
	 */
	public XCreator getXCreator(){
		return this;
	}
	
	/**
	 * 按百分比调整组件
	 * @param percent 百分比
	 * void
	 */
	public void adjustCompSize(double percent){
		return;
	}
	
	/**
	 * 返回一些需要的子组件
	 * @return 返回一些需要的子组件
	 * ArrayList<?>
	 */
	public ArrayList<?> getTargetChildrenList(){
		return new ArrayList();
	}
	
	public XLayoutContainer getOuterLayout(){
		return this.getBackupParent();
	}
	
	/**
	 * 重新调整子组件宽度
	 * @param width 宽度
	 */
	public void recalculateChildWidth(int width){
		return;
	}
	/**
	 * 重新调整子组件高度
	 * @param height 高度
	 */
	public void recalculateChildHeight(int height){
		return;
	}
}