package com.fr.design.actions.community;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;

import com.fr.design.utils.BrowseUtils;
import com.fr.general.CloudCenter;

import javax.swing.*;

import java.awt.event.ActionEvent;

public class BBSAction extends UpdateAction
{
	

	public BBSAction()
	{
		 this.setMenuKeySet(BBS);
	     this.setName(getMenuKeySet().getMenuName());
	     this.setMnemonic(getMenuKeySet().getMnemonic());
	     this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/bbs/bbs.png"));
		
	}

	 /**
     * 动作
     * @param arg0 事件
     */
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
	        String url = CloudCenter.getInstance().acquireUrlByKind("bbs");
			BrowseUtils.browser(url);
	}
	  public static final MenuKeySet BBS = new MenuKeySet() {
	        @Override
	        public char getMnemonic() {
	            return 'B';
	        }

	        @Override
	        public String getMenuName() {
	            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Community_Bbs");
	        }

	        @Override
	        public KeyStroke getKeyStroke() {
	            return null;
	        }
	    };

}
