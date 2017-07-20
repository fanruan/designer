package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.islider.UISlider;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

/**
 * Created by MoMeak on 2017/7/13.
 */
public class JSliderPane extends JPanel {

    private static final double ONEPOINTEIGHT = 1.8;
    private static final int SIX = 6;
    private static final int TEN = 10;
    private static final int ONEEIGHT = 18;
    private static final int TWOFIVE = 25;
    private static final int FOURTEN = 40;
    private static final int HALFHUNDRED = 50;
    private static final int HUNDRED = 100;
    private static final int TWOHUNDRED = 200;
    private static final int THREEHUNDRED = 300;
    private static final int FOURHUNDRED = 400;
    private static final int DIALOGWIDTH = 150;
    private static final int DIALOGHEIGHT = 200;
    public int showValue = 100;
    public double resolutionTimes = 1.0;
    private static JSliderPane THIS;
    private UITextField showVal;
    private UISlider slider;
    private int times;
    private int sliderValue;
    private UIButton downButton;
    private UIButton upButton;
    private UIButton showValButton;
    private UIRadioButton twoHundredButton;
    private UIRadioButton oneHundredButton;
    private UIRadioButton SevenFiveButton;
    private UIRadioButton fiveTenButton;
    private UIRadioButton twoFiveButton;
    private UIRadioButton selfAdaptButton;
    private UIRadioButton customButton;
    //拖动条处理和button、直接输入不一样
    private boolean isButtonOrIsTxt = true;
    private PopupPane dialog;
    private int upButtonX;


    public JSliderPane() {
        this.setLayout(new BorderLayout());
        slider = new UISlider(0,HUNDRED,HALFHUNDRED);
        slider.setUI(new JSliderPaneUI(slider));
        slider.addChangeListener(listener);

        showVal = new UITextField();
        showVal.setText("100%");
        showVal.setPreferredSize(new Dimension(FOURTEN,ONEEIGHT));
        showVal.getDocument().addDocumentListener(showValDocumentListener);

        downButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/data/source/moveDown.png"));
        upButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/data/source/moveUp.png"));
        downButton.setActionCommand("less");
        upButton.setActionCommand("more");
        downButton.addActionListener(buttonActionListener);
        upButton.addActionListener(buttonActionListener);

        JPanel panel = new JPanel(new FlowLayout(1,1,0));

        showValButton = new UIButton(showVal.getText());
        showValButton.setBorderPainted(false);
        showValButton.setPreferredSize(new Dimension(HALFHUNDRED,TWOFIVE));

        showValButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupDialog();
            }
        });
        panel.add(downButton);
        panel.add(slider);
        panel.add(upButton);
        panel.add(showValButton);
        this.add(panel,BorderLayout.NORTH);
        this.setBounds(0,0,THREEHUNDRED,ONEEIGHT);

    }

    public static final JSliderPane getInstance() {
//        if (THIS == null) {
//            THIS = new JSliderPane();
//        }
        THIS = new JSliderPane();
        return THIS;
    }

    private void initUIRadioButton(){
        twoHundredButton = new UIRadioButton("200%");
        oneHundredButton = new UIRadioButton("100%");
        SevenFiveButton = new UIRadioButton("75%");
        fiveTenButton = new UIRadioButton("50%");
        twoFiveButton = new UIRadioButton("25%");
        selfAdaptButton = new UIRadioButton(Inter.getLocText("FR-Designer_Scale_selfAdaptButton"));
        customButton = new UIRadioButton(Inter.getLocText("FR-Designer_Scale_customButton"));

        ButtonGroup bg=new ButtonGroup();// 初始化按钮组
        bg.add(twoHundredButton);// 加入按钮组
        bg.add(oneHundredButton);
        bg.add(SevenFiveButton);
        bg.add(fiveTenButton);
        bg.add(twoFiveButton);
        bg.add(selfAdaptButton);
        bg.add(customButton);
    }


    //定义一个监听器，用于监听所有滑动条
    ChangeListener listener = new ChangeListener()
    {
        public void stateChanged( ChangeEvent event) {
            //取出滑动条的值，并在文本中显示出来
            if (!isButtonOrIsTxt){
                JSlider source = (JSlider) event.getSource();
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        sliderValue = slider.getValue();
                        getTimes(sliderValue);
                        showValue = times;
                        showVal.setText(times + "%");
                    }
                });
            }else {
                isButtonOrIsTxt = false;
            }
        }
    };

    DocumentListener showValDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            isButtonOrIsTxt = true;
            resolutionTimes = divide(showValue,100,2);
            refreshSlider();
            refreshBottun();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
