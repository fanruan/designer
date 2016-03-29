package com.fr.design.mainframe.widget.renderer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import com.fr.base.FRContext;
import com.fr.base.Icon;
import com.fr.base.IconManager;
import com.fr.form.ui.WidgetManager;

public class IconCellRenderer extends GenericCellRenderer {
	private Image img;

	@Override
	public void setValue(Object v) {
		try {
			Icon icon = WidgetManager.getProviderInstance().getIconManager().getIcon(v);
			this.setImage(icon == null ? null : icon.getImage());
		} catch (CloneNotSupportedException e) {
			this.setImage(null);
			FRContext.getLogger().error(e.getMessage(), e);
		}
	}

	private void setImage(Image image) {
		this.img = image;
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		g.setColor(getBackground());
		g.fillRect(0, 0, width, height);
		
		Graphics2D g2d = (Graphics2D) g;
		if (img != null) {
			g2d.drawImage(img, 4,
					(this.getHeight() - IconManager.DEFAULT_ICONHEIGHT) / 2, IconManager.DEFAULT_ICONWIDTH,
					IconManager.DEFAULT_ICONHEIGHT, null);
		}
	}

}