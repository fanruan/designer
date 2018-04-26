package com.fr.design.style.background.texture;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.general.Background;
import com.fr.design.style.AbstractSelectBox;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-10-31 下午04:20:33
 * 类说明: 纹理的选择box. 类似{@code ColorSelectBox}
 */
public class TextureSelectBox extends AbstractSelectBox<Background> {
	private static final long serialVersionUID = -113494509215643549L;
	
	private Background background = null;
	
	public TextureSelectBox(int preWidth) {
		initBox(preWidth);
	}
	
	public Background getSelectObject() {
		return this.background;
	}
	
	@Override
	public JPanel initWindowPane(double preWidth) {
		TextureSelectPane selectPane = new TextureSelectPane(preWidth);
    	
		selectPane.addTextureChangeListener(new ChangeListener() {
    		
    		public void stateChanged(ChangeEvent e) {
    			TextureSelectPane source = (TextureSelectPane)e.getSource();
    			setSelectObject(source.getSelectBackground());
    			
    			hidePopupMenu();
    		}
    	});
    	
    	return selectPane;
	}

	@Override
	public void setSelectObject(Background t) {
		this.background = t;
		
		fireDisplayComponent(t);
	}
}