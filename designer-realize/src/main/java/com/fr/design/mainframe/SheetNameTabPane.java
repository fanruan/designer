package com.fr.design.mainframe;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.actions.UpdateAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonUI;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.utils.gui.GUIPaintUtils;
import com.fr.general.ComparatorUtils;

import com.fr.general.IOUtils;
import com.fr.main.impl.WorkBook;
import com.fr.poly.PolyDesigner;
import com.fr.report.poly.PolyWorkSheet;
import com.fr.report.report.TemplateReport;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ProductConstants;

/**
 * NameTabPane of sheets
 *
 * @editor zhou
 * @since 2012-3-26下午1:45:53
 */
public class SheetNameTabPane extends JComponent implements MouseListener, MouseMotionListener {

    private static final Color LINE_COLOR = new Color(0xababab);

    private static final Icon ADD_WORK_SHEET = IOUtils.readIcon("com/fr/base/images/oem/addworksheet.png");
    protected static final Icon ADD_POLY_SHEET = IOUtils.readIcon("com/fr/design/images/sheet/addpolysheet.png");
    private static final Icon WORK_SHEET_ICON = IOUtils.readIcon("com/fr/base/images/oem/worksheet.png");
    private static final Icon POLY_SHEET_ICON = IOUtils.readIcon("com/fr/design/images/sheet/polysheet.png");
    private static final Icon LEFT_ICON = IOUtils.readIcon("com/fr/design/images/sheet/left_normal@1x.png");
    private static final Icon RIGHT_ICON = IOUtils.readIcon("com/fr/design/images/sheet/right_normal@1x.png");
    private static final Icon DISABLED_LEFT_ICON = IOUtils.readIcon("com/fr/design/images/sheet/left_hover@1x.png");
    private static final Icon DISABLED_RIGHT_ICON = IOUtils.readIcon("com/fr/design/images/sheet/right_hover@1x.png");
    private static final int NUM = 10;

    private static final int ICON_SEP_DISTANCE = 8;
    private static final int TOOLBAR_HEIGHT = 16;
    private static final int ADD_WIDTH_BY_SHEETNAME = 20; //sheet名字的文本到图标边框的距离
    private static final int GRID_TOSHEET_RIGHT = 20; // 添加grid按钮右侧距sheet面板右侧的距离
    private static final int POLY_TOSHEET_LEFT = 30; // 添加poly按钮左侧距sheet面板右侧的距离
    private static final int POLY_TOSHEET_RIGHT = 50; // 添加poly按钮右侧距sheet面板右侧的距离
    private static final int SHEET_ICON_GAP = 5; // 每个sheet图标之间的距离

    private static final int GRAP = 12; // 给两个添加按钮与其他组件预留的间隔
    private static final int LEFT_CORNOR = 0;// 左角落.
    private static final int RIGHT_CORNOR = 0;// 右角落

    /**
     * 左移和右移按钮
     */
    private UIButton leftButton;
    private UIButton rightButton;
    /**
     * 鼠标按下时的坐标数组、鼠标放开时的坐标数组
     */
    private int[] xyPressedCoordinate = {0, 0};
    private int[] xyReleasedCoordinate = {0, 0};

    /**
     * 保存每个workSheet的宽度.
     */
    private int[] widthArray;

    /**
     * 这个数组用来记录鼠标drag过的区域里面的点.
     */
    private List<Point> lineArray = new ArrayList<Point>();

    /**
     * 鼠标是否已经释放
     */
    private boolean isReleased = false;

    /**
     * 是否越界
     */
    private boolean isOvertakeWidth = false;

    /**
     * 能显示的tab个数
     */
    private int showCount = 0;

    /**
     * 两个添加图标的位置。
     */
    protected int iconLocation;

    /**
     * august： 当sheet的数量过多了，scrollIndex就有用了，用来表示最左边的sheet的Index
     */
    private int scrollIndex = 0;

    /**
     * 当超过最大宽度时，最右边的sheet的index
     */
    private int lastOneIndex;

    /**
     * 编辑的对象实例
     */
    private ReportComponentComposite reportComposite;
    
    private int selectedIndex = -1;

    private JPanel buttonPane;

