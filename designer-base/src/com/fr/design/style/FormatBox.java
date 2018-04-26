package com.fr.design.style;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fr.base.TextFormat;
import com.fr.data.core.FormatField;
import com.fr.data.core.FormatField.FormatContents;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;


/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-4 上午11:05:32
 * 类说明: 数字格式的UIComboBox界面. 原型图参考bug5471效果3
 * 
 * @<a> 有可能只需要百分比(饼图的百分比格式设置)
 */
public class FormatBox extends BasicPane {
	private static final long serialVersionUID = -8728652510003088618L;
	private static final double DOUBLE_VALUE = 12345.6789;
	
	private UIComboBox typeBox;
	private UIComboBox formatBox;
	private JTextField formatField;
	
	private UILabel previewLabel;
	
	public FormatBox() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		JPanel pane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		this.add(pane, BorderLayout.NORTH);
		
		pane.add(typeBox = new UIComboBox());
		typeBox.setPreferredSize(new Dimension(80, 20));
		
		typeBox.addItem(FormatField.getInstance().getName(FormatContents.NULL));
		typeBox.addItem(FormatField.getInstance().getName(FormatContents.NUMBER));
		typeBox.addItem(FormatField.getInstance().getName(FormatContents.CURRENCY));
		typeBox.addItem(FormatField.getInstance().getName(FormatContents.PERCENT));
		typeBox.addItem(FormatField.getInstance().getName(FormatContents.SCIENTIFIC));
		typeBox.addItem(FormatField.getInstance().getName(FormatContents.DATE));
		typeBox.addItem(FormatField.getInstance().getName(FormatContents.TIME));
		typeBox.addItem(FormatField.getInstance().getName(FormatContents.TEXT));
		
		pane.add(new UILabel(Inter.getLocText("Format") + ":"));
		formatBox = new UIComboBox();
		// kunsnat: 大小: 这个有的格式的大小需要很宽!
		formatBox.setPreferredSize(new Dimension(150, 20));
		formatBox.setEditable(false);
		formatBox.setEnabled(false);
		pane.add(formatBox);
		
		pane.add(new UILabel(Inter.getLocText("StyleFormat-Sample") + ":"));
		pane.add(previewLabel = new UILabel(""));
		
		typeBox.addItemListener(typeListener);
		formatBox.addItemListener(formatListener);
		
		formatField = (JTextField)formatBox.getEditor().getEditorComponent();
		formatField.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				refreshLabelText();
			}
			public void insertUpdate(DocumentEvent e) {
				refreshLabelText();
			}
			public void changedUpdate(DocumentEvent e) {
				refreshLabelText();
			}
		});
	}
	
	public void setDateTypeBox() {
		typeBox.setSelectedItem(FormatField.getInstance().getName(FormatContents.DATE));
		formatBox.setSelectedItem(new SimpleDateFormat("yyyy-MM-dd").toPattern());
	}
	
	public void setTextTypeBox() {
		typeBox.setSelectedItem(FormatField.getInstance().getName(FormatContents.NULL));
	}
	
	ItemListener typeListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			Object selectObject = ((UIComboBox)e.getSource()).getSelectedItem();
			formatBox.removeAllItems();
			
			int contents = FormatField.getInstance().getContents(selectObject);
			
			formatBox.setEditable((contents != FormatContents.NULL) && (contents != FormatContents.TEXT));
			formatBox.setEnabled((contents != FormatContents.NULL) && (contents != FormatContents.TEXT));
			addItem2Box(formatBox, FormatField.getInstance().getFormatArray(contents));
		}
	};
	
	ItemListener formatListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			if(formatBox != null && formatBox.getSelectedItem() != null) {
				resetLabelText();
				
			}
		}
	};
	
	public void populate(Format format) {
		if (format == null) {
			this.typeBox.setSelectedItem(FormatField.getInstance().getName(FormatContents.NULL));
        } else {
            if (format instanceof DecimalFormat) {
                String pattern = ((DecimalFormat) format).toPattern();
                if (isMoneyPattern(pattern)) {
                	this.typeBox.setSelectedItem(FormatField.getInstance().getName(FormatContents.CURRENCY));
                	this.formatBox.setSelectedItem(pattern);
                } else if (pattern.endsWith("%")) {
                	this.typeBox.setSelectedItem(FormatField.getInstance().getName(FormatContents.PERCENT));
                	this.formatBox.setSelectedItem(pattern);
                } else if (pattern.indexOf("E") > 0) {
                	this.typeBox.setSelectedItem(FormatField.getInstance().getName(FormatContents.SCIENTIFIC));
                	this.formatBox.setSelectedItem(pattern);
                } else {
                	this.typeBox.setSelectedItem(FormatField.getInstance().getName(FormatContents.NUMBER));
                	this.formatBox.setSelectedItem(pattern);
                }
            } else if (format instanceof SimpleDateFormat) { //date and time
                String pattern = ((SimpleDateFormat) format).toPattern();
                int index = FormatPane.isArrayContainPattern(FormatField.getInstance().getFormatArray(FormatContents.DATE), pattern);
                if (index != -1) {
                	this.typeBox.setSelectedItem(FormatField.getInstance().getName(FormatContents.DATE));
                	this.formatBox.setSelectedItem(pattern);
                } else {
                    index = FormatPane.isArrayContainPattern(FormatField.getInstance().getFormatArray(FormatContents.TIME), pattern);
                    if (index != -1) {
                    	this.typeBox.setSelectedItem(FormatField.getInstance().getName(FormatContents.TIME));
                    	this.formatBox.setSelectedItem(pattern);
                    }
                }

                if (index == -1) {
                	this.typeBox.setSelectedItem(FormatField.getInstance().getName(FormatContents.DATE));
                	this.formatBox.setSelectedItem(pattern);
                }
            } else if (format instanceof TextFormat) { //Text
            	this.typeBox.setSelectedItem(FormatField.getInstance().getName(FormatContents.TEXT));
            }
        }
		resetLabelText();
	}
	
	public Format update() {
		FormatField field = FormatField.getInstance();
		int contents = field.getContents(typeBox.getSelectedItem());
		return field.getFormat(contents, formatField.getText());
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Style");
	}
	
	private void refreshLabelText() {
		this.previewLabel.setForeground(UIManager.getColor("Label.foreground"));
		try{
			resetLabelText();
		} catch (Exception e) {
			this.previewLabel.setForeground(Color.red);
			this.previewLabel.setText(e.getMessage());
		}
	}
	
	private void resetLabelText() {
		FormatField field = FormatField.getInstance();
		int contents = field.getContents(typeBox.getSelectedItem());
		Format format = field.getFormat(contents, formatField.getText());
		if(format != null) {
			this.previewLabel.setText(format.format(DOUBLE_VALUE));
		} else {
			this.previewLabel.setText("" + DOUBLE_VALUE);
		}
	}
	
	private void addItem2Box(UIComboBox box, Object[] object) {
		for(int i = 0; i < object.length; i++) {
			box.addItem(object[i]);
		}
	}
	
	private boolean isMoneyPattern(String pattern) {
		return (pattern.length() > 0 && pattern.charAt(0) == '¤') || (pattern.length() > 0 && pattern.charAt(0) == '$');
	}
}