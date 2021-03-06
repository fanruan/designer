package com.fr.design.report;

import com.fr.data.Verifier;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.VerifyDefineProvider;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ObjectJControlPane;
import com.fr.design.mainframe.ElementCasePane;

import com.fr.general.NameObject;
import com.fr.report.write.ReportWriteAttr;
import com.fr.report.write.ValueVerifier;
import com.fr.report.write.WClassVerifier;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;

import java.util.ArrayList;
import java.util.Set;

/**
 * 校验列表
 */
public class VerifierListPane extends ObjectJControlPane {

    public VerifierListPane(ElementCasePane ePane) {
        super(ePane);
    }

    /**
     * 创建选项
     *
     * @return 选项
     */
    public NameableCreator[] createNameableCreators() {
        NameableCreator[] creators = new NameableCreator[]{
                new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_BuildIn_Verify"),
                        "/com/fr/web/images/reportlet.png",
                        ValueVerifier.class,
                        BuildInVerifierPane.class)
        };
        Set<VerifyDefineProvider> set = ExtraDesignClassManager.getInstance().getArray(VerifyDefineProvider.MARK_STRING);
        for (VerifyDefineProvider provider : set) {
            creators = ArrayUtils.add(creators, new NameObjectCreator(
                    provider.nameForVerifier(),
                    provider.iconPath(),
                    provider.classForVerifier(),
                    provider.appearanceForVerifier()
            ) {
            });
        }
        creators = ArrayUtils.add(creators, new NameObjectCreator(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Custom_Verify"),
                "/com/fr/web/images/reportlet.png",
                WClassVerifier.class,
                CustomVerifierPane.class));
        return creators;
    }

    @Override
    protected String title4PopupWindow() {
        return "Verify Collections";
    }

    public void populate(ReportWriteAttr reportWriteAttr) {
        if (reportWriteAttr == null) {
            return;
        }

        java.util.List<NameObject> nameObjectList = new ArrayList<>();

        int verifierCount = reportWriteAttr.getVerifierCount();
        for (int i = 0; i < verifierCount; i++) {
            Verifier verifier = reportWriteAttr.getVerifier(i);
            nameObjectList.add(new NameObject(verifier.getName(), verifier));
        }

        this.populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));
    }

    public void updateReportWriteAttr(ReportWriteAttr reportWriteAttr) {
        // Nameable[]居然不能强转成NameObject[],一定要这么写...
        Nameable[] res = this.update();
        NameObject[] res_array = new NameObject[res.length];
        java.util.Arrays.asList(res).toArray(res_array);

        reportWriteAttr.clearVerifiers();

        for (int i = 0; i < res_array.length; i++) {
            NameObject nameObject = res_array[i];
            if (nameObject.getObject() instanceof Verifier) {
                Verifier verifier = (Verifier) nameObject.getObject();
                verifier.setName(nameObject.getName());
                reportWriteAttr.addVerifier(verifier);
            }
        }
    }


}