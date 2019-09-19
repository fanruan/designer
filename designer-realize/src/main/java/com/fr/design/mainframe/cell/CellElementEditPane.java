package com.fr.design.mainframe.cell;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.CellAttributeProvider;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.itabpane.TitleChangeListener;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.cell.settingpane.*;
import com.fr.design.utils.DesignUtils;
import com.fr.general.ComparatorUtils;

import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * the new 单元格属性表 !!!:只对当前选中的设置面板进行数据的populate和update操作
 *
 * @author zhou
 * @since 2012-5-8下午12:18:53
 */
public class CellElementEditPane extends BasicPane {
    private static final int LEFT_BORDER = -5;
    private static final int RIGHT_BORDER = 5;
    private List<AbstractCellAttrPane> paneList;
    private TemplateCellElement cellelement;
    private ElementCasePane ePane;
    private UIHeadGroup tabsHeaderIconPane;

    private boolean isEditing;
    private CardLayout card;
    private JPanel center;
    private JPanel downTitle;
    private TitleChangeListener titleChangeListener = null;

    private CellAttributeProvider cellAttributeProvider = null;


    public CellElementEditPane() {
        setLayout(new BorderLayout());
        initPaneList();
        String[] iconArray = new String[paneList.size()];
        card = new CardLayout();
        center = new JPanel(card);
        for (int i = 0; i < paneList.size(); i++) {
            AbstractCellAttrPane pane = paneList.get(i);
            iconArray[i] = pane.getIconPath();
            center.add(pane, pane.title4PopupWindow());
        }

        tabsHeaderIconPane = new UIHeadGroup(iconArray) {
            @Override
            public void tabChanged(int index) {
                card.show(center, paneList.get(index).title4PopupWindow());
                populate(ePane);
                if (titleChangeListener != null) {
                    titleChangeListener.fireTitleChange(getSelectedTabName());
                }
            }
        };
        tabsHeaderIconPane.setNeedLeftRightOutLine(false);

        downTitle = new JPanel();
        downTitle.setLayout(new BorderLayout());
        downTitle.add(tabsHeaderIconPane, BorderLayout.NORTH);
        center.setBorder(BorderFactory.createEmptyBorder(0, LEFT_BORDER, 0, 0));
        downTitle.add(center, BorderLayout.CENTER);

        this.add(downTitle, BorderLayout.CENTER);

    }


    public void setSelectedIndex(String... id) {
        String firstid = id[0];
        for (int i = 0; i < paneList.size(); i++) {
            if (ComparatorUtils.equals(firstid, paneList.get(i).title4PopupWindow())) {
                tabsHeaderIconPane.setSelectedIndex(i);
                if (id.length == 2) {
                    paneList.get(i).setSelectedByIds(1, id);
                }
                break;
            }
        }
    }

    public void populate(ElementCasePane elementCasePane) {
        if (isEditing) {
            isEditing = false;
            return;
        }
        if (elementCasePane == null) {
            return;
        }
        this.ePane = elementCasePane;
        Selection selection = ePane.getSelection();
        final TemplateElementCase elementCase = ePane.getEditingElementCase();
        if (elementCase != null && selection instanceof CellSelection) {
            CellSelection cs = (CellSelection) selection;
            // isSelectedOneCell()方法放在外面这儿一起算，因为它是比较耗时间的
            // 如果直接只传递elementCasePane，那么每个AbstratCellAttrPane都要算一次，很浪费时间

            CellElement cellElement = elementCase.getCellElement(cs.getColumn(), cs.getRow());
            if (cellElement == null) {
                cellElement = new DefaultTemplateCellElement(cs.getColumn(), cs.getRow());
                //默认选中的是A1单元格，所以若是A1单元格没有加到列表时要加上，否则在聚合报表时会出错
                if (cs.isSelectedOneCell(elementCasePane) && (cs.getColumn() + cs.getRow() == 0)) {
                    elementCase.addCellElement((TemplateCellElement) cellElement);
                }
            }

            cellelement = (TemplateCellElement) cellElement;
            // 这儿只对当前选中的面板populate
            paneList.get(tabsHeaderIconPane.getSelectedIndex()).populateBean(cellelement, ePane);
            paneList.get(tabsHeaderIconPane.getSelectedIndex()).addAttributeChangeListener(listener);
        }
    }

    AttributeChangeListener listener = new AttributeChangeListener() {
        @Override
        public void attributeChange() {
            boolean isChooseFatherPane = ComparatorUtils.equals(paneList.get(tabsHeaderIconPane.getSelectedIndex()).getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_LeftParent")) ||
                    ComparatorUtils.equals(paneList.get(tabsHeaderIconPane.getSelectedIndex()).getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ExpandD_Up_Father_Cell"));
            boolean isChooseExpandPane = ComparatorUtils.equals(paneList.get(tabsHeaderIconPane.getSelectedIndex()).getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ExpandD_Expand_Direction"));
            if (isChooseExpandPane || isChooseFatherPane) {
                ePane.setSupportDefaultParentCalculate(true);
            }

            if (ePane.getSelection() instanceof CellSelection) {
                isEditing = true;
                if (ePane.isSelectedOneCell()) {
                    paneList.get(tabsHeaderIconPane.getSelectedIndex()).updateBean();
                    ePane.fireTargetModified();
                } else {
                    paneList.get(tabsHeaderIconPane.getSelectedIndex()).updateBeans();
                    ePane.fireTargetModified();
                }
            } else {
                DesignUtils.errorMessage(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Float_Element_Edit_Tips") + "!");
            }
            ePane.setSupportDefaultParentCalculate(false);
        }
    };

    public String getSelectedTabName() {
        return paneList.get(tabsHeaderIconPane.getSelectedIndex()).title4PopupWindow();
    }

    /**
     * 添加TitleChangeListener
     *
     * @param titleChangeListener titleChangeListener 监听
     */
    public void addTitleChangeListner(TitleChangeListener titleChangeListener) {
        this.titleChangeListener = titleChangeListener;
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_CellElement_Property_Table");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(240, 340);
    }

    private void initPaneList() {
        paneList = new ArrayList<AbstractCellAttrPane>();
        paneList.add(new CellExpandAttrPane());
        paneList.add(new CellStylePane());
        paneList.add(new CellPresentPane());
        paneList.add(new CellOtherSetPane());

        cellAttributeProvider = ExtraDesignClassManager.getInstance().getSingle(CellAttributeProvider.MARK_STRING);
        if (cellAttributeProvider != null) {
            paneList.add((AbstractCellAttrPane) cellAttributeProvider.createCellAttributePane());
        }
    }

}
