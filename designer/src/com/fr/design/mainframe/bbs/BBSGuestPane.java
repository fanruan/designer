/**
 *
 */
package com.fr.design.mainframe.bbs;

import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import com.fr.start.BBSGuestPaneProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

/**
 * @author neil
 * @date: 2015-3-13-下午12:54:45
 */
public class BBSGuestPane extends JPanel implements BBSGuestPaneProvider {

    /**
     * 构造函数
     */
    public BBSGuestPane() {
        this.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
        initTableContent();
    }

    private void initTableContent() {
        JPanel guestPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        //感谢标签
        JPanel infoPane = initInfoPane();
        guestPane.add(infoPane, BorderLayout.NORTH);
        //用户名+超链
        JPanel userPane = initUserPane();
        guestPane.add(userPane, BorderLayout.CENTER);
        //如何加入
        JPanel howToJoin = initHowToJoinPane();
        guestPane.add(howToJoin, BorderLayout.SOUTH);

        this.add(guestPane);
    }

    private JPanel initUserPane() {
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p};
        double[] colSize = {p};

        Component[][] components = new Component[rowSize.length][colSize.length];
        String[] allGuest = BBSConstants.getAllGuest();
        String[] allLink = BBSConstants.getAllLink();
        int min = Math.min(allGuest.length, components.length);
        for (int i = 0; i < min; i++) {
            String userName = allGuest[i];
            String url = allLink[i];
            JPanel sPane = new JPanel(FRGUIPaneFactory.createLeftZeroLayout());
            sPane.add(getURLActionLabel(userName, url));
            components[i][0] = sPane;
        }

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, colSize);
    }

    private JPanel initInfoPane() {
        JPanel infoPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        UILabel infoNorthLabel = new UILabel(Inter.getLocText("FR-Designer_Thank_guest"));
        UILabel centerLabel = new UILabel(StringUtils.BLANK);
        infoPane.add(infoNorthLabel, BorderLayout.NORTH);
        infoPane.add(centerLabel, BorderLayout.CENTER);

        return infoPane;
    }

    /**
     * Border中嵌套Flow布局。Border布局为了空出间隔，
     * Flow布局为了下划线不会超过文字长度
     */
    private JPanel initHowToJoinPane() {
        // 超链文字
        JPanel infoPane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane_First0();
        UILabel infoNorthLabel = getURLActionLabel(Inter.getLocText("FR-Designer_How_To_Join"), BBSConstants.getHowToJoinLink());
        infoPane.add(infoNorthLabel);
        // 空白行
        UILabel centerLabel = new UILabel(StringUtils.BLANK);

        JPanel borderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        borderPane.add(centerLabel, BorderLayout.NORTH);
        borderPane.add(infoPane, BorderLayout.CENTER);
        return borderPane;
    }

    private ActionLabel getURLActionLabel(final String text, final String url) {
        ActionLabel actionLabel = new ActionLabel(text);
        actionLabel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception exp) {

                }
            }
        });

        return actionLabel;
    }
}