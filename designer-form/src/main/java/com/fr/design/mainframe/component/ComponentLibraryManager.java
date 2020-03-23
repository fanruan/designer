package com.fr.design.mainframe.component;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.ComponentLibraryPaneProcessor;
import com.fr.design.mainframe.component.pane.ComponentLibraryPaneProcessorImpl;

/**
 * created by Harrison on 2020/03/16
 **/
public class ComponentLibraryManager {
    
    private static ComponentLibraryPaneProcessor DEFAULT = new ComponentLibraryPaneProcessorImpl();
    
    public static ComponentLibraryPaneProcessor selectPaneProcessor() {
    
        ComponentLibraryPaneProcessor right = ExtraDesignClassManager.getInstance().getSingle(ComponentLibraryPaneProcessor.XML_TAG);
        
        if (right == null || DEFAULT.layerIndex() > right.layerIndex()) {
            right = DEFAULT;
        }
        return right;
    }
}
