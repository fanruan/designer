package com.fr.design.webattr.printsettings;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.print.PrintAttr;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by plough on 2018/3/1.
 */
public class PrintSettingPane extends BasicPane {
    private UIRadioButton noClientPrintRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_No_Client_Print"));
    private UIRadioButton nativePrintRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_Native_Print"));

    private NoClientPrintSettingPane noClientPrintSettingPane;
    private NativePrintSettingPane nativePrintSettingPane;
    private CardLayout printCard;
    private JPanel printPane;

    public PrintSettingPane() {
        initComponents();
        initListener();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel allPanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(allPanel, BorderLayout.CENTER);
        JPanel north = FRGUIPaneFactory.createVerticalFlowLayout_S_Pane(true);
        allPanel.add(north, BorderLayout.NORTH);
        ButtonGroup buttonGroup = new ButtonGroup();
        noClientPrintRadioButton.setSelected(true);
        buttonGroup.add(noClientPrintRadioButton);
        buttonGroup.add(nativePrintRadioButton);
        noClientPrintRadioButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));
        JPanel radioGroupPane = GUICoreUtils.createFlowPane(new Component[] {
                noClientPrintRadioButton, nativePrintRadioButton}, FlowLayout.LEFT, 0, 0);
        north.add(radioGroupPane);

        noClientPrintSettingPane = new NoClientPrintSettingPane();
        nativePrintSettingPane = new NativePrintSettingPane();
        printCard = new CardLayout();
        printPane = new JPanel();
        printPane.setLayout(printCard);
        printPane.add(noClientPrintRadioButton.getText(), noClientPrintSettingPane);
        printPane.add(nativePrintRadioButton.getText(), nativePrintSettingPane);

        north.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        allPanel.add(printPane, BorderLayout.CENTER);
    }

    private void initListener() {
        noClientPrintRadioButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    printCard.show(printPane, noClientPrintRadioButton.getText());
                }
            }
        });
        nativePrintRadioButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    printCard.show(printPane, nativePrintRadioButton.getText());
                }
            }
        });
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Print_Setting");
    }

    public void populate(PrintAttr printAttr) {
        if (printAttr == null) {
            return;
        }
        if (printAttr.getPrintType() == PrintAttr.NO_CLIENT_PRINT) {
            noClientPrintRadioButton.setSelected(true);
        } else {
            nativePrintRadioButton.setSelected(true);
        }
        noClientPrintSettingPane.populate(printAttr.getNoClientPrintAttr());
        nativePrintSettingPane.populate(printAttr.getNativePrintAttr());
    }

    public PrintAttr updateBean() {
        PrintAttr printAttr = new PrintAttr();
        printAttr.setPrintType(noClientPrintRadioButton.isSelected() ?
                PrintAttr.NO_CLIENT_PRINT : PrintAttr.NATIVE_PRINT);
        if (noClientPrintRadioButton.isSelected()) {
            printAttr.setPrintType(PrintAttr.NO_CLIENT_PRINT);
            noClientPrintSettingPane.update(printAttr.getNoClientPrintAttr());
        } else {
            printAttr.setPrintType(PrintAttr.NATIVE_PRINT);
            nativePrintSettingPane.update(printAttr.getNativePrintAttr());
        }
        return printAttr;
    }
}
