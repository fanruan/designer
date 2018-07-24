package com.fr.design.chart.series.PlotSeries;

import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.base.Utils;
import com.fr.chart.base.MapSvgAttr;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.FilterComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.chart.gui.data.DatabaseTableDataPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;

import com.fr.general.data.DataModel;
import com.fr.stable.StringUtils;
import org.apache.batik.swing.svg.SVGFileFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义地图界面.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-10-15 下午03:38:15
 */
public class MapCustomPane extends BasicBeanPane<String> implements AbstrctMapAttrEditPane{ // 储存地图对应的字段. 名称, 类型. shape (点 用圆形代替)

	private FilterComboBox<String> areaString;// 区域字段
	private DatabaseTableDataPane tableDataNameBox;// 数据集名称  + 后面跟随预览按钮
	private MapImageEditPane imageShowPane; // 图片展示编辑的界面
	private String lastSelectPath;
	private boolean isNeedDataSource = true;

	public MapCustomPane() {
		initComp();
	}

	public MapCustomPane(boolean isNeedDataSource){
		this.isNeedDataSource = isNeedDataSource;
		initComp();
	}

	private void initComp() {
		this.setLayout(new BorderLayout(0, 0));

		JPanel pane = new JPanel();
		this.add(pane, BorderLayout.NORTH);

		pane.setLayout(new BorderLayout());

		pane.add(northPaneCreate(), BorderLayout.NORTH);

		imageShowPane = new MapImageEditPane();

		pane.add(imageShowPane, BorderLayout.CENTER);
	}

	private JPanel northPaneCreate() {
		JPanel northPane = new JPanel();

		northPane.setLayout(new FlowLayout(FlowLayout.LEFT));

		UIButton loadMap = new UIButton(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Import_Map"));
		loadMap.setPreferredSize(new Dimension(160, 20));
		northPane.add(loadMap);

		loadMap.addActionListener(selectPictureActionListener);

		if(isNeedDataSource){
			UILabel label =new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Table_Data") + ":", SwingConstants.RIGHT) ;

            tableDataNameBox = new DatabaseTableDataPane(label) {
                  protected void userEvent() {
refreshAreaNameBox();
}
            };
            tableDataNameBox.setPreferredSize(new Dimension(200, 20));
            northPane.add(tableDataNameBox);

            northPane.add(new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Filed", "Field"}) + ":"));

            areaString = new FilterComboBox<String>();
            areaString.setPreferredSize(new Dimension(120, 20));
            areaString.addItemListener(areaChange);
            northPane.add(areaString);
		}


		return northPane;
	}

	private ActionListener selectPictureActionListener = new ActionListener() {

		public void actionPerformed(ActionEvent evt) {
            JFileChooser svgFileChooser = new JFileChooser();
            svgFileChooser.addChoosableFileFilter(new SVGFileFilter());
			if (StringUtils.isNotBlank(lastSelectPath)) {
				svgFileChooser.setSelectedFile(new File(lastSelectPath));
			}
			int returnVal = svgFileChooser.showOpenDialog(DesignerContext.getDesignerFrame());
			if (returnVal != JFileChooser.CANCEL_OPTION) {
				File selectedFile = svgFileChooser.getSelectedFile();
				lastSelectPath = selectedFile.getAbsolutePath();
				if (selectedFile != null && selectedFile.isFile()) {
                    imageShowPane.setSvgMap(selectedFile.getPath());
                    imageShowPane.repaint();
				}
			}
		}
	};

	private ItemListener areaChange = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			Object select = areaString.getSelectedItem();
			if (select != null) {
				String colName = Utils.objectToString(areaString.getSelectedItem());

				TableDataWrapper tableDataWrappe = tableDataNameBox.getTableDataWrapper();
			
				imageShowPane.refreshFromDataList(getColValuesInData(tableDataWrappe, colName));
			}
		}
	};

	public static List<String> getColValuesInData(TableDataWrapper tableDataWrappe, String colName) {
		List<String> colValues = new ArrayList<>();

		EmbeddedTableData embeddedTableData = null;
		try {
			embeddedTableData = DesignTableDataManager.previewTableDataNotNeedInputParameters(tableDataWrappe.getTableData(), TableData.RESULT_ALL, false);
		} catch (Exception ee) {
			FRContext.getLogger().error(ee.getMessage(), ee);
		}

		if(embeddedTableData == null){
			return colValues;
		}

		int columnIndex = getColumnIndex(embeddedTableData, colName);

		if(columnIndex == DataModel.COLUMN_NAME_NOT_FOUND){
			return colValues;
		}

		for (int rowIndex = 0, rowCount = embeddedTableData.getRowCount(); rowIndex < rowCount; rowIndex++) {
			String colValueName = GeneralUtils.objectToString(embeddedTableData.getValueAt(rowIndex, columnIndex));
			if (!colValues.contains(colValueName)) {
				colValues.add(colValueName);
			}
		}

		return colValues;
	}

	private static int getColumnIndex(EmbeddedTableData tableData, String colName) {
		for (int columnIndex = 0, columnCount = tableData.getColumnCount(); columnIndex < columnCount; columnIndex++) {
			if (ComparatorUtils.tableDataColumnNameEquals(tableData.getColumnName(columnIndex), colName)) {
				return columnIndex;
			}
		}
		return DataModel.COLUMN_NAME_NOT_FOUND;
	}

	/**
	 * 选中方式: 区域或者点
	 */
	public void setImageSelectType(int selectType) {
		if (imageShowPane != null) {
			imageShowPane.setEditType(selectType);
		}
	}

	private void refreshAreaNameBox() {// 刷新区域名称列表
		if(!isNeedDataSource){
			return;
		}
		TableDataWrapper tableDataWrappe = tableDataNameBox.getTableDataWrapper();
		if (tableDataWrappe == null) {
			return;
		}
		List<String> columnNameList = tableDataWrappe.calculateColumnNameList();

		Object oldSelected = areaString.getSelectedItem();
		areaString.removeAllItems();

		for(String item : columnNameList) {
			areaString.addItem(item);
		}
		areaString.getModel().setSelectedItem(oldSelected);
	}

    /**
     * 当前正在编辑的条目的类别(国家，省市)名和地图名
     * @param typeName 类别名
     * @param mapName 地图名
     */
    public void setTypeNameAndMapName(String typeName, String mapName){
        imageShowPane.setTypeNameAndMapName(typeName, mapName);
    }

	/**
	 * 根据地图名称 加载信息
	 */
	public void populateBean(String list) {
		imageShowPane.populateBean(list);
	}

	/**
	 * 根据地图名称 保存信息
	 */
	public String updateBean() {
		// 地图类型等 加入Helper
		return imageShowPane.updateBean();
	}

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Datasource-User_Defined", "Chart-Map"});
	}

	/**
      * 更新界面
      * @param attr  地图属性
    */
	public void populateMapAttr(MapSvgAttr attr) {
		imageShowPane.populateMapSvgAttr(attr);
	}

	/**
      * 更新MapSvgAttr
      * @return  返回属性
	 */
	public MapSvgAttr updateCurrentAttr() {
		return imageShowPane.updateWithOutSave();
	}
}