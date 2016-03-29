package com.fr.design.mainframe;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fr.base.FRContext;
import com.fr.general.ComparatorUtils;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.form.main.ClonedWidgetCreator;
import com.fr.form.ui.Widget;

public class FormSelectionUtils {

	public static void paste2Container(FormDesigner designer, XLayoutContainer parent, FormSelection selection, int x,
			int y) {
		LayoutAdapter adapter = parent.getLayoutAdapter();
		if (selection.size() == 1) {
			try {
				XCreator creator = selection.getSelectedCreator();
				Widget cloned = new ClonedWidgetCreator(designer.getTarget()).clonedWidgetWithNoRepeatName(creator
						.toData());
				XCreator clondCreator = XCreatorUtils.createXCreator(cloned, creator.getSize());
				if (adapter.addBean(clondCreator, x + clondCreator.getWidth() / 2, y + clondCreator.getHeight() / 2)) {
					designer.getSelectionModel().getSelection().setSelectedCreator(clondCreator);
					designer.getEditListenerTable().fireCreatorModified(clondCreator, DesignerEvent.CREATOR_PASTED);
					return;
				}
			} catch (CloneNotSupportedException e) {
				FRContext.getLogger().error(e.getMessage(), e);
			}
		} else if (selection.size() > 1) {
			if (parent instanceof XWAbsoluteLayout) {
				designer.getSelectionModel().getSelection().reset();
				Rectangle rec = selection.getSelctionBounds();
				for (XCreator creator : selection.getSelectedCreators()) {
					try {
						Widget cloned = new ClonedWidgetCreator(designer.getTarget())
								.clonedWidgetWithNoRepeatName(creator.toData());
						XCreator clondCreator = XCreatorUtils.createXCreator(cloned, creator.getSize());
						// 设置位置，移动20x20，防止被粘帖的组件重叠，照顾表单布局情况下
						adapter.addBean(clondCreator, x + creator.getX() - rec.x + clondCreator.getWidth() / 2, y
								+ creator.getY() - rec.y + clondCreator.getHeight() / 2);
						designer.getSelectionModel().getSelection().addSelectedCreator(clondCreator);
					} catch (CloneNotSupportedException e) {
						FRContext.getLogger().error(e.getMessage(), e);
					}
				}
				designer.getEditListenerTable().fireCreatorModified(
						designer.getSelectionModel().getSelection().getSelectedCreator(), DesignerEvent.CREATOR_PASTED);
				return;
			}
		}
		Toolkit.getDefaultToolkit().beep();
	}

	public static void rebuildSelection(FormDesigner designer) {
		ArrayList<XCreator> newSelection = new ArrayList<XCreator>();
		List<Widget> widgetList = new ArrayList<Widget>();
		for (XCreator comp : designer.getSelectionModel().getSelection().getSelectedCreators()) {
			widgetList.add(comp.toData());
		}
		designer.getSelectionModel().setSelectedCreators(
				rebuildSelection(designer.getRootComponent(), widgetList, newSelection));
	}

	public static ArrayList<XCreator> rebuildSelection(XCreator rootComponent, Widget[] selectWidgets) {
		List<Widget> selectionWidget = new ArrayList<Widget>();
		if(selectWidgets != null){
			selectionWidget.addAll(Arrays.asList(selectWidgets));
		}
		return FormSelectionUtils.rebuildSelection(rootComponent, selectionWidget, new ArrayList<XCreator>());
	}

	private static ArrayList<XCreator> rebuildSelection(XCreator rootComponent, List<Widget> selectionWidget,
			ArrayList<XCreator> newSelection) {
		FormSelectionUtils._rebuild(rootComponent, selectionWidget, newSelection);
		if (newSelection.isEmpty()) {
			newSelection.add(rootComponent);
		}
		return newSelection;
	}

	private static void _rebuild(XCreator root, List<Widget> selectionWidget, List<XCreator> newSelection) {
		if (selectionWidget.isEmpty()) {
			return;
		}
		for (Widget x : selectionWidget) {
			if (ComparatorUtils.equals(x, root.toData())) {
				if (!newSelection.contains(root)) {
					newSelection.add(root);
					selectionWidget.remove(x);
				}
				break;
			}
		}

		int count = root.getComponentCount();
		for (int i = 0; i < count && !selectionWidget.isEmpty(); i++) {
			Component c = root.getComponent(i);
			if (c instanceof XCreator) {
				XCreator creator = (XCreator) c;
				for (Widget x : selectionWidget) {
					if (ComparatorUtils.equals(x, creator.toData())) {
						newSelection.add(creator);
						selectionWidget.remove(x);
						break;
					}
				}
				if (c instanceof XLayoutContainer) {
					_rebuild((XLayoutContainer) c, selectionWidget, newSelection);
				} else {
					continue;
				}
			}
		}
	}
}