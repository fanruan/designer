package com.fr.design.upm;

import com.fr.design.dialog.BasicPane;
import com.fr.design.ui.ModernUIPane;

import java.awt.*;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-12
 * Update Plugin Manager容器
 */
public class UPMPane extends BasicPane {
    @Override
    protected String title4PopupWindow() {
        return "UPM";
    }

    public UPMPane() {
        setLayout(new BorderLayout());
        ModernUIPane<Void> modernUIPane = new ModernUIPane.Builder<Void>()
                .withURL("https://market.fanruan.com")
                .build();
        add(modernUIPane, BorderLayout.CENTER);
    }
}
