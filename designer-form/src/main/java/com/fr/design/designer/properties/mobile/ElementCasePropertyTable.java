package com.fr.design.designer.properties.mobile;

import com.fr.base.FRContext;
import com.fr.base.mobile.MobileFitAttrState;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.gui.itable.PropertyGroup;
import com.fr.design.gui.xtable.ReportAppPropertyGroupModel;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.widget.editors.InChangeBooleanEditor;
import com.fr.design.mainframe.widget.editors.RefinedDoubleEditor;
import com.fr.form.ui.ElementCaseEditor;


import javax.swing.table.TableModel;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/16/0016.
 */
public class ElementCasePropertyTable extends AbstractPropertyTable{

    private XCreator xCreator;
    private FormDesigner designer;
    private boolean cascade = false;

    public ElementCasePropertyTable(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        if (((ElementCaseEditor ) xCreator.toData()).getVerticalAttr() == MobileFitAttrState.VERTICAL && !((ElementCaseEditor ) xCreator.toData()).isHeightRestrict()) {
            ((ElementCaseEditor ) xCreator.toData()).setHeightRestrict(true);
            cascade = true;
            return revealHeightLimit();
        }
        CRPropertyDescriptor[] crp = ((ElementCaseEditor) xCreator.toData()).isHeightRestrict() ? revealHeightLimit() : getDefault();
        cascade = ((ElementCaseEditor ) xCreator.toData()).getVerticalAttr() == MobileFitAttrState.VERTICAL;
        return crp;
    }

    protected List<CRPropertyDescriptor> createNonListenerProperties() throws IntrospectionException {
        CRPropertyDescriptor[] propertyTableEditor = {
                new CRPropertyDescriptor("horziontalAttr", this.xCreator.toData().getClass()).setEditorClass(MobileFitEditor.class)
                        .setRendererClass(MobileFitRender.class)
                        .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Horizontal"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Fit")),
                new CRPropertyDescriptor("verticalAttr", this.xCreator.toData().getClass()).setEditorClass(MobileFitEditor.class)
                        .setRendererClass(MobileFitRender.class)
                        .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Vertical"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Fit")),
                new CRPropertyDescriptor("heightRestrict", this.xCreator.toData().getClass()).setEditorClass(InChangeBooleanEditor.class)
                        .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_EC_Heightrestrict"))
                        .setRendererClass(BooleanRender.class)
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Fit"))
        };
        List<CRPropertyDescriptor> defaultList = new ArrayList<>();

        for (CRPropertyDescriptor propertyDescriptor: propertyTableEditor) {
            defaultList.add(propertyDescriptor);
        }
        return defaultList;
    }

    protected CRPropertyDescriptor[] revealHeightLimit() throws IntrospectionException {
        CRPropertyDescriptor heightLimitProperty = new CRPropertyDescriptor("heightPercent", this.xCreator.toData().getClass())
                                                                .setEditorClass(RefinedDoubleEditor.class)
                                                                .setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_EC_Heightpercent"))
                                                                .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced");
        ArrayList<CRPropertyDescriptor> defaultList = (ArrayList<CRPropertyDescriptor>) createNonListenerProperties();
        defaultList.add(heightLimitProperty);
        return defaultList.toArray(new CRPropertyDescriptor[defaultList.size()]);
    }

    protected CRPropertyDescriptor[] getDefault() throws IntrospectionException {
        ArrayList<CRPropertyDescriptor> defaultList = (ArrayList<CRPropertyDescriptor>) createNonListenerProperties();
        return defaultList.toArray(new CRPropertyDescriptor[defaultList.size()]);
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();

        groups = new ArrayList<PropertyGroup>();
        CRPropertyDescriptor[] propertyTableEditor = null;
        try {
            propertyTableEditor = supportedDescriptor();
        } catch (IntrospectionException e) {
            FRContext.getLogger().error(e.getMessage());
        }


        groups.add(new PropertyGroup(new ReportAppPropertyGroupModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Fit"), xCreator, propertyTableEditor, designer)));

        TableModel model = new BeanTableModel();
        setModel(model);
        this.repaint();
    }

    @Override
    public void firePropertyEdit() {
        designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (cascade && row ==3 ) {
            return false;
        }
        return super.isCellEditable(row, column);
    }

    public void populate(FormDesigner designer) {
        this.designer = designer;
        initPropertyGroups(this.designer.getTarget());
    }
}
