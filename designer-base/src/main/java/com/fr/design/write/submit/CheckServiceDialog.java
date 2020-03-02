package com.fr.design.write.submit;

import com.fr.design.env.DesignerWorkspaceInfo;
import com.fr.design.env.RemoteDesignerWorkspaceInfo;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;
import com.fr.general.GeneralContext;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author: Maksim
 * @Date: Created in 2020/2/3
 * @Description: 远程连接时，服务检测提醒对话框
 */
public class CheckServiceDialog extends JDialog implements ActionListener {
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private UICheckBox remindBox;
    private DesignerWorkspaceInfo selectEnv;

    public CheckServiceDialog(Frame parent, DesignerWorkspaceInfo selectEnv, String areaText, String localBranch, String remoteBranch){
        super(parent,true);
        this.selectEnv = selectEnv;
        //上面的标签面板
        topPanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        JPanel imagePanel = new JPanel();
        Icon icon = IOUtils.readIcon("com/fr/design/images/warnings/warning5.png");

        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(icon);
        imagePanel.add(imageLabel);
        imagePanel.setPreferredSize(new Dimension(110,80));

        JPanel verticalPanel = FRGUIPaneFactory.createVerticalFlowLayout_S_Pane(true);
        FRFont font = FRFont.getInstance();
        font = font.applySize(15).applyStyle(1);
        JLabel label = new JLabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Branch_Inconsistency"));
        label.setFont(font);
        label.setPreferredSize(new Dimension(600,30));
        JLabel label2 = new JLabel("<html>"+Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Local_Designer")
                + localBranch + "/" + Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Remote_Server") + remoteBranch+"</html>");
        label2.setPreferredSize(new Dimension(600,30));
        JLabel label3 = new JLabel(Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Branch_Need_Update"));
        label3.setPreferredSize(new Dimension(500,30));

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
        remindBox = new UICheckBox(Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Remind_Show"));
        remindBox.addActionListener(remindCheckboxListener);
        UIButton okButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Button_Confirm"));
        JPanel buttonPanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        buttonPanel.add(remindBox, BorderLayout.WEST);
        buttonPanel.add(okButton,BorderLayout.EAST);
        okButton.addActionListener(this );
        bottomPanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        bottomPanel.add(buttonPanel);

        this.setTitle(Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Title_Hint"));
        this.setResizable(false);

        this.add(topPanel,BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(buttonPanel,BorderLayout.SOUTH);
        this.setSize(new Dimension(GeneralContext.getLocale().equals(Locale.US)? 700:600, 500));

        GUICoreUtils.centerWindow(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }


    private ActionListener remindCheckboxListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean needRemind = remindBox.isSelected();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String remindTime = format.format(new Date());
            if(needRemind){
                //前面做过验证，一定是远程环境，直接强转
                ((RemoteDesignerWorkspaceInfo)selectEnv).setRemindTime(remindTime);
            }else {
                ((RemoteDesignerWorkspaceInfo)selectEnv).setRemindTime("");
            }
        }
    };

}
