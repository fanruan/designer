package com.fr.design.widget.ui.designer.layout;

import com.fr.base.io.IOFile;
import com.fr.base.iofile.attr.WatermarkAttr;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteBodyLayout;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.XWScaleLayout;
import com.fr.design.designer.properties.items.FRFitConstraintsItems;
import com.fr.design.designer.properties.items.FRLayoutTypeItems;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelectionUtils;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.widget.accessibles.AccessibleBodyWatermarkEditor;
import com.fr.design.mainframe.widget.accessibles.AccessibleWLayoutBorderStyleEditor;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.design.widget.ui.designer.component.PaddingBoundPane;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WAbsoluteBodyLayout;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WBodyLayoutType;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WSortLayout;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.report.core.ReportUtils;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Created by ibm on 2017/8/2.
 */
public class FRFitLayoutDefinePane extends AbstractFRLayoutDefinePane<WFitLayout> {
    private static final int ADAPT_LABEL_MAX_WIDTH = 80;
    private XWFitLayout xWFitLayout;
    private WFitLayout wFitLayout;
    private UIComboBox layoutComboBox;
    private UIComboBox adaptComboBox;
    private UISpinner componentIntervel;
    private PaddingBoundPane paddingBound;
    private AccessibleWLayoutBorderStyleEditor stylePane;
    private AccessibleBodyWatermarkEditor watermarkEditor;

