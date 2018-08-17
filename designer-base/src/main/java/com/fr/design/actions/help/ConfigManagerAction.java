package com.fr.design.actions.help;

import com.fr.config.ConfigContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicPane;
import com.fr.workspace.WorkContext;

import java.awt.event.ActionEvent;

public class ConfigManagerAction extends UpdateAction  {


    public ConfigManagerAction() {
        setName("ConfigManager");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static class  ConfigManagerPane extends BasicPane {

        public ConfigManagerPane() {

        }

        @Override
        protected String title4PopupWindow() {
            return "ConfigManagerPane";
        }
    }
}
