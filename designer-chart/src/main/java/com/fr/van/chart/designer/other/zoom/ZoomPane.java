package com.fr.van.chart.designer.other.zoom;

import com.fr.chartx.attr.ZoomAttribute;
import com.fr.chartx.attr.ZoomInitialDisplayType;
import com.fr.chartx.attr.ZoomModeType;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.extended.chart.StringFormula;
import com.fr.general.ComparatorUtils;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Created by shine on 2019/08/28.
 */
public class ZoomPane extends BasicBeanPane<ZoomAttribute> {

    private UIButtonGroup<ZoomModeType> modeTypeButtonGroup;

    private JPanel customModePane;
    private UIComboBox initialDisplayTypeComboBox;
    private JPanel initialDisplayCardPane;
    private UISpinner topCategorySpinner;
    private TinyFormulaPane leftFormulaPane;
    private TinyFormulaPane rightFormulaPane;

    private UIButtonGroup<Boolean> selectionZoomGroup;

    public ZoomPane() {
        initComponent();
    }

    private void initComponent() {

        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH;
        double p = TableLayout.PREFERRED;
        double[] columnSize = {f, e};

        JPanel northPane = createNorthPane(f, p);

        initCustomModePane(columnSize, p);

        JPanel southPane = createSouthPane(f, p);

        this.setLayout(new BorderLayout(0, 6));

        if (northPane != null) {
            this.add(northPane, BorderLayout.NORTH);
        }
        if (customModePane != null) {
            this.add(customModePane, BorderLayout.CENTER);
        }
        if (southPane != null) {
            this.add(southPane, BorderLayout.SOUTH);
        }
    }

    protected JPanel createNorthPane(double f, double p) {
        modeTypeButtonGroup = new UIButtonGroup<ZoomModeType>(new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Mode_Auto"),
                Toolkit.i18nText("Fine-Design_Chart_Mode_Custom"),
                Toolkit.i18nText("Fine-Design_Chart_Close")
        }, new ZoomModeType[]{ZoomModeType.AUTO, ZoomModeType.CUSTOM, ZoomModeType.CLOSE});

