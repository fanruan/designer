/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.fr.design.mainframe.widget.editors.InChangeBooleanEditor;
import com.fr.design.mainframe.widget.editors.TreeModelEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.TreeModelRenderer;
import com.fr.form.ui.FieldEditor;
import com.fr.form.ui.TreeEditor;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public class XTreeEditor extends XWidgetCreator {

    public XTreeEditor(TreeEditor widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
	/**
	 * 树控件的属性面板
	 * @return   属性数组
	 * @throws  Introspection过程错误
	 */
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        CRPropertyDescriptor[] crp = !((FieldEditor) toData()).isAllowBlank() ?
                new CRPropertyDescriptor[]{
                        new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
                                Inter.getLocText(new String[]{"FR-Designer_Widget", "Value"})).setEditorClass(WidgetValueEditor.class),
                        new CRPropertyDescriptor("model", this.data.getClass(), "getNodeOrDict", "setNodeOrDict").setI18NName(
                                Inter.getLocText("FR-Designer_DS-Dictionary")).setEditorClass(TreeModelEditor.class).setRendererClass(
                                TreeModelRenderer.class),
                        new CRPropertyDescriptor("allowBlank", this.data.getClass()).setI18NName(
                                Inter.getLocText("FR-Designer_Allow_Blank")).setEditorClass(InChangeBooleanEditor.class).putKeyValue(
                                XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                        new CRPropertyDescriptor("errorMessage", this.data.getClass()).setI18NName(
                                Inter.getLocText("FR-Designer_Verify-Message"))
                                .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                }
                : new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
                        Inter.getLocText(new String[]{"FR-Designer_Widget", "Value"})).setEditorClass(WidgetValueEditor.class),
                new CRPropertyDescriptor("model", this.data.getClass(), "getNodeOrDict", "setNodeOrDict").setI18NName(
                        Inter.getLocText("FR-Designer_DS-Dictionary")).setEditorClass(TreeModelEditor.class).setRendererClass(
                        TreeModelRenderer.class),
                new CRPropertyDescriptor("allowBlank", this.data.getClass()).setI18NName(
                        Inter.getLocText("FR-Designer_Allow_Blank")).setEditorClass(InChangeBooleanEditor.class).putKeyValue(
                        XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),};

		crp = this.addWaterMark(crp);
		crp = (CRPropertyDescriptor[]) ArrayUtils.add(crp,
				new CRPropertyDescriptor("fontSize", this.data.getClass(), "getFontSize", "setFontSize")
						.setI18NName(Inter.getLocText(new String[]{"FR-Designer_Font", "FRFont-Size"}))
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"));
        crp = (CRPropertyDescriptor[]) ArrayUtils.add(crp, new CRPropertyDescriptor("multipleSelection", this.data.getClass()).setI18NName(
                Inter.getLocText("Tree-Mutiple_Selection_Or_Not")).putKeyValue(
                XCreatorConstants.PROPERTY_CATEGORY, "Advanced").setEditorClass(InChangeBooleanEditor.class));

        crp = (CRPropertyDescriptor[]) ArrayUtils.add(crp, new CRPropertyDescriptor("ajax", this.data.getClass()).setI18NName(
                Inter.getLocText("Widget-Load_By_Async")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
                "Advanced"));

		crp = this.addAllowEdit(crp);
		crp = this.addCustomData(crp);

        crp = (CRPropertyDescriptor[]) ArrayUtils.add(crp, new CRPropertyDescriptor("selectLeafOnly", this.data
                .getClass()).setI18NName(Inter.getLocText("Tree-Select_Leaf_Only")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"));
        crp = (CRPropertyDescriptor[]) ArrayUtils.add(crp, new CRPropertyDescriptor("returnFullPath", this.data
                .getClass()).setI18NName(Inter.getLocText("Tree-Return_Full_Path")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"));

        return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(), crp);
    }

	protected CRPropertyDescriptor[] addWaterMark(CRPropertyDescriptor[] crp) throws IntrospectionException{
		return crp;
	}

	protected CRPropertyDescriptor[] addAllowEdit(CRPropertyDescriptor[] crp) throws IntrospectionException{
		return crp;
	}

	protected CRPropertyDescriptor[] addCustomData(CRPropertyDescriptor[] crp) throws IntrospectionException{
		return crp;
	}

    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("Tree");
            root.add(new DefaultMutableTreeNode("Leaf1"));
            root.add(new DefaultMutableTreeNode("Leaf2"));
            editor = new JTree(root);
            editor.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        }
        return editor;
    }

    @Override
    /**
     * 控件的预定义大小
	 * @return     控件的预定义大小
     */
    public Dimension initEditorSize() {
        return SMALL_PREFERRED_SIZE;
    }

    @Override
    protected String getIconName() {
        return "tree_16.png";
    }
    
}