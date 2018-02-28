/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions;

import java.util.ArrayList;
import java.util.List;

import com.fr.design.menu.MenuDef;

public class ToolbarActionManager {

    private static ToolbarActionManager toolbarActionManager = null; //key objMap
    //所有的Action Map.
    private List<UpdateAction> actionList = new ArrayList<UpdateAction>();
    private List<MenuDef> menuList=new ArrayList<MenuDef>();

    private ToolbarActionManager() {
    }

    public static ToolbarActionManager createToolbarActionManager() {
        if (toolbarActionManager == null) {
            toolbarActionManager = new ToolbarActionManager();
        }
        return toolbarActionManager;
    }

    public void registerAction(UpdateAction updateAction) {
        actionList.add(updateAction);
    }
    
    public void registerAction(MenuDef menuDef) {
    	menuList.add(menuDef);
	}

    public void update() {
        int actionCount = actionList.size();
        for (int i = 0; i < actionCount; i++) {
            UpdateAction action = actionList.get(i);
            if (action != null) {
                action.update();
            }
        }
        actionCount=menuList.size();
        for(int i=0;i<actionCount;i++){
        	MenuDef menuDef=menuList.get(i);
        	if(menuDef!=null){
        		menuDef.updateMenu();
        	}
        }
    }
}