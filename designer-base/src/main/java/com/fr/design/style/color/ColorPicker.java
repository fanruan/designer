package com.fr.design.style.color;

/**
 * Created by plough on 2016/12/22.
 */

import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.IOUtils;
import com.fr.log.FineLoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/**
 * 取色框
 */
public class ColorPicker extends JDialog implements ActionListener {
    private Container container = getContentPane();  // 主容器
    private int setCoordinateX;  // 取色框x坐标
    private int setCoordinateY;  // 取色框y坐标
    private int colorPickerSize = 190;  // 取色框尺寸
    private int scaleFactor = 16;  // 放大倍数
    private ColorPickerPanel colorPickerPanel = new ColorPickerPanel(scaleFactor);  // 取色框内容面板

    private Timer timer;  // 用于定时重绘
    private int FPS = 45;  // 重绘取色器的频率
    private int timeCycle = 1000 / FPS;  // 时钟周期

    private ColorSelectable colorSelectable;
    private Point mousePos;  // 鼠标的绝对坐标
    private Color colorToSet;  // 暂存要设置的颜色值

    private Boolean setColorRealTime;  // 实时设定颜色值


    /**
     * 构造函数，创建一个取色框窗体
     */
    public ColorPicker(ColorSelectable colorSelectable, Boolean setColorRealTime) {
        setUndecorated(true); // 去掉窗体边缘
        setResizable(false);
        Shape shape = new Ellipse2D.Double(0, 0, colorPickerSize, colorPickerSize);
        setShape(shape);
        container.add(colorPickerPanel);
        addMouseListener(new MouseFunctions());
        updateSize(colorPickerSize);
        this.colorSelectable = colorSelectable;
        this.setColorRealTime = setColorRealTime;
        start();
        this.setModal(true);
        this.setAlwaysOnTop(true);
        updateLocation();
        this.setVisible(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public void start() {
        timer = new Timer(timeCycle, this);
        timer.start();
        hideCursor();

        // 如果要求实时变化，确保先关闭弹窗，再截屏
        // 主要针对"图案"选项卡中的"前景"、"背景"
        if (this.setColorRealTime) {
            colorSelectable.setColor(Color.WHITE);  // setColor 可以关闭弹窗
            try {
                Thread.sleep(100);  // 等待弹窗关闭
            } catch (InterruptedException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
            colorPickerPanel.captureScreen();
        }
    }

    /**
     * 执行Timer要执行的部分，
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        updateLocation();
        colorToSet = colorPickerPanel.getPixelColor(mousePos);
        if (setColorRealTime && (colorSelectable.getColor() == null || !colorSelectable.getColor().equals(colorToSet))) {
            colorSelectable.setColor(colorToSet);
        }
    }

    public void updateLocation() {
        mousePos = MouseInfo.getPointerInfo().getLocation();
        updateCoordinate();
        setLocation(setCoordinateX, setCoordinateY);
        updateMousePos();
        updateCoordinate();
        colorPickerPanel.setMagnifierLocation(setCoordinateX,
                setCoordinateY);

    }

    private void updateCoordinate() {
        setCoordinateX = mousePos.x - getSize().width / 2;
        setCoordinateY = mousePos.y - getSize().height / 2;
    }

    /**
     * 兼容多屏下鼠标位置的计算
     */
    private void updateMousePos() {
        Rectangle bounds = GUICoreUtils.getRectScreen();
        mousePos.x -= bounds.x;
        mousePos.y -= bounds.y;
        if (mousePos.x < 0) {
            mousePos.x *= -1;
        }
        if (mousePos.y < 0) {
            mousePos.y *= -1;
        }
    }

    /**
     * 更新窗体
     *
     * @param colorPickerSize 取色框尺寸
     */
    public void updateSize(int colorPickerSize) {
        colorPickerPanel.setColorPickerSize(colorPickerSize);
        setSize(colorPickerSize, colorPickerSize);
        validate();    // 更新所有子控件
    }

    public void pickComplete(boolean setColor) {
        timer.stop();
        if (setColor) {
            colorSelectable.setColor(colorToSet);
        }
        this.dispose();
    }

    // 隐藏鼠标光标
    public void hideCursor() {
        Image imageCursor = Toolkit.getDefaultToolkit().getImage("");
        Cursor cu = Toolkit.getDefaultToolkit().createCustomCursor(imageCursor, new Point(0, 0), "cursor");
        setCursor(cu);
    }

    private class MouseFunctions extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            pickComplete(e.getButton() == MouseEvent.BUTTON1);  // 左键确定
        }
    }
}

class ColorPickerPanel extends JPanel {
    private BufferedImage screenImage;
    private Image colorPickerFrame;  // 取色框的边框图案
    private int colorPickerSize;  // 取色框尺寸
    private int locationX;  // 取色框 x 坐标
    private int locationY;  // 取色框 y 坐标
    private int scaleFactor;  // 放大倍数
    private Robot robot;

    // getPixelColor 常数
    private static int SHIFT_STEP = 8;  // 比特位右移步长
    private static int AND_R = 0xff0000;
    private static int AND_G = 0xff00;
    private static int AND_B = 0xff;

    /**
     * 带参数的构造函数
     *
     * @param scaleFactor 放大倍数
     */
    public ColorPickerPanel(int scaleFactor) {
        colorPickerFrame = IOUtils.readImage("/com/fr/design/images/gui/colorPicker/colorPickerFrame.png");
        this.scaleFactor = scaleFactor;
        captureScreen();
    }

    /**
     * 截屏
     */
    public void captureScreen() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        // 截屏幕
        screenImage = robot.createScreenCapture(GUICoreUtils.getRectScreen());
    }

    /**
     * 设置取色框的位置
     *
     * @param locationX x坐标
     * @param locationY y坐标
     */
    public void setMagnifierLocation(int locationX, int locationY) {
        this.locationX = locationX;
        this.locationY = locationY;
        repaint();        // 注意重画控件
    }

    public Color getPixelColor(Point mousePos) {
        int rgb = screenImage.getRGB(mousePos.x, mousePos.y);
        int R = (rgb & AND_R) >> SHIFT_STEP * 2;
        int G = (rgb & AND_G) >> SHIFT_STEP;
        int B = (rgb & AND_B);
        return new Color(R, G, B);
    }

    public void setColorPickerSize(int colorPickerSize) {
        this.colorPickerSize = colorPickerSize;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double pixelCount = (double) colorPickerSize / scaleFactor;  // 取色器一条边上的放大后的像素点个数（可以是小数）
        // 关键处理代码
        g2d.drawImage(
                screenImage,                 // 要画的图片
                0,                    // 目标矩形的第一个角的x坐标
                0,                    // 目标矩形的第一个角的y坐标
                colorPickerSize,                 // 目标矩形的第二个角的x坐标
                colorPickerSize,                 // 目标矩形的第二个角的y坐标
                locationX + (int) ((colorPickerSize - pixelCount) * 0.5) + 1,     // 源矩形的第一个角的x坐标
                locationY + (int) ((colorPickerSize - pixelCount) * 0.5) + 1,    // 源矩形的第一个角的y坐标
                locationX + (int) ((colorPickerSize + pixelCount) * 0.5) + 1,     // 源矩形的第二个角的x坐标
                locationY + (int) ((colorPickerSize + pixelCount) * 0.5) + 1,     // 源矩形的第二个角的y坐标
                this
        );
        g2d.drawImage(colorPickerFrame, 0, 0, this);
    }
}