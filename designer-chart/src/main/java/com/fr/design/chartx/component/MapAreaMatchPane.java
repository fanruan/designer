package com.fr.design.chartx.component;

import com.fr.base.BaseUtils;
import com.fr.base.ParameterMapNameSpace;
import com.fr.chartx.TwoTuple;
import com.fr.chartx.data.ChartDataDefinitionProvider;
import com.fr.chartx.data.DataSetDefinition;
import com.fr.chartx.data.DrillMapChartDataDefinition;
import com.fr.chartx.data.MapChartDataDefinition;
import com.fr.chartx.data.execute.ExecuteDataSetHelper;
import com.fr.chartx.data.field.CollectionWithMapAreaAttr;
import com.fr.data.TableDataSource;
import com.fr.data.TableDataSourceTailor;
import com.fr.data.core.DataCoreUtils;
import com.fr.data.impl.NameTableData;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataComboBox;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.chart.gui.data.table.DataPaneHelper;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.general.GeneralUtils;
import com.fr.general.data.DataModel;
import com.fr.general.data.TableDataException;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.map.MapMatchResult;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.server.ChartGEOJSONHelper;
import com.fr.plugin.chart.type.MapType;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2019-11-04
 */
public class MapAreaMatchPane extends BasicBeanPane<VanChart> {

    private TableDataComboBox tableNameCombox;
    private UIComboBox areaNameBox;
    private UILabel refreshLabel;

    private MatchAreaTable matchAreaTable;

    private MatchResultTable matchResultTable;

    private static final Object[] HEADER = new Object[]{Toolkit.i18nText("Fine-Design_Chart_Area_Name"), Toolkit.i18nText("Fine-Design_Chart_Match_To")};

    private static final Object[] HEADER_WITH_EMPTY = new Object[]{Toolkit.i18nText("Fine-Design_Chart_Area_Name"), Toolkit.i18nText("Fine-Design_Chart_Match_To"), ""};

    private MapType mapType;

    private int level;


