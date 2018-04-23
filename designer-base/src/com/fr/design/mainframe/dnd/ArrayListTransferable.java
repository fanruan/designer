package com.fr.design.mainframe.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.List;

import com.fr.base.FRContext;

public class ArrayListTransferable implements Transferable {
    private DataFlavor localArrayListFlavor;
    private DataFlavor serialArrayListFlavor;
    private String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType + 
    					";class=java.util.ArrayList";
    private List data;

    public ArrayListTransferable(List alist) {
        data = alist;

        try {
            localArrayListFlavor = new DataFlavor(localArrayListType);
        } catch (ClassNotFoundException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        
        serialArrayListFlavor = new DataFlavor(ArrayList.class, "ArrayList");
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return data;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{localArrayListFlavor, serialArrayListFlavor};
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (localArrayListFlavor.equals(flavor)) {
            return true;
        }

        return serialArrayListFlavor.equals(flavor);
    }
}