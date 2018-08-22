/**
 * 
 */
package com.fr.design.cell.editor;

import com.fr.base.BaseFormula;
import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.report.RichTextEditingPane;
import com.fr.design.report.RichTextPane;
import com.fr.design.style.color.UIToolbarColorButton;
import com.fr.general.FRFont;

import com.fr.report.cell.cellattr.core.RichTextConverter;
import com.fr.stable.Constants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;

/**
 *
 *
 * @date: 2014-12-5-下午1:10:31
 */
public class RichTextToolBar extends BasicPane{

    private static final Dimension BUTTON_SIZE = new Dimension(24, 20);

    private UIComboBox fontNameComboBox;
    private UIComboBox fontSizeComboBox;
    private UIToggleButton bold;
    private UIToggleButton italic;
    private UIToggleButton underline;

    private UIToolbarColorButton colorSelectPane;
    private UIToggleButton superPane;
    private UIToggleButton subPane;
    private UIToggleButton formulaPane;
    
    //外部传进来的
    private RichTextEditingPane textPane;

    public RichTextToolBar() {
        this.initComponents();
    }
    
    public RichTextToolBar(RichTextEditingPane textPane) {
    	this.textPane = textPane;
    	
    	this.initComponents();
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Font");
    }

    protected void initComponents() {
    	//初始化并设置所有按钮样式
        initAllButton();
        //添加到工具栏
        addToToolBar();
    }
    
