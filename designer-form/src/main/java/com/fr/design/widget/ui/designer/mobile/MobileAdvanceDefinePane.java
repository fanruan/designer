package com.fr.design.widget.ui.designer.mobile;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.form.main.Form;
import com.fr.form.ui.FormWidgetHelper;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.WSortLayout;
import com.fr.form.ui.mobile.MobileBookMark;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2019/12/24
 */
public class MobileAdvanceDefinePane extends MobileWidgetDefinePane {

    private XCreator xCreator;
    private UICheckBox useBookMarkCheck;
    private UITextField bookMarkNameField;

    public MobileAdvanceDefinePane(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel contentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.useBookMarkCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Use_BookMark"));
        this.bookMarkNameField = new UITextField() {
            @Override
            protected void initListener() {
                if (shouldResponseChangeListener()) {
                    addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            attributeChange();
                        }
                    });
                    addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                attributeChange();
                            }
                        }
                    });
                }
            }
        };
        JPanel useBookMarkPane = TableLayoutHelper.createGapTableLayoutPane(
                new Component[][]{new Component[]{useBookMarkCheck}},
                TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.HGAP_LARGE);
        final JPanel bookMarkNamePane = TableLayoutHelper.createGapTableLayoutPane(
                new Component[][]{new Component[]{new UILabel(
                        com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_BookMark_Name")), bookMarkNameField}},
                TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.HGAP_LARGE);
        this.useBookMarkCheck.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bookMarkNamePane.setVisible(MobileAdvanceDefinePane.this.useBookMarkCheck.isSelected());
            }
        });
        contentPane.add(useBookMarkPane, BorderLayout.NORTH);
        contentPane.add(bookMarkNamePane, BorderLayout.CENTER);
        UIExpandablePane uiExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_BookMark"), 280, 20, contentPane);
        JPanel wrapPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        wrapPane.add(uiExpandablePane, BorderLayout.NORTH);
        this.add(wrapPane, BorderLayout.NORTH);
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

    private void reInitAllListeners() {
        initListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, 80);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(400, 200);
    }

    @Override
    public void populate(FormDesigner designer) {
        MobileBookMark bookMark = xCreator.toData().getMobileBookMark();
        this.bookMarkNameField.setText(bookMark.getBookMarkName());
        if (bookMark.isFrozen()) {
            this.useBookMarkCheck.setSelected(false);
            this.useBookMarkCheck.setEnabled(false);
        } else {
            this.useBookMarkCheck.setSelected(bookMark.isUseBookMark());
        }
        this.bindListeners2Widgets();
    }

    @Override
    public void update() {
        MobileBookMark bookMark = xCreator.toData().getMobileBookMark();
        bookMark.setUseBookMark(this.useBookMarkCheck.isSelected());
        String newBookMarkName = this.bookMarkNameField.getText();
        if (ComparatorUtils.equals(newBookMarkName, bookMark.getBookMarkName())) {
            return;
        }
        if (!isExist(newBookMarkName)) {
            bookMark.setBookMarkName(newBookMarkName);
        } else {
            FineJOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
                                              com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_BookMark_Rename_Failure"),
                                              com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Joption_News"),
                                              JOptionPane.ERROR_MESSAGE, IOUtils.readIcon("com/fr/design/form/images/joption_failure.png"));
            this.bookMarkNameField.setText(bookMark.getBookMarkName());
        }
    }

    private boolean isExist(String name) {
        Form form = WidgetPropertyPane.getInstance().getEditingFormDesigner().getTarget();
        WLayout container = form.getContainer();
        WSortLayout wSortLayout = (WSortLayout) container.getWidget(container.getWidgetCount() - 1);
        Iterator<String> iterator = wSortLayout.getMobileWidgetIterator();
        while (iterator.hasNext()) {
            Widget widget = form.getWidgetByName(iterator.next());
            if (widget != null && ComparatorUtils.equals(widget.getMobileBookMark().getBookMarkName(), name)) {
                return true;
            }
        }
        return false;
    }

}
