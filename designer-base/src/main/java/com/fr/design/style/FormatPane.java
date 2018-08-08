/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.style;

import com.fr.base.FRContext;
import com.fr.base.TextFormat;
import com.fr.data.core.FormatField;
import com.fr.data.core.FormatField.FormatContents;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;

import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 属性界面中的格式面板
 */
public class FormatPane extends BasicPane {
    private static final long serialVersionUID = 724330854437726751L;
    
	private Format format;
	private UILabel sampleLabel; //preview label.
    private UIRadioButton nullRadioButton;
    private UIRadioButton numberRadioButton;
    private UIRadioButton currencyRadioButton;
    private UIRadioButton percentRadioButton;
    private UIRadioButton scientificRadioButton;
    private UIRadioButton dateRadioButton;
    private UIRadioButton timeRadioButton;
    private UIRadioButton textRadioButton;
    //content pane.
    private UITextField patternTextField = null;
    private JList patternList = null;
    
    /**
     * Constructor.
     */
    public FormatPane() {
    	this.initComponents();
    }
    
    public UILabel getSampleLabel() {
        return sampleLabel;
    }

    public void setSampleLabel(UILabel sampleLabel) {
        this.sampleLabel = sampleLabel;
    }
	

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        //sample pane
        JPanel samplePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(samplePane, BorderLayout.NORTH);
        samplePane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("StyleFormat-Sample"), null));
        samplePane.setLayout(FRGUIPaneFactory.createBorderLayout());
        sampleLabel = new UILabel(FormatField.getInstance().getFormatValue());
        samplePane.add(sampleLabel, BorderLayout.CENTER);
        sampleLabel.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
        sampleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sampleLabel.setFont(FRContext.getDefaultValues().getFRFont());
        //left control pane
        JPanel leftControlPane =FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        this.add(leftControlPane, BorderLayout.WEST);
        leftControlPane.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("StyleFormat-Category"), null));
        initRadioButton();
        ButtonGroup categoryButtonGroup = new ButtonGroup();
        categoryButtonGroup.add(nullRadioButton);
        categoryButtonGroup.add(numberRadioButton);
        categoryButtonGroup.add(currencyRadioButton);
        categoryButtonGroup.add(percentRadioButton);
        categoryButtonGroup.add(scientificRadioButton);
        categoryButtonGroup.add(dateRadioButton);
        categoryButtonGroup.add(timeRadioButton);
        categoryButtonGroup.add(textRadioButton);

        leftControlPane.add(this.createRadioCenterPane(nullRadioButton));
        leftControlPane.add(this.createRadioCenterPane(numberRadioButton));
        leftControlPane.add(this.createRadioCenterPane(currencyRadioButton));
        leftControlPane.add(this.createRadioCenterPane(percentRadioButton));
        leftControlPane.add(this.createRadioCenterPane(scientificRadioButton));
        leftControlPane.add(this.createRadioCenterPane(dateRadioButton));
        leftControlPane.add(this.createRadioCenterPane(timeRadioButton));
        leftControlPane.add(this.createRadioCenterPane(textRadioButton));
        //content pane.
        JPanel contentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(contentPane, BorderLayout.CENTER);
        contentPane.setBorder(BorderFactory.createEmptyBorder(4, 0, 2, 0));
        patternTextField = new UITextField();
        contentPane.add(patternTextField, BorderLayout.NORTH);
        patternTextField.getDocument().addDocumentListener(patternTextDocumentListener);
        patternList = new JList(new DefaultListModel());
        contentPane.add(new JScrollPane(patternList), BorderLayout.CENTER);
        patternList.addListSelectionListener(patternListSelectionListener);
        //init values.
        nullRadioButton.setSelected(true);
        this.applyRadioActionListener(this.nullRadioButton);
    }

    private void initRadioButton () {
        nullRadioButton = new UIRadioButton(FormatField.getInstance().getName(FormatContents.NULL));
        nullRadioButton.setMnemonic('o');
        numberRadioButton = new UIRadioButton(FormatField.getInstance().getName(FormatContents.NUMBER));
        numberRadioButton.setMnemonic('N');
        currencyRadioButton = new UIRadioButton(FormatField.getInstance().getName(FormatContents.CURRENCY));
        currencyRadioButton.setMnemonic('C');
        percentRadioButton = new UIRadioButton(FormatField.getInstance().getName(FormatContents.PERCENT));
        percentRadioButton.setMnemonic('P');
        scientificRadioButton = new UIRadioButton(FormatField.getInstance().getName(FormatContents.SCIENTIFIC));
        scientificRadioButton.setMnemonic('S');
        dateRadioButton = new UIRadioButton(FormatField.getInstance().getName(FormatContents.DATE));
        dateRadioButton.setMnemonic('D');
        timeRadioButton = new UIRadioButton(FormatField.getInstance().getName(FormatContents.TIME));
        timeRadioButton.setMnemonic('I');
        textRadioButton = new UIRadioButton(FormatField.getInstance().getName(FormatContents.TEXT));
        textRadioButton.setMnemonic('T');

        nullRadioButton.addActionListener(radioActionListener);
        numberRadioButton.addActionListener(radioActionListener);
        currencyRadioButton.addActionListener(radioActionListener);
        percentRadioButton.addActionListener(radioActionListener);
        scientificRadioButton.addActionListener(radioActionListener);
        dateRadioButton.addActionListener(radioActionListener);
        timeRadioButton.addActionListener(radioActionListener);
        textRadioButton.addActionListener(radioActionListener);
    }

    /**
     * Create radio center pane.
     */
    private JPanel createRadioCenterPane(UIRadioButton radioButton) {
        JPanel pane = new  JPanel();

        pane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pane.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        pane.add(radioButton);

        return pane;
    }

    @Override
    protected String title4PopupWindow() {
    	return com.fr.design.i18n.Toolkit.i18nText("Style");
    }
    
    /**
     * Populate
     */
    public void populate(Format format) {
        this.format = format;

        if (format == null) {
            this.nullRadioButton.setSelected(true);
            this.applyRadioActionListener(this.nullRadioButton);
        } else {
            if (format instanceof DecimalFormat) {
                checkDecimalFormat();
            } else if (format instanceof SimpleDateFormat) { 
            	//date and time
            	checkDateFormat();  
            } else if (format instanceof TextFormat) { 
            	//Text
                this.textRadioButton.setSelected(true);
                this.applyRadioActionListener(this.textRadioButton);
            }
        }
    }
    
    private void checkDateFormat() {
    	 String pattern = ((SimpleDateFormat) format).toPattern();
         if(pattern == null) {
         	return;
         }
         int index = isArrayContainPattern(FormatField.getInstance().getFormatArray(FormatContents.DATE), pattern);
         if (index != -1) {
             this.dateRadioButton.setSelected(true);
             this.applyRadioActionListener(this.dateRadioButton);
         } else {
             index = isArrayContainPattern(FormatField.getInstance().getFormatArray(FormatContents.TIME), pattern);

             if (index != -1) {
                 this.timeRadioButton.setSelected(true);
                 this.applyRadioActionListener(this.timeRadioButton);
             }
         }
         if (index != -1) {
             patternList.setSelectedIndex(index);
         } else {
             this.dateRadioButton.setSelected(true);
             this.applyRadioActionListener(this.dateRadioButton);
             patternTextField.setText(pattern);
         }
    }

    private void checkDecimalFormat() {
        String pattern = ((DecimalFormat) format).toPattern();
        if(pattern == null) {
            return;
        }

        if (isCurrency(pattern)) {
            this.currencyRadioButton.setSelected(true);
            this.applyRadioActionListener(this.currencyRadioButton);
        } else if (pattern.endsWith("%")) {
            this.percentRadioButton.setSelected(true);
            this.applyRadioActionListener(this.percentRadioButton);
        } else if (pattern.indexOf("E") > 0) {
            this.scientificRadioButton.setSelected(true);
            this.applyRadioActionListener(this.scientificRadioButton);
        } else {
            this.numberRadioButton.setSelected(true);
            this.applyRadioActionListener(this.numberRadioButton);
        }

        patternTextField.setText(pattern);
    }

    private boolean isCurrency (String pattern) {
       return pattern.length() > 0 && pattern.charAt(0) == '¤' || pattern.length() > 0 && pattern.charAt(0) == '$';
    }

    /**
     * 设置可用的格式
     * @param formatStyle 可用的格式
     */
    public void setStyle4Pane(int[] formatStyle) {
        numberRadioButton.setEnabled(false);
        currencyRadioButton.setEnabled(false);
        scientificRadioButton.setEnabled(false);
        textRadioButton.setEnabled(false);
        percentRadioButton.setEnabled(false);
        nullRadioButton.setEnabled(false);
        dateRadioButton.setEnabled(false);
        timeRadioButton.setEnabled(false);
        for(int i=0 ; i < formatStyle.length; i++) {
        	switch(formatStyle[i]) {
        	case FormatContents.DATE :
        		dateRadioButton.setEnabled(true);
        		break;
        	case FormatContents.TIME :
        		timeRadioButton.setEnabled(true);
        		break;
        	default :
        		nullRadioButton.setEnabled(true);
        		break;
        	}
        }
    }
    
    /**
     * 百分比面板
     */
    public void percentFormatPane() {
        nullRadioButton.setEnabled(false);
        numberRadioButton.setEnabled(false);
        currencyRadioButton.setEnabled(false);
        percentRadioButton.addActionListener(radioActionListener);
        scientificRadioButton.setEnabled(false);
        dateRadioButton.setEnabled(false);
        timeRadioButton.setEnabled(false);
        textRadioButton.setEnabled(false);

        this.percentRadioButton.setSelected(true);
        this.applyRadioActionListener(this.percentRadioButton);
    }

    /**
     * 是否属于数组中的列表样式
     * 
     * @param stringArray 格式数组
     * @param pattern 输入的格式
     * @return  返回在数组中的位置
     */
    public static int isArrayContainPattern(String[] stringArray, String pattern) {
        for (int i = 0; i < stringArray.length; i++) {
            if (ComparatorUtils.equals(stringArray[i], pattern)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * update
     */
    public Format update() {
    	String text = patternTextField.getText();
        if (getFormatContents() == FormatContents.TEXT) {
            return FormatField.getInstance().getFormat(getFormatContents(), text);
        }
    	if(StringUtils.isEmpty(text)) {
    		return null;
    	}
    	return FormatField.getInstance().getFormat(getFormatContents(), text);
    }
    
    private int getFormatContents(){
    	if (nullRadioButton.isSelected())
    		return FormatContents.NULL;
    	else if (numberRadioButton.isSelected())
    		return FormatContents.NUMBER;
    	else if (currencyRadioButton.isSelected())
    		return FormatContents.CURRENCY;
    	else if (percentRadioButton.isSelected())
    		return FormatContents.PERCENT;
    	else if (scientificRadioButton.isSelected())
    		return FormatContents.SCIENTIFIC;
    	else if (dateRadioButton.isSelected()) 
    		return FormatContents.DATE;
    	else if (timeRadioButton.isSelected())
    		return FormatContents.TIME;
    	else if (textRadioButton.isSelected())
    		return FormatContents.TEXT;
    	else
    		return FormatContents.NULL;
    }

    /**
     * Refresh preview label.
     */
    private void refreshPreviewLabel() {
    	this.sampleLabel.setText(FormatField.getInstance().getFormatValue());
    	String text = patternTextField.getText();
        try{
        	// 参数面板日期控件的属性格式里，也要加上提示格式校验不通过
        	Color c = UIManager.getColor("Label.foreground");
        	int formatType = getFormatContents();
        	if (ComparatorUtils.equals(formatType, FormatContents.DATE) || ComparatorUtils.equals(formatType, FormatContents.TIME)) {
        		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(text);
        		String sample = simpleDateFormat.format(new Date());
        		if (!ArrayUtils.contains(FormatField.getInstance().getDateFormatArray(), text)) {
        			sample += " " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DateFormat_Custom_Warning");
        			c = Color.red;
        		}
        		this.sampleLabel.setText(sample);
        		this.sampleLabel.setForeground(c);
        	} else {
        		this.sampleLabel.setText(FormatField.getInstance().getFormatValue(formatType, text));
        	}
        } catch (Exception e) {
	    	this.sampleLabel.setForeground(Color.red);
	        this.sampleLabel.setText(e.getMessage());
        }
    }

    /**
     * Apply radio action listener
     */
    private void applyRadioActionListener(UIRadioButton radioButton) {
        ActionEvent evt = new ActionEvent(radioButton, 100, "");
        this.radioActionListener.actionPerformed(evt);
    }
    
    /**
     * Radio selection listener.
     */
    ActionListener radioActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            Object source = evt.getSource();

            patternTextField.setEnabled(true);
            patternTextField.setEditable(true);
            patternList.setEnabled(true);

            int contents = getContents(source);

            String[] patternArray = FormatField.getInstance().getFormatArray(contents);
            //
            DefaultListModel patternModel = (DefaultListModel) patternList.getModel();
            patternModel.removeAllElements();

            for (int i = 0; i < patternArray.length; i++) {
                patternModel.addElement(patternArray[i]);
            }

            //maybe need to select the first item.
            String text = patternTextField.getText();

            if (hasPattenText(text, patternModel)) {
                String pattern = null;
                if (format instanceof DecimalFormat) {
                    pattern = ((DecimalFormat) format).toPattern();
                } else if (format instanceof SimpleDateFormat) {
                    pattern = ((SimpleDateFormat) format).toPattern();
                } else if (format instanceof TextFormat) {
                }

                if (pattern != null) {
                    patternList.setSelectedValue(pattern, true);
                } else {
                    patternList.setSelectedIndex(0);
                }
            }
        }
    };

    private boolean hasPattenText (String text, DefaultListModel patternModel) {
        return (text == null || text.length() <= 0) && patternModel.size() > 0;
    }
    
    private void setPatternTextEnable() {
    	patternTextField.setEnabled(false);
        patternTextField.setEditable(false);
        patternTextField.setText("");
        patternList.setEnabled(false);
    }

    private int getContents (Object source) {
        int contents = FormatContents.NULL;
        if (ComparatorUtils.equals(source, nullRadioButton)) {
        	setPatternTextEnable();
            contents = FormatContents.NULL;
        } else if (ComparatorUtils.equals(source,numberRadioButton)) {
            contents = FormatContents.NUMBER;
        } else if (ComparatorUtils.equals(source,currencyRadioButton)) {
            contents = FormatContents.CURRENCY;
        } else if (ComparatorUtils.equals(source,percentRadioButton)) {
            contents = FormatContents.PERCENT;
        } else if (ComparatorUtils.equals(source,scientificRadioButton)) {
            contents = FormatContents.SCIENTIFIC;
        } else if (ComparatorUtils.equals(source,dateRadioButton)) {
            contents = FormatContents.DATE;
        } else if (ComparatorUtils.equals(source,timeRadioButton)) {
            contents = FormatContents.TIME;
        } else if (ComparatorUtils.equals(source,textRadioButton)) {
        	setPatternTextEnable();
            contents = FormatContents.TEXT;
        }
        return contents;
    }
    
    /**
     * text pattern document listener.
     */
    DocumentListener patternTextDocumentListener = new DocumentListener() {

        public void insertUpdate(DocumentEvent evt) {
            refreshPreviewLabel();
        }

        public void removeUpdate(DocumentEvent evt) {
            refreshPreviewLabel();
        }

        public void changedUpdate(DocumentEvent evt) {
            refreshPreviewLabel();
        }
    };
    
    /**
     * Pattern list selection listener.
     */
    ListSelectionListener patternListSelectionListener = new ListSelectionListener() {

        public void valueChanged(ListSelectionEvent evt) {
            patternTextField.setText((String) patternList.getSelectedValue());
        }
    };
    
}