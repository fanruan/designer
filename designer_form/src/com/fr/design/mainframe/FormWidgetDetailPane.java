package com.fr.design.mainframe;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.fr.base.BaseUtils;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.general.Inter;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-7-8
 * Time: 下午8:18
 */
public class FormWidgetDetailPane extends FormDockView{
    public static final String PARA = "para";
    public static final String BODY = "body";

    private UITabbedPane tabbedPane;
    private ParameterPropertyPane parameterPropertyPane;
    private MobileWidgetTable mobileWidgetTable;
    private MobileBodyWidgetTable mobileBodyWidgetTable;
    private UIScrollPane downPanel;
    private JPanel centerPane;
    private CardLayout cardLayout;

    public static FormWidgetDetailPane getInstance() {
        if (HOLDER.singleton == null) {
            HOLDER.singleton = new FormWidgetDetailPane();
        }
        return HOLDER.singleton;
    }

    private  FormWidgetDetailPane(){
        setLayout(FRGUIPaneFactory.createBorderLayout());
    }


    public static FormWidgetDetailPane getInstance(FormDesigner formEditor) {
        HOLDER.singleton.setEditingFormDesigner(formEditor);
        HOLDER.singleton.refreshDockingView();
        return HOLDER.singleton;
    }

    private static class HOLDER {
        private static FormWidgetDetailPane singleton = new FormWidgetDetailPane();
    }

    public String getViewTitle() {
        return Inter.getLocText("FR-Widget_Tree_And_Table");
    }

    @Override
    public Icon getViewIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/m_report/attributes.png");
    }

    /**
     * 初始化
     */
    public void refreshDockingView(){
        FormDesigner designer = this.getEditingFormDesigner();
        removeAll();
        if(designer == null){
            clearDockingView();
            return;
        }
        parameterPropertyPane = ParameterPropertyPane.getInstance(designer);
        parameterPropertyPane.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel esp = FRGUIPaneFactory.createBorderLayout_S_Pane();
        esp.setBorder(null);
        mobileWidgetTable = new MobileWidgetTable(designer);
        mobileBodyWidgetTable = new MobileBodyWidgetTable(designer);
        designer.addDesignerEditListener(new mobileWidgetDesignerAdapter());
        centerPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardLayout = (CardLayout) centerPane.getLayout();
        centerPane.add(mobileWidgetTable,PARA);
        centerPane.add(mobileBodyWidgetTable,BODY);
        if(hasSelectParaPane(designer)){
            cardLayout.show(centerPane,PARA);

        } else {
            cardLayout.show(centerPane,BODY);
        }
        downPanel = new UIScrollPane(centerPane);
        downPanel.setBorder(new LineBorder(Color.gray));
        esp.add(downPanel,BorderLayout.CENTER);
        UILabel upLabel = new UILabel(Inter.getLocText("FR-Widget_Mobile_Table"),SwingConstants.CENTER);
        upLabel.setBorder(BorderFactory.createEmptyBorder(6,0,6,0));
        esp.add(upLabel,BorderLayout.NORTH);

        tabbedPane = new UITabbedPane();
        tabbedPane.setOpaque(true);
        tabbedPane.setBorder(null);
        tabbedPane.setTabPlacement(SwingConstants.BOTTOM);
        tabbedPane.addTab(Inter.getLocText("FR-Widget_Mobile_Tree"), parameterPropertyPane);
        tabbedPane.addTab(Inter.getLocText("FR-Widget_Mobile_Terminal"), esp);
        add(tabbedPane, BorderLayout.CENTER);

    }

    public void setSelectedIndex(int index){
        tabbedPane.setSelectedIndex(index);
    }

    /**
     * 选中的组件是否在参数面板里
     * @param designer   设计器
     * @return     是则返回true
     */
    public boolean hasSelectParaPane(FormDesigner designer){
        XCreator xCreator = designer.getSelectionModel().getSelection().getSelectedCreator();
        if(xCreator == null){
            xCreator = designer.getRootComponent();
        }
        XLayoutContainer container = XCreatorUtils.getHotspotContainer(xCreator);
        return xCreator.acceptType(XWParameterLayout.class) || container.acceptType(XWParameterLayout.class);
    }

    /**
     * 清除数据
     */
    public void clearDockingView() {
        parameterPropertyPane = null;
        mobileWidgetTable = null;
        JScrollPane psp = new JScrollPane();
        psp.setBorder(null);
        this.add(psp, BorderLayout.CENTER);
    }

    public class mobileWidgetDesignerAdapter implements DesignerEditListener {

        public mobileWidgetDesignerAdapter() {
        }

        /**
         *  响应界面改变事件
         * @param evt  事件
         */
        public void fireCreatorModified(DesignerEvent evt) {
            if (evt.getCreatorEventID() == DesignerEvent.CREATOR_RESIZED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_EDITED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_SELECTED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_ADDED) {
                int value = downPanel.getVerticalScrollBar().getValue();
                if(hasSelectParaPane(getEditingFormDesigner())){
                    cardLayout.show(centerPane,PARA);
                    mobileWidgetTable.refresh();
                } else {
                    cardLayout.show(centerPane,BODY);
                    mobileBodyWidgetTable.refresh();
                }
                //出现滚动条
                downPanel.doLayout();
                //控件列表选中某组件，触发表单中选中控件，选中事件又触发列表刷新，滚动条回到0
                //此处设置滚动条值为刷新前
                downPanel.getVerticalScrollBar().setValue(value);
            }
        }
    }

    /**
     * 定位
     * @return  位置
     */
    public Location preferredLocation() {
        return Location.WEST_BELOW;
    }


}