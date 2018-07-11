package com.fr.design.mainframe.dnd;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;

/**
 * 传递Serializable对象.
 */
public class SerializableTransferable implements Transferable, ClipboardOwner {
    public static final DataFlavor SerializableDataFlavor =
            new DataFlavor(Serializable.class, "Serializable");
    private static final DataFlavor[] flavors = {
            SerializableTransferable.SerializableDataFlavor,
    };

    private Serializable data;

    /**
     * Creates a <code>Transferable</code> capable.
     */
    public SerializableTransferable(Serializable data) {
        this.data = data;
    }

    /**
     * Returns an array of flavors in which this <code>Transferable</code>
     * can provide the data.
     */
    public DataFlavor[] getTransferDataFlavors() {
        return SerializableTransferable.flavors;
    }

    /**
     * Returns whether the requested flavor is supported by this
     * <code>Transferable</code>.
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        // JCK UIPopover StringSelection0003: if 'flavor' is null, throw NPE
        for (int i = 0; i < SerializableTransferable.flavors.length; i++) {
            if (flavor.equals(SerializableTransferable.flavors[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the <code>Transferable</code>'s data in the requested
     * <code>DataFlavor</code> if possible.
     */
    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        return data;
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}