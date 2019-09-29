package com.fr.design.javascript;

import com.fr.design.write.submit.DBManipulationPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.CommitTabbedPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;

import com.fr.js.Commit2DBJavaScript;
import com.fr.write.DBManipulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Commit2DBJavaScriptPane extends FurtherBasicBeanPane<Commit2DBJavaScript> {
    private List dbmPaneList = new ArrayList();
    private CommitTabbedPane commitTabbedPane;
    private JavaScriptActionPane javaScriptActionPane;
    private UIButton addCallbackButton;

    private JPanel cardPane;
    private String[] cardNames;

    /**
     * 构造函数，控件事件的提交入库面板
     * @param javaScriptActionPane JS提交面板对象
     * @param dbManipulationPaneList 提交入库的提交面板列表
     */
    public Commit2DBJavaScriptPane(final JavaScriptActionPane javaScriptActionPane, List dbManipulationPaneList) {
        this.dbmPaneList=dbManipulationPaneList;
        this.javaScriptActionPane = javaScriptActionPane;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        commitTabbedPane = new CommitTabbedPane(this,dbmPaneList);
        commitTabbedPane.setPreferredSize(new Dimension(commitTabbedPane.getWidth(),20));
        this.add(commitTabbedPane, BorderLayout.NORTH) ;

        cardPane = new JPanel(new CardLayout());
        cardNames = new String[dbmPaneList.size()] ;
        for (int i = 0; i < this.dbmPaneList.size(); i++) {
            if(((DBManipulationPane) this.dbmPaneList.get(i)).getSubMitName() == null){
                cardNames[i] = "";
            } else{
                cardNames[i] =((DBManipulationPane) this.dbmPaneList.get(i)).getSubMitName();
            }
            cardPane.add((DBManipulationPane)this.dbmPaneList.get(i),cardNames[i]);
        }
        this.add(cardPane, BorderLayout.CENTER);

        JPanel btPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		this.add(btPane, BorderLayout.SOUTH);

		addCallbackButton = javaScriptActionPane.createCallButton();
		btPane.add(addCallbackButton);
	}


    /**
     * 更新DBManipulationPane
     */
    public void updateCardPane(){
        cardNames = new String[dbmPaneList.size()] ;
        for (int i = 0; i < this.dbmPaneList.size(); i++) {
            if(((DBManipulationPane) this.dbmPaneList.get(i)).getSubMitName() == null){
                cardNames[i] = "";
            } else{
                cardNames[i] =((DBManipulationPane) this.dbmPaneList.get(i)).getSubMitName();
            }
            cardPane.add((DBManipulationPane)this.dbmPaneList.get(i),cardNames[i]);
        }
        CardLayout cardLayout = (CardLayout)cardPane.getLayout();
        cardLayout.show(cardPane,cardNames[commitTabbedPane.getSelectedIndex()]);
    }

    public void setList(List list){
        this.dbmPaneList = list;
    }

    /**
     * 新建DBManipulationPane
     * @return    新建的DBManipulationPane
     */
    public DBManipulationPane createDBManipulationPane(){
        DBManipulationPane db = javaScriptActionPane.createDBManipulationPane();
        db.populateBean(null);
        dbmPaneList.add(db);
        return db;

    }

    /**
     * 窗口名称
     * @return 返回窗口名称
     */
	public String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_JavaScript_Commit_To_Database");
	}

	/**
	 * 界面重置
	 */
	public void reset() {
		this.javaScriptActionPane.setCall(null);
		//重置后只保留，只留第一个tab
        while (dbmPaneList.size() > 1){
            dbmPaneList.remove(1);
        }
        ((DBManipulationPane)dbmPaneList.get(0)).populateBean(null);
	}

	@Override
    /**
     * 将JavaBean内的数据输出至界面上
     */
	public void populateBean(Commit2DBJavaScript commit2db) {
		if (commit2db == null) {
			reset();
			return;
		}
        //先把原来的list清除，然后再根据传入参数重新add
        dbmPaneList.clear();
		this.javaScriptActionPane.setCall(commit2db.getCallBack());
        for(int i = 0;i < commit2db.getDBManipulation().size();i++){
            DBManipulationPane dbmp = javaScriptActionPane.createDBManipulationPane();
            dbmp.populateBean((DBManipulation)commit2db.getDBManipulation().get(i));
            dbmPaneList.add(dbmp);
        }
        commitTabbedPane.refreshTab();
    }

    /**
     * 更新数据层JavaBean
     * @return 返回JavaBean
     */
	public Commit2DBJavaScript updateBean() {
		Commit2DBJavaScript commit2dbJavaScript = new Commit2DBJavaScript();

        List dbmaniList = new ArrayList();
       for(int i = 0; i < this.dbmPaneList.size(); i++){
           DBManipulationPane dbmpane =(DBManipulationPane)this.dbmPaneList.get(i);
            if(i > dbmPaneList.size()-1){
                dbmPaneList.add(dbmpane);
            }
            DBManipulation dbManipulation = dbmpane.updateBean();
            dbmaniList.add(dbManipulation);
        }
		commit2dbJavaScript.setDBManipulation(dbmaniList);

		commit2dbJavaScript.setCallBack(this.javaScriptActionPane.getCall());

		this.javaScriptActionPane.setCall(null);

		return commit2dbJavaScript;
	}

    /**
     * 判断是否是能接受的数据类型
     * @param ob 对象
     * @return 返回是否是能接受的数据类型
     */
	public boolean accept(Object ob) {
		return ob instanceof Commit2DBJavaScript;
	}

}