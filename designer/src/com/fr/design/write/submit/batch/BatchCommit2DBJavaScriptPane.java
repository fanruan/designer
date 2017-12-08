package com.fr.design.write.submit.batch;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.write.batch.SubmitMain;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by loy on 16/8/22.
 */
public class BatchCommit2DBJavaScriptPane extends FurtherBasicBeanPane<BatchCommit2DBJavaScript> {
    private List dbmPaneList = new ArrayList();
    private BatchCommitTabbedPane commitTabbedPane;
    private JavaScriptActionPane javaScriptActionPane;
    private UIButton addCallbackButton;

    private JPanel cardPane;
    private String[] cardNames;

    /**
     * 构造函数，控件事件的提交入库面板
     */
    public BatchCommit2DBJavaScriptPane() {
        init(null);
    }

    //    public BatchCommit2DBJavaScriptPane(final JavaScriptActionPane javaScriptActionPane, List dbManipulationPaneList) {
    public BatchCommit2DBJavaScriptPane(final JavaScriptActionPane javaScriptActionPane) {
//        this.dbmPaneList=dbManipulationPaneList;
        init(javaScriptActionPane);
    }

    private void init(final JavaScriptActionPane javaScriptActionPane) {
        this.dbmPaneList.add(new BatchSubmitPane());
        this.javaScriptActionPane = javaScriptActionPane;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        commitTabbedPane = new BatchCommitTabbedPane(this, dbmPaneList);
        commitTabbedPane.setPreferredSize(new Dimension(commitTabbedPane.getWidth(), 20));
        this.add(commitTabbedPane, BorderLayout.NORTH);

        cardPane = new JPanel(new CardLayout());
        cardNames = new String[dbmPaneList.size()];
        for (int i = 0; i < this.dbmPaneList.size(); i++) {
            if (((BatchSubmitPane) this.dbmPaneList.get(i)).getSubMitName() == null) {
                cardNames[i] = "";
            } else {
                cardNames[i] = ((BatchSubmitPane) this.dbmPaneList.get(i)).getSubMitName();
            }
            cardPane.add((BatchSubmitPane) this.dbmPaneList.get(i), cardNames[i]);
        }
        this.add(cardPane, BorderLayout.CENTER);

        JPanel btPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        this.add(btPane, BorderLayout.SOUTH);

        if (javaScriptActionPane != null) {
            addCallbackButton = javaScriptActionPane.createCallButton();
            btPane.add(addCallbackButton);
        }
    }


    /**
     * 更新DBManipulationPane
     */
    public void updateCardPane() {
        cardNames = new String[dbmPaneList.size()];
        for (int i = 0; i < this.dbmPaneList.size(); i++) {
            if (((BatchSubmitPane) this.dbmPaneList.get(i)).getSubMitName() == null) {
                cardNames[i] = "";
            } else {
                cardNames[i] = ((BatchSubmitPane) this.dbmPaneList.get(i)).getSubMitName();
            }
            cardPane.add((BatchSubmitPane) this.dbmPaneList.get(i), cardNames[i]);
        }
        CardLayout cardLayout = (CardLayout) cardPane.getLayout();
        cardLayout.show(cardPane, cardNames[commitTabbedPane.getSelectedIndex()]);
    }

    public void setList(List list) {
        this.dbmPaneList = list;
    }

    /**
     * 新建DBManipulationPane
     *
     * @return 新建的DBManipulationPane
     */
    public BatchSubmitPane createDBManipulationPane() {
        BatchSubmitPane db = new BatchSubmitPane();
//        BatchSubmitPane db = javaScriptActionPane.createDBManipulationPane();
        db.populateBean(null);
        dbmPaneList.add(db);
        return db;

    }

    /**
     * 窗口名称
     *
     * @return 返回窗口名称
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Performance-plugin_submitbatch_name");
    }

    /**
     * 界面重置
     */
    public void reset() {
        if (javaScriptActionPane != null) {
            this.javaScriptActionPane.setCall(null);
        }
        //重置后只保留，只留第一个tab
        while (dbmPaneList.size() > 1) {
            dbmPaneList.remove(1);
        }
        ((BatchSubmitPane) dbmPaneList.get(0)).populateBean(null);
    }

    @Override
    /**
     * 将JavaBean内的数据输出至界面上
     */
    public void populateBean(BatchCommit2DBJavaScript commit2db) {
        if (commit2db == null) {
            reset();
            return;
        }
        //先把原来的list清除，然后再根据传入参数重新add
        dbmPaneList.clear();
        if (javaScriptActionPane != null) {
            this.javaScriptActionPane.setCall(commit2db.getCallBack());
        }
        for (int i = 0; i < commit2db.getDBManipulation().size(); i++) {
            BatchSubmitPane dbmp = new BatchSubmitPane();
//            BatchSubmitPane dbmp = javaScriptActionPane.createDBManipulationPane();
            dbmp.populateBean((SubmitMain) commit2db.getDBManipulation().get(i));
            dbmPaneList.add(dbmp);
        }
        commitTabbedPane.refreshTab();
    }

    /**
     * 更新数据层JavaBean
     *
     * @return 返回JavaBean
     */
    public BatchCommit2DBJavaScript updateBean() {
        BatchCommit2DBJavaScript commit2dbJavaScript = new BatchCommit2DBJavaScript();

        List dbmaniList = new ArrayList();
        for (int i = 0; i < this.dbmPaneList.size(); i++) {
            BatchSubmitPane dbmpane = (BatchSubmitPane) this.dbmPaneList.get(i);
            if (i > dbmPaneList.size() - 1) {
                dbmPaneList.add(dbmpane);
            }
            SubmitMain dbManipulation = dbmpane.updateBean();
            dbmaniList.add(dbManipulation);
        }
        commit2dbJavaScript.setDBManipulation(dbmaniList);

        if (javaScriptActionPane != null) {
            commit2dbJavaScript.setCallBack(this.javaScriptActionPane.getCall());
        }

        return commit2dbJavaScript;
    }

    /**
     * 判断是否是能接受的数据类型
     *
     * @param ob 对象
     * @return 返回是否是能接受的数据类型
     */
    public boolean accept(Object ob) {
        return ob instanceof BatchCommit2DBJavaScript;
    }
}
