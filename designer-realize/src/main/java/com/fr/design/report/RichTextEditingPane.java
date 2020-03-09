/**
 * 
 */
package com.fr.design.report;

import com.fr.base.BaseFormula;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.frpane.UITextPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.report.cell.cellattr.core.RichText;
import com.fr.report.cell.cellattr.core.RichTextConverter;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.StyledDocument;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 富文本的编辑区域
 * 
 *
 *
 * @date: 2014-12-24-上午9:42:15
 */
public class RichTextEditingPane extends UITextPane{
	
	// = FLAG.length() + PREFIX.length() + SUFFIX.length();
	private static final int WRAPPER_LEN = 3;
	// = FLAG.length() + PREFIX.length()
	private static final int PREFIX_LEN = 2;
	
	//是否正在编辑文本内容, 主要有两个监听器, 可能会互相覆盖, 一个是监听输入的, 一个是监听光标的
	private boolean updating = false;
	
	/**
	 * 构造函数
	 */
	public RichTextEditingPane() {
		this.addMouseListener(doubleClickFormulaListener);
        this.addFocusListener(focusListener);
	}

	/**
	 * 是否有其他进程正在更新编辑区域
	 * 
	 * @return 是否有其他进程正在更新编辑区域
	 * 
	 *
	 * @date 2014-12-24-上午10:01:49
	 * 
	 */
	public boolean isUpdating() {
		return updating;
	}

	/**
	 * 设置更新状态
	 * 
	 * @param updating 是否正在更新
	 * 
	 *
	 * @date 2014-12-24-上午10:02:13
	 * 
	 */
	public void setUpdating(boolean updating) {
		this.updating = updating;
	}
	
	/**
	 * 开始更新
	 * 
	 *
	 * @date 2014-12-24-上午10:02:31
	 * 
	 */
	public void startUpdating(){
		this.updating = true;
	}
	
	/**
	 * 结束更新
	 * 
	 *
	 * @date 2014-12-24-上午10:02:41
	 * 
	 */
	public void finishUpdating(){
		this.updating = false;
	}
	
	//双击选取公式监听器
	private MouseListener doubleClickFormulaListener = new MouseAdapter() {
		
		private int findFormulaStart(int start, StyledDocument doc) throws BadLocationException{
			//往前回溯, 寻找${, 如果发现先找到了}, 说明不在公式内部, 直接return.
			//有可能当前字符刚好处于{后面, 所以要-1
			for (int i = start - 1; i >= 0; i--) {
				String _char = doc.getText(i, 1);
				if(ComparatorUtils.equals(_char, RichText.SUFFIX)){
					return - 1;
				}
				
				//发现大括号了, 再找$
				if(ComparatorUtils.equals(_char, RichText.PREFIX)){
					if(i - 1 >= 0 && ComparatorUtils.equals(doc.getText(i - 1, 1), RichText.FLAG)){
						return i - 1;
					}
				}
			}
			
			return -1;
		}
		
		private int findFormulaEnd(int start, StyledDocument doc) throws BadLocationException{
			//再往后找"}"
			int total = doc.getLength();
			for (int j = start; j < total; j++) {
				String _char = doc.getText(j, 1);
				//发现左大括号了, 肯定异常
				if(ComparatorUtils.equals(_char, RichText.PREFIX)){
					return -1;
				}
				
				if(ComparatorUtils.equals(_char, RichText.SUFFIX)){
					//要把后缀包进去, 所以+1
					return j + 1;
				}
			}
			
			return -1;
		}
		
		private void popUpFormulaEditPane(final String formulaContent, final int formulaStart, 
				final AttributeSet attrs){
			final UIFormula formulaPane = FormulaFactory.createFormulaPane();
			formulaPane.populate(BaseFormula.createFormulaBuilder().build(formulaContent));
			formulaPane.showLargeWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
				@Override
				public void doOk() {
					StyledDocument doc = (StyledDocument) RichTextEditingPane.this.getDocument();
					BaseFormula fm = formulaPane.update();
					String content = RichTextConverter.asFormula(fm.getContent());
					try {
						doc.remove(formulaStart, formulaContent.length() + WRAPPER_LEN);
						doc.insertString(formulaStart, content, attrs);
					} catch (BadLocationException e) {
                        FineLoggerFactory.getLogger().error(e.getMessage(), e);
					}
				}
			}).setVisible(true);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getClickCount() == 2){
				// 取得选择文本的起始位置和结束位置
				int start = RichTextEditingPane.this.getSelectionStart();
				
				if(start <= 0){
					return;
				}
				
				StyledDocument doc = (StyledDocument) RichTextEditingPane.this.getDocument();
				try {
					//公式起点
					final int formulaStart = findFormulaStart(start, doc);
					if(formulaStart == -1){
						return;
					}
					
					//公式终点
					int formulaEnd = findFormulaEnd(start, doc);
					if(formulaEnd == -1){
						return;
					}
					
					//找到公式的起点与终点了, 下面就是选中, 并弹出编辑窗口
					RichTextEditingPane.this.select(formulaStart, formulaEnd);
					//缓存第一个字符的样式, 用于给新公式设置样式
					Element ele = doc.getCharacterElement(formulaStart);
					final AttributeSet attrs = ele.getAttributes();
					
					final String formulaContent = doc.getText(formulaStart + PREFIX_LEN, formulaEnd - formulaStart - WRAPPER_LEN);
					//弹出公式编辑窗口
					popUpFormulaEditPane(formulaContent, formulaStart, attrs);
				} catch (BadLocationException e1) {
                    FineLoggerFactory.getLogger().error(e1.getMessage());
				}
			}
		}
	};

    private FocusListener focusListener = new FocusAdapter() {
        /**
         * 移除高亮，重新选中文本
         * @param e
         */
        public void focusGained(FocusEvent e) {
            RichTextEditingPane richTextPane = RichTextEditingPane.this;
            richTextPane.getHighlighter().removeAllHighlights();
            richTextPane.select(richTextPane.getSelectionStart(), richTextPane.getSelectionEnd());
        }

        /**
         * 失去焦点时，被选中的文本保持着被选中时的样式
         * @param e
         */
        public void focusLost(FocusEvent e) {
            RichTextEditingPane richTextPane = RichTextEditingPane.this;
            int start = richTextPane.getSelectionStart();
            int end = richTextPane.getSelectionEnd();
            richTextPane.select(start, end);
            Highlighter highlighter = richTextPane.getHighlighter();
            richTextPane.getHighlighter().removeAllHighlights();
            try {
                highlighter.addHighlight(start, end, DefaultHighlighter.DefaultPainter);
            } catch (BadLocationException exception) {
                FineLoggerFactory.getLogger().error(exception.getMessage(), exception);
            }
        }
    };
}