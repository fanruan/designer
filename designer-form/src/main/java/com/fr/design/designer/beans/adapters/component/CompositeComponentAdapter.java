package com.fr.design.designer.beans.adapters.component;

import com.fr.design.actions.UpdateAction;
import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.actions.ChangeNameAction;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.PropertyGroupPane;
import com.fr.design.designer.creator.XButton;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.xtable.PropertyGroupModel;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.Button;
import com.fr.form.ui.Widget;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CompositeComponentAdapter implements ComponentAdapter {

	protected FormDesigner designer;
	protected DesignerEditor<? extends JComponent> editorComponent;
	protected XCreator xCreator;

	public CompositeComponentAdapter(FormDesigner designer, Component c) {
		this.designer = designer;
		this.xCreator = (XCreator) c;
	}
    /**
     * 实例化组件的适配器后，在这儿进行初始化
     */
	public void initialize() {
		initButtonText();
		Dimension initialSize = xCreator.getPreferredSize();
		xCreator.setSize(initialSize);
		LayoutUtils.layoutContainer(xCreator);
	}

	private void initButtonText() {
		Widget widget = xCreator.toData();
		if (xCreator instanceof XButton && StringUtils.isEmpty(((Button) widget).getText())) {
			((Button) xCreator.toData()).setText(widget.getWidgetName());
			((XButton) xCreator).setButtonText(widget.getWidgetName());
		}
	}

	@Override
	public void paintComponentMascot(Graphics g) {
        //自适应交叉点渲染有点问题，拖拽的控件设置成半透明
        Graphics2D g2d = (Graphics2D) g;
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.5f);
        g2d.setComposite(composite);
		xCreator.paint(g2d);
		g.setColor(XCreatorConstants.RESIZE_BOX_BORDER_COLOR);
		g.drawRect(0, 0, xCreator.getWidth() - 1, xCreator.getHeight() - 1);
	}

	@Override
	public JPopupMenu getContextPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		if (changeVarNameAction == null) {
			changeVarNameAction = new ChangeNameAction(designer);
		}
        //底层布局或者是自适应布局都不能删除
        boolean isRootComponent = ComponentUtils.isRootComponent(xCreator) || designer.isRoot(xCreator);
		//bug103155 有的布局的重命名(tab布局)涉及到其他非子节点的属性修改,支持起来比较麻烦,先屏蔽了控件树面板的修改,强制使用属性面板修改
		changeVarNameAction.setEnabled(!isRootComponent && xCreator.supportRenameInWidgetTree());
		popupMenu.add(changeVarNameAction);

		Action[] actions = designer.getActions();
		for (Action action : actions) {
            action.setEnabled(!designer.isRootRelatedAction(((UpdateAction)action).getName()) || !isRootComponent);
			popupMenu.add(action);
		}
		return popupMenu;
	}

	private ChangeNameAction changeVarNameAction;

	private ArrayList<PropertyGroupModel> createPropertyGroupModels(CRPropertyDescriptor[] properties) {
		HashMap<String, ArrayList<CRPropertyDescriptor>> maps = new HashMap<String, ArrayList<CRPropertyDescriptor>>();
		ArrayList<String> groupNames = getGroupNames(properties, maps);
		ArrayList<PropertyGroupModel> groups = new ArrayList<PropertyGroupModel>();
		for (String groupName : groupNames) {
			ArrayList<CRPropertyDescriptor> groupProperties = maps.get(groupName);
			PropertyGroupModel groupModel = new PropertyGroupModel(groupName, xCreator, groupProperties
					.toArray(new CRPropertyDescriptor[0]), designer);
			groups.add(groupModel);
		}
		return groups;
	}

	private ArrayList<PropertyGroupPane> createPropertyGroupPanes(CRPropertyDescriptor[] properties) {
		HashMap<String, ArrayList<CRPropertyDescriptor>> maps = new HashMap<String, ArrayList<CRPropertyDescriptor>>();
		ArrayList<String> groupNames = getGroupNames(properties, maps);
		ArrayList<PropertyGroupPane> groups = new ArrayList<PropertyGroupPane>();
		for (String groupName : groupNames) {
			ArrayList<CRPropertyDescriptor> groupProperties = maps.get(groupName);
			PropertyGroupPane propertyGroupPane = new PropertyGroupPane(groupProperties.toArray(new CRPropertyDescriptor[0]), xCreator, groupName, designer);
			groups.add(propertyGroupPane);
		}
		return groups;
	}

	private ArrayList<String> getGroupNames(CRPropertyDescriptor[] properties, HashMap<String, ArrayList<CRPropertyDescriptor>> maps ){
		ArrayList<String> groupNames = new ArrayList<String>();
		for (CRPropertyDescriptor property : properties) {
			String groupName = (String) property.getValue(XCreatorConstants.PROPERTY_CATEGORY);
			if (StringUtils.isEmpty(groupName)) {
				groupName = (String) property.getValue(XCreatorConstants.PROPERTY_VALIDATE);
				if(StringUtils.isEmpty(groupName)){
					groupName = XCreatorConstants.DEFAULT_GROUP_NAME;
				}
			}
			ArrayList<CRPropertyDescriptor> groupProperties = maps.get(groupName);
			if (groupProperties == null) {
				groupProperties = new ArrayList<CRPropertyDescriptor>();
				maps.put(groupName, groupProperties);
				groupNames.add(groupName);
			}
			groupProperties.add(property);
		}
		adjustGroupNamesPosition(groupNames);
		return groupNames;
	}

		public void adjustGroupNamesPosition(ArrayList<String> groupNames){
			for(String groupName : groupNames){
				if(groupName.equals("Fine-Design_Basic_Form_Basic_Properties")){
					groupNames.remove(groupName);
					groupNames.add(0,groupName);
					break;
				}
			}
		}
	@Override
	public ArrayList<GroupModel> getXCreatorPropertyModel() {
		ArrayList<GroupModel> groupModels = new ArrayList<GroupModel>();
		CRPropertyDescriptor[] properties;
		properties = getCalculateCreatorProperties();
		ArrayList<PropertyGroupModel> groups = createPropertyGroupModels(properties);
		Collections.sort(groups);
		groupModels.addAll(groups);
		return groupModels;
	}

	@Override
	public ArrayList<PropertyGroupPane> getXCreatorPropertyPane() {
		ArrayList<PropertyGroupPane> groupModels = new ArrayList<PropertyGroupPane>();
		CRPropertyDescriptor[] properties;
		properties = getCalculateCreatorProperties();
		ArrayList<PropertyGroupPane> groups = createPropertyGroupPanes(properties);
//		Collections.sort(groups);
		groupModels.addAll(groups);
		return groupModels;
	}

	/**
	 * 自适应布局中放置文本框等用的scaleLayout和报表块、图表块支持的标题控件用的titleLayout时
	 * 控件树处只显示父容器，但是控件属性还是为自身的
	 * @return
	 */
	private CRPropertyDescriptor[] getCalculateCreatorProperties() {
		try {
			return xCreator.getPropertyDescriptorCreator().supportedDescriptor();
		} catch (IntrospectionException ex) {
            FineLoggerFactory.getLogger().error(ex.getMessage(), ex);
			return new CRPropertyDescriptor[0];
		}
	}

	@Override
	public DesignerEditor<? extends JComponent> getDesignerEditor() {
		if (editorComponent == null) {
			editorComponent = xCreator.getDesignerEditor();
			if (editorComponent != null) {
				editorComponent.addPropertyChangeListener(new PropertyChangeAdapter() {

					@Override
					public void propertyChange() {
						designer.fireTargetModified();
					}
				});
			}
		}
		if (editorComponent != null) {
			editorComponent.reset();
		}
		return editorComponent;
	}
}