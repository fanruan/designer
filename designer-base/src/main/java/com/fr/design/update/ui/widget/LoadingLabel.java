package com.fr.design.update.ui.widget;

import com.fr.design.gui.ilable.UILabel;
import com.fr.general.IOUtils;

import javax.swing.Icon;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class LoadingLabel extends UILabel {
    private Icon[] busyIcons = new Icon[15];
    private Timer busyIconTimer;
    private int busyIconIndex;

    public LoadingLabel() {
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = IOUtils.readIcon("/com/fr/design/images/update/busy-icon" + i + ".png");
        }
        int busyAnimationRate = 30;

        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                setIcon(busyIcons[busyIconIndex]);
                setHorizontalAlignment(SwingConstants.CENTER);
            }
        });
        busyIconTimer.start();
    }

    /**
     * 停止加载，并显示
     *
     * @param text 要显示的字段
     */
    public void stopLoading(String text) {
        busyIconTimer.stop();
        setIcon(null);
        setText(text);
    }

}