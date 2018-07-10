package com.fr.design.mainframe.chart.gui.data;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.fr.base.present.DictPresent;
import com.fr.base.present.FormulaPresent;
import com.fr.base.present.Present;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.present.FormulaPresentPane;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.Inter;

/**
 * 公式下拉框编辑器.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-27 上午01:59:49
 */
public class PresentComboBox extends UIComboBox{
	private Present present;
	
	private String[] ITEMS = {
			Inter.getLocText("DS-Dictionary"),
			Inter.getLocText("Present-Formula_Present"),
			Inter.getLocText("Present-No_Present")
	};
	
	public PresentComboBox() {
		super();
		
		this.addItem(ITEMS[0]);
		this.addItem(ITEMS[1]);
		this.addItem(ITEMS[2]);
		
		this.setSelectedItem(ITEMS[2]);
		
		this.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}
			
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				if (PresentComboBox.this.getSelectedIndex() == 0) {
					final DictionaryPane pp = new DictionaryPane();
					if (present instanceof DictPresent) {
						pp.populateBean(((DictPresent) present).getDictionary());
					}
					
					BasicDialog bg = pp.showWindow(SwingUtilities.getWindowAncestor(new JPanel()), new DialogActionAdapter(){
						public void doOk() {
							PresentComboBox.this.populate(new DictPresent(pp.updateBean()));
							fireChange();
						}
					});
					
					bg.setVisible(true);
				} else if (PresentComboBox.this.getSelectedIndex() == 1) {
					final FormulaPresentPane pp = new FormulaPresentPane();
					if (present instanceof FormulaPresent) {
						pp.populateBean((FormulaPresent) present);
					}
					
					BasicDialog bg = pp.showSmallWindow(SwingUtilities.getWindowAncestor(new JPanel()), new DialogActionAdapter(){
						public void doOk() {
							PresentComboBox.this.populate(pp.updateBean());
							fireChange();
						}
					});
					
					bg.setVisible(true);
				} else {
					present = null;
					fireChange();
				}
			}
			
			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		});
	}
	
	/**
	 * 重载用于加载 完成编辑后 ok的响应事件
	 */
	protected void fireChange() {
		
	}
	
	/**
	 * 更新公式选择.
	 */
	public void populate(Present present) {
		if (present instanceof DictPresent) {
			this.setSelectedIndex(0);
		} else if (present instanceof FormulaPresent) {
			this.setSelectedIndex(1);
		} else if (present == null) {
			this.setSelectedIndex(2);
		}
		
		this.present = present;
	}
	
	/**
	 * 返回编辑的结果.
	 */
	public Present update() {
		return present;
	}
}