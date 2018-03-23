package com.fr.design.webattr.printsettings;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.stable.Constants;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by plough on 2018/3/5.
 */
public class PageOrderSettingPane extends JPanel {
    private UIRadioButton topBottomRadioButton;  // 先列后行
    private UIRadioButton leftRightRadioButton;  // 先行后列

    public PageOrderSettingPane() {
        initComponents();
    }

    private void initComponents() {
        // page order
        JPanel pageOrderPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);

        Icon topBottomIcon = BaseUtils.readIcon("/com/fr/base/images/dialog/pagesetup/down.png");
        topBottomRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Top_to_bottom"));
        pageOrderPane.add(FRGUIPaneFactory.createIconRadio_S_Pane(topBottomIcon, topBottomRadioButton));

        Icon leftRightIcon = BaseUtils.readIcon("/com/fr/base/images/dialog/pagesetup/over.png");
        leftRightRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Left_to_right"));
        pageOrderPane.add(FRGUIPaneFactory.createIconRadio_S_Pane(leftRightIcon, leftRightRadioButton));

        ButtonGroup pageOrderButtonGroup = new ButtonGroup();
        pageOrderButtonGroup.add(topBottomRadioButton);
        pageOrderButtonGroup.add(leftRightRadioButton);

        topBottomRadioButton.setSelected(true);

        this.setLayout(new BorderLayout());
        this.add(pageOrderPane, BorderLayout.CENTER);
    }

    public void populate(int pageOrder) {
        if (pageOrder == Constants.TOP_TO_BOTTOM) {
            topBottomRadioButton.setSelected(true);
        } else {
            leftRightRadioButton.setSelected(true);
        }
    }

    public int updateBean() {
        return topBottomRadioButton.isSelected() ? Constants.TOP_TO_BOTTOM : Constants.LEFT_TO_RIGHT;
    }
}
