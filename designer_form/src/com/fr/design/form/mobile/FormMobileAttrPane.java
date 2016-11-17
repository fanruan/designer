package com.fr.design.form.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.report.mobile.ElementCaseMobileAttr;

import javax.swing.*;
import java.awt.*;

/**
 * Created by fanglei on 2016/11/17.
 */
public class FormMobileAttrPane extends BasicBeanPane<ElementCaseMobileAttr>{
    //工具栏容器
    private MobileToolBarPane mobileToolBarPane;

    public FormMobileAttrPane() {
        this.initComponents();
    }

    private void initComponents() {
        JPanel jPanel = new JPanel();
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.mobileToolBarPane = new MobileToolBarPane();
        //设置一个JPanel包裹mobileToolBarPane这个Panel，让jPanel的高度等于mobileToolBarPane高度加10，再放入this中
        jPanel.setPreferredSize(new Dimension(0, (int)this.mobileToolBarPane.getPreferredSize().getHeight() + 10));
        jPanel.add("North", mobileToolBarPane);
        this.add(jPanel);
    }

    @Override
    public void populateBean(ElementCaseMobileAttr ob) {
        if (ob == null) {
            ob = new ElementCaseMobileAttr();
        }
        this.mobileToolBarPane.populateBean(ob);
    }

    @Override
    public ElementCaseMobileAttr updateBean() {
        ElementCaseMobileAttr caseMobileAttr = new ElementCaseMobileAttr();
        this.mobileToolBarPane.updateBean(caseMobileAttr);
        return caseMobileAttr;
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Mobile-Attr");
    }
}
