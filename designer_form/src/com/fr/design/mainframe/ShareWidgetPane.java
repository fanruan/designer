package com.fr.design.mainframe;



import com.fr.form.ui.ElCaseBindInfo;
import javax.swing.*;
import java.awt.*;


/**
 * Created by xiaxiang on 2016/10/10.
 */
public class ShareWidgetPane extends JPanel {

    public ShareWidgetPane(ElCaseBindInfo[] elCaseBindInfoList) {
        this.setBorder(BorderFactory.createEmptyBorder(10, 3, 0, 0));// 设置面板的边框 ，距离上、左、下、右 的距离
        int rowCount = (elCaseBindInfoList.length + 1) / 2;
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        for (ElCaseBindInfo rbModuleInfo : elCaseBindInfoList) {
            ShareWidgetButton widgetButton = new ShareWidgetButton(rbModuleInfo);
            this.add(widgetButton);
        }
        this.setPreferredSize(new Dimension(240, rowCount * 80));

    }

}