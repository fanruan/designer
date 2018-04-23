package com.fr.design.designer.beans.location;

import com.fr.design.mainframe.FormDesigner;

public interface Direction {
	
	/**
	 * 拖拽组件
	 * @param dx 水平方向位移
	 * @param dy 垂直方向位移
	 * @param designer 设计器
	 */
	void drag(int dx, int dy, FormDesigner designer);

	/**
	 * 更新鼠标样式
	 * @param formEditor ： 设计器
	 */
    void updateCursor(FormDesigner formEditor);
    
	/**
	 * Direction的位置标示，top = 1,bottom = 2等
	 */
    int getActual();

    /**
     * 拖拽前先备份原始位置，拖拽过程中用于比较位移跟原始位置从而确定新位置大小
     * @param formEditor   设计器
     */
    void backupBounds(FormDesigner formEditor);
    
    public static final int TOP = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    public static final int LEFT_TOP = 5;
    public static final int LEFT_BOTTOM = 6;
    public static final int RIGHT_TOP = 7;
    public static final int RIGHT_BOTTOM = 8;
    public static final int INNER = 0;
    public static final int OUTER = -1;
    
    public static final int[] ALL = new int[]{TOP, BOTTOM, LEFT, RIGHT, LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM, INNER};
    public static final int[] TOP_BOTTOM_LEFT_RIGHT= new int[]{TOP, BOTTOM, LEFT, RIGHT};
}