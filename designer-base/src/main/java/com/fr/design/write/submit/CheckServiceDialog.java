package com.fr.design.write.submit;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;
import com.fr.general.IOUtils;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author: Maksim
 * @Date: Created in 2020/2/3
 * @Description: 远程连接时，服务检测提醒对话框
 */
public class CheckServiceDialog extends JDialog implements ActionListener {
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;

    public CheckServiceDialog(Frame parent, String areaText, String localBranch, String remoteBranch){
        super(parent,true);
        //上面的标签面板
        topPanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        JPanel imagePanel = new JPanel();
        Icon icon = IOUtils.readIcon("com/fr/design/images/warnings/warning4.png");

        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(icon);
        imagePanel.add(imageLabel);
        imagePanel.setPreferredSize(new Dimension(100,80));

        JPanel verticalPanel = FRGUIPaneFactory.createVerticalFlowLayout_S_Pane(true);
        FRFont font = FRFont.getInstance();
        font = font.applySize(15).applyStyle(1);
        JLabel label = new JLabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Branch_Inconsistency"));
        label.setFont(font);
        label.setPreferredSize(new Dimension(500,30));
        JLabel label2 = new JLabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Local_Designer")
                + localBranch + "/" + Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Remote_Server") + remoteBranch);
        label2.setPreferredSize(new Dimension(500,20));
        JLabel label3 = new JLabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Branch_Need_Update"));
        label3.setPreferredSize(new Dimension(500,20));

        verticalPanel.add(label);
        verticalPanel.add(label2);
        verticalPanel.add(label3);

        topPanel.add(imagePanel,BorderLayout.WEST);
        topPanel.add(verticalPanel,BorderLayout.CENTER);

        //中间的文本域面板
        centerPanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        centerPanel.setPreferredSize(new Dimension(480,320));

        JLabel titleLabel = new JLabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Affected_Function"));
        titleLabel.setPreferredSize(new Dimension(400,40));
        JTextArea checkArea = new JTextArea(areaText);
        checkArea.setEnabled(false);
        centerPanel.add(titleLabel,BorderLayout.NORTH);
        centerPanel.add(checkArea,BorderLayout.CENTER);

        //下面的按钮面板
        UIButton okButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Button_Confirm"));
        JPanel buttonPanel = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
        buttonPanel.add(okButton);
        okButton.addActionListener(this );
        bottomPanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        bottomPanel.add(buttonPanel);

        this.setTitle(Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Title_Hint"));
        this.setResizable(false);

        this.add(topPanel,BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(buttonPanel,BorderLayout.SOUTH);
        this.setSize(new Dimension(600, 500));

        GUICoreUtils.centerWindow(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }
}
