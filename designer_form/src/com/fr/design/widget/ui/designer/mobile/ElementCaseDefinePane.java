package com.fr.design.widget.ui.designer.mobile;

import com.fr.base.mobile.MobileFitAttrState;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * 报表块-移动端属性面板
 *
 * Created by fanglei on 2017/8/8.
 */
public class ElementCaseDefinePane extends MobileWidgetDefinePane{
    private static final String[] ITEMS = {
            MobileFitAttrState.HORIZONTAL.description(),
            MobileFitAttrState.VERTICAL.description(),
            MobileFitAttrState.BIDIRECTIONAL.description(),
            MobileFitAttrState.NONE.description()
    };

    private XCreator xCreator; // 当前选中控件的xCreator
    private FormDesigner designer; // 当前设计器
    private UIComboBox hComboBox; // 横屏下拉框
    private UIComboBox vComboBox;// 竖屏下拉框
    private UICheckBox heightRestrictCheckBox; // 手机显示限制高度复选框
    private UILabel maxHeightLabel;
    private UISpinner maxHeightSpinner; // 最大高度Spinner

    public ElementCaseDefinePane (XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    protected String title4PopupWindow() {
        return "ElementCase";
    }


    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        this.hComboBox = new UIComboBox(ITEMS);
        this.vComboBox = new UIComboBox(ITEMS);
        this.heightRestrictCheckBox = new UICheckBox(Inter.getLocText("Form-EC_heightrestrict"));
        this.maxHeightLabel = new UILabel(Inter.getLocText("Form-EC_heightpercent"), SwingConstants.LEFT);
        this.maxHeightSpinner = new UISpinner(0, 1, 0.01, 0.75);
        maxHeightSpinner.setVisible(false);
        maxHeightLabel.setVisible(false);

        Component[][] components = new Component[][]{
                new Component[] {new UILabel(Inter.getLocText("FR-Designer_Mobile-Horizontal"), SwingConstants.LEFT), hComboBox},
                new Component[] {new UILabel(Inter.getLocText("FR-Designer_Mobile-Vertical"), SwingConstants.LEFT), vComboBox},
                new Component[] {heightRestrictCheckBox, null},
                new Component[] {maxHeightLabel, maxHeightSpinner}
        };
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}};
        final JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 30, LayoutConstants.VGAP_LARGE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        final JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);
        UIExpandablePane folderPane = new UIExpandablePane(Inter.getLocText("FR-Designer_Fit"), 280, 20, panelWrapper);
        this.add(folderPane, BorderLayout.NORTH);
        this.bingListeners2Widgets();
        this.repaint();
    }

    public void bingListeners2Widgets() {
        this.hComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((ElementCaseEditor)xCreator.toData()).setHorziontalAttr(MobileFitAttrState.parse(hComboBox.getSelectedIndex() + 1));
            }
        });

        this.vComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((ElementCaseEditor)xCreator.toData()).setVerticalAttr(MobileFitAttrState.parse(vComboBox.getSelectedIndex() + 1));
            }
        });

        this.heightRestrictCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isHeightRestrict = heightRestrictCheckBox.isSelected();
                ((ElementCaseEditor)xCreator.toData()).setHeightRestrict(isHeightRestrict);
                maxHeightSpinner.setVisible(isHeightRestrict);
                maxHeightLabel.setVisible(isHeightRestrict);
            }
        });

        this.maxHeightSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ((ElementCaseEditor)xCreator.toData()).setHeightPercent(maxHeightSpinner.getValue());
            }
        });
    }
}
