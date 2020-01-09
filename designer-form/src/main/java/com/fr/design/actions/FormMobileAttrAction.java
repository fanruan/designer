package com.fr.design.actions;

import com.fr.base.BaseUtils;
import com.fr.base.iofile.attr.MobileOnlyTemplateAttrMark;
import com.fr.design.actions.JTemplateAction;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteBodyLayout;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.form.mobile.FormMobileAttrPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormArea;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.JForm;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.FILE;
import com.fr.form.main.Form;
import com.fr.form.main.mobile.FormMobileAttr;
import com.fr.intelli.record.Focus;
import com.fr.intelli.record.Original;
import com.fr.record.analyzer.EnableMetrics;


import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by fanglei on 2016/11/14.
 */
@EnableMetrics
public class FormMobileAttrAction extends JTemplateAction<JForm> {

    public FormMobileAttrAction(JForm jf) {
        super(jf);
        this.setMenuKeySet(REPORT_APP_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/mobile.png"));
    }

    /**
     * 执行动作
     *
     * @return 是否执行成功
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        final JForm jf = getEditingComponent();
        if (jf == null) {
            return;
        }
        final Form formTpl = jf.getTarget();
        FormMobileAttr mobileAttr = formTpl.getFormMobileAttr();

        final FormMobileAttrPane mobileAttrPane = new FormMobileAttrPane();
        mobileAttrPane.populateBean(mobileAttr);
        BasicDialog dialog = mobileAttrPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
            @Override
            public void doOk() {
                FormMobileAttr formMobileAttr = mobileAttrPane.updateBean();
                if (formMobileAttr.isMobileOnly() && jf.getTarget().getAttrMark(MobileOnlyTemplateAttrMark.XML_TAG) == null) {
                    // 如果是老模板，选择手机专属之后需要另存为
                    FILE editingFILE = jf.getEditingFILE();
                    if (editingFILE != null && editingFILE.exists()) {
                        String fileName = editingFILE.getName().substring(0, editingFILE.getName().length() - jf.suffix().length()) + "_mobile";
                        if (!jf.saveAsTemplate(true, fileName)) {
                            return;
                        }
                    }
                    // 放到后面。如果提前 return 了，则仍然处于未设置状态，不要添加
                    jf.getTarget().addAttrMark(new MobileOnlyTemplateAttrMark());
                }
                recordFunction();
                // 设置移动端属性并刷新界面
                formTpl.setFormMobileAttr(formMobileAttr);  // 会调整 body 的自适应布局，放到最后
                ((FormArea)jf.getFormDesign().getParent()).onMobileAttrModified();
                jf.getFormDesign().getSelectionModel().setSelectedCreator(jf.getFormDesign().getRootComponent());
                //当自适应属性自动匹配处于勾选状态 进行切换
                if (formMobileAttr.isAdaptivePropertyAutoMatch()) {
                    doChangeBodyLayout();
                }

                WidgetPropertyPane.getInstance().refreshDockingView();
                jf.fireTargetModified();
            }
        });
        dialog.setVisible(true);
    }

    private void doChangeBodyLayout(){
        FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        XLayoutContainer rootLayout = formDesigner.getRootComponent();
        if (rootLayout.getComponentCount() == 1 && rootLayout.getXCreator(0).acceptType(XWAbsoluteBodyLayout.class)) {
            rootLayout = (XWAbsoluteBodyLayout) rootLayout.getXCreator(0);
        }
        ((XWFitLayout)formDesigner.getRootComponent()).switch2FitBodyLayout(rootLayout);
    }

    @Focus(id = "com.fr.mobile.mobile_template_frm", text = "Fine-Design_Function_Mobile_Template_Frm", source = Original.EMBED)
    private void recordFunction() {
        // do nothing
    }

    private static final MenuKeySet REPORT_APP_ATTR = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

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
