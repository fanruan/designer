/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import com.fr.base.iofile.attr.DesignBanCopyAttrMark;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.base.mode.DesignerMode;
import com.fr.design.dialog.BasicPane;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;

import java.awt.BorderLayout;


/**
 * Created by IntelliJ IDEA. User : Richer Version: 6.5.5 Date : 11-7-21 Time :
 * 下午5:09
 */
public class DesktopCardPane extends BasicPane implements TargetModifiedListener {

    private static final long serialVersionUID = 1L;
    private JTemplate<?, ?> component;

    protected DesktopCardPane() {
        setLayout(new BorderLayout());
    }

    protected void showJTemplate(final JTemplate<?, ?> jt) {
        // 判断是否切换设计器状态到禁止拷贝剪切
        if (jt.getTarget().getAttrMark(DesignBanCopyAttrMark.XML_TAG) != null) {
            DesignModeContext.switchTo(DesignerMode.BAN_COPY_AND_CUT);
        } else if (!DesignModeContext.isVcsMode()){
            DesignModeContext.switchTo(DesignerMode.NORMAL);
        }
        DesignerFrameFileDealerPane.getInstance().setCurrentEditingTemplate(jt);
        if (component != null) {
            component.onLostFocus();
            remove(component);
        }
        add(component = jt, BorderLayout.CENTER);
        validate();
        repaint();
        revalidate();
        component.requestGridFocus();
        component.onGetFocus();
    }

    protected JTemplate<?, ?> getSelectedJTemplate() {
        return component;
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Desktop");
    }

    @Override
    public void targetModified(TargetModifiedEvent e) {
    }
}