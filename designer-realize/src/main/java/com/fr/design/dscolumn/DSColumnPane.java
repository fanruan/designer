package com.fr.design.dscolumn;

import com.fr.data.TableDataSource;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.log.FineLoggerFactory;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author null
 * @version 2018年2月9日13点47分
 * @since 8.0
 */
public class DSColumnPane extends BasicPane {

    public static final Dimension DEFAULT_DIMENSION = new Dimension(700, 600);

    private TableDataSource tplEC;
    private UITabbedPane tabbedPane;
    private DSColumnBasicPane basicPane = null;
    private DSColumnConditionsPane conditionPane = null;
    private DSColumnAdvancedPane advancedPane = null;
    private TemplateCellElement cellElement;
    private Component lastSelectedComponent;

    public static final int SETTING_ALL = 2;
    public static final int SETTING_DSRELATED = 1;


    private ChangeListener appliedWizardTabChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent evt) {
            try {
                if (lastSelectedComponent == null) {
                    lastSelectedComponent = basicPane;
                }
                // selectTabComponent是正要切换到的那个Pane
                Component selectTabComponent = tabbedPane.getSelectedComponent();
                // denny: 如果切换Tab时上一个Pane是basicPane, 则刷新一下其他Pane，
                // 因为选择的数据列可能改变, 导致后面过滤和使用公式用到的数据项改变
                if (lastSelectedComponent == basicPane) {
                    basicPane.update(cellElement);

                    // denny_GUI: 刷新其他面板
                    refreshOtherTabs();
                }
                // 切换标签的时候就，确认是否有没有添加到列表中的条件
                lastSelectedComponent = selectTabComponent;
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }

        }
    };
    /**
     * cellElement 改变时，刷新一下
     * 比如：上边切换Tab时，basicPane Update了一下，可能会改变Field cellElement的值
     */
    private PropertyChangeListener myPropertyChangeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            refreshOtherTabs();
        }
    };

    public DSColumnPane() {
        this(SETTING_ALL);
    }

    public DSColumnPane(int setting) {
        this.initComponents(setting);
    }

    protected void initComponents(int setting) {
        JPanel contentPane = this;
        contentPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        //peter:中心Panel.
        tabbedPane = new UITabbedPane();
        tabbedPane.addChangeListener(appliedWizardTabChangeListener);

        contentPane.add(tabbedPane, BorderLayout.CENTER);

        //_denny: 数据列面板
        basicPane = new DSColumnBasicPane(setting);
        basicPane.addPropertyChangeListener("cellElement", myPropertyChangeListener);
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Basic"), basicPane);

        conditionPane = new DSColumnConditionsPane(setting);
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Filter"), conditionPane);

        advancedPane = new DSColumnAdvancedPane(setting);
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), advancedPane);

        this.setPreferredSize(new Dimension(610, 400));
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ExpandD_Data_Column");
    }

    /**
     * 更新面板信息
     *
     * @param tds         数据源
     * @param cellElement 单元格
     * @throws Exception e
     */
    public void populate(TableDataSource tds, TemplateCellElement cellElement) throws Exception {
        this.tplEC = tds;

        if (tds == null || cellElement == null) {
            // _denny: 我不认为这种情况应该出现，以防万一
            this.cellElement = new DefaultTemplateCellElement();
            return;
        }
        // _denny: 这边需要克隆一下，因为在设置时，可能改变字段cellElement，但改变真实值是不被期望的
        try {
            this.cellElement = (TemplateCellElement) cellElement.clone();
        } catch (CloneNotSupportedException ignored) {
        }
        //REPORT-7744 9.0里面过滤条件和高级设置可以通过其他地方设置，populate的时候需要更新所有面板的信息,防止设置丢失
        this.basicPane.populate(tds, this.cellElement);
        this.conditionPane.populate(tds, this.cellElement);
        this.advancedPane.populate(this.cellElement);
    }

    /**
     * update 保存
     *
     * @return 单元格信息
     */
    public CellElement update() {
        this.basicPane.update(cellElement);
        this.conditionPane.update(cellElement);
        this.advancedPane.update(cellElement);
        return cellElement;
    }

    /**
     * denny:当数据tab中的数据发生变化的时候刷新后面的tab
     */
    private void refreshOtherTabs() {
        // deny:当JTabPane中加入一个Pane时，后面的Pane可能还没有初始化
        if (conditionPane == null || advancedPane == null) {
            return;
        }
        this.conditionPane.populate(tplEC, cellElement);
        this.advancedPane.populate(cellElement);
    }

    public void putElementcase(ElementCasePane t) {
        basicPane.putElementcase(t);
    }

    public void putCellElement(TemplateCellElement tplCE) {
        basicPane.putCellElement(tplCE);
    }
}
