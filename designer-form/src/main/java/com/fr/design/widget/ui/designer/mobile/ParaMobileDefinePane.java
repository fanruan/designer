package com.fr.design.widget.ui.designer.mobile;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.properties.PropertyTab;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.fun.ParameterExpandablePaneUIProvider;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.MobileWidgetListPane;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.form.ui.container.WSortLayout;
import com.fr.general.CloudCenter;
import com.fr.general.ComparatorUtils;
import com.fr.general.SiteCenter;
import com.fr.log.FineLoggerFactory;
import com.fr.report.ExtraReportClassManager;
import com.fr.report.fun.MobileParamStyleProvider;
import com.fr.report.mobile.DefaultMobileParamStyle;


import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.Set;

/**
 * Created by plough on 2018/2/5.
 */
public class ParaMobileDefinePane extends MobileWidgetDefinePane {
    private XCreator paraCreator;
    private FormDesigner designer;
    private Item[] items;
    private UIComboBox paramLocationComboBox;
    private AttributeChangeListener changeListener;
    private MobileWidgetListPane mobileWidgetListPane;

    public ParaMobileDefinePane(XCreator xCreator) {
        this.paraCreator = xCreator;
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        this.add(getMobilePropertyPane(), BorderLayout.NORTH);
        this.add(getMobileWidgetListPane(), BorderLayout.CENTER);
        this.addExtraUIExpandablePaneFromPlugin();
        this.repaint();
    }

    private void addExtraUIExpandablePaneFromPlugin() {
        Set<ParameterExpandablePaneUIProvider> pluginCreators = ExtraDesignClassManager.getInstance().getArray(ParameterExpandablePaneUIProvider.XML_TAG);
        JPanel panel = FRGUIPaneFactory.createYBoxEmptyBorderPane();
        for (ParameterExpandablePaneUIProvider provider : pluginCreators) {
            UIExpandablePane uiExpandablePane = provider.createUIExpandablePane();
            PropertyTab propertyTab = provider.addToWhichPropertyTab();
            if (uiExpandablePane != null && propertyTab == PropertyTab.MOBILE) {
                panel.add(uiExpandablePane);
            }
        }
        this.add(panel, BorderLayout.SOUTH);
    }

    // 手机属性
    private UIExpandablePane getMobilePropertyPane() {
        paramLocationComboBox = getParamLocationComboBox();
        UILabel tipLabel = getTipLabel();
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}};
        if (ExtraReportClassManager.getInstance().getArray(MobileParamStyleProvider.MARK_STRING).isEmpty()) {
            ((WParameterLayout) (paraCreator.toData())).setProvider((MobileParamStyleProvider) ((Item) paramLocationComboBox.getItemAt(0)).getValue());
        }
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Parameter_Panel")), paramLocationComboBox},
                new Component[]{tipLabel, null},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W0, IntervalConstants.INTERVAL_L1);
        JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        jPanel.add(panel);
        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Properties_Mobile"), 280, 20, jPanel);
    }

    private Item[] getItems() {
        Set<MobileParamStyleProvider> pluginCreators =  ExtraReportClassManager.getInstance().getArray(MobileParamStyleProvider.MARK_STRING);
        Item[] items = new Item[pluginCreators.size() + 1];
        MobileParamStyleProvider provider = new DefaultMobileParamStyle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default"));
        items[0] = new Item(provider.descriptor(), provider);
        int i = 1;
        for (MobileParamStyleProvider mobileParamStyleProvider : pluginCreators) {
            items[i++] = new Item(mobileParamStyleProvider.descriptor(), mobileParamStyleProvider);
        }
        return items;
    }

    private UILabel getTipLabel() {
        UILabel tipLabel = new UILabel();
        StringBuilder text = new StringBuilder();
        text.append("<html><font color=gray>").append(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Properties_Mobile_Tip"))
        .append("</font><font color=blue><u>").append(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Properties_Mobile_Install_Parameter_Pane_Plugin"))
        .append("</u></font><font color=gray>").append(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Properties_Mobile_To_Get_More_Style"))
        .append("</font></html>");
        tipLabel.setText(text.toString());
        tipLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(CloudCenter.getInstance().acquireUrlByKind("plugin.mobile.style")));
                } catch (Exception exp) {
                    FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                Object source = e.getSource();
                if (source instanceof UILabel) {
                    ((UILabel) source).setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                Object source = e.getSource();
                if (source instanceof UILabel) {
                    ((UILabel) source).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                Object source = e.getSource();
                if (source instanceof UILabel) {
                    ((UILabel) source).setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
        });
        return tipLabel;
    }

    private UIComboBox getParamLocationComboBox() {
        items = getItems();

        UIComboBox paramLocationComoBox = new UIComboBox(items);
        paramLocationComoBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ((WParameterLayout) (paraCreator.toData())).setProvider((MobileParamStyleProvider) ((Item) e.getItem()).getValue());
                }
            }
        });
        return paramLocationComoBox;
    }

    // 控件顺序
    private UIExpandablePane getMobileWidgetListPane() {
        mobileWidgetListPane = new MobileWidgetListPane(designer, (WSortLayout) paraCreator.toData());
        mobileWidgetListPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(mobileWidgetListPane, BorderLayout.CENTER);

        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Order"), 280, 20, panelWrapper);
    }

    private void bindListeners2Widgets() {
        reInitAllListeners();
        this.changeListener = new AttributeChangeListener() {
            @Override
            public void attributeChange() {
                update();
            }
        };
    }

    /**
     * 后台初始化所有事件.
     */
    private void reInitAllListeners() {
        initListener(this);
    }


    @Override
    public void populate(FormDesigner designer) {
        this.designer = designer;
        // 设置监听
        this.bindListeners2Widgets();
        this.addAttributeChangeListener(changeListener);
        int index = 0;
        try {
            MobileParamStyleProvider provider = ((WParameterLayout) paraCreator.toData()).getProvider();
            String currentQueryType = provider.createJSON().getString("queryType");
            for (int i = 0; i < items.length; i++) {
                String existedQueryType = ((MobileParamStyleProvider) items[i].getValue()).createJSON().getString("queryType");
                if (ComparatorUtils.equals(existedQueryType, currentQueryType)) {
                    index = i;
                    break;
                }
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        paramLocationComboBox.setSelectedIndex(index);

    }

    @Override
    public void update() {
        mobileWidgetListPane.updateToDesigner();
        designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
    }
}
