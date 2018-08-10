package com.fr.design.report;

import com.fr.base.BaseFormula;
import com.fr.base.FRContext;
import com.fr.base.Style;
import com.fr.design.cell.editor.RichTextToolBar;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;

import com.fr.report.cell.CellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.cell.cellattr.core.RichChar;
import com.fr.report.cell.cellattr.core.RichText;
import com.fr.report.cell.cellattr.core.RichTextConverter;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.Constants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Iterator;

public class RichTextPane extends BasicPane {
	
	//12号字体有个奇怪的bug, 字体下部分渲染会截断一部分, 换其他字体没问题, 字体改大小也没问题.
	//直接弄个jtexarea放到jframe里也有同样问题, 不知道是字体渲染bug还是jdk问题
	public static final FRFont DEFAUL_FONT = FRFont.getInstance().applySize(13);
	
	private RichTextEditingPane textPane;
	//用于populate时动态更新按钮
	private RichTextToolBar toolBar;
	
	public RichTextPane() {
		this.initComponents();
	}

    protected void initComponents() {
    	this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
    	JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
    	textPane = new RichTextEditingPane();
    	textPane.setFont(DEFAUL_FONT);
    	toolBar = new RichTextToolBar(textPane);
    	northPane.add(toolBar);
    	
    	JPanel southPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
    	southPane.add(new UIScrollPane(textPane));
    	this.add(southPane, BorderLayout.CENTER);
    	this.add(northPane, BorderLayout.NORTH);
    }
    
    @Override
    protected String title4PopupWindow() {
    	return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_RichTextEditor");
    }

    /**
	 * 展示富文本
	 * 
	 * @param report 当前报表
	 * @param cellElment 当前格子
	 * 
	 *
	 * @date 2014-12-6-下午6:42:02
	 * 
	 */
    public void populate(ElementCase report, CellElement cellElment) {
    	Object cellValue = cellElment.getValue();
    	if(cellValue == null){
    		return;
    	}
    	if(cellValue instanceof BaseFormula){
    		cellValue = RichTextConverter.asFormula(String.valueOf(cellValue));
    	}
    	
    	if(cellValue instanceof Number){
    		cellValue = StableUtils.convertNumberStringToString(((Number) cellValue), false);
    	}
    	
    	//不是富文本的, 就转为字符串, 再转为富文本.
    	if(!(cellValue instanceof RichText)){
    		//用白名单来过滤单元格值, 限定字符串, 数字与公式, 其他类型的都return.
    		if(!(cellValue instanceof String)){
    			return;
    		}
    		
    		CellGUIAttr guiAttr = cellElment.getCellGUIAttr();
    		//不改变原有的style, 直接用新的
    		Style style = Style.DEFAULT_STYLE;
    		FRFont font = cellElment.getStyle().getFRFont();
    		double dpi96 = Constants.FR_PAINT_RESOLUTION;
    		double dpi72 = Constants.DEFAULT_FONT_PAINT_RESOLUTION;
    		int fontSize = roundUp(font.getSize() * dpi96 / dpi72);
    		font = FRFont.getInstance(font.getFontName(), font.getStyle(), fontSize);
    		style = style.deriveFRFont(font);
    		boolean asHtml = guiAttr != null && guiAttr.isShowAsHTML();
			cellValue = RichTextConverter.converHtmlToRichText(asHtml, cellValue.toString(), style);
    	}
    	
    	populateDocContent((RichText) cellValue);
    }
    
	private int roundUp(double num){
		String numStr = Double.toString(num);
		numStr = new BigDecimal(numStr).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
		return Integer.valueOf(numStr);
	}
    
    /**
	 * 更新富文本
	 * 
	 * @return 富文本数据
	 * 
	 *
	 * @date 2014-12-6-下午6:41:43
	 * 
	 */
    public RichText update() {
    	RichText richText = new RichText();
    	
    	DefaultStyledDocument doc = (DefaultStyledDocument) textPane.getDocument();
    	Style style = Style.DEFAULT_STYLE;
    	updateRichText(doc, style, richText);

    	return richText;
    }
    
    private void updateRichText(DefaultStyledDocument doc, Style style, RichText richText){
    	int len = doc.getLength();
    	RichChar lastChar = new RichChar(StringUtils.EMPTY, style);
    	for (int i = 0; i < len; i++) {
    		Element em = doc.getCharacterElement(i);
    		AttributeSet attrs = em.getAttributes();
			FRFont frFont = evalFont(attrs);
			style = style.deriveFRFont(frFont);
			
	    	try{
	    		String charStr = doc.getText(i, 1);
	    		//处理公式
	    		String formulaContent = parseFormula(charStr, doc, i, len);
	    		if(StringUtils.isNotEmpty(formulaContent)){
	    			lastChar = new RichChar(formulaContent, style);
	        		richText.addContent(lastChar);
	        		i += (formulaContent.length() - 1);
	        		continue;
	    		}
	    		
	    		//合并相同样式的字符
	    		if(lastChar.styleEquals(style)){
	    			lastChar.appendText(charStr);
	    			continue;
	    		}
	    		
	    		lastChar = new RichChar(charStr, style);
	    		richText.addContent(lastChar);
	    	}catch (Exception e) {
	    		FRContext.getLogger().error(e.getMessage());
			}
		}
    }
    
