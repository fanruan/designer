package com.fr.design.gui.date;

import com.fr.base.BaseUtils;
import com.fr.base.background.GradientBackground;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUIPaintUtils;

import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UICalendarPanel extends JPanel {
    private static final Font FONT_SONG = new Font(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Song_TypeFace"),0,12);
    private static final Font FONT_BLACK = new Font(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Black_Font"),0,12);
    private static final int WEEKDAY_COUNT = 7;
    private static final int TOTAL_DAYS_COUNT = 42;

    protected Color selectedBackground;
    protected Color selectedForeground;
    protected Color background;
    protected Color foreground;

    private Calendar calendar = null;
    private UILabel monthLabel = null;
    private DayPane days = null;
    private HMSPane hms = null;
    private MouseListener dayBttListener = null;
    private boolean isSupportDateChangeListener = false;
    private java.util.Date selectedDate = null;
    private boolean isTimePicker;
    /**
     * 年月格式
     */
    final SimpleDateFormat monthFormat
            = new SimpleDateFormat("yyyy-MM");

    public UICalendarPanel() {
        this(new Date(), false);
    }

    public UICalendarPanel(boolean isTimerPicker) {
        this(new Date(), isTimerPicker);
    }

    public UICalendarPanel(Date selectedDate, boolean isTimerPicker) {

        this.selectedDate = selectedDate;
        this.isTimePicker = isTimerPicker;
        calendar = Calendar.getInstance();

        selectedBackground = UIManager.getColor(
                "ComboBox.selectionBackground");
        selectedForeground = UIManager.getColor(
                "ComboBox.selectionForeground");
        background = UIManager.getColor("ComboBox.background");
        foreground = UIManager.getColor("ComboBox.foreground");

        dayBttListener = createDayBttListener();

        //renderer this
        setPreferredSize(new Dimension(218, 179));
        setBackground(new Color(0xFFFFFF));
        setBorder(BorderFactory.createLineBorder(new Color(0x959595)));

        setLayout(FRGUIPaneFactory.createBorderLayout());
        add(BorderLayout.NORTH, createNorthPane());
        add(BorderLayout.CENTER, createCenterPane());
        if (isTimerPicker) {
            setPreferredSize(new Dimension(218, 209));
            add(BorderLayout.SOUTH, createSouthPane());
            updateHMS();
        }
    }

    //   << <   yyyy/MM/dd  > >>
    private JPanel createNorthPane () {
        JPanel pNorth = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
        pNorth.setBackground(new Color(0xFFFFFF));
        pNorth.setPreferredSize(new Dimension(1, 22));

        pNorth.add(Box.createHorizontalStrut(5));
        pNorth.add(createSkipButton(Calendar.YEAR, -1 , new Icon[] {
                BaseUtils.readIcon("/com/fr/design/images/calender/year_reduce.png"),
                BaseUtils.readIcon("/com/fr/design/images/calender/year_reduce_hover.png"),
                BaseUtils.readIcon("/com/fr/design/images/calender/year_reduce_click.png")
        }));
        pNorth.add(Box.createHorizontalStrut(11));
        UILabel monthMinus = createSkipButton(Calendar.MONTH, -1, new Icon[]{
                BaseUtils.readIcon("/com/fr/design/images/calender/month_reduce.png"),
                BaseUtils.readIcon("/com/fr/design/images/calender/month_reduce_hover.png"),
                BaseUtils.readIcon("/com/fr/design/images/calender/month_reduce_click.png")
        });
        monthMinus.setPreferredSize(new Dimension(20, 20));
        pNorth.add(monthMinus);
        monthLabel = new UILabel("", UILabel.CENTER);
        monthLabel.setBackground(new Color(0xFFFFFF));
        monthLabel.setForeground(new Color(0x000000));
        monthLabel.setFont(FONT_SONG);
        pNorth.add(Box.createHorizontalGlue());
        pNorth.add(monthLabel);
        pNorth.add(Box.createHorizontalGlue());

        UILabel monthPlus = createSkipButton(Calendar.MONTH, 1, new Icon[]{
                BaseUtils.readIcon("/com/fr/design/images/calender/month_add.png"),
                BaseUtils.readIcon("/com/fr/design/images/calender/month_add_hover.png"),
                BaseUtils.readIcon("/com/fr/design/images/calender/month_add_click.png")
        });
        monthPlus.setPreferredSize(new Dimension(20, 20));
        monthPlus.setHorizontalAlignment(SwingConstants.RIGHT);
        pNorth.add(monthPlus);
        pNorth.add(Box.createHorizontalStrut(11));
        pNorth.add(createSkipButton(Calendar.YEAR, 1 , new Icon[] {
                BaseUtils.readIcon("/com/fr/design/images/calender/year_add.png"),
                BaseUtils.readIcon("/com/fr/design/images/calender/year_add_hover.png"),
                BaseUtils.readIcon("/com/fr/design/images/calender/year_add_click.png")
        }));
        pNorth.add(Box.createHorizontalStrut(5));

        return pNorth;
    }


    private JPanel createCenterPane() {
        //星期日 星期一 星期二 星期三 星期四 星期五 星期六
        JPanel pWeeks = new JPanel(new GridLayout(1, 7, 1, 0));
        pWeeks.setPreferredSize(new Dimension(216, 22));
        pWeeks.setBackground(new Color(0xFFFFFF));
        pWeeks.setOpaque(true);
        String[] strWeeks = new String[] {StringUtils.EMPTY, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Sun"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Mon"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tue"),com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Wed"),com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Thu"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Fri"),com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Sat")
        };
        for (int i = 1; i <= WEEKDAY_COUNT; i++) {
            UILabel label = new UILabel();
            label.setHorizontalAlignment(UILabel.CENTER);
            label.setForeground(new Color(0x4D4C4C));
            label.setFont(FONT_BLACK);
            label.setText(strWeeks[i]);
            pWeeks.add(label);
        }

        //中间放日期的面板
        days = new DayPane();
        days.setOpaque(true);
        days.setPreferredSize(new Dimension(216, 115));
        JPanel pCenter = FRGUIPaneFactory.createBorderLayout_S_Pane();
        pCenter.setOpaque(true);
        pCenter.add(pWeeks, BorderLayout.NORTH);
        pCenter.add(days, BorderLayout.CENTER);

        //显示今天的日期,直接单击图标跳到今天
        GradientPane pToday = new GradientPane(new GradientBackground(new Color(0x097BDA), new Color(0x40A3EE), GradientBackground.TOP2BOTTOM), false);
        pToday.setPreferredSize(new Dimension(216, 18));
        pToday.setLayout(new BorderLayout());
        UIDayLabel lbToday = new UIDayLabel(new Date(), false);
        lbToday.setForeground(new Color(0x000000));
        lbToday.addMouseListener(createTodayListener(pToday, lbToday));
        pToday.setBackground(new Color(0xF0F0F0));
        pToday.add(lbToday, BorderLayout.CENTER);
        pCenter.add(pToday, BorderLayout.SOUTH);

        return pCenter;
    }

    private JPanel createSouthPane () {
        JPanel sPane = new JPanel();
        sPane.setPreferredSize(new Dimension(216, 30));
        sPane.setBackground(Color.WHITE);
        sPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 6));
        UILabel timeLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Time") + ":");
        timeLabel.setBorder(BorderFactory.createEmptyBorder(0,9,0,5));
        timeLabel.setFont(FONT_SONG);
        sPane.add(timeLabel);
        hms = new HMSPane();
        sPane.add(hms);

        UILabel gap = new UILabel();
        gap.setPreferredSize(new Dimension(26,1));
        sPane.add(gap);

        UIButton okButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_OK")) {
            public Dimension getPreferredSize() {
                return new Dimension(32,18);
            }

            public Insets getInsets() {
                return new Insets(0, 0, 0, 0);
            }
        };
        okButton.setFont(FONT_SONG);
        okButton.setVerticalAlignment(SwingConstants.CENTER);
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                hms.update(UICalendarPanel.this.calendar);
                UICalendarPanel.this.selectedDate = UICalendarPanel.this.calendar.getTime();
                fireDateChanged(new ChangeEvent(UICalendarPanel.this.selectedDate));
            }
        });

        sPane.add(okButton);

        return sPane;
    }

    public void resetHMSPaneSelectedNumberField() {
        if (this.hms != null) {
            this.hms.selectedNumberField = this.hms.hField;
            this.hms.nextButton.requestFocus();
        }
    }

    /**
     * 创建上一月,下一月,上一年,下一年"按钮"
     * @param field int
     * @param amount int
     * @return UILabel
     */
    protected UILabel createSkipButton(final int field, final int amount,final Icon[] icons) {
        if (icons.length != 3) {
            return new UILabel();
        }
        UILabel label = new UILabel();
        label.setIcon(icons[0]);
        label.setRequestFocusEnabled(false);
        label.addMouseListener(createSkipListener(label, field, amount, icons));
        return label;
    }

    protected MouseListener createSkipListener(final UILabel label, final int field,
                                               final int amount, final Icon[] icons) {
        return new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                label.setIcon(icons[1]);
                calendar.add(field, amount);
                updateDays();
                resetHMSPaneSelectedNumberField();
            }

            public void mouseEntered(MouseEvent e) {
               label.setIcon(icons[1]);
            }

            public void mouseExited(MouseEvent e) {
               label.setIcon(icons[0]);
            }

            public void mousePressed(MouseEvent e) {
                label.setIcon(icons[2]);
            }
        };
    }

    /**
     * 更新日期
     */
    protected void updateDays() {
        //更新月份
        monthLabel.setText(monthFormat.format(calendar.getTime()));
        days.removeAll();
        days.setFloatIndex(-1);
        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.setTime(selectedDate);
        Calendar setupCalendar = (Calendar) calendar.clone();
        setupCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int first = setupCalendar.get(Calendar.DAY_OF_WEEK);
        setupCalendar.add(Calendar.DATE, -first);

        boolean isCurrentMonth = false;
        for (int i = 0; i < TOTAL_DAYS_COUNT; i++) {
            setupCalendar.add(Calendar.DATE, 1);
            GradientPane gp = new GradientPane(new GradientBackground(new Color(0xFEFEFE), new Color(0xF3F2F3), GradientBackground.TOP2BOTTOM), true);
            gp.setIndex(i);
            gp.setLayout(new BorderLayout());
            gp.setBorder(null);
            UIDayLabel label = new UIDayLabel(setupCalendar.getTime());
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            label.setBorder(BorderFactory.createEmptyBorder(0,0,0,9));
            label.addMouseListener(dayBttListener);
            if ("1".equals(label.getText())) {
                isCurrentMonth = !isCurrentMonth;
            }
            label.setEnabled(isCurrentMonth);
            if (!isCurrentMonth) {
                label.setForeground(new Color(0x6F6F6));
            }
            //当前选择的日期
            if (setupCalendar.get(Calendar.DAY_OF_MONTH) == selectedCalendar.get(Calendar.DAY_OF_MONTH) && isCurrentMonth) {
                gp.setGradientBackground(new GradientBackground(new Color(0x097BD9), new Color(0x41A3EE), GradientBackground.TOP2BOTTOM));
                gp.add(label, BorderLayout.CENTER);
                days.add(gp);
                days.setSelectedIndex(i);
                this.selectedDate = label.getDate();
                this.calendar.setTime(this.selectedDate);
            }else {
                gp.add(label, BorderLayout.CENTER);
                days.add(gp);
            }
        }
        days.validate();
    }

    public void updateHMS() {
        if (hms != null){
            hms.populate(this.calendar);
            hms.validate();
        }
    }

    protected MouseListener createTodayListener(final GradientPane jp, final UIDayLabel label) {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                jp.setBorder(BorderFactory.createLineBorder(new Color(0x3868AA)));
                jp.setPaintGradientBackground(true);
                jp.repaint();
                label.setForeground(new Color(0xFFFFFF));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jp.setBackground(new Color(0xF0F0F0));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                jp.setBackground(new Color(0xC8DDEE));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                jp.setBackground(new Color(0xF0F0F0));
                label.setForeground(new Color(0x000000));
                jp.setBorder(null);
                jp.setPaintGradientBackground(false);
                jp.repaint();
                if (isTimePicker) {
                    UICalendarPanel.this.setSelectedDate(label.getDate());
                    updateDays();
                } else {
                    UICalendarPanel.this.isSupportDateChangeListener = true;
                    UICalendarPanel.this.setSelectedDate(label.getDate());
                    UICalendarPanel.this.isSupportDateChangeListener = false;
                }
                resetHMSPaneSelectedNumberField();
            }
        };
    }

    protected MouseListener createDayBttListener() {
        return new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    UIDayLabel com = (UIDayLabel) e.getComponent();
                    GradientPane gp = (GradientPane)com.getParent();
                    if (days.selectedIndex != -1) {
                        ((GradientPane)days.getComponent(days.selectedIndex)).setGradientBackground(new GradientBackground(new Color(0xFEFEFE), new Color(0xF3F2F3), GradientBackground.TOP2BOTTOM));
                    }
                    gp.setGradientBackground(new GradientBackground(new Color(0x097BD9), new Color(0x41A3EE), GradientBackground.TOP2BOTTOM));
                    days.setSelectedIndex(gp.getIndex());
                }
            }


            public void mouseReleased(MouseEvent e) {
                UIDayLabel com = (UIDayLabel) e.getComponent();
                if (isTimePicker) {
                    UICalendarPanel.this.setSelectedDate(com.getDate());
                } else {
                    UICalendarPanel.this.isSupportDateChangeListener = true;
                    UICalendarPanel.this.setSelectedDate(com.getDate());
                    UICalendarPanel.this.isSupportDateChangeListener = false;
                }
                resetHMSPaneSelectedNumberField();
            }

            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    JComponent com = (JComponent) e.getComponent();
                    GradientPane gp = (GradientPane)com.getParent();
                    if (gp.getIndex() == days.selectedIndex) {
                        return;
                    }
                    gp.setGradientBackground(new GradientBackground(new Color(0xFFFFFF), new Color(0xEAF4FC), GradientBackground.TOP2BOTTOM));
                    days.setFloatIndex(gp.getIndex());
                }

            }

            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    JComponent com = (JComponent) e.getComponent();
                    GradientPane gp = (GradientPane)com.getParent();
                    if (gp.getIndex() != days.selectedIndex) {
                        gp.setGradientBackground(new GradientBackground(new Color(0xFEFEFE), new Color(0xF3F2F3), GradientBackground.TOP2BOTTOM));
                    }
                    days.setFloatIndex(-1);
                    days.repaint();
                }
            }
        };
    }

    protected EventListenerList eventlistenerList = new EventListenerList();

    public void addDateChangeListener(ChangeListener l) {
        eventlistenerList.add(ChangeListener.class, l);
    }

    public void removeDateChangeListener(ChangeListener l) {
        eventlistenerList.remove(ChangeListener.class, l);
    }

    protected void fireDateChanged(ChangeEvent e) {
        Object[] listeners = eventlistenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
        this.calendar.setTime(selectedDate);
        updateDays();
        if (isSupportDateChangeListener) {
            fireDateChanged(new ChangeEvent(selectedDate));
        }
    }

    public void setSelectedHMS() {
        hms.update(this.calendar);
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    private class GradientPane extends JPanel {

        private int index;
        private boolean isGradientBackground;
        private GradientBackground gradientBackground;

        public GradientPane() {

        }

        public GradientPane(GradientBackground gradientBackground, boolean isGradientBackground) {
            super();
            this.gradientBackground = gradientBackground;
            this.isGradientBackground = isGradientBackground;
        }

        public void paint(Graphics g){
            super.paint(g);
            if (isGradientBackground && gradientBackground != null) {
                gradientBackground.paint(g, new Rectangle(this.getWidth(), this.getHeight()));
            }
            paintChildren(g);
        }

        public void setPaintGradientBackground(boolean flag) {
            this.isGradientBackground = flag;
        }

        public void setGradientBackground(GradientBackground gradientBackground) {
            this.gradientBackground = gradientBackground;
        }

        public void setIndex(int i) {
            this.index = i;
        }

        public int getIndex () {
            return this.index;
        }

        public void setForeground(Color c) {
            super.setForeground(c);
            if (getComponentCount() > 0) {
                getComponent(0).setForeground(c);
            }
        }
    }

    private class DayPane extends JPanel {
        private Color floatColor = new Color(0xC5E2F9);
        private Color selectedColor = new Color(0x41A3EE);
        private int floateIndex;
        private int selectedIndex;

        public DayPane() {
            floateIndex = -1;
            selectedIndex = -1;
            this.setLayout(new GridLayout(6,7,1,1));
            this.setBackground(new Color(0xFFFFFF));
            this.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(0xDADADA)));
        }

        public void paint (Graphics g) {
            super.paint(g);
            int width = 31;
            int height = 19;
            Color oldColor = g.getColor();
            g.setColor(new Color(0xDADADA));
            int start_x = 30;
            int start_y = 19;
            for (int i = 0; i < 6; i ++) {
                g.drawLine(start_x, 0, start_x, getHeight());
                start_x += width;
            }
            for (int i = 0; i < 5; i ++){
                g.drawLine(0, start_y, getWidth(), start_y);
                start_y += height;
            }

            if (floateIndex > -1) {
                g.setColor(floatColor);
                paintChindPane(g, floateIndex);
            }
            if (selectedIndex > -1) {
                g.setColor(selectedColor);
                paintChindPane(g,selectedIndex);
            }
            g.setColor(oldColor);
        }

        private void paintChindPane(Graphics g, int index) {
            if (index%7 == 0 ) {
                int y1 = index/7*19;
                g.drawLine(0,y1,30,y1);
                g.drawLine(0,y1 + 19, 30, y1 +19);
                g.drawLine(30, y1, 30 ,y1 + 19);
            }else if (index%7 == 6) {
                int y1 = index/7*19;
                g.drawLine(185,y1,216,y1);
                g.drawLine(185,y1 + 19, 216, y1 +19);
                g.drawLine(185, y1, 185 ,y1 + 19);
            } else {
                int x1 = index%7*31 -1;
                int y1 = index/7*19;
                g.drawRect(x1, y1, 31, 19);
            }
        }

        public void setFloatIndex(int index) {
            this.floateIndex = index;
            repaint();
        }

        public void setSelectedIndex(int index) {
            if (this.selectedIndex != -1 && this.selectedIndex < getComponentCount()) {
                this.getComponent(selectedIndex).setForeground(Color.black);
            }
            if (index != -1 && index < getComponentCount()) {
                this.getComponent(index).setForeground(Color.WHITE);
            }
            this.selectedIndex = index;
            repaint();
        }

    }

    private class HMSPane extends JPanel{
        private boolean isRolOver;
        private UIButton preButton;
        private UIButton nextButton;
        private CalendarNumberField hField;
        private CalendarNumberField mField;
        private CalendarNumberField sField;
        private CalendarNumberField selectedNumberField;

        public HMSPane () {
            this.setPreferredSize(new Dimension(101, 18));
            this.setLayout(new BorderLayout(0, 0));
            this.setBackground(null);

            initComponents();
            initListener();

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    isRolOver = false;
                    repaint();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    isRolOver = true;
                    repaint();
                }
            });

        }

        private void initComponents() {
            JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEFT,0,2));
            jp.setBackground(null);
            jp.setBorder(null);

            hField = new CalendarNumberField(23);
            mField = new CalendarNumberField(59);
            sField = new CalendarNumberField(59);
            selectedNumberField = hField;

            jp.add(hField);
            jp.add(createGapLabel());
            jp.add(mField);
            jp.add(createGapLabel());
            jp.add(sField);

            this.add(jp, BorderLayout.CENTER);
            preButton = new UIButton(UIConstants.ARROW_UP_ICON){
                public boolean shouldResponseChangeListener() {
                    return false;
                }
            };
            preButton.setRoundBorder(true, Constants.LEFT);
            nextButton = new UIButton(UIConstants.ARROW_DOWN_ICON){
                public boolean shouldResponseChangeListener() {
                    return false;
                }
            };
            nextButton.setRoundBorder(true, Constants.LEFT);
            JPanel arrowPane = new JPanel();
            arrowPane.setPreferredSize(new Dimension(11, 18));
            arrowPane.setLayout(new GridLayout(2, 1));
            arrowPane.add(preButton);
            arrowPane.add(nextButton);
            this.add(arrowPane, BorderLayout.EAST);
        }

        private void initListener() {
            MouseAdapter backgroundAdapter = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                   HMSPane.this.selectedNumberField = (CalendarNumberField)e.getComponent();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isRolOver = false;
                    HMSPane.this.repaint();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    isRolOver = true;
                    HMSPane.this.repaint();
                }
            };
            hField.addMouseListener(backgroundAdapter);
            mField.addMouseListener(backgroundAdapter);
            sField.addMouseListener(backgroundAdapter);
            nextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    HMSPane.this.selectedNumberField.setValue(HMSPane.this.selectedNumberField.getIntValue() - 1);
                    HMSPane.this.selectedNumberField.requestFocus();
                    HMSPane.this.selectedNumberField.selectAll();

                }
            });
            preButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    HMSPane.this.selectedNumberField.setValue(HMSPane.this.selectedNumberField.getIntValue() + 1);
                    HMSPane.this.selectedNumberField.requestFocus();
                    HMSPane.this.selectedNumberField.selectAll();
                }
            });
        }

        private UILabel createGapLabel () {
            UILabel uiLabel = new UILabel(":");
            uiLabel.setHorizontalAlignment(SwingConstants.CENTER);
            uiLabel.setBackground(null);
            uiLabel.setBorder(null);
            uiLabel.setPreferredSize(new Dimension(6,10));
            return uiLabel;
        }

        public Insets getInsets() {
            return new Insets(1,3,1,0);
        }

        public void paint(Graphics g) {
            super.paint(g);
            paintBorder(g);
        }

        public void paintBorder(Graphics g) {
            Graphics2D g2d = (Graphics2D)g;
            if (isRolOver) {
                Shape shape = new RoundRectangle2D.Double(1, 1, 86, 15, UIConstants.ARC, UIConstants.ARC);
                GUIPaintUtils.paintBorderShadow(g2d, 3, shape, UIConstants.HOVER_BLUE, Color.WHITE);
            } else {
                GUIPaintUtils.drawBorder(g2d, 0, 0, 101, 18, true, 3);
            }
        }

        public void populate(Calendar calendar){
            this.hField.setValue(calendar.get(Calendar.HOUR_OF_DAY));
            this.mField.setValue(calendar.get(Calendar.MINUTE));
            this.sField.setValue(calendar.get(Calendar.SECOND));
        }

        public void update(Calendar calendar) {
            calendar.set(Calendar.HOUR_OF_DAY, hField.getIntValue());
            calendar.set(Calendar.MINUTE, mField.getIntValue());
            calendar.set(Calendar.SECOND, sField.getIntValue());
        }
    }

    public static void main(String[] args){
        JFrame frame = new JFrame();

        UICalendarPanel calendarPanel = new UICalendarPanel();
        final UITextField field = new UITextField();
        field.setPreferredSize(new Dimension(120, 25));
        calendarPanel.addDateChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
                Date selectedDate = (Date)e.getSource();
                SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
                field.setText(f.format(selectedDate));
            }
        });
        frame.getContentPane().setLayout(FRGUIPaneFactory.createCenterFlowLayout());
        frame.getContentPane().add(field);
        frame.getContentPane().add(calendarPanel);
        frame.setVisible(true);
    }
}
