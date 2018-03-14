package com.fr.design.mainframe;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.controlpane.UISimpleListControlPane;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WSortLayout;
import com.fr.general.NameObject;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plough on 2018/1/31.
 */
public class MobileWidgetListPane extends UISimpleListControlPane {
    public static final String LIST_NAME = "Widget_List";

    private FormDesigner designer;
    private WSortLayout wSortLayout;
    private String[] widgetNameList;

    public MobileWidgetListPane(FormDesigner designer, WSortLayout wSortLayout) {
        super();
        this.designer = designer;
        this.wSortLayout = wSortLayout;
        widgetNameList = getData();

        List<NameObject> nameObjectList = new ArrayList<NameObject>();
        for (String name : widgetNameList) {
            nameObjectList.add(new NameObject(name, null));
        }
        populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));
    }

    /**
     * 保存移动端控件列表顺序
     */
    public void updateToDesigner() {
        Nameable[] nameableList = update();
        List<String> newMobileWidgetList = new ArrayList<>();
        for (Nameable nameable : nameableList) {
            newMobileWidgetList.add(nameable.getName());
        }
        wSortLayout.updateSortedMobileWidgetList(newMobileWidgetList);
    }

    /**
     * 获取选中控件的控件列表
     *
     * @return List<String> widgetNameList
     */
    private String[] getData() {
        //选择的控件
        XCreator selectedCreator = designer.getSelectionModel().getSelection().getSelectedCreator();
        Widget selectedModel = selectedCreator != null ? selectedCreator.toData() : null;

        if (selectedModel == null || !selectedModel.acceptType(WSortLayout.class)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        // 选择的控件有两种类型，一种是WLayout，代表容器，一种是Widget，代表控件
        java.util.List<String> mobileWidgetList = ((WSortLayout) selectedModel).getOrderedMobileWidgetList();
        String[] widgetNames = new String[mobileWidgetList.size()];
        for (int i = 0; i < mobileWidgetList.size(); i++) {
            widgetNames[i] = mobileWidgetList.get(i);
        }
        return widgetNames;
    }
}