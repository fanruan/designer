package com.fr.design.file;


import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIScrollPopUpMenu;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JVirtualTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.utils.gui.GUIPaintUtils;
import com.fr.file.FILE;
import com.fr.file.FileNodeFILE;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.ProductConstants;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.util.logging.Level;

/**
 * Author : daisy
 * Date: 13-8-5
 * Time: 下午6:12
 */
public class MutilTempalteTabPane extends JComponent implements MouseListener, MouseMotionListener, Action {
    private static Icon LIST_DOWN = BaseUtils.readIcon("/com/fr/design/images/buttonicon/list_normal.png");
    private static Icon MOUSE_OVER_LIST_DOWN = BaseUtils.readIcon("/com/fr/design/images/buttonicon/list_pressed.png");
    private static Icon MOUSE_PRESS_LIST_DOWN = BaseUtils.readIcon("/com/fr/design/images/buttonicon/list_pressed.png");
    private static Icon CLOSE = BaseUtils.readIcon("/com/fr/design/images/buttonicon/close_icon.png");
    private static Icon MOUSE_OVER_CLOSE = BaseUtils.readIcon("/com/fr/design/images/buttonicon/mouseoverclose icon.png");
    private static Icon MOUSE_PRESS_CLOSE = BaseUtils.readIcon("/com/fr/design/images/buttonicon/pressclose icon.png");
    private static final String ELLIPSIS = "...";
    private static final int GAP_BEFORE_CLOSE = 10;
    private static final int GAP = 5;
    private static final int SMALLGAP = 3;
    private static final int LIST_BUTTON_WIDTH = 34;
    private static final int HEIGHT = 26;
    private static final int LIST_DOWN_HEIGHT = 25;
    private static final double FOR_CAL_BYTES = 0.5;
    private static final double CORNOR_RADIUS = 0.0;
    //选了30度和60度的特殊角度的x,y作为经过的两个点的坐标
    private static final double SPECIAL_LOCATION_1 = 2.5;
    private static final double SPECIAL_LOCATION_2 = 4.330127;
    private static final int ICON_WIDTH = 22;


    //每个标签页的最大的长度和最小长度。这些长度均为均分

    private static final int MAXWIDTH = 240;
    private static final int MINWIDTH = 100;


    private static MutilTempalteTabPane THIS;
    //用于存放工作簿
    private java.util.List<JTemplate<?, ?>> openedTemplate;
    //选中的Tab项
    private int selectedIndex = 0;
    //
    private int mouseOveredIndex = -1;

    //tab栏可以放下的每个tab的实际宽度
    private int realWidth = MAXWIDTH;


    //当前标签页栏存放的所有标签页的index
    private int minPaintIndex = 0;
    private int maxPaintIndex = 0;

    /**
     * 鼠标按下时的坐标数组、鼠标放开时的坐标数组
     */
    private int[] xyPressedCoordinate = {0, 0};


    //每个关闭图标的起始位置
    private int[] startX;

    private boolean[] isNeedToolTips;

    private Graphics2D g2d;

    //记录关闭按钮的状态
    private int closeIconIndex = -1;
    private boolean isCloseCurrent = false;
    private Icon clodeMode = CLOSE;
    private Icon listDownMode = LIST_DOWN;
    private boolean isShowList = false;

    //自动新建的模板B若没有进行任何编辑，切换到其他
    //
    // 模板时，模板B会自动关闭
    private JTemplate<?, ?> temTemplate = null;


    private AWTEventListener awt = new AWTEventListener() {
        public void eventDispatched(AWTEvent event) {
            if (event instanceof MouseEvent) {
                MouseEvent mv = (MouseEvent) event;
                if (mv.getClickCount() > 0 && !ComparatorUtils.equals(mv.getSource(), MutilTempalteTabPane.this)) {
                    isShowList = false;
                }
            }
        }

    };


    public static final MutilTempalteTabPane getInstance() {
        if (THIS == null) {
            THIS = new MutilTempalteTabPane();
        }
        return THIS;
    }

    public JTemplate getSelectedFile() {
        return openedTemplate.get(selectedIndex);
    }


    /**
     * 关闭掉当前已打开文件列表中指定的文件
     *
     * @param file 指定的文件
     */
    public void closeFileTemplate(FILE file) {
        for (JTemplate<?, ?> temp : openedTemplate) {
            if (ComparatorUtils.equals(file, temp.getEditingFILE())) {
                closeSpecifiedTemplate(temp);
                break;
            }
        }

    }

    public Dimension getPreferredSize() {
        Dimension dimension = super.getPreferredSize();
        dimension.height = HEIGHT;
        return dimension;
    }

