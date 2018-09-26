package com.fr.design.mainframe.app;

import com.fr.base.io.XMLEncryptUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.AbstractAppProvider;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.DecodeDialog;
import com.fr.design.mainframe.JTemplate;
import com.fr.exception.PermissionDeniedException;
import com.fr.exception.TplLockedException;
import com.fr.file.FILE;
import com.fr.form.main.Form;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.Constants;
import com.fr.stable.bridge.StableFactory;

import java.util.HashMap;

/**
 * Created by juhaoyu on 2018/6/27.
 */
class FormApp extends AbstractAppProvider {

    @Override
    public String[] defaultExtensions() {

        return new String[]{"frm", "form"};
    }

    @Override
    @SuppressWarnings("unchecked")
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
                FineLoggerFactory.getLogger().error(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_ECP_Error_Password"));
                return new Form();
            }
        }

        // peter:打开新报表.
        Form tpl = new Form();
        // richer:打开报表通知
        FineLoggerFactory.getLogger().info(com.fr.design.i18n.Toolkit.i18nText("file.getName()", file.getName()) + "...");
        try {
            tpl.readStream(file.asInputStream());
        } catch (PermissionDeniedException exp) {
            FineLoggerFactory.getLogger().error(Toolkit.i18nText("Fine-Design_Basic_Template_Permission_Denied") + file, exp);
        } catch (TplLockedException exp) {
            FineLoggerFactory.getLogger().error(file + Toolkit.i18nText("Fine-Design_Basic_Template_Status_Locked"), exp);
        } catch (Exception exp) {
            FineLoggerFactory.getLogger().error("Failed to generate frm from " + file, exp);
            return null;
        }
        return tpl;
    }
}
