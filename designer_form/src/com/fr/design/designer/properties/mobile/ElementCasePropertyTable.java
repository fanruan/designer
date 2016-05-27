package com.fr.design.designer.properties.mobile;

import com.fr.base.FRContext;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.gui.itable.PropertyGroup;
import com.fr.design.gui.xtable.PropertyGroupModel;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.general.Inter;

import javax.swing.table.TableModel;
import java.beans.IntrospectionException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/16/0016.
 */
public class ElementCasePropertyTable extends AbstractPropertyTable {

    private XCreator xCreator;
    private FormDesigner designer;

    public ElementCasePropertyTable(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();

        groups = new ArrayList<PropertyGroup>();
        CRPropertyDescriptor[] propertyTableEditor = null;
        try {
            propertyTableEditor = new CRPropertyDescriptor[]{
                    new CRPropertyDescriptor("horziontalAttr", this.xCreator.toData().getClass()).setEditorClass(MobileFitEditor.class)
                            .setRendererClass(MobileFitRender.class)
                            .setI18NName(Inter.getLocText("FR-Designer_Mobile-Horizontal"))
                            .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, Inter.getLocText("FR-Designer_Fit-App")),
                    new CRPropertyDescriptor("verticalAttr", this.xCreator.toData().getClass()).setEditorClass(MobileFitEditor.class)
                            .setRendererClass(MobileFitRender.class)
                            .setI18NName(Inter.getLocText("FR-Designer_Mobile-Vertical"))
                            .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, Inter.getLocText("FR-Designer_Fit-App"))

            };
        } catch (IntrospectionException e) {
            FRContext.getLogger().error(e.getMessage());
        }


        groups.add(new PropertyGroup(new PropertyGroupModel(Inter.getLocText("FR-Designer_Fit-App"), xCreator, propertyTableEditor, designer)));

        TableModel model = new BeanTableModel();
        setModel(model);
        this.repaint();
    }

    @Override
    public void firePropertyEdit() {
    }

    public void populate(FormDesigner designer) {
        this.designer = designer;
        initPropertyGroups(this.designer.getTarget());
    }
}
