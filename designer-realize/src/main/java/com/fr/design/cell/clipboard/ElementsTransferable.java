/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.clipboard;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fr.stable.StableUtils;

public class ElementsTransferable implements Transferable, ClipboardOwner {
    public static final DataFlavor CellElementsClipDataFlavor =
            createConstant(CellElementsClip.class, "CellElementsClip");
    public static final DataFlavor FloatElementClipDataFlavor =
            createConstant(FloatElementsClip.class, "FloatElementClip");
    public static final DataFlavor StringDataFlavor =
            createConstant(String.class, "String");

    private static final DataFlavor[] flavors = {
        ElementsTransferable.CellElementsClipDataFlavor,
        ElementsTransferable.FloatElementClipDataFlavor,
        ElementsTransferable.StringDataFlavor
    };
    
    private List dataList = new ArrayList();    

    private static DataFlavor createConstant(Class rc, String prn) {
        try {
            return new DataFlavor(rc, prn);
        } catch (Exception e) {
            return null;
        }
    }    

    /**
     * Creates a <code>Transferable</code> capable.
     */
    public ElementsTransferable() {
    }

    public void addObject(Object object) {
        dataList.add(object);
    }

    public Object getFirstObject() {
        if(this.dataList.size() <= 0) {
            return null;
        }

        return this.dataList.get(0);
    }

    /**
     * Returns an array of flavors in which this <code>Transferable</code>
     * can provide the data.
     */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        // returning flavors itself would allow client code to modify
        // our core behavior
        return flavors.clone();
    }

    /**
     * Returns whether the requested flavor is supported by this
     * <code>Transferable</code>.
     */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        // JCK Test StringSelection0003: if 'flavor' is null, throw NPE
        for (int i = 0; i < flavors.length; i++) {
            if (flavor.equals(flavors[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the <code>Transferable</code>'s data in the requested
     * <code>DataFlavor</code> if possible.
     */
    @Override
    public Object getTransferData(DataFlavor flavor)
        throws UnsupportedFlavorException, IOException {
        if(flavor == null) {
            return null;
        }

        for(int i = 0; i < dataList.size(); i++) {
            Object dataObj = dataList.get(i);
            if(dataObj == null) {
                continue;
            }

            if(StableUtils.classInstanceOf(flavor.getRepresentationClass(), dataObj.getClass())) {
                return dataObj;
            }
        }

        return null;
    }
    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }

    /**
     * ElementNotStringTransderData.
     */
    public static Object getElementNotStringTransderData(Transferable transferable)
            throws UnsupportedFlavorException, IOException {
    	//marks:The flavor of string is  the last one, and the loop don't contain the last one 
        for(int i = 0; i < flavors.length - 1; i++) {
            Object obj = transferable.getTransferData(flavors[i]);
            if(obj != null) {
                return obj;
            }
        }

        return null;
    }
}