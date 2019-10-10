package com.fr.design.mainframe;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.creator.XComponent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.creator.cardlayout.XWCardTagLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.Widget;
import com.fr.log.FineLoggerFactory;

import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.ArrayList;

public class FormSelection {

    private ArrayList<XCreator> selection;
    private Rectangle backupBounds;
    private ArrayList<Rectangle> recs = new ArrayList<Rectangle>();

    public FormSelection() {
        selection = new ArrayList<XCreator>();
    }

    /**
     * 重置选中的组件
     */
    public void reset() {
        selection.clear();
    }

    /**
     * 是否没有选中的组件
     *
     * @return 为空返回true
     */
    public boolean isEmpty() {
        return selection.isEmpty();
    }

    /**
     * 选中的组件数量
     *
     * @return 选中的组件数量
     */
    public int size() {
        return selection.size();
    }

    /**
     * 去除选中的组件中指定组件
     *
     * @param creator 待去除组件
     */
    public void removeCreator(XCreator creator) {
        selection.remove(creator);
    }

    /**
     * 是否成功删除选择的组件
     *
     * @param comp 组件
     * @return 是则返回true
     */
    public boolean removeSelectedCreator(XCreator comp) {
        if (selection.size() > 1 && selection.contains(comp)) {
            removeCreator(comp);
            return true;
        }
        return false;
    }

    /**
     * 成功增加选中的组件
     *
     * @param creator 组件
     * @return 成功增加返回true
     */
    public boolean addSelectedCreator(XCreator creator) {
        if (addedable(creator)) {
            selection.add(creator);
            return true;
        }
        return false;
    }

