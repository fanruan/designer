/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.xcombox;

import com.fr.base.FRContext;
import com.fr.base.GraphHelper;
import com.fr.base.ScreenResolution;
import com.fr.base.background.ColorBackground;
import com.fr.chart.chartglyph.Marker;
import com.fr.chart.chartglyph.NullMarker;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.general.FRFont;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Combobox for selecting marker.
 */
public class MarkerComboBox extends UIComboBox {
	/**
	 * Constructor.
	 *
	 * @param markerArray the array of marker.
	 */
	public MarkerComboBox(Marker[] markerArray) {
		this.setModel(new DefaultComboBoxModel(markerArray));
		this.setRenderer(new MarkerCellRenderer());
	}

	/**
	 * Get selected marker.
	 */
	public Marker getSelectedMarkder() {
		return (Marker) getSelectedItem();
	}

	/**
	 * Set the selected marker.
	 */
	public void setSelectedMarker(Marker marker) {
		setSelectedItem(marker);
	}

	/**
	 * CellRenderer.
	 */
	class MarkerCellRenderer extends UIComboBoxRenderer {
		public Component getListCellRendererComponent(JList list,
													  Object value, int index, boolean isSelected, boolean cellHasFocus) {
			this.marker = (Marker) value;
			this.isSelected = isSelected;

			return this;
		}

		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;

			Dimension d = getSize();
			g2d.setColor(Color.black);
			g2d.setFont(FRContext.getDefaultValues().getFRFont());

			if (marker != null) {
				if (marker instanceof NullMarker) {
					g2d.setColor(Color.black);
					FRFont font = FRContext.getDefaultValues().getFRFont();
					int resolution = ScreenResolution.getScreenResolution();
					Font rfont = font.applyResolutionNP(resolution);
					g2d.setFont(rfont);
					FontMetrics fm = GraphHelper.getFontMetrics(rfont);

					GraphHelper.drawString(g2d, Inter.getLocText("None"), 12, (d.height - fm.getHeight()) / 2 + fm.getAscent());
				} else {
					if (marker.getBackground() == null) {
						marker.setBackground(ColorBackground.getInstance(Color.black));
					}
					marker.paint(g2d, d.width / 2, d.height / 2);
				}
			}

			if (isSelected) {
				g2d.setColor(Color.blue);
				GraphHelper.drawRect(g2d, 0, 0, d.width - 1, d.height - 1);
			}
		}

		public Dimension getPreferredSize() {
			return new Dimension(36, 16);
		}

		public Dimension getMinimumSize() {
			return getPreferredSize();
		}

		private Marker marker = null;
		private boolean isSelected = false;
	}
}