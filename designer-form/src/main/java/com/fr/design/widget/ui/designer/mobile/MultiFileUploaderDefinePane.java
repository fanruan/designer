package com.fr.design.widget.ui.designer.mobile;

import com.fr.base.mobile.FileUploadModeState;
import com.fr.base.mobile.MultiFileUploaderMobileAttr;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.properties.items.Item;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.MultiFileEditor;


import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by plough on 2018/4/19.
 */
public class MultiFileUploaderDefinePane extends MobileWidgetDefinePane {
    private static final Item[] ITEMS = {
            new Item(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Take_Photos_And_Choose_From_Album"), FileUploadModeState.TAKE_PHOTOS_AND_CHOOSE_FROM_ALBUM),
            new Item(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Only_Take_Photos"), FileUploadModeState.ONLY_TAKE_PHOTOS)
    };

    private XCreator xCreator; // 当前选中控件的xCreator
    private UIComboBox uploadModeComboBox;// 上传方式下拉框

    public MultiFileUploaderDefinePane(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel mobileSettingsPane;
        mobileSettingsPane = getMobileSettingsPane();

        this.add(mobileSettingsPane, BorderLayout.NORTH);
        this.repaint();
    }

    private UIExpandablePane getMobileSettingsPane() {
        initUploadModeComboBox();

        // 以后可能会扩展，还是用 TableLayout 吧
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

    private void bindListeners2Widgets() {
        reInitAllListeners();
        AttributeChangeListener changeListener = new AttributeChangeListener() {
            @Override
            public void attributeChange() {
                update();
            }
        };
        this.addAttributeChangeListener(changeListener);
    }

    /**
     * 后台初始化所有事件.
     */
    private void reInitAllListeners() {
        initListener(this);
    }

    @Override
    public void populate(FormDesigner designer) {
        MultiFileUploaderMobileAttr mobileAttr = ((MultiFileEditor)xCreator.toData()).getMobileAttr();
        FileUploadModeState fileUploadModeState = mobileAttr.getFileUploadModeState();
        uploadModeComboBox.setSelectedIndex(fileUploadModeState.getState());
        // 数据 populate 完成后，再设置监听
        this.bindListeners2Widgets();
    }

    @Override
    public void update() {
        MultiFileUploaderMobileAttr mobileAttr = ((MultiFileEditor)xCreator.toData()).getMobileAttr();
        mobileAttr.setFileUploadModeState((FileUploadModeState) ((Item)uploadModeComboBox.getSelectedItem()).getValue());
        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified(); // 触发设计器保存按钮亮起来
    }
}
