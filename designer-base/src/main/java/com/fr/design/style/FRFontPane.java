/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.style;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.fr.base.FRContext;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.DefaultValues;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * Pane to edit Font.
 */
public class FRFontPane extends BasicPane {

	private FontFamilyPane familyPane;
	private FontSizeStylePane fontSizeStylePane;

    //foreground.
    private ColorSelectBox foregroundColorPane;

    //underline
    private LineComboBox underlineCombo;

    //effects.
    private UICheckBox isStrikethroughCheckBox;
    private UICheckBox isShadowCheckBox;
    private UICheckBox isSuperscriptCheckBox;
    private UICheckBox isSubscriptCheckBox;

    private FRFontPreviewArea preview;
    
    private EventListenerList eventChangeList = new EventListenerList();
    
    public static  Integer[] Font_Sizes = {new Integer(6), new Integer(8), new Integer(9),
        new Integer(10), new Integer(11), new Integer(12),
        new Integer(14), new Integer(16), new Integer(18),
        new Integer(20), new Integer(22), new Integer(24),
        new Integer(26), new Integer(28), new Integer(36),
        new Integer(48), new Integer(72)};
    
    public FRFontPane() {
    	this.initComponents();
    }
      
    protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        
        // 纵向布局 放置list 和 下划线 颜色
        JPanel listVPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        this.add(listVPane, BorderLayout.NORTH);
        
        JPanel listHPane =FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
        listVPane.add(listHPane);

        // 名字
        familyPane = new FontFamilyPane();
        listHPane.add(familyPane);
        
        // 字形和大小
        fontSizeStylePane = new FontSizeStylePane();
        listHPane.add(fontSizeStylePane);
        
        fontSizeStylePane.getStyleList().addListSelectionListener(listSelectionListener);
        
        //Richie:直接输入FRFont的size.
        fontSizeStylePane.getSizeField().getDocument().addDocumentListener(documentListener);
        fontSizeStylePane.getSizeField().getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {// 这是更改操作的处理
				updatePreviewLabel();
			}

			public void insertUpdate(DocumentEvent e) {// 这是插入操作的处理
				updatePreviewLabel();
			}

			public void removeUpdate(DocumentEvent e) {// 这是删除操作的处理
				updatePreviewLabel();
			}
		}); 
        
        fontSizeStylePane.getSizeList().addListSelectionListener(listSelectionListener);
        
        // 下划线 和 颜色
        JPanel listVBottomPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
        listVPane.add(listVBottomPane);
        
        
        // underline pane
        JPanel underlinePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        listVBottomPane.add(underlinePane);
        underlinePane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FRFont-Underline"), null));

        this.underlineCombo = new LineComboBox(CoreConstants.UNDERLINE_STYLE_ARRAY);
        this.underlineCombo.addActionListener(actionListener);
        underlinePane.add(this.underlineCombo, BorderLayout.CENTER);

        // foreground
        JPanel foregroundPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        listVBottomPane.add(foregroundPane);

        foregroundPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FRFont-Foreground"),null));
        foregroundColorPane = new ColorSelectBox(140);
        foregroundColorPane.addSelectChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				fireStateChanged();		
			}       	
        });
        foregroundPane.add(foregroundColorPane, BorderLayout.WEST);

        // center pane
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(centerPane, BorderLayout.CENTER);
        centerPane.setLayout(FRGUIPaneFactory.createM_BorderLayout());

        // other reportFont dialog
        JPanel otherFontPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerPane.add(otherFontPane, BorderLayout.WEST);
//        otherFontPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        // effects pane
        JPanel effectsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        otherFontPane.add(effectsPane, BorderLayout.CENTER);
//        effectsPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        effectsPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FRFont-Effects"),null));

        JPanel effectsPane2 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        effectsPane.add(effectsPane2, BorderLayout.NORTH);

        isStrikethroughCheckBox = new UICheckBox(Inter.getLocText("FRFont-Strikethrough") + "  ");
        isStrikethroughCheckBox.addChangeListener(changeListener);
        isStrikethroughCheckBox.setMnemonic('K');
        isShadowCheckBox = new UICheckBox(Inter.getLocText("FRFont-Shadow"));
        isShadowCheckBox.addChangeListener(changeListener);
        isShadowCheckBox.setMnemonic('S');
        isSuperscriptCheckBox = new UICheckBox(Inter.getLocText("FRFont-Superscript"));
        isSuperscriptCheckBox.addChangeListener(changeListener);
        isSuperscriptCheckBox.setMnemonic('E');
        isSubscriptCheckBox = new UICheckBox(Inter.getLocText("FRFont-Subscript"));
        isSubscriptCheckBox.addChangeListener(changeListener);
        isSubscriptCheckBox.setMnemonic('B');

        effectsPane2.add(this.getLeftJustPane(isStrikethroughCheckBox));
        effectsPane2.add(this.getLeftJustPane(isShadowCheckBox));
        effectsPane2.add(this.getLeftJustPane(isSuperscriptCheckBox));
        effectsPane2.add(this.getLeftJustPane(isSubscriptCheckBox));

        // right pane
        JPanel rightPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerPane.add(rightPane, BorderLayout.CENTER);

