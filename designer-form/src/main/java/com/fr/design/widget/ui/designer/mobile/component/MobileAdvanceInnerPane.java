package com.fr.design.widget.ui.designer.mobile.component;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.form.main.Form;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.WSortLayout;
import com.fr.form.ui.mobile.MobileBookMark;
import com.fr.form.ui.widget.CRBoundsWidget;
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
import java.util.List;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/4/17
 */
public class MobileAdvanceInnerPane extends BasicPane {

    private XCreator xCreator;
    private UICheckBox useBookMarkCheck;
    private UITextField bookMarkNameField;


    public MobileAdvanceInnerPane(XCreator xCreator) {
        this.xCreator = xCreator;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        initComponent();
    }

    private void initComponent() {
        JPanel contentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.useBookMarkCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Use_BookMark"), false);
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
                boolean selected = MobileAdvanceInnerPane.this.useBookMarkCheck.isSelected();
                Widget widget = MobileAdvanceInnerPane.this.xCreator.toData();
                MobileBookMark bookMark = widget.getMobileBookMark();
                bookMarkNamePane.setVisible(selected);
                if (selected && StringUtils.isEmpty(bookMark.getBookMarkName())) {
                    String name = widget.getWidgetName();
                    MobileAdvanceInnerPane.this.bookMarkNameField.setText(name);
                    bookMark.setBookMarkName(name);
                }
            }
        });
        bookMarkNamePane.setVisible(xCreator.toData().getMobileBookMark().isUseBookMark());
        contentPane.add(useBookMarkPane, BorderLayout.NORTH);
        contentPane.add(bookMarkNamePane, BorderLayout.CENTER);
        this.add(contentPane);
        initData();
    }

    private void initData() {
        MobileBookMark bookMark = xCreator.toData().getMobileBookMark();
        String bookMarkName = bookMark.getBookMarkName();
        if (StringUtils.isEmpty(bookMarkName)) {
            String widgetName = xCreator.toData().getWidgetName();
            this.bookMarkNameField.setText(widgetName);
            bookMark.setBookMarkName(widgetName);
        } else {
            this.bookMarkNameField.setText(bookMarkName);
        }
    }

    public void populate() {
        MobileBookMark bookMark = xCreator.toData().getMobileBookMark();
        this.bookMarkNameField.setText(bookMark.getBookMarkName());
        if (bookMark.isFrozen()) {
            this.useBookMarkCheck.setSelected(false);
            this.useBookMarkCheck.setEnabled(false);
        } else {
            this.useBookMarkCheck.setSelected(bookMark.isUseBookMark());
        }
    }

    public void update() {
        MobileBookMark bookMark = xCreator.toData().getMobileBookMark();
        bookMark.setUseBookMark(this.useBookMarkCheck.isSelected());
        String newBookMarkName = this.bookMarkNameField.getText();
        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified();
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
        List<String> list = wSortLayout.getOrderedMobileWidgetList();
        for (String value : list) {
            Widget widget = form.getWidgetByName(value);
            if (widget != null && ComparatorUtils.equals(widget.getMobileBookMark().getBookMarkName(), name)) {
                return true;
            }
            CRBoundsWidget boundsWidget = (CRBoundsWidget) wSortLayout.getWidget(value);
            if (boundsWidget != null && ComparatorUtils.equals(boundsWidget.getWidget().getMobileBookMark().getBookMarkName(), name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String title4PopupWindow() {
        return "MobileAdvanceInnerPane";
    }
}
