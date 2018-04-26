/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.headerfooter;

import com.fr.base.BaseFormula;
import com.fr.base.headerfooter.DateHFElement;
import com.fr.base.headerfooter.FormulaHFElement;
import com.fr.base.headerfooter.HFElement;
import com.fr.base.headerfooter.ImageHFElement;
import com.fr.base.headerfooter.NewLineHFElement;
import com.fr.base.headerfooter.NumberOfPageHFElement;
import com.fr.base.headerfooter.PageNumberHFElement;
import com.fr.base.headerfooter.TextHFElement;
import com.fr.base.headerfooter.TimeHFElement;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.FRFontPane;
import com.fr.design.style.FormatPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The dialog used to edit
 */
public class HFAttributesEditDialog extends BasicPane {

    private HFElement hfElement = null;
    private UITabbedPane tabbedPane;
    //some tab pane.
    private TextPane textPane;
    private JPanel formulaPane;
    private UITextField formulaContentField;
    private FRFontPane frFontPane;
    private FormatPane formatPane;
    private ImagePane imagePane;
    private NewLinePane newLinePane;
    private UIButton moveLeftButton = null;
    private UIButton moveRightButton = null;
    private UIButton deleteButton = null;
    private java.util.List moveActionListeners = new ArrayList();

    /**
     * Apply default pane.
     */
    public HFAttributesEditDialog() {
        this.setLayout(new BorderLayout(0, 4));
	
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(centerPane);

        //top
        JPanel topControlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerPane.add(topControlPane, BorderLayout.NORTH);
//        topControlPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel topControlButtonPane =FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        topControlPane.add(topControlButtonPane, BorderLayout.WEST);


        moveLeftButton = new UIButton(Inter.getLocText("HF-Move_Left"));
        moveLeftButton.setMnemonic('L');
        moveLeftButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doMoveLeft();
            }
        });
        topControlButtonPane.add(moveLeftButton);

        moveRightButton = new UIButton(Inter.getLocText("HF-Move_Right"));
        moveRightButton.setMnemonic('R');
        moveRightButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doMoveRight();
            }
        });
        topControlButtonPane.add(moveRightButton);

        deleteButton = new UIButton(Inter.getLocText("HF-Delete_it"));
        deleteButton.setMnemonic('D');
        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doDelete();
            }
        });
        topControlButtonPane.add(deleteButton);

        //tabbedpane
        tabbedPane = new UITabbedPane();
        centerPane.add(tabbedPane, BorderLayout.CENTER);

        //init all tab panes.
        textPane = new TextPane();
        formulaPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();

        formulaContentField = new UITextField();
        formulaPane.add(new UILabel(Inter.getLocText("Value") + ":"));
        formulaPane.add(Box.createHorizontalStrut(2));
        UILabel label = new UILabel("=");
        label.setFont(new Font("Dialog", Font.BOLD, 12));
        formulaPane.add(label);
        formulaContentField = new UITextField(20);
        formulaPane.add(formulaContentField);

        UIButton formulaButton = new UIButton("...");
        formulaPane.add(formulaButton);
        formulaButton.setToolTipText(Inter.getLocText("Formula") + "...");
        formulaButton.setPreferredSize(new Dimension(25, formulaContentField.getPreferredSize().height));
        formulaButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                BaseFormula valueFormula = BaseFormula.createFormulaBuilder().build();
                String text = formulaContentField.getText();
                if (text == null || text.length() <= 0) {
                    valueFormula.setContent("");
                } else {
                    valueFormula.setContent(text);
                }

                final UIFormula formulaPane = FormulaFactory.createFormulaPane();
                formulaPane.populate(valueFormula);
                formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor((Component)formulaPane), new DialogActionAdapter() {

                    @Override
                    public void doOk() {
                        BaseFormula valueFormula = formulaPane.update();
                        if (valueFormula.getContent().length() <= 1) {
                            formulaContentField.setText("");
                        } else {
                            formulaContentField.setText(valueFormula.getContent().substring(1));
                        }
                    }
                }).setVisible(true);
            }
        });
        frFontPane = new FRFontPane();
        formatPane = new FormatPane();
        imagePane = new ImagePane();
        newLinePane = new NewLinePane();
    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("HF-Header_and_Footer");
    }

    /**
     * Populate
     */
    public void populate(HFElement hfElement) {
        this.populate(hfElement, false);
    }

    /**
     * Populate
     */
    public void populate(HFElement hfElement, boolean isInsert) {
        this.hfElement = hfElement;
        this.tabbedPane.removeAll();
        if (this.hfElement != null) {
            if (ComparatorUtils.equals(this.hfElement.getClass(), TextHFElement.class)) {
                this.addTextPaneToTab();
                this.addReportFontPaneToTab();
                this.textPane.populate(((TextHFElement) hfElement).getText());
                this.frFontPane.populate(((TextHFElement) hfElement).getFRFont());
            } else if (ComparatorUtils.equals(this.hfElement.getClass(), FormulaHFElement.class)) {
                this.addFormulaPaneToTab();
                this.addReportFontPaneToTab();
                this.formulaContentField.setText(((FormulaHFElement) hfElement).getFormulaContent());
                this.frFontPane.populate(((FormulaHFElement) hfElement).getFRFont());
            } else if (ComparatorUtils.equals(this.hfElement.getClass(), PageNumberHFElement.class)) {
                this.addReportFontPaneToTab();
                this.frFontPane.populate(((PageNumberHFElement) hfElement).getFRFont());
            } else if (ComparatorUtils.equals(this.hfElement.getClass(), NumberOfPageHFElement.class)) {
                this.addReportFontPaneToTab();
                this.frFontPane.populate(((NumberOfPageHFElement) hfElement).getFRFont());
            } else if (ComparatorUtils.equals(this.hfElement.getClass(), DateHFElement.class)) {
                this.addReportFontPaneToTab();
                this.addFormatPaneToTab();
                this.frFontPane.populate(((DateHFElement) hfElement).getFRFont());
                this.formatPane.populate(((DateHFElement) hfElement).getFormat());
            } else if (ComparatorUtils.equals(this.hfElement.getClass(), TimeHFElement.class)) {
                this.addReportFontPaneToTab();
                this.addFormatPaneToTab();
                this.frFontPane.populate(((TimeHFElement) hfElement).getFRFont());
                this.formatPane.populate(((TimeHFElement) hfElement).getFormat());
            } else if (ComparatorUtils.equals(this.hfElement.getClass(), ImageHFElement.class)) {
                this.addImagePaneToTab();
                this.imagePane.populate(((ImageHFElement) hfElement).getImage());
            } else if (ComparatorUtils.equals(this.hfElement.getClass(), NewLineHFElement.class)) {
                this.addNewLinePaneToTab();
            }
        }
        this.tabbedPane.revalidate();
        if (isInsert) {
            this.moveLeftButton.setEnabled(false);
            this.moveRightButton.setEnabled(false);
            deleteButton.setEnabled(false);
        } else {
            this.moveLeftButton.setEnabled(true);
            this.moveRightButton.setEnabled(true);
            deleteButton.setEnabled(true);
        }
    }

    /**
     * Add text pane.
     */
    private void addTextPaneToTab() {
        this.tabbedPane.addTab(Inter.getLocText("Text"), this.textPane);
    }

    /**
     * Add parameter pane.
     */
    private void addFormulaPaneToTab() {
        this.tabbedPane.addTab(Inter.getLocText("Formula"), this.formulaPane);
    }

    private void addReportFontPaneToTab() {
        this.tabbedPane.addTab(Inter.getLocText("FRFont"), this.frFontPane);
    }

    private void addFormatPaneToTab() {
        this.tabbedPane.addTab(Inter.getLocText("Format"), this.formatPane);
    }

    private void addImagePaneToTab() {
        this.tabbedPane.addTab(Inter.getLocText("Image"), this.imagePane);
    }

    private void addNewLinePaneToTab() {
        this.tabbedPane.addTab(Inter.getLocText("HF-New_Line"), this.newLinePane);
    }

    /**
     * update
     */
    public void update() {
        if (this.hfElement == null) {
            return;
        }

        //update
        if (ComparatorUtils.equals(this.hfElement.getClass(), TextHFElement.class)) {
            ((TextHFElement) hfElement).setText(this.textPane.update());
            ((TextHFElement) hfElement).setFRFont(this.frFontPane.update());
        } else if (ComparatorUtils.equals(this.hfElement.getClass(),FormulaHFElement.class)) {
            ((FormulaHFElement) hfElement).setFormulaContent(this.formulaContentField.getText());
            ((FormulaHFElement) hfElement).setFRFont(this.frFontPane.update());
        } else if (ComparatorUtils.equals(this.hfElement.getClass(),PageNumberHFElement.class)) {
            ((PageNumberHFElement) hfElement).setFRFont(this.frFontPane.update());
        } else if (ComparatorUtils.equals(this.hfElement.getClass(),NumberOfPageHFElement.class)) {
            ((NumberOfPageHFElement) hfElement).setFRFont(this.frFontPane.update());
        } else if (ComparatorUtils.equals(this.hfElement.getClass(),DateHFElement.class)) {
            ((DateHFElement) hfElement).setFRFont(this.frFontPane.update());
            ((DateHFElement) hfElement).setFormat(this.formatPane.update());
        } else if (ComparatorUtils.equals(this.hfElement.getClass(),TimeHFElement.class)) {
            ((TimeHFElement) hfElement).setFRFont(this.frFontPane.update());
            ((TimeHFElement) hfElement).setFormat(this.formatPane.update());
        } else if (ComparatorUtils.equals(this.hfElement.getClass(),ImageHFElement.class)) {
            ((ImageHFElement) hfElement).setImage(this.imagePane.update());
        } else if (ComparatorUtils.equals(this.hfElement.getClass(),NewLineHFElement.class)) {
            //Cannot be edit.
            //((ImageHFElement) hfElement).setImage(this.newLinePane.update());
        }
    }

    /**
     * Do OK.
     */
    public void doOK() {
        update();

        dialogExit();
    }

    public void addMoveActionListener(MoveActionListener l) {
        this.moveActionListeners.add(l);
    }

    /**
     * move left action listener
     */
    public void doMoveLeft() {
        for (int i = 0, len = moveActionListeners.size(); i < len; i++) {
            MoveActionListener l = (MoveActionListener) moveActionListeners.get(i);
            if (l != null) {
                l.doMoveLeft();
            }
        }
    }

    /**
     * move right action listener
     */
    public void doMoveRight() {
        for (int i = 0, len = moveActionListeners.size(); i < len; i++) {
            MoveActionListener l = (MoveActionListener) moveActionListeners.get(i);
            if (l != null) {
                l.doMoveRight();
            }
        }
    }

    /**
     * Delete action listener
     */
    public void doDelete() {
        for (int i = 0, len = moveActionListeners.size(); i < len; i++) {
            MoveActionListener l = (MoveActionListener) moveActionListeners.get(i);
            if (l != null) {
                l.doDelete();
            }
        }
    }

    /**
     * Dialog exit.
     */
    public void dialogExit() {
        this.setVisible(false);
    }

    private class TextPane extends JPanel {

        public TextPane() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            textArea = new UITextArea();
            this.add(new JScrollPane(textArea));
        }

        public void populate(String text) {
            this.textArea.setText(text);
        }

        public String update() {
            return this.textArea.getText();
        }
        private UITextArea textArea;
    }

    private class NewLinePane extends JPanel {

        public NewLinePane() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            UILabel label = new UILabel(Inter.getLocText("HF-NewLine_Des"));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(label, BorderLayout.CENTER);
            
        }
    }
}