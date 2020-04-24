package com.fr.design.mainframe;

import java.awt.event.ActionListener;
import java.util.List;

/**
 * created by Harrison on 2020/03/23
 **/
public class PropertyItemBean {
    
    private String name;
    private String title;
    private String btnIconName;
    private String btnIconBaseDir;
    private EastRegionContainerPane.PropertyMode[] visibleModes;
    private EastRegionContainerPane.PropertyMode[] enableModes;
    private List<ActionListener> buttonListeners;
    
    public PropertyItemBean() {
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getBtnIconName() {
        return btnIconName;
    }
    
    public void setBtnIconName(String btnIconName) {
        this.btnIconName = btnIconName;
    }
    
    public String getBtnIconBaseDir() {
        
        return btnIconBaseDir;
    }
    
    public void setBtnIconBaseDir(String btnIconBaseDir) {
        this.btnIconBaseDir = btnIconBaseDir;
    }
    
    public EastRegionContainerPane.PropertyMode[] getVisibleModes() {
        return visibleModes;
    }
    
    public void setVisibleModes(EastRegionContainerPane.PropertyMode[] visibleModes) {
        this.visibleModes = visibleModes;
    }
    
    public EastRegionContainerPane.PropertyMode[] getEnableModes() {
        return enableModes;
    }
    
    public void setEnableModes(EastRegionContainerPane.PropertyMode[] enableModes) {
        this.enableModes = enableModes;
    }
    
    public List<ActionListener> getButtonListeners() {
        return buttonListeners;
    }
    
    public void setButtonListeners(List<ActionListener> buttonListeners) {
        this.buttonListeners = buttonListeners;
    }
}
