package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.BaseUtils;
import com.fr.base.background.ColorBackground;
import com.fr.design.chartx.component.button.ColorButton;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.VerticalFlowLayout;
import com.fr.design.style.AbstractSelectBox;
import com.fr.design.style.color.ColorSelectPane;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * 配色方案选择组合色之后，可以调整颜色的组件
 *
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2020-03-25
 */
public class ChartColorAdjustPane extends JPanel implements UIObserver {

    public static final Color[] DEFAULT_COLORS = {
            new Color(99, 178, 238),
            new Color(118, 218, 145),
            new Color(248, 203, 127),
            new Color(248, 149, 136),
            new Color(124, 214, 207),
            new Color(145, 146, 171),
            new Color(120, 152, 225),
            new Color(239, 166, 102),
            new Color(237, 221, 134),
            new Color(153, 135, 206),
    };

    private static final int COUNT_OF_ROW = 8;

    private static final int MAX_BUTTON = 40;

    private List<ColorButton> colorButtons = new ArrayList<>();

    private List<UIObserverListener> uiObserverListener;

    private ChangeListener changeListener;


    public ChartColorAdjustPane() {
        this(DEFAULT_COLORS);
    }

    public ChartColorAdjustPane(Color[] colors) {
        iniListener();
        createColorButton(colors);
        createContentPane();
    }

    public void updateColor(Color[] colors) {
        createColorButton(colors);
        relayout();
    }

    public Color[] getColors() {
        int size = colorButtons.size();
        Color[] colors = new Color[size];
        for (int i = 0; i < size; i++) {
            colors[i] = colorButtons.get(i).getSelectObject();
        }
        return colors;
    }

    private void relayout() {
        this.removeAll();
        createContentPane();
        this.validate();
        this.repaint();
    }

    private void createContentPane() {
        VerticalFlowLayout layout = new VerticalFlowLayout(0, 0, 0);
        layout.setAlignLeft(true);
        this.setLayout(layout);

        for (int i = 0, size = colorButtons.size(); i < size; i += COUNT_OF_ROW) {
            JPanel panel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
            int count = i + COUNT_OF_ROW > colorButtons.size() ? colorButtons.size() : i + COUNT_OF_ROW;
            for (int j = i; j < count; j++) {
                colorButtons.get(j).setLastButton(false);
                panel.add(colorButtons.get(j));
            }
            if (i + COUNT_OF_ROW > colorButtons.size()) {
                panel.add(new AddColorButton());
                this.add(panel);
            } else if (i + COUNT_OF_ROW == colorButtons.size() && colorButtons.size() != MAX_BUTTON) {
                this.add(panel);
                this.add(new AddColorButton());
            } else {
                this.add(panel);
            }
        }
        if (colorButtons.size() == 1) {
            colorButtons.get(0).setLastButton(true);
        }
    }

    private void createColorButton(Color[] colors) {
        colorButtons.clear();
        int size = Math.min(colors.length, MAX_BUTTON);
        for (int i = 0; i < size; i++) {
            colorButtons.add(createColorButton(colors[i]));
        }
    }

    private ColorButton createColorButton(Color color) {
        ColorButton colorButton = new ColorButton(color) {
            @Override
            protected void deleteButton() {
                colorButtons.remove(this);
                stateChanged();
                relayout();
            }
        };

        colorButton.addChangeListener(changeListener);
        return colorButton;
    }

    private void iniListener() {
        uiObserverListener = new ArrayList<>();
        if (shouldResponseChangeListener()) {
            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    for (UIObserverListener observerListener : uiObserverListener) {
                        observerListener.doChange();
                    }
                }
            });
        }
    }

    public void stateChanged() {
        if (changeListener != null) {
            changeListener.stateChanged(null);
        }
    }

    public void addChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener.add(listener);
    }

    public boolean shouldResponseChangeListener() {
        return true;
    }


    private class AddColorButton extends AbstractSelectBox<Color> {

        BufferedImage image = BaseUtils.readImageWithCache("/com/fr/design/images/buttonicon/add.png");

        public AddColorButton() {
            addMouseListener(getMouseListener());
        }

        @Override
        public void paint(Graphics g) {
            this.setSize(ColorButton.WIDTH, ColorButton.WIDTH);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaint(Color.WHITE);
            Rectangle2D rec = new Rectangle2D.Double(0, 0, ColorButton.WIDTH + 1, ColorButton.WIDTH + 1);
            g2d.fill(rec);
            g2d.drawImage(image, 0, 0, ColorButton.WIDTH + 1, ColorButton.WIDTH + 1, null);
        }

        public Dimension getPreferredSize() {
            return new Dimension(ColorButton.WIDTH, ColorButton.WIDTH);
        }

        protected MouseListener getMouseListener() {
            return new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showPopupMenu();
                }
            };
        }

        public JPanel initWindowPane(double preferredWidth) {
            // 下拉的时候重新生成面板，刷新最近使用颜色
            ColorSelectPane colorPane = new ColorSelectPane(false) {
                public void setVisible(boolean b) {
                    super.setVisible(b);
                }
            };
            colorPane.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    hidePopupMenu();
                    Color color = ((ColorSelectPane) e.getSource()).getColor();
                    fireDisplayComponent(ColorBackground.getInstance(color));
                    if(colorButtons.size() < MAX_BUTTON) {
                        colorButtons.add(createColorButton(color));
                    }
                    ChartColorAdjustPane.this.stateChanged();
                    relayout();
                }
            });
            return colorPane;
        }

        public Color getSelectObject() {
            return null;
        }

        public void setSelectObject(Color color) {

        }
    }
}
