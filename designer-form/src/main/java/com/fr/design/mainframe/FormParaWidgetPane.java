package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.fun.FormWidgetOptionProvider;
import com.fr.design.gui.core.FormWidgetOption;
import com.fr.design.gui.core.UserDefinedWidgetOption;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.UserDefinedWidgetConfig;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetConfig;
import com.fr.form.ui.WidgetInfoConfig;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.injectable.SpecialLevel;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.stable.ArrayUtils;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author null
 */
public class FormParaWidgetPane extends JPanel {
    private static FormParaWidgetPane THIS;
    private final static int BORDER = 5;
    private final static int WIDGET_WIDTHGAP = 4;

    private List<WidgetOption> predifinedwidgeList = new ArrayList<WidgetOption>();

    private UIPopupMenu chartTypePopupMenu;
    private UIPopupMenu widgetTypePopupMenu;
    private WidgetOption[] widgetOptions = null;
    private WidgetOption[] chartOptions = null;
    private WidgetOption[] layoutOptions = null;
    private int widgetButtonWidth = 22;
    private int widgetButtonHeight = 20;
    private int smallGAP = 6;
    private int jsparatorWidth = 2;
    private int jsparatorHeight = 50;
    //预定义控件每行最多显示3个
    private int preWidgetShowMaxNum = 3;
    //预定义控件最多显示20行
    private int preWidgetShowMaxRow = 20;
    //显示8个图表组件
    private static final int COMMON_CHAR_NUM = 8;
    //显示10个普通控件
    private int commonWidgetNum = 10;
    private JSeparator jSeparatorPara;
    private JSeparator jSeparatorChart;
    private JSeparator jSeparatorLayout;

    private UILabel paraLabel;

    private FormDesigner designer;

