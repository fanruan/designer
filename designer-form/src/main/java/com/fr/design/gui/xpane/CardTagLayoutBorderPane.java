/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.xpane;

import com.fr.base.Utils;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.BackgroundNoImagePane;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.LayoutBorderStyle;


import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * CardTagLayoutBorderPane Pane.
 */
public class CardTagLayoutBorderPane extends LayoutBorderPane {
	public CardTagLayoutBorderPane(){
		initComponents();
	}


	protected UIScrollPane initRightBottomPane(){
        this.setFontSizeComboBox(new UIComboBox(FRFontPane.FONT_SIZES));
        this.setFontNameComboBox(new UIComboBox(Utils.getAvailableFontFamilyNames4Report()));
        JPanel fontSizeTypePane = new JPanel(new BorderLayout(10,0));
        fontSizeTypePane.add(this.getFontSizeComboBox(), BorderLayout.CENTER);
        fontSizeTypePane.add(this.getFontNameComboBox(), BorderLayout.EAST);

        this.setTitleBackgroundPane(new BackgroundNoImagePane());

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p,p,p,p,p,p};
        double[] columnSize = { p, f};

        JPanel rightBottomContentPane = TableLayoutHelper.createCommonTableLayoutPane( new JComponent[][]{
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Widget-Style_Title_Format")), fontSizeTypePane},
                {new UILabel(""), initFontButtonPane()},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Widget-Style_Title_Background")), this.getTitleBackgroundPane()},
        }, rowSize, columnSize, 10);
        rightBottomContentPane.setBorder(BorderFactory.createEmptyBorder(15, 12, 10, 12));
        this.setTitlePane(new UIScrollPane(rightBottomContentPane));
        this.getTitlePane().setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Widget-Style_Title"),null));
        this.getTitlePane().setVisible(false);
        return this.getTitlePane();
    }
	
	  protected void initComponents() {

	    	this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	        this.setLayout(FRGUIPaneFactory.createBorderLayout());
	    
	        JPanel defaultPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
	        this.add(defaultPane, BorderLayout.CENTER);

	        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
	        defaultPane.add(centerPane, BorderLayout.CENTER);
	        centerPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Widget-Style_Preview"), null));

	        JPanel borderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
	        centerPane.add(borderPane, BorderLayout.CENTER);
	        borderPane.setBorder(BorderFactory.createEmptyBorder(10, 4, 10, 4));

	        this.setLayoutBorderPreviewPane(new CardTagLayoutBorderPreviewPane(this.getBorderStyle()));
	        
	        borderPane.add(this.getLayoutBorderPreviewPane(), BorderLayout.CENTER);

	        JPanel rightPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
	        defaultPane.add(rightPane, BorderLayout.EAST);
            rightPane.add(initRightTopPane(), BorderLayout.NORTH);
	  }

	protected JComponent[] getBorderTypeComp(){
		return new JComponent[]{null, null};
	}
	protected JComponent[] getBorderCornerSpinnerComp(){
		return new JComponent[]{null, null};
	}

	protected void switchBorderType(){
		return;
	}

	public LayoutBorderStyle update() {
		LayoutBorderStyle style = new LayoutBorderStyle();
		if (this.getBorderStyle() != null) {
			style.setStyle(this.getBorderStyle());
		}
		style.setBorderStyle(this.getBorderStyleCombo().getSelectedIndex());
		style.setBorder(this.getCurrentLineCombo().getSelectedLineStyle());
		style.setColor(this.getCurrentLineColorPane().getColor());
		style.setBackground(this.getBackgroundPane().update());
		style.setAlpha((float) (this.getNumberDragPane().updateBean() / this.getMaxNumber()));
		return style;
	}

	protected void populateBorderType(){
		return;
	}
	    
	protected void populateTitle(){
			return;
	    }
}