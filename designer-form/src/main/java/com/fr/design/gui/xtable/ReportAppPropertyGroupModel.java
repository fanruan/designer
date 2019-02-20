package com.fr.design.gui.xtable;

import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.mainframe.FormDesigner;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.report.stable.FormConstants;

import javax.swing.JOptionPane;
import java.lang.reflect.Method;

public class ReportAppPropertyGroupModel extends PropertyGroupModel {

    private static final double MAX_HEIGHT = 0.8;

    public ReportAppPropertyGroupModel(String name, XCreator creator, CRPropertyDescriptor[] propArray,
                                       FormDesigner designer) {
        super(name, creator, propArray, designer);
    }

	@Override
	public boolean setValue(Object value, int row, int column) {
		double state = 0;
        if (column == 0) {
            return false;
        }
        if (value instanceof Double) {
        	state = (Double) value;
        }

        try {
            Method m = properties[row].getWriteMethod();
            if (state > MAX_HEIGHT) {
            	//弹窗提示
                JOptionPane.showMessageDialog(null,
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Mobile_Warning"),
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tool_Tips"),
                        JOptionPane.PLAIN_MESSAGE);
                return false;
            }
            m.invoke(dealCreatorData(), value);
            //属性名称为控件名时，单独处理下
            if(ComparatorUtils.equals(FormConstants.NAME, properties[row].getName())){
                creator.resetCreatorName(value.toString());
            }
            properties[row].firePropertyChanged();
            return true;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    private Object dealCreatorData() {
        return creator.getPropertyDescriptorCreator().toData();
    }
}
