package com.fr.design.chart.series.SeriesCondition.impl;


import com.fr.design.DesignModelAdapter;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;

import com.fr.js.FormHyperlinkProvider;
import com.fr.stable.bridge.StableFactory;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * + * Created with IntelliJ IDEA.
 * + * User: zx
 * + * Date: 14-8-6
 * + * Time: 下午2:53
 * +
 *
 * @author zx
 */
public class FormHyperlinkNorthPane extends BasicBeanPane<FormHyperlinkProvider> {

    private UITextField itemNameTextField;
    private boolean needRenamePane = false;
    private Widget[] formHyperlinkEditors;
    private UIComboBox targetFrameComboBox;

    public FormHyperlinkNorthPane(boolean needRenamePane) {
        this.needRenamePane = needRenamePane;
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();

        this.add(centerPane, BorderLayout.CENTER);
        formHyperlinkEditors = getFormHyperlinkEditors();
        targetFrameComboBox = formHyperlinkEditors == null ? new UIComboBox() : new UIComboBox(getFormHyperlinkEditNames());
        targetFrameComboBox.setRenderer(new DefaultListCellRenderer());
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, TableLayout.FILL};
        Component[][] components;
        if (!this.needRenamePane) {
            components = new Component[][]{
                    {new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Form-Object")), targetFrameComboBox},
            };
        } else {
            itemNameTextField = new UITextField();
            components = new Component[][]{
                    {new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Name_has_Colon")), itemNameTextField},
                    {new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Form-Object")), targetFrameComboBox},
            };
        }
        JPanel northPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        centerPane.add(northPane, BorderLayout.NORTH);
    }

    public Widget getEditingEditor() {
        if (formHyperlinkEditors == null) {
            return null;
        }
        String editingEditorName = (String) targetFrameComboBox.getSelectedItem();
        for (Widget editor : formHyperlinkEditors) {
            if (ComparatorUtils.equals(editingEditorName, editor.getWidgetName())) {
                return editor;
            }
        }
        return null;
    }

    private Widget[] getFormHyperlinkEditors() {
        return DesignModelAdapter.getCurrentModelAdapter().getLinkableWidgets();
    }


    private String[] getFormHyperlinkEditNames() {
        String[] editorNames = new String[formHyperlinkEditors.length];
        int i = 0;
        for (Widget editor : formHyperlinkEditors) {
            editorNames[i] = editor.getWidgetName();
            i++;
        }
        return editorNames;
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Hyperlink-Form_link");
    }

    @Override
    public void populateBean(FormHyperlinkProvider formHyperlink) {
        formHyperlinkEditors = getFormHyperlinkEditors();
        if (itemNameTextField != null) {
            this.itemNameTextField.setText(formHyperlink.getItemName());
        }
        String editorName = formHyperlink.getRelateEditorName();
        //防止初始的时候有空白选项
        if (editorName == null) {
            return;
        }
        if (targetFrameComboBox != null) {
            //noinspection unchecked
            targetFrameComboBox.setModel(new DefaultComboBoxModel(getFormHyperlinkEditNames()));
            targetFrameComboBox.setSelectedItem(editorName);
        }
    }


    @Override
    public FormHyperlinkProvider updateBean() {
        FormHyperlinkProvider formHyperlink =
                StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);
        updateBean(formHyperlink);
        return formHyperlink;
    }

    @Override
    public void updateBean(FormHyperlinkProvider formHyperlink) {
        if (itemNameTextField != null) {
            formHyperlink.setItemName(this.itemNameTextField.getText());
        }
        formHyperlink.setRelateEditorName((String) targetFrameComboBox.getSelectedItem());
    }
}