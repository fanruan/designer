package com.fr.design.style.background.image;

import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.stable.StringUtils;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.plaf.metal.MetalFileChooserUI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

/**
 * 扩展的文件选择框(底部控制区域扩展一个复选框)
 * Created by zack on 2018/3/8.
 */
public class ExpandFileChooser extends JFileChooser {
    private JDialog dialog;
    private UICheckBox checkBox;//选择框底部的复选按钮
    private int retVal = ERROR_OPTION;
    private UIButton approve;
    private UIButton cancel;
    private static final int DEFAULT_WIDTH = 520;

    public ExpandFileChooser() {
        this(StringUtils.EMPTY, StringUtils.EMPTY);
    }

    public ExpandFileChooser(String checkBoxText, String approveButtonText) {
        JPanel previewContainerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        MetalFileChooserUI chooserUI = (MetalFileChooserUI) getUI();
        String approveText = StringUtils.isEmpty(approveButtonText) ? chooserUI.getApproveButtonText(this) : approveButtonText;
        dialog = new JDialog();

        dialog.setSize(new Dimension(DEFAULT_WIDTH, 362));
        dialog.add(previewContainerPane);
        dialog.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/logo.png"));
        dialog.setTitle(approveText);
        previewContainerPane.add(this, BorderLayout.CENTER);


        JPanel bottomControlPanel = new JPanel();
        bottomControlPanel.setLayout(new ImageAreaLayout());
        bottomControlPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, 40));

        approve = new UIButton(approveText);
        cancel = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Button_Cancel"));
        if (StringUtils.isNotEmpty(checkBoxText)) {
            checkBox = new UICheckBox(checkBoxText);
            checkBox.setSelected(DesignerEnvManager.getEnvManager().isImageCompress());
            bottomControlPanel.add(checkBox);
            checkBox.addActionListener(checkAction());
        }
        bottomControlPanel.add(approve);
        approve.addActionListener(chooserUI.getApproveSelectionAction());
        cancel.addActionListener(chooserUI.getCancelSelectionAction());
        bottomControlPanel.add(cancel);
        previewContainerPane.add(bottomControlPanel, BorderLayout.SOUTH);
        GUICoreUtils.centerWindow(dialog);
    }

    public ActionListener checkAction() {
        return null;
    }

    public boolean isCheckSelected() {
        if (checkBox != null) {
            return checkBox.isSelected();
        }
        return false;
    }


    @Override
    public int showDialog(Component parent, String approveButtonText) {
        dialog.setModal(true);
        dialog.setVisible(true);
        return retVal;
    }

    @Override
    public void approveSelection() {
        retVal = APPROVE_OPTION;
        if (dialog != null) {
            dialog.setVisible(false);
        }
        fireActionPerformed(APPROVE_SELECTION);
    }

    @Override
    public void cancelSelection() {
        retVal = CANCEL_OPTION;
        if (dialog != null) {
            dialog.setVisible(false);
        }
        fireActionPerformed(CANCEL_SELECTION);
    }

    @Override
    public boolean getControlButtonsAreShown() {
        return false;//隐藏默认的控制按钮(打开/取消)
    }

    private class ImageAreaLayout implements LayoutManager {
        private static final int TEN = 10;
        private int hGap = 5;
        private int topMargin = TEN;
        private int leftMargin = TEN;
        private int leftStart = 8;

        @Override
        public void addLayoutComponent(String string, Component comp) {
        }

        @Override
        public void layoutContainer(Container container) {
            Component[] children = container.getComponents();

            if (children != null && children.length > 0) {
                int numChildren = children.length;
                Dimension[] sizes = new Dimension[numChildren];
                Insets insets = container.getInsets();
                int yLocation = insets.top + topMargin;
                int maxWidth = 0;

                for (int counter = 0; counter < numChildren; counter++) {
                    sizes[counter] = children[counter].getPreferredSize();
                    maxWidth = Math.max(maxWidth, sizes[counter].width);
                }
                int xLocation, xOffset;
                if (container.getComponentOrientation().isLeftToRight()) {
                    xLocation = container.getSize().width - insets.left - maxWidth - leftMargin;
                    xOffset = hGap + maxWidth;
                } else {
                    xLocation = insets.left;
                    xOffset = -(hGap + maxWidth);
                }
                //单独设置图片压缩按钮的位置
                children[0].setBounds(leftStart, yLocation,
                        maxWidth, sizes[0].height);

                for (int counter = numChildren - 1; counter >= 1; counter--) {
                    children[counter].setBounds(xLocation, yLocation,
                            maxWidth, sizes[counter].height);
                    xLocation -= xOffset;
                }
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container c) {
            if (c != null) {
                Component[] children = c.getComponents();

                if (children != null && children.length > 0) {
                    int numChildren = children.length;
                    int height = 0;
                    Insets cInsets = c.getInsets();
                    int extraHeight = topMargin + cInsets.top + cInsets.bottom;
                    int extraWidth = cInsets.left + cInsets.right;
                    int maxWidth = 0;

                    for (int counter = 0; counter < numChildren; counter++) {
                        Dimension aSize = children[counter].getPreferredSize();
                        height = Math.max(height, aSize.height);
                        maxWidth = Math.max(maxWidth, aSize.width);
                    }
                    return new Dimension(extraWidth + numChildren * maxWidth +
                            (numChildren - 1) * hGap,
                            extraHeight + height);
                }
            }
            return new Dimension(0, 0);
        }

        @Override
        public Dimension preferredLayoutSize(Container c) {
            return minimumLayoutSize(c);
        }

        @Override
        public void removeLayoutComponent(Component c) {
        }
    }
}
