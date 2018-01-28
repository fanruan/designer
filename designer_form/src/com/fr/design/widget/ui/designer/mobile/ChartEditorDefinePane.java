package com.fr.design.widget.ui.designer.mobile;

import com.fr.base.mobile.ChartMobileFitAttrState;
import com.fr.base.mobile.ChartMobileFitAttrStateProvider;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.form.ui.BaseChartEditor;
import com.fr.form.ui.container.WFitLayout;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by plough on 2018/1/18.
 */
public class ChartEditorDefinePane extends MobileWidgetDefinePane{
    private static final Item[] ITEMS = {
            new Item(ChartMobileFitAttrState.AUTO.description(), ChartMobileFitAttrState.AUTO),
            new Item(ChartMobileFitAttrState.AREA.description(), ChartMobileFitAttrState.AREA),
            new Item(ChartMobileFitAttrState.PROPORTION.description(), ChartMobileFitAttrState.PROPORTION)
    };

    private XCreator xCreator; // 当前选中控件的xCreator
    private FormDesigner designer; // 当前设计器
    private UIComboBox zoomOutComboBox;// 缩小逻辑下拉框
    private AttributeChangeListener changeListener;
    private UILabel tipLabel;
    private boolean isPopulating = false;

    public ChartEditorDefinePane (XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    protected void initContentPane() {}

    @Override
    protected JPanel createContentPane() {
        return null;
    }

    @Override
    public String getIconPath() {
        return "";
    }

    @Override
    public String title4PopupWindow() {
        return "ChartEditor";
    }


    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();

        if (((WFitLayout)designer.getRootComponent().toData()).isAppRelayout()) {  // 如果开启了手机重布局
            this.add(getMobileSettingsPane(), BorderLayout.NORTH);
            this.bingListeners2Widgets();
            this.addAttributeChangeListener(changeListener);
        } else {
            this.add(getUnavailableTipPane(), BorderLayout.NORTH);
        }

        this.repaint();
    }

    private JPanel getUnavailableTipPane() {
        JPanel panel = new JPanel(new BorderLayout());
        UILabel unavailableTipLabel = new UILabel();
        unavailableTipLabel.setText("<html>" + Inter.getLocText("FR-Designer_Tip_Chart_Adaptivity_Unavailable") + "<html>");
        unavailableTipLabel.setForeground(Color.gray);
        panel.add(unavailableTipLabel, BorderLayout.NORTH);
        return panel;
    }

    private UIExpandablePane getMobileSettingsPane() {
        initZoomOutComboBox();

        tipLabel = new UILabel();
        tipLabel.setForeground(Color.gray);
        updateTipLabel();

        Component[][] components = new Component[][]{
                new Component[] {new UILabel(Inter.getLocText("FR-Designer_Zoom_In_Logic"), SwingConstants.LEFT), new UILabel(ChartMobileFitAttrState.PROPORTION.description())},
                new Component[] {new UILabel(Inter.getLocText("FR-Designer_Zoom_Out_Logic"), SwingConstants.LEFT), zoomOutComboBox},
                new Component[] {tipLabel, null}
        };

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        final JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 30, LayoutConstants.VGAP_LARGE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        final JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);

        return new UIExpandablePane(Inter.getLocText("FR-Designer_Chart_Adaptivity"), 280, 20, panelWrapper);
    }

    private void initZoomOutComboBox() {
        this.zoomOutComboBox = new UIComboBox(ITEMS);

        BaseChartEditor chartEditor = (BaseChartEditor)xCreator.toData();
        ChartMobileFitAttrStateProvider zoomOutAttr = chartEditor.getMobileAttr().getZoomOutAttr();
        this.zoomOutComboBox.setSelectedItem(new Item(zoomOutAttr.description(), zoomOutAttr));

        this.zoomOutComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateTipLabel();
            }
        });
    }


    private void updateTipLabel() {
        ChartMobileFitAttrState fitAttrState = (ChartMobileFitAttrState) ((Item)zoomOutComboBox.getSelectedItem()).getValue();
        // 使用 html，可以自动换行
        tipLabel.setText("<html>" + fitAttrState.tip() + "</html>");
    }

    private void bingListeners2Widgets() {
        reInitAllListeners();
        this.changeListener = new AttributeChangeListener() {
            @Override
            public void attributeChange() {
                if (isPopulating) {
                    return;
                }
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
        // 感觉 populate 方法没啥用。可以直接在 initPropertyGroups 中更新界面
    }

    @Override
    public void update() {
        ((BaseChartEditor)xCreator.toData()).getMobileAttr().setZoomInAttr(ChartMobileFitAttrState.PROPORTION);
        ((BaseChartEditor)xCreator.toData()).getMobileAttr().setZoomOutAttr((ChartMobileFitAttrState)((Item)zoomOutComboBox.getSelectedItem()).getValue());
        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified(); // 触发设计器保存按钮亮起来
    }
}
