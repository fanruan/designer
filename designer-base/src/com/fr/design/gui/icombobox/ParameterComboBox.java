package com.fr.design.gui.icombobox;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.fr.base.Parameter;
import com.fr.design.DesignModelAdapter;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.general.ComparatorUtils;

public class ParameterComboBox extends UIComboBox {

    public ParameterComboBox() {
        this.initComponents();
    }

    private void initComponents() {
        //render
        this.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list,
                                                          Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof com.fr.base.Parameter) {
                    this.setText(((Parameter) value).toString());
                }

                return this;
            }
        });

        //update parameter items
        updateParaItems();
    }

    /**
     * Update the parameter items of the comboBox
     */
    public void updateParaItems() {
        //model
        DesignModelAdapter<?, ?> designModel = DesignModelAdapter.getCurrentModelAdapter();

        this.removeAllItems();
        if (designModel != null) {
            Parameter[] paras = designModel.getParameters();
            List<String> paraNameList = new ArrayList<String>();
            for (int i = 0; i < paras.length; i++) {
                String paraName = paras[i].getName();
                if (!paraNameList.contains(paraName)) {
                    paraNameList.add(paraName);
                    this.addItem(paras[i]);
                }

            }
        }
    }


    @Override
    public Parameter getSelectedItem() {
        if (super.getSelectedItem() instanceof Parameter) {
            return (Parameter) super.getSelectedItem();
        } else if (super.getSelectedItem() instanceof String && ((String) super.getSelectedItem()).startsWith("$")) {
            return new Parameter(((String) super.getSelectedItem()).substring(1));
        }
        return new Parameter();
    }

    public void setSelectedParameter(Parameter parameter) {
        if (parameter == null) {
            return;
        }
        for (int i = 0; i < this.getItemCount(); i++) {
            if (ComparatorUtils.equals(((Parameter) this.getItemAt(i)).getName(), parameter.getName())) {
                this.setSelectedIndex(i);
                return;
            }
        }
        //august:加入itemlist里面没有,应该加进去,不然就不显示了
        this.addItem(parameter);
        this.setSelectedItem(parameter);
    }
}