//        rightPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        // preview pane.
        preview = new FRFontPreviewArea();
        preview.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Preview")));
        rightPane.add(preview, BorderLayout.CENTER);

        // Cannot select superscript and subscript in the same time.
        this.isSuperscriptCheckBox.addChangeListener(changeListener);
        this.isSuperscriptCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                if (isSuperscriptCheckBox.isSelected()) {
                    isSubscriptCheckBox.setSelected(false);
                }
            }
        });
        this.isSubscriptCheckBox.addChangeListener(changeListener);
        this.isSubscriptCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                if (isSubscriptCheckBox.isSelected()) {
                    isSuperscriptCheckBox.setSelected(false);
                }
            }
        });

        //peteter:这里主动从Context, 获得默认的FRFont的值.
        DefaultValues defaultValues = FRContext.getDefaultValues();
        populate(defaultValues.getFRFont());
    }
    
    public void addChangeListener(ChangeListener changeListener) {
    	eventChangeList.add(ChangeListener.class, changeListener);
    }
    
    /**
     */
    public void fireStateChanged() {
        Object[] listeners = eventChangeList.getListenerList();
        ChangeEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (e == null) {
                    e = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }
    
    ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			fireStateChanged();			
		}   	
    };
    
    ChangeListener changeListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			fireStateChanged();			
		}    	
    };
    
    ListSelectionListener listSelectionListener = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
			fireStateChanged();			
		}
    };
	
    DocumentListener documentListener = new DocumentListener() {
		public void changedUpdate(DocumentEvent e) {
			fireStateChanged();			
		}

		public void insertUpdate(DocumentEvent e) {
			fireStateChanged();			
		}

		public void removeUpdate(DocumentEvent e) {
			fireStateChanged();		
		}    	
    };
    
    //use the method to make all checkbox donot margin == 0.
    private JPanel getLeftJustPane(JComponent comp) {
        JPanel leftJustPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
//        leftJustPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        leftJustPane.add(comp, BorderLayout.CENTER);

        leftJustPane.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        return leftJustPane;
    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("Sytle-FRFont");
    }

    /**
     * Use font to populate pane.
     */
    public void populate(FRFont frFont) {
    	familyPane.populate(frFont);

    	fontSizeStylePane.populate(frFont);

        //foreground.
        this.foregroundColorPane.setSelectObject(frFont.getForeground());
        //update frFont.
        this.underlineCombo.setSelectedLineStyle(frFont.getUnderline());

        //effects
        this.isStrikethroughCheckBox.setSelected(frFont.isStrikethrough());
        this.isShadowCheckBox.setSelected(frFont.isShadow());
        this.isSuperscriptCheckBox.setSelected(frFont.isSuperscript());
        this.isSubscriptCheckBox.setSelected(frFont.isSubscript());

        //添加ListHandler.
        ListHandler listHandler = new ListHandler();
        
        familyPane.addListSelectionListener(listHandler);
        fontSizeStylePane.getStyleList().addListSelectionListener(listHandler);
        fontSizeStylePane.getSizeList().addListSelectionListener(listHandler);

        //actionlistner
        ActionListener updatePreviewActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updatePreviewLabel();
            }
        };
        this.isStrikethroughCheckBox.addActionListener(updatePreviewActionListener);
        this.isShadowCheckBox.addActionListener(updatePreviewActionListener);
        this.isSuperscriptCheckBox.addActionListener(updatePreviewActionListener);
        this.isSubscriptCheckBox.addActionListener(updatePreviewActionListener);

//underline
        this.underlineCombo.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updatePreviewLabel();
            }
        });

        foregroundColorPane.addSelectChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                updatePreviewLabel();
            }
        });

        //update preview label.
        updatePreviewLabel();
    }

    /**
     * Update pane to get new font.
     */
    public FRFont update() {
    	double fs = fontSizeStylePane.getSizeField().getValue();
    	if (!(fs + "").endsWith(".5")) {
    		fs = (int)fs;
    	}
        return FRFont.getInstance(this.familyPane.getText(),
                this.fontSizeStylePane.getStyleList().getSelectedIndex(), 
                (float)fs,
                this.foregroundColorPane.getSelectObject(),
                this.underlineCombo.getSelectedLineStyle(),

                this.isStrikethroughCheckBox.isSelected(),
                this.isShadowCheckBox.isSelected(),
                this.isSuperscriptCheckBox.isSelected(),
                this.isSubscriptCheckBox.isSelected());
    }

    public static JPanel createTextFieldListPane(String label, UITextField textField, JList list) {
        GridBagLayout layout = new GridBagLayout();
        JPanel panel = new JPanel(layout);

        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = cons.gridy = 0;
        cons.gridwidth = cons.gridheight = 1;
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1.0f;

        UILabel _label = new UILabel(label);
        layout.setConstraints(_label, cons);
        panel.add(_label);

        cons.gridy = 1;
        Component vs = Box.createVerticalStrut(0);
        layout.setConstraints(vs, cons);
        panel.add(vs);

        cons.gridy = 2;
        layout.setConstraints(textField, cons);
        panel.add(textField);

        cons.gridy = 3;
        vs = Box.createVerticalStrut(2);
        layout.setConstraints(vs, cons);
        panel.add(vs);

        cons.gridy = 4;
        cons.gridheight = GridBagConstraints.REMAINDER;
        cons.weighty = 1.0f;
        JScrollPane scroller = new JScrollPane(list);
        layout.setConstraints(scroller, cons);
        panel.add(scroller);

        return panel;
    }

    private void updatePreviewLabel() {
        preview.setFontObject(this.update());
    }

    class ListHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent evt) {
            updatePreviewLabel();
        }
    }
}