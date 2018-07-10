package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.ScreenResolution;
import com.fr.base.Utils;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.base.GlyphUtils;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.StockLabel;
import com.fr.chart.chartdata.StockReportDefinition;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itable.UITableUI;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.TableUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 股价图 属性表 单元格数据源.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-19 下午04:10:47
 */
public class StockPlotReportDataContentPane extends AbstractReportDataContentPane {
    private static final String AXIS = Inter.getLocText("Chart_HorialTimeAxis");
    private static final String VOLUME = Inter.getLocText("ChartF_Stock_Volume");
    private static final String OPEN = Inter.getLocText("ChartF_Stock_Open");
    private static final String HIGHT = Inter.getLocText("ChartF_Stock_High");
    private static final String LOW = Inter.getLocText("ChartF_Stock_Low");
    private static final String CLOSE = Inter.getLocText("ChartF_Stock_Close");
    private static final int PRE_WIDTH = 210;
    private static final int VOLUMN_INDEX = 0;
    private static final int OPEN_INDEX = 1;
    private static final int HIGH_INDEX = 2;
    private static final int LOW_INDEX = 3;
    private static final int CLOSE_INDEX = 4;

    private TinyFormulaPane axisTime;

    public StockPlotReportDataContentPane(ChartDataPane parent) {
        initEveryPane();

        axisTime = new TinyFormulaPane() {
            @Override
            protected void initLayout() {
                this.setLayout(new BorderLayout(4, 0));
                this.add(new BoldFontTextLabel(AXIS), BorderLayout.WEST);
                this.add(formulaTextField, BorderLayout.CENTER);
                this.add(formulaTextFieldButton, BorderLayout.EAST);
            }

            @Override
            public Dimension getPreferredSize() {
                Dimension dim = new Dimension();
                dim.width = PRE_WIDTH;
                dim.height = super.getPreferredSize().height;
                return dim;
            }
        };

        this.add(axisTime, "0,0,2,0");

        List list = new ArrayList();
        list.add(new Object[]{VOLUME, ""});
        list.add(new Object[]{OPEN, ""});
        list.add(new Object[]{HIGHT, ""});
        list.add(new Object[]{LOW, ""});
        list.add(new Object[]{CLOSE, ""});

        seriesPane.populateBean(list);
        seriesPane.noAddUse();
    }

    protected void initSeriesPane() {
        seriesPane = new UICorrelationPane(columnNames()) {
            public UITableEditor createUITableEditor() {
                return new StockTableEditor();
            }

            protected boolean isDeletable(){
                return false;
            }

            protected UITable initUITable() {
                return new UITable(columnCount) {

                    public UITableEditor createTableEditor() {
                        return createUITableEditor();
                    }

                    public void tableCellEditingStopped(ChangeEvent e) {
                        stopPaneEditing(e);
                    }

                    public boolean isCellEditable(int row, int column) {
                        return column <= 1;
                    }

                    public TableUI getUI() {
                   		return new UITableUI(){
                            protected boolean isDeletable(){
                                return false;
                            }
                        };
                   	}

                    public void dealWithRollOver(int index){
                        String text =(String)this.getModel().getValueAt(index, 0);
                        double width = GlyphUtils.calculateTextDimensionWithNoRotation(text, new TextAttr(FRFont.getInstance()), ScreenResolution.getScreenResolution()).getWidth();
                        double cellWidth =  this.getCellRect(index,0,false).getWidth();
                        if(width<=cellWidth){
                             this.setToolTipText(null);
                        }else{
                            this.setToolTipText(text);
                        }
                    }
                };
            }
        };
    }


    public void populateBean(ChartCollection collection) {
        TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
        if (definition instanceof StockReportDefinition) {
            StockReportDefinition stockDefinition = (StockReportDefinition) definition;

            if (stockDefinition.getCategoryName() != null) {
                axisTime.getUITextField().setText(stockDefinition.getCategoryName().toString());
            }
            populateSeriesPane(stockDefinition);
        }
    }

