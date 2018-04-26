package com.fr.design.dscolumn;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.FRContext;
import com.fr.data.TableDataSource;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;

public class DSColumnPane extends BasicPane {

    private TableDataSource tplEC;
    private UITabbedPane tabbedPane;
    private DSColumnBasicPane basicPane = null;
    private DSColumnConditionsPane conditionPane = null;
    private DSColumnAdvancedPane advancedPane = null;
    private TemplateCellElement cellElement;
    protected Component lastSelectedComponent;
    
    public static final int SETTING_ALL = 2;
    public static final int SETTING_DSRELATED = 1;
    
    public DSColumnPane() {
    	this.initComponents(SETTING_ALL);
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
        tabbedPane.addTab(Inter.getLocText("Basic"), basicPane);

        conditionPane = new DSColumnConditionsPane(setting);
        tabbedPane.addTab(Inter.getLocText("Filter"), conditionPane);

        advancedPane = new DSColumnAdvancedPane(setting);
        tabbedPane.addTab(Inter.getLocText("Advanced"), advancedPane);

        this.setPreferredSize(new Dimension(610, 400));
    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("ExpandD-Data_Column");
    }

    /*
     * populate
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
        } catch (CloneNotSupportedException ce) {
        }
        this.basicPane.populate(tds, this.cellElement);
        this.conditionPane.populate(tds, this.cellElement);
        this.advancedPane.populate(this.cellElement);
    }

    /*
     * update
     */
    public CellElement update() {
        this.basicPane.update(cellElement);
        this.conditionPane.update(cellElement);
        this.advancedPane.update(cellElement);
        return cellElement;
    }
    public ChangeListener appliedWizardTabChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent evt) {
            try {
                if (lastSelectedComponent == null) {
                    lastSelectedComponent = basicPane;
                }
                //selectTabComponent是正要切换到的那个Pane
                Component selectTabComponent = tabbedPane.getSelectedComponent();
                // _denny: 如果切换Tab时上一个Pane是basicPane, 则刷新一下其他Pane，
                // 因为选择的数据列可能改变, 导致后面过滤和使用公式用到的数据项改变
                if (lastSelectedComponent == basicPane) {
                    basicPane.update(cellElement);

                    // denny_GUI: 刷新其他面板
                    refrushOtherTabs();
                }
                // 切换标签的时候就，确认是否有没有添加到列表中的条件
                lastSelectedComponent = selectTabComponent;
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }

        }
    };
    // cellElement 改变时，刷新一下
    // 比如：上边切换Tab时，basicPane Update了一下，可能会改变Field cellElement的值
    PropertyChangeListener myPropertyChangeListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            refrushOtherTabs();
        }
    };

    //_denny:当数据tab中的数据发生变化的时候刷新后面的tab
    public void refrushOtherTabs() {
        // ——deny:当JTabPane中加入一个Pane时，后面的Pane可能还没有初始化
        if (conditionPane == null || advancedPane == null) {
            return;
        }
        this.conditionPane.populate(tplEC, cellElement);
        this.advancedPane.populate(cellElement);
    }
    public void putElementcase(ElementCasePane t){
    	basicPane.putElementcase(t);
    }

	public void putCellElement(TemplateCellElement tplCE) {
		basicPane.putCellElement(tplCE);
	}
}