    //转换公式
    private String parseFormula(String charStr, DefaultStyledDocument doc, int i, int len){
    	String formula = StringUtils.EMPTY;
		if(!ComparatorUtils.equals(charStr, "$")){
			return formula;
		}
		try{
			//$后面的字符
			String restContent= doc.getText(i, len - i);
			int end = restContent.indexOf("}");
			
			if(end != -1){
				return restContent.substring(0, end + 1);
			}
		}catch (Exception e) {
		}
		
		return formula;
    }
    
    /**
	 * 生成富文本文档
	 * 
	 * @param richText 富文本数据
	 * 
	 *
	 * @date 2014-12-6-下午6:40:32
	 * 
	 */
    private void populateDocContent(RichText richText){
		DefaultStyledDocument doc = (DefaultStyledDocument) this.textPane.getDocument();
		
		Iterator<RichChar> it = richText.charIterator();
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		while(it.hasNext()){
			RichChar richChar = it.next();
			Style style = richChar.getStyle();
			String charStr = richChar.getText();
			populateRichTextStye(style, attrs);
			
			try {
				toolBar.removeInputListener();
				doc.insertString(doc.getLength(), charStr, attrs);
				toolBar.addInputListener();
			} catch (BadLocationException e) {
				FRContext.getLogger().error(e.getMessage());
			}
		}
    }
    
    /**
	 * 将Style转为swing富文本样式
	 * 
	 * @param style 当前字符样式
	 * @param attrs 富文本样式
	 * 
	 *
	 * @date 2014-12-6-下午6:39:54
	 * 
	 */
    private void populateRichTextStye(Style style, SimpleAttributeSet attrs){
    	if(style == null){
    		return;
    	}
    	
		FRFont font = style.getFRFont();
		
		StyleConstants.setFontFamily(attrs, font.getFamily());
		StyleConstants.setFontSize(attrs, font.getSize());
		StyleConstants.setBold(attrs, font.isBold());
		StyleConstants.setItalic(attrs, font.isItalic());
		StyleConstants.setUnderline(attrs, font.getUnderline() != Constants.LINE_NONE);
		StyleConstants.setSubscript(attrs, font.isSubscript());
		StyleConstants.setSuperscript(attrs, font.isSuperscript());
		StyleConstants.setForeground(attrs, font.getForeground());
		
		this.toolBar.populateToolBar(attrs);
    }
    
    /**
	 * 计算字体
	 * 
	 * @param attrs 样式属性
	 * 
	 * @return FR字体
	 * 
	 *
	 * @date 2014-12-6-下午6:39:23
	 * 
	 */
    private FRFont evalFont(AttributeSet attrs){
		String fontName = StyleConstants.getFontFamily(attrs);
		int fontSize = StyleConstants.getFontSize(attrs);

		Color color = StyleConstants.getForeground(attrs);
		boolean isSub = StyleConstants.isSubscript(attrs);
		boolean isSuper = StyleConstants.isSuperscript(attrs);
		
		int fontStyle = evalFontStyle(attrs);
		int underLine = evalUnderLine(attrs);

		
		return FRFont.getInstance(fontName, fontStyle, fontSize, color, underLine, false, false, isSuper, isSub);
    }
    
    /**
	 * 计算下划线, 默认只支持1种THIN
	 * 
	 * @param attrs 样式属性
	 * 
	 * @return 下划线样式
	 * 
	 *
	 * @date 2014-12-6-下午6:38:25
	 * 
	 */
    private int evalUnderLine(AttributeSet attrs){
    	int underLine = Constants.LINE_NONE;
    	boolean isUnder = StyleConstants.isUnderline(attrs);
    	
		if(isUnder){
			underLine += Constants.LINE_THIN;
		}
		
		return underLine;
    }
    
    /**
	 * 计算字体样式, 默认0, 加粗1, 斜体2
	 * 
	 * @param attrs 样式属性
	 * 
	 * @return 字体样式
	 * 
	 *
	 * @date 2014-12-6-下午6:37:50
	 * 
	 */
    private int evalFontStyle(AttributeSet attrs){
    	int fontStyle = Font.PLAIN;
		boolean isBold = StyleConstants.isBold(attrs);
		boolean isIta = StyleConstants.isItalic(attrs);
		
		if(isBold){
			fontStyle += Font.BOLD;
		}
		
		if(isIta){
			fontStyle += Font.ITALIC;
		}
		
		return fontStyle;
    }

}
