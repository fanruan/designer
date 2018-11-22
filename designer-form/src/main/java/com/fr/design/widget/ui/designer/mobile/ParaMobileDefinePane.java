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
import com.fr.general.ComparatorUtils;
import com.fr.general.SiteCenter;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.report.ExtraReportClassManager;
import com.fr.report.fun.LocationAttrProvider;
import com.fr.report.fun.impl.AbstractLocationAttrProvider;



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
        if (ExtraReportClassManager.getInstance().getArray(LocationAttrProvider.MARK_STRING).size() != 0) {
            tipLabel = null;
        } else {
            ((WParameterLayout) (paraCreator.toData())).setProvider((LocationAttrProvider) ((Item) paramLocationComboBox.getItemAt(0)).getValue());
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
        Set<LocationAttrProvider> pluginCreators =  ExtraReportClassManager.getInstance().getArray(LocationAttrProvider.MARK_STRING);
        Item[] items = new Item[pluginCreators.size() + 1];
        LocationAttrProvider provider = getDefaultLocationAttr();
        items[0] = new Item(provider.descriptor(), provider);
        for (int i = 0; i < pluginCreators.size(); i++) {
            provider = pluginCreators.iterator().next();
            items[i + 1] = new Item(provider.descriptor(), provider);
        }
        return items;
    }

    private UILabel getTipLabel() {
        UILabel tipLabel = new UILabel();
        String[] strings = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Properties_Mobile_Tip").split("\\|");
        StringBuilder text = new StringBuilder();
        text.append("<html><font color=gray>").append(strings[0])
        .append("</font><font color=blue><u>").append(strings[1])
        .append("</u></font><font color=gray>").append(strings[2])
        .append("</font></html>");
        tipLabel.setText(text.toString());
        tipLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    //todo 添加对应插件下载地址
                    Desktop.getDesktop().browse(new URI(SiteCenter.getInstance().acquireUrlByKind("plugin.download")));
                } catch (Exception exp) {

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
                    ((WParameterLayout) (paraCreator.toData())).setProvider((LocationAttrProvider) ((Item) e.getItem()).getValue());
                }
            }
        });
        return paramLocationComoBox;
    }

    private LocationAttrProvider getDefaultLocationAttr() {
        return new AbstractLocationAttrProvider() {
            @Override
            public String descriptor() {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Default");
            }

            @Override
            public JSONObject createJSON() {
                JSONObject jo = JSONObject.create();
                try {
                    jo.put("queryType", "default");
                } catch (JSONException e) {

                }
                return jo;
            }
        };
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
            LocationAttrProvider provider = ((WParameterLayout) paraCreator.toData()).getProvider();
            String currentQueryType = provider.createJSON().getString("queryType");
            for (int i = 0; i < items.length; i++) {
                String existedQueryType = ((LocationAttrProvider) items[i].getValue()).createJSON().getString("queryType");
                if (ComparatorUtils.equals(existedQueryType, currentQueryType)) {
                    index = i;
                    break;
                }
            }
        } catch (JSONException e) {
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
