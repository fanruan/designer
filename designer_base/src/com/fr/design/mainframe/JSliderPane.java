package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.islider.UISlider;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by MoMeak on 2017/7/13.
 */
public class JSliderPane extends JPanel {

    private final int KERNING = 2;
    private static JSliderPane THIS;
    private UITextField showVal;
    private UISlider slider;
    private int times;
    private int sliderValue;
    private UIButton downButton;
    private UIButton upButton;
    private int showValue;
    //拖动条处理和button、直接输入不一样
    private boolean isButtonOrIsTxt = true;


    public JSliderPane() {
        this.setLayout(new BorderLayout());
        slider = new UISlider(0,100,50);
        slider.setUI(new JSliderPaneUI(slider));
        slider.addChangeListener(listener);

        showVal = new UITextField();
        showVal.setText("100%");

        showVal.getDocument().addDocumentListener(showValDocumentListener);

        downButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/data/source/moveDown.png"));
        upButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/data/source/moveUp.png"));
        downButton.setActionCommand("less");
        upButton.setActionCommand("more");
        downButton.addActionListener(buttonActionListener);
        upButton.addActionListener(buttonActionListener);

//        double f = TableLayout.FILL;
//        double p = TableLayout.PREFERRED;
//        Component[][] components = new Component[][]{
//                new Component[]{downButton, slider, upButton, showVal},
//        };
//        double[] rowSize = {p};
//        double[] columnSize = {p,p,p,p};
//        JPanel panel =  TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        JPanel panel = new JPanel(new FlowLayout(1,1,0));

        panel.add(downButton);
        panel.add(slider);
        panel.add(upButton);
        panel.add(showVal);

//        JPanel panel = new JPanel(null);
//        panel.add(downButton);
//        panel.add(slider);
//        panel.add(upButton);
//        panel.add(showVal);
//        downButton.setBounds(0,0,16,16);
//        slider.setBounds(16+KERNING,0,160,16);
//        upButton.setBounds(176+KERNING*2,0,16,16);
//        showVal.setBounds(192+KERNING*3,0,40,16);
        this.add(panel,BorderLayout.NORTH);
        this.setBounds(0,0,260,16);
    }

    public static final JSliderPane getInstance() {
        if (THIS == null) {
            THIS = new JSliderPane();
        }
        return THIS;
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
            refreshSlider();
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
        if (showValue >100){
            slider.setValue((int)(showValue+200)/6);
        }else if (showValue <100){
            slider.setValue((int)((showValue-10)/1.8));
        }else if (showValue == 100){
            slider.setValue(50);
        }
    }

    ActionListener buttonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showValue = Integer.parseInt(showVal.getText().substring(0, showVal.getText().indexOf("%")));
            isButtonOrIsTxt = true;
            if(e.getActionCommand().equals("less")){
                int newDownVal = showValue - 10;
                if (newDownVal >= 10 ){
                    showVal.setText(newDownVal + "%");
                }else {
                    showVal.setText(10 + "%");
                }
            }
            if(e.getActionCommand().equals("more")){
                int newUpVal = showValue + 10;
                if (newUpVal <= 400 ){
                    showVal.setText(newUpVal + "%");
                }else {
                    showVal.setText(400 + "%");
                }
            }
            isButtonOrIsTxt = true;
        }
    };

    private void getTimes(int value){
        if (value == 50){
            times=100;
        }else if (value < 50){
            times = (int) Math.round(1.8*value + 10);
        }else {
            times = (int) (6*value - 200);
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
        jf.setSize(320, 80);
        jf.setVisible(true);

    }
}

class JSliderPaneUI extends BasicSliderUI {

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
            size.width = 11;
            size.height = 16;
        }
        else {
            size.width = 11;
            size.height = 16;
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
        g.fillRect(0, 1, w-6, h+1);
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
            g.drawLine(0, cy, cw+5, cy);
            g.drawLine(5+cw/2, cy-4, 5+cw/2, cy+4);
        } else {
            super.paintTrack(g);
        }
    }

}
