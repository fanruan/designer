package com.fr.design.mainframe.backgroundpane;

import com.fr.base.GraphHelper;
import com.fr.base.background.TextureBackground;
import com.fr.design.constants.UIConstants;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.event.UIObserverListener;
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

public class TextureBackgroundQuickPane extends BackgroundQuickPane {

	private TexturePaint texturePaint;
	private TextureButton[] textureButtonArray;

	public TextureBackgroundQuickPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		JPanel borderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		borderPane.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, 5));
		JPanel contentPane = new JPanel();
		borderPane.add(contentPane, BorderLayout.NORTH);
		this.add(borderPane, BorderLayout.NORTH);
		contentPane.setLayout(new GridLayout(0, 8, 1, 1));
		contentPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		ButtonGroup patternButtonGroup = new ButtonGroup();
		textureButtonArray = new TextureButton[EMBED_TEXTURE_PAINT_ARRAY.length];
		for (int i = 0; i < EMBED_TEXTURE_PAINT_ARRAY.length; i++) {
			textureButtonArray[i] = new TextureButton(EMBED_TEXTURE_PAINT_ARRAY[i], EMBED_TEXTURE_PAINT_DES_ARRAY[i]);
			patternButtonGroup.add(textureButtonArray[i]);
			contentPane.add(textureButtonArray[i]);
		}
	}

	public void populateBean(Background background) {
		TextureBackground textureBackground = (TextureBackground) background;

		this.texturePaint = textureBackground.getTexturePaint();


		for (int i = 0; i < textureButtonArray.length; i++) {
			if (ComparatorUtils.equals(textureButtonArray[i].getTexturePaint(), this.texturePaint)) {
				textureButtonArray[i].setSelected(true);
				break;
			}
		}
	}

	public Background updateBean() {
		if (this.texturePaint == null) {
			textureButtonArray[0].doClick();
		}
		return new TextureBackground(this.texturePaint);
	}

	/**
	 * 给组件登记一个观察者监听事件
	 *
	 * @param listener 观察者监听事件
	 */
	@Override
	public void registerChangeListener(final UIObserverListener listener) {
		for (int i = 0, count = textureButtonArray.length; i < count; i++) {
			textureButtonArray[i].addChangeListener(new ChangeListenerImpl(listener));
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
			GraphHelper.fill(g2d, new Rectangle2D.Double(0, 0, (double) d.width - 1, (double)d.height - 1));

			if (ComparatorUtils.equals(texturePaint, this.buttonTexturePaint)) {// it's
				// selected.
				g2d.setPaint(UIConstants.LINE_COLOR);
			} else {
				g2d.setPaint(null);
			}
			GraphHelper.draw(g2d, new Rectangle2D.Double(0, 0, (double)d.width - 1, (double) d.height - 1));
		}

		public Dimension getPreferredSize() {
			return new Dimension(super.getPreferredSize().width, 20);
		}

		public TexturePaint getTexturePaint() {
			return this.buttonTexturePaint;
		}

		/**
		 * set Pattern setIndex.
		 */
		public void actionPerformed(ActionEvent evt) {
			TextureBackgroundQuickPane.this.texturePaint = this.getTexturePaint();

			fireChagneListener();
			TextureBackgroundQuickPane.this.repaint(); // repaint.
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

	public static final TexturePaint[] EMBED_TEXTURE_PAINT_ARRAY = new TexturePaint[]{TextureBackground.NEWSPRINT_TEXTURE_PAINT, TextureBackground.RECYCLED_PAPER_TEXTURE_PAINT,
			TextureBackground.PARCHMENT_TEXTURE_PAINT, TextureBackground.STATIONERY_TEXTURE_PAINT, TextureBackground.GREEN_MARBLE_TEXTURE_PAINT,
			TextureBackground.WHITE_MARBLE_TEXTURE_PAINT, TextureBackground.BROWN_MARBLE_TEXTURE_PAINT, TextureBackground.GRANITE_TEXTURE_PAINT,
			TextureBackground.BLUE_TISSUE_PAPER_TEXTURE_PAINT, TextureBackground.PINK_TISSUE_PAPER_TEXTURE_PAINT, TextureBackground.PURPLE_MESH_TEXTURE_PAINT,
			TextureBackground.BOUQUET_TEXTURE_PAINT, TextureBackground.PAPYRUS_TEXTURE_PAINT, TextureBackground.CANVAS_TEXTURE_PAINT, TextureBackground.DENIM_TEXTURE_PAINT,
			TextureBackground.WOVEN_MAT_TEXTURE_PAINT, TextureBackground.WATER_DROPLETS_TEXTURE_PAINT, TextureBackground.PAPER_BAG_TEXTURE_PAINT, TextureBackground.FISH_FOSSIL_TEXTURE_PAINT,
			TextureBackground.SAND_TEXTURE_PAINT, TextureBackground.CORK_TEXTURE_PAINT, TextureBackground.WALNUT_TEXTURE_PAINT, TextureBackground.OAK_TEXTURE_PAINT,
			TextureBackground.MEDIUM_WOOD_TEXTURE_PAINT};
	private static final String[] EMBED_TEXTURE_PAINT_DES_ARRAY = new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Newsprint"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Recycled_Paper"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Parchment"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Stationery"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Green_Marble"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_White_Marble"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Brown_Marble"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Granite"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Blue_Tissue_Paper"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Pink_Tissue_Paper"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Purple_Mesh"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Bouquet"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Papyrus"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Canvas"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Denim"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Woven_Mat"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Water_Droplets"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_PaperBag"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_FishFossil"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Sand"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Cork"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Walnut"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Oak"),
			com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture_Medium_Wood")};

	@Override
	public boolean accept(Background background) {
		return background instanceof TextureBackground;
	}

	@Override
	public String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture");
	}

	@Override
	public void reset() {
		this.texturePaint = null;
		textureButtonArray[0].setSelected(true);
	}
}