//            refreshSlider();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
//            refreshSlider();
        }
    };

    private void refreshSlider(){
        showValue = Integer.parseInt(showVal.getText().substring(0, showVal.getText().indexOf("%")));
        if (showValue >HUNDRED){
            slider.setValue((int)(showValue+TWOHUNDRED)/SIX);
        }else if (showValue <HUNDRED){
            slider.setValue((int)((showValue-TEN)/ONEPOINTEIGHT));
        }else if (showValue == HUNDRED){
            slider.setValue(HALFHUNDRED);
        }
    }


    private void refreshBottun(){
        showValButton.setText(showVal.getText());
    }

    public double getResolutionTimes(){
        return this.resolutionTimes;
    }

    public int getshowValue(){
        return this.showValue;
    }

    public static double divide(double v1, double v2,int scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale).doubleValue();
    }

    ActionListener buttonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showValue = Integer.parseInt(showVal.getText().substring(0, showVal.getText().indexOf("%")));
            isButtonOrIsTxt = true;
            if(e.getActionCommand().equals("less")){
                int newDownVal = showValue - TEN;
                if (newDownVal >= TEN ){
                    showValue = newDownVal;
                    showVal.setText(newDownVal + "%");
                }else {
                    showValue = newDownVal;
                    showVal.setText(TEN + "%");
                }
            }
            if(e.getActionCommand().equals("more")){
                int newUpVal = showValue + TEN;
                if (newUpVal <= FOURHUNDRED ){
                    showValue = newUpVal;
                    showVal.setText(newUpVal + "%");
                }else {
                    showValue = newUpVal;
                    showVal.setText(FOURHUNDRED + "%");
                }
            }
            isButtonOrIsTxt = true;
        }
    };



    private void getTimes(int value){
        if (value == HALFHUNDRED){
            times=HUNDRED;
        }else if (value < HALFHUNDRED){
            times = (int) Math.round(ONEPOINTEIGHT*value + TEN);
        }else {
            times = (int) (SIX*value - TWOHUNDRED);
        }
    }


    public UITextField getShowVal(){
        return this.showVal;
    }

    private void popupDialog(){
        Point btnCoords = upButton.getLocationOnScreen();
        if (dialog == null){
            dialog = new PopupPane(upButton,showVal);
            if (upButtonX == 0) {
                upButtonX = btnCoords.x;
                GUICoreUtils.showPopupMenu(dialog, upButton,  - DIALOGWIDTH + upButton.getWidth() +HALFHUNDRED, -DIALOGHEIGHT);
            }
        }else {
            if (upButtonX == 0) {
                upButtonX = btnCoords.x;
                GUICoreUtils.showPopupMenu(dialog, upButton,  - DIALOGWIDTH + upButton.getWidth() +HALFHUNDRED, -DIALOGHEIGHT);
            } else {
                GUICoreUtils.showPopupMenu(dialog, upButton,  - DIALOGWIDTH + upButton.getWidth() +HALFHUNDRED, -DIALOGHEIGHT);
            }
        }
    }

    public static void main(String[] args)
    {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel)jf.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(JSliderPane.getInstance(),BorderLayout.CENTER);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(400, 80);
        jf.setVisible(true);

    }
}

class JSliderPaneUI extends BasicSliderUI {

    private static final int VERTICAL_WIDTH = 11;
    private static final int VERTICAL_HEIGHT = 16;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;

    public JSliderPaneUI(UISlider b) {
        super(b);
    }

    /** */
    /**
     * 绘制指示物
     */

    public Dimension getThumbSize() {
        Dimension size = new Dimension();

        if ( slider.getOrientation() == JSlider.VERTICAL ) {
            size.width = VERTICAL_WIDTH;
            size.height = VERTICAL_HEIGHT;
        }
        else {
            size.width = VERTICAL_WIDTH;
            size.height = VERTICAL_HEIGHT;
        }

        return size;
    }

    public void paintThumb(Graphics g) {
        Rectangle knobBounds = thumbRect;
        int w = knobBounds.width;
        int h = knobBounds.height;

        g.translate(knobBounds.x, knobBounds.y);
        if ( slider.isEnabled() ) {
            g.setColor(slider.getBackground());
        }
        else {
            g.setColor(slider.getBackground().darker());
        }
        g.setColor(Color.darkGray);
        g.fillRect(0, 1, w-SIX, h+1);
    }

