/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.module;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.io.XMLEncryptUtils;
import com.fr.chart.base.ChartInternationalNameContentBean;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.chart.module.ChartModule;
import com.fr.design.DesignerEnvManager;
import com.fr.design.chart.gui.ChartWidgetOption;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.mainframe.*;
import com.fr.file.FILE;
import com.fr.form.ui.ChartBook;
import com.fr.form.ui.ChartEditor;
import com.fr.general.Inter;
import com.fr.general.ModuleContext;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-13
 * Time: 下午2:56
 */
public class ChartStartModule extends ChartDesignerModule {

    protected void dealBeforeRegister(){
        ModuleContext.startModule(ChartModule.class.getName());
    }

    protected void registerFloatEditor() {

    }

    protected WidgetOption[] options4Show() {
   		ChartInternationalNameContentBean[] typeName = ChartTypeManager.getInstance().getAllChartBaseNames();
   		ChartWidgetOption[] child = new ChartWidgetOption[typeName.length];
   		for (int i = 0; i < typeName.length; i++) {
   			Chart[] rowChart = ChartTypeManager.getInstance().getChartTypes(typeName[i].getPlotID());
   			child[i] = new ChartWidgetOption(Inter.getLocText(typeName[i].getName()), BaseUtils
   					.readIcon("com/fr/design/images/form/toolbar/" + typeName[i].getName() + ".png"),
   					ChartEditor.class, rowChart[0]);
   		}
   		return child;
   	}

    /**
     * 应用打开器
     * @return 应用
     */
    public App<?>[] apps4TemplateOpener() {
        return new App[]{new AbstractAppProvider<ChartBook>() {

            @Override
            public String[] defaultExtentions() {
                return new String[]{"crt"};
            }

            @Override
            public JTemplate<ChartBook, ?> openTemplate(FILE tplFile) {
                return new JChart(asIOFile(tplFile), tplFile);
            }

            @Override
            public ChartBook asIOFile(FILE file) {
                if (XMLEncryptUtils.isCptEncoded() &&
                        !XMLEncryptUtils.checkVaild(DesignerEnvManager.getEnvManager().getEncryptionKey())) {
                    if (!new DecodeDialog(file).isPwdRight()) {
                        FRContext.getLogger().error(Inter.getLocText("FR-Chart-Password_Error"));
                        return new ChartBook();
                    }
                }


                ChartBook tpl = new ChartBook();
                //打开通知
                FRContext.getLogger().info(Inter.getLocText(new String[]{"LOG-Is_Being_Openned", "LOG-Please_Wait"},
                        new String[]{"\"" + file.getName() + "\"" + ",", "..."}));
                try {
                    tpl.readStream(file.asInputStream());
                } catch (Exception exp) {
                    FRContext.getLogger().error("Failed to generate frm from " + file, exp);
                    return null;
                }
                return tpl;
            }
        }};
    }
}