    public MapAreaMatchPane(MapType mapType, int level, TwoTuple<DefaultMutableTreeNode, Set<String>> treeNodeAndItems) {
        this.mapType = mapType;
        this.level = level;

        initButtonGroup();
        initRefreshLabel();
        areaNameBox = new UIComboBox();
        this.setLayout(new BorderLayout(5, 10));
        this.add(createContentPane(), BorderLayout.NORTH);
        initTable(treeNodeAndItems);
        JScrollPane matchAreaScroll = new JScrollPane(matchAreaTable) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(400, 300);
            }
        };
        this.add(matchAreaScroll, BorderLayout.CENTER);
        JScrollPane matchResultScroll = new JScrollPane(matchResultTable) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(400, 200);
            }
        };
        matchResultScroll.setBorder(BorderFactory.createTitledBorder(Toolkit.i18nText("Fine-Design_Chart_Custom_Match_List")));
        this.add(matchResultScroll, BorderLayout.SOUTH);
        this.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
    }

    private JPanel createContentPane() {
        JPanel panel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();

        JPanel tableDataPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        panel.add(tableDataPane);
        tableDataPane.add(new UILabel(Toolkit.i18nText("Fine-Design_Chart_Table_Data") + ":"));
        tableNameCombox.setPreferredSize(new Dimension(96, 20));
        tableDataPane.add(tableNameCombox);

        JPanel areaNamePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        panel.add(areaNamePane);
        areaNamePane.add(new UILabel(Toolkit.i18nText("Fine-Design_Chart_Area_Name") + ":"));
        areaNamePane.add(areaNameBox);
        areaNameBox.setPreferredSize(new Dimension(96, 20));
        panel.add(refreshLabel);
        return panel;
    }

    private void initTable(TwoTuple<DefaultMutableTreeNode, Set<String>> treeNodeAndItems) {
        matchAreaTable = new MatchAreaTable(new Object[0][2], HEADER);
        matchAreaTable.setRoot(treeNodeAndItems.getFirst());
        matchAreaTable.setItems(treeNodeAndItems.getSecond());

        matchResultTable = new MatchResultTable(new Object[0][3], HEADER_WITH_EMPTY);

        DefaultTableModel model = new DefaultTableModel(new Object[0][3], HEADER_WITH_EMPTY);
        matchResultTable.setModel(model);

        matchAreaTable.setMatchResultTable(matchResultTable);
        matchResultTable.setMatchAreaTable(matchAreaTable);
    }

    private void initButtonGroup() {
        tableNameCombox = new TableDataComboBox(DesignTableDataManager.getEditingTableDataSource());
        tableNameCombox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    refreshBox();
                }
            }
        });
    }

    private void initRefreshLabel() {
        Icon refreshImage = BaseUtils.readIcon("/com/fr/design/images/control/refresh.png");
        refreshLabel = new UILabel(refreshImage);
        refreshLabel.addMouseListener(new MouseAdapter() {
            boolean mouseEntered = false;
            boolean buttonPressed = false;

            public void mouseEntered(MouseEvent e) { // 当鼠标进入时候调用.
                mouseEntered = true;
                if (!buttonPressed) {
                    refreshLabel.setBackground(java.awt.Color.WHITE);
                    refreshLabel.setOpaque(true);
                    refreshLabel.setBorder(BorderFactory.createLineBorder(java.awt.Color.GRAY));
                }
            }

            public void mouseExited(MouseEvent e) {
                mouseEntered = false;
                refreshLabel.setOpaque(false);
                refreshLabel.setBorder(BorderFactory.createEmptyBorder());
            }

            public void mousePressed(MouseEvent e) {
                buttonPressed = true;
                refreshLabel.setBackground(java.awt.Color.lightGray);
            }

            public void mouseReleased(MouseEvent e) {
                buttonPressed = false;
                if (mouseEntered) {
                    refreshLabel.setBackground(java.awt.Color.WHITE);
                    populateData(tableNameCombox.getSelectedItem().getTableDataName(), GeneralUtils.objectToString(areaNameBox.getSelectedItem()));
                }
            }
        });
    }

    public void updateBean(VanChart chart) {
        MapMatchResult matchResult = new MapMatchResult();
        if (level < 0) {
            VanChartMapPlot plot = chart.getPlot();
            plot.setMatchResult(matchResult);
        } else {
            VanChartDrillMapPlot plot = chart.getPlot();
            plot.getMatchResultList().set(level, matchResult);
        }
        if (tableNameCombox.getSelectedItem() != null) {
            matchResult.setTableName(tableNameCombox.getSelectedItem().getTableDataName());
        }
        matchResult.setColumnName(GeneralUtils.objectToString(areaNameBox.getSelectedItem()));

        matchResultTable.updateBean(matchResult);
    }

    public void populateBean(VanChart chart) {
        //先取保存的数据集名称和区域名，若不存在，就取数据集面板配置的数据集名称和区域名
        MapMatchResult matchResult;
        if (level < 0) {
            VanChartMapPlot plot = chart.getPlot();
            matchResult = plot.getMatchResult();
        } else {
            VanChartDrillMapPlot plot = chart.getPlot();
            matchResult = plot.getMatchResultList().get(level);
        }
        matchResultTable.populateBean(matchResult);

        String tableName = matchResult.getTableName();
        String areaName = matchResult.getColumnName();
        if (tableName == null) {
            DataSetDefinition dataSetDefinition = getDataSetDefinition(chart.getChartDataDefinition());
            if (dataSetDefinition == null) {
                return;
            }
            NameTableData nameTableData = dataSetDefinition.getNameTableData();
            if (nameTableData == null) {
                return;
            }
            tableName = nameTableData.getName();
            CollectionWithMapAreaAttr columnFieldCollection = (CollectionWithMapAreaAttr) dataSetDefinition.getColumnFieldCollection();

            areaName = columnFieldCollection.getMatchColumn();
        }
        tableNameCombox.setSelectedTableDataByName(tableName);
        if (StringUtils.isEmpty(areaName)) {
            return;
        }
        areaNameBox.setSelectedItem(areaName);
        populateData(tableName, areaName);
    }

    private DataSetDefinition getDataSetDefinition(ChartDataDefinitionProvider chartDataDefinitionProvider) {
        DataSetDefinition dataSetDefinition = null;
        if (chartDataDefinitionProvider instanceof MapChartDataDefinition) {
            MapChartDataDefinition mapChartDataDefinition = (MapChartDataDefinition) chartDataDefinitionProvider;
            if (mapChartDataDefinition == null) {
                return null;
            }
            switch (mapType) {
                case AREA:
                    dataSetDefinition = (DataSetDefinition) mapChartDataDefinition.getAreaMapDataDefinition();
                    break;
                case POINT:
                    dataSetDefinition = (DataSetDefinition) mapChartDataDefinition.getPointMapDataDefinition();
                    break;
                case LINE:
                    dataSetDefinition = (DataSetDefinition) mapChartDataDefinition.getLineMapDataDefinition();
                    break;
            }
        } else if (chartDataDefinitionProvider instanceof DrillMapChartDataDefinition) {
            DrillMapChartDataDefinition drillMapChartDataDefinition = (DrillMapChartDataDefinition) chartDataDefinitionProvider;
            if (drillMapChartDataDefinition == null) {
                return null;
            }
            if (drillMapChartDataDefinition.isFromBottomData()) {
                dataSetDefinition = (DataSetDefinition) drillMapChartDataDefinition.getBottomDataDefinition();
            } else {
                dataSetDefinition = (DataSetDefinition) drillMapChartDataDefinition.getEachLayerDataDefinitionList().get(level);
            }

        } else {
            dataSetDefinition = (DataSetDefinition) chartDataDefinitionProvider;
        }
        return dataSetDefinition;
    }

    private void populateData(String tableName, String columnName) {
        Object[] columnData = getColumnData(tableName, columnName);
        if (columnData == null) {
            return;
        }
        populateMatchData(columnData);
    }

    private Object[] getColumnData(String tableName, String columnName) {
        NameTableData nameTableData = new NameTableData(tableName);
        TableDataSource dataSource = TableDataSourceTailor.extractTableData(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getTarget());
        Calculator calculator = Calculator.createCalculator();
        calculator.setAttribute(TableDataSource.KEY, dataSource);
        ParameterProvider[] parameters = nameTableData.getParameters(calculator);
        final Map<String, Object> parameterMap = new HashMap<>();

        if (ArrayUtils.isNotEmpty(parameters)) {
            final ParameterInputPane pPane = new ParameterInputPane(parameters);
            pPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
                @Override
                public void doOk() {
                    parameterMap.putAll(pPane.update());
                }
            }).setVisible(true);
        }
        for (ParameterProvider parameter : parameters) {
            if (parameterMap.containsKey(parameter.getName())) {
                parameter.setValue(parameterMap.get(parameter.getName()));
            }
        }
        ParameterMapNameSpace parameterMapNameSpace = ParameterMapNameSpace.create(parameterMap);
        calculator.pushNameSpace(parameterMapNameSpace);

        try {
            DataModel dataModel = ExecuteDataSetHelper.createDataModel(calculator, nameTableData);
            int colIndex = DataCoreUtils.getColumnIndexByName(dataModel, columnName);
            if (colIndex == DataModel.COLUMN_NAME_NOT_FOUND) {
                return null;
            }
            int size = dataModel.getRowCount();
            HashSet<Object> columnData = new LinkedHashSet<>();
            for (int i = 0; i < size; i++) {
                columnData.add(dataModel.getValueAt(i, colIndex));
            }
            return columnData.toArray();
        } catch (TableDataException ignore) {
            return null;
        }
    }

    private void populateMatchData(Object[] columnData) {
        Set<String> geoAreas = matchAreaTable.getItems();

        Map<String, String> resultMap = ChartGEOJSONHelper.matchArea(columnData, geoAreas, matchResultTable.getCustomResult());

        Object[][] data = new Object[resultMap.size()][2];

        //构造table的数据结构
        Map<Object, Integer> areaNameIndex = new HashMap<>();
        int i = 0;
        for (Map.Entry entry : resultMap.entrySet()) {
            areaNameIndex.put(entry.getKey(), i);
            data[i++] = new Object[]{entry.getKey(), entry.getValue()};
        }

        matchAreaTable.setAreaNameIndex(areaNameIndex);
        matchAreaTable.setModel(new DefaultTableModel(data, HEADER));
    }

    private void refreshBox() {
        TableDataWrapper dataWrap = tableNameCombox.getSelectedItem();

        if (dataWrap == null) {
            return;
        }

        List<String> columnNameList = dataWrap.calculateColumnNameList();

        DataPaneHelper.refreshBoxItems(areaNameBox, columnNameList);
        areaNameBox.setSelectedItem(null);
    }

    public VanChart updateBean() {
        return null;
    }

    @Override
    protected String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Chart_Location_With_Area_Name");
    }
}
