package com.fr.design.widget.ui.btn;

import com.fr.base.background.ColorBackground;
import com.fr.base.background.ImageFileBackground;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleBackgroundEditor;
import com.fr.design.style.background.BackgroundButtonPane;
import com.fr.form.ui.FreeButton;
import com.fr.general.Background;


import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonSytleDefinedPane extends BasicPane {
    protected AccessibleBackgroundEditor initBackgroundPane;
    protected AccessibleBackgroundEditor overBackgroundPane;
    protected AccessibleBackgroundEditor clickBackgroundPane;

    public ButtonSytleDefinedPane() {
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        initBackgroundPane = new AccessibleBackgroundEditor();
        overBackgroundPane = new AccessibleBackgroundEditor();
        clickBackgroundPane = new AccessibleBackgroundEditor();
        double f = TableLayout.FILL;
        final double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1},{1, 1},{1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Background_Initial")), initBackgroundPane},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Background_Over")), overBackgroundPane},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Background_Click")), clickBackgroundPane},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 7, 7);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        this.add(panel, BorderLayout.CENTER);

    }

    public void populate(FreeButton button) {
        if (button == null) {
            return;
        }
        initBackgroundPane.setValue(button.getInitialBackground());
        overBackgroundPane.setValue(button.getOverBackground());
        clickBackgroundPane.setValue(button.getClickBackground());
    }

    public FreeButton update(FreeButton button) {
        button.setCustomStyle(true);
        button.setInitialBackground((Background) initBackgroundPane.getValue());
        button.setOverBackground((Background) overBackgroundPane.getValue());
        button.setClickBackground((Background) clickBackgroundPane.getValue());

        return button;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    class BackgroundPane extends JPanel {
        private UIButton editButton;
        private BackgroundButtonPane choosePane;
        private Background background;
        private UILabel ImagePreviewPane;

        BackgroundPane(String text, String ToolTips) {
            this.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
            this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 150));

            UILabel label = new UILabel(text);
            label.setToolTipText(ToolTips);
            label.setPreferredSize(new Dimension(100, 20));
            this.add(label);

            ImagePreviewPane = new UILabel();
            ImagePreviewPane.setPreferredSize(new Dimension(100, 20));
            this.add(ImagePreviewPane);

            editButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Edit"));
            editButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    choosePane = new BackgroundButtonPane();
                    BasicDialog dlg = choosePane.showWindow(SwingUtilities
                            .getWindowAncestor(ButtonSytleDefinedPane.this));
                    dlg.addDialogActionListener(new DialogActionAdapter() {
                        @Override
                        public void doOk() {
                            populate(choosePane.update());
                        }
                    });
                    if(BackgroundPane.this.background == null){
                        BackgroundPane.this.background = new ColorBackground();
                    }
                    choosePane.populate((Background) BackgroundPane.this.background);
                    dlg.setVisible(true);
                }
            });
            this.add(editButton);
        }

        public void populate(Background background) {
            this.background = background;

            if (background instanceof ImageFileBackground && ((ImageFileBackground) background).getImage() != null) {
                ImagePreviewPane.setIcon(new ImageIcon(((ImageFileBackground) background).getImage()));
            } else if(background instanceof ColorBackground && ((ColorBackground) background).getColor() != null){
                ImagePreviewPane.setIcon(null);
                ImagePreviewPane.setOpaque(true);
                ImagePreviewPane.setBackground(((ColorBackground) background).getColor());
            }else{
                ImagePreviewPane.setIcon(null);
                ImagePreviewPane.setOpaque(false);
                ImagePreviewPane.setBackground(null);
            }
        }

        public Background update() {
            return background;
        }
    }
}