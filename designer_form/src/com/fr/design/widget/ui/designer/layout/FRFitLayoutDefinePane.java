package com.fr.design.widget.ui.designer.layout;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
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
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.design.widget.ui.designer.component.PaddingBoundPane;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WAbsoluteBodyLayout;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WBodyLayoutType;
import com.fr.form.ui.container.WFitLayout;
import com.fr.general.FRLogger;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/2.
 */
public class FRFitLayoutDefinePane extends AbstractDataModify<WFitLayout> {
    private XWFitLayout xWFitLayout;
    private WFitLayout wFitLayout;
    private UIComboBox layoutComboBox;
    private UIComboBox adaptComboBox;
    private UISpinner componentIntervel;
    private PaddingBoundPane paddingBound;

    public FRFitLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
        this.xWFitLayout = (XWFitLayout) xCreator;
        wFitLayout = xWFitLayout.toData();
        initComponent();
    }


    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel advancePane = createAdvancePane();
        UIExpandablePane advanceExpandablePane = new UIExpandablePane("高级", 280, 20, advancePane);
        this.add(advanceExpandablePane, BorderLayout.NORTH);
        UIExpandablePane layoutExpandablePane = new UIExpandablePane("布局", 280, 20, createLayoutPane());
        this.add(layoutExpandablePane, BorderLayout.CENTER);
    }

    public JPanel createAdvancePane() {
        JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        paddingBound = new PaddingBoundPane(creator);
        jPanel.add(paddingBound, BorderLayout.CENTER);
        return jPanel;
    }

    public JPanel createLayoutPane() {
        JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        layoutComboBox = initUIComboBox(FRLayoutTypeItems.ITEMS);
        adaptComboBox = initUIComboBox(FRFitConstraintsItems.ITEMS);
        componentIntervel = new UISpinner(0, 100, 1);
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p,p,p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel("布局方式"), layoutComboBox},
                new Component[]{new UILabel("组件缩放"), adaptComboBox},
                new Component[]{new UILabel("组件间隔"), componentIntervel}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 20, 7);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel.add(panel, BorderLayout.CENTER);

        return jPanel;
    }


    public UIComboBox initUIComboBox(Item [] items) {
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

    }


    @Override
    public WFitLayout updateBean() {
        WFitLayout layout = (WFitLayout)creator.toData();
        Item item = (Item) layoutComboBox.getSelectedItem();
        Object value = item.getValue();
        int state = 0;
        if (value instanceof Integer) {
            state = (Integer) value;
        }
        try {
            if (state == WBodyLayoutType.ABSOLUTE.getTypeValue()) {
                WAbsoluteBodyLayout wAbsoluteBodyLayout = new WAbsoluteBodyLayout("body");
                wAbsoluteBodyLayout.setCompState(WAbsoluteLayout.STATE_FIXED);
                Component[] components = xWFitLayout.getComponents();
                xWFitLayout.removeAll();
                XWAbsoluteBodyLayout xwAbsoluteBodyLayout = new XWAbsoluteBodyLayout(wAbsoluteBodyLayout, new Dimension(0, 0));
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
                FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
                formDesigner.getSelectionModel().setSelectedCreators(
                        FormSelectionUtils.rebuildSelection(xWFitLayout, new Widget[]{wAbsoluteBodyLayout}));
            } else {
                FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
                formDesigner.getSelectionModel().setSelectedCreators(
                        FormSelectionUtils.rebuildSelection(xWFitLayout, new Widget[]{xWFitLayout.toData()}));
            }
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());

        }
        layout.setLayoutType(WBodyLayoutType.parse(state));

        return layout;
    }

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }

}
