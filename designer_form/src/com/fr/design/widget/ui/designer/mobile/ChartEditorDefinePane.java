package com.fr.design.widget.ui.designer.mobile;

import com.fr.base.mobile.ChartMobileFitAttrState;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.form.ui.ChartEditor;
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
    private UILabel maxHeightLabel;
    private UISpinner maxHeightSpinner; // 最大高度Spinner
    private AttributeChangeListener changeListener;
    private UILabel tipLabel;

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
//        this.hComboBox = new UIComboBox(ITEMS);
        this.zoomOutComboBox = new UIComboBox(ITEMS);
        this.zoomOutComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateTipLabel();
            }
        });

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
        UIExpandablePane folderPane = new UIExpandablePane(Inter.getLocText("FR-Designer_Chart_Adaptivity"), 280, 20, panelWrapper);
        this.add(folderPane, BorderLayout.NORTH);
        this.bingListeners2Widgets();
        this.setGlobalNames();
        this.repaint();
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
        this.addAttributeChangeListener(changeListener);
        ChartEditor chartEditor = (ChartEditor)xCreator.toData();
//        this.zoomOutComboBox.setSelectedIndex(0);
//        this.hComboBox.setSelectedItem(new Item (elementCaseEditor.getHorziontalAttr().description(), elementCaseEditor.getHorziontalAttr()));
//        this.vComboBox.setSelectedItem(new Item (elementCaseEditor.getVerticalAttr().description(), elementCaseEditor.getVerticalAttr()));
//        this.heightRestrictCheckBox.setSelected(elementCaseEditor.isHeightRestrict());
//        this.maxHeightLabel.setVisible();
//        this.maxHeightSpinner.setVisible(elementCaseEditor.isHeightRestrict());
//        this.maxHeightSpinner.setValue(elementCaseEditor.getHeightPercent());
    }

    @Override
    public void update() {
//        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified(); // 触发设计器保存按钮亮起来
//        String globalName = this.getGlobalName();
//        switch (globalName) {
//            case "hComboBox":
////                ((ChartEditor)xCreator.toData()).setHorziontalAttr(((MobileFitAttrState)((Item)hComboBox.getSelectedItem()).getValue()));
//                break;
//            case "vComboBox":
////                ((ChartEditor)xCreator.toData()).setVerticalAttr(((MobileFitAttrState)((Item)vComboBox.getSelectedItem()).getValue()));
//                break;
//            case "heightRestrictCheckBox":
//                boolean isHeightRestrict = heightRestrictCheckBox.isSelected();
////                ((ChartEditor)xCreator.toData()).setHeightRestrict(isHeightRestrict);
//                maxHeightSpinner.setVisible(isHeightRestrict);
//                maxHeightLabel.setVisible(isHeightRestrict);
//                break;
//            case "maxHeightSpinner":
////                ((ChartEditor)xCreator.toData()).setHeightPercent(maxHeightSpinner.getValue());
//                break;
//        }
    }

    private void setGlobalNames() {
//        this.hComboBox.setGlobalName("hComboBox");
//        this.vComboBox.setGlobalName("vComboBox");
//        this.heightRestrictCheckBox.setGlobalName("heightRestrictCheckBox");
//        this.maxHeightSpinner.setGlobalName("maxHeightSpinner");
    }

}
