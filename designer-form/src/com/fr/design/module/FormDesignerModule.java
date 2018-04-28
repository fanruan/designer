package com.fr.design.module;

import com.fr.base.FRContext;
import com.fr.base.io.XMLEncryptUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.bridge.DesignToolbarProvider;
import com.fr.design.form.parameter.FormParaDesigner;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.actions.NewFormAction;
import com.fr.design.parameter.FormParameterReader;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.design.widget.ui.btn.FormSubmitButtonDetailPane;
import com.fr.file.FILE;
import com.fr.form.main.Form;
import com.fr.form.stable.ElementCaseThumbnailProcessor;
import com.fr.general.Inter;
import com.fr.general.ModuleContext;
import com.fr.stable.Constants;
import com.fr.stable.bridge.StableFactory;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA. Author : Richer Version: 6.5.6 Date : 11-11-24 Time
 * : 下午3:43
 */
public class FormDesignerModule extends DesignModule {

    public void start() {
        super.start();
        ModuleContext.startModule(CHART_DESIGNER_MODULE);

        StableFactory.registerMarkedObject(DesignToolbarProvider.STRING_MARKED, WidgetToolBarPane.getInstance());

        DesignModuleFactory.registerNewFormActionClass(NewFormAction.class);
        DesignModuleFactory.registerFormParaDesignerClass(FormParaDesigner.class);
        DesignModuleFactory.registerParaPropertyPaneClass(ParameterPropertyPane.class);
        DesignModuleFactory.registerFormHierarchyPaneClass(FormHierarchyTreePane.class);
        DesignModuleFactory.registerWidgetPropertyPaneClass(WidgetPropertyPane.class);
        DesignModuleFactory.registerButtonDetailPaneClass(FormSubmitButtonDetailPane.class);
        DesignModuleFactory.registerParameterReader(new FormParameterReader());

        registerData4Designer();

        StableFactory.registerMarkedObject(ElementCaseThumbnailProcessor.MARK_STRING, new ElementCaseThumbnail());
    }
    
    private void registerData4Designer(){
        StableFactory.registerMarkedClass(BaseJForm.XML_TAG, JForm.class);
    }

    /**
     *apps模板的Opener
     * @return  返回app
     */
    public App<?>[] apps4TemplateOpener() {
        return new App[]{new AbstractAppProvider<Form>() {

            @Override
            public String[] defaultExtensions() {
                return new String[]{"frm", "form"};
            }

			@Override
            public JTemplate<Form, ?> openTemplate(FILE tplFile) {
    			HashMap<String, Class> classType = new HashMap<String, Class>();
    			classType.put(Constants.ARG_0, Form.class);
    			classType.put(Constants.ARG_1, FILE.class);
            	
            	return (JTemplate<Form, ?>) StableFactory.getMarkedInstanceObjectFromClass(BaseJForm.XML_TAG,
                        new Object[]{asIOFile(tplFile), tplFile}, classType, BaseJForm.class);
            }

            @Override
            public Form asIOFile(FILE file) {
                if (XMLEncryptUtils.isCptEncoded() &&
                        !XMLEncryptUtils.checkVaild(DesignerEnvManager.getEnvManager().getEncryptionKey())) {
                    if (!new DecodeDialog(file).isPwdRight()) {
                        FRContext.getLogger().error(Inter.getLocText("FR-Engine_ECP_error_pwd"));
                        return new Form();
                    }
                }


                // peter:打开新报表.
                Form tpl = new Form();
                // richer:打开报表通知
//				FRContext.getLogger().info(Inter.getLocText("LOG-Is_Being_Openned") + "\"" + file.getName() + "\"" + "," + Inter.getLocText("LOG-Please_Wait") + "...");
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

    public String getInterNationalName() {
        return Inter.getLocText("FR-Designer_formDesignerModule");
    }
}