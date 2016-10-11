package com.fr.design.mainframe;


import com.fr.form.ui.ElCaseBindInfo;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


/**
 * Created by xiaxiang on 2016/10/10.
 */
public class ShareWidgetPane extends JPanel {
    public ShareWidgetPane(ArrayList<ElCaseBindInfo> elCaseBindInfos) {
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));// 设置面板的边框 ，距离上、左、下、右 的距离
        int rowCount = (elCaseBindInfos.size() + 1)/2;
        this.setLayout(new GridLayout(rowCount, 2, 10, 10));
        for (ElCaseBindInfo rbModuleInfo : elCaseBindInfos) {
            ShareWidgetButton widgetButton = new ShareWidgetButton(rbModuleInfo);
            this.add(widgetButton);
        }
        if (elCaseBindInfos.size() == 1) {
            this.add(new JPanel());
        }

    }
}
