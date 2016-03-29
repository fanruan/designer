package com.fr.design.mainframe;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.*;
import com.fr.chart.charttypes.GisMapIndependentChart;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 * Date: 14/12/1
 * Time: 下午3:11
 */
public class GisMapPlotPane4ToolBar extends AbstractMapPlotPane4ToolBar {
    private static final int BAIDU = 0;
    private static final int GOOGLE= 1;


    private static final String[] TYPE_NAMES = new String[]{
   			Inter.getLocText("FR-Chart-Map_Baidu"),
   			Inter.getLocText("FR-Chart-Map_Google")};


    private UITextField keyField = new UITextField(){
        public Dimension getPreferredSize() {
            return new Dimension(COMBOX_WIDTH, COM_HEIGHT);
        }
    };

    private DocumentListener keyListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            fireKeyChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            fireKeyChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            fireKeyChange();
        }
    } ;

    private void fireKeyChange(){
        ChartCollection chartCollection = (ChartCollection)chartDesigner.getTarget().getChartCollection();
        Chart chart =chartCollection.getSelectedChart();
        GisMapPlot plot =(GisMapPlot) chart.getPlot();
        String key = this.keyField.getText().trim();
        if(plot.isGisType() && key != plot.getBaiduKey()){
            plot.setBaiduKey(key);
        }else if(!plot.isGisType() && key != plot.getGoogleKey()){
            plot.setGoogleKey(key);
        }
        chartDesigner.fireTargetModified();
    }

    public GisMapPlotPane4ToolBar(final ChartDesigner chartDesigner){
        super(chartDesigner);
        this.add(getKeyPane());
        keyField.getDocument().addDocumentListener(keyListener);
    }

    private JPanel getKeyPane(){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p,f};
        double[] rowSize = {p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel("key"),keyField},
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }


    protected void calculateDetailMaps(int mapType){
        switch (mapType) {
            case BAIDU:
                populateDetilMaps(Inter.getLocText("FR-Chart-Map_Baidu"));
                break;
            case GOOGLE:
                populateDetilMaps(Inter.getLocText("FR-Chart-Map_Google"));
                break;
            default:
                populateDetilMaps(Inter.getLocText("FR-Chart-Map_Baidu"));
        }
        fireMapChange();

    }

    /**
     * 更新地图面板
     * @param mapType 地图名字
     */
    public void populateMapPane(String mapType){
        super.populateMapPane(mapType);
        populateDetilMaps(mapTypeComboBox.getSelectedItem().toString());
    }

    protected void populateDetilMaps(String mapType){
        mapTypeComboBox.removeItemListener(mapTypeListener);
        ChartCollection chartCollection = (ChartCollection)chartDesigner.getTarget().getChartCollection();
        Chart chart =chartCollection.getSelectedChart();
      	GisMapPlot plot = (GisMapPlot) chart.getPlot();
        keyField.getDocument().removeDocumentListener(keyListener);
      	if(plot.isGisType()){
      		keyField.setText(plot.getBaiduKey());
            mapTypeComboBox.setSelectedIndex(0);
      	}else{
      		keyField.setText(plot.getGoogleKey());
            mapTypeComboBox.setSelectedIndex(1);
      	}
        keyField.getDocument().addDocumentListener(keyListener);
        mapTypeComboBox.addItemListener(mapTypeListener);
    }

    private void fireMapChange(){
        ChartCollection chartCollection = (ChartCollection)chartDesigner.getTarget().getChartCollection();
        Chart chart =chartCollection.getSelectedChart();
        if(chart.getPlot().getPlotStyle() != ChartConstants.STYLE_NONE){
            resetChart(chart);
        }

		Chart[] cs = GisMapIndependentChart.gisChartTypes;
		GisMapPlot plot;
		if (cs.length > 0) {
			try {
				plot = (GisMapPlot)cs[0].getPlot().clone();
			} catch (Exception e) {
				plot = new GisMapPlot();
			}
		} else {
			plot = new GisMapPlot();
		}

		try {
			chart.switchPlot((Plot)plot.clone());
		} catch (CloneNotSupportedException e) {
			FRLogger.getLogger().error("Error In GisChart");
			chart.switchPlot(new GisMapPlot());
		}

		plot = (GisMapPlot) chart.getPlot();
		boolean index = plot.isGisType();
		plot.setGisType(mapTypeComboBox.getSelectedIndex() == 1);

		if(index != plot.isGisType()){
			if(plot.isGisType()){
				this.keyField.setText(plot.getBaiduKey());
			}else{
				this.keyField.setText(plot.getGoogleKey());
			}
		}else{
			String key = this.keyField.getText().trim();
			if(plot.isGisType() && key != plot.getBaiduKey()){
				plot.setBaiduKey(key);
			}else if(!plot.isGisType() && key != plot.getGoogleKey()){
				plot.setGoogleKey(key);
			}
		}
        chartDesigner.fireTargetModified();
    }

    protected Plot getSelectedClonedPlot() {
        Chart[] mapChart = GisMapIndependentChart.gisChartTypes;
        GisMapPlot newPlot;
        if (mapChart.length > 0) {
      		try {
      			newPlot = (GisMapPlot)mapChart[0].getPlot().clone();
      		} catch (Exception e) {
                newPlot = new GisMapPlot();
      		}
        } else {
            newPlot = new GisMapPlot();
      	}

        Plot cloned = null;
        try {
            cloned = (Plot) newPlot.clone();
        } catch (CloneNotSupportedException e) {
            FRLogger.getLogger().error("Error In GisMapChart");
        }
        return cloned;
    }

    public  String[] getMapTypes(){
       return TYPE_NAMES;
    }


}