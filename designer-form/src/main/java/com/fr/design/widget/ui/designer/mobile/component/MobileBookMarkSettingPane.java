package com.fr.design.widget.ui.designer.mobile.component;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.mobile.ui.MobileBookMarkStylePane;
import com.fr.design.mainframe.widget.accessibles.UneditableAccessibleEditor;
import com.fr.design.mainframe.widget.wrappers.MobileBookMarkStyleWrapper;
import com.fr.form.ui.container.WSortLayout;
import com.fr.form.ui.mobile.MobileBookMarkStyle;

import javax.swing.*;
import java.awt.*;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2019/12/18
 */
public class MobileBookMarkSettingPane extends BasicPane {

    private AccessibleMobileBookMarkStyleEditor mobileBookMarkStyleEditor;
    private MobileBookMarkUsePane showBookMarkPane;

    public MobileBookMarkSettingPane() {
        initComponent();
    }

    private void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.mobileBookMarkStyleEditor = new AccessibleMobileBookMarkStyleEditor(new MobileBookMarkStylePane());
        JPanel booKMarkPane = TableLayoutHelper.createGapTableLayoutPane(
                new Component[][]{new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText(
                        "Fine-Design_Mobile_BookMark_Style")), this.mobileBookMarkStyleEditor}},
                TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.HGAP_LARGE
        );
         this.showBookMarkPane = new MobileBookMarkUsePane();
        JPanel wrapPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        wrapPane.add(booKMarkPane, BorderLayout.NORTH);
        wrapPane.add(showBookMarkPane, BorderLayout.CENTER);
        this.add(wrapPane, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return "MobileBookMarkSettingPane";
    }

    public void populate(XCreator xCreator) {
        WSortLayout wSortLayout = ((WSortLayout) xCreator.toData());
        this.mobileBookMarkStyleEditor.setValue(wSortLayout.getMobileBookMarkStyle());
        this.showBookMarkPane.populate(xCreator);
    }

    public void update(XCreator xCreator) {
        WSortLayout wSortLayout = ((WSortLayout) xCreator.toData());
        wSortLayout.setMobileBookMarkStyle((MobileBookMarkStyle) mobileBookMarkStyleEditor.getValue());
        this.showBookMarkPane.update(xCreator);
    }


    class AccessibleMobileBookMarkStyleEditor extends UneditableAccessibleEditor {

        private MobileBookMarkStylePane mobileBookMarkStylePane;

        public AccessibleMobileBookMarkStyleEditor(MobileBookMarkStylePane mobileBookMarkStylePane) {
            super(new MobileBookMarkStyleWrapper());
            this.mobileBookMarkStylePane = mobileBookMarkStylePane;
        }

        @Override
        public void showEditorPane() {
            mobileBookMarkStylePane.setPreferredSize(BasicDialog.MEDIUM);
            BasicDialog dialog = mobileBookMarkStylePane.showWindow(SwingUtilities.getWindowAncestor(this));
            dialog.addDialogActionListener(new DialogActionAdapter() {
                @Override
                public void doOk() {
                    MobileBookMarkStyle mobileBookMarkStyle = mobileBookMarkStylePane.updateBean();
                    WSortLayout wSortLayout = (WSortLayout) WidgetPropertyPane.getInstance().getEditingFormDesigner().getSelectionModel().getSelection().getSelectedCreator().toData();
                    setValue(mobileBookMarkStyle);
                    wSortLayout.setMobileBookMarkStyle(mobileBookMarkStyle);
                    fireStateChanged();
                }
            });
            mobileBookMarkStylePane.populateBean((MobileBookMarkStyle) getValue());
            dialog.setVisible(true);
        }
    }


}