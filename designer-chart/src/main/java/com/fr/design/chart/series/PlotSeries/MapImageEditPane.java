package com.fr.design.chart.series.PlotSeries;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.util.*;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.fr.base.*;
import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.chart.chartglyph.MapShapeValue;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.event.ChangeEvent;
import com.fr.design.event.ChangeListener;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itable.UISelectTable;
import com.fr.design.gui.itable.UITableNoOptionUI;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.UIDialog;
import com.fr.general.ComparatorUtils;

import com.fr.stable.CoreGraphHelper;
import com.fr.stable.StringUtils;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * 地图 图片编辑界面 选中图片 编辑. 支持鼠标选中等事件
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-10-15 下午04:17:28
 */
public class MapImageEditPane extends BasicBeanPane<String> {
	private static final long serialVersionUID = -5925535686784344616L;
	private static final double ARCSIZE = 7;
	private static final int LOCATIONOFFSET = 10;
	private static final int NAME_EDIT_PANE_WIDTH  = 130;
	private static final int NAME_EDIT_PANE_HEIGHT = 225;
	
	private ImageEditPane imageEditPane;
	private UISelectTable recordTable;
	private int editType = 0;// 只标记mark或者标记出路径
	private List<String> fromDataList = new ArrayList<String>();// 编辑时从数据集中取出的使用字段.

	private HashMap<String, ArrayList<String>> resultAreaShape = new HashMap<String, ArrayList<String>>();// 名字 对应图形 // 已编辑区全都用指定颜色
    private String currentNodeName ; //当前选中的节点的名字
    private String typeName = "";
    private String mapName = "";

	private String mouseSelectListName = "";
	private String editMapName = "";

	public MapImageEditPane() {
		initCom();
	}

