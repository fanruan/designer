package com.fr.design.mainframe.chart.gui.style;

import com.fr.chart.base.ChartConstants;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.style.color.ColorCell;
import com.fr.design.style.color.ColorSelectConfigManager;
import com.fr.design.style.color.ColorSelectDetailPane;
import com.fr.design.style.color.ColorSelectDialog;
import com.fr.design.style.color.ColorSelectable;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 图表颜色填充--32种精确颜色选择界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-8-22 上午09:27:03
 */
public class ChartAccColorPane extends BasicPane implements MouseListener, UIObserver,ColorSelectable{
	private static final long serialVersionUID = 7536620547840565075L;
	private static final int WIDTH = 16;
	private static final int ROWCOUNT = 8;
	
	private Color[] colors = new Color[32];
	
	private int currentIndex = 0;
	
    private ChangeListener changeListener = null;
    private UIObserverListener uiObserverListener;
    
    private Color color = null;
	
	public ChartAccColorPane() {
		this.addMouseListener(this);
		
		Color[] values = ChartConstants.CHART_COLOR_ARRAY;
		for(int i = 0; i < values.length; i++) {
			colors[i] = values[i];
		}
		
		iniListener();
	}
	
    private void iniListener() {
        if (shouldResponseChangeListener()) {
            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.repaint();

		this.setLayout(null);
		this.setBounds(0,0,WIDTH*ROWCOUNT, WIDTH*4);
		Paint oldPaint = g2d.getPaint();
        g2d.setPaint(new Color(212, 212, 216));
        g2d.fillRect(0, 0, WIDTH*ROWCOUNT, WIDTH*4);
        g2d.setPaint(oldPaint);
		
		int y  = 0;
		int x = 0;
		for(int i = 0; i < colors.length; i++) {
			Color color = colors[i];
			g2d.setColor(color != null ? color : Color.WHITE);
			
			if(i % ROWCOUNT == 0 && i != 0) {
				y += WIDTH;
			}
			x = i % ROWCOUNT;
			g2d.fillRect(x * WIDTH, y, WIDTH, WIDTH);
		}
	}
	 
	@Override
	protected String title4PopupWindow() {
		return "";
	}
	
	private int getColorIndex(double ex, double ey) {
		int x = (int)(ex / WIDTH) % ROWCOUNT;
		int y = (int)(ey / WIDTH) % ROWCOUNT;
		
		return x + y * ROWCOUNT;
	}

    /**
     * 鼠标点击
     * @param e 鼠标事件
     */
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if(!(x > 0 && x < WIDTH * ROWCOUNT)) {
			return;
		}
		if(!(y > 0 && y < WIDTH * ROWCOUNT / 2)) {
			return;
		}
		
		int index = getColorIndex(e.getX(), e.getY());
		if(index < colors.length) {
			currentIndex = index;
			
			ColorSelectDetailPane pane = new ColorSelectDetailPane(colors[currentIndex]);
			ColorSelectDialog.showDialog(DesignerContext.getDesignerFrame(), pane, colors[currentIndex], this);
			Color choosedColor = this.getColor();
	        if (choosedColor != null) {
	        	colors[currentIndex] = choosedColor;
				DesignerEnvManager.getEnvManager().getColorConfigManager().addToColorQueue(choosedColor);
            	ChartAccColorPane.this.stateChanged();
	        }
	        ChartAccColorPane.this.repaint();
		}
	}

    /**
     * 鼠标进入
     * @param e 鼠标事件
     */
	public void mouseEntered(MouseEvent e) {
		
	}

    /**
     * 鼠标离开
     * @param e 鼠标事件
     */
	public void mouseExited(MouseEvent e) {
		
	}

    /**
     * 鼠标按压
     * @param e 鼠标事件
     */
	public void mousePressed(MouseEvent e) {
		
	}

    /**
     * 鼠标释放
     * @param e 鼠标事件
     */
	public void mouseReleased(MouseEvent e) {
		
	}
	
	public void populateBean(Color[] values) {
		for(int i = 0; i < colors.length; i++) {
			if(i < values.length) {
				colors[i] = values[i];
			} else {
				colors[i] = Color.WHITE;
			}
		}
		this.repaint();
	}
	
	public Color[] updateBean() {
		return colors;
	}
	
    /**
    * 根据接口 注册事件.
    * @param listener 监听事件
    */
   public void registerChangeListener(UIObserverListener listener) {
       uiObserverListener = listener;
   }

   /**
    * 是否响应事件.
    * @return boolean 响应
    */
   public boolean shouldResponseChangeListener() {
       return true;
   }
   
   /**
   * 属性改变时, 响应ChangeListener
   */
  public void stateChanged() {
      if (changeListener != null)  {
          changeListener.stateChanged(null);
      }
  }

  /**
   *添加改变事件.
   * @param changeListener 改变事件
   */
  public void addChangeListener(ChangeListener changeListener) {
      this.changeListener = changeListener;
  }

@Override
public void setColor(Color color) {
	this.color = color;
}

@Override
public Color getColor() {
	return this.color;
}

@Override
/**
 * 不处理
 * 
 * @param colorCell 颜色单元格
 */
public void colorSetted(ColorCell colorCell) {
	
}

}