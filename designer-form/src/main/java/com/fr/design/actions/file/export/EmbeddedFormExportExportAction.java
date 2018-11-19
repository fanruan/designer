package com.fr.design.actions.file.export;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JForm;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.form.main.Form;
import com.fr.io.exporter.DesignExportType;
import com.fr.stable.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

import static com.fr.io.exporter.DesignExportScope.FINE_FORM;

/**
 * Export Embedded.
 */
public class EmbeddedFormExportExportAction extends AbstractExportAction<JForm> {


    public EmbeddedFormExportExportAction(JForm jwb) {
        super(jwb);
        this.setMenuKeySet(KeySetUtils.EMBEDDED_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/oem/logo.png"));
    }

    @Override
    public String exportScopeName() {
        return FINE_FORM.toString();
    }

    @Override
    protected String getDefaultExtension() {
        return getEditingComponent().suffix().substring(1);
    }

    @Override
    public DesignExportType exportType() {
        return DesignExportType.EMBEDDED_FORM;
    }

    @Override
    protected Map<String, Object> processParameter() {
        // 输入参数
        final Map<String, Object> parameterMap = new HashMap<String, Object>();
        Form tpl = this.getEditingComponent().getTarget();

        Parameter[] parameters = tpl.getParameters();
        // 检查Parameter.
        if (ArrayUtils.isNotEmpty(parameters)) {
            final ParameterInputPane pPane = new ParameterInputPane(parameters);
            pPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                @Override
                public void doOk() {
                    parameterMap.putAll(pPane.update());
                }
            }).setVisible(true);
        }
        return parameterMap;
    }


    @Override
    protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(new String[]{"frm"}, Toolkit.i18nText("Fine-Design_Form_EmbeddedTD"));
    }
}
