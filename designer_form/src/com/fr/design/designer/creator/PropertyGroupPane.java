package com.fr.design.designer.creator;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.Widget;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by kerry on 2017/9/7.
 */
public class PropertyGroupPane extends BasicPane {
    private CRPropertyDescriptor[] crPropertyDescriptors;
    private CRPropertyDescriptorPane[] crPropertyDescriptorPanes;
    private XCreator xCreator;
    private String groupName;
    private FormDesigner designer;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public PropertyGroupPane(CRPropertyDescriptor[] crPropertyDescriptors, XCreator xCreator) {
        this.crPropertyDescriptors = crPropertyDescriptors;
        this.xCreator = xCreator;
        initComponent();
    }


    public PropertyGroupPane(CRPropertyDescriptor[] crPropertyDescriptors, XCreator xCreator, String groupName, FormDesigner designer) {
        this.designer = designer;
        this.groupName = groupName;
        this.crPropertyDescriptors = crPropertyDescriptors;
        this.xCreator = xCreator;
        initComponent();
    }

    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        int count = crPropertyDescriptors.length;
        crPropertyDescriptorPanes = new CRPropertyDescriptorPane[count];
        Component[][] components = new Component[count][];
        for (int i = 0; i < count; i++) {
            crPropertyDescriptorPanes[i] = new CRPropertyDescriptorPane(crPropertyDescriptors[i], xCreator, designer);
            components[i] = crPropertyDescriptorPanes[i].createTableLayoutComponent();
        }

        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        this.add(panel, BorderLayout.CENTER);
    }

    public void populate(Widget widget) {
        for (int i = 0; i < crPropertyDescriptorPanes.length; i++) {
            crPropertyDescriptorPanes[i].populate(widget);
        }
    }

    @Override
    protected String title4PopupWindow() {
        return "PropertyGroupPane";
    }


}
