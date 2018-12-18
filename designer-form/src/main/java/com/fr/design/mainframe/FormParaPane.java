package com.fr.design.mainframe;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.gui.core.UserDefinedWidgetOption;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.*;
import com.fr.general.GeneralContext;

import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.stable.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-7-8
 * Time: 上午10:09
 * To change this template use File | Settings | File Templates.
 */

public class FormParaPane extends JPanel {
    private static final int PANE_HEIGHT = 24;
    private static final int TOOLTIP_X = 5;
    private static final int TOOLTIP_Y = 10;
    private static Dimension originalSize;
    
    private static volatile FormParaPane THIS;
    private java.util.List<WidgetOption> predifinedwidgeList = new ArrayList<WidgetOption>();
    private UIButton predefineButton;
    private FormWidgetPopWindow predifinedWindow;


    private FormDesigner designer;
    
    static {
        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {
            
            @Override
            public void on(PluginEvent event) {
                
                THIS = null;
            }
        }, new PluginFilter() {
            
            @Override
            public boolean accept(PluginContext context) {
                
                return context.contain(PluginModule.ExtraDesign);
            }
        });
    }
    
    public static final FormParaPane getInstance(FormDesigner designer) {
        if(THIS == null) {
            THIS = new FormParaPane();
        }
        THIS.designer = designer;
        THIS.setTarget(designer);
        if (originalSize != null) {
            THIS.setPreferredSize(originalSize);
        }
        return THIS;
    }
    private ArrayList<WidgetOption> componentsList4Para = new ArrayList<WidgetOption>();


    public FormParaPane() {
        predefineButton = new UIButton(UIConstants.PRE_WIDGET_ICON) {
            @Override
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.width = PANE_HEIGHT;
                return dim;
            }
        };
        predefineButton.set4ToolbarButton();
        predefineButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_User_Defined_Widget_Config"));
        predefineButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (predifinedWindow == null) {
                    predifinedWindow = new FormWidgetPopWindow();
                }
                loadPredifinedWidget();
                predifinedWindow.showToolTip(predefineButton.getLocationOnScreen().x + predefineButton.getWidth() + TOOLTIP_X, predefineButton.getLocationOnScreen().y - TOOLTIP_Y, (WidgetOption[])predifinedwidgeList.toArray(new WidgetOption[predifinedwidgeList.size()]));
            }
        });
        setLayout(new FlowLayout(FlowLayout.LEFT));
        DesignerContext.getDesignerFrame().getCenterTemplateCardPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (FormParaPane.this.getParent() != null) {
                    JPanel fother = (JPanel)FormParaPane.this.getParent();
                    Dimension d = fother.getSize();
                    int delta_wdith = 0;
                    for (int i = 0; i < fother.getComponentCount() - 1; i ++) {
                        delta_wdith += fother.getComponent(i).getWidth();
                    }
                    setPreferredSize(new Dimension(d.width - delta_wdith, d.height));
                    LayoutUtils.layoutContainer(fother);
                }
            }
        });
        initParaComponent();
    }

    private void initParaComponent() {
        this.removeAll();
        initParaButtons();
        for (WidgetOption o : componentsList4Para) {
            add(new ToolBarButton(o));
        }
        add(new UILabel("|"));
        add(predefineButton);
    }

    private void loadPredifinedWidget() {
        predifinedwidgeList.clear();
        if(designer != null) {
            WidgetOption[] designerPre = designer.getDesignerMode().getPredefinedWidgetOptions();
            for(int i = 0; i < designerPre.length; i++) {
                predifinedwidgeList.add(designerPre[i]);
            }
        }
        WidgetInfoConfig mgr = WidgetInfoConfig.getInstance();
        Iterator<String> nameIt = mgr.getWidgetConfigNameIterator();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            WidgetConfig widgetConfig = mgr.getWidgetConfig(name);
            if (widgetConfig instanceof UserDefinedWidgetConfig) {
                Widget widget = ((UserDefinedWidgetConfig) widgetConfig).getWidget();
                String widgetClassName = widget.getClass().getName();
                if (isButtonWidget(widgetClassName)) {
                    // ...
                    continue;
                }
                if (!XCreatorUtils.createXCreator(widget).canEnterIntoParaPane()){
                    //预定义控件工具栏这儿不显示工具栏中没有的预定义控件
                    continue;
                }
                predifinedwidgeList.add(new UserDefinedWidgetOption(name));
            }
        }
    }

    private boolean isButtonWidget(String widgetClassName) {
        return widgetClassName.endsWith("DeleteRowButton") || widgetClassName.endsWith("AppendRowButton") || widgetClassName.endsWith("TreeNodeToogleButton");
    }

    private void setTarget(FormDesigner designer) {
        if (designer == null) {
            return;
        }
        initParaComponent();
    }


    private void initParaButtons() {
        if(componentsList4Para.isEmpty()) {
            WidgetOption[] options = WidgetOption.getReportParaWidgetIntance();

            WidgetOption[] basicWidgetArray = (WidgetOption[]) ArrayUtils.addAll(
                    options, ExtraDesignClassManager.getInstance().getParameterWidgetOptions()
            );


            for (WidgetOption no : basicWidgetArray) {
                this.componentsList4Para.add(no);
            }
        }
    }

}
