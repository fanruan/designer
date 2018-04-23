package com.fr.design.mainframe.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.lang.reflect.Array;

public class ArrayTransferable implements Transferable {
    private DataFlavor serialArrayListFlavor;
    private String[][] array;

    public ArrayTransferable(String[][] array) {
    	this.array = array;
        
        serialArrayListFlavor = new DataFlavor(Array.class, "Array");
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return array;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{serialArrayListFlavor};
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return serialArrayListFlavor.equals(flavor);
    }
}