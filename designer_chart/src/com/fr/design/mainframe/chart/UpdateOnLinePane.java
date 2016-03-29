package com.fr.design.mainframe.chart;

import com.fr.design.ChartEnvManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.actions.ChartDownLoadWorker;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ProductConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 * 在线更新面板
 */
public class UpdateOnLinePane extends BasicPane{
    private static final int GAP = 40;
    private static final int H_GAP = 16;
    private static final int SIDE_GAP =30;
    private static final int RIGHT_BORDER_GAP = 34;
    private static final Color LABEL_COLOR = new Color(114,114,114);
    private static final int MESSAGE_FONT_SIZE = 20;
    private static final int PUSH_FONT_SIZE = 12;
    private static final int PROGRESS_WIDTH = 500;
    private static final int PROGRESS_HEIGHT = 14;
    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("##.##");
    private static final int PRECENT =100;
    private static final Color FOREGROUNG = new Color(23,190,86);
    private static final Color BACKGROUND = new Color(210,210,210);

    String serverVersion = ProductConstants.RELEASE_VERSION;
    UIButton okButton = new UIButton(Inter.getLocText("FR-Chart-Dialog_OK"));
    UIButton updateButton = new UIButton(Inter.getLocText("FR-Chart-App_Update"));
    UIButton cancleButton = new UIButton(Inter.getLocText("FR-Chart-Dialog_Cancle"));
    UICheckBox pushAuto = new UICheckBox(Inter.getLocText("FR-Chart-UpdateMessage_PushAuto"));
    private JPanel messagePane;
    private JPanel optionsPane;
    private BasicDialog parentDialog;
    private ChartDownLoadWorker downLoadWorker = null;
    private boolean isUpdateCancle = false;

    private ActionListener updateListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final JProgressBar progressBar = init4UpdatingPane();
            downLoadWorker = new ChartDownLoadWorker(){
                protected void process(java.util.List<Double> v) {
                    progressBar.setValue((int)(v.get(v.size() - 1) * PRECENT));
                }

                public void done() {
                    try {
                        get();
                    } catch (Exception e1) {
                        init4UpdateFaild();
                        return;
                    }
                    if(!isUpdateCancle){
                        replaceFiles();
                        dialogExit();
                        super.done();
                    }
                }
            };
            downLoadWorker.execute();
        }
    };

    private ActionListener okListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
             dialogExit();
        }
    };

    private ActionListener cancleListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(downLoadWorker !=null){
                isUpdateCancle = true;
                downLoadWorker.cancel(true);
            }
            dialogExit();
        }
    };

    public void setParentDialog(BasicDialog dialog){
       this.parentDialog = dialog;
    }

    public UpdateOnLinePane(String serverVersion){
        this.serverVersion = serverVersion;
        this.isUpdateCancle = false;
        pushAuto.setSelected(ChartEnvManager.getEnvManager().isPushUpdateAuto());
        pushAuto.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ChartEnvManager.getEnvManager().setPushUpdateAuto(pushAuto.isSelected());
            }
        });
        init4PanesLayout();
        initListeners();
        judge();
    }

    private void initListeners(){
        updateButton.addActionListener(updateListener);
        okButton.addActionListener(okListener);
        cancleButton.addActionListener(cancleListener);
    }

    private void init4PanesLayout(){
        this.setLayout(new BorderLayout());
        this.messagePane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.optionsPane = new JPanel(new FlowLayout(FlowLayout.RIGHT,H_GAP ,0)) ;
        this.optionsPane.setBorder(new EmptyBorder(0, 0, GAP, RIGHT_BORDER_GAP));
        this.add(this.messagePane, BorderLayout.CENTER);
        this.add(this.optionsPane, BorderLayout.SOUTH);
        pushAuto.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, PUSH_FONT_SIZE));
        pushAuto.setForeground(LABEL_COLOR);
        this.revalidate();
    }

    //更新失败的提示
    private void init4UpdateFaild(){
        this.messagePane.removeAll();
        UILabel label = new UILabel(Inter.getLocText("FR-Chart-Version_UpdateFail")+"!");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, MESSAGE_FONT_SIZE));
        label.setForeground(LABEL_COLOR);
        this.messagePane.add(label,BorderLayout.CENTER);
        optionsPane.removeAll();
        optionsPane.add(okButton);
        this.revalidate();
    }

    private JProgressBar init4UpdatingPane(){
        this.messagePane.removeAll();
        JPanel centerPane = new JPanel(new GridLayout(2,1));
        UILabel label = new UILabel(Inter.getLocText("FR-Chart-App_UpdateProgress"));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, MESSAGE_FONT_SIZE));
        label.setForeground(LABEL_COLOR);
        label.setBorder(new EmptyBorder(PUSH_FONT_SIZE,0,0,0));
        centerPane.add(label);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setMaximum(PRECENT);
        progressBar.setMinimum(0);
        progressBar.setValue(0);
        progressBar.setBorder(new EmptyBorder(MESSAGE_FONT_SIZE,SIDE_GAP,SIDE_GAP*2,SIDE_GAP));
        centerPane.add(progressBar);
        messagePane.add(centerPane,BorderLayout.CENTER);
        optionsPane.removeAll();
        optionsPane.add(cancleButton);
        this.revalidate();
        return progressBar;
    }

    private void init4VersionSamePane(){
        this.messagePane.removeAll();
        UILabel label = new UILabel(Inter.getLocText("FR-Chart-Versions_Lasted"));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, MESSAGE_FONT_SIZE));
        label.setForeground(LABEL_COLOR);
        this.messagePane.add(label,BorderLayout.CENTER);
        optionsPane.removeAll();
        optionsPane.add(pushAuto);
        optionsPane.add(okButton);
        this.revalidate();
    }

    private void init4VersionDifferentPane(){
        this.messagePane.removeAll();
        createPaneShowVersions();
        optionsPane.removeAll();
        optionsPane.add(pushAuto);
        optionsPane.add(updateButton);
        optionsPane.add(cancleButton);
        this.revalidate();
    }

    private void createPaneShowVersions(){
        JPanel centerPane = new JPanel(new GridLayout(2,1));
        UILabel localLabel = new UILabel(Inter.getLocText("FR-Chart-Version_Local")+":"+ ProductConstants.RELEASE_VERSION);
        localLabel.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, MESSAGE_FONT_SIZE));
        localLabel.setForeground(LABEL_COLOR);
        localLabel.setBorder(new EmptyBorder(PUSH_FONT_SIZE,0,0,0));
        UILabel serverLabel = new UILabel(Inter.getLocText("FR-Chart-Version_Lasted")+":"+serverVersion);
        serverLabel.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, MESSAGE_FONT_SIZE));
        serverLabel.setForeground(LABEL_COLOR);
        serverLabel.setBorder(new EmptyBorder(-MESSAGE_FONT_SIZE - PUSH_FONT_SIZE, 0, 0,0));
        localLabel.setHorizontalAlignment(SwingConstants.CENTER);
        serverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPane.add(localLabel);
        centerPane.add(serverLabel);
        messagePane.add(centerPane,BorderLayout.CENTER);
    }

    private void judge(){
        if(ComparatorUtils.equals(ProductConstants.RELEASE_VERSION,serverVersion)){
            //版本一致,提示已经是最新版本
            init4VersionSamePane();
        }else{
            init4VersionDifferentPane();
        }
    }

    /**
     * Dialog exit.
     */
    private void dialogExit() {
        parentDialog.setVisible(false);
        parentDialog.dispose();
    }




    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Chart-Help_UpdateOnline");
    }

}