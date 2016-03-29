package com.fr.design.mainframe;

import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.chart.chartattr.*;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.chart.ChartDesignEditPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 * Date: 14/12/1
 * Time: 上午11:57
 */
public class MapPlotPane4ToolBar extends AbstractMapPlotPane4ToolBar{

    private static final int WORLD_MAP = 0;
    private static final int STATE_MAP = 1;
    private static final int PROVINCE_MAP = 2;
    private static final int CUSTOM_MAP = 3;
    private static final int BUTTON_WIDTH = 44;


    private static final String[] TYPE_NAMES = new String[]{
   			Inter.getLocText("FR-Chart-World_Map"),
   			Inter.getLocText("FR-Chart-State_Map"),
   			Inter.getLocText("FR-Chart-Province_Map"),
   			Inter.getLocText("FR-Chart-Custom_Map")};

    private String lastEditingName  =StringUtils.EMPTY;

    private UIButton mapEditButton = new UIButton(Inter.getLocText("FR-Chart-Data_Edit")){
        public Dimension getPreferredSize() {
            return new Dimension(BUTTON_WIDTH, COM_HEIGHT);
        }
    };

    protected UIComboBox detailMaps = new UIComboBox(){
        public Dimension getPreferredSize() {
            return new Dimension(COMBOX_WIDTH, COM_HEIGHT);
        }
    };

    private ItemListener detailListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            fireMapChange();
        }
    };

    private ActionListener mapEditListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedName =StringUtils.EMPTY;
            if(detailMaps.getSelectedItem() != null){
               selectedName =  detailMaps.getSelectedItem().toString();
            }
            final MapArrayPane mapArrayPane = new MapArrayPane(mapTypeComboBox.getSelectedItem().toString(),selectedName,chartDesigner){
                public void updateBeans() {
                    super.updateBeans();
                    if(reCalculateDetailsMaps(mapTypeComboBox.getSelectedItem().toString(),lastEditingName) ||
                            ComparatorUtils.equals(StringUtils.EMPTY,lastEditingName)){
                        detailMaps.setSelectedItem(lastEditingName);
                        ChartCollection chartCollection = (ChartCollection)chartDesigner.getTarget().getChartCollection();
                        com.fr.chart.chartattr.Chart chart =chartCollection.getSelectedChart();
                      	if(chart.getPlot().isMapPlot()){
                            MapPlot mapPlot = (MapPlot) chart.getPlot();
                            mapPlot.setMapName(lastEditingName);
                        }
                    }
                }

                protected void update4Edited(String editingName){
                   lastEditingName = editingName;
                }
            };

            BasicDialog mapArrayDialog = mapArrayPane.showWindow4ChartMapArray(DesignerContext.getDesignerFrame(),
                    new DialogActionAdapter() {

                        @Override
                        public void doOk() {
                            mapArrayPane.updateBeans();
                        }
                    });
            mapArrayDialog.setModal(true);
            mapArrayPane.setToolBarPane(MapPlotPane4ToolBar.this);
            mapArrayPane.populate(MapSvgXMLHelper.getInstance().getAllMapObjects4Cate(mapTypeComboBox.getSelectedItem().toString()));
            if(detailMaps.getSelectedItem() != null){
                mapArrayPane.setSelectedName(detailMaps.getSelectedItem().toString());
            }
            mapArrayDialog.setVisible(true);
        }
    };

    public MapPlotPane4ToolBar(ChartDesigner chartDesigner){
        super(chartDesigner);
        this.add(detailMaps);
        detailMaps.addItemListener(detailListener);
        mapEditButton.addActionListener(mapEditListener);
        this.add(mapEditButton);
    }

    /**
     * 更新地图面板
     * @param mapType 地图名字
     */
    public void populateMapPane(String mapType){
        super.populateMapPane(mapType);
        populateDetilMaps(mapTypeComboBox.getSelectedItem().toString());
        detailMaps.removeItemListener(detailListener);
        detailMaps.setSelectedItem(mapType);
        detailMaps.addItemListener(detailListener);
    }

    /**
     * 触发地图改变
     */
    public void fireMapChange(){
        MapPlot plot = new MapPlot();
        String selectedName = StringUtils.EMPTY;
        if(detailMaps.getSelectedItem() !=null ){
           selectedName = detailMaps.getSelectedItem().toString();
        }
      	plot.setMapName(selectedName);// 名字问题
        ChartCollection chartCollection = (ChartCollection)chartDesigner.getTarget().getChartCollection();
        Chart chart =chartCollection.getSelectedChart();
      	chart.setPlot(plot);
        ChartDesignEditPane.getInstance().populate(chartCollection);
        chartDesigner.fireTargetModified();
    }



    //默认选中国家地图
    protected void calculateDetailMaps(int mapType){
        switch (mapType) {
            case WORLD_MAP:
                populateDetilMaps(Inter.getLocText("FR-Chart-World_Map"));
                break;
            case STATE_MAP:
                populateDetilMaps(Inter.getLocText("FR-Chart-State_Map"));
                break;
            case PROVINCE_MAP:
                populateDetilMaps(Inter.getLocText("FR-Chart-Province_Map"));
                break;
            case CUSTOM_MAP:
                populateDetilMaps(Inter.getLocText("FR-Chart-Custom_Map"));
                break;
            default:
                populateDetilMaps(Inter.getLocText("FR-Chart-State_Map"));
        }
        fireMapChange();
    }

    private boolean reCalculateDetailsMaps(String mapType ,String detailMap){
        detailMaps.removeItemListener(detailListener);
        detailMaps.removeAllItems();
        java.util.List list = MapSvgXMLHelper.getInstance().getNamesListWithCateName(mapType);
        boolean isContains = false;
        for (Object name : list) {
            detailMaps.addItem(name);
            if(ComparatorUtils.equals(detailMap,name)){
                isContains = true;
            }
        }
        detailMaps.addItemListener(detailListener);
        return isContains;
    }


    protected void populateDetilMaps(String mapType){
        detailMaps.removeItemListener(detailListener);
        detailMaps.removeAllItems();
        java.util.List list = MapSvgXMLHelper.getInstance().getNamesListWithCateName(mapType);
        for (Object name : list) {
            detailMaps.addItem(name);
        }
        detailMaps.addItemListener(detailListener);
        if(detailMaps.getSelectedItem() != null){
            lastEditingName = detailMaps.getSelectedItem().toString();
        }
    }

    protected Plot getSelectedClonedPlot() {
        MapPlot mapPlot = new MapPlot();
        populateDetilMaps(Inter.getLocText("FR-Chart-State_Map"));
        if(detailMaps.getSelectedItem()!= null && !StringUtils.isEmpty(detailMaps.getSelectedItem().toString())){
            mapPlot.setMapName(detailMaps.getSelectedItem().toString());
        }
        return mapPlot;
    }

    public  String[] getMapTypes(){
       return TYPE_NAMES;
    }

}