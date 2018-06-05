/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.fun.BackgroundQuickUIProvider;
import com.fr.design.fun.CellAttributeProvider;
import com.fr.design.fun.PresentKindProvider;
import com.fr.design.gui.frpane.UITitlePanel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itabpane.TitleChangeListener;
import com.fr.design.mainframe.cell.CellElementEditPane;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.Elem;
import com.fr.report.elementcase.TemplateElementCase;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;


/**
 * 所有组件一次全部加载，不存在延迟加载。 原因：设计器打开第一张模板的时候，会初始化许多许多东西。这个过程需要很长时间（快的3-5s）。
 * 单元格属性表初始化全部组件，也用不了多长时间，相对于上面的（3-5s）很短，用户根本不会感觉多了时间。
 * 所以基本不影响体验。而且，以后用单元格属性表时，不会应为里面的部分组件没有初始化而导致设计器突然卡一下,很流畅的.
 *
 * @author zhou
 * @since 2012-5-24下午1:50:21
 */
public class CellElementPropertyPane extends DockingView {

    static {
        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {
            
            @Override
            public void on(PluginEvent event) {
                
                synchronized (CellElementPropertyPane.class) {
                    singleton = new CellElementPropertyPane();
                }
            }
        }, new PluginFilter() {
            
            @Override
            public boolean accept(PluginContext context) {

                return context.contain(PluginModule.ExtraDesign, BackgroundQuickUIProvider.MARK_STRING)
                        || context.contain(PluginModule.ExtraDesign, PresentKindProvider.MARK_STRING)
                        || context.contain(PluginModule.ExtraDesign, CellAttributeProvider.MARK_STRING);

            }
        });
    }
    
    
    public synchronized static CellElementPropertyPane getInstance() {
        if (singleton == null) {
            singleton = new CellElementPropertyPane();
        }
        return singleton;
    }

    private static CellElementPropertyPane singleton;

    private CellElementEditPane cellElementEditPane;

    private JPanel titlePane;
    private UILabel title;

    private TitleChangeListener titleListener = new TitleChangeListener() {

        @Override
        public void fireTitleChange(String addName) {
            title.setText(Inter.getLocText("FR-Designer_CellElement_Property_Table") + '-' + addName);
        }
    };


    private CellElementPropertyPane() {
        this.setLayout(new BorderLayout());
        this.setBorder(null);
        cellElementEditPane = new CellElementEditPane();
        cellElementEditPane.addTitleChangeListner(titleListener);
        titlePane = new JPanel(new BorderLayout());
        title = new UILabel(this.getViewTitle() + '-' + Inter.getLocText("ExpandD-Expand_Attribute")) {
            private static final long serialVersionUID = 1L;

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 19);
            }
        };
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        titlePane.add(title, BorderLayout.CENTER);
        titlePane.setBorder(BorderFactory.createEmptyBorder(10,0,1,0));
//        this.add(titlePane, BorderLayout.NORTH);
        this.add(cellElementEditPane, BorderLayout.CENTER);

    }

    @Override
    public void refreshDockingView() {
        singleton = new CellElementPropertyPane();
    }

    private Elem getSelectedElement(Selection selection, TemplateElementCase elementCase) {
        Elem element = null;
        if (selection instanceof CellSelection) {
            CellSelection cs = (CellSelection) selection;
            element = elementCase.getCellElement(cs.getColumn(), cs.getRow());
        } else if (selection instanceof FloatSelection) {
            FloatSelection fs = (FloatSelection) selection;
            element = elementCase.getFloatElement(fs.getSelectedFloatName());
        }

        if (element == null) {
            element = new DefaultTemplateCellElement(0, 0);
        }
        return element;
    }

    public void removeAll() {
        this.remove(titlePane);
        this.remove(cellElementEditPane);
    }

    public void reInit(ElementCasePane ePane) {
        if (cellElementEditPane.getParent() == null) {  // 如果处于隐藏状态，则让其显示
            this.add(cellElementEditPane, BorderLayout.CENTER);
        }
        cellElementEditPane.populate(ePane);
    }

    public void populate(ElementCasePane ePane) {
    	 TemplateElementCase elementCase = ePane.getEditingElementCase();
         if (elementCase == null) {
             return;
         }
         ePane.getSelection().populatePropertyPane(ePane);
    }

    @Override
    public String getViewTitle() {
        return Inter.getLocText("FR-Designer_CellElement_Property_Table");
    }

    @Override
    public Icon getViewIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/m_report/qb.png");
    }

    @Override
    public Location preferredLocation() {
        return Location.WEST_BELOW;
    }

    @Override
    public UITitlePanel createTitlePanel() {
        return new UITitlePanel(this);
    }

    /**
     * 一些面板可能有二级菜单（比如说形态）
     *
     * @param id
     */
    public void GoToPane(String... id) {
        cellElementEditPane.setSelectedIndex(id);
        EastRegionContainerPane.getInstance().switchTabTo(EastRegionContainerPane.KEY_CELL_ATTR);
        EastRegionContainerPane.getInstance().setWindow2PreferWidth();
    }

}