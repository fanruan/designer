package com.fr.design.gui.controlpane;

import com.fr.design.DesignerEnvManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.tabledata.tabledatapane.GlobalMultiTDTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.GlobalTreeTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.MultiTDTableDataPane;
import com.fr.design.data.tabledata.tabledatapane.TreeTableDataPane;
import com.fr.design.env.DesignerWorkspaceInfo;
import com.fr.design.env.RemoteDesignerWorkspaceInfo;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;

import javax.swing.JPanel;
import java.awt.CardLayout;

/**
 * Created by plough on 2018/8/13.
 */
class JControlUpdatePane extends JPanel {
    private ListControlPaneProvider listControlPane;
    private CardLayout card;
    private JPanel cardPane;
    private BasicBeanPane[] updatePanes;

    private ListModelElement elEditing;

    private JControlUpdatePane(ListControlPaneProvider listControlPane) {
        this.listControlPane = listControlPane;
        initUpdatePane();
    }

    public static JControlUpdatePane newInstance(ListControlPaneProvider listControlPane) {
        return new JControlUpdatePane(listControlPane);
    }

    private void initUpdatePane() {
        NameableCreator[] creators = listControlPane.creators();
        if (creators == null) {
            return;
        }
        card = new CardLayout();
        cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardPane.setLayout(card);
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.add(cardPane);
        int len = creators.length;
        updatePanes = new BasicBeanPane[len];
    }

    public BasicBeanPane[] getUpdatePanes() {
        return updatePanes;
    }

    public void populate() {
        ListModelElement el = listControlPane.getSelectedValue();
        if (el == null) {
            return;
        }

        elEditing = el;
        NameableCreator[] creators = listControlPane.creators();

        //倒序的原因是为了让一些继承内置连接类的插件实现能够生效REPORT-15409
        for (int i = updatePanes.length - 1; i > -1; i--) {
            Object ob2Populate = creators[i].acceptObject2Populate(el.wrapper);
            if (ob2Populate != null) {
                if (updatePanes[i] == null) {
                    if (isMulti(creators[i].getUpdatePane()) || isTree(creators[i].getUpdatePane())) {
                        updatePanes[i] = listControlPane.createPaneByCreators(creators[i], el.wrapper.getName());
                    } else {
                        updatePanes[i] = listControlPane.createPaneByCreators(creators[i]);
                    }
                    cardPane.add(updatePanes[i], String.valueOf(i));
                }
                card.show(cardPane, String.valueOf(i));
                try{
                    updatePanes[i].populateBean(ob2Populate);
                }catch (Exception e){
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
                break;
            }
        }
    }


    public boolean isMulti(Class _class) {
        return ComparatorUtils.equals(_class, GlobalMultiTDTableDataPane.class) || ComparatorUtils.equals(_class, MultiTDTableDataPane.class);
    }

    public boolean isTree(Class _class) {
        return ComparatorUtils.equals(_class, GlobalTreeTableDataPane.class) || ComparatorUtils.equals(_class, TreeTableDataPane.class);
    }

    public void update() {
        NameableCreator[] creators = listControlPane.creators();
        for (int i = 0; i < updatePanes.length; i++) {
            BasicBeanPane pane = updatePanes[i];

            if (pane != null && pane.isVisible()) {
                Object bean = pane.updateBean();
                try {
                    if (bean instanceof RemoteDesignerWorkspaceInfo) {
                        DesignerWorkspaceInfo info = DesignerEnvManager.getEnvManager().getWorkspaceInfo(elEditing.wrapper.getName());
                        String remindTime = info.getRemindTime();
                        ((RemoteDesignerWorkspaceInfo) bean).setRemindTime(remindTime);
                    }
                }catch (Exception e){
                    FineLoggerFactory.getLogger().info("remindTime is not exist");
                }
                if (i < creators.length) {
                    creators[i].saveUpdatedBean(elEditing, bean);
                }
            }
        }
    }

    public void checkValid() throws Exception {
        if (updatePanes != null) {
            for (int i = 0; i < updatePanes.length; i++) {
                if (updatePanes[i] != null) {
                    updatePanes[i].checkValid();
                }
            }
        }
    }
}