    private boolean isAuthorityEditing = false;

    public SheetNameTabPane(ReportComponentComposite reportCompositeX) {
        this.reportComposite = reportCompositeX;
        this.setLayout(new BorderLayout(0, 0));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setBorder(null);
        this.setForeground(new Color(99, 99, 99));
        leftButton = new UIButton(LEFT_ICON) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, TOOLBAR_HEIGHT);
            }
        };
        leftButton.setUI(new UIButtonUI() {
            @Override
            protected void doExtraPainting(UIButton b, Graphics2D g2d, int w, int h, String selectedRoles) {
                if (isPressed(b) && b.isPressedPainted()) {
                    GUIPaintUtils.fillPressed(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), UIConstants.PROPERTY_PANE_BACKGROUND);
                } else if (isRollOver(b)) {
                    GUIPaintUtils.fillRollOver(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted(), UIConstants.PROPERTY_PANE_BACKGROUND);
                } else if (b.isNormalPainted()) {
                    GUIPaintUtils.fillNormal(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted());
                }
            }
        });
        leftButton.set4ToolbarButton();
        leftButton.setDisabledIcon(DISABLED_LEFT_ICON);
        rightButton = new UIButton(RIGHT_ICON) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, TOOLBAR_HEIGHT);
            }
        };
        rightButton.setUI(new UIButtonUI() {
            @Override
            protected void doExtraPainting(UIButton b, Graphics2D g2d, int w, int h, String selectedRoles) {
                if (isPressed(b) && b.isPressedPainted()) {
                    GUIPaintUtils.fillPressed(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), UIConstants.PROPERTY_PANE_BACKGROUND);
                } else if (isRollOver(b)) {
                    GUIPaintUtils.fillRollOver(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted(), UIConstants.PROPERTY_PANE_BACKGROUND);
                } else if (b.isNormalPainted()) {
                    GUIPaintUtils.fillNormal(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted());
                }
            }
        });
        rightButton.set4ToolbarButton();
        rightButton.setDisabledIcon(DISABLED_RIGHT_ICON);
        buttonPane = new JPanel(new BorderLayout(3, 0));
        buttonPane.add(rightButton, BorderLayout.EAST);
        buttonPane.add(leftButton, BorderLayout.CENTER);
        this.add(buttonPane, BorderLayout.EAST);
        leftButton.addActionListener(createLeftButtonActionListener());
        rightButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int reportCount = reportComposite.getEditingWorkBook().getReportCount();
                if (selectedIndex <= (reportCount - 1) && scrollIndex < reportCount - showCount) {
                    scrollIndex = Math.min((showCount == 0 ? 1 : showCount) + scrollIndex, reportCount - showCount - 1);
                    repaint();
                }
            }
        });

        DesignerContext.getDesignerFrame().addComponentListener(new ComponentAdapter(){
            @Override public void componentResized(ComponentEvent e) {
                for (int i = 0; i < lastOneIndex * NUM; i++) {
                    moveLeft();
                }
            }
        });
    }

    private ActionListener createLeftButtonActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                moveLeft();
            }
        };
    }

    private void moveLeft() {
        int s = scrollIndex;
        if (s == lastOneIndex && s != 0) {
            scrollIndex--;
            lastOneIndex--;
            repaint();
        } else {
            while (s > lastOneIndex && showCount != 0) {
                scrollIndex++;
                lastOneIndex++;
                repaint();
            }
            while (s < lastOneIndex && scrollIndex > 0) {
                scrollIndex--;
                lastOneIndex--;
                repaint();
            }
        }
    }

    /**
     * 设置选择index
     *
     * @param newIndex
     */
    public void setSelectedIndex(int newIndex) {
        doBeforeChange(selectedIndex);
        selectedIndex = newIndex;
        doAfterChange(newIndex);
    }

    /**
     * 在权限细粒度状态下，点击sheet，进入编辑sheet可见不可见状态
     */
    private void doWithAuthority() {
        AuthoritySheetEditedPane sheetEditedPane = new AuthoritySheetEditedPane(reportComposite.getEditingWorkBook(), selectedIndex);
        sheetEditedPane.populate();
        EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.AUTHORITY_EDITION);
        EastRegionContainerPane.getInstance().replaceAuthorityEditionPane(sheetEditedPane);
        EastRegionContainerPane.getInstance().replaceConfiguredRolesPane(RolesAlreadyEditedPane.getInstance());
    }


    /**
     * /**
     * selectedIndex 改变之前所做的事情
     *
     * @param oldIndex
     */
    protected void doBeforeChange(int oldIndex) {
    	reportComposite.doBeforeChange(oldIndex);
    }

    /**
     * selectedIndex 改变之后所做的事情
     *
     * @param newIndex
     */
    protected void doAfterChange(int newIndex) {
    	reportComposite.doAfterChange(newIndex);
    }

    /**
     * 得到选择的index
     *
     * @return
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * 鼠标拖拽
     *
     * @param e 鼠标事件
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (isAuthorityEditing) {
            return;
        }
        lineArray.add(e.getPoint());
        repaint();
    }

    /**
     * 鼠标移动
     *
     * @param e 鼠标事件
     */
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    private void checkButton(boolean buttonEnabled) {
        leftButton.setEnabled(buttonEnabled);
        rightButton.setEnabled(buttonEnabled);
    }
    
    /**
     * 抽出来方便OEM
     * @return
     */
    public Icon getAddWorkSheet(){
    	return ADD_WORK_SHEET;
    }
    public Icon getWorkSheetIcon(){
    	return WORK_SHEET_ICON;
    }
    @Override
    /**
     * 画Tab
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        isAuthorityEditing = DesignerMode.isAuthorityEditing();
        showCount = 0;
        // 开始画那些Tab.
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = GraphHelper.getFontMetrics(this.getFont());
        int charWidth = fm.charWidth('M');
        int textAscent = fm.getAscent();
        double textHeight = this.getSize().getHeight() - 1;

        widthArray = calculateWidthArray();
        int operationWidth = GRAP + getAddWorkSheet().getIconWidth() + ICON_SEP_DISTANCE + ADD_POLY_SHEET.getIconWidth();
        double maxWidth = getWidth() - operationWidth - buttonPane.getWidth();// 最大宽度
        paintBackgroundAndLine(g2d, textHeight, maxWidth, charWidth, textAscent);
        checkButton(showCount < widthArray.length);

        // richie:当linearray不为空时，说明有了鼠标拖动,下面画由于鼠标拖动产生的效果.
        //REPORT-13572 点击切换会出现重影,保证:此时鼠标点击是没有放开的,才会绘制轨迹
        if (!lineArray.isEmpty() && !isReleased) {
            paintDragTab(g2d, textHeight, charWidth, textAscent);
        }

        // richie:鼠标松开时把drag的轨迹数组clear掉.
        if (isReleased) {
            lineArray.clear();
        }
    }

    private void paintBackgroundAndLine(Graphics2D g2d, double textHeight, double maxWidth, int charWidth, int textAscent) {
        showCount = 0;
        int addIconlocation = 0;
        WorkBook workBook = reportComposite.getEditingWorkBook();
        int reportCount = workBook.getReportCount();
        double textX = 0;
        Icon sheeticon;
        for (int i = scrollIndex; i < reportCount; i++) {
            lastOneIndex = i;
            TemplateReport templateReport = workBook.getTemplateReport(i);
            boolean isNeedPaintedAuthority = false;
            if (isAuthorityEditing) {
                String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
                isNeedPaintedAuthority = templateReport.getWorkSheetPrivilegeControl().checkInvisible(selectedRoles);
            }
            sheeticon = templateReport instanceof WorkSheet ? getWorkSheetIcon() : POLY_SHEET_ICON;
            String sheetName = workBook.getReportName(i);
            if (i == selectedIndex) {
                paintSelectedTab(g2d, sheeticon, textHeight, textX, sheetName, charWidth, textAscent, isNeedPaintedAuthority);
            } else {
                paintUnSelectedTab(g2d, sheeticon, textHeight, textX, sheetName, charWidth, textAscent, i, isNeedPaintedAuthority);
            }
            int width = widthArray[i];
            textX += width + 1;
            addIconlocation += width;
            if (i < widthArray.length - 1 && textX + widthArray[i + 1] + 1 > maxWidth) {
                isOvertakeWidth = true;
                break;
            } else {
                showCount++;
                isOvertakeWidth = false;
            }
            int count = (int) (maxWidth) / width;

            if (i >= scrollIndex + count - 1) {
                isOvertakeWidth = true;
            }
        }

        // 画两个添加sheet图标
        iconLocation = isOvertakeWidth ? (int) (maxWidth) : addIconlocation + GRAP;
        
        paintAddButton(g2d);
    }
    
    protected void paintAddButton(Graphics2D g2d){
    	getAddWorkSheet().paintIcon(this, g2d, iconLocation, 3);
    	ADD_POLY_SHEET.paintIcon(this, g2d, iconLocation + getAddWorkSheet().getIconWidth() + ICON_SEP_DISTANCE, 3);
    }

    /**
     * 画选中的tab
     *
     * @param g2d
     * @param sheeticon
     * @param textHeight
     * @param textX
     * @param sheetName
     * @param charWidth
     * @param textAscent
     */
    private void paintSelectedTab(Graphics2D g2d, Icon sheeticon, double textHeight, double textX, String sheetName, int charWidth, int textAscent, boolean isNeedPaintAuthority) {
        double[] x = {textX, textX, textX + LEFT_CORNOR, textX + widthArray[selectedIndex] - RIGHT_CORNOR, textX + widthArray[selectedIndex] + RIGHT_CORNOR};
        double[] y = {0, textHeight - LEFT_CORNOR, textHeight, textHeight, 0};
        if (isNeedPaintAuthority) {
            g2d.setPaint(new GradientPaint(1, 1, UIConstants.AUTHORITY_SHEET_LIGHT, 1, getHeight() - 1, UIConstants.AUTHORITY_SHEET_DARK));
        } else {
            g2d.setPaint(new GradientPaint(1, 1, Color.WHITE, 1, getHeight() - 1, Color.WHITE));
        }
        GeneralPath generalPath = new GeneralPath(Path2D.WIND_EVEN_ODD, x.length);
        generalPath.moveTo((float) x[0], (float) y[0]);

        for (int index = 1; index < x.length; index++) {
            generalPath.lineTo((float) x[index], (float) y[index]);
        }
        generalPath.closePath();
        g2d.fill(generalPath);
        sheeticon.paintIcon(this, g2d, (int) textX + charWidth, 2);
        // peter:画字符
        g2d.setPaint(getForeground());
        GraphHelper.drawString(g2d, sheetName, (int) textX + charWidth + 14, textAscent);
    }

    /**
     * 画不是选中状态的tab
     *
     * @param g2d
     * @param sheetIcon
     * @param textHeight
     * @param textX
     * @param sheetName
     * @param charWidth
     * @param textAscent
     * @param i
     */
    private void paintUnSelectedTab(Graphics2D g2d, Icon sheetIcon, double textHeight, double textX, String sheetName, int charWidth, int textAscent, int i, boolean isNeedPaintAuthority) {
        Color tabBackground = UIConstants.COMBOBOX_BTN_NORMAL;
        int width = widthArray[i];
        double[] x = {textX, textX, textX + LEFT_CORNOR, textX + width - RIGHT_CORNOR, textX + width, textX + width};
        double[] y = {0, textHeight - LEFT_CORNOR, textHeight, textHeight, textHeight - RIGHT_CORNOR, 0};
        if (isNeedPaintAuthority) {
            g2d.setPaint(UIConstants.AUTHORITY_SHEET_UNSELECTED);
        } else{
            g2d.setPaint(tabBackground);
        }
        GeneralPath generalPath = new GeneralPath(Path2D.WIND_EVEN_ODD, x.length);
        generalPath.moveTo((float) x[0], (float) y[0]);

        for (int index = 1; index < x.length; index++) {
            generalPath.lineTo((float) x[index], (float) y[index]);
        }
        generalPath.closePath();
        g2d.fill(generalPath);

        g2d.setPaint(LINE_COLOR);
        double startX = textX > 0 ? textX - 1 : textX;
        g2d.drawRect((int)startX, 0, width, (int)textHeight);

        sheetIcon.paintIcon(this, g2d, (int) textX + charWidth, 2);
        g2d.setPaint(getForeground());
        g2d.drawString(sheetName, (int) textX + charWidth + 14, textAscent);
    }

    /**
     * 画拖拽的轨迹
     *
     * @param g2d
     * @param textHeight
     * @param charWidth
     * @param textAscent
     */
    private void paintDragTab(Graphics2D g2d, double textHeight, int charWidth, int textAscent) {
        g2d.setPaint(UIManager.getColor("TabbedPane.darkShadow"));
        Point lastPoint = lineArray.get(lineArray.size() - 1);

        // richie:鼠标拖动开始时的x坐标
        int startPointX = this.getPressedXY()[0];

        // richie：当前选中的workSheet的宽度
        int width = widthArray[selectedIndex];
        int totalWidth = 0;
        // richie:当前选中的workSheet之前的所有workSheets的总宽度
        for (int i = 0; i < selectedIndex; i++) {
            totalWidth += widthArray[i];
        }

        int distance = startPointX - totalWidth;

        int[] x = {(int) lastPoint.getX() - distance, (int) lastPoint.getX() - distance, (int) lastPoint.getX() - distance + width, (int) lastPoint.getX() + width - distance};
        int[] y = {0, (int) (textHeight), (int) (textHeight), 0};
        g2d.drawPolygon(x, y, 4);
        // peter:画字符
        g2d.setPaint(getForeground());
        // richie：把当前选中的workSheet的名字画到鼠标拖动产生的图形上.
        g2d.drawString(reportComposite.getEditingWorkBook().getReportName(selectedIndex), (int) lastPoint.getX() - distance + charWidth, textAscent);
    }

    /**
     * 根据名字和个数计算出所有tab的宽度
     *
     * @return
     */
    private int[] calculateWidthArray() {
        FontMetrics fm = GraphHelper.getFontMetrics(this.getFont());
        int charWidth = fm.charWidth('M');
        WorkBook workBook = reportComposite.getEditingWorkBook();
        int reportCount = workBook.getReportCount();
        int[] widthArray = new int[reportCount];
        for (int i = 0; i < reportCount; i++) {
            String sheetName = workBook.getReportName(i);
            widthArray[i] = fm.stringWidth(sheetName) + charWidth * 2 - 1 + ADD_WIDTH_BY_SHEETNAME;
        }
        return widthArray;
    }

    private int[] getPressedXY() {
        return this.xyPressedCoordinate;
    }

    private void setPressedXY(int x, int y) {
        this.xyPressedCoordinate[0] = x;
        this.xyPressedCoordinate[1] = y;
    }

    private int[] getReleasedXY() {
        return this.xyReleasedCoordinate;
    }

    private void setReleasedXY(int x, int y) {
        this.xyReleasedCoordinate[0] = x;
        this.xyReleasedCoordinate[1] = y;
    }

    /**
     * 鼠标点击事件
     *
     * @param e 鼠标事件
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * 鼠标按下事件
     *
     * @param evt 鼠标事件
     */
    @Override
    public void mousePressed(MouseEvent evt) {
        isReleased = false;
        int reportcount = reportComposite.getEditingWorkBook().getReportCount();
        if (scrollIndex < 0 || scrollIndex >= reportcount) {
            return;
        }
        reportComposite.stopEditing();
        int evtX = evt.getX();
        int evtY = evt.getY();
        this.setPressedXY(evtX, evtY);
        boolean isBlank = true;
        int textX = 0;
        for (int i = scrollIndex; i <= lastOneIndex; i++) {
            int textWidth = widthArray[i];
            if (evtX >= textX && evtX < textX + textWidth) {
                boolean needRefreshPropertiesPane = getSelectedIndex() != i;
                setSelectedIndex(i);
                if (needRefreshPropertiesPane) {
                    HistoryTemplateListCache.getInstance().getCurrentEditingTemplate().refreshEastPropertiesPane();
                }

                isBlank = false;
                reportComposite.setComposite();
                if (isAuthorityEditing) {
                    doWithAuthority();
                }
                DesignerContext.getDesignerFrame().getContentFrame().repaint();
                break;
            }
            textX += textWidth;
        }

        if (SwingUtilities.isLeftMouseButton(evt)) {
        	processLeftMouseButton(evtX);
        }
        if (isBlank) {
            return;
        }
        if (SwingUtilities.isRightMouseButton(evt) && !isAuthorityEditing) {
        	processRightMouseButton(evtX, evtY);
        }
    }
    
    private void processRightMouseButton(int evtX, int evtY){
        MenuDef def = new MenuDef();
        addInsertGridShortCut(def);
    	def.addShortCut(new PolyReportInsertAction(), SeparatorDef.DEFAULT, new RemoveSheetAction(), new RenameSheetAction(),
        		new CopySheetAction());
        JPopupMenu tabPop = def.createJMenu().getPopupMenu();
        def.updateMenu();
        GUICoreUtils.showPopupMenu(tabPop, this, evtX - 1, evtY - 1);
    }
    
    private void processLeftMouseButton(int evtX){
        if (evtX > iconLocation && evtX < iconLocation + GRID_TOSHEET_RIGHT) {
        	firstInsertActionPerformed();
        } else if (evtX > iconLocation + POLY_TOSHEET_LEFT && evtX < iconLocation + POLY_TOSHEET_RIGHT) {
            new PolyReportInsertAction().actionPerformed(null);
        }
    
    }

    protected void addInsertGridShortCut(MenuDef def){
    	def.addShortCut(new GridReportInsertAction());
    }

    protected void firstInsertActionPerformed(){
		new GridReportInsertAction().actionPerformed(null);
    }


    /**
     * 鼠标释放事件
     *
     * @param e 鼠标事件
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        this.isReleased = true;
        this.setReleasedXY(e.getX(), e.getY());
        int width = 0;
        for (int w : widthArray) {
            width = width + w + SHEET_ICON_GAP;
        }
        if (isAuthorityEditing) {
            return;
        }
        if (this.getPressedXY()[0] != this.getReleasedXY()[0] || this.getPressedXY()[1] != this.getReleasedXY()[1]) {
            // 因为下面的操作会使得selectedIndex变化，所以要个量来保存开始的selectedIndex
            int si = selectedIndex;
            // richie:workSheet向右拖动
            int moveRighttDistance = this.getReleasedXY()[0] - this.getPressedXY()[0];
            // richie:向左拖动
            int moveLeftDistance = -moveRighttDistance;
            // samuel:拖动范围超过宽度的一半才移动,并且不越界
            if (moveRighttDistance > widthArray[si] / 2 && this.getReleasedXY()[0] < width) {
                move2Right(moveRighttDistance, si);
                //拖拽触发保存
                reportComposite.fireTargetModified();
                this.repaint(100);
                return;
            } else if (moveLeftDistance > widthArray[si] / 2) {
                move2Left(moveLeftDistance, si);
                //拖拽触发保存
                reportComposite.fireTargetModified();
                this.repaint(100);
                return;
            } else {
                setSelectedIndex(si);
                DesignerContext.getDesignerFrame().getContentFrame().repaint();
            }
        }
        this.repaint(100);
    }

    /**
     * 右移动
     *
     * @param moveRighttDistance 右侧移动距离
     * @param si 宽度坐标
     */
    private void move2Right(int moveRighttDistance, int si) {
        int reportcount = reportComposite.getEditingWorkBook().getReportCount();
        if (selectedIndex < reportcount - 1) {
            while (moveRighttDistance > widthArray[si] / 2) {
                int i = selectedIndex;
                this.exchangeWorkSheet(selectedIndex, selectedIndex + 1);
                setSelectedIndex(selectedIndex + 1);
                // richie:拖到越过所有的workSheet时，直接就作为最后一个workSheet
                if (i > reportcount - 3) {
                    setSelectedIndex(reportcount - 1);
                    return;
                }
                moveRighttDistance -= widthArray[i + 1];
            }

        } else {
            return;
        }
    }

    /**
     * 左移动
     *
     * @param moveLeftDistance 左侧距离
     * @param si 宽度坐标
     */
    private void move2Left(int moveLeftDistance, int si) {
        if (selectedIndex > 0) {
            while (moveLeftDistance > widthArray[si] / 2) {
                int i = selectedIndex;
                this.exchangeWorkSheet(selectedIndex, selectedIndex - 1);
                setSelectedIndex(selectedIndex - 1);
                // richie:拖到越过所有的workSheet时，直接就作为第一个workSheet
                if (i < 2) {
                    setSelectedIndex(0);
                    return;
                }
                moveLeftDistance -= widthArray[i - 1];
            }
        } else {
            return;
        }
    }

    /**
     * 鼠标进入事件
     *
     * @param e 鼠标事件
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * 鼠标退出事件
     *
     * @param e 鼠标事件
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * exchange workSheet
     *
     * @param
     * @param index1
     * @param index2
     * @return workBook
     */
    private void exchangeWorkSheet(int index1, int index2) {
        WorkBook workbook = reportComposite.getEditingWorkBook();
        TemplateReport workSheet1 = workbook.getTemplateReport(index1);
        String name1 = reportComposite.getEditingWorkBook().getReportName(index1);

        TemplateReport workSheet2 = workbook.getTemplateReport(index2);
        String name2 = workbook.getReportName(index2);
        workbook.addReport(index1, name2, workSheet2);
        workbook.removeReport(index1 + 1);
        workbook.addReport(index2, name1, workSheet1);
        workbook.removeReport(index2 + 1);
    }

    protected abstract class SheetInsertAction extends UpdateAction {
        SheetInsertAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Insert") + getTemplateReportType());
            this.setSmallIcon(IOUtils.readIcon("/com/fr/base/images/cell/control/add.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (isAuthorityEditing) {
                return;
            }
            int insertPos = selectedIndex + 1;
            if (isOvertakeWidth) {
                scrollIndex++;
            }

            reportComposite.getEditingWorkBook().addReport(insertPos, newTemplateReport());
            setSelectedIndex(insertPos);

            // sheet名字的公式也需要做相应的变化.
            reportComposite.setComposite();
            reportComposite.fireTargetModified();
            ReportComponent ReportComponent = reportComposite.centerCardPane.editingComponet;
            ReportComponent.setSelection(ReportComponent.getDefaultSelectElement());


            showCount = 1;
            WorkBook workBook = reportComposite.getEditingWorkBook();
            int reportCount = workBook.getReportCount();
            double textX = 0;
            for (int i = scrollIndex; i < reportCount; i++) {
                widthArray = calculateWidthArray();
                int width = widthArray[i];
                textX += width + 1;
                int operationWidth = GRAP + getAddWorkSheet().getIconWidth() + ICON_SEP_DISTANCE + ADD_POLY_SHEET.getIconWidth();
                double maxWidth = getWidth() - operationWidth - buttonPane.getWidth();// 最大宽度
                if (i < widthArray.length - 1 && textX + widthArray[i + 1] + 1 > maxWidth) {
                    isOvertakeWidth = true;
                    scrollIndex++;
                    continue;
                } else {
                    showCount++;
                    isOvertakeWidth = false;
                }
            }

            if (scrollIndex > 0 && showCount + scrollIndex < reportCount) {
                scrollIndex++;
            }

            DesignerContext.getDesignerFrame().getContentFrame().repaint();
        }

        protected abstract TemplateReport newTemplateReport();

        protected abstract String getTemplateReportType();
    }

    protected class GridReportInsertAction extends SheetInsertAction {
    	
        @Override
        protected TemplateReport newTemplateReport() {
            return new WorkSheet();
        }

        @Override
        protected String getTemplateReportType() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Report");
        }
    }

    protected class PolyReportInsertAction extends SheetInsertAction {
        @Override
        protected TemplateReport newTemplateReport() {
            return new PolyWorkSheet();
        }

        @Override
        protected String getTemplateReportType() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Poly");
        }
    }

    private class RemoveSheetAction extends UpdateAction {
        RemoveSheetAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isAuthorityEditing) {
                return;
            }
            int count = reportComposite.getEditingWorkBook().getReportCount();
            if (count <= 1) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(reportComposite), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_At_Least_One_Visual_Worksheet") + "！");
                return;
            }
            int returnValue = FineJOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(reportComposite), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Des_Remove_Work_Sheet"),
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Confirm"),
                    JOptionPane.OK_CANCEL_OPTION);
            if (returnValue == JOptionPane.OK_OPTION) {
                if (DesignerContext.getFormatState() != DesignerContext.FORMAT_STATE_NULL) {
                    doCancelFormat();
                }
                reportComposite.getEditingWorkBook().removeReport(selectedIndex);
                int insertPos = selectedIndex > 0 ? selectedIndex - 1 : 0;
                setSelectedIndex(insertPos);
                final int s = scrollIndex;
                if (s < lastOneIndex && scrollIndex > 0) {
                    scrollIndex--;
                    lastOneIndex--;
                }
                reportComposite.setComposite();
                reportComposite.repaint();
                reportComposite.fireTargetModified();
            }
        }
    }


    private void doCancelFormat() {
        boolean isSameCase = ComparatorUtils.equals(reportComposite.centerCardPane.editingComponet.elementCasePane, DesignerContext.getReferencedElementCasePane());
        boolean isPolyContains = false;
        if (reportComposite.centerCardPane.editingComponet instanceof PolyDesigner) {
            isPolyContains = ((PolyDesigner) reportComposite.centerCardPane.editingComponet).containsBlocks(DesignerContext.getReferencedElementCasePane());
        }
        boolean isDelPane = isSameCase || isPolyContains;

        if (isDelPane && this.selectedIndex == DesignerContext.getReferencedIndex()) {
            DesignerContext.setFormatState(DesignerContext.FORMAT_STATE_NULL);
            ((ElementCasePane) DesignerContext.getReferencedElementCasePane()).getGrid().setNotShowingTableSelectPane(true);
            DesignerContext.setReferencedElementCasePane(null);
            DesignerContext.setReferencedIndex(0);
            this.repaint();

        }
    }

    private class RenameSheetAction extends UpdateAction {
        RenameSheetAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Rename"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/rename.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedIndex < 0 || selectedIndex >= reportComposite.getEditingWorkBook().getReportCount()) {
                return;
            }

            String newName = JOptionPane.showInputDialog(reportComposite, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Rename") + ":", reportComposite.getEditingWorkBook().getReportName(selectedIndex));
            if (newName != null) {
                // marks：判断是否重名
                boolean isExisted = false;
                for (int i = 0; i < reportComposite.getEditingWorkBook().getReportCount(); i++) {
                    if (newName.equalsIgnoreCase(reportComposite.getEditingWorkBook().getReportName(i))) {
                        isExisted = true;
                        break;
                    }
                }
                if (!isExisted) {
                    reportComposite.getEditingWorkBook().setReportName(selectedIndex, newName);
                    reportComposite.getEditingReportComponent().fireTargetModified();
                    // sheet名字的公式也需要做相应的变化.
                    reportComposite.repaint();
                } else {
                    JOptionPane.showMessageDialog(reportComposite, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_The_Name_Has_Been_Existed"));
                }
            }
        }
    }

    private class CopySheetAction extends UpdateAction {
        CopySheetAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Copy"));
            this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_edit/copy.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            TemplateReport tr;
            try {
                tr = (TemplateReport) reportComposite.getEditingWorkBook().getReport(selectedIndex).clone();
            } catch (CloneNotSupportedException ex) {
                return;
            }

            // 在需要复制的sheet后加上
            int index = selectedIndex;
            reportComposite.getEditingWorkBook().addReport(index + 1, tr);

            String prefix = reportComposite.getEditingWorkBook().getReportName(index);
            int times = 0;
            for (int i = 0; i < reportComposite.getEditingWorkBook().getReportCount(); i++) {
                if (reportComposite.getEditingWorkBook().getReportName(i).startsWith(prefix)) {
                    times++;
                }
            }
            String suffix = "-" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Copy") + times;
            reportComposite.getEditingWorkBook().setReportName(index + 1, prefix + suffix);
            setSelectedIndex(index + 1);
            reportComposite.validate();
            reportComposite.repaint(100);
            reportComposite.fireTargetModified();
        }
    }
}
