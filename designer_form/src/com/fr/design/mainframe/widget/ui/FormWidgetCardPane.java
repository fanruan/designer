package com.fr.design.mainframe.widget.ui;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.*;
import com.fr.design.dialog.AttrScrollPane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.widget.DataModify;
import com.fr.design.widget.Operator;
import com.fr.design.widget.FormWidgetDefinePaneFactoryBase;
import com.fr.design.widget.ui.designer.component.WidgetAbsoluteBoundPane;
import com.fr.design.widget.ui.designer.component.WidgetBoundPane;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WScaleLayout;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.form.ui.widget.CRBoundsWidget;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;


/**
 * Created by ibm on 2017/7/25.
 */
public class FormWidgetCardPane extends AbstractAttrNoScrollPane {
    private AttributeChangeListener listener;
    private FormDesigner designer;
    //当前的编辑器属性定义面板
    private DataModify<Widget> currentEditorDefinePane;
    private FormBasicPropertyPane widgetPropertyPane;
    private JPanel attriCardPane;

    private XCreator xCreator;
    private WidgetBoundPane widgetBoundPane;


    public FormWidgetCardPane(FormDesigner designer) {
        super();
        this.xCreator = findXcreator(designer);
        this.designer = designer;
        initComponents();
        initDefinePane();
        widgetBoundPane = createWidgetBoundPane(xCreator);
        if (widgetBoundPane != null) {
            attriCardPane.add(widgetBoundPane, BorderLayout.CENTER);
        }
    }

    public XLayoutContainer getParent(XCreator source) {
        XLayoutContainer container = XCreatorUtils.getParentXLayoutContainer(source);
        if (source.acceptType(XWFitLayout.class) || source.acceptType(XWParameterLayout.class)) {
            container = null;
        }
        return container;
    }

    public WidgetBoundPane createWidgetBoundPane(XCreator xCreator) {
        XLayoutContainer xLayoutContainer = getParent(xCreator);
        if (xLayoutContainer == null || xCreator instanceof XWParameterLayout || xCreator instanceof XWAbsoluteLayout) {
            return null;
        } else if (xLayoutContainer instanceof XWAbsoluteLayout) {
            return new WidgetAbsoluteBoundPane(xCreator);
        }
        return new WidgetBoundPane(xCreator);
    }

    protected JPanel createContentPane() {
        return null;
    }

    public XCreator findXcreator(FormDesigner designer) {
        int size = designer.getSelectionModel().getSelection().size();
        if (size == 0 || size == 1) {
            XCreator creator = size == 0 ? designer.getRootComponent() : designer.getSelectionModel().getSelection()
                    .getSelectedCreator();
            return creator;
        } else {
            return null;
        }
    }

    /**
     * 后台初始化所有事件.
     */
    public void initAllListeners() {

    }

    /**
     * 后台初始化所有事件.
     */
    public void reinitAllListeners() {
        initListener(this);
    }


    protected void initContentPane() {
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        XCreator innerCreator = getXCreatorDedicated();

        final JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        BasicScrollPane basicScrollPane = new AttrScrollPane() {
            @Override
            protected JPanel createContentPane() {
                return jPanel;
            }
        };
        this.add(basicScrollPane, BorderLayout.CENTER);
        attriCardPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jPanel.add(attriCardPane, BorderLayout.CENTER);
        jPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        final boolean isExtraWidget = FormWidgetDefinePaneFactoryBase.isExtraXWidget(innerCreator.toData());
        this.listener = new AttributeChangeListener() {
            @Override
            public void attributeChange() {
                if(!isExtraWidget){
                    updateCreator();
                }
                updateWidgetBound();
                firePropertyEdit();
            }
        };

        if(isExtraWidget){
            return;
        }

        widgetPropertyPane = WidgetBasicPropertyPaneFactory.createBasicPropertyPane(innerCreator);

        UIExpandablePane uiExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Basic"), 280, 20, widgetPropertyPane);

        jPanel.add(uiExpandablePane, BorderLayout.NORTH);

    }

    private void initDefinePane() {
        currentEditorDefinePane = null;
        XCreator creator = getXCreatorDedicated();
        FormWidgetDefinePaneFactoryBase.RN rn = FormWidgetDefinePaneFactoryBase.createWidgetDefinePane(creator, designer, creator.toData(), new Operator() {
            @Override
            public void did(DataCreatorUI ui, String cardName) {
                //todo
            }
        });
        DataModify<Widget> definePane = rn.getDefinePane();

        JComponent jComponent = definePane.toSwingComponent();

        attriCardPane.add(jComponent, BorderLayout.NORTH);
        currentEditorDefinePane = definePane;
    }

    private XCreator getXCreatorDedicated() {
        boolean dedicateLayout = xCreator.acceptType(XWScaleLayout.class) && xCreator.getComponentCount() > 0 && ((XCreator) xCreator.getComponent(0)).shouldScaleCreator() || xCreator.acceptType(XWTitleLayout.class);
        return dedicateLayout ? (XCreator) xCreator.getComponent(0) : xCreator;
    }

    @Override
    public String title4PopupWindow() {
        return "Widget";
    }

    public void populate() {
        //populate之前先移除所有的监听
        removeAttributeChangeListener();
        Widget cellWidget = xCreator.toData();
        if (widgetBoundPane != null) {
            widgetBoundPane.populate();
        }
        Widget innerWidget = cellWidget;
        if (cellWidget.acceptType(WScaleLayout.class)) {
            Widget crBoundsWidget = ((WScaleLayout) cellWidget).getBoundsWidget();
            innerWidget = ((CRBoundsWidget) crBoundsWidget).getWidget();
        } else if (cellWidget.acceptType(WTitleLayout.class)) {
            CRBoundsWidget crBoundsWidget = ((WTitleLayout) cellWidget).getBodyBoundsWidget();
            innerWidget = crBoundsWidget.getWidget();
        }
        currentEditorDefinePane.populateBean(innerWidget);
        if(widgetPropertyPane != null){
            widgetPropertyPane.populate(innerWidget);
        }
        reinitAllListeners();
        this.addAttributeChangeListener(listener);
    }


    public void updateCreator() {
        currentEditorDefinePane.setGlobalName(getGlobalName());
        Widget widget = currentEditorDefinePane.updateBean();
        if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("FR-Designer_Basic")) && widgetPropertyPane != null) {
            widgetPropertyPane.update(widget);
            xCreator.resetCreatorName(widget.getWidgetName());
            xCreator.resetVisible(widget.isVisible());
            designer.getEditListenerTable().fireCreatorModified(xCreator, DesignerEvent.CREATOR_RENAMED);
            return;
        }
        fireValueChanged();
    }

    public void updateWidgetBound (){
        if (widgetBoundPane != null && ComparatorUtils.equals(getGlobalName(), Inter.getLocText("FR-Designer_Coords_And_Size"))) {
            widgetBoundPane.update();
            designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_RESIZED);
        }
        designer.refreshDesignerUI();
    }


    @Override
    /**
     *检查
     */
    public void checkValid() throws Exception {
        currentEditorDefinePane.checkValid();
    }

    public void fireValueChanged() {
        XCreator creator = getXCreatorDedicated();
        creator.firePropertyChange();
    }

    public String getIconPath() {
        return StringUtils.EMPTY;
    }

    public void firePropertyEdit() {
        designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
    }

}
