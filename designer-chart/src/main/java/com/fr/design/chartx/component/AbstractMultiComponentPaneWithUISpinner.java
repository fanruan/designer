package com.fr.design.chartx.component;

import com.fr.chartx.data.field.ColumnField;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.stable.AssistUtils;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by shine on 2019/6/18.
 * 一列组件<T extends JComponent> 可增可删，通过UISpinner增删。
 */
public abstract class AbstractMultiComponentPaneWithUISpinner<T extends JComponent> extends JPanel {

    private UISpinner levelNumSpinner;

    private List<T> levelComponentList = new ArrayList<T>();

    private JPanel levelPane;

    private int currentNum = 3;

    public AbstractMultiComponentPaneWithUISpinner() {
        initComps();
    }

    public List<T> getComponentList() {
        return levelComponentList;
    }

    protected abstract T createJComponent();

    protected abstract void populateField(T component, ColumnField field);

    protected abstract void updateField(T component, ColumnField field);

    protected void initComps() {

        this.setLayout(new BorderLayout(0, 6));

        levelNumSpinner = new UISpinner(1, 15, 1, currentNum) {
            @Override
            protected void fireStateChanged() {
                //先处理自身的空间布局
                refreshLevelPane();
                //然后更新数据
                super.fireStateChanged();
            }

            @Override
            public void setTextFieldValue(double value) {
                //如果为0，则没有改变值
                if (AssistUtils.equals(value,0)) {
                    return;
                }
                super.setTextFieldValue(value);
            }
        };

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Level_Number")), levelNumSpinner},
        };

        JPanel northPane = TableLayoutHelper.createGapTableLayoutPane(components, new double[]{TableLayout.PREFERRED}, new double[]{ChartDataPane.LABEL_WIDTH, 122}, 0, 6);

        this.add(northPane, BorderLayout.NORTH);

        initLevelPane();
    }

    private void initLevelPane() {
        double[] rows = new double[currentNum];

        Component[][] components = new Component[currentNum][2];

        List<T> newList = new ArrayList<T>();

        int maxSize = levelComponentList.size();
        for (int i = 0; i < currentNum; i++) {
            rows[i] = TableLayout.PREFERRED;
            T component = i < maxSize ? levelComponentList.get(i) : createJComponent();
            newList.add(component);

            components[i] = new Component[]{
                    new UILabel(Toolkit.i18nText("Fine-Design_Chart_Level") + String.valueOf(i + 1)),
                    component
            };
        }

        levelComponentList = newList;

        levelPane = TableLayoutHelper.createGapTableLayoutPane(components, rows, new double[]{ChartDataPane.LABEL_WIDTH, 122}, 0, 6);

        this.add(levelPane, BorderLayout.CENTER);
    }

    private void refreshLevelPane() {
        if (levelNumSpinner == null) {
            return;
        }

        int newNum = (int) levelNumSpinner.getValue();

        if (newNum != currentNum) {
            currentNum = newNum;
            this.remove(levelPane);
            this.initLevelPane();
        }

        refreshPane();
    }

    private void refreshPane() {
        this.validate();
        this.repaint();
        this.revalidate();
    }

    public void populate(List<ColumnField> categoryList) {
        int len = categoryList.size();
        levelNumSpinner.setValue(len);

        refreshLevelPane();

        for (int i = 0; i < len; i++) {
            ColumnField columnField = categoryList.get(i);
            T component = levelComponentList.get(i);
            populateField(component, columnField);
        }
    }

    public void update(List<ColumnField> categoryList) {
        categoryList.clear();

        for (T comboBox : levelComponentList) {
            ColumnField temp = new ColumnField();
            categoryList.add(temp);
            updateField(comboBox, temp);
        }
    }

    public void checkEnable(boolean hasUse){
        levelNumSpinner.setEnabled(hasUse);
    }
}
