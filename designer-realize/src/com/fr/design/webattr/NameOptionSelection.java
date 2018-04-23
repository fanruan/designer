package com.fr.design.webattr;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;

import com.fr.design.gui.core.WidgetOption;

public class NameOptionSelection implements Transferable, Serializable {
	private static final int STRING = 0;
	private static final DataFlavor[] flavors = { DataFlavor.stringFlavor };

	private WidgetOption data;

	public NameOptionSelection(WidgetOption data) {
		this.data = data;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return flavors.clone();
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (int i = 0; i < flavors.length; i++) {
			if (flavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {

		if (flavor.equals(flavors[STRING])) {
			return data;
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}
}