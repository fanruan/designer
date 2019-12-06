package com.fr.design.gui.frpane;


import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.javascript.Commit2DBJavaScriptPane;
import com.fr.general.ComparatorUtils;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

/**
 * User: zheng
 * Date: 14-3-28
 * Time: 下午3:24
 */

public  class CommitTabbedPane extends JComponent implements MouseListener, MouseMotionListener {

    private Icon closeIcon = BaseUtils.readIcon("com/fr/design/images/gui/tab_delete.png");
    private static final Icon ADD_NORMAL = BaseUtils.readIcon("com/fr/design/images/gui/tab_add_normal.png");
    private static final Icon ADD_OVER = BaseUtils.readIcon("com/fr/design/images/gui/tab_add_hover.png");
    private static final Icon ADD_CLICK = BaseUtils.readIcon("com/fr/design/images/gui/tab_add_click.png");
    private static final Image DESIGN_IMAGE = BaseUtils.readImage("com/fr/design/images/sheet/left_right_btn.png");
    private static final Icon LEFT_ICON = BaseUtils.createIcon(DESIGN_IMAGE, 0, 0, 14, 14);
    private static final Icon RIGHT_ICON = BaseUtils.createIcon(DESIGN_IMAGE, 14, 0, 14, 14);
    private static final Icon DISABLED_LEFT_ICON = BaseUtils.createIcon(DESIGN_IMAGE, 0, 14, 14, 14);
    private static final Icon DISABLED_RIGHT_ICON = BaseUtils.createIcon(DESIGN_IMAGE, 14, 14, 14, 14);
    private Icon addIcon = ADD_NORMAL;

    private static final int TOOLBAR_HEIGHT = 16;  //  按钮高度
    private static final int GAP = 5;       //间隔
    private static final int SMALLGAP = 3;
    
    private static final int FIRST_TAB_POSITION = 20;


    // 左移和右移按钮
    private UIButton leftButton;
    private UIButton rightButton;

    private JPanel buttonPane;

    private List dbManipulationPaneList ;

    private Commit2DBJavaScriptPane commit2DBJavaScriptPane;

    // 能显示的tab个数
    private int showCount = 0;

    //选了30度和60度的特殊角度的x,y作为经过的两个点的坐标
    private double specialLocation1 = 2.5;
    private double specialLocation2 = 4.330127;

    private int mouseOveredIndex = -1;

    private int selectedIndex = -1;

    private static final double CORNOR_RADIUS = 5.0;


    //tab栏可以放下的每个tab的实际宽度
    private int tabWidth = 70;


    //当前标签页栏存放的所有标签页的index
    private int scrollIndex = 0;
    private int lastOneIndex = 0;

    //添加标签位置
    private int addX = -1;
    private int addY = -1;


    //各删除标签位置
    private int[] closeIconStartX;


