package com.fr.design.actions.report;

import com.fr.base.PaperSize;
import com.fr.base.iofile.attr.MobileOnlyTemplateAttrMark;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.report.mobile.ReportMobileAttrPane;
import com.fr.file.FILE;
import com.fr.general.IOUtils;

import com.fr.intelli.record.Focus;
import com.fr.main.TemplateWorkBook;
import com.fr.page.PaperSettingProvider;
import com.fr.record.analyzer.EnableMetrics;
import com.fr.report.mobile.ElementCaseMobileAttr;
import com.fr.report.report.Report;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * 设置cpt在移动端的一些属性, 包括自适应以及以后可能加展示区域之类的东西.
 *
 * Created by Administrator on 2016/5/12/0012.
 */
@EnableMetrics
public class ReportMobileAttrAction extends JWorkBookAction{

    public ReportMobileAttrAction(JWorkBook jwb) {
        super(jwb);
        this.setMenuKeySet(REPORT_APP_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_report/mobile.png"));
        this.generateAndSetSearchText(ReportMobileAttrPane.class.getName());
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
        final boolean oldMobileCanvasSize = mobileAttr.isMobileCanvasSize();
        BasicDialog dialog = mobileAttrPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
            @Override
            public void doOk() {
                ElementCaseMobileAttr elementCaseMobileAttr = mobileAttrPane.updateBean();
                if (elementCaseMobileAttr.isMobileCanvasSize() && wbTpl.getAttrMark(MobileOnlyTemplateAttrMark.XML_TAG) == null) {
                    // 如果是老模板，选择手机专属之后需要另存为
                    FILE editingFILE = jwb.getEditingFILE();
                    if (editingFILE != null && editingFILE.exists()) {
                        String fileName = editingFILE.getName().substring(0, editingFILE.getName().length() - jwb.suffix().length()) + "_mobile";
                        if (!jwb.saveAsTemplate(true, fileName)) {
                            return;  // 不激活保存按钮
                        }
                    }
                    // 放到后面。如果提前 return 了，则仍然处于未设置状态，不要添加
                    wbTpl.addAttrMark(new MobileOnlyTemplateAttrMark());
                }
                recordFunction();
                // 设置移动端属性并刷新界面
                wbTpl.setReportMobileAttr(elementCaseMobileAttr);
                boolean change = !oldMobileCanvasSize && elementCaseMobileAttr.isMobileCanvasSize();
                if (change) {
                    // 当相关属性从未勾选到勾选状态时 设置成移动端标准页面大小
                    for (int i = 0; i < wbTpl.getReportCount(); i++) {
                        Report report = wbTpl.getReport(i);
                        PaperSettingProvider paperSetting = report.getReportSettings().getPaperSetting();
                        paperSetting.setPaperSize(PaperSize.PAPERSIZE_MOBILE);
                    }
                }
                jwb.fireTargetModified();
            }
        });
        dialog.setVisible(true);
    }

    @Focus(id = "com.fr.mobile.mobile_template_cpt", text = "Fine-Design_Function_Mobile_Template_Cpt")
    private void recordFunction() {
        // do nothing
    }

    private static final MenuKeySet REPORT_APP_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() { return 'P'; }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Attr");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}
