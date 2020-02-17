package com.fr.design.mainframe.mobile.ui;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.i18n.Toolkit;
import com.fr.form.ui.mobile.MobileCollapsedStyle;
import com.fr.general.ComparatorUtils;

import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/2/13
 */
public class MobileComboBoxDialogEditor extends BasicPane {

    private static final Dimension DEFAULT_DIMENSION = new Dimension(600, 400);
    private static final Dimension COMBOX_DIMENSION = new Dimension(135,20);
    private static final String NONE = Toolkit.i18nText("无");
    private static final String CUSTOM = Toolkit.i18nText("自定义");


    private MobileCollapsedStyle style;
    private MobileCollapsedStylePane stylePane;
    private UIComboBox comboBox;
    private ActionListener listener;

    public MobileComboBoxDialogEditor(MobileCollapsedStylePane stylePane) {
        this.stylePane = stylePane;
        this.comboBox = new UIComboBox(new Object[] {NONE, CUSTOM});
        this.comboBox.setPreferredSize(COMBOX_DIMENSION);
        this.comboBox.setSelectedItem(NONE);
        listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ComparatorUtils.equals(MobileComboBoxDialogEditor.this.comboBox.getSelectedItem(), CUSTOM)) {
                    showEditorPane();
                }
            }
        };
        this.comboBox.addActionListener(listener);
        this.add(comboBox);

    }

    public boolean isSelectedCustom()  {
        return ComparatorUtils.equals(CUSTOM, this.comboBox.getSelectedItem());
    }

    public void setSelected(boolean selectedCustom) {
        this.comboBox.removeActionListener(listener);
        this.comboBox.setSelectedItem(selectedCustom ? CUSTOM : NONE);
        this.comboBox.addActionListener(listener);
    }

    private void showEditorPane() {
        stylePane.setPreferredSize(DEFAULT_DIMENSION);
        BasicDialog dialog = stylePane.showWindow(SwingUtilities.getWindowAncestor(this));
        dialog.addDialogActionListener(new DialogActionAdapter() {
            @Override
            public void doOk() {
                MobileCollapsedStyle style = stylePane.updateBean();
                style.setCollapsedWork(true);
                setStyle(style);
                MobileComboBoxDialogEditor.this.firePropertyChanged();
            }
        });
        stylePane.populateBean(getStyle());
        dialog.setVisible(true);
    }


    protected void firePropertyChanged() {
        HistoryTemplateListCache.getInstance().getCurrentEditingTemplate().fireTargetModified();
    }

    public MobileCollapsedStyle getStyle() {
        return style;
    }

    public void setStyle(MobileCollapsedStyle style) {
        this.style = style;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }




}
