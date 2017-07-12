package com.fr.design.expand;

import com.fr.base.BaseUtils;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.gui.columnrow.ColumnRowPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.stable.ColumnRow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class ExpandFatherPane extends JPanel implements GlobalNameObserver {

    private UIComboBox comboBox;
    private ColumnRowPane customParentColumnRowPane;
    private ElementCasePane ePane;
    private SelectionListener gridSelectionChangeListener;
    private CellSelection oldSelection;
    private String expandFatherName = "";
    private GlobalNameListener globalNameListener = null;
    private boolean isAlreadyAddListener = false;
    private final JPanel customPane;

    public ExpandFatherPane() {
        this.setLayout(new BorderLayout(0, LayoutConstants.VGAP_SMALL));
        comboBox = new UIComboBox(new String[]{
                Inter.getLocText("None"),
                Inter.getLocText("Default"),
                Inter.getLocText("Custom")});
        final CardLayout cardLayout = new CardLayout();
        customPane = new JPanel(cardLayout);
        customParentColumnRowPane = new ColumnRowPane() {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 20);
            }

            public void setGlobalName() {
                if (shouldResponseNameListener()) {
                    globalNameListener.setGlobalName(expandFatherName);
                }
            }
        };

        UIButton imageButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/select.png"));
        imageButton.setPreferredSize(new Dimension(24, 20));
        JPanel cc = new JPanel(new BorderLayout(LayoutConstants.HGAP_SMALL, 0));
        cc.add(customParentColumnRowPane, BorderLayout.CENTER);
        cc.add(imageButton, BorderLayout.EAST);
        customPane.add(cc, "content");
        customPane.add(new JPanel(), "none");
        customPane.setPreferredSize(new Dimension(0, 0) );
        this.add(comboBox, BorderLayout.NORTH);
        this.add(customPane, BorderLayout.CENTER);

        comboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if(comboBox.getSelectedIndex() == 2){
                    customPane.setPreferredSize(new Dimension(100, 20) );
                    cardLayout.show(customPane,"content");
                }else {
                    cardLayout.show(customPane,"none");
                    customPane.setPreferredSize(new Dimension(0, 0) );
                }
//                cardLayout.show(customPane, comboBox.getSelectedIndex() == 2 ? "content" : "none");
                if (globalNameListener != null && shouldResponseNameListener()) {
                    globalNameListener.setGlobalName(expandFatherName);
                }
            }
        });
        imageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (ePane == null || isAlreadyAddListener) {
                    return;
                }
                oldSelection = (CellSelection) ePane.getSelection();
                ePane.getGrid().setNotShowingTableSelectPane(false);
                ePane.setEditable(false);
                ePane.repaint(10);

                gridSelectionChangeListener = new SelectionListener() {

                    @Override
                    public void selectionChanged(SelectionEvent e) {
                        Selection selection = ePane.getSelection();
                        if (selection instanceof CellSelection) {
                            CellSelection cellselection = (CellSelection) selection;
                            ColumnRow cr = ColumnRow.valueOf(cellselection.getColumn(), cellselection.getRow());
                            ePane.setOldSelecton(oldSelection);
                            customParentColumnRowPane.setColumnRow(cr);
                        }
                        ePane.removeSelectionChangeListener(gridSelectionChangeListener);
                        isAlreadyAddListener = false;
                        ePane.getGrid().setNotShowingTableSelectPane(true);
                        ePane.setEditable(true);
                        ePane.repaint();
                    }
                };
                ePane.addSelectionChangeListener(gridSelectionChangeListener);
                isAlreadyAddListener = true;
            }
        });
        comboBox.setSelectedIndex(1);
    }

    /**
     * @param listener 观察者监听事件
     */
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

    /**
     * @return
     */
    public boolean shouldResponseNameListener() {
        return true;
    }

    protected abstract ColumnRow getColumnRow(CellExpandAttr cellExpandAttr);

    protected abstract boolean isParentDefault(CellExpandAttr cellExpandAttr);

    public void populate(CellExpandAttr cellExpandAttr) {
        ColumnRow columnRow = getColumnRow(cellExpandAttr);
        if (isParentDefault(cellExpandAttr)) {
            comboBox.setSelectedIndex(1);
            this.customParentColumnRowPane.populate(ColumnRow.valueOf(0, 0));
        } else if (ColumnRow.validate(columnRow)) {
            comboBox.setSelectedIndex(2);
            this.customParentColumnRowPane.populate(columnRow);
        } else {
            comboBox.setSelectedIndex(0);
            this.customParentColumnRowPane.populate(ColumnRow.valueOf(0, 0));
        }
    }


    public void setGlobalName(String name) {
        expandFatherName = name;
        this.comboBox.setGlobalName(name);
    }

    protected abstract void setValue(CellExpandAttr cellExpandAttr, boolean isDefault, ColumnRow columnRow);

    public void update(CellExpandAttr cellExpandAttr) {
        if (cellExpandAttr == null) {
            cellExpandAttr = new CellExpandAttr();
        }
        int i = comboBox.getSelectedIndex();
        switch (i) {
            case 1:
                setValue(cellExpandAttr, true, null);
                break;
            case 2:
                setValue(cellExpandAttr, false, this.customParentColumnRowPane.update());
                break;
            default:
                setValue(cellExpandAttr, false, null);
                break;
        }
    }

    public void setElementCasePane(ElementCasePane ePane) {
        this.ePane = ePane;
    }


}