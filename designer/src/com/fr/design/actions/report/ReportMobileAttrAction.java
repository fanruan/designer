package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.iofileattr.MobileOnlyTemplateAttrMark;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.report.mobile.ReportMobileAttrPane;
import com.fr.file.FILE;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;
import com.fr.plugin.ExtraClassManager;
import com.fr.report.mobile.ElementCaseMobileAttr;
import com.fr.stable.ReportFunctionProcessor;
import com.fr.stable.fun.FunctionProcessor;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * 设置cpt在移动端的一些属性, 包括自适应以及以后可能加展示区域之类的东西.
 *
 * Created by Administrator on 2016/5/12/0012.
 */
public class ReportMobileAttrAction extends JWorkBookAction{

    public ReportMobileAttrAction(JWorkBook jwb) {
        super(jwb);
        this.setMenuKeySet(REPORT_APP_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_report/mobile.png"));
        this.setSearchText(new ReportMobileAttrPane());
    }

    /**
     * 执行动作
     *
     * @return 是否执行成功
     */
    public void actionPerformed(ActionEvent evt) {
        final JWorkBook jwb = getEditingComponent();
        if (jwb == null) {
            return;
        }
        final TemplateWorkBook wbTpl = jwb.getTarget();
        ElementCaseMobileAttr mobileAttr = wbTpl.getReportMobileAttr();

        final ReportMobileAttrPane mobileAttrPane = new ReportMobileAttrPane();
        mobileAttrPane.populateBean(mobileAttr);
        BasicDialog dialog = mobileAttrPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
            @Override
            public void doOk() {
                ElementCaseMobileAttr elementCaseMobileAttr = mobileAttrPane.updateBean();
                try {
                    final TemplateWorkBook workBook = (TemplateWorkBook) wbTpl.clone();

                    wbTpl.setReportMobileAttr(elementCaseMobileAttr);

                    if (elementCaseMobileAttr.isMobileCanvasSize()) {
                        FunctionProcessor processor = ExtraClassManager.getInstance().getFunctionProcessor();
                        if (processor != null) {
                            processor.recordFunction(ReportFunctionProcessor.MOBILE_TEMPLATE_CPT);
                        }
                        MobileOnlyTemplateAttrMark mobileOnlyTemplateAttrMark = wbTpl.getAttrMark(MobileOnlyTemplateAttrMark.XML_TAG);
                        if (mobileOnlyTemplateAttrMark == null) {
                            //如果是新建的模板，选择手机专属之后不需要另存为
                            wbTpl.addAttrMark(new MobileOnlyTemplateAttrMark());
                            FILE editingFILE = jwb.getEditingFILE();
                            if (editingFILE == null || !editingFILE.exists()) {
                                return;
                            }
                            String fileName = editingFILE.getName().substring(0, editingFILE.getName().length() - jwb.suffix().length()) + "_mobile";
                            if (!jwb.saveAsTemplate(true, fileName)) {
                                jwb.setTarget((WorkBook) workBook);
                            }
                        }
                    }
                    jwb.fireTargetModified();
                } catch (CloneNotSupportedException e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
            }
        });
        dialog.setVisible(true);
    }

    private static final MenuKeySet REPORT_APP_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() { return 'P'; }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Mobile-Attr");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}
