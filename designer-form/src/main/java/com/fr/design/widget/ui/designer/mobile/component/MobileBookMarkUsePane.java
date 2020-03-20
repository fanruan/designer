package com.fr.design.widget.ui.designer.mobile.component;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.form.ui.container.WLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/2/12
 */
public class MobileBookMarkUsePane extends BasicPane {

    private UICheckBox showHierarchicalBookmarksCheck;

    public MobileBookMarkUsePane() {
       initComponent();
    }


    private void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.showHierarchicalBookmarksCheck = new UICheckBox(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Show_BookMark"), true) {
            @Override
            protected void initListener() {
                this.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        attributeChange();
                    }
                });
            }
        };
        UILabel hintLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Show_BookMark_Hint"));
        hintLabel.setForeground(Color.GRAY);
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double[] columnSize = {f};
        int[][] rowCount = {{1}, {1}};
        Component[][] components = new Component[][]{
                new Component[]{this.showHierarchicalBookmarksCheck},
                new Component[]{hintLabel}
        };
        JPanel wrapPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel showBookMarkPane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount,
                IntervalConstants.INTERVAL_L1,
                IntervalConstants.INTERVAL_L1);
        showBookMarkPane.setBorder(
                BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, IntervalConstants.INTERVAL_L1, 0));
        wrapPane.add(showBookMarkPane, BorderLayout.CENTER);
        this.add(showBookMarkPane, BorderLayout.CENTER);
    }

    public void populate(XCreator xCreator) {
        WLayout wLayout = ((WLayout) xCreator.toData());
        this.showHierarchicalBookmarksCheck.setSelected(wLayout.isShowBookmarks());
    }

    public void update(XCreator xCreator) {
        WLayout wLayout = ((WLayout) xCreator.toData());
        wLayout.setShowBookmarks(showHierarchicalBookmarksCheck.isSelected());
        WidgetPropertyPane.getInstance().getEditingFormDesigner().getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
    }

    @Override
    protected String title4PopupWindow() {
        return "MobileBookMarkUsePane";
    }
}