    private void initAllButton(){
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontNameComboBox.setPreferredSize(new Dimension(144, 20));
		fontSizeComboBox = new UIComboBox(FRFontPane.getFontSizes());
		colorSelectPane = new UIToolbarColorButton(BaseUtils.readIcon("/com/fr/design/images/gui/color/foreground.png"));
		colorSelectPane.set4Toolbar();
        
        bold = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
        italic = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));
        underline = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/underline.png"));
        superPane = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/sup.png"));
        subPane = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/sub.png"));
        formulaPane = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_insert/formula.png"));

        //名字
        initAllNames();
        //悬浮提示
        setToolTips();
        //样式
        setAllButtonStyle();
    	//绑定监听器
        bindListener();
    }
    
    private void setAllButtonStyle(){
    	setButtonStyle(bold);
    	setButtonStyle(italic);
    	setButtonStyle(underline);
    	setButtonStyle(subPane);
    	setButtonStyle(superPane);
    	setButtonStyle(formulaPane);
    }
    
    private void setButtonStyle(UIButton button){
    	button.setNormalPainted(false);
		button.setBackground(null);
		button.setOpaque(false);
		button.setPreferredSize(BUTTON_SIZE);
		button.setBorderPaintedOnlyWhenPressed(true);
    }
    
    private void addToToolBar(){
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
    	
        this.add(fontNameComboBox);
        this.add(fontSizeComboBox);
        this.add(bold);
        this.add(italic);
        this.add(underline);
        this.add(colorSelectPane);
        this.add(superPane);
        this.add(subPane);
        this.add(formulaPane);
    }
    
    private void bindListener(){
        FRFont defaultFont = (this.textPane != null) ? FRFont.getInstance(this.textPane.getFont()) : RichTextPane.DEFAUL_FONT;
        fontNameComboBox.addItemListener(fontNameItemListener);
        fontNameComboBox.setSelectedItem(defaultFont.getFontName());
        fontSizeComboBox.addItemListener(fontSizeItemListener);
        fontSizeComboBox.setSelectedItem(scaleDown(defaultFont.getSize()));
        
        bold.addActionListener(blodChangeAction);
        italic.addActionListener(itaChangeAction);
        underline.addActionListener(underlineChangeAction);
        subPane.addActionListener(subChangeAction);
        superPane.addActionListener(superChangeAction);
        colorSelectPane.addColorChangeListener(colorChangeAction);
        formulaPane.addActionListener(formulaActionListener);
        
        //选中文字的监听器
        textPane.addCaretListener(textCareListener);
        textPane.addMouseListener(setMouseCurrentStyle);
        textPane.getDocument().addDocumentListener(inputListener);
    }

    private void initAllNames() {
        fontNameComboBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Font_Family"));
        fontSizeComboBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Font_Size"));
        italic.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Italic"));
        bold.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bold"));
        underline.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Underline"));
        superPane.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Super_Script"));
        subPane.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sub_Script"));
    }

    private void setToolTips() {
        colorSelectPane.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Foreground"));
        italic.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Italic"));
        bold.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bold"));
        underline.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Underline"));
        superPane.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Super_Script"));
        subPane.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sub_Script"));
        formulaPane.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Formula"));
    }
    
    /**
	 * 移除输入监听
	 * 用于populate时, 插入字符串, 那时不需要插入监听
	 * 
	 *
	 * @date 2015-1-5-下午5:13:04
	 * 
	 */
    public void removeInputListener(){
    	this.textPane.getDocument().removeDocumentListener(inputListener);
    }
    
    /**
	 * 增加输入监听事件
	 * 
	 *
	 * @date 2015-1-5-下午5:13:26
	 * 
	 */
    public void addInputListener(){
    	this.textPane.getDocument().addDocumentListener(inputListener);
    }
    
    private ActionListener blodChangeAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isBold = RichTextToolBar.this.bold.isSelected();
			// 调用setCharacterAttributes函数设置文本区选择文本的字体
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setBold(attr, !isBold);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
		}
	};
	
	private ActionListener itaChangeAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isIta = RichTextToolBar.this.italic.isSelected();
			// 调用setCharacterAttributes函数设置文本区选择文本的字体
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setItalic(attr, !isIta);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
		}
	};
	
	private ActionListener underlineChangeAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isUnder = RichTextToolBar.this.underline.isSelected();
			// 调用setCharacterAttributes函数设置文本区选择文本的字体
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setUnderline(attr, !isUnder);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
		}
	};
	private ActionListener subChangeAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isSub = RichTextToolBar.this.subPane.isSelected();
			// 调用setCharacterAttributes函数设置文本区选择文本的字体
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setSubscript(attr, !isSub);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
		}
	};
	private ActionListener superChangeAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isSuper = RichTextToolBar.this.superPane.isSelected();
			// 调用setCharacterAttributes函数设置文本区选择文本的字体
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setSuperscript(attr, !isSuper);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
		}
	};
	
	private ChangeListener colorChangeAction = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
        	Color color = RichTextToolBar.this.colorSelectPane.getColor();
        	color = color == null ? Color.BLACK : color;
        	// 调用setCharacterAttributes函数设置文本区选择文本的字体
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setForeground(attr, color);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
        }
    };
	
	// 设置文本区选择文本的样式
	private void setCharacterAttributes(JEditorPane editor, AttributeSet attr,
			boolean replace) {
		//注意不要失焦
		textPane.requestFocus();
		
		// 取得选择文本的起始位置和结束位置
		int start = editor.getSelectionStart();
		int end = editor.getSelectionEnd();

		// 如果选中文本，设置选中文本的样式
		if (start != end) {
			StyledDocument doc = (StyledDocument) textPane.getDocument();
			// 将所选文本设置为新的样式，replace为false表示不覆盖原有的样式
			doc.setCharacterAttributes(start, end - start, attr, replace);
		}
	}
	
	private ItemListener fontSizeItemListener = new ItemListener() {
    	@Override
    	public void itemStateChanged(ItemEvent e) {
    		int fontSize = (Integer) RichTextToolBar.this.fontSizeComboBox.getSelectedItem();
    		fontSize = scaleUp(fontSize);
    		// 调用setCharacterAttributes函数设置文本区选择文本的字体
    		MutableAttributeSet attr = new SimpleAttributeSet();
    		StyleConstants.setFontSize(attr, fontSize);
    		setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
    	}
    };
	
	private ItemListener fontNameItemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
        	String fontName = (String) RichTextToolBar.this.fontNameComboBox.getSelectedItem();
			// 调用setCharacterAttributes函数设置文本区选择文本的字体
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setFontFamily(attr, fontName);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
        }
    };
	
	private ActionListener formulaActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			final UIFormula formulaPane = FormulaFactory.createFormulaPane();
			formulaPane.populate(BaseFormula.createFormulaBuilder().build());
			formulaPane.showLargeWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
				@Override
				public void doOk() {
					StyledDocument doc = (StyledDocument) textPane.getDocument();
					BaseFormula fm = formulaPane.update();
					String content = RichTextConverter.asFormula(fm.getContent());
					int start = textPane.getSelectionStart();
                    AttributeSet attrs = start > 0 ? doc.getCharacterElement(start - 1).getAttributes() : new SimpleAttributeSet();
					try {
						doc.insertString(start, content, attrs);
					} catch (BadLocationException e) {
						FRContext.getLogger().error(e.getMessage(), e);
					}
				}
			}).setVisible(true);
		}
	};
	
	private int roundUp(double num){
		String numStr = Double.toString(num);
		numStr = new BigDecimal(numStr).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
		return Integer.valueOf(numStr);
	}

	private CaretListener textCareListener = new CaretListener() {
		
		//根据选中部分的文字样式, 来动态显示工具栏上按钮的状态
		private void setSelectedCharStyle(int start, int end, StyledDocument doc){
			boolean isBold = true;
			boolean isItalic = true;
			boolean isUnderline = true;
			boolean isSubscript = true;
			boolean isSuperscript = true;
			String fontName_1st = null;
			int fontSize_1st = 0;
			Color fontColor_1st = null;
			
			for (int i = start; i < end; i++) {
				Element ele = doc.getCharacterElement(i);
				AttributeSet attrs = ele.getAttributes();
				
				//粗体
				isBold = isBold && StyleConstants.isBold(attrs);
				//斜体
				isItalic = isItalic && StyleConstants.isItalic(attrs);
				//下划线
				isUnderline = isUnderline && StyleConstants.isUnderline(attrs);
				//下标
				isSubscript = isSubscript && StyleConstants.isSubscript(attrs);
				//上标
				isSuperscript = isSuperscript && StyleConstants.isSuperscript(attrs);
				
				if(i == start){
					fontName_1st = (String) attrs.getAttribute(StyleConstants.FontFamily);  
					fontSize_1st = (Integer) attrs.getAttribute(StyleConstants.FontSize);  
					fontColor_1st = (Color) attrs.getAttribute(StyleConstants.Foreground);
					fontColor_1st = fontColor_1st == null ? Color.BLACK : fontColor_1st;
				}
			}

			setButtonSelected(isBold, isItalic, isUnderline, isSubscript, isSuperscript, 
					fontName_1st, fontSize_1st, fontColor_1st);
		}
		
		//动态显示工具栏上按钮的状态
		private void setButtonSelected(boolean isBold, boolean isItalic, boolean isUnderline, 
				boolean isSubscript, boolean isSuperscript, String fontName_1st, 
				int fontSize_1st, Color fontColor_1st){
			bold.setSelected(isBold);
			italic.setSelected(isItalic);
			underline.setSelected(isUnderline);
			subPane.setSelected(isSubscript);
			superPane.setSelected(isSuperscript);
			//为什么字体名称, 大小, 颜色, 不需要去判断是否全相同呢
			//因为如果全相同, 则设置为第一个字符的样式, 如果不全相同, 那么默认也设置成第一个字符的样式.
			fontNameComboBox.setSelectedItem(fontName_1st);
			fontSizeComboBox.removeItemListener(fontSizeItemListener);
			fontSizeComboBox.setSelectedItem(scaleDown(fontSize_1st));
			fontSizeComboBox.addItemListener(fontSizeItemListener);
			selectColorPane(fontColor_1st);
		}
		
		private void selectColorPane(Color color){
			colorSelectPane.removeColorChangeListener(colorChangeAction);
			colorSelectPane.setColor(color);
			colorSelectPane.addColorChangeListener(colorChangeAction);
		}
	
		@Override
		public void caretUpdate(CaretEvent e) {
			StyledDocument doc = (StyledDocument) textPane.getDocument();
			
			// 取得选择文本的起始位置和结束位置
			int start = textPane.getSelectionStart();
			int end = textPane.getSelectionEnd();
			
			//如果没有选定字符
			if(end == start){
				return;
			}
			
			setSelectedCharStyle(start, end, doc);
		}
	};
	
	//设置当前光标位样式
	private MouseListener setMouseCurrentStyle = new MouseAdapter() {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			StyledDocument doc = (StyledDocument) textPane.getDocument();
			
			// 取得选择文本的起始位置和结束位置
			int start = textPane.getSelectionStart();
			int end = textPane.getSelectionEnd();
			
			if(start != end){
				return;
			}
			
			setToLastCharStyle(end, doc);
		}
		
		//如果默认不选字符, 那么设置为最后一个字符的样式
		private void setToLastCharStyle(int end, StyledDocument doc){
			if(textPane.isUpdating()){
				return;
			}
			
			//取前一个字符的样式
			Element ele = doc.getCharacterElement(end - 1);
			AttributeSet attrs = ele.getAttributes();
			populateToolBar(attrs);
		}
	};
	
	/**
	 * 从样式中更新工具栏上的按钮状态
	 * 
	 * @param attrs 样式
	 * 
	 *
	 * @date 2015-1-5-下午5:12:33
	 * 
	 */
	public void populateToolBar(AttributeSet attrs){
		int size = scaleDown(StyleConstants.getFontSize(attrs));
		fontNameComboBox.setSelectedItem(StyleConstants.getFontFamily(attrs));
		fontSizeComboBox.setSelectedItem(size);
		
		bold.setSelected(StyleConstants.isBold(attrs));
		italic.setSelected(StyleConstants.isItalic(attrs));
		underline.setSelected(StyleConstants.isUnderline(attrs));
		subPane.setSelected(StyleConstants.isSubscript(attrs));
		superPane.setSelected(StyleConstants.isSuperscript(attrs));
		Color foreGround = StyleConstants.getForeground(attrs);
		foreGround = foreGround == null ? Color.BLACK : foreGround;
		colorSelectPane.setColor(foreGround);
		colorSelectPane.repaint();
	}
	
	//pt转为px =*4/3
	private int scaleUp(int fontSize){
		return scale(fontSize, true);
	}
	
	//px转pt = *3/4
	private int scaleDown(int fontSize){
		return scale(fontSize, false);
	}
	
	private int scale(int fontSize, boolean isUp){
		double dpi96 = Constants.FR_PAINT_RESOLUTION;
		double dpi72 = Constants.DEFAULT_FONT_PAINT_RESOLUTION;
		double scale = isUp ? (dpi96 / dpi72) : (dpi72 / dpi96);
		
		return roundUp(fontSize * scale);
	}
	
	private DocumentListener inputListener = new DocumentListener() {
		
		@Override
		public void removeUpdate(DocumentEvent e) {
		}
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			//标志正在更新内容
			textPane.startUpdating();
			final MutableAttributeSet attr = updateStyleFromToolBar();
			final int start = textPane.getSelectionStart();
			int end = textPane.getSelectionEnd();
			
			if (start != end) {
				textPane.finishUpdating();
				return;
			}

			//放到SwingWorker里, 是因为在documentListener里不能动态改变doc内容
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					changeContentStyle(start, attr);
				}
			});
		}
		
		//根据Style来显示populate按钮
		private void changeContentStyle(int start, MutableAttributeSet attr){
			changeContentStyle(start, attr, 1);
		}
		
		private void changeContentStyle(int start, MutableAttributeSet attr, int contentLength){
			// 将所选文本设置为新的样式，replace为false表示不覆盖原有的样式
			StyledDocument doc = (StyledDocument) textPane.getDocument();
			doc.setCharacterAttributes(start, contentLength, attr, false);
			textPane.finishUpdating();
		}
		
		//将界面上的设置赋值给输入的字符
		private MutableAttributeSet updateStyleFromToolBar(){
			final boolean isBold = bold.isSelected();
			final boolean isItalic = italic.isSelected();
			final boolean isSub = subPane.isSelected();
			final boolean isSuper = superPane.isSelected();
			final boolean isUnderLine = underline.isSelected();
			final String fontName = (String) fontNameComboBox.getSelectedItem();
			final int fontSize = scaleUp((Integer) fontSizeComboBox.getSelectedItem());
			final Color foreGround = colorSelectPane.getColor() == null ? Color.BLACK : colorSelectPane.getColor();
			
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setBold(attr, isBold);
			StyleConstants.setItalic(attr, isItalic);
			StyleConstants.setSubscript(attr, isSub);
			StyleConstants.setSuperscript(attr, isSuper);
			StyleConstants.setUnderline(attr, isUnderLine);
			StyleConstants.setForeground(attr, foreGround);
			StyleConstants.setFontFamily(attr, fontName);
			StyleConstants.setFontSize(attr, fontSize);
			
			return attr;
		}

		private static final int NOT_INITED = -1;
		private static final int UPDATING = -2;
		//记录上一次输入成功后光标点定位, 因为有可能文本是在中间插入的
		private int inputStart = NOT_INITED;
		private static final int JDK_6 = 6;
		private static final int JDK_7 = 7;
		
		@Override
		public void changedUpdate(DocumentEvent e) {
			//这边需要注意, jdk1.6和1.7对于输入法的处理逻辑不一样, jdk6时直接在输入法中输入一大段中文
			//他会一个个insert进去直接触发inserupdate事件, 而jdk7会直接把所有的塞进来.
			//inserupdate那边绑定的是一个个插入的事件, 多个一起插入的放这
			//bug84777 8.0不走if逻辑，改成只有jdk7走if逻辑
			if(StableUtils.getMajorJavaVersion() == JDK_7){
				if(isUpdating()){
					return;
				}
				StyledDocument doc = (StyledDocument) textPane.getDocument();
				final String content;
				initFlag(doc);
				
				final int start = textPane.getSelectionStart();
				final int inputLen = start - inputStart;
				//检测输入内容
				try {
					content = doc.getText(inputStart, inputLen);
				} catch (BadLocationException e1) {
					return;
				}
				
				//中文输入法, 默认输入字符会被输入法的框截取住, jtextpane得到是一个空格, 此时不做处理
				if(StringUtils.isBlank(content) || inputLen <= 0){
					return;
				}
				//设置一次性输入多个文字的样式
				setContentStyle(inputLen);
			}
		}
		
		private void setContentStyle(final int inputLen){
			//缓存下Start, 下面要用来设置样式
			final int _start = inputStart;
			final MutableAttributeSet attr = updateStyleFromToolBar();

			//放到SwingWorker里, 是因为在documentListener里不能动态改变doc内容
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					//防止触发死循环change事件
					startUpdating();
					//Start-1 是因为中文输入法会用空格占1位
					changeContentStyle(_start, attr, inputLen);
					resetFlag();
				}
			});
		}
		
		private boolean isUpdating(){
			return inputStart == UPDATING;
		}
		
		private void startUpdating(){
			inputStart = UPDATING;
		}
		
		//初始标记状态, 用于记录中文输入法多个字符同时输入的问题
		private void initFlag(StyledDocument doc){
			if(inputStart != NOT_INITED){
				return;
			}
			inputStart = textPane.getSelectionStart() - 1;
		}
		
		//重置标记状态
		private void resetFlag(){
			inputStart = NOT_INITED;
		}
	};

}
