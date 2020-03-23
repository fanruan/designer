package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.fun.ComponentLibraryPaneProcessor;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.component.pane.ComponentLibraryPaneCreator;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.plugin.observer.PluginListenerRegistration;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-7-8
 * Time: 下午8:18
 */
public class FormWidgetDetailPane extends FormDockView{

    public static FormWidgetDetailPane getInstance() {
        if (HOLDER.singleton == null) {
            HOLDER.singleton = new FormWidgetDetailPane();
        }
        return HOLDER.singleton;
    }

    private  FormWidgetDetailPane(){
        setLayout(FRGUIPaneFactory.createBorderLayout());
        listenPluginComponentPane();
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
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Tree_And_Table");
    }

    @Override
    public Icon getViewIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/m_report/attributes.png");
    }
    
    /**
     * 初始化
     */
    @Override
    public void refreshDockingView(){
        
        FormDesigner designer = this.getEditingFormDesigner();
        removeAll();
        if(designer == null){
            clearDockingView();
            return;
        }
    
        initComponentPane();
    }
    
    private void listenPluginComponentPane() {
    
        PluginFilter filter = new PluginFilter() {
            @Override
            public boolean accept(PluginContext context) {
                return context.contain(ComponentLibraryPaneProcessor.XML_TAG);
            }
        };
        PluginListenerRegistration.getInstance().listenRunningChanged(new PluginEventListener() {
            @Override
            public void on(PluginEvent event) {
                 refreshDockingView();
            }
        }, filter);
    }
    
    private void initComponentPane() {
    
        ComponentLibraryPaneCreator creator = ComponentLibraryPaneCreator.getNew();
        JPanel componentLibraryPane = creator.create(this);
        add(componentLibraryPane, BorderLayout.CENTER);
    }

    /**
     * 清除数据
     */
    public void clearDockingView() {
        
        JScrollPane psp = new JScrollPane();
        psp.setBorder(null);
        this.add(psp, BorderLayout.CENTER);
    }

    /**
     * 定位
     * @return  位置
     */
    public Location preferredLocation() {
        return Location.WEST_BELOW;
    }


}
