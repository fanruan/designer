package com.fr.design.report.fit.menupane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.report.fit.ReportFitAttr;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

/**
 * Created by Administrator on 2015/7/6 0006.
 */
public class ReportFitAttrPane extends BasicBeanPane<ReportFitAttr> {

    private BrowserFitAttrPane attrPane;


    public ReportFitAttrPane() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        attrPane = new BrowserFitAttrPane();
        this.add(attrPane);

    }

    /**
     * 展示界面
     *
     * @param fitAttr 自适应属性
     */
    public void populateBean(ReportFitAttr fitAttr) {
        attrPane.populateBean(fitAttr);
    }

    /**
     * 提交数据
     *
     * @return 界面上的更新数据
     */
    public ReportFitAttr updateBean() {
        return attrPane.updateBean();
    }

    /**
     * 标题
     *
     * @return 标题
     */
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_PC_Element_Case_Fit_Attr");
    }

}
