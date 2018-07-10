package com.fr.grid;

import java.awt.Color;
import java.awt.Graphics;

import com.fr.base.FRContext;

/**
 * 画动态虚线
 * @author Daniel~
 *
 */
public class DrawFlowRect implements Runnable {
	private int templen=0;
	private int lenReal=0;
	private boolean isReal=false;
	private Grid grid;
	private boolean running = true;
	
	
	public DrawFlowRect(){
		new Thread(this, "drawFlowLine").start();
	}

	/**
	 *
	 * @param grid
	 */
	public void setGrid(Grid grid){
		this.grid=grid;
	}

	/**
	 *
	 */
	public  void run() {
		while(running){
			templen = templen +1;
			if(templen >= 4)
	        {
	            templen = 0;
	            isReal = !isReal;
	        }
			
			//不是这个状态就不要继续画了，否则太卡
			if(grid != null && !grid.IsNotShowingTableSelectPane()){
				grid.repaint();
			}
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				FRContext.getLogger().error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 画框框
	 * @param g
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void drawFlowRect(Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();
        
        lenReal = templen;
            
        boolean oldisReal = isReal;  //备份
        int oldlenReal = lenReal; //备份
                
		drawTopLine(g, x, y, width, oldisReal, oldlenReal);
            
		drawRightLine(g, x, y, width, height, oldisReal, oldlenReal);

		drawLeftLine(g, x, y, height, oldisReal, oldlenReal);
                        
		drawBottomLine(g, x, y, width, height, oldisReal, oldlenReal);
                        
        isReal = oldisReal;     //还原 数据                
        g.setColor(oldColor);
    }

	private void drawTopLine(Graphics g, int x, int y, int width, boolean oldisReal, int oldlenReal) {
		g.setColor(Color.black);          //上
		//第一根线先画黑色背景
		g.drawLine(x, y, width, y);
		//第二根线同上
		g.drawLine(x-1, y-1, width, y-1);
		g.setColor(Color.WHITE);
		//第一根白色虚线
		drawFlowLine(g, x, y, width, y);
		isReal = oldisReal;     //还原 数据
		lenReal = oldlenReal;
		//第二根白色虚线
		drawFlowLine(g, x-1, y-1, width, y-1);
	}

	private void drawRightLine(Graphics g, int x, int y, int width,int height, boolean oldisReal, int oldlenReal) {
		g.setColor(Color.black);         //右
		g.drawLine(width, y, width, height);
		g.drawLine(width-1, y-1, width-1, height);
		g.setColor(Color.WHITE);
		isReal = oldisReal;     //还原 数据
		lenReal = oldlenReal;
		drawFlowLine(g, width, y, width, height);
		isReal = oldisReal;     //还原 数据
		lenReal = oldlenReal;
		drawFlowLine(g, width-1, y-1, width-1, height);
	}

	private void drawLeftLine(Graphics g, int x, int y, int height, boolean oldisReal, int oldlenReal) {
		//左下要反过来画
        g.setColor(Color.black);          //左
        g.drawLine(x, height, x, y);
        g.drawLine(x+1, height-1, x+1, y);
        g.setColor(Color.WHITE);
        isReal = oldisReal;     //还原 数据
        lenReal = oldlenReal;
        drawFlowLine(g, x, height, x, y );
        isReal = oldisReal;     //还原 数据
        lenReal = oldlenReal;
        drawFlowLine(g, x+1, height+1, x+1, y );
	}

	private void drawBottomLine(Graphics g, int x, int y, int width,int height, boolean oldisReal, int oldlenReal) {
		g.setColor(Color.black);         //下
		g.drawLine(width, height, x, height);
		g.drawLine(width+1, height+1, x, height+1);
		g.setColor(Color.WHITE);
		isReal = oldisReal;     //还原 数据
		lenReal = oldlenReal;
		drawFlowLine(g, width, height, x, height);
		isReal = oldisReal;     //还原 数据
		lenReal = oldlenReal;
		drawFlowLine(g, width+1, height+1, x, height+1);
	}
	
    //流动虚线,支持反方向作画
    private void drawFlowLine(Graphics g, int x1, int y1, int x2, int y2) {
        int x = x1, y=y1;
        int n = 4;  //实线段长度
        int m = 5;  //虚线段长度
        
        int tx = 0,ty = 0;
        
        int mark_x = 0;  //标记 如果为1 表示水平线
        int mark_y = 0;  //标记 如果为1 标识垂直线
        
        int c = 1;
        boolean flag = true;
        
        //正向为1反向为-1不变为0
        if(x2-x1 != 0)
            mark_x = x1<x2?1:-1;
        else
            mark_y = y1<y2?1:-1;
        
        
        //先画流动虚线的第一部分
        if(isReal)
        {
            //实线
            tx = (lenReal*mark_x + x1);
            ty = (lenReal*mark_y + y1);
            g.drawLine(x,y,tx,ty);
            x = ((m + lenReal)*mark_x + x1);
            y = ((m + lenReal)*mark_y + y1);
        }
        else
        {
            //虚线
            x = (lenReal*mark_x + x1);
            y = (lenReal*mark_y + y1);
        }
        
        int ttx = x; //第二部分起始位置 x坐标
        int tty = y; //第二部分起始位置 y坐标
        
        
		drawTheRestPart(g, x, y, x1, y1, x2, y2, m, n, ttx, tty, mark_x, mark_y, tx, ty, flag, c);
    }

	private void drawTheRestPart(Graphics g, int x, int y, int x1, int y1, int x2, int y2, int m, int n, int ttx, int tty,
								 int mark_x, int mark_y, int tx, int ty, boolean flag, int c) {
		do
		{
			tx = ((c*(n+m) - m)*mark_x + ttx);
			ty = ((c*(n+m) - m)*mark_y + tty);

			if(Math.abs(tx-x1) > Math.abs(x2-x1))
			{
				lenReal = Math.abs(tx - x2);   //只支持 水平线和竖线
				isReal = true;

				tx = x2;
				flag = false;
			}
			if(Math.abs(ty - y1) > Math.abs(y2 - y1))
			{
				lenReal = Math.abs(ty - y2);
				isReal = true;

				ty = y2;
				flag = false;
			}
			g.drawLine(x,y,tx,ty);
			x = (c*(n+m)*mark_x + ttx);
			y = (c*(n+m)*mark_y + tty);
			if(x > Math.max(x1, x2) || y > Math.max(y1, y2))
			{
				if(flag)
				{
					isReal = false;
					if(x > Math.max(x1, x2))
						lenReal = x-Math.max(x1, x2);
					else
						lenReal = y-Math.max(y1, y2);
				}
				break;
			}
			c++;
		}
		while(flag);
	}

	/**
	 *
	 */
    public void exit() {
    	this.running = false;
    }

}