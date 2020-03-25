package com.fr.design.hyperlink;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.parameter.ParameterReader;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.js.ReportletHyperlink;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;

import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;

public class ReportletHyperlinkPane extends AbstractHyperLinkPane<ReportletHyperlink> {
    /**
     * 超链配置面板
     */
    private ReportletHyperNorthPane northPane;
    /**
     * 是否继承参数勾选框
     */
    private UICheckBox extendParametersCheckBox;

    public ReportletHyperlinkPane(HashMap hyperLinkEditorMap, boolean needRenamePane) {
        super(hyperLinkEditorMap, needRenamePane);
        this.initComponents();
    }

    public ReportletHyperlinkPane() {
        super();
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        northPane = new ReportletHyperNorthPane(needRenamePane());
        this.add(northPane, BorderLayout.NORTH);

        parameterViewPane = new ReportletParameterViewPane(
                new UITableEditAction[]{
                        new HyperlinkParametersAction()
                },
                getChartParaType(),
                getValueEditorPane(),
                getValueEditorPane()
        );

        this.add(parameterViewPane, BorderLayout.CENTER);

        parameterViewPane.setBorder(GUICoreUtils.createTitledBorder(Toolkit.i18nText("Fine-Design_Basic_Parameter"), null));

        extendParametersCheckBox = new UICheckBox(Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Extends_Report_Parameters"));
        this.add(GUICoreUtils.createFlowPane(extendParametersCheckBox, FlowLayout.LEFT), BorderLayout.SOUTH);
    }

    @Override
    public String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Reportlet");
    }

    @Override
    public void populateBean(ReportletHyperlink ob) {
        northPane.populateBean(ob);
        // parameter
        List<ParameterProvider> parameterList = this.parameterViewPane.update();
        parameterList.clear();

        ParameterProvider[] parameters = ob.getParameters();
        parameterViewPane.populate(parameters);

        extendParametersCheckBox.setSelected(ob.isExtendParameters());
    }

    @Override
    public ReportletHyperlink updateBean() {
        ReportletHyperlink reportletHyperlink = new ReportletHyperlink();
        updateBean(reportletHyperlink);

        return reportletHyperlink;
    }

    @Override
    public void updateBean(ReportletHyperlink reportletHyperlink) {
        northPane.updateBean(reportletHyperlink);
        // Parameter.
        List<ParameterProvider> parameterList = this.parameterViewPane.update();
        if (!parameterList.isEmpty()) {
            Parameter[] parameters = new Parameter[parameterList.size()];
            parameterList.toArray(parameters);

            reportletHyperlink.setParameters(parameters);
        } else {
            reportletHyperlink.setParameters(null);
        }

        reportletHyperlink.setExtendParameters(extendParametersCheckBox.isSelected());

    }

    /**
     * 自动添加模板参数的按钮操作
     */
    protected class HyperlinkParametersAction extends UITableEditAction {
        public HyperlinkParametersAction() {
            this.setName(Toolkit.i18nText("Fine-Design_Basic_Template_Parameter"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/p.gif"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String tpl = northPane.getReportletName();
            if (StringUtils.isBlank(tpl)) {
                FineJOptionPane.showMessageDialog(ReportletHyperlinkPane.this, Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Please_Select_Reportlet") + ".", Toolkit.i18nText("Fine-Design_Basic_Alert"), JOptionPane.WARNING_MESSAGE);
                northPane.requestButtonFocus();
                return;
            }

            //根据模板路径返回参数
            //与当前模块、当前文件无关
            Parameter[] parameters = new Parameter[0];

            ParameterReader[] readers = DesignModuleFactory.getParameterReaders();
            for (ParameterReader reader : readers) {
                Parameter[] ps = reader.readParameterFromPath(tpl);
                if (ps != null) {
                    parameters = ps;
                }
            }
            parameterViewPane.populate(parameters);
        }

        @Override
        public void checkEnabled() {
            //do nothing
        }
    }

    public static class ChartNoRename extends ReportletHyperlinkPane {
        @Override
        protected int getChartParaType() {
            return ParameterTableModel.CHART_NORMAL_USE;
        }

        @Override
        protected boolean needRenamePane() {
            return false;
        }
    }

    public ReportletHyperNorthPane getNorthPane() {
        return northPane;
    }

    public void setNorthPane(ReportletHyperNorthPane northPane) {
        this.northPane = northPane;
    }

    public UICheckBox getExtendParametersCheckBox() {
        return extendParametersCheckBox;
    }

    public void setExtendParametersCheckBox(UICheckBox extendParametersCheckBox) {
        this.extendParametersCheckBox = extendParametersCheckBox;
    }
}