    /** */
    /**
     * 绘制刻度轨迹
     */
    public void paintTrack(Graphics g) {
        int cy, cw;
        Rectangle trackBounds = trackRect;
        if (slider.getOrientation() == UISlider.HORIZONTAL) {
            Graphics2D g2 = (Graphics2D) g;
            cy = (trackBounds.height / 2);
            cw = trackBounds.width;
            g.setColor(Color.lightGray);
            g.drawLine(0, cy, cw+FIVE, cy);
            g.drawLine(FIVE+cw/2, cy-FOUR, FIVE+cw/2, cy+FOUR);
        } else {
            super.paintTrack(g);
        }
    }

}
class Dialog extends JDialog {
//    private Container container;
//    private static final int UPLABELHEIGHT = 25;
//    private static final int HALFHUNDRED = 50;
//    private static final int DIALOGWIDTH = 150;
//    private static final int DIALOGHEIGHT = 200;
//    private static final int UPLABELWIDTH = 300;
//    private int minHeight;  // 对话框最小高度
//    private JComponent contentPane;
//    private JComponent centerPane;
//    private UILabel upLabel;

//    public Dialog(UIButton b,UITextField j) {
//        super(DesignerContext.getDesignerFrame());
//        container = getContentPane();
//        setUndecorated(true);
//        contentPane = new JPanel(new BorderLayout());
//        centerPane = new JPanel(new BorderLayout());
//        upLabel = new UILabel(Inter.getLocText("FR-Designer_Scale_EnlargeOrReduce"));
//        upLabel.setOpaque(true);
//        upLabel.setPreferredSize(new Dimension(UPLABELWIDTH,UPLABELHEIGHT));
//        upLabel.setBackground(Color.LIGHT_GRAY);
//        upLabel.setBorder(BorderFactory.createLineBorder(Color.gray,1));
//        upLabel.setBorder(new MatteBorder(0,0,1,0,Color.gray));
//        centerPane.add(j,BorderLayout.NORTH);
//        contentPane.add(upLabel,BorderLayout.NORTH);
//        contentPane.add(centerPane,BorderLayout.CENTER);
////        contentPane.setBorder(BorderFactory.createLineBorder(Color.gray,1));
//        contentPane.setBorder(new MatteBorder(1,1,1,1,Color.darkGray));
////        contentPane.add(new JPanel())
//        container.add(contentPane, BorderLayout.CENTER);
//        minHeight = container.getPreferredSize().height;
//        setSize(DIALOGWIDTH, DIALOGHEIGHT);
////            validate();
//        Point btnCoords = b.getLocationOnScreen();
//
//        this.setLocation(btnCoords.x - DIALOGWIDTH + b.getWidth() +HALFHUNDRED, btnCoords.y -DIALOGHEIGHT);
////            initListener();
//
//        this.setVisible(true);
//    }
}
class PopupPane extends JPopupMenu {
    private JComponent contentPane;
    private static final int UPLABELHEIGHT = 25;
    private static final int HALFHUNDRED = 50;
    private static final int DIALOGWIDTH = 150;
    private static final int DIALOGHEIGHT = 200;
    private static final int UPLABELWIDTH = 300;
    private JComponent centerPane;
    private UILabel upLabel;
    PopupPane(UIButton b,UITextField j) {
        contentPane = new JPanel(new BorderLayout());
        centerPane = new JPanel(new BorderLayout());
        upLabel = new UILabel(Inter.getLocText("FR-Designer_Scale_EnlargeOrReduce"));
        upLabel.setOpaque(true);
        upLabel.setPreferredSize(new Dimension(UPLABELWIDTH,UPLABELHEIGHT));
        upLabel.setBackground(Color.LIGHT_GRAY);
        upLabel.setBorder(new MatteBorder(0,0,1,0,Color.gray));
        centerPane.add(j,BorderLayout.NORTH);
        contentPane.add(upLabel,BorderLayout.NORTH);
        contentPane.add(centerPane,BorderLayout.CENTER);
//        contentPane.setBorder(new MatteBorder(1,1,1,1,Color.darkGray));
        this.add(contentPane, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(DIALOGWIDTH, DIALOGHEIGHT));
        this.setOpaque(false);
    }



}