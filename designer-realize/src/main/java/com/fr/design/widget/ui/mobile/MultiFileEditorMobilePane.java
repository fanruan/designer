package com.fr.design.widget.ui.mobile;

import com.fr.base.mobile.FileUploadModeState;
import com.fr.base.mobile.MultiFileUploaderMobileAttr;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.mobile.WidgetMobilePane;
import com.fr.form.ui.MultiFileEditor;
import com.fr.form.ui.Widget;


import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by plough on 2018/4/25.
 */
public class MultiFileEditorMobilePane extends WidgetMobilePane {
    private static final Item[] ITEMS = {
            new Item(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Take_Photos_And_Choose_From_Album"), FileUploadModeState.TAKE_PHOTOS_AND_CHOOSE_FROM_ALBUM),
            new Item(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Only_Take_Photos"), FileUploadModeState.ONLY_TAKE_PHOTOS)
    };

    private UIComboBox uploadModeComboBox;// 上传方式下拉框

    protected void init() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.add(getMobileSettingsPane(), BorderLayout.NORTH);
    }

    /**
     * 从 widget 中提取数据展示在属性面板中
     *
     * @param widget
     */
    public void populate(Widget widget) {
        MultiFileUploaderMobileAttr mobileAttr = ((MultiFileEditor)widget).getMobileAttr();
        FileUploadModeState fileUploadModeState = mobileAttr.getFileUploadModeState();
        uploadModeComboBox.setSelectedIndex(fileUploadModeState.getState());
    }

    /**
     * 从属性面板把数据保存到 widget 中
     * @param widget
     */
    public void update(Widget widget) {
        MultiFileUploaderMobileAttr mobileAttr = ((MultiFileEditor)widget).getMobileAttr();
        mobileAttr.setFileUploadModeState((FileUploadModeState) ((Item)uploadModeComboBox.getSelectedItem()).getValue());
    }

    private UIExpandablePane getMobileSettingsPane() {
        initUploadModeComboBox();

        // 以后可能会扩展
        Component[][] components = new Component[][]{
                new Component[] {new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Upload_Mode"), SwingConstants.LEFT), uploadModeComboBox}
        };

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {p,f};
        int[][] rowCount = {{1, 1}};
        final JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 30, LayoutConstants.VGAP_LARGE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        final JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);

        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Terminal"), 280, 20, panelWrapper);
    }

    private void initUploadModeComboBox() {
        this.uploadModeComboBox = new UIComboBox(ITEMS);
    }
}
