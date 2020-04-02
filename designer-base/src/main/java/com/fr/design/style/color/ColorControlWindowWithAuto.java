package com.fr.design.style.color;

import com.fr.chart.base.ChartConstants;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ipoppane.PopupHider;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ColorControlWindowWithAuto extends ColorControlWindow {

    private ColorSelectionPopupPaneWithAuto selectionPopupPaneWithAuto;

    public ColorControlWindowWithAuto(PopupHider popupHider) {
        this(false, popupHider);
    }

    public ColorControlWindowWithAuto(boolean isSupportTransparent, PopupHider popupHider) {
        super(isSupportTransparent, popupHider);
    }

    public Color getColor() {
        if (selectionPopupPaneWithAuto == null) {
            return null;
        }
        return selectionPopupPaneWithAuto.getColor();
    }

    protected void initSelectionPopupPane(boolean isSupportTransparent) {
        selectionPopupPaneWithAuto = new ColorSelectionPopupPaneWithAuto(isSupportTransparent);
        this.add(selectionPopupPaneWithAuto, BorderLayout.CENTER);
    }

    class ColorSelectionPopupPaneWithAuto extends NewColorSelectPane {
        private static final long serialVersionUID = 7822856562329146354L;

        private final static int BUTTON_HEIGHT = 15;

        public ColorSelectionPopupPaneWithAuto(boolean isSupportTransparent) {
            super(isSupportTransparent);

            this.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    colorChanged();
                }
            });
        }

        protected void doTransparent() {
            getPopupHider().hidePopupMenu();
            setColor(null);
        }

        protected void doAuto() {
            getPopupHider().hidePopupMenu();
            setColor(ChartConstants.AUTO_FONT_COLOR);
        }

        public void customButtonPressed() {
            getPopupHider().hidePopupMenu();
            super.customButtonPressed();
        }

        protected void initSelectButton(boolean isSupportTransparent) {
            setSupportTransparent(isSupportTransparent);
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            this.setBorder(new UIRoundedBorder(UIConstants.TOOLBAR_BORDER_COLOR, 1, 5));

            UIButton transparentButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_ChartF_Transparency"));
            UIButton autoButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_ChartF_Auto"));

            transparentButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doTransparent();
                }
            });

            autoButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doAuto();
                }
            });

            if (isSupportTransparent) {
                double p = TableLayout.PREFERRED;
                double f = TableLayout.FILL;
                double[] columnSize = {f, 0};
                double[] rowSize = {p, p};

                Component[][] components = new Component[][]{
                        new Component[]{autoButton, null},
                        new Component[]{transparentButton, null}
                };

                JPanel buttonGroup = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
                this.add(buttonGroup, BorderLayout.NORTH);
            } else {
                this.add(autoButton, BorderLayout.NORTH);
            }
        }

        public Dimension getPreferredSize() {
            if (isSupportTransparent()) {
                return new Dimension(super.getPreferredSize().width, TRANSPARENT_WINDOW_HEIGHT + BUTTON_HEIGHT);
            }
            return new Dimension(super.getPreferredSize().width, WINDOW_HEIGHT + BUTTON_HEIGHT);
        }

    }

}