    /**
     * 多工作簿面板
     */
    public MutilTempalteTabPane() {
        this.setLayout(new BorderLayout(0, 0));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setBorder(null);
        this.setForeground(new Color(58, 56, 58));
        this.setFont(new Font(Inter.getLocText("FR-Designer_Song_TypeFace"), 0, 12));
        openedTemplate = HistoryTemplateListPane.getInstance().getHistoryList();
        selectedIndex = openedTemplate.size() - 1;
        Toolkit.getDefaultToolkit().addAWTEventListener(awt, AWTEvent.MOUSE_EVENT_MASK);
    }

    private UIMenuItem initCloseOther() {
        UIMenuItem closeOther = new UIMenuItem(Inter.getLocText("FR-Designer_FS_Close_Other_Templates"));
        setListDownItemPreferredSize(closeOther);
        closeOther.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (openedTemplate.size() == 1) {
                    return;
                }
                SaveSomeTemplatePane saveSomeTempaltePane = new SaveSomeTemplatePane(false);
                //点击关闭其他模板，并且点击确定保存
                if (saveSomeTempaltePane.showSavePane()) {
                    JTemplate<?, ?>[] panes = new JTemplate<?, ?>[openedTemplate.size()];
                    for (int i = 0; i < openedTemplate.size(); i++) {
                        panes[i] = openedTemplate.get(i);
                    }
                    for (int i = 0; i < panes.length; i++) {
                        if (i != selectedIndex) {
                            //判断关闭的模板是不是格式刷的被参照的模板
                            openedTemplate.remove(panes[i]);
                            closeFormat(panes[i]);
                            HistoryTemplateListPane.getInstance().closeSelectedReport(panes[i]);
                        }
                    }
                    JTemplate<?, ?> currentTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                    HistoryTemplateListPane.getInstance().removeAllHistory();
                    DesignerContext.getDesignerFrame().activateJTemplate(currentTemplate);
                    THIS.repaint();
                }
                //如果取消保存了，则不关闭其他模板


            }
        });
        if (openedTemplate.size() == 1) {
            closeOther.setEnabled(false);
        }

        return closeOther;
    }


    private UIMenuItem[] createListDownTemplate() {
        UIMenuItem[] templates = new UIMenuItem[openedTemplate.size()];
        for (int i = 0; i < openedTemplate.size(); i++) {
            final int index = i;
            final JTemplate tem = openedTemplate.get(i);
            templates[i] = new UIMenuItem(tempalteShowName(tem), tem.getIcon());
            templates[i].setUI(new UIListDownItemUI());
            setListDownItemPreferredSize(templates[i]);
            if (i == selectedIndex) {
                //画选中的高亮
                templates[i].setBackground(UIConstants.SHADOW_CENTER);
            }
            templates[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectedIndex = index;
                    tem.activeNewJTemplate();
                }
            });
        }
        return templates;
    }

    private void setListDownItemPreferredSize(UIMenuItem item) {
        Dimension dimension = item.getPreferredSize();
        dimension.height = LIST_DOWN_HEIGHT;
        item.setPreferredSize(dimension);
    }


    private String tempalteShowName(JTemplate<?, ?> template) {
        String name = template.getEditingFILE().getName();
        if (!template.isSaved() && !name.endsWith(" *")) {
            name += " *";
        }
        return name;
    }

    /**
     * 刷新打开模板
     *
     * @param history 模板
     */
    public void refreshOpenedTemplate(java.util.List<JTemplate<?, ?>> history) {
        openedTemplate = history;
    }

    public void setTemTemplate(JTemplate<?, ?> auotCreate) {
        temTemplate = auotCreate;
    }


    private void showListDown() {

        UIScrollPopUpMenu menu = new UIScrollPopUpMenu();
        menu.setBorder(BorderFactory.createEmptyBorder(-3, 3, 3, 0));
        menu.add(initCloseOther());
        JSeparator separator = new JSeparator() {
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 1;
                return d;
            }
        };
        menu.add(new JPanel() {
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 1;
                return d;
            }
        });
        separator.setForeground(UIConstants.LINE_COLOR);
        menu.add(separator);
        menu.add(new JPanel() {
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 1;
                return d;
            }
        });
        UIMenuItem[] items = createListDownTemplate();
        for (int i = 0; i < items.length; i++) {
            menu.add(items[i]);
        }
        GUICoreUtils.showPopupMenu(menu, MutilTempalteTabPane.getInstance(), MutilTempalteTabPane.getInstance().getWidth() - menu.getPreferredSize().width, getY() - 1 + getHeight());
    }


    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double maxWidth = getWidth() - LIST_BUTTON_WIDTH; //最大宽度
        g2d = (Graphics2D) g;
        paintBackgroundAndLine(g2d, maxWidth);
    }


    private void paintBackgroundAndLine(Graphics2D g2d, double maxWidth) {
        paintDefaultBackground(g2d);
        //最多能画的个数
        int maxTemplateNum = (int) (maxWidth) / MINWIDTH;
        //计算开始画的最小模板index和最大模板index
        calMinAndMaxIndex(maxTemplateNum);
        calculateRealAverageWidth(maxWidth, maxTemplateNum);
        int maxStringlength = calculateStringMaxLength();
        if (selectedIndex >= openedTemplate.size()) {
            selectedIndex = openedTemplate.size() - 1;
        }
        if (selectedIndex < 0) {
            selectedIndex = 0;
        }
        double templateStartX = 0;
        startX = new int[maxPaintIndex - minPaintIndex + 1];
        isNeedToolTips = new boolean[maxPaintIndex - minPaintIndex + 1];

        //从可以开始展示在tab面板上的tab开始画
        for (int i = minPaintIndex; i <= maxPaintIndex; i++) {
            JTemplate template = openedTemplate.get(i);
            Icon icon = template.getIcon();
            String name = tempalteShowName(template);
            //如果tab名字的长度大于最大能显示的英文字符长度，则进行省略号处理
            if (getStringWidth(name) > maxStringlength) {
                name = getEllipsisName(name, maxStringlength);
                isNeedToolTips[i - minPaintIndex] = true;
            } else {
                isNeedToolTips[i - minPaintIndex] = false;
            }

            Icon selectedIcon = null;
            if (i == closeIconIndex) {
                selectedIcon = clodeMode;
            } else {
                selectedIcon = CLOSE;
            }
            if (i == selectedIndex) {
                startX[i - minPaintIndex] = paintSelectedTab(g2d, icon, templateStartX, name, selectedIcon);
            } else {
                boolean isLeft = i < selectedIndex;
                startX[i - minPaintIndex] = paintUnSelectedTab(g2d, icon, templateStartX, name, selectedIcon, isLeft, mouseOveredIndex, i);
            }
            templateStartX += realWidth;
        }

        paintListDown(g2d, maxWidth);
        paintUnderLine(templateStartX, maxWidth, g2d);
    }


    private void paintUnderLine(double templateStartX, double maxWidth, Graphics2D g2d) {
        //画下面的那条线
        if (templateStartX < maxWidth) {
            GeneralPath generalPath = new GeneralPath(Path2D.WIND_EVEN_ODD, 2);
            generalPath.moveTo((float) templateStartX, getHeight() - 1);
            generalPath.lineTo((float) maxWidth, getHeight() - 1);
            g2d.fill(generalPath);
            //TODO hzzz delete
//            g2d.setPaint(UIConstants.LINE_COLOR);
//            g2d.draw(new Line2D.Double((float) templateStartX, getHeight() - 1, (float) maxWidth + LIST_BUTTON_WIDTH, getHeight() - 1));
        }
    }

    private void paintDefaultBackground(Graphics2D g2d) {
        //画默认背景
        g2d.setPaint(new GradientPaint(1, 1, UIConstants.TEMPLATE_TAB_PANE_BACKGROUND, 1, getHeight() - 1, UIConstants.TEMPLATE_TAB_PANE_BACKGROUND));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }


    private void paintListDown(Graphics2D g2d, double maxWidth) {
        int x = (int) maxWidth + (LIST_BUTTON_WIDTH - listDownMode.getIconWidth()) / 2;
        int y = (getHeight() - listDownMode.getIconHeight()) / 2;
        listDownMode.paintIcon(this, g2d, x, y);
    }

    /**
     * 判断tab文字的长度大于能装下的最大文字长度，要用省略号
     *
     * @param name
     * @param maxStringlength
     * @return
     */
    private String getEllipsisName(String name, int maxStringlength) {

        //若是名字长度大于能显示的长度，那能显示的文字的最大长度还要减去省略号的最大长度
//        int maxellipsislength = maxStringlength - ELLIPSIS.length();
        int ellipsisWidth = getStringWidth(ELLIPSIS);
        int leftkeyPoint = 0;
        int rightKeyPoint = name.length() - 1;
        int leftStrWidth = 0;
        int rightStrWidth = 0;
        while (leftStrWidth + rightStrWidth + ellipsisWidth < maxStringlength) {
            if (leftStrWidth <= rightStrWidth) {
                leftkeyPoint++;
            } else {
                rightKeyPoint--;
            }
            leftStrWidth = getStringWidth(name.substring(0, leftkeyPoint));
            rightStrWidth = getStringWidth(name.substring(rightKeyPoint));

            if (leftStrWidth + rightStrWidth + ellipsisWidth > maxStringlength) {
                if (leftStrWidth <= rightStrWidth) {
                    rightKeyPoint++;
                }
                break;
            }
        }

        return name.substring(0, leftkeyPoint) + ELLIPSIS + name.substring(rightKeyPoint);
    }

    private void calMinAndMaxIndex(int maxTemplateNum) {
        //如果个数大于最多能容纳的个数，则多余的进行处理
        if (openedTemplate.size() > maxTemplateNum) {
            //所点击列表中的标签页处在标签页栏最后一个标签页之后，则标签页栏左移至所点击标签页出现
            if (selectedIndex >= maxPaintIndex) {
                minPaintIndex = selectedIndex - maxTemplateNum + 1;
                maxPaintIndex = selectedIndex;
                if (minPaintIndex <= 0) {
                    minPaintIndex = 0;
                    maxPaintIndex = maxTemplateNum - 1;
                }
            } else if (selectedIndex <= minPaintIndex) {
                //所点击列表中的标签页处在标签页栏第一个标签页之前，则标签页栏右移至所点击标签页出现
                minPaintIndex = selectedIndex;
                maxPaintIndex = minPaintIndex + maxTemplateNum - 1;
                if (maxPaintIndex > openedTemplate.size() - 1) {
                    maxPaintIndex = openedTemplate.size() - 1;
                }
            } else {
                if (selectedIndex >= openedTemplate.size() - 1) {
                    selectedIndex = openedTemplate.size() - 1;
                    maxPaintIndex = selectedIndex;
                    minPaintIndex = selectedIndex - maxTemplateNum + 1;
                } else {
                    maxPaintIndex = minPaintIndex + maxTemplateNum - 1;
                    if (maxPaintIndex > openedTemplate.size() - 1) {
                        maxPaintIndex = openedTemplate.size() - 1;
                    }
                }
            }
        } else {
            minPaintIndex = 0;
            maxPaintIndex = openedTemplate.size() - 1;
        }
    }


    //个数小于最多能容纳的个数的情况下，看看宽度每个要画多少
    private void calculateRealAverageWidth(double maxwidth, int templateNum) {

        int num = openedTemplate.size() > templateNum ? templateNum : openedTemplate.size();
        realWidth = (int) (maxwidth / (num));
        if (realWidth > MAXWIDTH) {
            realWidth = MAXWIDTH;
        } else if (realWidth < MINWIDTH) {
            //平均下来每个的宽度小于最小宽度
            realWidth = MINWIDTH;
        }
    }

    /**
     * 计算过长度之后的每个tab的能接受的文字的英文字符数
     *
     * @return
     */
    private int calculateStringMaxLength() {
        return realWidth - 3 * GAP - ICON_WIDTH - SMALLGAP - CLOSE.getIconWidth();

    }

    private int getStringWidth(String str) {
        return GraphHelper.getFontMetrics(this.getFont()).stringWidth(str);
    }


    /**
     * 画选中的tab
     *
     * @param g2d
     * @param sheeticon
     * @param templateStartX
     * @param sheetName
     * @param closeIcon
     * @return
     */
    private int paintSelectedTab(Graphics2D g2d, Icon sheeticon, double templateStartX, String sheetName, Icon closeIcon) {
        double[] x = {templateStartX, templateStartX, templateStartX + realWidth, templateStartX + realWidth, templateStartX};
        double[] y = {1, getHeight() + 1, getHeight() + 1, 1, 1};
        RoundRectangle2D.Double rect1 = new RoundRectangle2D.Double(templateStartX, 1, this.getWidth(), this.getHeight(), 7, 7);
        g2d.setPaint(new GradientPaint(1, 1, UIConstants.SELECT_TAB, 1, getHeight() - 1, UIConstants.SELECT_TAB));
        //选了30度和60度的特殊角度的x,y作为经过的两个点的坐标
        double specialLocation1 = 2.5;
        double specialLocation2 = 4.330127;
        GeneralPath generalPath = new GeneralPath(Path2D.WIND_EVEN_ODD, x.length);
        generalPath.moveTo((float) x[0] + CORNOR_RADIUS, (float) y[0]);
        generalPath.curveTo(((float) x[0] + CORNOR_RADIUS - specialLocation1), (y[0] + CORNOR_RADIUS - specialLocation2), ((float) x[0] + CORNOR_RADIUS - specialLocation2), (y[0] + CORNOR_RADIUS - specialLocation1), x[0], y[0] + CORNOR_RADIUS);

        for (int index = 1; index <= 2; index++) {
            generalPath.lineTo((float) x[index], (float) y[index]);
        }

        generalPath.lineTo((float) x[3], (float) y[3] + CORNOR_RADIUS);
        generalPath.curveTo(((float) x[3] - CORNOR_RADIUS + specialLocation1), ((float) y[3] + CORNOR_RADIUS - specialLocation2), ((float) x[3] - CORNOR_RADIUS + specialLocation2), ((float) y[3] + CORNOR_RADIUS - specialLocation1), (float) x[3] - CORNOR_RADIUS, (float) y[3]);
        generalPath.lineTo((float) x[0] + CORNOR_RADIUS, (float) y[0]);

        generalPath.closePath();
        g2d.fill(generalPath);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(new Color(200, 201, 205));
        g2d.draw(new Arc2D.Double(x[0], y[0], CORNOR_RADIUS * 2, CORNOR_RADIUS * 2, 90, 90, 0));
        g2d.draw(new Line2D.Double(x[0], y[0] + CORNOR_RADIUS, x[1], y[1]));
        g2d.draw(new Line2D.Double(x[1], y[1], x[2], y[2]));
        g2d.draw(new Line2D.Double(x[2], y[2], x[3], y[3] + CORNOR_RADIUS));
        g2d.draw(new Arc2D.Double(x[3] - CORNOR_RADIUS * 2, y[3], CORNOR_RADIUS * 2, CORNOR_RADIUS * 2, 90, -90, 0));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        int sheetIconY = (getHeight() - sheeticon.getIconHeight()) / 2;
        sheeticon.paintIcon(this, g2d, (int) templateStartX + GAP, sheetIconY);
        // 画字符
        g2d.setPaint(getForeground());
        g2d.drawString(sheetName, (int) templateStartX + sheeticon.getIconWidth() + 2 * GAP, getHeight() - GAP * 2);
        int closePosition = (int) templateStartX + realWidth - CLOSE.getIconWidth() - SMALLGAP;
        int closeY = (getHeight() - closeIcon.getIconHeight()) / 2;
        closeIcon.paintIcon(this, g2d, closePosition, closeY);
        return closePosition;

    }

    /**
     * 画没有选中的tab
     *
     * @param g2d
     * @param sheeticon
     * @param templateStartX
     * @param sheetName
     * @param closeIcon
     * @param isLeft
     * @return
     */
    private int paintUnSelectedTab(Graphics2D g2d, Icon sheeticon, double templateStartX, String sheetName, Icon closeIcon, boolean isLeft, int mouseOveredIndex, int selfIndex) {
        double[] x = {templateStartX, templateStartX, templateStartX + realWidth, templateStartX + realWidth, templateStartX};
        double[] y = {-1, getHeight() - 1, getHeight() - 1, -1, -1};
        if (selfIndex == mouseOveredIndex) {
            g2d.setPaint(new GradientPaint(1, 1, UIConstants.HOVER_BLUE, 1, getHeight() - 1, UIConstants.HOVER_BLUE));
        } else {
            g2d.setPaint(new GradientPaint(1, 1, UIConstants.SHADOW_GREY, 1, getHeight() - 1, UIConstants.SHADOW_GREY));
        }


        GeneralPath generalPath = new GeneralPath(Path2D.WIND_EVEN_ODD, x.length);

        unSelectedClosedPath(generalPath, isLeft, x, y);
        g2d.fill(generalPath);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(UIConstants.TEMPLATE_TAB_PANE_BACKGROUND);
        //TODO hzzz delete
//        if (isLeft) {
//            g2d.draw(new Arc2D.Double(x[0], y[0], CORNOR_RADIUS * 2, CORNOR_RADIUS * 2, 90, 90, 0));
//        } else {
//            g2d.draw(new Arc2D.Double(x[0] - CORNOR_RADIUS * 2, y[0], CORNOR_RADIUS * 2, CORNOR_RADIUS * 2, 90, -90, 0));
//        }

//        g2d.draw(new Line2D.Double(x[0], y[0] + CORNOR_RADIUS, x[1], y[1] + 1));
//        g2d.draw(new Line2D.Double(x[1], y[1], x[2], y[2]));
        g2d.draw(new Line2D.Double(x[2], y[2], x[3], y[3] + CORNOR_RADIUS));
//        if (isLeft) {
//            g2d.draw(new Arc2D.Double(x[3], y[3], CORNOR_RADIUS * 2, CORNOR_RADIUS * 2, 90, 90, 0));
//        } else {
//            g2d.draw(new Arc2D.Double(x[3] - CORNOR_RADIUS * 2, y[3], CORNOR_RADIUS * 2, CORNOR_RADIUS * 2, 90, -90, 0));
//        }

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        int sheetIconY = (getHeight() - sheeticon.getIconHeight()) / 2;
        sheeticon.paintIcon(this, g2d, (int) templateStartX + GAP, sheetIconY);
        // 画字符
        g2d.setPaint(getForeground());
        g2d.drawString(sheetName, (int) templateStartX + sheeticon.getIconWidth() + 2 * GAP, getHeight() - GAP * 2);
        int closeY = (getHeight() - closeIcon.getIconHeight()) / 2;
        int closePosition = (int) templateStartX + realWidth - CLOSE.getIconWidth() - SMALLGAP;
        closeIcon.paintIcon(this, g2d, closePosition, closeY);
        return closePosition;
    }


    private void unSelectedClosedPath(GeneralPath generalPath, boolean isLeft, double[] x, double[] y) {

        if (isLeft) {
            generalPath.moveTo((float) x[0] + CORNOR_RADIUS, (float) y[0]);
            generalPath.curveTo(((float) x[0] + CORNOR_RADIUS - SPECIAL_LOCATION_1), (y[0] + CORNOR_RADIUS - SPECIAL_LOCATION_2), ((float) x[0] + CORNOR_RADIUS - SPECIAL_LOCATION_2), (y[0] + CORNOR_RADIUS - SPECIAL_LOCATION_1), x[0], y[0] + CORNOR_RADIUS);
        } else {
            generalPath.moveTo((float) x[0] - CORNOR_RADIUS, (float) y[0]);
            generalPath.curveTo(((float) x[0] - CORNOR_RADIUS + SPECIAL_LOCATION_1), (y[0] + CORNOR_RADIUS - SPECIAL_LOCATION_2), ((float) x[0] - CORNOR_RADIUS + SPECIAL_LOCATION_2), (y[0] + CORNOR_RADIUS - SPECIAL_LOCATION_1), x[0], y[0] + CORNOR_RADIUS);
        }

        for (int index = 1; index <= 2; index++) {
            generalPath.lineTo((float) x[index], (float) y[index]);
        }

        generalPath.lineTo((float) x[3], (float) y[3] + CORNOR_RADIUS);

        if (isLeft) {
            generalPath.curveTo(((float) x[3] + CORNOR_RADIUS - SPECIAL_LOCATION_1), ((float) y[3] + CORNOR_RADIUS - SPECIAL_LOCATION_2), ((float) x[3] + CORNOR_RADIUS - SPECIAL_LOCATION_2), ((float) y[3] - CORNOR_RADIUS + SPECIAL_LOCATION_1), (float) x[3] + CORNOR_RADIUS, (float) y[3]);
            generalPath.lineTo((float) x[0] + CORNOR_RADIUS, (float) y[0]);
        } else {
            generalPath.curveTo(((float) x[3] - CORNOR_RADIUS + SPECIAL_LOCATION_1), ((float) y[3] + CORNOR_RADIUS - SPECIAL_LOCATION_2), ((float) x[3] - CORNOR_RADIUS + SPECIAL_LOCATION_2), ((float) y[3] + CORNOR_RADIUS - SPECIAL_LOCATION_1), (float) x[3] - CORNOR_RADIUS, (float) y[3]);
            generalPath.lineTo((float) x[0] - CORNOR_RADIUS, (float) y[0]);
        }

        generalPath.closePath();
    }

    /**
     * 点击
     *
     * @param e 鼠标事件
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * 按下
     *
     * @param e 鼠标事件
     */
    public void mousePressed(MouseEvent e) {

        int evtX = e.getX();
        int evtY = e.getY();
        this.setPressedXY(evtX, evtY);

        //是否点击关闭按钮 如果点击了关闭按钮，则将点击的模板关闭，不需要切换，如果没有点击关闭按钮，则切换到点击的模板处
        boolean isOverCloseIcon = isOverCloseIcon(evtX);
        if (isOverListDown(evtX)) {
            listDownMode = isOverListDown(evtX) ? MOUSE_PRESS_LIST_DOWN : LIST_DOWN;
            if (!isShowList) {
                showListDown();
            }
            isShowList = !isShowList;

        } else if (isOverCloseIcon) {
            //关闭按钮的图标变化
            closeIconIndex = getTemplateIndex(evtX);
            clodeMode = MOUSE_PRESS_CLOSE;
            //关闭close图标所在的模板{
            closeFormat(openedTemplate.get(closeIconIndex));
            closeSpecifiedTemplate(openedTemplate.get(closeIconIndex));
            DesignerContext.getDesignerFrame().getContentFrame().repaint();
            isShowList = false;
        } else {
            //没有点击关闭和ListDown按钮，则切换到点击的模板处
            closeIconIndex = -1;
            clodeMode = CLOSE;
            int tempSelectedIndex = selectedIndex;
            if (selectedIndex != getTemplateIndex(evtX) && getTemplateIndex(evtX) != -1) {
                openedTemplate.get(selectedIndex).stopEditing();
                selectedIndex = getTemplateIndex(evtX);
                //如果在权限编辑情况下，不允许切换到表单类型的工作簿
                if (BaseUtils.isAuthorityEditing() && !openedTemplate.get(selectedIndex).isJWorkBook()) {
                    DesignerContext.getDesignerFrame().addAndActivateJTemplate(openedTemplate.get(tempSelectedIndex));
                    JOptionPane.showMessageDialog(this, Inter.getLocText("FR-Designer_Form-AuthorityEdited_Cannot_be_Supported")
                            + "!", Inter.getLocText("FR-Designer_Alert"), JOptionPane.WARNING_MESSAGE);
                    this.repaint();
                    return;
                }
                JTemplate evtXTemplate = openedTemplate.get(getTemplateIndex(evtX));
                evtXTemplate.activeNewJTemplate();
            }
            isShowList = false;
        }
        this.repaint();


    }

    public void setIsCloseCurrent(boolean isCloseCurrent) {
        this.isCloseCurrent = isCloseCurrent;

    }

    /**
     * 关闭模板
     *
     * @param specifiedTemplate 模板
     */
    public void closeSpecifiedTemplate(JTemplate<?, ?> specifiedTemplate) {
        if (specifiedTemplate == null) {
            return;
        }

        //当前激活的模板
        String filename = openedTemplate.get(selectedIndex).getEditingFILE().getPath();
        if (filename.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            filename = ((FileNodeFILE) openedTemplate.get(selectedIndex).getEditingFILE()).getEnvPath() + File.separator + filename;
        }

        filename = filename.replaceAll("/", "\\\\");

        if (!specifiedTemplate.isALLSaved()) {
            specifiedTemplate.stopEditing();
            int returnVal = JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Utils-Would_you_like_to_save") + " \"" + specifiedTemplate.getEditingFILE() + "\" ?",
                    ProductConstants.PRODUCT_NAME, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (returnVal == JOptionPane.YES_OPTION && specifiedTemplate.saveTemplate()) {
                specifiedTemplate.saveTemplate();
                FRLogger.getLogger().log(Level.INFO, Inter.getLocText(new String[]{"Template", "already-saved"}, new String[]{specifiedTemplate.getEditingFILE().getName(), "."}));
            }
        }
        HistoryTemplateListPane.getInstance().closeSelectedReport(specifiedTemplate);
        activeTemplate(filename);
    }

    /**
     * 关闭模板
     *
     * @param closedTemplate 模板
     */
    public void closeFormat(JTemplate closedTemplate) {
        //表单不需要处理
        if (!closedTemplate.isJWorkBook()) {
            return;
        }

        if (DesignerContext.getFormatState() == DesignerContext.FORMAT_STATE_NULL) {
            return;
        }

        //是被参照的模板被关闭，则重置格式刷
        closedTemplate.doConditionCancelFormat();
    }

    /**
     * 关闭掉一个模板之后该激活的Tab
     *
     * @param fileName
     */
    private void activeTemplate(String fileName) {
        if (openedTemplate.isEmpty()) {
            //新建并激活模板
            DesignerContext.getDesignerFrame().addAndActivateJTemplate();
            selectedIndex = 0;
            //此时刚自动新建的模板在HistoryTemplateListPane的editingTemplate
            temTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();

        } else {
            //如果关闭的模板是当前选中的模板，则重新激活
            if (closeIconIndex == selectedIndex) {
                if (closeIconIndex == maxPaintIndex) {
                    selectedIndex--;
                }
            } else if (isCloseCurrent) {
                //不是通过关闭按钮，而是通过文件关闭菜单关闭的当前模板的，也重新激活
                if (selectedIndex > openedTemplate.size() - 1) {
                    selectedIndex -= 1;
                    if (selectedIndex < 0) {
                        selectedIndex = 0;
                    }
                    isCloseCurrent = false;
                }
            } else {
                //如果关闭的模板不是当前选中的模板，则激活的模板不变
                selectedIndex = HistoryTemplateListPane.getInstance().contains(fileName);
            }
            //如果是已后台关闭的模板，则重新打开文件
            openedTemplate.get(selectedIndex).activeOldJTemplate();
        }
    }

    /**
     * 鼠标释放
     *
     * @param e 鼠标事件
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * 鼠标进入
     *
     * @param e 鼠标事件
     */
    public void mouseEntered(MouseEvent e) {

    }


    private boolean isOverCloseIcon(int evtX) {
        boolean isOverCloseIcon = false;
        for (int i = 0; i < startX.length; i++) {
            if (evtX >= startX[i] && evtX <= startX[i] + CLOSE.getIconWidth()) {
                isOverCloseIcon = true;
                break;
            }
        }
        return isOverCloseIcon;
    }


    private boolean isOverListDown(int evtX) {
        int maxWidth = getWidth() - LIST_BUTTON_WIDTH;
        return evtX >= (maxWidth + SMALLGAP) && evtX <= (getWidth() - SMALLGAP);
    }


    private int getTemplateIndex(int evtX) {
        int textX = 0;
        for (int i = minPaintIndex; i <= maxPaintIndex; i++) {
            int textWidth = realWidth;
            if (evtX >= textX && evtX < textX + textWidth) {
                return i;
            }
            textX += textWidth;
        }
        return -1;
    }

    /**
     * 鼠标离开
     *
     * @param e 鼠标事件
     */
    public void mouseExited(MouseEvent e) {
        listDownMode = LIST_DOWN;
        closeIconIndex = -1;
        mouseOveredIndex = -1;
        this.repaint();
    }

    /**
     * 鼠标拖拽
     *
     * @param e 鼠标事件
     */
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * 鼠标移动
     *
     * @param e 鼠标事件
     */
    public void mouseMoved(MouseEvent e) {
        int evtX = e.getX();
        mouseOveredIndex = getTemplateIndex(evtX);

        //看是否需要显示toolTip
        if (mouseOveredIndex != -1 && isNeedToolTips[mouseOveredIndex - minPaintIndex]) {
            setToolTipText(openedTemplate.get(mouseOveredIndex).getEditingFILE().getName());
        } else {
            setToolTipText(null);
        }

        listDownMode = isOverListDown(evtX) ? MOUSE_OVER_LIST_DOWN : LIST_DOWN;

        boolean isOverCloseIcon = isOverCloseIcon(evtX);
        clodeMode = isOverCloseIcon ? MOUSE_OVER_CLOSE : CLOSE;
        closeIconIndex = isOverCloseIcon ? mouseOveredIndex : -1;
        this.repaint();
    }


    private void setPressedXY(int x, int y) {
        this.xyPressedCoordinate[0] = x;
        this.xyPressedCoordinate[1] = y;
    }

    public Object getValue(String key) {
        return null;
    }

    /**
     * 加入值
     *
     * @param key   键
     * @param value 值
     */
    public void putValue(String key, Object value) {
    }

    /**
     * 动作
     *
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * 处理自动新建的模板 在切换时的处理
     */
    public void doWithtemTemplate() {
        //temtemplate保存的一定是手动新建的没有编辑或是编辑了没有保存的模板
        //没有保存，说明有编辑；已经保存在磁盘里的文件，说明有过处理，并且已经保存，此时切换都不将其自动关闭
        if (temTemplate == null || temTemplate == HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()) {
            return;
        }

        if (!temTemplate.isSaved() || !temTemplate.getEditingFILE().isMemFile()) {
            temTemplate = null;
        }

        //自动新建的模板B若没有进行任何编辑(新建模板没有进行任何编辑时saved都是true):还没有存盘
        if (temTemplate != null && temTemplate.getEditingFILE().isMemFile() && temTemplate.isSaved()) {
            HistoryTemplateListPane.getInstance().closeSelectedReport(temTemplate);
            temTemplate = null;
            setSelectedIndex(HistoryTemplateListPane.getInstance().contains(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()));
        }
    }

    private class UIListDownItemUI extends BasicMenuItemUI {
        @Override
        protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
            if (menuItem.getIcon() == null) {
                super.paintBackground(g, menuItem, bgColor);
                return;
            }
            ButtonModel model = menuItem.getModel();
            Color oldColor = g.getColor();
            int menuWidth = menuItem.getWidth();
            int menuHeight = menuItem.getHeight();
            g.setColor(UIConstants.NORMAL_BACKGROUND);
            g.fillRect(0, 0, menuWidth, menuHeight);
            boolean itemIsSelected = menuItem instanceof JMenu && model.isSelected();
            if (menuItem.isOpaque()) {
                if (model.isArmed() || itemIsSelected) {
                    GUIPaintUtils.fillPaint((Graphics2D) g, GAP, 0, menuWidth - GAP, menuHeight, true, Constants.NULL, UIConstants.FLESH_BLUE, UIConstants.ARC);
                } else {
                    GUIPaintUtils.fillPaint((Graphics2D) g, GAP, 0, menuWidth - GAP, menuHeight, true, Constants.NULL, menuItem.getBackground(), UIConstants.ARC);
                }
                g.setColor(oldColor);
            } else if (model.isArmed() || itemIsSelected) {
                GUIPaintUtils.fillPaint((Graphics2D) g, GAP, 0, menuWidth - GAP, menuHeight, true, Constants.NULL, UIConstants.FLESH_BLUE, UIConstants.ARC);
                g.setColor(oldColor);
            }
        }
    }


}