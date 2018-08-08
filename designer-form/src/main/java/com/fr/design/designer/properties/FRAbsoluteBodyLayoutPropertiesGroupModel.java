package com.fr.design.designer.properties;

import com.fr.design.designer.creator.*;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.widget.editors.LayoutTypeEditor;
import com.fr.form.ui.container.WBodyLayoutType;
import com.fr.log.FineLoggerFactory;


import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by zhouping on 2016/10/14.
 */
public class FRAbsoluteBodyLayoutPropertiesGroupModel extends FRAbsoluteLayoutPropertiesGroupModel {
    private LayoutTypeEditor layoutTypeEditor;
    private LayoutTypeRenderer layoutTypeRenderer;

    //默认body是0,自适应布局;1,绝对布局.
    private WBodyLayoutType layoutType = WBodyLayoutType.ABSOLUTE;
    private static final int EACH_ROW_COUNT = 4;

    public FRAbsoluteBodyLayoutPropertiesGroupModel(XWAbsoluteBodyLayout xwAbsoluteBodyLayout) {
        super(xwAbsoluteBodyLayout);
    }

    public FRAbsoluteBodyLayoutPropertiesGroupModel(XWAbsoluteBodyLayout xwAbsoluteBodyLayout, WBodyLayoutType layoutType){
        this(xwAbsoluteBodyLayout);
        this.layoutTypeEditor = new LayoutTypeEditor();
        this.layoutTypeRenderer = new LayoutTypeRenderer();
        this.layoutType = layoutType;
    }

