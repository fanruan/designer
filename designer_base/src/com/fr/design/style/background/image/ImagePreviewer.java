package com.fr.design.style.background.image;

import com.fr.base.Style;

import java.awt.Image;

/**
 * 图片预览接口(由于子类上层父类差别较大,无奈的接口)
 * Created by zack on 2018/3/10.
 */
public interface ImagePreviewer {
    /**
     * 设置图片样式(平铺,拉伸)
     * @param style 样式
     */
    void setImageStyle(Style style);

    /**
     * 设置图片
     * @param image 图片
     */
    void setImage(Image image);

    /**
     * 显示正在加载
     */
    void showLoading();

    /**
     * 重绘
     */
    void repaint();
}
