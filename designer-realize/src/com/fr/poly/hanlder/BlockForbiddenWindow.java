/**
 * 
 */
package com.fr.poly.hanlder;

import javax.swing.JWindow;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.icon.IconPathConstants;
import com.fr.general.Inter;

/**
 * 禁止块重叠的提示窗口
 * 
 * @author neil
 *
 * @date: 2015-2-27-上午10:49:42
 */
public class BlockForbiddenWindow extends JWindow{
	
	private static final int WIDTH = 150;
	private static final int HEIGHT = 20;
	
    private UIButton promptButton = new UIButton(Inter.getLocText("FR-Designer_Block-intersect"), BaseUtils.readIcon(IconPathConstants.FORBID_ICON_PATH));

	/**
	 * 构造函数
	 */
	public BlockForbiddenWindow() {
		this.add(promptButton);
		
		this.setSize(WIDTH, HEIGHT);
	}
	
	/**
	 * 在指定位置显示窗口, 默认将window的中心点放到指定位置上
	 * 
	 * @param x x坐标
	 * @param y y坐标
	 * 
	 */
	public void showWindow(int x, int y){
		this.setLocation(x - WIDTH / 2, y - HEIGHT / 2);
		this.setVisible(true);
	}
	
	/**
	 * 隐藏当前窗口
	 * 
	 */
	public void hideWindow(){
		this.setVisible(false);
	}
}