        modeTypeButtonGroup.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkCustomModePane();
            }
        });
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Zoom_Mode_type")), modeTypeButtonGroup}
        };
        return TableLayout4VanChartHelper.createGapTableLayoutPane(components, new double[]{p}
                , new double[]{f, TableLayout4VanChartHelper.EDIT_AREA_WIDTH});
    }

    private JPanel createSouthPane(double f, double p) {

        Component[][] components = getSouthComps();

        double[] rows = new double[components.length];
        Arrays.fill(rows, p);

        return TableLayout4VanChartHelper.createGapTableLayoutPane(components, rows
                , new double[]{f, TableLayout4VanChartHelper.EDIT_AREA_WIDTH});
    }

    protected Component[][] getSouthComps() {
        selectionZoomGroup = new UIButtonGroup<Boolean>(new String[]{Toolkit.i18nText("Fine-Design_Chart_Open")
                , Toolkit.i18nText("Fine-Design_Chart_Close")}, new Boolean[]{true, false});

        return new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Selection_Zoom")), selectionZoomGroup}
        };
    }

    protected void initCustomModePane(double[] columnSize, double p) {
        initialDisplayTypeComboBox = new UIComboBox(new ZoomInitialDisplayType[]{
                ZoomInitialDisplayType.TOP_CATEGORY,
                ZoomInitialDisplayType.LEFT_RIGHT_BOUNDARY});

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_InitialDisplay")), initialDisplayTypeComboBox}
        };
        JPanel northPane = TableLayout4VanChartHelper.createGapTableLayoutPane(components, new double[]{p}, columnSize);


        initInitialDisplayCardPane(columnSize, p);

        customModePane = new JPanel(new BorderLayout(0, 6));
        customModePane.add(northPane, BorderLayout.NORTH);
        customModePane.add(initialDisplayCardPane, BorderLayout.CENTER);
        customModePane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
    }

    private void initInitialDisplayCardPane(double[] columnSize, double p) {
        topCategorySpinner = new UISpinner(1, Integer.MAX_VALUE, 1);
        Component[][] components1 = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Category_Number")), topCategorySpinner}
        };
        final JPanel topPane = TableLayout4VanChartHelper.createGapTableLayoutPane(components1, new double[]{p}, columnSize);


        leftFormulaPane = new TinyFormulaPane();
        rightFormulaPane = new TinyFormulaPane();
        Component[][] components2 = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Left_Boundary")), leftFormulaPane},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Right_Boundary")), rightFormulaPane}
        };
        final JPanel leftRightPane = TableLayout4VanChartHelper.createGapTableLayoutPane(components2, new double[]{p, p}, columnSize);

        initialDisplayCardPane = new JPanel(new CardLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return initialDisplayTypeComboBox.getSelectedIndex() == 0 ? topPane.getPreferredSize() : leftRightPane.getPreferredSize();
            }
        };
        initialDisplayCardPane.add(topPane, ZoomInitialDisplayType.TOP_CATEGORY.toString());
        initialDisplayCardPane.add(leftRightPane, ZoomInitialDisplayType.LEFT_RIGHT_BOUNDARY.toString());
        initialDisplayTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkInitialDisplayCardPane();
            }
        });
    }

    private void checkCustomModePane() {
        if (customModePane == null){
            return;
        }
        customModePane.setVisible(modeTypeButtonGroup.getSelectedItem() == ZoomModeType.CUSTOM);
    }

    private void checkInitialDisplayCardPane() {
        if (initialDisplayCardPane == null){
            return;
        }

        CardLayout cardLayout = (CardLayout) initialDisplayCardPane.getLayout();
        if (ComparatorUtils.equals(initialDisplayTypeComboBox.getSelectedItem(), ZoomInitialDisplayType.TOP_CATEGORY)) {
            cardLayout.show(initialDisplayCardPane, ZoomInitialDisplayType.TOP_CATEGORY.toString());
        } else {
            cardLayout.show(initialDisplayCardPane, ZoomInitialDisplayType.LEFT_RIGHT_BOUNDARY.toString());
        }
    }


    @Override
    public void populateBean(ZoomAttribute ob) {
        if (modeTypeButtonGroup != null) {
            modeTypeButtonGroup.setSelectedItem(ob.getModeType());
        }

        if (initialDisplayTypeComboBox != null){
            initialDisplayTypeComboBox.setSelectedItem(ob.getInitialDisplayType());
        }

        if (topCategorySpinner != null) {
            topCategorySpinner.setValue(ob.getTopCategory());
        }

        if (ob.getLeft() != null && leftFormulaPane != null) {
            leftFormulaPane.populateBean(ob.getLeft().getContent());
        }
        if (ob.getRight() != null && rightFormulaPane != null) {
            rightFormulaPane.populateBean(ob.getRight().getContent());
        }

        if (selectionZoomGroup != null) {
            selectionZoomGroup.setSelectedItem(ob.isSelectionZoom());
        }

        checkInitialDisplayCardPane();
        checkCustomModePane();
    }

    @Override
    public ZoomAttribute updateBean() {
        ZoomAttribute zoomAttribute = new ZoomAttribute();

        if (modeTypeButtonGroup != null) {
            zoomAttribute.setModeType(modeTypeButtonGroup.getSelectedItem());
        }

        if (initialDisplayTypeComboBox != null) {
            zoomAttribute.setInitialDisplayType((ZoomInitialDisplayType) initialDisplayTypeComboBox.getSelectedItem());
        }

        if (topCategorySpinner != null){
            zoomAttribute.setTopCategory((int) topCategorySpinner.getValue());
        }

        if (leftFormulaPane != null){
            zoomAttribute.setLeft(new StringFormula(leftFormulaPane.updateBean()));
        }

        if (rightFormulaPane != null){
            zoomAttribute.setRight(new StringFormula(rightFormulaPane.updateBean()));
        }

        if (selectionZoomGroup != null) {
            zoomAttribute.setSelectionZoom(selectionZoomGroup.getSelectedItem());
        }

        return zoomAttribute;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
