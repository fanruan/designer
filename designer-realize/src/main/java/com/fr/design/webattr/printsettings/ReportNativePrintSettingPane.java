package com.fr.design.webattr.printsettings;

import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Component;

/**
 * 本地打印设置面板——单模版
 * Created by plough on 2018/10/31.
 */
public class ReportNativePrintSettingPane extends AbstractNativePrintSettingPane {
    @Override
    JPanel createHeaderPane(Component... comps) {
        JPanel headerPane = GUICoreUtils.createHeaderLayoutPane(comps);
        headerPane.setBorder(BorderFactory.createEmptyBorder(2, 12, 12, 0));
        return headerPane;
    }
}
