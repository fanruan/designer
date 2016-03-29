package com.fr.start;

import java.awt.Image;

import com.fr.base.BaseUtils;

public class ChartSplashPane extends SplashPane{

	/**
	 * 创建启动画面的背景图片
	 * 
	 * @return 背景图片
	 * 
	 */
	public Image createSplashBackground() {
		return BaseUtils.readImage("/com/fr/design/images/splash4Chart.png");
	}
}