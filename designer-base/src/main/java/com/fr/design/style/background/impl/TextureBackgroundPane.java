package com.fr.design.style.background.impl;

import com.fr.base.GraphHelper;
import com.fr.base.background.TextureBackground;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

/**
 * Texture background pane.  TODO kunsnat: 拆出去. 真特么的长..
 */
public class TextureBackgroundPane extends BPane {

    private static final TexturePaint[] EMBED_TEXTURE_PAINT_ARRAY = new TexturePaint[]{
        TextureBackground.NEWSPRINT_TEXTURE_PAINT,
        TextureBackground.RECYCLED_PAPER_TEXTURE_PAINT,
        TextureBackground.PARCHMENT_TEXTURE_PAINT,
        TextureBackground.STATIONERY_TEXTURE_PAINT,
        TextureBackground.GREEN_MARBLE_TEXTURE_PAINT,
        TextureBackground.WHITE_MARBLE_TEXTURE_PAINT,
        TextureBackground.BROWN_MARBLE_TEXTURE_PAINT,
        TextureBackground.GRANITE_TEXTURE_PAINT,
        TextureBackground.BLUE_TISSUE_PAPER_TEXTURE_PAINT,
        TextureBackground.PINK_TISSUE_PAPER_TEXTURE_PAINT,
        TextureBackground.PURPLE_MESH_TEXTURE_PAINT,
        TextureBackground.BOUQUET_TEXTURE_PAINT,
        TextureBackground.PAPYRUS_TEXTURE_PAINT,
        TextureBackground.CANVAS_TEXTURE_PAINT,
        TextureBackground.DENIM_TEXTURE_PAINT,
        TextureBackground.WOVEN_MAT_TEXTURE_PAINT,
        TextureBackground.WATER_DROPLETS_TEXTURE_PAINT,
        TextureBackground.PAPER_BAG_TEXTURE_PAINT,
        TextureBackground.FISH_FOSSIL_TEXTURE_PAINT,
        TextureBackground.SAND_TEXTURE_PAINT,
        TextureBackground.CORK_TEXTURE_PAINT,
        TextureBackground.WALNUT_TEXTURE_PAINT,
        TextureBackground.OAK_TEXTURE_PAINT,
        TextureBackground.MEDIUM_WOOD_TEXTURE_PAINT};

    private static final String[] EMBED_TEXTURE_PAINT_DES_ARRAY = new String[]{
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Newsprint"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Recycled_Paper"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Parchment"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Stationery"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Green_Marble"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_White_Marble"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Brown_Marble"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Granite"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Blue_Tissue_Paper"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Pink_Tissue_Paper"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Purple_Mesh"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Bouquet"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Papyrus"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Canvas"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Denim"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Woven_Mat"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Water_Droplets"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_PaperBag"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_FishFossil"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Sand"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Cork"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Walnut"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Oak"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Medium_Wood")
    };

    private TexturePaint texturePaint;
    private TextureButton[] textureButtonArray;

    public TextureBackgroundPane() {
        super(8);// 默认的.
    }

    public TextureBackgroundPane(int colum) {
        super(colum);// 自定义的.
    }

    protected LayoutManager layoutOfTypePane(int nColumn) {
        return FRGUIPaneFactory.createNColumnGridLayout(nColumn);
    }

    protected String titleOfTypePane() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture");
    }
    protected void setChildrenOfTypePane(JPanel typePane2) {
        ButtonGroup patternButtonGroup = new ButtonGroup();
        textureButtonArray = new TextureButton[EMBED_TEXTURE_PAINT_ARRAY.length];
        for (int i = 0; i < EMBED_TEXTURE_PAINT_ARRAY.length; i++) {
            textureButtonArray[i] = new TextureButton(
                EMBED_TEXTURE_PAINT_ARRAY[i], EMBED_TEXTURE_PAINT_DES_ARRAY[i]);
            patternButtonGroup.add(textureButtonArray[i]);
            typePane2.add(textureButtonArray[i]);
        }
    }

    public void populate(Background background) {
        if (background instanceof TextureBackground) {
            TextureBackground textureBackground = (TextureBackground) background;

            this.texturePaint = textureBackground.getTexturePaint();

            for (int i = 0; i < textureButtonArray.length; i++) {
                if (ComparatorUtils.equals(textureButtonArray[i].getTexturePaint(), this.texturePaint)) {
                    textureButtonArray[i].setSelected(true);
                    break;
                }
            }
        } else {
            this.textureButtonArray[0].setSelected(true);
            this.texturePaint = textureButtonArray[0].getTexturePaint();
        }
    }

    public Background update() throws Exception {
        return new TextureBackground(this.texturePaint);
    }

    public void addChangeListener(ChangeListener changeListener) {
        for (int i = 0; i < this.textureButtonArray.length; i++) {
            this.textureButtonArray[i].addChangeListener(changeListener);
        }
    }

    /**
     * Texture type button.
     */
    class TextureButton extends JToggleButton implements ActionListener {

        private TexturePaint buttonTexturePaint;

        public TextureButton(TexturePaint buttonTexturePaint, String tooltip) {
            this.buttonTexturePaint = buttonTexturePaint;
            this.setToolTipText(tooltip);

            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.addActionListener(this);
            this.setBorder(null);
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            Dimension d = getSize();

            g2d.setPaint(this.buttonTexturePaint);
            GraphHelper.fill(g2d, new Rectangle2D.Double(0, 0, d.width - 1d,
                d.height - 1d));

            if (ComparatorUtils.equals(texturePaint, this.buttonTexturePaint)) {// it's
                // selected.
                g2d.setPaint(Color.black);
            } else {
                g2d.setPaint(Color.gray);
            }
            GraphHelper.draw(g2d, new Rectangle2D.Double(0, 0, d.width - 1d,
                d.height - 1d));
        }

        public Dimension getPreferredSize() {
            return new Dimension(36, 32);
        }

        public TexturePaint getTexturePaint() {
            return this.buttonTexturePaint;
        }

        /**
         * set Pattern setIndex.
         */
        public void actionPerformed(ActionEvent evt) {
            TextureBackgroundPane.this.texturePaint = this.getTexturePaint();

            fireChagneListener();
            TextureBackgroundPane.this.repaint(); // repaint.
        }

        public void addChangeListener(ChangeListener changeListener) {
            this.changeListener = changeListener;
        }

        private void fireChagneListener() {
            if (this.changeListener != null) {
                ChangeEvent evt = new ChangeEvent(this);
                this.changeListener.stateChanged(evt);
            }
        }
    }
}