    /**
     * 布局管理器自己的属性
     */
    @Override
    public String getGroupName() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Attr_Layout");
    }

    @Override
    public int getRowCount() {
        return 2;
    }

    @Override
    public TableCellRenderer getRenderer(int row) {
        return row == 0 ? layoutTypeRenderer : stateRenderer;
    }

    @Override
    public TableCellEditor getEditor(int row) {
        return row == 0 ? layoutTypeEditor : stateEditor;
    }

    @Override
    public Object getValue(int row, int column) {
        if (column == 0) {
            switch (row) {
                case 0:
                    return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Attr_Layout_Type");
                default:
                    return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Scaling_Mode");
            }
        } else {
            switch (row) {
                case 0:
                    return layoutType.getTypeValue();
                default:
                    return layout.getCompState();
            }
        }
    }

    @Override
    public boolean setValue(Object value, int row, int column) {
        if (layoutType == WBodyLayoutType.ABSOLUTE){
            int state = 0;
            if(value instanceof Integer) {
                state = (Integer)value;
            }
            if (column == 0 || state < 0) {
                return false;
            } else {
                if (row == 0) {
                    if (state == WBodyLayoutType.FIT.getTypeValue()) {
                        return switch2FitBodyLayout();
                    }
                }
                if (row == 1) {
                    layout.setCompState(state);
                    return true;
                }
                return false;
            }
        }
        int state = 0;
        if(value instanceof Integer) {
            state = (Integer)value;
        }
        if (column == 0 || state < 0) {
            return false;
        } else {
            if (row == 0) {
                layout.setCompState(state);
                return true;
            }
            return false;
        }
    }

    /**
     * 是否可编辑
     * @param row 行
     * @return 否
     */
    @Override
    public boolean isEditable(int row) {
        return true;
    }

    private boolean switch2FitBodyLayout() {
        try {
            XWFitLayout xfl = (XWFitLayout) xwAbsoluteLayout.getBackupParent();
            //备份一下组件间隔
            int compInterval = xfl.toData().getCompInterval();
            Component[] components = xwAbsoluteLayout.getComponents();

            Arrays.sort(components, new ComparatorComponentLocation());

            xfl.getLayoutAdapter().removeBean(xwAbsoluteLayout, xwAbsoluteLayout.getWidth(), xwAbsoluteLayout.getHeight());
            xfl.remove(xwAbsoluteLayout);

            for (Component comp : components) {
                XCreator xCreator = (XCreator) comp;
                if (xCreator.shouldScaleCreator()) {
                    XLayoutContainer parentPanel = xCreator.initCreatorWrapper(xCreator.getHeight());
                    xfl.add(parentPanel, xCreator.toData().getWidgetName());
                    parentPanel.updateChildBound(xfl.getActualMinHeight());
                    continue;
                }
                xfl.add(xCreator);
            }
            //这边计算的时候会先把组件间隔去掉
            moveComponents2FitLayout(xfl);
            FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
            formDesigner.getSelectionModel().setSelectedCreator(xfl);

            for (int i = 0; i < components.length; i++) {
                Component comp = xfl.getComponent(i);
                XCreator creator = (XCreator) comp;
                creator.setBackupBound(components[i].getBounds());
            }

            //把组件间隔加上
            if (xfl.toData().getCompInterval() != compInterval) {
                xfl.moveContainerMargin();
                xfl.moveCompInterval(xfl.getAcualInterval());
                xfl.toData().setCompInterval(compInterval);
                xfl.addCompInterval(xfl.getAcualInterval());
            }
            xfl.toData().setLayoutType(WBodyLayoutType.FIT);
            return true;
        }
        catch (Exception e){
            FineLoggerFactory.getLogger().error(e.getMessage());
            return false;
        }
    }

    // 把绝对布局中的元素按规则移动到自适应布局中
    // 规则：各元素按顺序放置，其中每行最多4个元素，超出则换行，各元素均分body的高度和宽度
    private void moveComponents2FitLayout(XWFitLayout xwFitLayout) {
        Component[] components = xwFitLayout.getComponents();
        if (components.length == 0){
            xwFitLayout.updateBoundsWidget();
            return;
        }
        int layoutWidth = xwFitLayout.getWidth() - xwFitLayout.toData().getMargin().getLeft() - xwFitLayout.toData().getMargin().getRight();
        int layoutHeight = xwFitLayout.getHeight() - xwFitLayout.toData().getMargin().getTop() - xwFitLayout.toData().getMargin().getBottom();
        int leftMargin = xwFitLayout.toData().getMargin().getLeft();
        int topMargin = xwFitLayout.toData().getMargin().getTop();
        xwFitLayout.toData().setCompInterval(0);
        int row = (components.length / EACH_ROW_COUNT) + (components.length % EACH_ROW_COUNT == 0 ? 0 : 1);
        //最后一行的列数不定
        int column = components.length % EACH_ROW_COUNT == 0 ? EACH_ROW_COUNT : components.length % EACH_ROW_COUNT;
        int componentWidth = layoutWidth / EACH_ROW_COUNT;
        int componentHeight = layoutHeight / row;
        for(int i = 0;i < row - 1;i++){
            for(int j = 0;j < EACH_ROW_COUNT;j++){
                components[EACH_ROW_COUNT * i + j].setBounds(
                        leftMargin + componentWidth * j,
                        topMargin + componentHeight * i,
                        j == EACH_ROW_COUNT - 1 ? layoutWidth - componentWidth * (EACH_ROW_COUNT - 1) : componentWidth,
                        componentHeight
                );
            }
        }
        //最后一行列数是特殊的，要单独处理
        int lastRowWidth = layoutWidth / column;
        int lastRowHeight = layoutHeight - componentHeight * (row - 1);
        for (int i = 0;i < column;i++) {
            components[EACH_ROW_COUNT * (row - 1) + i].setBounds(
                    leftMargin + lastRowWidth * i,
                    topMargin + componentHeight * (row - 1),
                    i == column - 1 ? layoutWidth - lastRowWidth * (column - 1) : lastRowWidth,
                    lastRowHeight
            );
        }
        for (int i = 0;i < components.length;i++){
            if (components[i] instanceof XWCardMainBorderLayout){
                ((XWCardMainBorderLayout)components[i]).recalculateChildWidth(components[i].getWidth(), false);
                ((XWCardMainBorderLayout)components[i]).recalculateChildHeight(components[i].getHeight(), false);
            }
            xwFitLayout.dealDirections((XCreator)components[i], false);
        }
        xwFitLayout.updateBoundsWidget();
    }

    private class ComponentLocationInfo{
        private Component component;
        private int horizontalNO;
        private int verticalNO;

        public ComponentLocationInfo(Component component, int horizontalNO, int verticalNO){
            this.component = component;
            this.horizontalNO = horizontalNO;
            this.verticalNO = verticalNO;
        }

        public int getHorizontalNO() {
            return this.horizontalNO;
        }

        public int getVerticalNO() {
            return this.verticalNO;
        }

        public void setHorizontalNO(int horizontalNO){
            this.horizontalNO = horizontalNO;
        }

        public void setVerticalNO(int verticalNO){
            this.verticalNO = verticalNO;
        }
    }

    //以组件的位置来确定先后顺序，y小的在前，x小的在前
    private class ComparatorComponentLocation implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            if(((Component)o1).getY() < ((Component)o2).getY()){
                return -1;
            }
            else if (((Component)o1).getY() > ((Component)o2).getY()) {
                return 1;
            }
            else {
                if (((Component)o1).getX() < ((Component)o2).getX()){
                    return -1;
                }
                else if (((Component)o1).getX() > ((Component)o2).getX()) {
                    return 1;
                }
                else{
                    return 0;
                }
            }
        }
    }
}