    /**
     * 是否是可以增加的
     *
     * @param creator 组件
     * @return 是则返回true
     */
    public boolean addedable(XCreator creator) {
        if (selection.isEmpty()) {
            return true;
        }
        XLayoutContainer container = XCreatorUtils.getParentXLayoutContainer(creator);
        if (!(container instanceof XWAbsoluteLayout)) {
            return false;
        }
        for (XCreator selected : selection) {
            if (selected == creator || XCreatorUtils.getParentXLayoutContainer(selected) != container) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回选中的第一个组件，为空返回null
     *
     * @return 返回选中组件
     */
    public XCreator getSelectedCreator() {
        return !selection.isEmpty() ? selection.get(0) : null;
    }

    /**
     * 返回选中的所有组件
     *
     * @return 所有组件s
     */
    public XCreator[] getSelectedCreators() {
        return selection.toArray(new XCreator[selection.size()]);
    }

    public Widget[] getSelectedWidgets() {
        Widget[] selectWidget = new Widget[selection.size()];
        for (int i = 0; i < selection.size(); i++) {
            selectWidget[i] = selection.get(i).toData();
        }
        return selectWidget;
    }

    public void setSelectedCreator(XCreator creator) {
        reset();
        selection.add(creator);
    }

    public void setSelectedCreators(ArrayList<XCreator> selections) {
        reset();
        for (XCreator creator : selections) {
            if (addedable(creator)) {
                selection.add(creator);
            }
        }
    }

    /**
     * 是否包含当前控件
     *
     * @param widget 控件
     * @return 是则返回true
     */
    public boolean contains(Widget widget) {
        for (XCreator creator : selection) {
            if (creator.toData() == widget) {
                return true;
            }
        }
        return false;
    }

    public int[] getDirections() {
        if (this.selection.size() > 1) {
            return Direction.ALL;
        } else if (this.selection.size() == 1) {
            return this.selection.get(0).getDirections();
        } else {
            return new int[0];
        }
    }

    /**
     * 备份组件的bound
     */
    public void backupBounds() {
        backupBounds = getRelativeBounds();
        recs.clear();
        for (XComponent comp : selection) {
            recs.add(comp.getBounds());
        }
    }

    public Rectangle getBackupBounds() {
        return backupBounds;
    }

    public Rectangle getRelativeBounds() {
        Rectangle bounds = getSelctionBounds();
        XLayoutContainer parent = XCreatorUtils.getParentXLayoutContainer(selection.get(0));
        if (parent == null) {
            return bounds;
        }
        Rectangle rec = ComponentUtils.getRelativeBounds(parent);
        bounds.x += rec.x;
        bounds.y += rec.y;
        return bounds;
    }

    public Rectangle getSelctionBounds() {
        if (selection.isEmpty()) {
            return new Rectangle();
        }
        Rectangle bounds = selection.get(0).getBounds();
        for (int i = 1, len = selection.size(); i < len; i++) {
            bounds = bounds.union(selection.get(i).getBounds());
        }
        return bounds;
    }

    public void setSelectionBounds(Rectangle rec, FormDesigner designer) {
        XLayoutContainer parent = XCreatorUtils.getParentXLayoutContainer(selection.get(0));
        Rectangle backupBounds = new Rectangle(this.backupBounds);
        if (parent != null) {
            Rectangle r = ComponentUtils.getRelativeBounds(parent);
            rec.x -= r.x;
            rec.y -= r.y;
            backupBounds.x -= r.x;
            backupBounds.y -= r.y;
        }

        int size = selection.size();
        if (size == 1) {
            XCreator creator = selection.get(0);
            if(creator.acceptType(XWCardTagLayout.class)){
                creator = (XCreator)selection.get(0).getParent();
            }
            creator.setBounds(rec);
            if (creator.acceptType(XWParameterLayout.class)) {
                designer.setParaHeight((int) rec.getHeight());
                designer.getArea().doLayout();
            }
            LayoutUtils.layoutContainer(creator);
        } else if (size > 1) {
            for (int i = 0; i < selection.size(); i++) {
                Rectangle newBounds = new Rectangle(recs.get(i));
                newBounds.x = rec.x + (newBounds.x - backupBounds.x) * rec.width / backupBounds.width;
                newBounds.y = rec.y + (newBounds.y - backupBounds.y) * rec.height / backupBounds.height;
                newBounds.width = rec.width * newBounds.width / backupBounds.width;
                newBounds.height = rec.height * newBounds.height / backupBounds.height;
                XCreator creator = selection.get(i);
                creator.setBounds(newBounds);
                if (creator.acceptType(XWParameterLayout.class)) {
                    designer.setParaHeight((int) rec.getHeight());
                    designer.getArea().doLayout();
                }
            }
            LayoutUtils.layoutRootContainer(designer.getRootComponent());
        }
    }

    /**
     * 调整组件大小
     *
     * @param designer 设计界面组件
     */
    public void fixCreator(FormDesigner designer) {
        for (XCreator creator : selection) {
            LayoutAdapter layoutAdapter = AdapterBus.searchLayoutAdapter(designer, creator);
            if (layoutAdapter != null) {
                creator.setBackupBound(backupBounds);
                layoutAdapter.fix(creator);
            }
        }
    }

    private void removeCreatorFromContainer(XCreator creator) {
        XLayoutContainer parent = XCreatorUtils.getParentXLayoutContainer(creator);
        if (parent == null) {
            return;
        }
        boolean changeCreator = creator.shouldScaleCreator() || creator.hasTitleStyle();
        if (parent.acceptType(XWFitLayout.class) && changeCreator) {
            creator = (XCreator) creator.getParent();
        }
        parent.getLayoutAdapter().removeBean(creator, creator.getWidth(), creator.getHeight());
        // 删除其根组件，同时就删除了同时被选择的叶子组件
        parent.remove(creator);
        LayoutManager layout = parent.getLayout();

        if (layout != null) {
            // 刷新组件容器的布局
            LayoutUtils.layoutContainer(parent);
        }
    }

    /**
     * 剪切选中的所有组件
     *
     * @param clipBoard 剪切板
     */
    public void cut2ClipBoard(FormSelection clipBoard) {
        clipBoard.reset();
        clipBoard.selection.addAll(selection);

        for (XCreator creator : selection) {
            removeCreatorFromContainer(creator);
        }
        reset();
    }

    /**
     * 复制选中的所有组件
     *
     * @param clipBoard 复制板
     */
    public void copy2ClipBoard(FormSelection clipBoard) {
        clipBoard.reset();

        for (XCreator root : selection) {
            if(root.acceptType(XWTabFitLayout.class)){
                continue;
            }
            try {
                XCreator creator = XCreatorUtils.createXCreator((Widget) root.toData().clone());
                creator.setBounds(root.getBounds());
                clipBoard.selection.add(creator);
            } catch (CloneNotSupportedException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
    }
}
