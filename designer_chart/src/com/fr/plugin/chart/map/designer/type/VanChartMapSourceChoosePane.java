package com.fr.plugin.chart.map.designer.type;

import com.fr.base.Parameter;
import com.fr.base.ParameterHolder;
import com.fr.base.Utils;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.FRTreeComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.general.http.HttpClient;
import com.fr.plugin.chart.base.GisLayer;
import com.fr.plugin.chart.base.ViewCenter;
import com.fr.plugin.chart.base.WMSLayer;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.drillmap.designer.data.comp.MapDataTree;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.server.CompatibleGEOJSONHelper;
import com.fr.plugin.chart.map.server.GEOJSONHelper;
import com.fr.plugin.chart.map.server.MapLayerConfigManager;
import com.fr.plugin.chart.service.WMSFactory;
import com.fr.plugin.chart.type.GISLayerType;
import com.fr.plugin.chart.type.MapType;
import com.fr.plugin.chart.type.ZoomLevel;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Created by Mitisky on 16/5/11.
 * 地图类型选择面板,关于json资源路径/gis图层等设置面板
 */
public class VanChartMapSourceChoosePane extends JPanel implements UIObserver {
    private static final double[] COLUMN_SIZE = {48, TableLayout.FILL, TableLayout.PREFERRED};

    public static final ZoomLevel[] ZOOM_LEVELS = new ZoomLevel[]{
            ZoomLevel.AUTO,
            ZoomLevel.ZERO, ZoomLevel.ZEROPOINTFIVE,
            ZoomLevel.ONE, ZoomLevel.ONEPOINTFIVE,
            ZoomLevel.TWO, ZoomLevel.TWOPOINTFIVE,
            ZoomLevel.THREE, ZoomLevel.THREEPOINTFIVE,
            ZoomLevel.FOUR, ZoomLevel.FOURPOINTFIVE,
            ZoomLevel.FIVE, ZoomLevel.FIVEPOINTFIVE,
            ZoomLevel.SIX, ZoomLevel.SIXPOINTFIVE,
            ZoomLevel.SEVEN, ZoomLevel.SEVENPOINTFIVE,
            ZoomLevel.EIGHT, ZoomLevel.EIGHTPOINTFIVE,
            ZoomLevel.NINE, ZoomLevel.NINEPOINTFIVE,
            ZoomLevel.TEN, ZoomLevel.TENPOINTFIVE,
            ZoomLevel.ELEVEN, ZoomLevel.ELEVENTPOINTFIVE,
            ZoomLevel.TWELVE, ZoomLevel.TWELVEPOINTFIVE,
            ZoomLevel.THIRTEEN, ZoomLevel.THIRTEENPOINTFIVE,
            ZoomLevel.FOURTEEN, ZoomLevel.FOURTEENPOINTFIVE,
            ZoomLevel.FIFTEEN, ZoomLevel.FIFTEENPOINTFIVE,
            ZoomLevel.SIXTEEN, ZoomLevel.SIXTEENPOINTFIVE,
            ZoomLevel.SEVENTEEN, ZoomLevel.SEVENTEENPOINTFIVE,
            ZoomLevel.EIGHTEEN
    };
    private static final String AUTO_CENTER_STRING = Inter.getLocText("Plugin-ChartF_Automatic");
    private static final String CUSTOM_CENTER_STRING = Inter.getLocText("Plugin-ChartF_Custom");

    private UILabel sourceTitleLabel;
    private FRTreeComboBox sourceComboBox;
    private MapDataTree mapDataTree;
    private TreePath selectTreePath;

    private UIComboBox gisLayer;
    private JPanel layerCardPane;

    private UITextArea wmsUrl;
    private UIButton connectButton;
    private JPanel wmsLayerPane;

    private java.util.List<UICheckBox> wmsLayerCheckBoxs = new ArrayList<UICheckBox>();

    private UITextArea customTileLayer;
    private UITextArea attribution;

    private UIComboBox zoomLevel;
    private UIButtonGroup viewCenterCom;
    private JPanel longAndLatPane;
    private UISpinner longitude;
    private UISpinner latitude;

    private String[] oldParams;

    private UIObserverListener listener;
    private String[] layers = MapLayerConfigManager.getInstance().getLayerItems();
    /**
     * 给组件登记一个观察者监听事件
     *
     * @param listener 观察者监听事件
     */
    @Override
    public void registerChangeListener(UIObserverListener listener) {
        this.listener = listener;
    }