    public  CommitTabbedPane(Commit2DBJavaScriptPane commit2DBJavaScriptPane, List dbManipulationPaneList ){
        this.commit2DBJavaScriptPane = commit2DBJavaScriptPane;
        this.dbManipulationPaneList = dbManipulationPaneList;
        this.setLayout(new BorderLayout(0, 0));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setBorder(null);
        this.setForeground(new Color(99, 99, 99));
        leftButton = new UIButton(LEFT_ICON) {
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, TOOLBAR_HEIGHT);
            }
        };
        leftButton.set4ToolbarButton();
        leftButton.setDisabledIcon(DISABLED_LEFT_ICON);
        rightButton = new UIButton(RIGHT_ICON) {
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, TOOLBAR_HEIGHT);
            }
        };
        rightButton.set4ToolbarButton();
        rightButton.setDisabledIcon(DISABLED_RIGHT_ICON);
        buttonPane = new JPanel(new BorderLayout(3, 0));
        buttonPane.add(rightButton, BorderLayout.EAST);
        buttonPane.add(leftButton, BorderLayout.CENTER);
        this.add(buttonPane, BorderLayout.EAST);
        leftButton.addActionListener(createLeftButtonActionListener());
        rightButton.addActionListener(createRightButtonActionListener());

    }

    private ActionListener createRightButtonActionListener(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tabCount = getTabCount();
                if (lastOneIndex < tabCount && lastOneIndex + showCount <= tabCount) {
                    scrollIndex += showCount;
                    lastOneIndex += showCount;
                    selectedIndex = lastOneIndex;
                } else if(lastOneIndex < tabCount && lastOneIndex + showCount > tabCount){
                    lastOneIndex = tabCount -1;
                    scrollIndex = lastOneIndex - showCount;
                    selectedIndex = lastOneIndex;
                }
                repaint();
            }
        };
    }

    private ActionListener createLeftButtonActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(scrollIndex >= showCount) {
                    scrollIndex -= showCount;
                    selectedIndex = scrollIndex;
                    lastOneIndex -= showCount;
                } else if (scrollIndex > 0 && scrollIndex< showCount){
                    scrollIndex =0;
                    selectedIndex = 0;
                    lastOneIndex = showCount;

                }
                repaint();
            }
        };
    }

    private void checkButton(boolean buttonEnabled) {
        leftButton.setEnabled(buttonEnabled);
        rightButton.setEnabled(buttonEnabled);
    }

    public int getSelectedIndex(){
        return selectedIndex;
    }

    public void  paintComponent(Graphics g){
        super.paintComponent(g);
        double maxWidth = getWidth() - buttonPane.getWidth();
        Graphics2D  g2d = (Graphics2D) g;
        paintBackgroundAndLine(g2d, maxWidth);

    }

    private void paintBackgroundAndLine(Graphics2D g2d, double maxWidth) {
        //能画的个数
        showCount = (int) (maxWidth) / tabWidth;
        //计算开始画的最小模板index和最大模板index
        if (selectedIndex >= dbManipulationPaneList.size()) {
            selectedIndex = dbManipulationPaneList.size() - 1;
        }
        if (selectedIndex < 0) {
            selectedIndex = 0;
        }
        calMinAndMaxIndex();
        closeIconStartX = new int[lastOneIndex - scrollIndex + 1];

        int startX = 0;
        //从可以开始展示在tab面板上的tab开始画
        for (int i = scrollIndex; i <= lastOneIndex; i++) {
            DBManipulationPane dbManipulationPane = (DBManipulationPane)dbManipulationPaneList.get(i);
            String name ;
            if (dbManipulationPane.getSubMitName() != null){
                name = dbManipulationPane.getSubMitName();
            } else {
                name = createName();
                dbManipulationPane.setSubMitName(name);
            }
            if (i == selectedIndex) {
                closeIconStartX[i - scrollIndex] = paintSelectedTab(g2d, startX,name, i);
            } else {
                closeIconStartX[i - scrollIndex] = paintUnSelectedTab(g2d,startX,name,i);
            }
            startX += tabWidth;
        }
        paintUnderLine(startX, maxWidth, g2d);
        addX = startX + GAP;
        addIcon.paintIcon(this,g2d,addX,0);
        checkButton(getTabCount() > showCount);
    }

    public int getTabCount(){
        return dbManipulationPaneList.size();
    }

    private String createName(){
        String prefix = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_CommitTab_Submit");
        int  count = getTabCount();
        while (true) {
            //从提交1开始
            count = count == 0 ? 1 : count;
            String newName = prefix + count;
            boolean repeated = false;
            for (int  i= 0;i < getTabCount();i++) {
                if (ComparatorUtils.equals( ((DBManipulationPane)dbManipulationPaneList.get(i)).getSubMitName(), newName)) {
                    repeated = true;
                    break;
                }
            }

            if (!repeated) {
                return newName;
            }

            count++;
        }
    }



    private void paintUnderLine(double startX, double maxWidth, Graphics2D g2d) {
        //画下面的那条线
        if (startX < maxWidth) {
            GeneralPath generalPath = new GeneralPath(Path2D.WIND_EVEN_ODD, 2);
            generalPath.moveTo((float) startX, (float) (getHeight() - 1));
            generalPath.lineTo((float) maxWidth, (float) (getHeight() - 1));
            g2d.fill(generalPath);
            g2d.setPaint(UIConstants.LINE_COLOR);
            g2d.draw(new Line2D.Double((float) startX, (float) (getHeight() - 1), (float) maxWidth , (float) (getHeight() - 1)));
        }
    }


    private void calMinAndMaxIndex() { //如果个数大于最多能容纳的个数，则多余的进行处理       

        if (dbManipulationPaneList.size() > showCount) {

            if (selectedIndex >= lastOneIndex) {     //所点击列表中的标签页处在标签页栏最后一个标签页之后，则标签页栏左移至所点击标签页出现
                scrollIndex = selectedIndex - showCount + 1;
                lastOneIndex = selectedIndex;
                if (scrollIndex <= 0) {
                    scrollIndex = 0;
                    lastOneIndex = showCount - 1;
                }
            } else if (selectedIndex <= scrollIndex) {  //所点击列表中的标签页处在标签页栏第一个标签页之前，则标签页栏右移至所点击标签页出现

                scrollIndex = selectedIndex;
                lastOneIndex = scrollIndex + showCount - 1;
                if (lastOneIndex > dbManipulationPaneList.size() - 1) {
                    lastOneIndex = dbManipulationPaneList.size() - 1;
                }
            } else {
                if (selectedIndex >= dbManipulationPaneList.size() - 1) {
                    selectedIndex = dbManipulationPaneList.size() - 1;
                    lastOneIndex = selectedIndex;
                    scrollIndex = selectedIndex - showCount + 1;
                } else {
                    lastOneIndex = scrollIndex + showCount - 1;
                    if (lastOneIndex > dbManipulationPaneList.size() - 1) {
                        lastOneIndex = dbManipulationPaneList.size() - 1;
                    }
                }
            }
        } else {
            scrollIndex = 0;
            lastOneIndex = dbManipulationPaneList.size() - 1;
        }
    }


    /**
     * 画选中的tab
     *
     * @param g2d
     * @param sheetName
     * @return
     */
    private int paintSelectedTab(Graphics2D g2d,int startX, String sheetName, int selfIndex) {
        double[] x = {startX, startX, startX + tabWidth, startX + tabWidth, startX};
        double[] y = {-1, getHeight(), getHeight(), -1, -1};
        RoundRectangle2D.Double rect1 = new RoundRectangle2D.Double(startX, 1, this.getWidth(), this.getHeight(), 7, 7);
        g2d.setPaint(new GradientPaint(1, 1, new Color(255, 255, 255), 1, getHeight() - 1f, UIConstants.NORMAL_BACKGROUND));

        GeneralPath generalPath = new GeneralPath(Path2D.WIND_EVEN_ODD, x.length);
        generalPath.moveTo((float) x[0] + CORNOR_RADIUS, (float) y[0]);
        generalPath.curveTo(((float) x[0] + CORNOR_RADIUS - specialLocation1), (y[0] + CORNOR_RADIUS - specialLocation2), ((float) x[0] + CORNOR_RADIUS - specialLocation2), (y[0] + CORNOR_RADIUS - specialLocation1), (double) x[0], y[0] + CORNOR_RADIUS);

        for (int index = 1; index <= 2; index++) {
            generalPath.lineTo((float) x[index], (float) y[index]);
        }

        generalPath.lineTo((float) x[3], (float) y[3] + CORNOR_RADIUS);
        generalPath.curveTo(((float) x[3] - CORNOR_RADIUS + specialLocation1), ((float) y[3] + CORNOR_RADIUS - specialLocation2), ((float) x[3] - CORNOR_RADIUS + specialLocation2), ((float) y[3] + CORNOR_RADIUS - specialLocation1), (float) x[3] - CORNOR_RADIUS, (float) y[3]);
        generalPath.lineTo((float) x[0] + CORNOR_RADIUS, (float) y[0]);

        generalPath.closePath();
        g2d.fill(generalPath);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(UIConstants.LINE_COLOR);
        g2d.draw(new Arc2D.Double(x[0], y[0], CORNOR_RADIUS * 2, CORNOR_RADIUS * 2, 90, 90, 0));
        g2d.draw(new Line2D.Double(x[0], y[0] + CORNOR_RADIUS, x[1], y[1]));
        g2d.draw(new Line2D.Double(x[1], y[1], x[2], y[2]));
        g2d.draw(new Line2D.Double(x[2], y[2], x[3], y[3] + CORNOR_RADIUS));
        g2d.draw(new Line2D.Double(x[0] + 3 ,0,x[2] - 3,0));
        g2d.draw(new Arc2D.Double(x[3] - CORNOR_RADIUS * 2, y[3], CORNOR_RADIUS * 2, CORNOR_RADIUS * 2, 90, -90, 0));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        // 画字符
        g2d.setPaint(getForeground());
        g2d.drawString(sheetName,  startX  + 2 * GAP, getHeight()-GAP);
        int closePosition =  startX + tabWidth - closeIcon.getIconWidth() - SMALLGAP;
        int closeY = (getHeight() - closeIcon.getIconHeight()) / 2;
        if (canClose() && mouseOveredIndex == selfIndex){
            closeIcon.paintIcon(this, g2d, closePosition, closeY);
        }
        return closePosition;

    }

    /**
     * 画没有选中的tab
     *
     * @param g2d
     * @param startX
     * @param sheetName
     * @return
     */
    private int paintUnSelectedTab(Graphics2D g2d,  int startX, String sheetName, int selfIndex) {
        double[] x = {startX, startX, startX + tabWidth, startX + tabWidth, startX};
        double[] y = {-1, getHeight() - 1, getHeight() - 1, -1, -1};
        if (selfIndex == mouseOveredIndex) {
            g2d.setPaint(new GradientPaint(1, 1, new Color(255, 255, 255), 1, getHeight() - 1f, UIConstants.NORMAL_BACKGROUND));
        } else {
            g2d.setPaint(new GradientPaint(1, 1, UIConstants.NORMAL_BACKGROUND, 1, getHeight() - 1f, UIConstants.NORMAL_BACKGROUND));
        }


        GeneralPath generalPath = new GeneralPath(Path2D.WIND_EVEN_ODD, x.length);
        generalPath.moveTo((float) x[0] + CORNOR_RADIUS, (float) y[0]);
        generalPath.curveTo(((float) x[0] + CORNOR_RADIUS - specialLocation1), (y[0] + CORNOR_RADIUS - specialLocation2), ((float) x[0] + CORNOR_RADIUS - specialLocation2), (y[0] + CORNOR_RADIUS - specialLocation1), (double) x[0], y[0] + CORNOR_RADIUS);

        for (int index = 1; index <= 2; index++) {
            generalPath.lineTo((float) x[index], (float) y[index]);
        }

        generalPath.lineTo((float) x[3], (float) y[3] + CORNOR_RADIUS);
        generalPath.curveTo(((float) x[3] - CORNOR_RADIUS + specialLocation1), ((float) y[3] + CORNOR_RADIUS - specialLocation2), ((float) x[3] - CORNOR_RADIUS + specialLocation2), ((float) y[3] + CORNOR_RADIUS - specialLocation1), (float) x[3] - CORNOR_RADIUS, (float) y[3]);
        generalPath.lineTo((float) x[0] + CORNOR_RADIUS, (float) y[0]);

        generalPath.closePath();

        g2d.fill(generalPath);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(UIConstants.LINE_COLOR);

        g2d.draw(new Arc2D.Double(x[0], y[0], CORNOR_RADIUS * 2, CORNOR_RADIUS * 2, 90, 90, 0));
        g2d.draw(new Line2D.Double(x[0], y[0] + CORNOR_RADIUS, x[1], y[1]));
        g2d.draw(new Line2D.Double(x[1], y[1], x[2], y[2]));
        g2d.draw(new Line2D.Double(x[2], y[2], x[3], y[3] + CORNOR_RADIUS));
        g2d.draw(new Line2D.Double(x[0] + 3 ,0,x[2] - 3,0));
        g2d.draw(new Arc2D.Double(x[3] - CORNOR_RADIUS * 2, y[3], CORNOR_RADIUS * 2, CORNOR_RADIUS * 2, 90, -90, 0));

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        // 画字符
        g2d.setPaint(getForeground());
        g2d.drawString(sheetName,  startX  + 2 * GAP, getHeight() - GAP );
        int closeY = (getHeight() - closeIcon.getIconHeight()) / 2;
        int closePosition =  startX + tabWidth - closeIcon.getIconWidth() - SMALLGAP;
        if (canClose()  && mouseOveredIndex == selfIndex){
            closeIcon.paintIcon(this, g2d, closePosition, closeY);
        }
        return closePosition;
    }


    /**
     * 鼠标按下
     * @param e  事件
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * 鼠标按下
     * @param e 事件
     */
    public void mousePressed(MouseEvent e) {
        int x = e.getX(), y = e.getY();
        if (addX!= -1 && isOverAddIcon(x, y)){
            addIcon = ADD_CLICK;
            commit2DBJavaScriptPane.createDBManipulationPane();
            selectedIndex = dbManipulationPaneList.size()-1;
            commit2DBJavaScriptPane.updateCardPane();
        } else if (isOverCloseIcon(x)){
            int re = FineJOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_CommitTab_Sure_To_Delete")+ "?", com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_CommitTab_Remove")
                    , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (re == JOptionPane.OK_OPTION) {
                dbManipulationPaneList.remove(getTabIndex(x));
                commit2DBJavaScriptPane.setList(dbManipulationPaneList);
                // 删除tab以后，获得第一个tab，再刷新一下，否则画面会停留在已删除的tab上，第一个tab是不可删除的
                selectedIndex = getTabIndex(FIRST_TAB_POSITION);
                commit2DBJavaScriptPane.updateCardPane();
                
            }
        } else if (selectedIndex != getTabIndex(x)){
            selectedIndex = getTabIndex(x);
            commit2DBJavaScriptPane.updateCardPane();
        }
        repaint();
    }

    /**
     * 鼠标离开
     * @param e   事件
     */
    public void mouseReleased(MouseEvent e) {
        if(addX != -1 && isOverAddIcon(e.getX(), e.getY())){
            addIcon = ADD_NORMAL;
        }
    }

    /**
     * 鼠标进入
     * @param e   事件
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * 鼠标离开
     * @param e   事件
     */
    public void mouseExited(MouseEvent e) {
        mouseOveredIndex = -1;
        repaint();
    }

    /**
     * 鼠标拖动
     * @param e   事件
     */
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * 鼠标移动
     * @param e   事件
     */
    public void mouseMoved(MouseEvent e) {
        if(addX!= -1 && isOverAddIcon(e.getX(), e.getY())){
            addIcon = ADD_OVER;
        }  else {
            mouseOveredIndex = getTabIndex(e.getX());
            addIcon = ADD_NORMAL;
        }
        repaint();
    }


    /**
     *  判断鼠标所在Tab
     * @param   evtX
     * @return    index
     */
    private int getTabIndex ( int evtX ){
        int x = 0;
        for( int i = scrollIndex;i <= lastOneIndex;i++){
            if(evtX > x && evtX <= x + tabWidth ){
                return i;
            }
            x = x + tabWidth;
        }
        return -1;
    }

    /**
     * 判断鼠标是否在添加按钮上
     * @param x 鼠标坐标x
     * @param y 鼠标坐标y
     * @return 返回鼠标是否在添加按钮上
     */
    private boolean isOverAddIcon(int x, int y){
        int addWidth = addIcon.getIconWidth(),addHeight = addIcon.getIconHeight();
        return x >= addX && x <= addX + addWidth && y > addY && y <= addY + addHeight;
    }


    /**
     * 判断鼠标是否在关闭按钮上
     * @param evtX      x
     * @return 返回鼠标是否在关闭按钮上
     */
    private boolean isOverCloseIcon(int evtX) {
        boolean isOverCloseIcon = false;
        if( canClose()){
        for (int i = 0; i < closeIconStartX.length; i++) {
            if (evtX >= closeIconStartX[i] && evtX <= closeIconStartX[i] + closeIcon.getIconWidth()) {
                isOverCloseIcon = true;
                break;
            }
        }
        }
        return isOverCloseIcon;
    }

    /**
     * 如果tab只剩下最后一个，则不画删除按钮
     * @return 返回当前tab还可否关闭
     */
    private boolean canClose(){
        return closeIconStartX.length > 1;
    }

    /**
     * 刷新tab，停留在第一个tab上面
     */
    public void refreshTab(){
        selectedIndex = getTabIndex(FIRST_TAB_POSITION);
        commit2DBJavaScriptPane.updateCardPane();
    }

}