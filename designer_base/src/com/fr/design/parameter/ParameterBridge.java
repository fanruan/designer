package com.fr.design.parameter;


import com.fr.general.Background;

/**
 * @author richie
 * @date 2014/11/06
 * @since 8.0
 */
public interface ParameterBridge {

    /**
     * 是否延迟展示报表内容，也就是说是否要等点击了查询之后才执行报表
     * @return 如果是true，则表示点击之后才开始计算，false则表示会根据参数默认值直接计算报表并展现
     */
    public boolean isDelayDisplayContent();

    /**
     * 是否显示参数界面
     * @return 显示参数界面则返回true，否则返回false
     */
    public boolean isDisplay();

    /**
     * 获取参数界面背景
     * @return 参数界面背景
     *
     */
    public Background getDataBackground();

    /**
     * 获取参数界面的宽度
     * @return 宽度
     */
    public int getDesignWidth();

    /**
     * 获取参数面板的对齐方式
     * @return 左中右三种对齐方式
     */
    public int getPosition();

    public void setDelayDisplayContent(boolean delayPlaying);

    public void setPosition(int align);

    public void setDisplay(boolean showWindow);

    public void setBackground(Background background);
}