    private void populateSeriesPane(StockReportDefinition stockDefinition){
        StockLabel stockLabel  = stockDefinition.getStockLabel();
        List list = new ArrayList();
        String volumeLabel = !ComparatorUtils.equals(stockLabel.getVolumeLabel(),VOLUME) ?
                StringUtils.perfectEnd(stockLabel.getVolumeLabel(),"("+VOLUME+")"): stockLabel.getVolumeLabel();
        String openLabel = !ComparatorUtils.equals(stockLabel.getOpenLabel(),OPEN) ?
                StringUtils.perfectEnd(stockLabel.getOpenLabel(),"("+OPEN+")"): stockLabel.getOpenLabel();
        String highLabel = !ComparatorUtils.equals(stockLabel.getHighLabel(),HIGHT) ?
                StringUtils.perfectEnd(stockLabel.getHighLabel(),"("+HIGHT+")"): stockLabel.getHighLabel();
        String lowLabel = !ComparatorUtils.equals(stockLabel.getLowLabel(),LOW) ?
                StringUtils.perfectEnd(stockLabel.getLowLabel(),"("+LOW+")"): stockLabel.getLowLabel();
        String closeLabel = !ComparatorUtils.equals(stockLabel.getCloseLabel(),CLOSE) ?
                StringUtils.perfectEnd(stockLabel.getCloseLabel(),"("+CLOSE+")"): stockLabel.getCloseLabel();
        list.add(new Object[]{volumeLabel, stockDefinition.getStockVolumn()});
        list.add(new Object[]{openLabel, stockDefinition.getStockOpen()});
        list.add(new Object[]{highLabel, stockDefinition.getStockHigh()});
        list.add(new Object[]{lowLabel, stockDefinition.getStockLow()});
        list.add(new Object[]{closeLabel, stockDefinition.getStockClose()});

        seriesPane.populateBean(list);
    }


    public void updateBean(ChartCollection collection) {
        collection.getSelectedChart().setFilterDefinition(new StockReportDefinition());

        TopDefinitionProvider definition = collection.getSelectedChart().getFilterDefinition();
        if (definition instanceof StockReportDefinition) {
            StockReportDefinition stockDefinition = (StockReportDefinition) definition;

            stockDefinition.setCategoryName(axisTime.getUITextField().getText());

            List<Object[]> list =seriesPane.updateBean();
            StockLabel stockLabel = new StockLabel((list.get(VOLUMN_INDEX)[0]).toString(),(list.get(OPEN_INDEX)[0]).toString(),
                    (list.get(HIGH_INDEX)[0]).toString(),(list.get(LOW_INDEX)[0]).toString(),(list.get(CLOSE_INDEX)[0]).toString());
            stockDefinition.setStockLabel(stockLabel);
            stockDefinition.setStockVolumn(canBeFormula(list.get(VOLUMN_INDEX)[1]));
            stockDefinition.setStockOpen(canBeFormula(list.get(OPEN_INDEX)[1]));
            stockDefinition.setStockHigh(canBeFormula(list.get(HIGH_INDEX)[1]));
            stockDefinition.setStockLow(canBeFormula(list.get(LOW_INDEX)[1]));
            stockDefinition.setStockClose(canBeFormula(list.get(CLOSE_INDEX)[1]));
            populateSeriesPane(stockDefinition);
        }

    }

    protected HashMap<Object, Object> createNameValue(List<Object[]> list) {
        HashMap<Object, Object> values = new HashMap<Object, Object>();

        for (int i = 0; i < list.size(); i++) {
            Object[] tmp = list.get(i);
            values.put(tmp[0], tmp[1]);
        }

        return values;
    }

    @Override
    protected String[] columnNames() {
        return new String[]{"", ""};
    }

    private class StockTableEditor extends UITableEditor {
        private TinyFormulaPane formulaComponent;
        private UITextField labelComponent;
        private int currentEditColumn = 1;

        public StockTableEditor() {
            labelComponent = new UITextField();
            formulaComponent = new TinyFormulaPane() {
                @Override
                public void okEvent() {
                    seriesPane.stopCellEditing();
                    seriesPane.fireTargetChanged();
                }
            };

            formulaComponent.setBackground(UIConstants.FLESH_BLUE);

            formulaComponent.getUITextField().registerChangeListener(new UIObserverListener() {
                @Override
                public void doChange() {
                    seriesPane.fireTargetChanged();
                }
            });
        }

        /**
         * 返回当前编辑器的值
         */
        public Object getCellEditorValue() {
            if (currentEditColumn == 0) {
                return labelComponent.getText();
            }
            return formulaComponent.getUITextField().getText();
        }

        /**
         * 返回当前编辑器..
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (column == table.getModel().getColumnCount()) {
                return null;
            }
            seriesPane.stopCellEditing();

            currentEditColumn = column;
            if (currentEditColumn == 0) {
                labelComponent.setText(getShowText(row,Utils.objectToString(value)));
                return labelComponent;
            }

            formulaComponent.getUITextField().setText(Utils.objectToString(value));
            return formulaComponent;
        }

        private String getShowText(int row,String label){
             if(row == VOLUMN_INDEX){
                 return StringUtils.cutStringEndWith(label,"("+VOLUME+")");
             }else if(row == OPEN_INDEX){
                 return StringUtils.cutStringEndWith(label, "(" + OPEN + ")");
             }else if(row == HIGH_INDEX){
                 return StringUtils.cutStringEndWith(label, "(" + HIGHT + ")");
             }else if(row == LOW_INDEX){
                 return StringUtils.cutStringEndWith(label, "(" + LOW + ")");
             }else if(row == CLOSE_INDEX ){
                 return StringUtils.cutStringEndWith(label, "(" + CLOSE + ")");
             }else {
                 return label;
             }
        }
    }
}