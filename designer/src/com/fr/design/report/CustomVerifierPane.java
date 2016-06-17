package com.fr.design.report;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.report.write.WClassVerifier;

import java.awt.*;

/**
 * Created by richie on 16/6/12.
 */
public class CustomVerifierPane extends BasicBeanPane<WClassVerifier> {
    private CustomVerifyJobPane pane;

    public CustomVerifierPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        pane = new CustomVerifyJobPane();
        this.add(pane, BorderLayout.CENTER);
    }

    @Override
    public void populateBean(WClassVerifier ob) {
        this.pane.populateBean(ob.getClassVerifyJob());
    }

    @Override
    public WClassVerifier updateBean() {
        WClassVerifier verifier = new WClassVerifier();
        verifier.setClassVerifyJob(this.pane.updateBean());
        return verifier;
    }

    @Override
    protected String title4PopupWindow() {
        return "custom";
    }
}
