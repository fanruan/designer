package com.fr.design.update.ui.dialog;

import com.fr.decision.update.data.UpdateConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.update.factory.DirectoryOperationFactory;
import com.fr.design.update.ui.widget.ColorfulCellRender;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StableUtils;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class RestoreDialog extends JDialog {
    private static final int LISTCELLHEIGHT = 30;
    private static final Dimension RESTOREJAR = new Dimension(523, 480);
    private static final Dimension RESTOREJAR_NORTHPANE = new Dimension(500, 392);
    //一个页面上最少显示13个元素
    private static final int NUMOFCELL_LEAST = 13;

    private UIButton okButton;
    private UIButton cancelButton;
    private JPanel buttonPanel;
    private String jarSelected;

    public RestoreDialog(Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public RestoreDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void initButton() {
        okButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Ok"));
        cancelButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Cancel"));

        okButton.setEnabled(false);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RestoreResultDialog dialog = new RestoreResultDialog(DesignerContext.getDesignerFrame(), true, jarSelected);
                dialog.showDialog();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void initComponents() {

        this.setResizable(false);
        JPanel pane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.setContentPane(pane);

        initButton();

        buttonPanel = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        pane.add(buttonPanel, BorderLayout.SOUTH);

        JPanel jarListPane = new JPanel();
        jarListPane.setLayout(new BoxLayout(jarListPane, BoxLayout.Y_AXIS));
        String[] jarBackupFiles = DirectoryOperationFactory.listFilteredFiles(StableUtils.getInstallHome(), UpdateConstants.DESIGNER_BACKUP_DIR);

        ArrayUtils.reverse(jarBackupFiles);
        String[] jarFilesList = ((jarBackupFiles.length < NUMOFCELL_LEAST) ? Arrays.copyOf(jarBackupFiles, NUMOFCELL_LEAST) : jarBackupFiles);
        final JList jarList = new JList(jarFilesList);
        jarList.setFixedCellHeight(LISTCELLHEIGHT);
        jarList.setCellRenderer(new ColorfulCellRender());
        jarList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                jarSelected = (String) jarList.getSelectedValue();
                okButton.setEnabled((jarSelected != null));
            }
        });

        JScrollPane jsp = new JScrollPane(jarList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setPreferredSize(RESTOREJAR_NORTHPANE);
        pane.add(jsp, BorderLayout.NORTH);

    }

    /**
     * 显示窗口
     */
    public void showDialog() {
        this.setSize(RESTOREJAR);
        this.setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Jar_Restore"));
        GUICoreUtils.centerWindow(this);
        this.setVisible(true);
    }

}
