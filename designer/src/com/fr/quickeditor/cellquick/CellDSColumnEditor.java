package com.fr.quickeditor.cellquick;

import com.fr.design.dscolumn.DSColumnAdvancedEditorPane;
import com.fr.design.dscolumn.DSColumnBasicEditorPane;
import com.fr.design.dscolumn.ResultSetGroupDockingPane;
import com.fr.design.dscolumn.SelectedDataColumnPane;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.mainframe.cell.CellEditorPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.quickeditor.CellQuickEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * 单元格元素 数据列编辑器
 *
 * @author yaoh.wu
 * @version 2017年7月24日
 * @since 9.0
 */
public class CellDSColumnEditor extends CellQuickEditor {

    private JPanel dsColumnRegion;
    private JPanel centerPane;
    //数据集列选择组件
    private SelectedDataColumnPane dataPane;
    //数据分组设置组件
    private ResultSetGroupDockingPane groupPane;
    // 基本和高级设置
    private ArrayList<CellEditorPane> paneList;
    // 基本和高级设置 卡片布局
    private CardLayout card;
    // 基本和高级设置 容器面板
    private JPanel center;
    // 卡片布局TAB切换按钮
    private UIHeadGroup tabsHeaderIconPane;
    // 分组设置监听器
    private ItemListener groupListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e == null) {
                //分组-高级-自定义点确定的时候传进来null的e,但是这时候应该触发保存
                groupPane.update();
                fireTargetModified();
                return;
            }
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                if (!isEditing) {
                    return;
                }
                groupPane.update();
                fireTargetModified();
            }
        }
    };
    //数据集列设置监听器
    private ItemListener dataListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (!isEditing) {
                    return;
                }
                dataPane.update(cellElement);
                fireTargetModified();
            }
        }
    };

    private CellDSColumnEditor() {
        super();
    }

    /**
     * Test Main
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(new CellDSColumnEditor(), BorderLayout.CENTER);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(220, 400);
        jf.setVisible(true);
    }

    /**
     * 创建面板占位
     *
     * @return JComponent 详细信息面板
     */
    @Override
    public JComponent createCenterBody() {
        this.createPanes();
        this.createSwitchTab();
        dsColumnRegion = new JPanel(new BorderLayout());
        dsColumnRegion.add(tabsHeaderIconPane, BorderLayout.NORTH);
        dsColumnRegion.add(center, BorderLayout.CENTER);
        centerPane = new JPanel(new BorderLayout());
        centerPane.add(dsColumnRegion, BorderLayout.CENTER);
        return centerPane;
    }

    /**
     * 内容全部重新动态生成，不然容易出错
     * 刷新详细信息面板
     */
    @Override
    protected void refreshDetails() {

        this.createPanes();
        this.createSwitchTab();
        dsColumnRegion = new JPanel(new BorderLayout());
        dsColumnRegion.add(tabsHeaderIconPane, BorderLayout.NORTH);
        dsColumnRegion.add(center, BorderLayout.CENTER);
        //必须removeAll之后再添加；重新再实例化一个centerJPanel，因为对象变了会显示不出来
        centerPane.removeAll();
        centerPane.add(dsColumnRegion, BorderLayout.CENTER);
        for (CellEditorPane cellEditorPane : paneList) {
            cellEditorPane.populate(cellElement);
        }
        this.validate();
    }


    /**
     * 关闭时候释放
     */
    public void release() {
        super.release();
        dsColumnRegion = null;
        centerPane = null;
    }


    /**
     * 初始化基本和高级设置切换tab
     */
    private void createSwitchTab() {
        String[] iconArray = new String[paneList.size()];
        card = new CardLayout();
        center = new JPanel(card);
        for (int i = 0; i < paneList.size(); i++) {
            CellEditorPane pane = paneList.get(i);
            iconArray[i] = pane.getIconPath();
            center.add(pane, pane.title4PopupWindow());
        }
        tabsHeaderIconPane = new UIHeadGroup(iconArray) {
            @Override
            public void tabChanged(int index) {
                card.show(center, paneList.get(index).title4PopupWindow());
                paneList.get(index).populate(cellElement);
            }
        };
        tabsHeaderIconPane.setNeedLeftRightOutLine(false);
    }

    /**
     * 刷新数据列基本和高级设置面板
     */
    private void createPanes() {
        paneList = new ArrayList<>();

        /*基本设置面板*/
        this.dataPane = new SelectedDataColumnPane(false);
        this.groupPane = new ResultSetGroupDockingPane(tc);
        dataPane.addListener(dataListener);
        groupPane.addListener(groupListener);
        paneList.add(new DSColumnBasicEditorPane(cellElement, dataPane, groupPane));


        /*高级设置面板*/
        paneList.add(new DSColumnAdvancedEditorPane());
    }
}