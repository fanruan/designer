package com.fr.design.parameter;

import com.fr.base.Parameter;
import com.fr.base.ParameterConfig;
import com.fr.design.dialog.BasicPane;
import com.fr.design.layout.FRGUIPaneFactory;


import java.awt.*;


public class ParameterManagerPane extends BasicPane {
    private ParameterArrayPane parameterArrayPane;

    public ParameterManagerPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        parameterArrayPane = new ParameterArrayPane();
        this.add(parameterArrayPane, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_M_Server_Global_Parameters");
    }
    
    public void populate(Parameter[] parameters) {
        //todo 原来界面上显示的xml路径
//        this.parameterTextField.setText(WorkContext.getCurrent().getPath() + File.separator +
//                ProjectConstants.RESOURCES_NAME +
//                File.separator + configManager.fileName());
        this.parameterArrayPane.populate(parameters);
    }
    
    public void update() {
        
        ParameterConfig.getInstance().setGlobeParameters(parameterArrayPane.updateParameters());
    }

    /**
     * 是否重命名
     *
     * @return 是则返回true
     */
    public boolean isContainsRename() {
        return parameterArrayPane.isContainsRename() || parameterArrayPane.isNameRepeated();
    }
}