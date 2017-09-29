/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.xpane;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.Utils;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.BackgroundNoImagePane;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.JForm;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.WidgetTitle;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.Constants;

/**
 * CardTagLayoutBorderPane Pane.
 */
public class CardTagLayoutBorderPane extends LayoutBorderPane {
	public CardTagLayoutBorderPane(){
		initComponents();
	}
	
	protected UIScrollPane initRightBottomPane(){
        this.setFontSizeComboBox(new UIComboBox(FRFontPane.getFontSizes()));
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
                {new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Title_Format")), fontSizeTypePane},
                {new UILabel(""), initFontButtonPane()},
                {new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Title_Background")), this.getTitleBackgroundPane()},
        }, rowSize, columnSize, 10);
        rightBottomContentPane.setBorder(BorderFactory.createEmptyBorder(15, 12, 10, 12));
        this.setTitlePane(new UIScrollPane(rightBottomContentPane));
        this.getTitlePane().setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer-Widget-Style_Title"),null));
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
	        centerPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer-Widget-Style_Preview"), null));

	        JPanel borderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
	        centerPane.add(borderPane, BorderLayout.CENTER);
	        borderPane.setBorder(BorderFactory.createEmptyBorder(10, 4, 10, 4));

	        this.setLayoutBorderPreviewPane(new CardTagLayoutBorderPreviewPane(this.getBorderStyle())); 
	        
	        borderPane.add(this.getLayoutBorderPreviewPane(), BorderLayout.CENTER);

	        JPanel rightPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
	        defaultPane.add(rightPane, BorderLayout.EAST);
	        rightPane.add(initRightBottomPane(), BorderLayout.CENTER);
	        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
	        if (!jTemplate.isJWorkBook() && ((JForm)jTemplate).isSelectRootPane()){
	            //界面上表单主体只有背景和透明度可以设置
	            rightPane.add(initBodyRightTopPane(), BorderLayout.NORTH);
	        } else {
	            rightPane.add(initRightTopPane(), BorderLayout.NORTH);
	        }
	  }
	  
	  
	    public LayoutBorderStyle update() {
	        LayoutBorderStyle style = new LayoutBorderStyle();
	        style.setType(this.getBorderTypeCombo().getSelectedIndex());
	        style.setBorderStyle(this.getBorderStyleCombo().getSelectedIndex());
	        style.setBorder(this.getCurrentLineCombo().getSelectedLineStyle());
	        style.setColor(this.getCurrentLineColorPane().getColor());
	        style.setBackground(this.getBackgroundPane().update());
	        style.setAlpha((float)(this.getNumberDragPane().updateBean()/this.getMaxNumber()));

	        WidgetTitle title = style.getTitle() == null ? new WidgetTitle() : style.getTitle();
	        title.setTextObject("title");
	        FRFont frFont = title.getFrFont();
	        frFont = frFont.applySize((Integer)this.getFontSizeComboBox().getSelectedItem());
	        frFont = frFont.applyName(this.getFontNameComboBox().getSelectedItem().toString());
	        frFont = frFont.applyForeground(this.getColorSelectPane().getColor());
	        frFont = updateItalicBold(frFont);
	        int line = this.getUnderline().isSelected() ? this.getUnderlineCombo().getSelectedLineStyle() : Constants.LINE_NONE;
	        frFont = frFont.applyUnderline(line);
	        title.setFrFont(frFont);
	        title.setBackground(this.getTitleBackgroundPane().update());
	        style.setTitle(title);
			return style;
		}
	    
	    
	    protected void populateTitle(){
	        WidgetTitle widgetTitle = this.getBorderStyle() == null ? new WidgetTitle() : this.getBorderStyle().getTitle();
	        widgetTitle = widgetTitle == null ? new WidgetTitle() : widgetTitle;
	        populateFont(widgetTitle);
	        this.getUnderline().addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                paintPreviewPane();
	            }
	        });
	        this.getUnderlineCombo().addItemListener(new ItemListener() {
	            @Override
	            public void itemStateChanged(ItemEvent e) {
	                paintPreviewPane();
	            }
	        });

	    	this.getTitleBackgroundPane().populateBean(widgetTitle.getBackground());
	        this.getTitleBackgroundPane().addChangeListener(new ChangeListener() {
	            @Override
	            public void stateChanged(ChangeEvent e) {
	                paintPreviewPane();
	            }
	        });
	        paintPreviewPane();
	    }
}