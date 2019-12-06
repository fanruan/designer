package com.fr.design.chart.series.PlotSeries;

import com.fr.base.BaseUtils;
import com.fr.base.MapHelper;
import com.fr.base.MapXMLHelper;
import com.fr.base.Utils;
import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.chart.chartattr.MapPlot;
import com.fr.chart.chartglyph.MapShapeValue;
import com.fr.design.DesignerEnvManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.event.ChangeEvent;
import com.fr.design.event.ChangeListener;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CoreConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.SvgProvider;
import com.fr.workspace.WorkContext;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 7.0.3
 * Date: 12-12-29
 * Time: 下午2:41
 */
public class MapGroupExtensionPane extends BasicPane implements UIObserver {
	private static final String[] TYPE_NAMES = new String[]{
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_World_Map"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_State_Map"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Province_Map"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Map")};
	private static final int WORD = 0;
	private static final int NATION = 1;
	private static final int STATE = 2;
	private static final int USER = 3;
	private static final int OFFSET_X = 800;
	private static final int OFFSET_Y = 250;

	private UIGroupExtensionPane groupExtensionPane;
	private UIButton addButton;
	private JPopupMenu popupMenu;
	private ArrayList<javax.swing.event.ChangeListener> changeListeners = new ArrayList<javax.swing.event.ChangeListener>();
	private boolean hasPopulated = false;

	@Override
	protected String title4PopupWindow() {
		return "Map";
	}

	public MapGroupExtensionPane() {
		setLayout(new BorderLayout());
		groupExtensionPane = new UIGroupExtensionPane(TYPE_NAMES){

			/**
			 * 新添加的数据的序号
			 * @param data 数据
			 */
			protected void dealNewAddedDataIndex(Object data){
				String newName = (String)data;
				MapSvgXMLHelper helper =  MapSvgXMLHelper.getInstance();
				if(helper.getNewMapAttr(newName) != null){
					return;
				}
				MapSvgAttr attr = new MapSvgAttr();
				attr.setFilePath(MapSvgXMLHelper.customMapPath() + CoreConstants.SEPARATOR + newName + ".svg");
				helper.addNewSvgMaps(attr.getName(), attr);
			}


			/**
			 * 一次鼠标的点击会有两次事件响应（按下和释放）。前者的事件属性中getValueIsAdjusting()=true，后者是false。
			 * 是否响应list值改变
			 * @return 鼠标按下时不响应，先响应mousePress事件，在鼠标释放是再响应list值改编的事件,并且点击删除不触发更新,并且populate后触发更新
			 */
			protected boolean isRespondToValueChange(ListSelectionEvent e){
				return !e.getValueIsAdjusting() && !this.isPressOnDelete() && hasPopulated;
			}
		};
		groupExtensionPane.addSelectionChangeListener(new ChangeListener() {
			@Override
			public void fireChanged(ChangeEvent event) {
				fireStateChange();
			}
		});
		groupExtensionPane.addItemEditListener(new ChangeListener() {
			@Override
			public void fireChanged(ChangeEvent event) {
				doEdit(event);
			}
		});
		groupExtensionPane.addDeleteListener(new ChangeListener() {
			@Override
			public void fireChanged(ChangeEvent event) {
				//这么写有点问题
				String oldName = Utils.objectToString(groupExtensionPane.getSelectedObject());
				saveMapInfo(oldName);
			}
		});
		setPreferredSize(new Dimension(400, 210));
		add(groupExtensionPane, BorderLayout.CENTER);

		addButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png")) {
			@Override
			protected void paintBorder(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setStroke(UIConstants.BS);
				Shape shape = new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, UIConstants.ARC, UIConstants.ARC);
				g2d.setColor(UIConstants.LINE_COLOR);
				g2d.draw(shape);
			}
		};
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popupMenu.show(MapGroupExtensionPane.this, addButton.getX() + 1, addButton.getY() + addButton.getHeight());
			}
		});
		add(addButton, BorderLayout.SOUTH);
		initPopupMenu();
	}

	private void initPopupMenu() {
		popupMenu = new JPopupMenu() {
			@Override
			public Dimension getPreferredSize() {
				Dimension dimension = new Dimension();
				dimension.height = super.getPreferredSize().height;
				dimension.width = addButton.getWidth() - 2;
				return dimension;
			}
		};
		JMenuItem worldMap = new JMenuItem(TYPE_NAMES[WORD]);
		popupMenu.add(worldMap);
		worldMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				groupExtensionPane.addData(TYPE_NAMES[WORD], TYPE_NAMES[WORD], true);
			}
		});

		JMenuItem countMap = new JMenuItem(TYPE_NAMES[NATION]);
		popupMenu.add(countMap);
		countMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				groupExtensionPane.addData(TYPE_NAMES[NATION],
						TYPE_NAMES[NATION], true);
			}
		});

		JMenuItem proMap = new JMenuItem(TYPE_NAMES[STATE]);
		popupMenu.add(proMap);
		proMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				groupExtensionPane.addData(TYPE_NAMES[STATE],
						TYPE_NAMES[STATE], true);
			}
		});

		JMenuItem menu = new JMenuItem(TYPE_NAMES[USER]);
		popupMenu.add(menu);
		menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				groupExtensionPane.addData(TYPE_NAMES[USER],
						TYPE_NAMES[USER], true);
			}
		});
	}

	private void doEdit(ChangeEvent e) {
		MouseEvent event = (MouseEvent) e.getSource();
		JPopupMenu editPopMenu = new JPopupMenu();

		final String oldName = Utils.objectToString(groupExtensionPane.getSelectedObject());
		editPopMenu.add(createAreaItem(oldName));
		editPopMenu.add(createMarkerItem(oldName));
		editPopMenu.add(createLayerItem(oldName));
		editPopMenu.add(createRenameItem());

		editPopMenu.show(MapGroupExtensionPane.this, event.getXOnScreen() - OFFSET_X, event.getYOnScreen() - OFFSET_Y);
	}

	private void mapCheckBeforeEdit(String name){
		if(MapSvgXMLHelper.getInstance().containsMapName(name) || MapSvgXMLHelper.getInstance().getNewMapAttr(name)!=null){
			return;
		}
		MapSvgAttr attr = new MapSvgAttr();
		attr.setFilePath(MapSvgXMLHelper.customMapPath() + CoreConstants.SEPARATOR + name + ".svg");
		MapSvgXMLHelper.getInstance().addNewSvgMaps(name,attr);
	}

	private JMenuItem createAreaItem(final String oldName) {
		JMenuItem editFileItem = new JMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Edit_Image_Region"));
		editFileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final MapCustomPane image = new MapCustomPane();
				image.setImageSelectType(MapShapeValue.AREA);
				image.populateBean(oldName);
				image.setTypeNameAndMapName(groupExtensionPane.getSelectedType(), Utils.objectToString(groupExtensionPane.getSelectedObject()));
				mapCheckBeforeEdit(oldName);
				final Image oldImage = getMapImage(oldName);
				BasicDialog dialog = image.showMediumWindow(SwingUtilities.getWindowAncestor(MapGroupExtensionPane.this), new DialogActionAdapter() {
					public void doOk() {
						image.updateBean(); // 比较两个前后的名字是否相同,  是图片  如果图片不同, 则直接比较

						Image newImage = getMapImage(oldName);
						if(!ComparatorUtils.equals(oldImage, newImage)) {
							fireStateChange();
						}

						//versionID递增
						MapSvgAttr old = MapSvgXMLHelper.getInstance().getMapAttr(oldName);
						if(old != null) {
							old.addVersionID();
						}

						saveMapInfo(oldName);
						refresh();
					}
				});
				dialog.setVisible(true);
			}
		});
		return editFileItem;
	}

	//根据地图的名字返回地图的图片
	private Image getMapImage(String mapName) {
		if (MapSvgXMLHelper.getInstance().containsMapName(mapName)) {
			MapSvgAttr mapAttr =  MapSvgXMLHelper.getInstance().getMapAttr(mapName);
			if(mapAttr == null) {
				return null;
			}
			return mapAttr.getMapImage();
		}

		return null;
	}

	private JMenuItem createMarkerItem(final String oldName) {
		JMenuItem editMarkerItem = new JMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Edit_Image_Marker"));
		editMarkerItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final MapCustomPane image = new MapCustomPane();
				image.setImageSelectType(MapShapeValue.POINT);
				image.populateBean(oldName);
				mapCheckBeforeEdit(oldName);
				final Image oldImage = getMapImage(oldName);
				BasicDialog dialog = image.showMediumWindow(SwingUtilities.getWindowAncestor(image), new DialogActionAdapter() {
					public void doOk() {
						image.updateBean();

						Image newImage = getMapImage(oldName);
						if(!ComparatorUtils.equals(oldImage, newImage)) {
							fireStateChange();
						}

						//versionID递增
						MapSvgAttr old = MapSvgXMLHelper.getInstance().getMapAttr(oldName);
						if(old != null) {
							old.addVersionID();
						}

						saveMapInfo(oldName);
						refresh();
					}
				});
				dialog.setVisible(true);
			}
		});
		return editMarkerItem;
	}

	private JMenuItem createLayerItem(final String oldName) {
		JMenuItem corrItem = new JMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Map_Corresponding_Fields"));
		corrItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final MapDefiAreaNamePane namedPane = new MapDefiAreaNamePane();
				namedPane.populateBean(oldName);
				mapCheckBeforeEdit(oldName);
				BasicDialog dialog = namedPane.showMediumWindow(SwingUtilities.getWindowAncestor(namedPane), new DialogActionAdapter() {
					public void doOk() {
						namedPane.updateBean();// 地图的名称 value对应情况

						MapSvgAttr old = MapSvgXMLHelper.getInstance().getMapAttr(oldName);
						if(old != null) {
							old.addVersionID();
						}

						saveMapInfo(oldName);
					}
				});
				dialog.setVisible(true);
				refresh();
			}
		});

		return corrItem;
	}

	private void showRenameWaring(String newName){
		FineJOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), "\"" + newName + "\"" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Has_Been_Existed")
				+ "!", com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Alert"), JOptionPane.WARNING_MESSAGE);
	}

	private JMenuItem createRenameItem() {
		JMenuItem renameItem = new JMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Map_Rename"));
		renameItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newName = FineJOptionPane.showInputDialog(DesignerContext.getDesignerFrame().getContentPane(),
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Map_Rename"), groupExtensionPane.getSelectedObject());
				if (StringUtils.isNotBlank(newName)) {
					String oldName = Utils.objectToString(groupExtensionPane.getSelectedObject());
					if(ComparatorUtils.equals(oldName, newName)){
						return;
					}
					//本地在看看临时的helper里面有没有
					if(MapSvgXMLHelper.getInstance().getNewMapAttr(newName) != null){
						showRenameWaring(newName);
						return;
					}
					try{//提醒名字已存在
						if (WorkContext.getWorkResource().exist(StableUtils.pathJoin(
								MapSvgXMLHelper.relativeDefaultMapPath(),newName+SvgProvider.EXTENSION))){
							showRenameWaring(newName);
							return;
						}
						if (WorkContext.getWorkResource().exist(StableUtils.pathJoin(
								MapSvgXMLHelper.relativeCustomMapPath(), newName + SvgProvider.EXTENSION))){
							showRenameWaring(newName);
							return;
						}
						MapSvgAttr editingAttr = MapSvgXMLHelper.getInstance().getMapAttr(oldName);
						if(editingAttr == null){
							editingAttr = MapSvgXMLHelper.getInstance().getNewMapAttr(oldName);
						}
						if( editingAttr == null) {
							return;
						}
						editingAttr.renameMap(newName);
						groupExtensionPane.setValueAtCurrentSelectIndex(newName);
						fireStateChange();
						saveMapInfo(newName);
						WorkContext.getWorkResource().delete(
								StableUtils.pathJoin(MapSvgXMLHelper.relativeDefaultMapPath(),oldName+SvgProvider.EXTENSION));
						WorkContext.getWorkResource().delete(
								StableUtils.pathJoin(MapSvgXMLHelper.relativeCustomMapPath(),oldName+SvgProvider.EXTENSION));
						refresh();
					}catch (Exception exp){
						FineLoggerFactory.getLogger().error(exp.getMessage());
					}
				}
			}
		});
		return renameItem;
	}

	private void refresh() {
		this.validate();
		this.repaint();

		DesignerFrame frame = DesignerContext.getDesignerFrame();
		if(frame != null) {
			frame.repaint();//kunsnat: 图表属性没变, 只是读取时 内容变化.
		}
	}

	//保存修改过的地图信息
	private void saveMapInfo(final String mapName) {
		SwingWorker worker = new SwingWorker<Integer, Void>() {
			@Override
			protected Integer doInBackground() throws Exception {
				MapSvgAttr attr = MapSvgXMLHelper.getInstance().getMapAttr(mapName);// 只有在编辑地图时才需要储存相关数据 @kuns
				if(attr != null){
					attr.writeBack(mapName);
				}
				return 0;
			}

			@Override
			protected void done() {
				FineLoggerFactory.getLogger().info(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Map_Saved")); // 地图已经保存.
			}

		};
		worker.execute();
		DesignerEnvManager.addWorkers(worker);
	}

	private void fireStateChange() {
		for (int i = changeListeners.size(); i > 0; i--) {
			changeListeners.get(i - 1).stateChanged(new javax.swing.event.ChangeEvent(this));
		}
	}

	/**
	 * 更新地图属性
	 * @return 返回地图名称.
	 */
	public String updateBean(MapPlot plot) {
		if(!hasPopulated){
			this.populateBean(plot);
			hasPopulated = true;
		}

		MapHelper helper = plot.isSvgMap() ? MapSvgXMLHelper.getInstance() : MapXMLHelper.getInstance();

		helper.clearCateNames();
		for (String type : TYPE_NAMES) {
			Object[] datas = groupExtensionPane.getData(type);
			for (Object name : datas) {
				helper.addCateNames(type, name);
			}
		}

		return Utils.objectToString(groupExtensionPane.getSelectedObject());
	}

	/**
	 * 更新地图名称
	 * @param mapPlot 地图
	 */
	public void populateBean(MapPlot mapPlot) {
		hasPopulated = false;
		groupExtensionPane.clearData();

		for (String type : TYPE_NAMES) {
			MapHelper helper = mapPlot.isSvgMap() ? MapSvgXMLHelper.getInstance() : MapXMLHelper.getInstance();
			java.util.List list = helper.getNamesListWithCateName(type);
			for (Object name : list) {
				groupExtensionPane.addData(name, type);
			}
		}

		groupExtensionPane.setSelectedObject(mapPlot.getMapName());

		hasPopulated = true;
	}

	/**
	 * 给组件登记一个观察者监听事件
	 *
	 * @param listener 观察者监听事件
	 */
	public void registerChangeListener(final UIObserverListener listener) {
		changeListeners.add(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent e) {
				listener.doChange();
			}
		});
	}

	/**
	 * 组件是否需要响应添加的观察者事件
	 *
	 * @return 如果需要响应观察者事件则返回true，否则返回false
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}

	public void setEnabled(boolean isEnabled){
		super.setEnabled(isEnabled);
		addButton.setEnabled(isEnabled);
		popupMenu.setEnabled(isEnabled);
		groupExtensionPane.setEnabled(isEnabled);
	}
}