	private void initCom() {
		this.setLayout(new BorderLayout(0, 0));

		imageEditPane = new ImageEditPane();
		this.add(imageEditPane, BorderLayout.CENTER);

		recordTable = new UISelectTable(1){
			public int columnAtPoint(Point point) {
				//只有一列
				return 0;
			}
		};
		recordTable.addSelectionChangeListener(new ChangeListener() {
			@Override
			public void fireChanged(ChangeEvent event) {
				mouseSelectListName = Utils.objectToString(event.getSource());

				imageEditPane.repaint();
				MapImageEditPane.this.repaint();
			}
		});

		recordTable.setUI(new UITableNoOptionUI());

		recordTable.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent e) {
				List<Object[]> names = recordTable.updateBean();//  得到所有的List

				List<String> test = new ArrayList<String>();
				for (int i = 0; i < names.size(); i++) {
					test.add(Utils.objectToString(recordTable.getValueAt(i, 0)));
				}

				Iterator<String> keys = resultAreaShape.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();
					if (!test.contains(key)) {
						keys.remove();
					}
				}
				recordTable.revalidate();
				repaint();
			}
		});

		UIScrollPane pane = new UIScrollPane(recordTable);
		pane.setPreferredSize(new Dimension(150, 320));
		pane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Filed_Name")));

		this.add(pane, BorderLayout.EAST);
	}

	/**
	 * 设置编辑的类型: 区域 或者 点
	 */
	public void setEditType(int editType) {
		this.editType = editType;
	}

	/**
	 * 返回编辑的类型: 区域 或者 点
	 */
	public int getEditType() {
		return editType;
	}

    /**
     * 设置正在编辑的svg文件
     */
    public void setSvgMap(String filePath){
        resultAreaShape.clear();
        mouseSelectListName = StringUtils.EMPTY;
        recordTable.populateBean(new ArrayList<Object[]>());
        recordTable.revalidate();

        imageEditPane.setSvgMap(filePath);

        repaint();
    }

	/**
	 * 清空图片内容
	 */
	public void clearSvgMap(){
		resultAreaShape.clear();
        mouseSelectListName = StringUtils.EMPTY;
        recordTable.populateBean(new ArrayList<Object[]>());
        recordTable.revalidate();

        imageEditPane.clearSvgMap();

        repaint();
	}

    /**
     * 设置正在编辑的svg文件
     * @param attr 已经读取过的文件
     */
    public void setSvgMap(MapSvgAttr attr){
        resultAreaShape.clear();
        mouseSelectListName = StringUtils.EMPTY;
        recordTable.populateBean(new ArrayList<Object[]>());
        recordTable.revalidate();

        imageEditPane.setSvgMap(attr);

        repaint();
    }

	/**
	 * 刷新数据列表中的数据
     * @param list 列表
	 */
	public void refreshFromDataList(List list) {
		fromDataList.clear();

		for (Object aList : list) {
			fromDataList.add(Utils.objectToString(aList));
		}
	}

    /**
     * 当前正在编辑的条目的类别(国家，省市)名和地图名
     * @param typeName 类别名
     * @param mapName 地图名
     */
    public void setTypeNameAndMapName(String typeName, String mapName){
        this.typeName = typeName;
        this.mapName = mapName;
    }

	private class ImageEditPane extends JComponent implements MouseListener, MouseMotionListener {

        private MapSvgAttr currentSvgMap;//当前选中的svg地图
		private GeneralPath selectedShape; // 当前选中的Shape
		private Image image = BaseUtils.readImage("");// 所选择的图片

        //平移的位置
		private double moveLeft = 0;
		private double moveTop = 0;

        //鼠标落点的位置
		private double mouseStartX;
		private double mouseStartY;

		private boolean dragged = false;

		public ImageEditPane() {
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}

		public void paintComponent(Graphics g) {// 选中图片之后, 记录选中的区域, 点中的 标记点
			Rectangle bounds = this.getBounds();
			if (bounds == null || this.image == null) {
				return;
			}

			dealReady4Paint(g, bounds);

			int imageWidth = this.image.getWidth(new JPanel());
			int imageHeight = this.image.getHeight(new JPanel());

			Graphics2D g2d = (Graphics2D) g;
			if (this.image != null) {// 太小会放大, 太大, 则默认拖动
				g2d.drawImage(this.image, (int) moveLeft, (int) moveTop, imageWidth, imageHeight, new JPanel()); // 只是移动起始位置
			}

			g2d.translate(moveLeft, moveTop);
			g2d.setStroke(new BasicStroke(1));

			if (resultAreaShape == null) {
				throw new IllegalArgumentException("resultAreaShape can not be null!");
			}
			if (!resultAreaShape.isEmpty()) {
				for (String key : resultAreaShape.keySet()) {

					GeneralPath selectShape = getSelectedNodePath(key);
					g2d.setColor(Color.green);
                    g2d.draw(selectShape);
				}
			}

			g2d.setColor(Color.blue);
			if (StringUtils.isNotEmpty(mouseSelectListName) && resultAreaShape.containsKey(mouseSelectListName)) {// 当前悬浮选中的 list, 自己画的悬浮提示, 背景 边框, 文字
				GeneralPath highSelect = getSelectedNodePath(mouseSelectListName);
				if (highSelect != null) {
                    g2d.fill(highSelect);
				}
			} else if (selectedShape != null) {
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				g2d.fill(selectedShape);
			}

			g2d.translate(-moveLeft, -moveTop);
		}
		
		private void dealReady4Paint(Graphics g, Rectangle bounds) {
			super.paintComponent(g);

			int x = (int) bounds.getX();
			int y = (int) bounds.getY();
			int width = (int) bounds.getWidth();
			int height = (int) bounds.getHeight();
			g.clipRect(x, y, width, height);
		}


        //初始化下图片的状态
		private void initImage() {
            this.image = currentSvgMap.getMapImage();
            CoreGraphHelper.waitForImage(this.image);

			this.selectedShape = null;
			this.moveLeft = 0;
			this.moveTop = 0;
		}

        /**
         * 设置当前正在编辑的文件
         * @param filePath 文件路径
         */
        public void setSvgMap(String filePath){
            currentSvgMap = new MapSvgAttr(filePath);
            currentSvgMap.setMapTypeAndName(typeName, mapName);
            initImage();
        }

        /**
         * 设置当前正在编辑的文件
         * @param attr 读取过的文件
         */
        public void setSvgMap(MapSvgAttr attr){
            currentSvgMap = attr;

            initImage();
        }

		public void clearSvgMap(){
			currentSvgMap = null;
			this.image = BaseUtils.readImage("");;
            this.selectedShape = null;
            this.moveLeft = 0;
            this.moveTop = 0;
			this.dragged = false;
			this.mouseStartX = 0;
			this.mouseStartY = 0;
		}


		public Image getImage() {
			return this.image;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			drawSelectShape(e);
			if (e.getClickCount() == 2) {// 2次编辑, 不然很容易错乱 和 update 错误
				showEditNamePane(e);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			this.mouseStartX = e.getPoint().getX();
			this.mouseStartY = e.getPoint().getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			drawWhenDragEnd(e);
		}

		public void mouseDragged(MouseEvent e) {
			this.dragged = true;
		}

		public void mouseMoved(MouseEvent e) {

		}

		private void drawWhenDragEnd(MouseEvent e) {
			if (this.image == null || this.getBounds() == null) {
				return;
			}

			if (dragged) {
				double endX = e.getPoint().getX();
				double endY = e.getPoint().getY();

				int imageWidth = image.getWidth(new JPanel());
				int imageHeight = image.getHeight(new JPanel());

				int paneWidth = (int) this.getBounds().getWidth();
				int paneHeight = (int) this.getBounds().getHeight();

				if (imageWidth > paneWidth) {
					double offX = endX - this.mouseStartX;
					this.moveLeft += offX;

					this.moveLeft = Math.max(this.moveLeft, paneWidth - imageWidth);
					this.moveLeft = Math.min(0, this.moveLeft);
				} else {
					this.moveLeft = 0;
				}

				if (imageHeight > paneHeight) {
					double offY = endY - this.mouseStartY;
					this.moveTop += offY;

					this.moveTop = Math.max(this.moveTop, paneHeight - imageHeight);
					this.moveTop = Math.min(0, this.moveTop);
				} else {
					this.moveTop = 0;
				}
				this.repaint();
			}

			this.dragged = false;
		}

        //画鼠标点击的区域
		private void drawSelectShape(MouseEvent e) {
			selectedShape = null;
			mouseSelectListName = StringUtils.EMPTY;
			if (this.image == null) {
				return;
			}

			Point ePoint = e.getPoint();
			Point select = new Point((int) (ePoint.getX() - moveLeft), (int) (ePoint.getY() - moveTop));// 支持ctrl 选定时的多选..  只是记录多个鼠标位置 然后shape合并
			boolean gotSelectedShape = false;
			for (String key : resultAreaShape.keySet()) {
				GeneralPath mapSelect = getSelectedNodePath(key);
				if (mapSelect.contains(select)) {
					selectedShape = mapSelect;
					currentNodeName = currentSvgMap.getSelectedPathName(select);
					gotSelectedShape = true;
					break;
				}
			}

			if (!gotSelectedShape) {// 根据坐标重新在图片中选取Shape

				if (getEditType() == MapShapeValue.AREA) {
					selectedShape = currentSvgMap.getSelectPath(select);
                    currentNodeName = currentSvgMap.getSelectedPathName(select);
				} else {
                    //标记点类型的要不要待定

				}
			}
			this.repaint();
		}

		private void showEditNamePane(MouseEvent e) {
			if (this.image == null || selectedShape == null) {
				return;
			}
			final EditNamePane namePane = new EditNamePane();
			Point ePoint = e.getPoint();
			final Point select = new Point((int) (ePoint.getX() - moveLeft), (int) (ePoint.getY() - moveTop));
			namePane.setEditViewRow(getEditViewRow(select));
			String isSelectName = StringUtils.EMPTY;
			for (String name : resultAreaShape.keySet()) {
				GeneralPath shape = getSelectedNodePath(name);
				if (shape.contains(select)) {
					isSelectName = name;
					break;
				}
			}
			namePane.populateBean(isSelectName);
			namePane.resetPaneWithNewNameList(fromDataList);
            UIDialog bg = namePane.showUnsizedWindow(SwingUtilities.getWindowAncestor(ImageEditPane.this), new DialogActionAdapter() {
				public void doOk() {
					namePane.changeList();
					String endName = namePane.updateBean();
					if (resultAreaShape.containsKey(endName)) {
						if(ComparatorUtils.equals(endName,namePane.startName)) {
							return;
						}
						ArrayList<String> pathID = resultAreaShape.get(endName);
						if(!pathID.contains(currentNodeName)){
							pathID.add(currentNodeName);
						}
					}else{
						ArrayList<String> paths = new ArrayList<String>();
						resultAreaShape.put(endName,paths);
						paths.add(currentNodeName);
						ArrayList<String> exists = resultAreaShape.get(namePane.startName);
						if(exists!= null){
							 for(String id:exists){
								 paths.add(id);
							 }
							resultAreaShape.remove(namePane.startName);
						}
					}
				}
			});
			bg.setSize(NAME_EDIT_PANE_WIDTH, NAME_EDIT_PANE_HEIGHT);
			bg.setLocation((int) (e.getLocationOnScreen().getX()) + LOCATIONOFFSET, (int) e.getLocationOnScreen().getY());
			bg.setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Filed_Name_Edit"));
			bg.setVisible(true);
		}

		// viewList 根据内容名称 得到是哪一行.
		public int getEditViewRow(Point point) {
			int rowIndex = recordTable.getRowCount();// 最差 没有找到应该也是返回 row + 1

			// 根据编辑的位置, 是否有shape对应, 有则找出, 没有则是新的行
			String isSelectName = StringUtils.EMPTY;
			for (String name : resultAreaShape.keySet()) {
				GeneralPath shape = getSelectedNodePath(name);
				if (shape.contains(point)) {
					isSelectName = name;
					break;
				}
			}

			if (resultAreaShape.containsKey(isSelectName)) {
				for (int i = 0; i < recordTable.getRowCount(); i++) {
					String tmp = (String) recordTable.getValueAt(i, 0);
					if (ComparatorUtils.equals(isSelectName, tmp)) {
						rowIndex = i;
						break;
					}
				}
			}// 没有则添加一行

			return rowIndex;
		}
	}

	// 图片界面  悬浮点击弹出的名称列表
	private class EditNamePane extends BasicBeanPane<String> {// 点击编辑名称

		private UITextField nameText; //  文本框
		private JList dataList;
		private String startName;

		private JList hasNamedList;

		private int editViewRow = -1;// 所对应的view的index行 ,  确定当前正在编辑的viewList的行, 然后改变值

		private UILabel namedLabel = new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Pre_Defined") + "------");
		private JPanel listPane = new JPanel();

		public EditNamePane() {
			initCom();
		}

		public void setEditViewRow(int index) {
			this.editViewRow = index;
		}

		private void initCom() {
			this.setLayout(new BorderLayout(0, 0));

			nameText = new UITextField();
			nameText.setPreferredSize(new Dimension(100, 20));

			this.add(nameText, BorderLayout.NORTH);

			listPane.setLayout(new BoxLayout(listPane, BoxLayout.Y_AXIS));
			this.add(listPane, BorderLayout.CENTER);

			listPane.add(new UIScrollPane(dataList = new JList(new DefaultListModel())));
			dataList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() >= 2 && dataList.getSelectedValue() != null) {
						String value = Utils.objectToString(dataList.getSelectedValue());
						nameText.setText(value);// 直接对文本值改变, 让文本去触发事件
					}
				}
			});

			listPane.add(namedLabel);
			listPane.add(new UIScrollPane(hasNamedList = new JList(new DefaultListModel())));

			hasNamedList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() >= 2 && hasNamedList.getSelectedValue() != null) {
						nameText.setText(Utils.objectToString(hasNamedList.getSelectedValue()));
					}
				}
			});
		}

		private void relayoutList() {
			listPane.removeAll();

			listPane.setLayout(new BoxLayout(listPane, BoxLayout.Y_AXIS));
			listPane.add(new UIScrollPane(dataList));

			if (hasNamedList.getModel().getSize() > 0) {
				listPane.add(namedLabel);
				listPane.add(new UIScrollPane(hasNamedList));
			}
		}


        //列表改变，选中的时候添加shape信息
		private void changeList() {
			String textValue = nameText.getText();
			if (editViewRow >= recordTable.getRowCount()) {// 添加一行  确定的行数 即时大于viewCount
				recordTable.addLine(new String[]{textValue});
			} else if (editViewRow > -1) {
				recordTable.setValueAt(textValue, editViewRow, 0);
			}
			recordTable.revalidate();
			recordTable.repaint();

			resetPaneWithNewNameList(fromDataList);
		}

		public void resetPaneWithNewNameList(List<String> list) {// 界面不变, 只是刷新重置list
			DefaultListModel model = (DefaultListModel) dataList.getModel();
			model.removeAllElements();

			DefaultListModel hasNameModel = (DefaultListModel) hasNamedList.getModel();
			hasNameModel.removeAllElements();

			for (int i = 0; list != null && i < list.size(); i++) {
				String value = list.get(i);
				if (!resultAreaShape.containsKey(value)) {
					model.addElement(value);
				}
			}

			for (String name : resultAreaShape.keySet()) {
				if (!hasNameModel.contains(name)) {
					hasNameModel.addElement(name);
				}
			}

			relayoutList();
		}

		public void populateBean(String list) {
			nameText.setText(list);
			startName = list;
			nameText.setCaretPosition(list == null ? 0 : list.length());
		}

		@Override
		public String updateBean() {
			return nameText.getText();
		}

		@Override
		protected String title4PopupWindow() {
			return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Edit_Image");
		}
	}

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Edit_Image");
	}

	/**
	 * 更新地图 名称列表以及正在编辑的图片
	 */
	public void populateBean(String ob) {// 确定要编辑的地图名称
		this.editMapName = ob;// 当前编辑的地图名称
        MapSvgXMLHelper mapHelper = MapSvgXMLHelper.getInstance();
		if (mapHelper.containsMapName(editMapName)) {
            MapSvgAttr svgAttr = mapHelper.getMapAttr(editMapName);
			populateMapSvgAttr(svgAttr);
		}else if(mapHelper.getNewMapAttr(editMapName) != null ){
			clearSvgMap();
			imageEditPane.currentSvgMap = mapHelper.getMapAttr(editMapName);
		}else {
			clearSvgMap();
		}
	}

	/**
	 * 保存编辑的地图 形状等信息.
	 */
	public String updateBean() {
		if(imageEditPane.currentSvgMap !=null){
			this.editMapName = imageEditPane.currentSvgMap.getName();
		}
		// 将地图加入helper 固定存储   记录固定的hShape  原图片的坐标位置, 然后在根据现在现实的 实际坐标 展示
		if (StringUtils.isNotEmpty(editMapName) && this.imageEditPane.getImage() != null) {
            MapSvgXMLHelper mapHelper = MapSvgXMLHelper.getInstance();
            MapSvgAttr mapAttr = imageEditPane.currentSvgMap;
			if(mapHelper.getNewMapAttr(editMapName)!=null){
				mapHelper.removeNewMapAttr(editMapName);
				mapHelper.pushMapAttr(editMapName,mapAttr);
			}
            if(mapHelper.containsMapName(editMapName)){
                //处理下mapAttr存节点name属性
                updateMapShapePath(mapAttr);
                mapHelper.addCustomSvgMap(editMapName, mapAttr);

                //设置以后要写回
                mapAttr.writeBack(editMapName);
            }
		}

		return editMapName;
	}

	/**
      * 更新界面
      * @param svgAttr  地图属性
	 * */
	public void populateMapSvgAttr(MapSvgAttr svgAttr){
		if(svgAttr == null || svgAttr.getMapImage() == null){
			clearSvgMap();
			return;
		}
		setSvgMap(svgAttr);
		Iterator names = svgAttr.shapeValuesIterator();
		while (names.hasNext()) {
			String name = Utils.objectToString(names.next());
			ArrayList<String> pathIDs= svgAttr.getExistedShapePathID(name);
			if (name != null) {
				resultAreaShape.put(name,pathIDs);
				recordTable.addLine(new String[]{name});
			}
		}
		recordTable.revalidate();
	}

	/**
      * 更新MapSvgAttr
      * @return  返回属性
	 * */
	public MapSvgAttr updateWithOutSave(){
		if(imageEditPane.currentSvgMap !=null){
			this.editMapName = imageEditPane.currentSvgMap.getName();
		}
		// 将地图加入helper 固定存储   记录固定的hShape  原图片的坐标位置, 然后在根据现在现实的 实际坐标 展示
		if (StringUtils.isNotEmpty(editMapName) && this.imageEditPane.getImage() != null) {
            MapSvgXMLHelper mapHelper = MapSvgXMLHelper.getInstance();
            MapSvgAttr mapAttr = imageEditPane.currentSvgMap;
			if(mapHelper.getNewMapAttr(editMapName)!=null){
				mapHelper.removeNewMapAttr(editMapName);
				mapHelper.pushMapAttr(editMapName,mapAttr);
			}
            if(mapHelper.containsMapName(editMapName)){
                //处理下mapAttr存节点name属性
                updateMapShapePath(mapAttr);
                mapHelper.addCustomSvgMap(editMapName, mapAttr);
            }
			return mapAttr;
		}
		return null;
	}

	private void updateMapShapePath(MapSvgAttr mapSvgAttr){
		mapSvgAttr.clearExistShape();
		for(String key : resultAreaShape.keySet()){
			ArrayList<String> nodeName = resultAreaShape.get(key);
			if(nodeName == null){
				continue;
			}
			for(String node :nodeName){
				mapSvgAttr.setNodeName(node, key);
			}
		}
	}

	private GeneralPath getSelectedNodePath(String nodeName){
		if(imageEditPane.currentSvgMap == null){
			return new GeneralPath();
		}
		MapSvgAttr attr = imageEditPane.currentSvgMap;
		ArrayList<String> pathsID = resultAreaShape.get(nodeName);
		GeneralPath unionPath =new GeneralPath();
        //这边必须在新建的一个path上append，不然直接在存的path上append，不然后面删除组合了，也不能正常的选单个
        for(String id :pathsID){
			unionPath.append(attr.getPath4PathID(id),false);

        }
        return unionPath;
	}

}