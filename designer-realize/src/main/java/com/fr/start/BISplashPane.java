/**
 * 
 */
package com.fr.start;

import java.awt.Image;

import com.fr.base.BaseUtils;

/**
 * @author neil
 *
 * @date: 2015-3-13-上午9:47:58
 */
public class BISplashPane extends SplashPane{

	/**
	 * 创建启动画面的背景图片
	 * 
	 * @return 背景图片
	 * 
	 */
	public Image createSplashBackground() {
        return BaseUtils.readImage("/com/fr/base/images/oem/splash-EN.jpg");
    }
	
}