    public FRFitLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
        this.xWFitLayout = (XWFitLayout) xCreator;
        wFitLayout = xWFitLayout.toData();
        initComponent();
    }


    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel advancePane = createAdvancePane();
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20, advancePane);
        this.add(advanceExpandablePane, BorderLayout.NORTH);
        UIExpandablePane layoutExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout"), 280, 20, createLayoutPane());
        this.add(layoutExpandablePane, BorderLayout.CENTER);
    }

    public JPanel createAdvancePane() {
        JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        stylePane = new AccessibleWLayoutBorderStyleEditor();
        watermarkEditor = new AccessibleBodyWatermarkEditor();
        paddingBound = new PaddingBoundPane();
        JPanel jp2 = TableLayoutHelper.createGapTableLayoutPane(
                new Component[][]{
                        new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style")), stylePane},
                        new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_WaterMark")), watermarkEditor}
                }, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W3, IntervalConstants.INTERVAL_L1);
        jp2.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jPanel.add(paddingBound, BorderLayout.CENTER);
        jPanel.add(jp2, BorderLayout.NORTH);
        return jPanel;
    }

    public JPanel createLayoutPane() {
        JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        layoutComboBox = initUIComboBox(FRLayoutTypeItems.ITEMS);
        adaptComboBox = initUIComboBox(FRFitConstraintsItems.ITEMS);
        JPanel adaptComboBoxPane = UIComponentUtils.wrapWithBorderLayoutPane(adaptComboBox);
        componentIntervel = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        JPanel componentIntervelPane = UIComponentUtils.wrapWithBorderLayoutPane(componentIntervel);

        UILabel adaptLabel = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Component_Scale"));
        UILabel intervalLabel = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Component_Interval"));

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double adaptLabelColumnWidth = adaptLabel.getPreferredSize().width > ADAPT_LABEL_MAX_WIDTH ? ADAPT_LABEL_MAX_WIDTH : p;
        double[] columnSize = {adaptLabelColumnWidth, f};
        int[][] rowCount = {{1, 1}, {1, 1}};
        JPanel northPane = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Attr_Layout_Type")), layoutComboBox}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        northPane.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, 0, 0));



        Component[][] components = new Component[][]{
                new Component[]{adaptLabel, adaptComboBoxPane},
                new Component[]{intervalLabel, componentIntervelPane}
        };
        JPanel centerPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        centerPane.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, IntervalConstants.INTERVAL_L5, 0, 0));
        jPanel.add(northPane, BorderLayout.NORTH);
        jPanel.add(centerPane, BorderLayout.CENTER);
        return jPanel;
    }


    public UIComboBox initUIComboBox(Item[] items) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (Item item : items) {
            model.addElement(item);
        }
        return new UIComboBox(model);
    }


    @Override
    public String title4PopupWindow() {
        return "fitLayout";
    }

    @Override
    public void populateBean(WFitLayout ob) {
        FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        XLayoutContainer rootLayout = selectedBodyLayout(formDesigner);
        if (rootLayout != formDesigner.getRootComponent()
                && formDesigner.getSelectionModel().getSelection().getSelectedCreator() == formDesigner.getRootComponent()) {
            formDesigner.getSelectionModel().setSelectedCreators(
                    FormSelectionUtils.rebuildSelection(xWFitLayout, new Widget[]{selectedBodyLayout(formDesigner).toData()}));

        }
        paddingBound.populate(ob);
        layoutComboBox.setSelectedIndex(ob.getBodyLayoutType().getTypeValue());
        adaptComboBox.setSelectedIndex(ob.getCompState());
        componentIntervel.setValue(ob.getCompInterval());
        stylePane.setValue(ob.getBorderStyle());
        watermarkEditor.setValue(ReportUtils.getWatermarkAttrFromTemplate(getCurrentIOFile()));
    }

    private XLayoutContainer selectedBodyLayout(FormDesigner formDesigner) {
        XLayoutContainer rootLayout = formDesigner.getRootComponent();
        if (rootLayout.getComponentCount() == 1 && rootLayout.getXCreator(0).acceptType(XWAbsoluteBodyLayout.class)) {
            rootLayout = (XWAbsoluteBodyLayout) rootLayout.getXCreator(0);
        }
        return rootLayout;
    }


    @Override
    public WFitLayout updateBean() {
        WFitLayout layout = (WFitLayout) creator.toData();
        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_Padding_Duplicate"))) {
            paddingBound.update(layout);
        }
        LayoutBorderStyle borderStyle = (LayoutBorderStyle) stylePane.getValue();
        if (borderStyle != null) {
            layout.setBorderStyle(borderStyle);
        }
        updateWatermark();
        Item item = (Item) layoutComboBox.getSelectedItem();
        Object value = item.getValue();
        int state = 0;
        if (value instanceof Integer) {
            state = (Integer) value;
        }
        //todo 验证下
        layout.setLayoutType(WBodyLayoutType.parse(state));
        layout.setCompState(adaptComboBox.getSelectedIndex());
        try {
            if (state == WBodyLayoutType.ABSOLUTE.getTypeValue()) {
                WAbsoluteBodyLayout wAbsoluteBodyLayout = new WAbsoluteBodyLayout("body");
                wAbsoluteBodyLayout.setCompState(WAbsoluteLayout.STATE_FIXED);
                Component[] components = xWFitLayout.getComponents();
                xWFitLayout.removeAll();
                layout.resetStyle();
                XWAbsoluteBodyLayout xwAbsoluteBodyLayout = xWFitLayout.getBackupParent() == null ? new XWAbsoluteBodyLayout(wAbsoluteBodyLayout, new Dimension(0, 0)) : (XWAbsoluteBodyLayout) xWFitLayout.getBackupParent();
                xWFitLayout.getLayoutAdapter().addBean(xwAbsoluteBodyLayout, 0, 0);
                for (Component component : components) {
                    XCreator xCreator = (XCreator) component;
                    //部分控件被ScaleLayout包裹着，绝对布局里面要放出来
                    if (xCreator.acceptType(XWScaleLayout.class)) {
                        if (xCreator.getComponentCount() > 0 && ((XCreator) xCreator.getComponent(0)).shouldScaleCreator()) {
                            component = xCreator.getComponent(0);
                            component.setBounds(xCreator.getBounds());
                        }
                    }
                    xwAbsoluteBodyLayout.add(component);
                }
                copyLayoutAttr(wFitLayout, wAbsoluteBodyLayout);
                xWFitLayout.setBackupParent(xwAbsoluteBodyLayout);
                FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
                formDesigner.getSelectionModel().setSelectedCreators(
                        FormSelectionUtils.rebuildSelection(xWFitLayout, new Widget[]{wAbsoluteBodyLayout}));
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);

        }

        int intervelValue = (int) componentIntervel.getValue();
        if (xWFitLayout.canAddInterval(intervelValue)) {
//             设置完间隔后，要同步处理界面组件，容器刷新后显示出对应效果
            setLayoutGap(intervelValue);
        }

        return layout;
    }

    private void updateWatermark() {
        WatermarkAttr watermark = (WatermarkAttr) watermarkEditor.getValue();
        if (watermark != null) {
            IOFile ioFile = getCurrentIOFile();
            ioFile.addAttrMark(watermark);
        }
    }

    private IOFile getCurrentIOFile() {
        return WidgetPropertyPane.getInstance().getEditingFormDesigner().getTarget();
    }

    private void setLayoutGap(int value) {
        int interval = wFitLayout.getCompInterval();
        if (value != interval) {
            xWFitLayout.moveContainerMargin();
            xWFitLayout.moveCompInterval(xWFitLayout.getAcualInterval());
            wFitLayout.setCompInterval(value);
            xWFitLayout.addCompInterval(xWFitLayout.getAcualInterval());
        }
    }

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }

}