    /**
     * 组件是否需要响应添加的观察者事件
     *
     * @return 如果需要响应观察者事件则返回true，否则返回false
     */
    @Override
    public boolean shouldResponseChangeListener() {
        return true;
    }

    public VanChartMapSourceChoosePane() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        double p = TableLayout.PREFERRED;
        double[] columnSize = {246};
        double[] rowSize = {p,p,p,p,p,p,p,p};

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createMapSourcesPane(), BorderLayout.NORTH);
        panel.add(createGISLayerPane(), BorderLayout.CENTER);

        JPanel BasePane = TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("FR-Designer_Basic"), panel);

        Component[][] comps = new Component[][]{
                new Component[]{BasePane},
                new Component[]{createMapInitStatusPane()}
        };
        JPanel contentPane = TableLayoutHelper.createTableLayoutPane(comps,rowSize,columnSize);

        this.add(contentPane, BorderLayout.CENTER);
    }

    protected boolean supportParam(){
        return true;
    }

    protected TreeNode getRootNode() {
        return supportParam() ? GEOJSONTreeHelper.getInstance().getRootNodeWithPara() : GEOJSONTreeHelper.getInstance().getRootNodeWithoutPara();
    }

    protected UILabel createSourceTitleLabel() {
        return new UILabel(Inter.getLocText("Plugin-ChartF_Map_Area"));
    }

    private JPanel createMapSourcesPane() {
        mapDataTree = new MapDataTree(this.getRootNode());
        mapDataTree.setEditable(false);
        mapDataTree.selectDefaultTreeNode();
        sourceComboBox = new FRTreeComboBox(mapDataTree, mapDataTree.getCellRenderer()){
            //搜索
            protected void dealSamePath(TreePath parent, TreeNode node, UITextField textField){
                String searchText = textField.getText();
                VanChartMapSourceChoosePane.this.mapDataTree.search(searchText);
            }

            //选中 tree---combobox
            public void setSelectedItem(Object o) {
                TreePath oldPath = mapDataTree.getSelectionPath();
                Object oldText = getSelectedItem();
                if(o != null && o instanceof TreePath){
                    selectTreePath =(TreePath)o;
                    this.tree.setSelectionPath(selectTreePath);
                    this.getModel().setSelectedItem(pathToString(selectTreePath));
                    if(ComparatorUtils.equals(oldText, getSelectedItem()) && !ComparatorUtils.equals(oldPath, selectTreePath)) {
                        //point的江苏省切换到area的江苏省
                        listener.doChange();
                    }
                } else if(o instanceof String){//list里面没有
                    selectTreePath = null;
                    this.tree.setSelectionPath(null);
                    this.getModel().setSelectedItem(GEOJSONHelper.getPresentNameWithPath((String) o));
                }
            }

            @Override
            protected String pathToString(TreePath path) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                //不显示后缀
                return GEOJSONHelper.getPresentNameWithPath(node.toString());
            }

            @Override
            protected boolean customSelectable(DefaultMutableTreeNode node){
                return GEOJSONTreeHelper.isSelectableTreeNode(node);
            }
        };
        sourceComboBox.setEditable(true);
        sourceComboBox.setOnlyLeafSelectable(false);
        sourceComboBox.addPopupMenuListener(popupMenuListener);

        sourceTitleLabel = createSourceTitleLabel();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p};
        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{sourceTitleLabel,sourceComboBox},

        };
        return TableLayout4VanChartHelper.createGapTableLayoutPane(components,rowSize, columnSize);
    }

    private JPanel createGISLayerPane() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));

        gisLayer = new UIComboBox(layers);

        gisLayer.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuCanceled(PopupMenuEvent e) {
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                String selected = Utils.objectToString(gisLayer.getSelectedItem());

                gisLayer.removeAllItems();

                for(String item : MapLayerConfigManager.getInstance().getLayerItems()){
                    gisLayer.addItem(item);
                }

                gisLayer.setSelectedItem(selected);
            }
        });


        gisLayer.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    checkLayerCardPane();
                }
            }
        });

        final JPanel tileLyaerPane = createCustomTileLayer();
        final JPanel wmsLayerPane = createWMSPanel();

        layerCardPane = new JPanel(new CardLayout()){
            @Override
            public Dimension getPreferredSize() {
                String itemName = Utils.objectToString(gisLayer.getSelectedItem());
                if(MapLayerConfigManager.getInstance().isCustomLayer(itemName)){
                    return tileLyaerPane.getPreferredSize();
                }else if(MapLayerConfigManager.getInstance().isCustomWmsLayer(itemName)){
                    return wmsLayerPane.getPreferredSize();
                }
                return new Dimension(0,0);
            }
        };

        for(String itemName : layers){
            JPanel pane = new JPanel();
            if(MapLayerConfigManager.getInstance().isCustomLayer(itemName)){
                pane = tileLyaerPane;
            }else if(MapLayerConfigManager.getInstance().isCustomWmsLayer(itemName)){
                pane = wmsLayerPane;
            }

            layerCardPane.add(pane, itemName);
        }

        panel.add(gisLayer, BorderLayout.CENTER);
        panel.add(layerCardPane, BorderLayout.SOUTH);

        return TableLayout4VanChartHelper.createGapTableLayoutPane(Inter.getLocText("Plugin-ChartF_Gis_Layer"), panel);
    }

    private JPanel createCustomTileLayer() {
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double[] COLUMN_SIZE = {p, TableLayout.FILL};

        customTileLayer = new UITextArea();
        attribution = new UITextArea();
        Component[][] comps = new Component[][]{
                new Component[]{new UILabel("url", SwingConstants.RIGHT), customTileLayer},
                new Component[]{new UILabel("Attribution"), attribution}
        };
        return TableLayoutHelper.createTableLayoutPane(comps, rowSize, COLUMN_SIZE);
    }

    private JPanel createWMSPanel() {

        final double p = TableLayout.PREFERRED;
        double[] rowSize = {p};

        wmsUrl = new UITextArea();
        connectButton = new UIButton(Inter.getLocText("Plugin-ChartF_Connect_WMP"));

        Component[][] comps = new Component[][]{
                new Component[]{new UILabel("url", SwingConstants.RIGHT), wmsUrl, connectButton}
        };
        JPanel northPane = TableLayoutHelper.createTableLayoutPane(comps,rowSize, COLUMN_SIZE);

        JPanel wmsPanel = new JPanel(new BorderLayout(0, 4));
        wmsLayerPane = new JPanel(new BorderLayout());
        resetWMSLayerPane(new ArrayList<WMSLayer>());
        wmsPanel.add(northPane, BorderLayout.NORTH);
        wmsPanel.add(wmsLayerPane, BorderLayout.CENTER);

        connectButton.addActionListener(actionListener);

        addMouseListener();

        return wmsPanel;
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            new SwingWorker<Void, Double>() {
                private java.util.List<WMSLayer> list;

                @Override
                protected Void doInBackground() throws Exception {
                    HttpClient httpClient = new HttpClient(wmsUrl.getText() + "service=WMS&request=GetCapabilities");
                    httpClient.asGet();

                    if (!httpClient.isServerAlive()) {
                        return null;
                    }

                    String res =  httpClient.getResponseText();
                    list = WMSFactory.readLayers(res);
                    return null;
                }

                @Override
                protected void done() {
                    connectButton.setText(Inter.getLocText("Plugin-ChartF_Connect_WMP"));
                    if(list != null && list.size() > 0) {
                        resetWMSLayerPane(list);
                    } else {
                        JOptionPane.showMessageDialog(null, Inter.getLocText("Plugin-ChartF_Invalid_WMS"));
                    }
                }
            }.execute();
        }
    };

    private void addMouseListener() {
        connectButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                connectButton.setText(Inter.getLocText("Plugin-ChartF_Connecting_WMP"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void resetWMSLayerPane(java.util.List<WMSLayer> wmsLayers) {

        int size = wmsLayers.size();
        double[] rowSize = new double[size];
        Component[][] comps = new Component[size][2];
        wmsLayerCheckBoxs.clear();
        wmsLayerPane.removeAll();
        for(int i =0; i < size; i++){
            rowSize[i] = TableLayout.PREFERRED;
            comps[i][0] = i == 0 ? new UILabel(Inter.getLocText("Plugin-ChartF_WMS_Layers"), SwingConstants.RIGHT) : null;
            WMSLayer layer = wmsLayers.get(i);
            UICheckBox checkBox = new UICheckBox(layer.getLayer());
            checkBox.registerChangeListener(listener);
            checkBox.setToolTipText(layer.getLayer());
            checkBox.setSelected(layer.isSelected());
            comps[i][1] = checkBox;
            wmsLayerCheckBoxs.add(checkBox);
        }

        wmsLayerPane.add(TableLayoutHelper.createCommonTableLayoutPane(comps,rowSize, COLUMN_SIZE,0), BorderLayout.CENTER);

        VanChartMapSourceChoosePane.this.updateUI();
    }

    private JPanel createMapInitStatusPane() {
        zoomLevel = new UIComboBox(ZOOM_LEVELS);
        viewCenterCom = new UIButtonGroup(new String[]{AUTO_CENTER_STRING, CUSTOM_CENTER_STRING});
        longitude = new UISpinner(-Double.MAX_VALUE,Double.MAX_VALUE,1,0.0);
        latitude = new UISpinner(-Double.MAX_VALUE,Double.MAX_VALUE,1,0.0);

        double p = TableLayout.PREFERRED;
        double[] rowSize = {p,p,p};

        Component[][] comps = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Zoom_Layer")), zoomLevel},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_View_Center")), viewCenterCom},
        };
        final JPanel northPane = TableLayout4VanChartHelper.createGapTableLayoutPane(comps,rowSize,COLUMN_SIZE);

        Component[][] longAndLatComps = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Longitude")), longitude},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Latitude")), latitude}
        };
        longAndLatPane =TableLayout4VanChartHelper.createGapTableLayoutPane(longAndLatComps,rowSize,COLUMN_SIZE);
        longAndLatPane.setBorder(BorderFactory.createEmptyBorder(0,12,0,0));
        longAndLatPane.setVisible(false);

        JPanel contentPane = new JPanel(new BorderLayout(0, 6)){

            @Override
            public Dimension getPreferredSize() {
                if(longAndLatPane.isVisible()) {
                    return super.getPreferredSize();
                } else {
                    return northPane.getPreferredSize();
                }
            }
        };
        contentPane.add(northPane, BorderLayout.NORTH);
        contentPane.add(longAndLatPane, BorderLayout.CENTER);

        viewCenterCom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                longAndLatPane.setVisible(!isAutoViewCenter());
            }
        });
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Map_Init_Status"), contentPane);
    }

    private boolean isAutoViewCenter() {
        return viewCenterCom.getSelectedIndex()==0;
    }

    private PopupMenuListener popupMenuListener = new PopupMenuListener() {
        public void popupMenuCanceled(PopupMenuEvent e) {
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        }

        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

            GEOJSONTreeHelper.reset();
            mapDataTree.changeRootNode(VanChartMapSourceChoosePane.this.getRootNode());
            GEOJSONTreeHelper.getInstance().updateParamRootNode(VanChartMapSourceChoosePane.this.getParams());

            if(selectTreePath != null){
                mapDataTree.setSelectNodePath(GEOJSONHelper.completeJSONName(selectTreePath.getLastPathComponent().toString()));
                selectTreePath = mapDataTree.getSelectionPath();
            }

            mapDataTree.updateUI();//因为服务器那边可能随时编辑,所以这边要重画
            mapDataTree.setSelectionPath(selectTreePath);
            mapDataTree.scrollPathToVisible(selectTreePath);
        }
    };

    private void checkLayerCardPane() {
        CardLayout cardLayout = (CardLayout) layerCardPane.getLayout();
        cardLayout.show(layerCardPane, Utils.objectToString(gisLayer.getSelectedItem()));
    }

    public void resetComponentValue(VanChartMapPlot mapPlot) {
        resetComponentValue(mapPlot, true);
        //重置图层属性
        resetGisLayer(mapPlot);
        //重置缩放等级
        resetZoomLevel(mapPlot);
        //重置中心点位置
        resetViewCenter(mapPlot);
    }

    protected void resetComponentValue(VanChartMapPlot mapPlot, boolean samePlotChange) {
        MapType mapType = mapPlot.getMapType();

        //获取最新的参数
        String[] params = getParams();

        if (!ComparatorUtils.equals(oldParams, params)){
            oldParams = params;
            GEOJSONTreeHelper.getInstance().updateParamRootNode(params);
        }

        mapDataTree.changeRootNode(this.getRootNode());
        if(samePlotChange) {
            mapPlot.setGeoUrl(GEOJSONHelper.getInstance().getDefaultJSONURL());
            mapDataTree.selectDefaultTreeNode();
            selectTreePath = mapDataTree.getSelectionPath();
            sourceComboBox.setSelectedItem(selectTreePath);
        }
        switch (mapType){
            case CUSTOM:
                sourceTitleLabel.setText(Inter.getLocText("Plugin-ChartF_Map_Area_And_Point"));
                break;
            case POINT:
                sourceTitleLabel.setText(Inter.getLocText("Plugin-ChartF_Map_Point"));
                break;
            case LINE:
                sourceTitleLabel.setText(Inter.getLocText("Plugin-ChartF_Map_Point"));
                break;
            default:
                sourceTitleLabel.setText(Inter.getLocText("Plugin-ChartF_Map_Area"));
        }

    }

    private String[] getParams() {
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (jTemplate == null){
            return new String[0];
        }
        String[] params = new String[0];
        if(jTemplate.getTarget() instanceof ParameterHolder){
            params = getParamsName(((ParameterHolder)jTemplate.getTarget()).getParameters());
        }

        return params;
    }

    private String[] getParamsName(Parameter[] parameters) {
        String[] names = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++){
            names[i] =  parameters[i].getName();
        }
        return names;
    }

    private void resetViewCenter(VanChartMapPlot mapPlot) {
        mapPlot.getViewCenter().setAuto(true);
        viewCenterCom.setSelectedIndex(0);
    }

    private void resetZoomLevel(VanChartMapPlot mapPlot) {
        mapPlot.setZoomLevel(ZoomLevel.AUTO);
        zoomLevel.setSelectedItem(mapPlot.getZoomLevel());
    }

    private void resetGisLayer(VanChartMapPlot mapPlot) {

        String layerName = mapPlot.getDefaultGisLayerName();

        mapPlot.getGisLayer().setGisLayerType(GISLayerType.PREDEFINED_LAYER);
        mapPlot.getGisLayer().setLayerName(layerName);
        gisLayer.setSelectedItem(mapPlot.getGisLayer().getShowItemName());
    }

    public void populateBean(VanChartMapPlot mapPlot) {
        resetComponentValue(mapPlot, false);

        mapDataTree.setSelectNodePath(mapPlot.getGeoUrl());
        selectTreePath = mapDataTree.getSelectionPath();

        if(selectTreePath == null){//此url当前环境没有
            sourceComboBox.setSelectedItem(mapPlot.getGeoUrl());
        } else {
            sourceComboBox.setSelectedItem(selectTreePath);
        }

        GisLayer layer = mapPlot.getGisLayer();
        gisLayer.setSelectedItem(layer.getShowItemName());

        wmsUrl.setText(layer.getWmsUrl());
        resetWMSLayerPane(layer.getWmsLayers());
        customTileLayer.setText(layer.getCustomTileLayer());
        attribution.setText(layer.getAttribution());

        zoomLevel.setSelectedItem(mapPlot.getZoomLevel());

        ViewCenter viewCenter = mapPlot.getViewCenter();
        if(viewCenter.isAuto()){
            viewCenterCom.setSelectedIndex(0);
            longitude.setValue(0);
            latitude.setValue(0);
        } else {
            viewCenterCom.setSelectedIndex(1);
            longitude.setValue(viewCenter.getLongitude());
            latitude.setValue(viewCenter.getLatitude());
        }

        longAndLatPane.setVisible(!isAutoViewCenter());

        checkLayerCardPane();
    }

    public void updateBean(VanChartMapPlot mapPlot) {

        if(!CompatibleGEOJSONHelper.isDeprecated(mapPlot.getGeoUrl())){
            mapPlot.setGeoUrl(mapDataTree.getSelectNodeJSONPath());
        }

        GisLayer layer = mapPlot.getGisLayer();
        String layerName = Utils.objectToString(gisLayer.getSelectedItem());

        layer.setLayerName(layerName);
        layer.setGisLayerType(MapLayerConfigManager.getInstance().getGisLayerType(layerName));

        switch (layer.getGisLayerType()){
            case CUSTOM_WMS_LAYER:
                layer.setWmsUrl(wmsUrl.getText());
                layer.setWmsLayers(new ArrayList<WMSLayer>());
                for(UICheckBox checkBox : wmsLayerCheckBoxs){
                    layer.addWmsLayer(new WMSLayer(checkBox.getText(), checkBox.isSelected()));
                }
                break;
            case CUSTOM_TILE_LAYER:
                layer.setCustomTileLayer(customTileLayer.getText());
                layer.setAttribution(attribution.getText());
                break;
        }

        mapPlot.setZoomLevel((ZoomLevel) zoomLevel.getSelectedItem());

        ViewCenter viewCenter = mapPlot.getViewCenter();
        if(isAutoViewCenter()){
            viewCenter.setAuto(true);
        } else {
            viewCenter.setAuto(false);
            viewCenter.setLongitude(longitude.getValue());
            viewCenter.setLatitude(latitude.getValue());
        }
    }

    public UIComboBox getSourceComboBox(){
        return this.sourceComboBox;
    }
}
