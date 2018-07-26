/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.frpane;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.Inter;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.CardLayout;

/**
 * @author richer
 * @since 6.5.5 创建于2011-6-16 会显示正在加载的标志
 * august:改成不确定式
 */
public abstract class LoadingBasicPane extends BasicPane {
    private CardLayout card;
    private JPanel container;
    private JProgressBar progressBar;
    public LoadingBasicPane() {
        initCards();
        initPane();
    }

    protected void initPane(){
        new SwingWorker<Integer, Void>() {

            @Override
            protected Integer doInBackground() throws Exception {
                initComponents(container);
                return 0;
            }

            @Override
            protected void done() {
                complete();
                LayoutUtils.layoutRootContainer(LoadingBasicPane.this);
                card.show(LoadingBasicPane.this, "CONTAINER");
            }

        }.execute();
    }


    private void initCards() {
        card = new CardLayout();
        setLayout(card);
        JPanel loadingDisPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        String[] message = {"Loading","Please-Wait"};
        String[] operator = {",","..."};
        UILabel loadingPane = new UILabel(Inter.getLocText(message,operator) , SwingConstants.CENTER);
        loadingDisPane.add(loadingPane, BorderLayout.CENTER);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        loadingDisPane.add(progressBar, BorderLayout.SOUTH);

        add("LOADING", loadingDisPane);
        container = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        add("CONTAINER", container);
        card.show(this, "LOADING");


    }

    protected void renameConnection(String oldName,String newName){

    }

    protected void initComponents(JPanel container) {

    }

    /**
     * 为了alphafine搜索使用，预加载面板控件，获取全部控件的信息
     * @return
     */
    public JPanel getAllComponents() {
        JPanel panel = new JPanel();
        initComponents(panel);
        return panel;
    }

    /**
     * 完成时
     */
    public void complete() {

    }

}