    static {
        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {

            @Override
            public void on(PluginEvent event) {

                synchronized (FormParaWidgetPane.class) {
                    THIS = null;
                }
            }
        }, new PluginFilter() {

            @Override
            public boolean accept(PluginContext context) {

                return context.contain(PluginModule.ExtraDesign, FormWidgetOptionProvider.XML_TAG)
                        || context.contain(PluginModule.ExtraChartDesign, SpecialLevel.IndependentChartUIProvider.getTagName());
            }
        });
    }

    public static synchronized FormParaWidgetPane getInstance(FormDesigner designer) {
        if (THIS == null) {
            THIS = new FormParaWidgetPane();
        }
        THIS.designer = designer;
        THIS.setTarget(designer);
        return THIS;
    }

    public FormParaWidgetPane() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        DesignerContext.getDesignerFrame().getCenterTemplateCardPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (FormParaWidgetPane.this.getParent() != null) {
                    JPanel parent = (JPanel) FormParaWidgetPane.this.getParent();
                    int deltaWidth = 0;
                    for (int i = 0; i < parent.getComponentCount() - 1; i++) {
                        deltaWidth += parent.getComponent(i).getWidth();
                    }

                    if (deltaWidth == 0) {
                        return;
                    }

                    Dimension d = parent.getSize();
                    setPreferredSize(new Dimension(d.width - deltaWidth, d.height));
                    LayoutUtils.layoutContainer(parent);
                }
            }
        });
        initFormParaComponent();
    }

    private void initWidgetTypePopUp() {
        JPanel widgetPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        loadPredefinedWidget();
        int rowNum = calculateWidgetWindowRowNum();
        JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (WidgetOption o : loadWidgetOptions()) {
            westPanel.add(new ToolBarButton(o));
        }
        int x = commonWidgetNum * (widgetButtonWidth + smallGAP) - smallGAP;
        westPanel.setPreferredSize(new Dimension(x, (int) (rowNum * westPanel.getPreferredSize().getHeight())));
        JPanel eastPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (WidgetOption no : predifinedwidgeList) {
            eastPane.add(new ToolBarButton(no));
        }
        int maxWidth = preWidgetShowMaxNum * (widgetButtonWidth + smallGAP) - smallGAP + WIDGET_WIDTHGAP;
        int width = predifinedwidgeList.size() >= preWidgetShowMaxNum ? maxWidth : (int) eastPane.getPreferredSize().getWidth();
        eastPane.setPreferredSize(new Dimension(width, (int) (rowNum * eastPane.getPreferredSize().getHeight())));

        UIScrollPane eastScrollPane = new UIScrollPane(eastPane);
        eastScrollPane.setBorder(null);
        int maxHeight = preWidgetShowMaxRow * (widgetButtonHeight + smallGAP) - smallGAP;
        int height = predifinedwidgeList.size() >= preWidgetShowMaxNum * preWidgetShowMaxRow ? maxHeight : (int) eastPane.getPreferredSize().getHeight();
        width = predifinedwidgeList.size() >= preWidgetShowMaxNum * preWidgetShowMaxRow ? (int) eastPane.getPreferredSize().getWidth() + smallGAP + jsparatorWidth : (int) eastPane.getPreferredSize().getWidth();
        eastScrollPane.setPreferredSize(new Dimension(width, height));

        widgetPane.add(westPanel);
        widgetPane.add(createJSeparator(height));
        widgetPane.add(eastScrollPane);

        widgetTypePopupMenu = new UIPopupMenu();
        widgetTypePopupMenu.setBackground(UIConstants.SELECT_TAB);
        widgetTypePopupMenu.add(widgetPane);
    }

    private void initChartTypePopUp() {
        if (chartTypePopupMenu == null) {
            JPanel componentsPara = new JPanel(new FlowLayout(FlowLayout.LEFT));
            WidgetOption[] chartOptions = loadChartOptions();
            for (WidgetOption chartOption : chartOptions) {
                componentsPara.add(new ToolBarButton(chartOption));
            }
            int x = COMMON_CHAR_NUM * (widgetButtonWidth + smallGAP);
            int y = (int) Math.ceil(chartOptions.length / ((double) COMMON_CHAR_NUM)) * (widgetButtonHeight + smallGAP);
            componentsPara.setPreferredSize(new Dimension(x, y));
            chartTypePopupMenu = new UIPopupMenu();
            chartTypePopupMenu.setBackground(UIConstants.SELECT_TAB);
            chartTypePopupMenu.add(componentsPara);
        }
    }


    private void initFormParaComponent() {
        this.removeAll();
        // 菜单中的布局先注释掉
        JPanel paraPane = new JPanel(new FlowLayout());
        ToolBarButton paraButton = new paraButton(FormWidgetOption.PARAMETERCONTAINER);
        paraPane.add(paraButton);
        add(createNormalCombinationPane(paraPane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter")));
        jSeparatorPara = createJSeparator();
        add(jSeparatorPara);

        JPanel layoutPane = new JPanel(new FlowLayout());
        for (WidgetOption option : loadLayoutOptions()) {
            layoutPane.add(new ToolBarButton(option));
        }
        layoutPane.add(new ToolBarButton(FormWidgetOption.ELEMENTCASE));
        add(createNormalCombinationPane(layoutPane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_Block_Blank")));
        jSeparatorLayout = createJSeparator();
        add(jSeparatorLayout);

        // 初始化的时候根据图表的总个数获得单行显示图表的个数
        int totalChartNums = loadChartOptions().length;
        if (totalChartNums > 0) {
            JPanel chartTypePane = new JPanel(new FlowLayout());
            for (int i = 0; i < COMMON_CHAR_NUM; i++) {
                chartTypePane.add(new ToolBarButton(loadChartOptions()[i]));
            }
            add(createChartCombinationPane(chartTypePane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_ToolBar_Chart")));
            jSeparatorChart = createJSeparator();
            add(jSeparatorChart);
        }

        JPanel widgetPane = new JPanel(new FlowLayout());
        for (int i = 0; i < commonWidgetNum; i++) {
            widgetPane.add(new ToolBarButton(loadWidgetOptions()[i]));
        }
        widgetPane.add(createJSeparator(20));
        loadPredefinedWidget();
        int num = Math.min(predifinedwidgeList.size(), preWidgetShowMaxNum);
        for (int i = 0; i < num; i++) {
            widgetPane.add(new ToolBarButton(predifinedwidgeList.get(i)));
        }
        add(createWidgetCombinationPane(widgetPane, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_ToolBar_Widget")));
        add(createJSeparator());
    }

    private void loadPredefinedWidget() {
        predifinedwidgeList.clear();
        if (designer != null) {
            WidgetOption[] designerPre = designer.getDesignerMode().getPredefinedWidgetOptions();
            predifinedwidgeList.addAll(Arrays.asList(designerPre));
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
                if (!XCreatorUtils.createXCreator(widget).canEnterIntoParaPane()) {
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
        initFormParaComponent();

    }

    private JPanel createNormalCombinationPane(JComponent jComponent, String typeName) {
        JPanel reportPane = new JPanel(new BorderLayout(17, 5));
        reportPane.add(jComponent, BorderLayout.CENTER);
        JPanel labelPane = new JPanel(new BorderLayout());
        UILabel label = new UILabel(typeName, UILabel.CENTER);
        if (ComparatorUtils.equals(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter"), typeName)) {
            paraLabel = label;
        }
        labelPane.add(label, BorderLayout.CENTER);
        reportPane.add(labelPane, BorderLayout.SOUTH);
        reportPane.setPreferredSize(new Dimension((int) reportPane.getPreferredSize().getWidth(), (int) reportPane.getPreferredSize().getHeight()));
        return reportPane;
    }


    private JPanel createChartCombinationPane(JComponent jComponent, String typeName) {
        JPanel chartPane = new JPanel(new BorderLayout(17, 5));
        chartPane.add(jComponent, BorderLayout.CENTER);
        JPanel labelPane = new JPanel(new BorderLayout());
        labelPane.add(new UILabel(typeName, UILabel.CENTER), BorderLayout.CENTER);
        UIButton chartPopUpButton = createPopUpButton();
        chartPopUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                initChartTypePopUp();
                chartTypePopupMenu.show(FormParaWidgetPane.this,
                        (int) jSeparatorLayout.getLocation().getX() + BORDER,
                        (int) jSeparatorLayout.getLocation().getY());
            }
        });
        labelPane.add(chartPopUpButton, BorderLayout.EAST);
        chartPane.add(labelPane, BorderLayout.SOUTH);
        return chartPane;
    }

    private JPanel createWidgetCombinationPane(JComponent jComponent, String typeName) {
        JPanel widgetPane = new JPanel(new BorderLayout(17, 5));
        widgetPane.add(jComponent, BorderLayout.CENTER);
        JPanel labelPane = new JPanel(new BorderLayout());
        labelPane.add(new UILabel(typeName, UILabel.CENTER), BorderLayout.CENTER);
        UIButton chartPopUpButton = createPopUpButton();
        chartPopUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                initWidgetTypePopUp();
                if(jSeparatorChart != null) {
                    widgetTypePopupMenu.show(FormParaWidgetPane.this,
                            (int) jSeparatorChart.getLocation().getX() + BORDER,
                            (int) jSeparatorChart.getLocation().getY());
                }else{
                    widgetTypePopupMenu.show(FormParaWidgetPane.this,
                            (int) jSeparatorLayout.getLocation().getX() + BORDER,
                            (int) jSeparatorLayout.getLocation().getY());
                }
            }

        });
        labelPane.add(chartPopUpButton, BorderLayout.EAST);
        widgetPane.add(labelPane, BorderLayout.SOUTH);
        return widgetPane;
    }

    private int calculateWidgetWindowRowNum() {
        //向上取整
        int eastRowNum = (int) Math.ceil((double) predifinedwidgeList.size() / (double) preWidgetShowMaxNum);
        int westRowNum = (int) Math.ceil((double) loadWidgetOptions().length / (double) commonWidgetNum);
        int rowNum = Math.max(eastRowNum, westRowNum);
        rowNum = Math.max(rowNum, 2);
        rowNum = Math.min(rowNum, preWidgetShowMaxRow);
        return rowNum;
    }


    private JSeparator createJSeparator() {
        JSeparator jSeparator = new JSeparator(SwingConstants.VERTICAL);
        jSeparator.setPreferredSize(new Dimension(jsparatorWidth, jsparatorHeight));
        return jSeparator;
    }

    private JSeparator createJSeparator(double height) {
        JSeparator jSeparator = new JSeparator(SwingConstants.VERTICAL);
        jSeparator.setPreferredSize(new Dimension(jsparatorWidth, (int) height));
        return jSeparator;
    }

    private UIButton createPopUpButton() {
        UIButton popUpButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/arrowdown.png"));
        popUpButton.set4ToolbarButton();
        return popUpButton;
    }

    private UIButton createPopDownButton() {
        UIButton popUpButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/arrowup.png"));
        popUpButton.set4ToolbarButton();
        return popUpButton;
    }

    private class paraButton extends ToolBarButton {
        public paraButton(WidgetOption no) {
            super(no);
            this.setDisabledIcon(BaseUtils.readIcon("/com/fr/web/images/form/resources/layout_parameter2.png"));
            if (designer != null) {
                this.setEnabled(designer.getParaComponent() == null);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (designer.getParaComponent() != null) {
                return;
            }

            designer.addParaComponent();
            JPanel pane = FormWidgetDetailPane.getInstance(designer);
            EastRegionContainerPane.getInstance().replaceWidgetLibPane(pane);
            this.setEnabled(false);

            designer.addDesignerEditListener(new paraButtonDesignerAdapter(this));

            JTemplate targetComponent = DesignerContext.getDesignerFrame().getSelectedJTemplate();
            if (targetComponent != null) {
                targetComponent.fireTargetModified();
            }
        }

        @Override
        public void setEnabled(boolean b) {
            super.setEnabled(b);
            paraLabel.setForeground(b ? Color.BLACK : new Color(198, 198, 198));
        }

    }

    public class paraButtonDesignerAdapter implements DesignerEditListener {
        ToolBarButton button;

        public paraButtonDesignerAdapter(ToolBarButton button) {
            this.button = button;
        }

        /**
         * 响应界面改变事件
         *
         * @param evt 事件
         */
        @Override
        public void fireCreatorModified(DesignerEvent evt) {
            button.setEnabled(designer.getParaComponent() == null);
        }
    }

    private WidgetOption[] loadWidgetOptions() {
        if (widgetOptions == null) {
            widgetOptions = ArrayUtils.addAll(WidgetOption.getFormWidgetIntance(), ExtraDesignClassManager.getInstance().getFormWidgetOptions());
        }
        return widgetOptions;
    }

    private WidgetOption[] loadLayoutOptions() {
        if (layoutOptions == null) {
            layoutOptions = ArrayUtils.addAll(FormWidgetOption.getFormLayoutInstance(), ExtraDesignClassManager.getInstance().getFormWidgetContainerOptions());
        }
        return layoutOptions;
    }

    private WidgetOption[] loadChartOptions() {
        if (chartOptions == null) {
            chartOptions = DesignModuleFactory.getExtraWidgetOptions();
        }
        return chartOptions;
    }
}
