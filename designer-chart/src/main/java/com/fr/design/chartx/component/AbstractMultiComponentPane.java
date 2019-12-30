package com.fr.design.chartx.component;

import com.fr.chartx.data.field.ColumnField;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.general.IOUtils;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by shine on 2019/4/10.
 * 一列组件<T extends JComponent> 可增可删，通过JComponent后面的加减button增删。
 */
public abstract class AbstractMultiComponentPane<T extends JComponent> extends JPanel {
    private static final int H = 20;
    private static final int ICON_W = 20;

    private JPanel boxPane;
    private UIButton addButton;

    private T firstFieldComponent;

    private List<T> categoryComponentList = new ArrayList<T>();

    private boolean categoryAxis = true;

    protected abstract T createFirstFieldComponent();

    protected abstract T createOtherFieldComponent();

    protected abstract void populateField(T component, ColumnField field);

    protected abstract void updateField(T component, ColumnField field);

    public void setCategoryAxis(boolean categoryAxis) {
        this.categoryAxis = categoryAxis;
        if(!categoryAxis){
            addButton.setEnabled(false);
            for (JComponent component : categoryComponentList) {
                component.setEnabled(false);
            }
        }
    }

    public AbstractMultiComponentPane() {

        UILabel label = new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Category"));
        label.setPreferredSize(new Dimension(ChartDataPane.LABEL_WIDTH, ChartDataPane.LABEL_HEIGHT));

        firstFieldComponent = createFirstFieldComponent();
        firstFieldComponent.setPreferredSize(new Dimension(componentWidth(), H));

        addButton = new UIButton(IOUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        addButton.setPreferredSize(new Dimension(ICON_W, H));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canAdd()) {
                    addNewComboBox();
                }
            }
        });

        final JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 2));

        panel.add(label);
        panel.add(firstFieldComponent);
        panel.add(addButton);

        boxPane = new JPanel();
        boxPane.setLayout(new BoxLayout(boxPane, BoxLayout.Y_AXIS));

        this.setLayout(new BorderLayout(4, 0));
        this.add(panel, BorderLayout.NORTH);
        this.add(boxPane, BorderLayout.CENTER);
    }

    protected int componentWidth() {
        return 96;
    }

    private JPanel addComboBoxAndButtonToBox(T uiComboBox, UIButton uiButton) {
        final JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 2));

        panel.add(uiComboBox);
        panel.add(uiButton);

        boxPane.add(panel);
        categoryComponentList.add(uiComboBox);

        return panel;
    }

    private void addNewComboBox() {
        final T comboBox = createOtherFieldComponent();
        comboBox.setPreferredSize(new Dimension(componentWidth(), H));

        UIButton delButton = new UIButton(IOUtils.readIcon("com/fr/design/images/toolbarbtn/close.png"));
        delButton.setPreferredSize(new Dimension(ICON_W, H));

        final JPanel panel = addComboBoxAndButtonToBox(comboBox, delButton);

        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boxPane.remove(panel);
                categoryComponentList.remove(comboBox);
                checkAddButton();
                relayoutPane();
            }
        });

        relayoutPane();
        checkAddButton();
    }

    private void relayoutPane() {
        this.revalidate();
    }

    private void checkAddButton() {
        addButton.setEnabled(canAdd());
    }

    private boolean canAdd() {
        return categoryComponentList.size() < 2 && categoryAxis;
    }

    public List<T> componentList() {
        List<T> list = new ArrayList<>(categoryComponentList);
        list.add(firstFieldComponent);
        return list;
    }

    public void populate(List<ColumnField> categoryList) {
        int comboBoxSize = categoryComponentList.size(),
                len = categoryList.size();

        if (len > 0) {
            populateField(firstFieldComponent, categoryList.get(0));
        }

        for (int i = 1; i < len; i++) {
            if (i > comboBoxSize) {
                addNewComboBox();
            }
            T comboBox = categoryComponentList.get(i - 1);
            populateField(comboBox, categoryList.get(i));
        }

        checkAddButton();
        relayoutPane();
    }

    public void update(List<ColumnField> categoryList) {
        categoryList.clear();

        ColumnField temp1 = new ColumnField();
        categoryList.add(temp1);
        updateField(firstFieldComponent, temp1);

        for (T comboBox : categoryComponentList) {
            ColumnField temp = new ColumnField();
            categoryList.add(temp);
            updateField(comboBox, temp);
        }
    }

    public void checkEnable(boolean hasUse) {
        //增加按钮是否灰化要根据是否选择了数据源，是否分类轴，分类数量是否超标三个判断
        boolean buttonUse = hasUse && categoryAxis && categoryComponentList.size() < 2;
        //额外的分类是否灰化根据是否选择了数据源，是否分类轴判断
        boolean categoryUse = hasUse && categoryAxis;

        addButton.setEnabled(buttonUse);
        for (JComponent component : categoryComponentList) {
            component.setEnabled(categoryUse);
        }
    }
}
