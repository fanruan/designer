package com.fr.design.mainframe;

import com.fr.chart.base.MapSvgAttr;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.design.DesignerEnvManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.*;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilist.JNameEdList;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 * Date: 14/12/3
 * Time: 上午10:00
 */
public class MapArrayPane extends JControlPane {
    private static final int LEFT_WIDTH = 180;
    private static final Color LINE_COLOR = new Color(176, 176, 176);
    private static final int TOP_GAP = 5;

    private static final String[] TYPE_NAMES = new String[]{
            Inter.getLocText("FR-Chart-World_Map"),
            Inter.getLocText("FR-Chart-State_Map"),
            Inter.getLocText("FR-Chart-Province_Map"),
            Inter.getLocText("FR-Chart-Custom_Map")};

    private String mapType;
    private String mapDetailName;

    MapPlotPane4ToolBar toolBar;
    UIComboBox mapTypeBox;
    private ArrayList<String> editedNames = new ArrayList<String>();

    private ItemListener typeListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            MapArrayPane.this.updateBeans();
            mapType = mapTypeBox.getSelectedItem().toString();
            MapArrayPane.this.populate(MapSvgXMLHelper.getInstance().getAllMapObjects4Cate(mapType));
        }
    };


    private ArrayList<String> removeNames = new ArrayList<String>();
    private MapEditPane mapEditPane;
    private ChartDesigner chartDesigner;

    public MapArrayPane(String mapType, String mapDetailName,ChartDesigner chartDesigner) {
        this.mapDetailName = mapDetailName;
        this.mapType = mapType;
        if (mapTypeBox != null) {
            mapTypeBox.setSelectedItem(mapType);
        }
        this.chartDesigner = chartDesigner;
        mapTypeBox.addItemListener(typeListener);
        this.setBorder(new EmptyBorder(TOP_GAP, 0, 0, 0));
        this.addEditingListner(new PropertyChangeAdapter() {
            public void propertyChange() {
                dealPropertyChange();
            }
        });
    }

    public void setToolBarPane(MapPlotPane4ToolBar pane) {
        this.toolBar = pane;
    }

    protected void doWhenPopulate(BasicBeanPane beanPane) {
        mapEditPane = (MapEditPane)beanPane;
        mapEditPane.dealWidthMap(mapType);
        String editingName =  ((MapEditPane)beanPane).getCurrentMapName();
        if(!editedNames.contains(editingName)){
            editedNames.add(editingName);
        }
    }

    protected JPanel getLeftPane() {
        JPanel centerPane = super.getLeftPane();
        mapTypeBox = new UIComboBox(TYPE_NAMES);
        JPanel leftPane = new JPanel();
        leftPane.setLayout(new BorderLayout());
        leftPane.setBorder(new EmptyBorder(3, 0, 0, 0));
        leftPane.add(mapTypeBox, BorderLayout.NORTH);
        leftPane.add(centerPane, BorderLayout.CENTER);
        return leftPane;
    }


    private void dealPropertyChange() {
        MapSvgXMLHelper helper = MapSvgXMLHelper.getInstance();
        java.util.List nameList =helper.getNamesListWithCateName(mapType);
        String[] allListNames = nameableList.getAllNames();
        allListNames[nameableList.getSelectedIndex()] = StringUtils.EMPTY;
        String tempName = getEditingName();
        if (StringUtils.isEmpty(tempName)) {
            String[] warning = new String[]{"NOT_NULL_Des", "Please_Rename"};
            String[] sign = new String[]{",", "!"};
            nameableList.stopEditing();
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(MapArrayPane.this), Inter.getLocText(warning, sign));
            setWarnigText(editingIndex);
            return;
        }
        if (!ComparatorUtils.equals(tempName, selectedName)
                && isNameRepeted(new List[]{nameList, Arrays.asList(allListNames)}, tempName)) {
            nameableList.stopEditing();
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(MapArrayPane.this),
                    Inter.getLocText(new String[]{"FR-Chart-Map_NameAlreadyExist", "Please_Rename"}, new String[]{",", "!"}));
            setWarnigText(editingIndex);
            return;
        }
        String oldname = mapEditPane.getCurrentMapName();
        mapEditPane.setCurrentMapName(tempName);
        mapEditPane.dealWidthMap(mapType);
        if(editedNames.contains(oldname)){
            editedNames.remove(oldname);
            editedNames.add(tempName);
        }
        if(helper.getNewMapAttr(oldname) != null){
            MapSvgAttr attr = helper.getNewMapAttr(oldname);
            attr.renameMap(tempName);
            helper.removeNewMapAttr(oldname);
            helper.addNewSvgMaps(tempName,attr);
        }
        this.toolBar.fireTargetModified();
        this.saveMapInfo(tempName);
    }

    protected void doAfterRemove(){
        for(String map2Remove : removeNames){
            MapSvgXMLHelper.getInstance().removeMapAttr(map2Remove);
            MapSvgXMLHelper.getInstance().removeNewMapAttr(map2Remove);
        }
        update4AllType();
    }

    protected void doBeforeRemove(){
        removeNames.clear();
        for(int index : nameableList.getSelectedIndices()){
            removeNames.add(nameableList.getNameAt(index));
        }
    }

    //保存修改过的地图信息
    private void saveMapInfo(final String mapName) {
        SwingWorker worker = new SwingWorker<Integer, Void>() {
            @Override
            protected Integer doInBackground() throws Exception {
                MapSvgAttr attr = MapSvgXMLHelper.getInstance().getMapAttr(mapName);// 只有在编辑地图时才需要储存相关数据 @kuns
                if (attr != null) {
                    attr.writeBack(mapName);
                }
                return 0;
            }

            @Override
            protected void done() {
                FRLogger.getLogger().info(Inter.getLocText("FR-Chart-Map_Saved")); // 地图已经保存.
            }

        };
        worker.execute();
        DesignerEnvManager.addWorkers(worker);
    }

    private void update4AllType() {
        MapSvgXMLHelper helper = MapSvgXMLHelper.getInstance();
        helper.clearNames4Cate(mapType);
        for(String name : nameableList.getAllNames()){
            MapSvgAttr attr = helper.getMapAttr(name);
            if(attr == null){
                continue;
            }
            helper.addCateNames(attr.getMapType(),attr.getName());
        }
    }


    /**
     * 创建菜单
     *
     * @return 菜单
     */
    public NameableCreator[] createNameableCreators() {
        return new NameableCreator[]{
                new NameableSelfCreator(Inter.getLocText("FR-Chart-Custom_Map"), MapSvgAttr.class, MapEditPane.class) {
                    public MapSvgAttr createNameable(UnrepeatedNameHelper helper) {
                        MapSvgAttr attr = new MapSvgAttr();
                        attr.setFilePath(MapSvgXMLHelper.customMapPath() + CoreConstants.SEPARATOR + helper.createUnrepeatedName(Inter.getLocText("FR-Chart-Custom_Map")) + ".svg");
                        MapSvgXMLHelper.getInstance().addNewSvgMaps(attr.getName(), attr);
                        update4Edited(attr.getName());
                        // 返回参数设置面板.
                        return attr;
                    }

                    @Override
                    public String createTooltip() {
                        return null;
                    }

                   	public void saveUpdatedBean(ListModelElement wrapper, Object bean) {
                   		wrapper.wrapper = (Nameable)bean;
                   	}
                }
        };
    }

    protected boolean isCreatorNeedIocn() {
        return false;
    }

    protected ShortCut4JControlPane[] createShortcuts() {
        return new ShortCut4JControlPane[]{
                addItemShortCut(),
                removeItemShortCut(),
        };
    }

    protected int getLeftPreferredSize() {
        return LEFT_WIDTH;
    }


    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText(new String[]{"FR-Chart-Map_Map", "FR-Chart-Data_Edit"});
    }

    /**
     * 更新
     */
    public void updateBeans() {
        super.update();
        this.update4AllType();
        this.updateAllEditedAttrMaps();
        MapSvgXMLHelper.getInstance().clearTempAttrMaps();
        //versionID递增
        this.toolBar.fireTargetModified();
        this.saveMapInfo(selectedName);
    }

    /**
     * 创建list
     * @return 返回list
     */
    public JNameEdList createJNameList() {
        JNameEdList nameEdList = new JNameEdList(new DefaultListModel()) {

            public Rectangle createRect(Rectangle rect, int iconWidth) {
                return rect;
            }

            protected void doAfterLostFocus() {
                MapArrayPane.this.updateControlUpdatePane();
            }

            public void setNameAt(String name, int index) {
                super.setNameAt(name,index);
                update4Edited(name);
            }

        };
        nameEdList.setCellRenderer(new NameableListCellRenderer());
        return nameEdList;
    }


    protected void update4Edited(String editingName){

    }

    private void updateAllEditedAttrMaps(){
        MapSvgXMLHelper helper = MapSvgXMLHelper.getInstance();
       for(String editedName : editedNames){
           if(helper.getMapAttr(editedName)!=null){
               helper.getMapAttr(editedName).writeBack(editedName);
           }else if(helper.getNewMapAttr(editedName)!=null){
               helper.getNewMapAttr(editedName).writeBack(editedName);
           }
       }
    }

    //根据地图的名字返回地图的图片
    private Image getMapImage(String mapName) {
        if (MapSvgXMLHelper.getInstance().containsMapName(mapName)) {
            MapSvgAttr mapAttr = MapSvgXMLHelper.getInstance().getMapAttr(mapName);
            if (mapAttr == null) {
                return null;
            }
            return mapAttr.getMapImage();
        }

        return null;
    }

        /*
     * Nameable的ListCellRenerer
     */
    private class NameableListCellRenderer extends
            DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);

            if (value instanceof ListModelElement) {
                Nameable wrappee = ((ListModelElement) value).wrapper;
                this.setText(((ListModelElement) value).wrapper.getName());

                for (NameableCreator creator : MapArrayPane.this.creators()) {
                    if (creator.menuIcon() != null && creator.acceptObject2Populate(wrappee) != null) {
                        this.setIcon(creator.menuIcon());
                        this.setToolTipText(creator.createTooltip());
                        break;
                    }
                }
            }
            return this;
        }
    }
}