package com.fr.design.style.color;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * 最近使用颜色
 * @author focus
 *
 */
public class ColorSelectConfigManager{
	
	// 最近使用的颜色个数
	private int colorNums = 20;
	// 最近使用颜色
	private List<Color> colors;
	
	private static ColorSelectConfigManager colorSelectConfigManager = null;
	
	public  Color[] getColors() {
		if(colors == null){
			colors = new ArrayList<Color>();
		}
		return colors.toArray(new Color[colors.size()]);
	}

	public int getColorNum() {
		return colorNums;
	}
	public void setColorNum(int colorNums) {
		this.colorNums = colorNums;
	}

	public synchronized static ColorSelectConfigManager getInstance() {
		if (colorSelectConfigManager == null) {
			colorSelectConfigManager = new ColorSelectConfigManager();
		}
		return colorSelectConfigManager;
	}

	/**
	 * 添加颜色到最近使用队列中
	 * 
	 * @param color 颜色
	 * 
	 */
	public void addToColorQueue(Color color){
		// 过滤重复的最近使用颜色
		// 因为有个后进先出的问题，最近使用的颜色需要放到最前面所以没用set
		if(colors.contains(color)){
			colors.remove(color);
		}
		colors.add(color);
	} 
}